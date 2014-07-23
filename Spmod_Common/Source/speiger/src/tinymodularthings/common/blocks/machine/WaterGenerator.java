package speiger.src.tinymodularthings.common.blocks.machine;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import speiger.src.api.energy.EnergyProvider;
import speiger.src.api.energy.IEnergyProvider;
import speiger.src.api.util.WorldReading;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.tile.AdvancedFluidTank;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import cpw.mods.fml.common.network.PacketDispatcher;

public class WaterGenerator extends AdvTile implements IFluidHandler, IPowerReceptor, IEnergyProvider
{
	public AdvancedFluidTank tank = new AdvancedFluidTank(this, "Water", 50000);
	public EnergyProvider provider = new EnergyProvider(this, 10000);
	
	public WaterGenerator()
	{
		tank.setFluid(new FluidStack(FluidRegistry.WATER, 0));
		provider.setPowerLoss(500);
	}
	
	
	@Override
	public void onTick()
	{
		super.onTick();
		
		
		if(!worldObj.isRemote)
		{
			this.provider.update();
			produceWater();
			sendWater();
			if(worldObj.getWorldTime() % 10 == 0)
			{
				PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, getDescriptionPacket());
				this.updateBlock();
			}
		}
	}

	public void sendWater()
	{
		FluidStack toSend = tank.drain(50000, false);
		
		if(toSend != null)
		{
			FluidStack copySend = toSend.copy();
			for(ForgeDirection cu : ForgeDirection.VALID_DIRECTIONS)
			{
				if(toSend.amount > 0)
				{
					int x = this.xCoord + cu.offsetX;
					int y = this.yCoord + cu.offsetY;
					int z = this.zCoord + cu.offsetZ;
					if(WorldReading.hasTank(worldObj, x, y, z))
					{
						toSend.amount -= WorldReading.getFluidTank(worldObj, x, y, z).fill(cu.getOpposite(), toSend, true);
					}
				}
			}
			copySend.amount -= toSend.amount;
			tank.drain(copySend.amount, true);
		}
	
	}

	public void produceWater()
	{
		int energy = this.provider.getEnergy();
		int can = energy / 10;
		if(can > 0)
		{
			int added = tank.fill(new FluidStack(FluidRegistry.WATER, can*1000), false);
			if(added <= 0)
			{
				return;
			}
			double add = 1000 / added;
			double adde = add / 100;
			tank.fill(new FluidStack(FluidRegistry.WATER, can*1000), true);
			if(adde > 0 && adde < 1)
			{
				this.provider.useEnergy(1, false);
				return;
			}

			this.provider.useEnergy(added, false);
		}
		
	}
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		return null;
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		return this.drain(from, resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		return tank.drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return false;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		return new FluidTankInfo[]{tank.getInfo()};
	}

	@Override
	public EnergyProvider getEnergyProvider(ForgeDirection side)
	{
		return provider;
	}

	@Override
	public PowerReceiver getPowerReceiver(ForgeDirection side)
	{
		return provider.getSaveBCPowerProvider();
	}

	@Override
	public void doWork(PowerHandler workProvider)
	{
	}

	@Override
	public World getWorld()
	{
		return worldObj;
	}


	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound)
	{
		super.readFromNBT(par1nbtTagCompound);
		this.tank.readFromNBT(par1nbtTagCompound);
		this.provider.readFromNBT(par1nbtTagCompound);
	}


	@Override
	public void writeToNBT(NBTTagCompound par1nbtTagCompound)
	{
		super.writeToNBT(par1nbtTagCompound);
		this.tank.writeToNBT(par1nbtTagCompound);
		this.provider.writeToNBT(par1nbtTagCompound);
	}


	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, nbt);
	}


	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
	{
		this.readFromNBT(pkt.data);
	}
	
	
}
