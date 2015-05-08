package speiger.src.spmodapi.common.network.packets.client;

import java.io.DataInput;
import java.io.DataOutput;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import speiger.src.api.common.data.packets.SpmodPacketHelper.SpmodPacket;
import speiger.src.spmodapi.common.blocks.utils.InventoryAccesser;
import speiger.src.spmodapi.common.network.packets.base.PositionPacket;
import cpw.mods.fml.relauncher.Side;

public class AccesserPacket extends PositionPacket
{
	int task;
	
	int target;
	String text = "";
	
	public AccesserPacket()
	{
	}
	
	public AccesserPacket(InventoryAccesser par1)
	{
		super(par1.getWorldObj(), par1.xCoord, par1.yCoord, par1.zCoord);
	}
	
	@Override
	public void readData(DataInput par1) throws Exception
	{
		super.readData(par1);
		task = par1.readInt();
		target = par1.readInt();
		text = par1.readUTF();
	}

	@Override
	public void writeData(DataOutput par1) throws Exception
	{
		super.writeData(par1);
		par1.writeInt(task);
		par1.writeInt(target);
		par1.writeUTF(text);
	}

	@Override
	public void handlePacket(EntityPlayer par1, Side par2)
	{
		TileEntity tile = getTileEntity(par2);
		if(tile != null && tile instanceof InventoryAccesser)
		{
			((InventoryAccesser)tile).onSpmodPacket(new SpmodPacket(this, par1, par2));
		}
	}
	
	public int getTask()
	{
		return task;
	}
	
	public int getTarget()
	{
		return target;
	}
	
	public String getText()
	{
		return text;
	}
	
	public void setNameTask(int par1, String par2)
	{
		task = 0;
		target = par1;
		text = par2;
	}
	
	public void removeNameTask(int targetID)
	{
		task = 1;
		target = targetID;
	}
	
	public void openTask(int targetID)
	{
		task = 2;
		target = targetID;
	}
}
