package speiger.src.tinymodularthings.common.blocks.machine;

import java.math.RoundingMode;
import java.util.*;

import mods.railcraft.common.items.firestone.ItemFirestoneRefined;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.*;
import speiger.src.api.common.registry.recipes.pressureFurnace.IPressureFurnaceRecipe;
import speiger.src.api.common.registry.recipes.pressureFurnace.PressureRecipeList;
import speiger.src.api.common.utils.FluidUtils;
import speiger.src.api.common.utils.InventoryUtil;
import speiger.src.api.common.world.blocks.BlockPosition;
import speiger.src.api.common.world.tiles.interfaces.IAcceptor;
import speiger.src.api.common.world.tiles.interfaces.IAcceptor.AcceptorType;
import speiger.src.api.common.world.tiles.interfaces.InterfaceAcceptor;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.common.blocks.utils.ExpStorage;
import speiger.src.spmodapi.common.templates.ITemplate;
import speiger.src.spmodapi.common.templates.ITemplateProvider;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.tile.FacedInventory;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.plugins.BC.triggers.TriggerFuel;
import speiger.src.tinymodularthings.common.plugins.BC.triggers.TriggerHasWork;
import speiger.src.tinymodularthings.common.templates.TemplatePressureFurnace;
import buildcraft.BuildCraftCore;
import buildcraft.api.gates.IAction;
import buildcraft.api.gates.IActionReceptor;
import buildcraft.api.gates.IOverrideDefaultTriggers;
import buildcraft.api.gates.ITrigger;

