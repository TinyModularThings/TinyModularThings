package speiger.src.tinymodularthings.common.network.packets.client;

import java.io.DataInput;
import java.io.DataOutput;

import net.minecraft.entity.player.EntityPlayer;
import speiger.src.api.common.data.packets.SpmodPacketHelper.SpmodPacket;
import speiger.src.spmodapi.common.items.core.ItemInventory;
import speiger.src.spmodapi.common.network.packets.base.ItemInventoryPacket;
import speiger.src.tinymodularthings.common.items.energy.BatteryInventory;
import cpw.mods.fml.relauncher.Side;

public class BatteryPacket extends ItemInventoryPacket
{
	public int key;
	public float value;
	
	public BatteryPacket()
	{
	}
	
	public BatteryPacket(BatteryInventory par1)
	{
		super(par1);
	}
	
	public void setData(int id)
	{
		key = id;
		value = 0.0F;
	}
	
	public void setValue(float par1)
	{
		value = par1;
	}
	
	@Override
	public void readData(DataInput par1) throws Exception
	{
		super.readData(par1);
		key = par1.readInt();
		value = par1.readFloat();
	}

	@Override
	public void writeData(DataOutput par1) throws Exception
	{
		super.writeData(par1);
		par1.writeInt(key);
		par1.writeFloat(value);
	}

	public void setData(int id, float result)
	{
		key = id;
		value = result;
	}

	@Override
	public void handlePacket(EntityPlayer par1, Side par2)
	{
		ItemInventory inv = getInventory(par2);
		if(inv != null)
		{
			inv.onSpmodPacket(new SpmodPacket(this, par1, par2));
		}
	}
	
}
