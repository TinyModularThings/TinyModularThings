package speiger.src.tinymodularthings.common.blocks.storage;

import ic2.api.tile.IWrenchable;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import speiger.src.api.blocks.BlockStack;
import speiger.src.api.items.InfoStack;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.WorldReading;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.proxy.LangProxy;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;
import speiger.src.tinymodularthings.common.utils.fluids.TinyFluidTank;
import buildcraft.BuildCraftFactory;
import buildcraft.api.tools.IToolWrench;
import buildcraft.factory.TileTank;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TinyTank extends AdvTile implements IFluidHandler, IWrenchable
{
	
	public TinyFluidTank tank = new TinyFluidTank("TinyTank", 0, this);
	public int tankMode = -1;
	public int renderLiquid = 1;
	public boolean BCTank = false;
	public static Icon texture = null;
	public boolean renderTank = false;
	
	public static boolean dissableRenderer = false;
	
	
	@Override
	public void registerIcon(IconRegister par1)
	{
		texture = par1.registerIcon(TinyModularThingsLib.ModID.toLowerCase()+":storage/TinyTank");
	}

	public void setTankMode(int tankMode)
	{
		this.tankMode = tankMode;
	}
	
	public int getTankMode()
	{
		return tankMode;
	}
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		return texture;
	}
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		try
		{
			if(this.BCTank)
			{
				TinyTank tank = this.getHighestTank();
				if(tank != null && tank.BCTank)
				{
					TileEntity tile = worldObj.getBlockTileEntity(tank.xCoord, tank.yCoord+1, tank.zCoord);
					if(tile != null && tile instanceof TileTank)
					{
						TileTank bc = (TileTank) tile;
						if(bc.fill(from.DOWN, resource, false) > 0)
						{
							bc.hasUpdate = true;
							return bc.fill(from.DOWN, resource, doFill);
						}
					}
				}
			}
		}
		catch (Exception e)
		{
		}
		
		TinyTank tiny = this.getHighestTank();
		if(tiny != null)
		{
			return tiny.tank.fill(resource, doFill);
		}
		return 0;
	}
	
	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		return this.drain(from, resource.amount, doDrain);
	}
	
	@Override
	public int getBlockLightLevel()
	{
		if (tank.getFluid() != null && tank.getFluid().amount > 0 && tank.getFluid().getFluid() != null)
		{
			Fluid fluid = tank.getFluid().getFluid();
			return fluid.getLuminosity(tank.getFluid());
			
		}
		return 0;
	}
	
	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		try
		{
			if(this.BCTank)
			{
				TinyTank bottom = this.getBottomTank();
				if(bottom != null && bottom.BCTank)
				{
					TileEntity tile = worldObj.getBlockTileEntity(bottom.xCoord, bottom.yCoord-1, bottom.zCoord);
					if(tile != null && tile instanceof TileTank)
					{
						TileTank tank = (TileTank) tile;
						if(tank.drain(from.UP, maxDrain, false) != null)
						{
							return tank.drain(from.UP, maxDrain, doDrain);
						}
					}
				}
			}
		}
		catch (Exception e)
		{
		}
		
		TinyTank bottom = this.getBottomTank();
		if(bottom != null)
		{
			return bottom.tank.drain(maxDrain, doDrain);
		}
		return null;
	}
	
	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return true;
	}
	
	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return true;
	}
	
	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		return new FluidTankInfo[] { tank.getInfo() };
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		
		tankMode = nbt.getInteger("TankMode");
		
		if (isValid())
		{
			initTank();
		}
		
		tank = (TinyFluidTank) tank.readFromNBT(nbt);
		if(tank.getFluid() != null && tank.getFluidAmount() <= 0)
		{
			tank.setFluid(null);
		}
		renderLiquid = nbt.getInteger("Render");
		renderTank = nbt.getBoolean("Renders");
		BCTank = nbt.getBoolean("BC");
	}
	
	@Override
	public void onPlaced(int facing)
	{
		if (isValid())
		{
			initTank();
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("TankMode", tankMode);
		nbt.setInteger("Render", renderLiquid);
		nbt.setBoolean("BC", BCTank);
		nbt.setBoolean("Renders", renderTank);
		tank.writeToNBT(nbt);
	}
	
	@Override
	public void onTick()
	{
		super.onTick();
		
		updateBlock();
		if (!worldObj.isRemote)
		{
			
			if (worldObj.getWorldTime() % 80 == 0)
			{
				PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, getDescriptionPacket());
			}
			
			if(worldObj.getWorldTime() % 15 == 0)
			{
				renderTank = WorldReading.isBlockBlocked(worldObj, xCoord, yCoord, zCoord, true);
			}
			
			
			if(this.tank.getFluid() != null)
			{
				
				if(canFillBelow())
				{
					fillBelow();
				}
				else
				{
					fillToSide();
				}
			}
			
			if(this.BCTank)
			{
				TileEntity tile = worldObj.getBlockTileEntity(xCoord, yCoord+1, zCoord);
				if(tile != null && tile instanceof TileTank)
				{
					TileTank tanks = (TileTank) tile;
					tanks.drain(ForgeDirection.DOWN, this.tank.fill(tanks.tank.getFluid(), true), true);
				}
			}
		}
		
	}
	
	public void fillBelow()
	{
		TinyTank tank = this.getTankBelow(this);
		if(tank != null)
		{
			this.tank.drain(tank.tank.fill(this.tank.getFluid(), true), true);
		}
		else
		{
			tank = this;
		}
		
		
		if(this.BCTank)
		{
			try
			{
				TileEntity tile = worldObj.getBlockTileEntity(xCoord, yCoord-1, zCoord);
				if(tile != null && tile instanceof TileTank)
				{
					TileTank tanks = (TileTank) tile;
					this.tank.drain(tanks.fill(ForgeDirection.UP, this.tank.getFluid(), true), true);
				}
				

			}
			catch (Exception e)
			{
			}
		}
		
	}
	
	public void fillToSide()
	{
		TinyTank[] tanks = new TinyTank[4];
		for(int i = 0;i<tanks.length;i++)
		{
			tanks[i] = getTinyTankFromSide(i+2);
		}
		
		double[] prozent = new double[4];
		double ownProzent = ((double) tank.getFluidAmount() / (double)this.tank.getCapacity() )*100;
		for(int i = 0;i<tanks.length;i++)
		{
			if(tanks[i] != null)
			{
				prozent[i] = ((double)tanks[i].tank.getFluidAmount() / (double)tanks[i].tank.getCapacity())*100;
			}
		}
		
		for(int i = 0;i<tanks.length;i++)
		{
			if(tanks[i] != null)
			{
				double pro = ownProzent - prozent[i];
				if(pro > 0)
				{
					
					if(tanks[i].tank.fill(this.tank.drain(this.getProzentInMilliBuckets(pro/2, tanks[i]), false), false) == this.getProzentInMilliBuckets(pro/2, tanks[i]))
					{
						tanks[i].tank.fill(this.tank.drain(this.getProzentInMilliBuckets(pro/2, tanks[i]), true), true);
					}
					else if(this.getProzentInMilliBuckets(pro/2, tanks[i]) > this.tank.getFluidAmount())
					{
						this.tank.drain(tanks[i].tank.fill(this.tank.getFluid(), true), true);
					}
				}
			}
		}
	}
	
	public int getProzentInMilliBuckets(double persent, TinyTank tank)
	{
		double one = ((double)1 / (double)tank.tank.getCapacity())*100;
		
		int result = (int) (persent / one);

		return result;
	}
	
	private TinyTank getTinyTankFromSide(int i)
	{
		TileEntity tile = WorldReading.getTileEntity(worldObj, xCoord, yCoord, zCoord, i);
		if(tile != null && tile instanceof TinyTank)
		{
			return (TinyTank)tile;
		}
		return null;
	}

	public boolean canFillBelow()
	{
		TinyTank tank = this.getTankBelow(this);
		if(tank != null && tank.tank.fill(this.tank.getFluid(), false) > 0)
		{
			return true;
		}
		
		if(tank == null)
		{
			tank = this;
		}
		
		if(tank != null)
		{
			try
			{
				TileEntity tile = worldObj.getBlockTileEntity(tank.xCoord, tank.yCoord-1, tank.zCoord);
				if(tile != null && tile instanceof TileTank)
				{
					return ((TileTank)tile).fill(ForgeDirection.UP, this.tank.getFluid(), false) > 0;
				}
			}
			catch (Exception e)
			{
			}
		}
		return false;
	}
	
	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
	{
		readFromNBT(pkt.data);
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, nbt);
	}
	
	@Override
	public boolean onClick(boolean sneak, EntityPlayer par1, Block par2, int side)
	{
		if (sneak)
		{
			ItemStack cu = par1.getCurrentEquippedItem();
			if (cu == null)
			{
				this.renderLiquid++;
				if(renderLiquid > 2)
				{
					renderLiquid = 0;
				}
				this.sendMessage(renderLiquid, par1);
				return true;
			}
			else
			{
				if(cu.getItem() instanceof IToolWrench)
				{
					IToolWrench wrench = (IToolWrench) cu.getItem();
					if(wrench.canWrench(par1, xCoord, yCoord, zCoord))
					{
						this.wrench = true;
						worldObj.setBlockToAir(xCoord, yCoord, zCoord);
						wrench.wrenchUsed(par1, xCoord, yCoord, zCoord);
						return true;
					}
				}
			}
			
		}
		else
		{
			ItemStack current = par1.getCurrentEquippedItem();
			if (current != null)
			{
				if(FluidContainerRegistry.isContainer(current) || FluidContainerRegistry.isBucket(current))
				{
					FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(current);
					
					if (liquid != null)
					{
						int qty = fill(ForgeDirection.UNKNOWN, liquid, true);
						
						if (qty != 0 && !par1.capabilities.isCreativeMode)
						{
							par1.inventory.setInventorySlotContents(par1.inventory.currentItem, consumeItem(current));
						}
						
						return true;
						
					}
					else
					{
						
						FluidStack available = getTankInfo(ForgeDirection.UNKNOWN)[0].fluid;
						if (available != null)
						{
							ItemStack filled = FluidContainerRegistry.fillFluidContainer(available, current);
							
							liquid = FluidContainerRegistry.getFluidForFilledItem(filled);
							
							if (liquid != null)
							{
								if (!par1.capabilities.isCreativeMode)
								{
									if (current.stackSize > 1)
									{
										if (!par1.inventory.addItemStackToInventory(filled))
										{
											return false;
										}
										else
										{
											par1.inventory.setInventorySlotContents(par1.inventory.currentItem, consumeItem(current));
										}
									}
									else
									{
										par1.inventory.setInventorySlotContents(par1.inventory.currentItem, consumeItem(current));
										par1.inventory.setInventorySlotContents(par1.inventory.currentItem, filled);
									}
								}
								drain(ForgeDirection.UNKNOWN, liquid.amount, true);
								return true;
							}
						}
					}
				}
				else
				{
					try
					{
						if(!this.BCTank && current.itemID == BuildCraftFactory.tankBlock.blockID)
						{
							TinyTank bottom = this.getBottomTank();
							TinyTank top = this.getHighestTank();
							this.BCTank = true;
							par1.sendChatToPlayer(LanguageRegister.createChatMessage("Inited Tank to BuildCraft Compatiblity"));
							if(bottom.hasBCTank(false) && !bottom.BCTank)
							{
								par1.sendChatToPlayer(LanguageRegister.createChatMessage("Found at the Bottom Tank a BuildCraft and TinyTank has No Compatiblity Activated. The connection has to be activate too if you want to interact with that tank to"));
							}
							
							if(top.hasBCTank(true) && !top.BCTank)
							{
								par1.sendChatToPlayer(LanguageRegister.createChatMessage("Found at the Top Tank a BuildCraft and TinyTank has No Compatiblity Activated. The connection has to be activate too if you want to interact with that tank to"));
							}
							
							return true;
						}
					}
					catch (Exception e)
					{
					}
				}
				
			}
			else
			{
				String name = "Nothing";
				int amount = 0;
				
				if (tank.getFluid() != null && tank.getFluid().amount > 0 && tank.getFluid().getFluid() != null)
				{
					Fluid fluid = tank.getFluid().getFluid();
					if (fluid.getBlockID() != -1)
					{
						BlockStack stack = new BlockStack(fluid.getBlockID());
						name = stack.getBlockDisplayName();
						
					}
					else if (fluid.getBlockID() == -1 && !fluid.getLocalizedName().startsWith("tile"))
					{
						name = fluid.getLocalizedName();
					}
					else
					{
						name = "Unknowen Fluid";
					}
					amount = tank.getFluid().amount;
					
				}
				
				if (name.equals("Nothing"))
				{
					par1.sendChatToPlayer(LanguageRegister.createChatMessage(LanguageRegister.getLanguageName(new InfoStack(), "tank.stored.nothing", TinyModularThings.instance)));
				}
				else if (name.equals("Unknowen Fluid"))
				{
					String tank = LanguageRegister.getLanguageName(new InfoStack(), "tank.stored", TinyModularThings.instance);
					par1.sendChatToPlayer(LanguageRegister.createChatMessage(tank + ": " + LangProxy.UFluid(TinyModularThings.instance) + " " + LangProxy.getAmount(TinyModularThings.instance) + ": " + amount + "mB / " + this.tank.getCapacity() + "mB"));
					
				}
				else
				{
					String tank = LanguageRegister.getLanguageName(new InfoStack(), "tank.stored", TinyModularThings.instance);
					par1.sendChatToPlayer(LanguageRegister.createChatMessage(tank + ": " + name + " " + LangProxy.getAmount(TinyModularThings.instance) + ": " + amount + "mB / " + this.tank.getCapacity() + "mB"));
				}
			}
		}
		
		return false;
	}
	
	public void sendMessage(int id, EntityPlayer par1)
	{
		switch(id)
		{
			case 0:
				par1.sendChatToPlayer(LanguageRegister.createChatMessage(LanguageRegister.getLanguageName(this, "render.tankfluid.dissabled", TinyModularThings.instance)));
				break;
			case 1:
				par1.sendChatToPlayer(LanguageRegister.createChatMessage(LanguageRegister.getLanguageName(this, "render.tankfluid.auto", TinyModularThings.instance)));
				break;
			case 2: 
				par1.sendChatToPlayer(LanguageRegister.createChatMessage(LanguageRegister.getLanguageName(this, "render.tankfluid.enabled", TinyModularThings.instance)));
				break;
		}
	}
	
	public boolean hasBCTank(boolean top)
	{
		try
		{
			if(top)
			{
				TileEntity tile = worldObj.getBlockTileEntity(xCoord, yCoord+1, zCoord);
				if(tile != null && tile instanceof TileTank)
				{
					return true;
				}
			}
			else
			{
				TileEntity tile = worldObj.getBlockTileEntity(xCoord, yCoord-1, zCoord);
				if(tile != null && tile instanceof TileTank)
				{
					return true;
				}
			}
		}
		catch (Exception e)
		{
		}
		return false;
	}
	
	public ItemStack consumeItem(ItemStack stack)
	{
		if (stack.stackSize == 1)
		{
			if (stack.getItem().hasContainerItem())
			{
				return stack.getItem().getContainerItemStack(stack);
			}
			else
			{
				return null;
			}
		}
		else
		{
			stack.splitStack(1);
			
			return stack;
		}
	}
	
	public void initTank()
	{
		int[] tankSizes = new int[] { 1000, 2000, 4000, 8000, 12000, 16000, 24000, 32000, 64000 };
		
		int size = tankSizes[tankMode];
		String name = tank.getName();
		if (name.equals("Unamed Tank"))
		{
			name = "TinyTank";
		}
		
		tank = new TinyFluidTank(name, size, this);
		
	}
	
	public boolean isValid()
	{
		return tankMode != -1;
	}
	
	public boolean wrench = false;
	public boolean droped = false;
	
	@Override
	public ArrayList<ItemStack> onDrop(int fortune)
	{
		ArrayList<ItemStack> stack = new ArrayList<ItemStack>();
		if(droped)
		{
			return stack;
		}
		
		droped = true;
		
		ItemStack item = new ItemStack(TinyItems.tinyTank, 1, tankMode);
		
		if(tank.getFluid() != null)
		{
			if(wrench)
			{
				NBTTagCompound data = new NBTTagCompound();
				data.setInteger("FluidID", tank.getFluid().fluidID);
				data.setInteger("Amount", tank.getFluid().amount);
				if(tank.getFluid().tag != null)
				{
					data.setCompoundTag("Data", tank.getFluid().tag);
				}
				item.setTagInfo("Fluid", data);
			}
			else
			{
				boolean all = this.worldObj.rand.nextBoolean();
				double per = (double)this.worldObj.rand.nextInt(101);
				int fluidAmount = this.getProzentInMilliBuckets(per, this);
				if(fluidAmount > this.tank.getFluidAmount())
				{
					fluidAmount = this.tank.getFluidAmount();
				}
				if(fluidAmount <= 0 || !all)
				{
					stack.add(item);
					return stack;
				}
				NBTTagCompound data = new NBTTagCompound();
				data.setInteger("FluidID", tank.getFluid().fluidID);
				data.setInteger("Amount", fluidAmount);
				if(tank.getFluid().tag != null)
				{
					data.setCompoundTag("Data", tank.getFluid().tag);
				}
				item.setTagInfo("Fluid", data);
			}
		}
		stack.add(item);
		
		return stack;
	}
	
	@Override
	public ItemStack pickBlock(MovingObjectPosition target)
	{
		return new ItemStack(TinyItems.tinyTank, 1, tankMode);
	}
	
	public TinyTank getHighestTank()
	{
		TinyTank tiny = this;
		while(true)
		{
			TinyTank above = this.getTankAbove(tiny);
			if(above != null)
			{
				tiny = above;
			}
			else
			{
				break;
			}
		}
		return tiny;
	}
	
	public TinyTank getBottomTank()
	{
		TinyTank tiny = this;
		while(true)
		{
			TinyTank below = this.getTankBelow(tiny);
			if(below != null)
			{
				tiny = below;
			}
			else
			{
				break;
			}
		}
		return tiny;
	}
	
	public TinyTank getTankAbove(TinyTank tiny)
	{
		TileEntity tile = worldObj.getBlockTileEntity(tiny.xCoord, tiny.yCoord+1, tiny.zCoord);
		if(tile instanceof TinyTank)
		{
			return (TinyTank)tile;
		}
		return null;
	}
	
	public TinyTank getTankBelow(TinyTank tiny)
	{
		TileEntity tile = worldObj.getBlockTileEntity(tiny.xCoord, tiny.yCoord-1, tiny.zCoord);
		if(tile instanceof TinyTank)
		{
			return (TinyTank)tile;
		}
		return null;
	}

	public boolean renderLiquid()
	{
		if(this.renderLiquid == 0)
		{
			return false;
		}
		else if(this.renderLiquid == 1)
		{
			return !WorldReading.isBlockBlocked(worldObj, xCoord, yCoord, zCoord, false);
		}
		else if(this.renderLiquid == 2)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	@SideOnly(Side.CLIENT)
	public boolean renderTank()
	{
		if(this.dissableRenderer)
		{
			return true;
		}
		return renderTank;
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side)
	{
		return false;
	}

	@Override
	public short getFacing()
	{
		return 0;
	}

	@Override
	public void setFacing(short facing)
	{
		
	}

	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer)
	{
		return true;
	}

	@Override
	public float getWrenchDropRate()
	{
		return 1F;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer)
	{
		this.wrench = true;
		this.droped = false;
		ItemStack drop = this.onDrop(0).get(0);
		this.droped = true;
		return drop;
	}
	
}
