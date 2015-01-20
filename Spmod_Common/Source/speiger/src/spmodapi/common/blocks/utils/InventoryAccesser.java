package speiger.src.spmodapi.common.blocks.utils;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;

import java.io.DataInput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.common.data.nbt.INBTReciver;
import speiger.src.api.common.data.packets.IPacketReciver;
import speiger.src.api.common.data.packets.SpmodPacketHelper.ModularPacket;
import speiger.src.api.common.registry.helpers.SpmodMod;
import speiger.src.api.common.utils.MathUtils;
import speiger.src.api.common.world.blocks.BlockPosition;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.api.common.world.items.IBCBattery;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.tile.FacedInventory;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class InventoryAccesser extends FacedInventory implements ISidedInventory, INBTReciver, IPacketReciver
{

	public static ArrayList<String> allowedClasses = new ArrayList<String>();
	public HashMap<List<Integer>, String> customNames = new HashMap<List<Integer>, String>();
	public ArrayList<List<Integer>> canBeOpened = new ArrayList<List<Integer>>();
	public double power = 0;
	

	public InventoryAccesser()
	{
		super(5);
	}
	
	@Override
	public boolean isSixSidedFacing()
	{
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onItemInformation(EntityPlayer par1, List par2, ItemStack par3)
	{
		par2.add("Allow to access Inventories");
		par2.add("Require Redstone Cables and any Sort of TMT or IC2 Portable Power");
		if(GuiScreen.isCtrlKeyDown())
		{
			par2.add("This machine Requires 1 PowerUnit per Sek when the gui is Open");
			par2.add("1 EU = 1 PowerUnit");
			par2.add("1 MJ = 2.5 PowerUnits");
		}
		else
		{
			par2.add("Press Ctrl to get Extra Infos");
		}
	}
	
	@Override
	public float getBlockHardness()
	{
		return 4F;
	}
	@Override
	public float getExplosionResistance(Entity par1)
	{
		return 5F;
	}
	
	@Override
	public void registerIcon(TextureEngine par1, Block par2)
	{
		super.registerIcon(par1, par2);
		par1.registerTexture(new BlockStack(par2, 4), "InventoryAccesser_Top", "InventoryAccesser_Side", "InventoryAccesser_Front");
	}

	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		TextureEngine engine = TextureEngine.getTextures();
		BlockStack block = new BlockStack(APIBlocks.blockUtils, 4);
		if(side == getFacing())
		{
			return engine.getTexture(block, 2);
		}
		else if(side == 0 || side == 1)
		{
			return engine.getTexture(block, 0);
		}
		else
		{
			return engine.getTexture(block, 1);
		}
	}
	
	public String getCustomName(List<Integer> par1)
	{
		return customNames.get(par1);
	}
	
	public boolean hasCustomName(List<Integer> par1)
	{
		return customNames.containsKey(par1);
	}
	
	public BlockPosition getTarget(int i)
	{
		return new BlockPosition(canBeOpened.get(i));
	}
	
	public boolean isInRange(int i)
	{
		return this.canBeOpened.size() > i;
	}
	
	public boolean needPower()
	{
		return power <= 0;
	}

	@Override
	public void onTick()
	{
		if(!worldObj.isRemote)
		{
			if(power < 2000)
			{
				getPower();
			}
			if(worldObj.getWorldTime() % 20 == 0)
			{
				if(hasUsers() && power > 0)
				{
					power-=1;
				}
			}
		}
	}
	
	public void getPower()
	{
		ItemStack stack = getStackInSlot(4);
		if(stack != null)
		{
			if(stack.getItem() instanceof IBCBattery)
			{
				power += (2.5*((IBCBattery)stack.getItem()).discharge(stack, 1, false, false));
			}
			else if(stack.getItem() instanceof IElectricItem)
			{
				power +=ElectricItem.manager.discharge(stack, 1, ((IElectricItem)stack.getItem()).getTier(stack), false, false);
			}
		}
	}
	
	@Override
	public boolean onActivated(EntityPlayer par1)
	{
		if(!worldObj.isRemote)
		{
			reloadData();
		}
		return super.onActivated(par1);
	}
	
	@Override
	public boolean canMergeItem(ItemStack par1, int slotID)
	{
		if(par1 != null)
		{
			if(slotID >= 0 && slotID < 4)
			{
				return par1.itemID == APIItems.redstoneCable.itemID;
			}
			if(slotID == 4)
			{
				if(par1.getItem() instanceof IElectricItem)
				{
					return ElectricItem.manager.getCharge(par1) > 0;
				}
				if(par1.getItem() instanceof IBCBattery)
				{
					return !((IBCBattery)par1.getItem()).isEmpty(par1);
				}
			}
		}
		return super.canMergeItem(par1, slotID);
	}

	@Override
	public boolean hasContainer()
	{
		return true;
	}
	
	@Override
	public void addContainerSlots(AdvContainer par1)
	{
		for(int i = 0;i<4;i++)
		{
			par1.addSpmodSlot(this, i, 150, 0+i*18).addUsage("Redstone Cable Slot");
		}
		par1.addSpmodSlot(this, 4, 150, 90).addUsage("Power Slot", "Accept EU Batteries", "Accept BC Batteries");
	}
	
	@Override
	public boolean renderInnerInv()
	{
		return false;
	}
	
	@Override
	public void onReciveGuiInfo(int key, int val)
	{
		if(key == 0)
		{
			power = val;
		}
	}

	@Override
	public void onSendingGuiInfo(Container par1, ICrafting par2)
	{
		par2.sendProgressBarUpdate(par1, 0, (int)power);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ResourceLocation getTexture()
	{
		return getEngine().getTexture("Clear");
	}
	
	@Override
	public int getNameColor()
	{
		return 0xffffff;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getInvNameYOffset()
	{
		return 55;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getNameYOffset()
	{
		return -15;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onGuiLoad(GuiInventoryCore par1, int guiX, int guiY)
	{
		par1.setupExtraGuiObjects(1);
		GuiTextField field = new GuiTextField(par1.getFontRenderer(), 20, 40, 100, 10);
		field.setCanLoseFocus(true);
		field.setFocused(true);
		par1.getGuiObject(0).setObject(field);
	}
	
	

	@Override
	@SideOnly(Side.CLIENT)
	public boolean onKeyTyped(GuiInventoryCore par1, char par2, int par3)
	{
		NBTTagCompound data = par1.getExtraData();
		if(data.getBoolean("TextInited"))
		{
			par1.getGuiObject(0).getObject(GuiTextField.class).textboxKeyTyped(par2, par3);
			if(par2 == Character.valueOf('E') || par2 == Character.valueOf('c'))
			{
				return true;
			}
		}
		return super.onKeyTyped(par1, par2, par3);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onButtonClick(GuiInventoryCore par1, GuiButton par2)
	{
		int id = par2.id;
		NBTTagCompound nbt = par1.getExtraData();
		if(id == 6)
		{
			int page = nbt.getInteger("Page");
			if(isInRange((page+1)*6))
			{
				nbt.setInteger("Page", page+1);
			}
		}
		else if(id == 7)
		{
			int page = nbt.getInteger("Page");
			if(page > 0)
			{
				nbt.setInteger("Page", page-1);
			}
		}
		else if(id == 8)
		{
			nbt.setBoolean("Name", false);
			nbt.setBoolean("TextInited", false);
			nbt.setInteger("TargetID", 0);
			par1.getGuiObject(0).getObject(GuiTextField.class).setText("");
		}
		else if(id == 9)
		{
			ModularPacket packet = createBasicPacket(SpmodAPI.instance);
			packet.InjectNumbers(0, nbt.getInteger("TargetID"));
			packet.injetString(par1.getGuiObject(0).getObject(GuiTextField.class).getText());
			this.sendPacketToServer(packet.finishPacket());
			nbt.setBoolean("Name", false);
			nbt.setBoolean("TextInited", false);
			nbt.setInteger("TargetID", 0);
			par1.getGuiObject(0).getObject(GuiTextField.class).setText("");
		}
		else 
		{
			nbt.setInteger("TargetID", ((nbt.getInteger("Page") * 6) + id));
			if(par1.isShiftKeyDown())
			{
				nbt.setInteger("Page", 0);
				nbt.setBoolean("Name", true);
			}
			else if(par1.isCtrlKeyDown())
			{
				this.sendPacketToServer(createBasicPacket(SpmodAPI.instance).InjectNumbers(4, nbt.getInteger("TargetID")).finishPacket());
			}
			else
			{
				this.sendPacketToServer(createBasicPacket(SpmodAPI.instance).InjectNumbers(1, nbt.getInteger("TargetID")).injetString(par1.getMC().thePlayer.username).finishPacket());
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void drawFrontExtras(GuiInventoryCore par1, int guiX, int guiY, int mouseX, int mouseY)
	{
		par1.getButtonsList().clear();
		
		int lvl = (int)(((double)power / (double)2000D) * 100);
		par1.getFontRenderer().drawString("Charge: "+lvl+"%", 116, 127, 0xffffff);
		if(power <= 0)
		{
			return;
		}
		NBTTagCompound data = par1.getExtraData();
		if(data.getBoolean("Name"))
		{
			if(!isInRange(data.getInteger("TargetID")))
			{
				data.setBoolean("Name", false);
				data.setBoolean("TextInited", false);
				par1.getGuiObject(0).getObject(GuiTextField.class).setText("");
				data.setInteger("TargetID", 0);
				return;
			}
			BlockPosition pos = getTarget(data.getInteger("TargetID"));
			ItemStack stack = pos.getAsBlockStack().getPicketBlock(getPosition(), getSideFromPlayer(par1.getMC().thePlayer.username));
			par1.renderItem(stack, 60, 20);
			
			if(!data.getBoolean("TextInited"))
			{
				data.setBoolean("TextInited", true);
				if(hasCustomName(pos.getAsList()))
				{
					par1.getGuiObject(0).getObject(GuiTextField.class).setText(getCustomName(pos.getAsList()));
				}
			}
			par1.getGuiObject(0).getObject(GuiTextField.class).drawTextBox();
			par1.getButtonsList().add(new GuiButton(8, guiX+10, guiY+70, 50, 20, "Back"));
			par1.getButtonsList().add(new GuiButton(9, guiX+70, guiY+70, 50, 20, "Confirm"));
		}
		else
		{
			par1.getFontRenderer().drawString(""+data.getInteger("Page"), 72, 7, 0xffffff);
			par1.getButtonsList().add(new GuiButton(6, guiX+90, guiY, 20, 20, "+"));
			par1.getButtonsList().add(new GuiButton(7, guiX+40, guiY, 20, 20, "-"));
			
			for(int i = 0;i<6;i++)
			{
				if(!isInRange((data.getInteger("Page")* 6 )+ i))
				{
					return;
				}
				BlockPosition pos = getTarget((data.getInteger("Page") * 6) + i);
				String name = pos.getAsBlockStack().getPickedBlockDisplayName(pos, getSideFromPlayer(par1.getMC().thePlayer.username));
				
				if(hasCustomName(pos.getAsList()))
				{
					name = getCustomName(pos.getAsList());
				}
				par1.getButtonsList().add(new GuiButton(i, guiX, guiY + 20 + (20*i), 145, 20, name));
				ItemStack stack = pos.getAsBlockStack().getPicketBlock(pos, getSideFromPlayer(par1.getMC().thePlayer.username));
				
				if(stack == null)
				{
					continue;
				}
				par1.renderItem(stack, -17, 28 + (20*i));
			}
		}
	}
	
	

	public void reloadData()
	{
		canBeOpened.clear();
		int size = 0;
		for(int i = 0;i<4;i++)
		{
			ItemStack stack = this.getStackInSlot(i);
			if(stack != null)
			{
				size += stack.stackSize;
			}
		}
		LinkedList<List<Integer>> possebilities = new LinkedList<List<Integer>>();
		LinkedList<List<Integer>> added = new LinkedList<List<Integer>>();
		BlockPosition pos = this.getPosition();
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
		{		
			BlockPosition newPos = pos.add(dir);
			if(newPos.doesBlockExsist())
			{
				boolean par1 = false;
				if(newPos.isThisBlock(new BlockStack(Block.workbench), false))
				{
					par1 = true;
				}
				else if(newPos.isThisBlock(new BlockStack(Block.anvil), false))
				{
					par1 = true;
				}
				else if(newPos.hasTileEntity())
				{
					TileEntity tile = newPos.getTileEntity();
					if(allowedClasses.contains(tile.getClass().getSimpleName()))
					{
						par1 = true;
					}
				}
				
				if(par1)
				{
					possebilities.add(newPos.getAsList());
					added.add(newPos.getAsList());
				}
			}
		}
		for(;!possebilities.isEmpty();)
		{
			BlockPosition lastTarget = new BlockPosition(possebilities.removeFirst());
			for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
			{
				BlockPosition newPos = lastTarget.add(dir);
				if(newPos.isThisPosition(pos))
				{
					continue;
				}
				if(added.contains(newPos.getAsList()))
				{
					continue;
				}
				
				if(newPos.doesBlockExsist())
				{
					boolean par1 = false;
					if(newPos.isThisBlock(new BlockStack(Block.workbench), false))
					{
						par1 = true;
					}
					else if(newPos.isThisBlock(new BlockStack(Block.anvil), false))
					{
						par1 = true;
					}
					else if(newPos.hasTileEntity())
					{
						TileEntity tile = newPos.getTileEntity();
						if(allowedClasses.contains(tile.getClass().getSimpleName()))
						{
							par1 = true;
						}
					}
					
					if(par1)
					{
						possebilities.add(newPos.getAsList());
						added.add(newPos.getAsList());
					}
				}
			}
		}
		if(added.size() > size)
		{
			for(;added.size() > size;)
			{
				added.removeLast();
			}
		}
		canBeOpened.addAll(added);
		this.sendPacketToClient(getDescriptionPacket(), 20);
	}
	
	@Override
	public void onInventoryChanged()
	{
		super.onInventoryChanged();
		if(!worldObj.isRemote)
		{
			reloadData();
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		NBTTagList names = nbt.getTagList("CustomNames");
		for(int i = 0;i<names.tagCount();i++)
		{
			NBTTagCompound data = (NBTTagCompound)names.tagAt(i);
			String key = data.getString("Value");
			int[] value = data.getIntArray("Key");
			customNames.put(MathUtils.getListFromArray(value), key);
		}
		
		NBTTagList can = nbt.getTagList("Can");
		canBeOpened.clear();
		for(int i = 0;i<can.tagCount();i++)
		{
			NBTTagIntArray data2 = (NBTTagIntArray)can.tagAt(i);
			int[] array = data2.intArray;
			canBeOpened.add(MathUtils.getListFromArray(array));
		}
		
		power = nbt.getDouble("Power");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		NBTTagList names = new NBTTagList();
		Iterator<Entry<List<Integer>, String>> iter = customNames.entrySet().iterator();
		for(;iter.hasNext();)
		{
			Entry<List<Integer>, String> entry = iter.next();
			NBTTagCompound data = new NBTTagCompound();
			data.setIntArray("Key", MathUtils.getArrayFromList(entry.getKey()));
			data.setString("Value", entry.getValue());
			names.appendTag(data);
		}
		nbt.setTag("CustomNames", names);
		
		NBTTagList can = new NBTTagList();
		for(List<Integer> array : canBeOpened)
		{
			NBTTagIntArray data = new NBTTagIntArray("data", new int[]{array.get(0), array.get(1), array.get(2), array.get(3)});
			can.appendTag(data);
		}
		nbt.setTag("Can", can);
		
		nbt.setDouble("Power", power);
	}
	
	@Override
	public String getInvName()
	{
		return "Inventory Accesser";
	}
	
	@Override
	public int[] getAccessibleSlotsFromSide(int var1)
	{
		return new int[0];
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j)
	{
		return false;
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j)
	{
		return false;
	}
	
	public static void addTileEntity(String cu)
	{
		if(!allowedClasses.contains(cu))
		{
			allowedClasses.add(cu);
		}
	}

	public static void removeTileEntity(String tile)
	{
		if(allowedClasses.contains(tile))
		{
			allowedClasses.remove(tile);
		}
	}

	@Override
	public void recivePacket(DataInput par1)
	{
		if(!worldObj.isRemote)
		{
			try
			{
				int task = par1.readInt();
				if(task == 0)
				{
					int number = par1.readInt();
					String name = par1.readUTF();
					if(this.isInRange(number))
					{
						BlockPosition pos = this.getTarget(number);
						if(pos != null)
						{
							this.customNames.put(pos.getAsList(), name);
							PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, this.getDescriptionPacket());
						}
					}
				}
				else if(task == 1)
				{
					int number = par1.readInt();
					String name = par1.readUTF();
					int side = this.getSideFromPlayer(name);
					if(side != -1 && this.isInRange(number))
					{
						BlockPosition pos = this.getTarget(number);
						if(pos != null)
						{
							pos.getAsBlockStack().getBlock().onBlockActivated(pos.getWorld(), pos.getXCoord(), pos.getYCoord(), pos.getZCoord(), worldObj.getPlayerEntityByName(name), side, 0.5F, 0.5F, 0.5F);
						}
					}
				}
				else if(task == 2)
				{
					allowedClasses.add(par1.readUTF());
				}
				else if(task == 3)
				{
					allowedClasses.remove(par1.readUTF());
				}
				else if(task == 4)
				{
					int number = par1.readInt();
					if(this.isInRange(number))
					{
						BlockPosition pos = this.getTarget(number);
						if(pos != null)
						{
							this.customNames.remove(pos.getAsList());
							PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, this.getDescriptionPacket());
						}
					}
				}


			}
			catch(Exception e)
			{
			}
		}
		
	}

	@Override
	public String identifier()
	{
		return "Accesser";
	}

	@Override
	public void loadFromNBT(NBTTagCompound par1)
	{
		NBTTagList list = par1.getTagList("Data");
		for(int i = 0;i<list.tagCount();i++)
		{
			NBTTagString string = (NBTTagString)list.tagAt(i);
			if(!allowedClasses.contains(string.data))
			{
				allowedClasses.add(string.data);
			}
		}
	}

	@Override
	public void saveToNBT(NBTTagCompound par1)
	{
		NBTTagList list = new NBTTagList();
		for(String data : allowedClasses)
		{
			list.appendTag(new NBTTagString("Data", data));
		}
	}

	@Override
	public void finishLoading()
	{
		
	}

	@Override
	public SpmodMod getOwner()
	{
		return SpmodAPI.instance;
	}

	@Override
	public String getID()
	{
		return "AccessTile";
	}
}
