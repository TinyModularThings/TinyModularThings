package speiger.src.tinymodularthings.common.network.packets.client;

import java.io.DataInput;
import java.io.DataOutput;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import speiger.src.api.common.data.packets.SpmodPacketHelper.SpmodPacket;
import speiger.src.api.common.data.packets.receivers.ITilePacketAcceptor;
import speiger.src.spmodapi.common.network.packets.base.PositionPacket;
import cpw.mods.fml.relauncher.Side;

public class SlotClickPacket extends PositionPacket
{
	int slotID;
	
	public SlotClickPacket()
	{
	}
	
	public SlotClickPacket(TileEntity par1, int par2)
	{
		super(par1.getWorldObj(), par1.xCoord, par1.yCoord, par1.zCoord);
		slotID = par2;
	}
	
	@Override
	public void readData(DataInput par1) throws Exception
	{
		super.readData(par1);
		slotID = par1.readInt();
	}

	@Override
	public void writeData(DataOutput par1) throws Exception
	{
		super.writeData(par1);
		par1.writeInt(slotID);
	}

	@Override
	public void handlePacket(EntityPlayer par1, Side par2)
	{
		TileEntity tile = getTileEntity(par2);
		if(tile != null && tile instanceof ITilePacketAcceptor)
		{
			ITilePacketAcceptor receiver = (ITilePacketAcceptor)tile;
			receiver.onSpmodPacket(new SpmodPacket(this, par1, par2));
		}
	}
	
	public int getSlot()
	{
		return slotID;
	}
}