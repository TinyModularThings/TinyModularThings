package speiger.src.api.common.data.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public interface ISpmodPacket
{
	public void read(ByteBuf par1);
	
	public void write(ByteBuf par1);
	
	public void handlePacket(EntityPlayer par1);
}