import com.google.common.math.DoubleMath;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PressureFurnace extends FacedInventory implements IFluidHandler, InterfaceAcceptor, IOverrideDefaultTriggers, IActionReceptor, ITemplateProvider
{
	public boolean update = true;
	public boolean valid = false;
	public boolean firstTime = true;
	public final int MaxHeat = 1200;
	public final int MaxProgress = 200;
	public int heat = 0;
	public int fuel = 0;
	public int maxFuel = 0;
	public int progress = 0;
	public IPressureFurnaceRecipe currentRecipe = null;
	public IAcceptor fluidInterface = null;
	public IAcceptor[] itemInterfaces = new IAcceptor[3];
	public boolean paused = false;
	public int matches = 0;
	public double storedExp = 0;
	ITemplate temp = null;
	public static Class<? extends GuiInventoryCore> guiClass = null;
	public static HashSet<Integer> validFuels = new HashSet<Integer>();
	public static HashMap<Integer, Integer> fuelMeta = new HashMap<Integer, Integer>();
	public static HashMap<Integer, Integer> bonusHeat = new HashMap<Integer, Integer>();
	
	public PressureFurnace()
	{
		super(5);
	}
	
	public boolean hasFuel()
	{
		int totalFuel = fuel;
		if (inv[0] != null)
		{
			totalFuel += (TileEntityFurnace.getItemBurnTime(inv[0]) * 4) * inv[0].stackSize;
		}
		if (totalFuel < 100000)
		{
			return true;
		}
		return false;
		
	}
	
	@Override
	public boolean canMergeItem(ItemStack par1, int slotID)
	{
		if(slotID == 0)
		{
			int i = par1.itemID;
			if(!validFuels.contains(i))
			{
				return false;
			}
		}
		if(slotID == 4)
		{
			return false;
		}
		return true;
	}
	
	@Override
	public void addContainerSlots(AdvContainer par1)
	{
		par1.addSpmodSlot(this, 0, 13, 53).addUsage("Fuel Slot");
		par1.addSpmodSlot(this, 1, 58, 37).addUsage("First Input Slot");
		par1.addSpmodSlot(this, 2, 58, 58).addUsage("Second Input Slot");
		par1.addSpmodSlot(this, 3, 101, 20).addUsage("Combiner Slot");
		par1.addSpmodSlot(this, 4, 150, 48).addUsage("Output Slot");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getInvNameYOffset()
	{
		return 2;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawFrontExtras(GuiInventoryCore par1, int guiX, int guiY, int mouseX, int mouseY)
	{
		par1.getFontRenderer().drawString("Heat:", 32, 50, 4210752);
		int pro = (int)(((double)heat / (double)MaxHeat) * 100);
		par1.getFontRenderer().drawString(pro+"%", 32, 60, 4210752);
		
		if(heat < MaxHeat)
		{
			par1.getFontRenderer().drawString("Heating up", 80, 66, 4210752);
		}
		else
		{
			if(currentRecipe == null)
			{
				par1.getFontRenderer().drawString("Need Recipe", 80, 66, 4210752);
			}
			else
			{
				int max = Math.max(100, currentRecipe.getRequiredCookTime());
				int prog = (int)(((double)progress / (double)max) * 100);
				par1.getFontRenderer().drawString("Progress: "+prog+"%", 80, 68, 4210752);
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void drawExtras(GuiInventoryCore par1, int guiX, int guiY, int mouseX, int mouseY)
	{
		par1.setTexture(par1.engine.getTexture("Objects"));
		par1.defineSlot("Fire");
		par1.drawSlotPros(13, 35, 17, 15);
		int amount = (int)Math.min(15D, (((double)fuel / (double)maxFuel) * 15));
		if(amount > 0)
		{
			par1.defineSlot("FireOverlay");
			par1.drawSlotPros(13, 36 + 15 - amount, 0, 15-amount, 17, amount);
		}
		
		if(currentRecipe == null)
		{
			par1.defineSlot("ProgBarH", 2, 0);
			par1.drawSlotPros(80, 49, 12, 16);
			par1.drawSlotPros(92, 49, 12, 16);
			par1.drawSlotPros(104, 49, 12, 16);
			par1.drawSlotPros(116, 49, 12, 16);
			par1.drawSlotPros(122, 49, 20, 16);
			par1.defineSlot("ProgBarHV", 0, 1);
			par1.drawSlotPros(101, 43, 20, 13);
		}
		else
		{
			par1.defineSlot("ProgBarHOverlay", 2, 0);
			par1.drawSlotPros(80, 49, 12, 16);
			par1.drawSlotPros(92, 49, 12, 16);
			par1.drawSlotPros(104, 49, 12, 16);
			par1.drawSlotPros(116, 49, 12, 16);
			par1.drawSlotPros(122, 49, 20, 16);
			if(currentRecipe.getCombiner() != null)
			{
				par1.defineSlot("ProgBarHVOverlay", 0, 1);
				par1.drawSlotPros(101, 42, 20, 13);
			}
			else
			{
				par1.defineSlot("ProgBarHV", 0, 1);
				par1.drawSlotPros(101, 42, 20, 13);
			}
		}	
	}
	
	

	@Override
	@SideOnly(Side.CLIENT)
	public GuiInventoryCore getGui(InventoryPlayer par1)
	{
		if(guiClass != null)
		{
			try
			{
				return guiClass.getConstructor(InventoryPlayer.class, AdvTile.class).newInstance(par1, this).setAutoDrawing();
			}
			catch(Exception e)
			{
			}
			
		}
		return super.getGui(par1);
	}

	@Override
	public void onTick()
	{
		super.onTick();
		if (!worldObj.isRemote)
		{
			handleTemplate();
			if (valid)
			{
				handleFuel();
				handleExp();
				doWork();
			}
			
			if (update)
			{
				onInventoryChanged();
				PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 30, worldObj.provider.dimensionId, getDescriptionPacket());
				updateBlock();
				update = false;
			}
		}
		else
		{
			if(valid)
			{
				doWork();
			}
		}
		

	}
	
	public void handleExp()
	{
		if(worldObj.getWorldTime() % 40 != 0 || fluidInterface == null || storedExp < 1.0D)
		{
			return;
		}
		int exp = DoubleMath.roundToInt(storedExp, RoundingMode.DOWN);
		
		if(fluidInterface.isTilePressent(ExpStorage.class))
		{
			ExpStorage storage = fluidInterface.getTileEntity(ExpStorage.class);
			int added = storage.addExp(exp);
			storedExp -= added;
			return;
		}
		else if(FluidUtils.liquidExpAviable() && fluidInterface.isTilePressent(IFluidHandler.class))
		{
			IFluidHandler handler = fluidInterface.getTileEntity(IFluidHandler.class);
			ForgeDirection dir = ForgeDirection.getOrientation(fluidInterface.getSideFromTile(IFluidHandler.class)).getOpposite();
			if(FluidUtils.OpenBlocksExp())
			{
				int converting = exp * 20;
				if(handler.canFill(dir, FluidUtils.getLiquidExp()))
				{
					int added = handler.fill(dir, new FluidStack(FluidUtils.getLiquidExp(), converting), true);
					if(added > 0)
					{
						storedExp -= (added / 20);
					}
				}
			}
			else
			{
				if(handler.canFill(dir, FluidUtils.getMobEssens()))
				{
					storedExp -= handler.fill(dir, new FluidStack(FluidUtils.getMobEssens(), exp), true);
				}
			}
		}
		else
		{
			if(this.hasUsers() && this.getUserSize() == 1)
			{
				EntityPlayer player = this.worldObj.getPlayerEntityByName(this.users.get(0));
				if(player != null)
				{
					player.addExperience(exp);
					storedExp = 0;
				}
			}
		}
	}
	
	public void handleTemplate()
	{
		if ((firstTime && worldObj.getWorldTime() % 5 == 0) || (!firstTime && worldObj.getWorldTime() % 50 == 0))
		{
			boolean old = valid;
			valid = temp.match();
			if(old != valid)
			{
				update = true;
			}
			matches = temp.getTotalPatternSize();
			updateBlock();
			if (firstTime)
			{
				firstTime = false;
			}
		}
	}
	
	public void handleFuel()
	{
		if(maxFuel <= 0)
		{
			maxFuel = fuel;
		}
		if (fuel < 100 && inv[0] != null)
		{
			refuel();
		}
		
		if (fuel <= 0 && heat > 0)
		{
			if (worldObj.getWorldTime() % 5 == 0)
			{
				heat--;
			}
		}
		if(fuel > 0)
		{
			fuel--;
			if (heat < MaxHeat)
			{
				fuel -= 35;
				if (worldObj.getWorldTime() % 5 == 0)
				{
					heat += 1;
				}
			}
		}
	}
	
	public void refuel()
	{
		ItemStack fuelItem = inv[0];
		if (fuelItem != null)
		{
			int fuelID = fuelItem.itemID;
			if(!validFuels.contains(fuelID))
			{
				return;
			}
			if(fuelMeta.containsKey(fuelID))
			{
				int meta = fuelMeta.get(fuelID);
				if(meta != fuelItem.getItemDamage())
				{
					return;
				}
			}
			if(bonusHeat.containsKey(fuelID))
			{
				heat+=bonusHeat.get(fuelID);
				heat = Math.min(heat, MaxHeat);
			}
			
			int gainingFuel = TileEntityFurnace.getItemBurnTime(fuelItem) * 4;
			if (gainingFuel > 0)
			{
				fuel += gainingFuel;
				fuelItem.stackSize--;
				if (fuelItem.stackSize <= 0)
				{
					inv[0] = null;
				}
			}
			maxFuel = fuel;
		}
	}
	
	public void doWork()
	{
		if (heat >= MaxHeat)
		{
			PressureRecipeList list = PressureRecipeList.getInstance();
			if(currentRecipe != null && !currentRecipe.recipeMatches(inv[1], inv[2], inv[3], 1))
			{
				currentRecipe = null;
			}
			if(currentRecipe == null && list.hasRecipe(inv[1], inv[2], inv[3]))
			{
				currentRecipe = list.getRecipe(inv[1], inv[2], inv[3]);
			}
			if(paused)
			{
				paused = false;
				return;
			}
			
			if(currentRecipe != null)
			{
				if(canRun())
				{
					progress++;
					fuel--;
					int maxProgress = Math.max(currentRecipe.getRequiredCookTime(), 100);
					if(progress >= maxProgress)
					{
						progress = 0;
						int max = currentRecipe.isMultiRecipe() ? 5 : 1;

						for(;max > 1 && (!currentRecipe.recipeMatches(inv[1], inv[2], inv[3], max) || (inv[4] != null && inv[4].stackSize + (currentRecipe.getOutput().stackSize * max) > inv[4].getMaxStackSize()));)
						{
							max--;
						}
						currentRecipe.runRecipe(inv[1], inv[2], inv[3], max);
						ItemStack result = currentRecipe.getOutput().copy();
						result.stackSize *= max;
						storedExp += (double)(PressureRecipeList.getInstance().getExpFromResult(result) * max);
						
						for(int i = 1;i<4;i++)
						{
							if(inv[i] != null && inv[i].stackSize <= 0)
							{
								inv[i] = null;
							}
						}
						
						if(inv[4] == null)
						{
							inv[4] = result;
						}
						else
						{
							inv[4].stackSize += result.stackSize;
						}
					}
				
				}
				else
				{
					progress = 0;
				}
				
			}
			else
			{
				progress = 0;
			}
		}
	}
	
	public boolean canRun()
	{
		if(inv[4] == null)
		{
			return true;
		}
		ItemStack result = currentRecipe.getOutput();
		if(!InventoryUtil.isItemEqual(inv[4], result))
		{
			return false;
		}
		return inv[4].stackSize + result.stackSize <= inv[4].getMaxStackSize();
	}
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		TextureEngine engine = TextureEngine.getTextures();
		
		if (side == getFacing())
		{
			if(valid)
			{
				return engine.getTexture(TinyBlocks.machine, 2);
			}
			return engine.getTexture(TinyBlocks.machine, 1);
		}
		return engine.getTexture(TinyBlocks.machine, 0);
	}
	
	@Override
	public boolean hasContainer()
	{
		return valid;
	}
	
	@Override
	public float getBlockHardness()
	{
		return 6F;
	}

	@Override
	public float getExplosionResistance(Entity par1)
	{
		return 10F;
	}
	
	@Override
	public String getInvName()
	{
		return "Presure Furnace";
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer var1)
	{
		return valid;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setBoolean("Valid", valid);
		nbt.setInteger("Fuel", fuel);
		nbt.setInteger("Heat", heat);
		nbt.setInteger("Progress", progress);
		nbt.setInteger("Matches", matches);
		nbt.setBoolean("Paused", paused);
		nbt.setDouble("Exp", storedExp);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		valid = nbt.getBoolean("Valid");
		fuel = nbt.getInteger("Fuel");
		heat = nbt.getInteger("Heat");
		progress = nbt.getInteger("Progress");
		matches = nbt.getInteger("Matches");
		paused = nbt.getBoolean("Paused");
		storedExp = nbt.getDouble("Exp");
	}
	
	@Override
	public boolean isSixSidedFacing()
	{
		return false;
	}
	
	int helper = 0;
	
	@Override
	public void onReciveGuiInfo(int key, int val)
	{
		if (key == 0)
		{
			valid = val == 1 ? true : false;
		}
		if (key == 1)
		{
			helper = this.upcastShort(val);
		}
		if (key == 2)
		{
			heat = val;
		}
		if (key == 3)
		{
			progress = val;
		}
		if (key == 4)
		{
			fuel = (helper | (val << 16));
		}
		if(key == 5)
		{
			helper = this.upcastShort(val);
		}
		if(key == 6)
		{
			maxFuel = (helper | val << 16);
		}
	}
	
	@Override
	public void onSendingGuiInfo(Container par1, ICrafting par2)
	{
		par2.sendProgressBarUpdate(par1, 0, valid ? 1 : 0);
		par2.sendProgressBarUpdate(par1, 1, fuel);
		par2.sendProgressBarUpdate(par1, 2, heat);
		par2.sendProgressBarUpdate(par1, 3, progress);
		par2.sendProgressBarUpdate(par1, 4, fuel >> 16);
		par2.sendProgressBarUpdate(par1, 5, maxFuel);
		par2.sendProgressBarUpdate(par1, 6, maxFuel >> 16);
	}
	
	@Override
	public ArrayList<ItemStack> onDrop(int fortune)
	{
		ArrayList<ItemStack> drop = super.onDrop(fortune);
		for(ItemStack stack : inv)
		{
			if(stack != null)
			{
				drop.add(stack);
			}
		}
		return drop;
	}

	@Override
	public void onClientTick()
	{
		if (valid && heat > 0)
		{
			int l = getFacing();
			float f = xCoord + 0.5F;
			float f1 = yCoord + 0.0F + worldObj.rand.nextFloat() * 6.0F / 16.0F;
			float f2 = zCoord + 0.5F;
			float f3 = 0.52F;
			float f4 = worldObj.rand.nextFloat() * 0.6F - 0.3F;
			
			if (l == 4)
			{
				worldObj.spawnParticle("smoke", f - f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
				worldObj.spawnParticle("flame", f - f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
			}
			else if (l == 5)
			{
				worldObj.spawnParticle("smoke", f + f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
				worldObj.spawnParticle("flame", f + f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
			}
			else if (l == 2)
			{
				worldObj.spawnParticle("smoke", f + f4, f1, f2 - f3, 0.0D, 0.0D, 0.0D);
				worldObj.spawnParticle("flame", f + f4, f1, f2 - f3, 0.0D, 0.0D, 0.0D);
			}
			else if (l == 3)
			{
				worldObj.spawnParticle("smoke", f + f4, f1, f2 + f3, 0.0D, 0.0D, 0.0D);
				worldObj.spawnParticle("flame", f + f4, f1, f2 + f3, 0.0D, 0.0D, 0.0D);
			}
		}
	}
	
	@Override
	public boolean acceptItems(IAcceptor par1)
	{
		if (par1.getBlock() == null)
		{
			return false;
		}
		String name = par1.getBlock().getHiddenName();
		boolean ore = name.contains("Ore") || name.contains("ore");
		if ((!ore && (name.contains("cobble") || name.contains("sandStone") || name.contains("brick") || name.contains("stone"))))
		{
			return par1.getType() == AcceptorType.Items;
		}
		return false;
	}
	
	@Override
	public boolean acceptFluids(IAcceptor par1)
	{
		return fluidInterface == null;
	}
	
	@Override
	public boolean acceptEnergy(IAcceptor par1)
	{
		return false;
	}
	
	@Override
	public boolean addAcceptor(IAcceptor par1)
	{
		if(par1.getType() == AcceptorType.Fluids)
		{
			if(fluidInterface == null)
			{
				fluidInterface = par1;
				return true;
			}
		}
		else if(par1.getType() == AcceptorType.Items)
		{
			boolean flag = false;
			for(int i = 0;i<itemInterfaces.length;i++)
			{
				if(itemInterfaces[i] == null)
				{
					itemInterfaces[i] = par1;
					flag = true;
					break;
				}
			}
			return flag;
		}
		return false;
	}
	
	@Override
	public boolean removeAcceptor(IAcceptor par1)
	{
		if(par1.getType() == AcceptorType.Fluids)
		{
			if(fluidInterface != null)
			{
				fluidInterface = null;
				return true;
			}
		}
		else if(par1.getType() == AcceptorType.Items)
		{
			boolean flag = false;
			for(int i = 0;i<itemInterfaces.length;i++)
			{
				IAcceptor accept = itemInterfaces[i];
				if(accept != null && accept.getPosition().match(par1.getPosition().getAsList()))
				{
					flag = true;
					itemInterfaces[i] = null;
					break;
				}
			}
		}
		return false;
	}
	
	@Override
	public LinkedList<ITrigger> getTriggers()
	{
		LinkedList<ITrigger> trigger = new LinkedList<ITrigger>();
		trigger.add(new TriggerFuel());
		trigger.add(new TriggerHasWork(true));
		trigger.add(new TriggerHasWork(false));
		return trigger;
	}
	
	@Override
	public void actionActivated(IAction action)
	{
		try
		{
			if (action == BuildCraftCore.actionOn)
			{
				paused = false;
			}
			else if (action == BuildCraftCore.actionOff)
			{
				paused = true;
			}
		}
		catch (Exception e)
		{
			TinyModularThings.log.print("Error with BC Gate Actions");
		}
	}

	@Override
	public void loadInformation(List par1)
	{
		if(!valid)
		{
			if(itemInterfaces.length > 3)
			{
				par1.add("To Many ItemInterfaces Interfaces");
			}
			BlockPosition pos = this.getPosition().getPosFromFSide(ForgeDirection.getOrientation(facing).getOpposite());
			if(!pos.isAirBlock())
			{
				par1.add("The Core Block is not a Air Block: "+pos.getAsBlockStack().getHiddenName());
				par1.add("Real Block Name: "+pos.getAsBlockStack().getBlockDisplayName());
			}
			if(matches == 26 && !pos.isAirBlock())
			{
				
			}
			else if(matches != 27)
			{
				par1.add("Structure is not valid: out of 27 blocks only "+matches+" match");
			}
			

		}
		
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onItemInformation(EntityPlayer par1, List par2, ItemStack par3)
	{
		super.onItemInformation(par1, par2, par3);
		par2.add("A hollowed Multistructure made out of any Kind of brick or stone");
		par2.add("Put this block then into any Horizontal Side. In the Middle");
	}

	@Override
	public ITemplate getTemplate()
	{
		if(temp == null)
		{
			initTemplate();
		}
		return temp;
	}

	@Override
	public void initTemplate()
	{		
		if(temp == null)
		{
			temp = new TemplatePressureFurnace(this);
		}
		temp.setup(worldObj, xCoord, yCoord, zCoord, getFacing());
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return false;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		return new FluidTankInfo[]{new FluidTankInfo(new FluidStack(FluidRegistry.WATER, 0), 1)};
	}

	public static boolean isValidFuel(ItemStack ingredient)
	{
		if(ingredient == null)
		{
			return false;
		}
		if(!validFuels.contains(ingredient.itemID))
		{
			return false;
		}
		if(fuelMeta.containsKey(ingredient.itemID))
		{
			if(fuelMeta.get(ingredient.itemID) != ingredient.getItemDamage())
			{
				return false;
			}
		}
		return true;
	}
	
}
