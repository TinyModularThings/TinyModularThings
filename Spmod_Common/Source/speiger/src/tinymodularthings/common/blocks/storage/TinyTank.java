package speiger.src.tinymodularthings.common.blocks.storage;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
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
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.proxy.LangProxy;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.utils.fluids.TinyFluidTank;
import cpw.mods.fml.common.network.PacketDispatcher;

public class TinyTank extends AdvTile implements IFluidHandler
{
	
	public TinyFluidTank tank = new TinyFluidTank("TinyTank", 0, this);
	public int tankMode = -1;
	public int lastState = 0;
	public boolean renderLiquid = true;
	
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
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		return tank.fill(resource, doFill);
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
		return tank.drain(maxDrain, doDrain);
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
		
		tank.readFromNBT(nbt);
		renderLiquid = nbt.getBoolean("Render");
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
		nbt.setBoolean("Render", renderLiquid);
		tank.writeToNBT(nbt);
	}
	
	@Override
	public void onTick()
	{
		super.onTick();
		
		if (lastState != tank.getFluidAmount() && renderLiquid)
		{
			lastState = tank.getFluidAmount();
			
		}
		updateBlock();
		if (!worldObj.isRemote)
		{
			if (worldObj.getWorldTime() % 10 == 0)
			{
				PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, getDescriptionPacket());
			}
		}
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
				if (renderLiquid)
				{
					renderLiquid = false;
					par1.sendChatToPlayer(LanguageRegister.createChatMessage(LanguageRegister.getLanguageName(this, "render.tankfluid.dissabled", TinyModularThings.instance)));
				}
				else
				{
					renderLiquid = true;
					par1.sendChatToPlayer(LanguageRegister.createChatMessage(LanguageRegister.getLanguageName(this, "render.tankfluid.enabled", TinyModularThings.instance)));
				}
				return true;
			}
			
		}
		else
		{
			ItemStack current = par1.getCurrentEquippedItem();
			if (current != null)
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
		return new ItemStack(TinyItems.tinyTank, 1, tankMode - 1);
	}
	
}
