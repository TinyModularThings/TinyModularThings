package speiger.src.spmodapi.common.network.packets.base;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import speiger.src.api.common.data.packets.SpmodPacketHelper.SpmodPacket;
import speiger.src.api.common.data.packets.receivers.ITilePacketAcceptor;
import cpw.mods.fml.relauncher.Side;

public class BlankUpdatePacket extends PositionPacket
{
	public BlankUpdatePacket(ITilePacketAcceptor par1)
	{
		this((TileEntity)par1);
	}
	
	private BlankUpdatePacket(TileEntity par1)
	{
		super(par1.getWorldObj(), par1.xCoord, par1.yCoord, par1.zCoord);
	}
	
	
	@Override
	public void handlePacket(EntityPlayer par1, Side par2)
	{
		TileEntity tile = getTileEntity(par2);
		if(tile != null && tile instanceof ITilePacketAcceptor)
		{
			((ITilePacketAcceptor)tile).onSpmodPacket(new SpmodPacket(this, par1, par2));
		}
	}
	
}
