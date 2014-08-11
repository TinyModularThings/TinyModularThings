package speiger.src.tinymodularthings.common.blocks.storage;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
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
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import speiger.src.api.blocks.BlockStack;
import speiger.src.api.items.InfoStack;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.WorldReading;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.BlockPosition;
import speiger.src.spmodapi.common.util.proxy.LangProxy;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.utils.fluids.FluidAdjuster;
import speiger.src.tinymodularthings.common.utils.fluids.TinyFluidTank;
import cpw.mods.fml.common.network.PacketDispatcher;

public class TinyTank extends AdvTile implements IFluidHandler
{
	
	public TinyFluidTank tank = new TinyFluidTank("TinyTank", 0, this);
	public int tankMode = -1;
	public int renderLiquid = 1;
	public int updateTick = 80;
	public boolean keepFluid = false;
	
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
		return null;
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
		if(from == from.DOWN)
		{
			FluidAdjuster ad = this.getAdjuster();
			FluidStack stack = ad.drain(maxDrain, doDrain);
			ad.adjust();
			return stack;
		}
		
		TinyTank tank = this.tank.getFluid() != null ? this.getBottomTank(this.tank.getFluid(), this) : this.getBottomTank();
		if(tank != null)
		{
			if(tank.getPosition().isThisPosition(getPosition()))
			{
				FluidAdjuster ad = this.getAdjuster();
				FluidStack stack = ad.drain(maxDrain, doDrain);
				ad.adjust();
				return stack;
			}
			
			return tank.drain(from.UP, maxDrain, doDrain);
		}
		return null;
	}
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		if(resource != null)
		{
			if(this.tank.getFluid() != null)
			{
				if(!resource.isFluidEqual(this.tank.getFluid()))
				{
					return 0;
				}
			}
		}
		if(from == from.UP)
		{
			FluidAdjuster ad = this.getAdjuster();
			int filled = ad.fill(resource, doFill);
			ad.adjust();
			return filled;
		}
		TinyTank tank = this.getHighestTank(resource, this);
		if(tank != null)
		{
			if(tank.getPosition().isThisPosition(getPosition()))
			{
				if(this.tank.getFluid() == null)
				{
					FluidStack stack = resource.copy();
					stack.amount = 0;
					this.tank.setFluid(stack);
				}
				FluidAdjuster ad = this.getAdjuster();
				int filled = ad.fill(resource, doFill);
				ad.adjust();
				return filled;
			}
			return tank.fill(from.DOWN, resource, doFill);
		}
		return 0;
		
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
		this.renderLiquid = nbt.getInteger("Delay");
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
		nbt.setInteger("Delay", updateTick);
		tank.writeToNBT(nbt);
	}
	
	@Override
	public void onTick()
	{
		super.onTick();
		
		updateBlock();
		if (!worldObj.isRemote)
		{
			
			if (worldObj.getWorldTime() % 10 == 0)
			{
				PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, getDescriptionPacket());
			}
			
			if(worldObj.getWorldTime() % 20 == 0)
			{
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
			}
		}
	}
	
	public void fillBelow()
	{
		TinyTank tank = this.getTankBelow(this);
		if(tank != null)
		{
			this.drain(ForgeDirection.DOWN, tank.fill(ForgeDirection.UP, this.drain(ForgeDirection.DOWN, 50000, false), true), true);
		}
		
	}
	
	public void fillToSide()
	{
		if(updateTick > 0)
		{
			this.updateTick--;
		}
		else
		{
			FluidAdjuster ad = this.getAdjuster();
			ad.adjust();
		}
	}
	
	public FluidAdjuster getAdjuster()
	{
		ArrayList<List<Integer>> tankPos = new ArrayList<List<Integer>>();
		ArrayList<List<Integer>> todo = new ArrayList<List<Integer>>();
		ArrayList<List<Integer>> todoNext = new ArrayList<List<Integer>>();
		tankPos.add(this.getPosition().getAsList());
		for(int x = 2;x<ForgeDirection.VALID_DIRECTIONS.length;x++)
		{
			TinyTank tank = getTinyTankFromSide(x);
			if(tank != null && (tank.tank.getFluid() == null || tank.tank.getFluid().isFluidEqual(this.tank.getFluid())))
			{
				BlockPosition pos = tank.getPosition();
				if(!containsIt(tankPos, pos.getAsList()))
				{
					tankPos.add(pos.getAsList());
					todo.add(pos.getAsList());
				}

			}
		}
		
		
		boolean run = true;
		while(run)
		{
			if(!todo.isEmpty())
			{
				for(int i = 0;i<todo.size();i++)
				{
					BlockPosition pos = new BlockPosition(todo.get(i));
					for(int z = 2;z<ForgeDirection.VALID_DIRECTIONS.length;z++)
					{
						BlockPosition newPos = pos.getPosFromSide(z);
						if(newPos.hasTileEntity() && newPos.getTileEntity() instanceof TinyTank)
						{
							TinyTank tank = (TinyTank) newPos.getTileEntity();
							if(tank != null && (tank.tank.getFluid() == null || tank.tank.getFluid().isFluidEqual(this.tank.getFluid())))
							{
								if(!containsIt(tankPos, newPos.getAsList()))
								{
									tankPos.add(newPos.getAsList());
									todoNext.add(newPos.getAsList());
								}
							}
						}
					}
				}
				todo.clear();
			}
			else
			{
				if(todoNext.isEmpty())
				{
					run = false;
				}
				else
				{
					todo.addAll(todoNext);
					todoNext.clear();
				}
			}
		}
		FluidTank[] tanks = new FluidTank[tankPos.size()];
		for(int i = 0;i<tanks.length;i++)
		{
			TinyTank tank = ((TinyTank) new BlockPosition(tankPos.get(i)).getTileEntity());
			tank.updateTick = 85;
			tanks[i] = tank.tank;
		}
		this.updateTick = 70;
		
		FluidAdjuster adjuster = new FluidAdjuster(this.tank.getFluid(), tanks);
		return adjuster;
	}
	
	
	public boolean containsIt(ArrayList<List<Integer>> list, List<Integer> target)
	{
		for(List<Integer> cu : list)
		{
			if(cu.get(0) == target.get(0) && cu.get(1) == target.get(1) && cu.get(2) == target.get(2) && cu.get(3) == target.get(3))
			{
				return true;
			}
		}
		return false;
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
					else if (fluid.getBlockID() == -1 && !fluid.getName().startsWith("tile"))
					{
						name = fluid.getName();
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
	
	@Override
	public ArrayList<ItemStack> onDrop(int fortune)
	{
		ArrayList<ItemStack> stack = new ArrayList<ItemStack>();
		
		stack.add(new ItemStack(TinyItems.tinyTank, 1, tankMode));
		
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
	
	public TinyTank getHighestTank(FluidStack fluid, TinyTank tank)
	{
		TinyTank tiny = tank;
		while(true)
		{
			TinyTank top = this.getTankAbove(tiny);
			if(top != null)
			{
				if(top.tank.getFluid() != null && top.tank.getFluid().isFluidEqual(fluid))
				{
					tiny = top;
				}
				else if(top.tank.getFluid() == null)
				{
					tiny = top;
					break;
				}
				else
				{
					break;
				}
			}
			else
			{
				break;
			}
		}
		return tiny;
	}
	
	public TinyTank getBottomTank(FluidStack fluid, TinyTank tank)
	{
		TinyTank tiny = tank;
		while(true)
		{
			TinyTank bottom = this.getTankBelow(tiny);
			if(bottom != null)
			{
				tiny = bottom;
				if(fluid.isFluidEqual(bottom.tank.getFluid()))
				{
					break;
				}
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
			return !WorldReading.isBlockBlocked(worldObj, xCoord, yCoord, zCoord);
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
	
}
