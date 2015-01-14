package speiger.src.tinymodularthings.common.pipes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.tinymodularthings.common.handler.PipeIconHandler;
import buildcraft.api.core.IIconProvider;
import buildcraft.api.power.*;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;
import buildcraft.api.transport.IPipeTile;
import buildcraft.transport.IPipeTransportPowerHook;
import buildcraft.transport.Pipe;
import buildcraft.transport.PipeTransportPower;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PipeEmeraldExtractionPower extends Pipe<PipeTransportPower>
		implements IPowerReceptor, IPipeTransportPowerHook
{
	private PowerHandler powerHandler;
	private boolean[] powerSources = new boolean[6];
	private boolean full;
	
	public PipeEmeraldExtractionPower(int itemID)
	{
		super(new PipeTransportPower(), itemID);
		powerHandler = new PowerHandler(this, Type.PIPE);
		this.initPowerProvider();
		this.transport.initFromPipe(this.getClass());
	}
	
	private void initPowerProvider()
	{
		powerHandler.configure(2, 2500, 1, Short.MAX_VALUE);
		powerHandler.configurePowerPerdition(1, 10);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIconProvider getIconProvider()
	{
		return PipeIconHandler.getIcons();
	}
	
	@Override
	public boolean canPipeConnect(TileEntity tile, ForgeDirection side)
	{
		if (container.pipe instanceof PipeEmeraldExtractionPower && tile instanceof IPowerEmitter)
		{
			IPowerEmitter emitter = (IPowerEmitter) tile;
			if (emitter.canEmitPowerFrom(side.getOpposite()))
				return true;
		}
		return super.canPipeConnect(tile, side);
	}
	
	@Override
	public int getIconIndex(ForgeDirection direction)
	{
		if (direction == ForgeDirection.UNKNOWN)
			return 0;
		else
		{
			
			if (this.powerSources[direction.ordinal()])
				return 1;
			else
				return 0;
		}
	}
	
	@Override
	public PowerReceiver getPowerReceiver(ForgeDirection side)
	{
		return powerHandler.getPowerReceiver();
	}
	
	@Override
	public void doWork(PowerHandler workProvider)
	{
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if (container.worldObj.isRemote)
			return;
		
		if (powerHandler.getEnergyStored() <= 0)
			return;
		
		int sources = 0;
		for (ForgeDirection o : ForgeDirection.VALID_DIRECTIONS)
		{
			if (!container.isPipeConnected(o))
			{
				powerSources[o.ordinal()] = false;
				continue;
			}
			if (powerHandler.isPowerSource(o))
			{
				powerSources[o.ordinal()] = true;
			}
			if (powerSources[o.ordinal()])
			{
				sources++;
			}
		}
		
		if (sources <= 0)
		{
			powerHandler.useEnergy(5, 5, true);
			return;
		}
		
		float energyToRemove;
		
		if (powerHandler.getEnergyStored() > 40)
		{
			energyToRemove = powerHandler.getEnergyStored() / 20 + 4;
		}
		else if (powerHandler.getEnergyStored() > 10)
		{
			energyToRemove = powerHandler.getEnergyStored() / 10;
		}
		else
		{
			energyToRemove = 1;
		}
		energyToRemove /= (float) sources;
		
		for (ForgeDirection o : ForgeDirection.VALID_DIRECTIONS)
		{
			if (!powerSources[o.ordinal()])
				continue;
			
			float energyUsable = powerHandler.useEnergy(0, energyToRemove, false);
			
			float energySent = transport.receiveEnergy(o, energyUsable);
			if (energySent > 0)
			{
				powerHandler.useEnergy(0, energySent, true);
			}
		}
	}
	
	public boolean requestsPower()
	{
		if (full)
		{
			boolean request = powerHandler.getEnergyStored() < 100;
			if (request)
			{
				full = false;
			}
			return request;
		}
		full = powerHandler.getEnergyStored() >= powerHandler.getMaxEnergyStored() - 10;
		return !full;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound data)
	{
		super.writeToNBT(data);
		powerHandler.writeToNBT(data);
		for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++)
		{
			data.setBoolean("powerSources[" + i + "]", powerSources[i]);
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound data)
	{
		super.readFromNBT(data);
		powerHandler.readFromNBT(data);
		initPowerProvider();
		for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++)
		{
			powerSources[i] = data.getBoolean("powerSources[" + i + "]");
		}
	}
	
	@Override
	public float receiveEnergy(ForgeDirection from, float val)
	{
		return -1;
	}
	
	@Override
	public float requestEnergy(ForgeDirection from, float amount)
	{
		if (container.getTile(from) instanceof IPipeTile)
		{
			return amount;
		}
		return 0;
	}
	
}
