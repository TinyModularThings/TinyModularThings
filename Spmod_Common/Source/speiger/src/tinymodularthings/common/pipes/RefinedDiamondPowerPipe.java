package speiger.src.tinymodularthings.common.pipes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import buildcraft.BuildCraftTransport;
import buildcraft.api.core.IIconProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;
import buildcraft.transport.Pipe;
import buildcraft.transport.PipeIconProvider;
import buildcraft.transport.PipeTransportPower;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RefinedDiamondPowerPipe extends Pipe<PipeTransportPower>
{
	boolean[] requestPower = new boolean[ForgeDirection.VALID_DIRECTIONS.length];
	int[] prevRequestPower = new int[ForgeDirection.VALID_DIRECTIONS.length];
	
	public RefinedDiamondPowerPipe(int itemID)
	{
		super(new PipeTransportPower(), itemID);
		this.transport.initFromPipe(this.getClass());
		for (int i = 0; i < requestPower.length; i++)
		{
			requestPower[i] = true;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIconProvider getIconProvider()
	{
		return BuildCraftTransport.instance.pipeIconProvider;
	}
	
	@Override
	public int getIconIndex(ForgeDirection direction)
	{
		return PipeIconProvider.TYPE.PipePowerDiamond.ordinal();
	}
	
	@Override
	public boolean canPipeConnect(TileEntity tile, ForgeDirection side)
	{
		if (tile instanceof IPowerReceptor)
		{
			PowerReceiver power = ((IPowerReceptor) tile).getPowerReceiver(side.getOpposite());
			if (power != null)
			{
				return requestPower[side.ordinal()];
			}
		}
		
		return super.canPipeConnect(tile, side);
	}
	
	@Override
	public void updateEntity()
	{
		for (int i = 0; i < this.requestPower.length; i++)
		{
			ForgeDirection direction = ForgeDirection.getOrientation(i);
			int x = this.container.xCoord + direction.offsetX;
			int y = this.container.yCoord + direction.offsetY;
			int z = this.container.zCoord + direction.offsetZ;
			TileEntity tile = this.getWorld().getBlockTileEntity(x, y, z);
			if (tile != null && tile instanceof IPowerReceptor && ((IPowerReceptor) tile).getPowerReceiver(direction.getOpposite()) != null)
			{
				PowerReceiver power = ((IPowerReceptor) tile).getPowerReceiver(direction.getOpposite());
				power.update();
				power.receiveEnergy(Type.STORAGE, 0.01F, direction.getOpposite());
				if (requestPower(power) && !requestPower[direction.ordinal()])
				{
					requestPower[direction.ordinal()] = true;
					this.updateNeighbors(true);
				}
				else if (!requestPower(power) && requestPower[direction.ordinal()])
				{
					if (prevRequestPower[direction.ordinal()] >= 20)
					{
						requestPower[direction.ordinal()] = false;
						prevRequestPower[direction.ordinal()] = 0;
						this.updateNeighbors(true);
					}
					else
					{
						prevRequestPower[direction.ordinal()]++;
					}
				}
			}
		}
		super.updateEntity();
	}
	
	@Override
	public void writeToNBT(NBTTagCompound data)
	{
		super.writeToNBT(data);
		for (int i = 0; i < this.requestPower.length; i++)
		{
			data.setBoolean("Request_" + i, this.requestPower[i]);
			data.setInteger("PreRequest_" + i, this.prevRequestPower[i]);
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound data)
	{
		for (int i = 0; i < this.requestPower.length; i++)
		{
			requestPower[i] = data.getBoolean("Request_" + i);
			this.prevRequestPower[i] = data.getInteger("PreRequest_" + i);
		}
		super.readFromNBT(data);
	}
	
	public boolean requestPower(PowerReceiver par1)
	{
		return par1.getEnergyStored() < (par1.getMaxEnergyStored() - (par1.getMaxEnergyStored() / 100));
	}
	
}
