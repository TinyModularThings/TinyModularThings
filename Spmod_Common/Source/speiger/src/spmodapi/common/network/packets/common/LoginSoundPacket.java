package speiger.src.spmodapi.common.network.packets.common;

import java.io.DataInput;
import java.io.DataOutput;

import net.minecraft.entity.player.EntityPlayer;
import speiger.src.api.common.data.packets.ISpmodPacket;
import speiger.src.api.common.data.packets.SpmodPacketHelper.SpmodPacket;
import speiger.src.spmodapi.SpmodAPI;
import cpw.mods.fml.relauncher.Side;

public class LoginSoundPacket implements ISpmodPacket
{
	boolean answer;
	boolean privateSounds;
	String[] data;
	
	public LoginSoundPacket()
	{
	
	}
	
	public LoginSoundPacket(boolean par1, String...par2)
	{
		privateSounds = par1;
		data = par2;
		answer = true;
	}
	
	public LoginSoundPacket setRequest()
	{
		answer = false;
		privateSounds = false;
		data = new String[0];
		return this;
	}
	
	
	@Override
	public void readData(DataInput par1) throws Exception
	{
		answer = par1.readBoolean();
		privateSounds = par1.readBoolean();
		data = new String[par1.readInt()];
		for(int i = 0;i<data.length;i++)
		{
			data[i] = par1.readUTF();
		}
	}
	
	@Override
	public void writeData(DataOutput par1) throws Exception
	{
		par1.writeBoolean(answer);
		par1.writeBoolean(privateSounds);
		par1.writeInt(data.length);
		for(int i = 0;i<data.length;i++)
		{
			par1.writeUTF(data[i]);
		}
	}
	
	@Override
	public void handlePacket(EntityPlayer par1, Side par2)
	{
		SpmodAPI.manager.onPacket(new SpmodPacket(this, par1, par2));
	}
	
	public boolean isRequest()
	{
		return !answer;
	}

	public boolean isPrivate()
	{
		return privateSounds;
	}
	
	public String[] getData()
	{
		return data;
	}
	
}
