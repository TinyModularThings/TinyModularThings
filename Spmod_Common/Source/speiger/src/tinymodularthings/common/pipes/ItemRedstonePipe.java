package speiger.src.tinymodularthings.common.pipes;

import speiger.src.tinymodularthings.common.handler.PipeIconHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import buildcraft.api.core.IIconProvider;
import buildcraft.core.network.TileNetworkData;
import buildcraft.transport.Pipe;
import buildcraft.transport.PipeTransportItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemRedstonePipe extends Pipe<PipeTransportItems>
{
	@TileNetworkData
	public boolean powering = false;
	
	public ItemRedstonePipe(int itemID)
	{
		super(new PipeTransportItems(), itemID);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIconProvider getIconProvider()
	{
		return PipeIconHandler.getIcons();
	}

	@Override
	public int getIconIndex(ForgeDirection direction)
	{
		if(powering)
		{
			return 5;
		}
		return 4;
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if(((PipeTransportItems)this.transport).items.size() == 0 && this.powering)
		{
			powering = false;
			this.updateNeighbors(true);
		}
		else if(((PipeTransportItems)this.transport).items.size() > 0 && !this.powering)
		{
			powering = true;
			this.updateNeighbors(true);
		}
	}
	
	@Override
	public int isPoweringTo(int side)
	{
		if (powering)
		{
			return 15;
		}
		return 0;
	}
	
	@Override
	public int isIndirectlyPoweringTo(int l)
	{
		return isPoweringTo(l);
	}
	
	
	@Override
	public void readFromNBT(NBTTagCompound data)
	{
		super.readFromNBT(data);
		powering = data.getBoolean("isFilled");
	}

	@Override
	public void writeToNBT(NBTTagCompound data)
	{
		super.writeToNBT(data);
		data.setBoolean("IsFilled", powering);
	}
	
}
