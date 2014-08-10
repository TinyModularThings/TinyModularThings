package speiger.src.tinymodularthings.common.pipes;

import net.minecraft.item.Item;
import net.minecraftforge.common.util.ForgeDirection;
import speiger.src.tinymodularthings.common.handler.PipeIconHandler;
import buildcraft.api.core.IIconProvider;
import buildcraft.transport.Pipe;
import buildcraft.transport.PipeTransportPower;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PipeEmeraldPower extends Pipe<PipeTransportPower>
{

	public PipeEmeraldPower(Item itemID)
	{
		super(new PipeTransportPower(), itemID);
		this.transport.initFromPipe(this.getClass());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIconProvider getIconProvider()
	{
		return new PipeIconHandler();
	}

	@Override
	public int getIconIndex(ForgeDirection direction)
	{
		return 0;
	}
	
}
