package speiger.src.api.common.data.packets;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;


public class SpmodPacketHelper
{
	public static ISpmodPacketHandler handler;
	
	
	
	/**
	 * This class is just for Packet Receiver Classes that they have access to every required Data.
	 * If you do not make a packet that has only 1 use...
	 */
	public static class SpmodPacket
	{
		final ISpmodPacket packet;
		final EntityPlayer player;
		final Side side;
		
		public SpmodPacket(ISpmodPacket par1, EntityPlayer par2, Side par3)
		{
			packet = par1;
			player = par2;
			side = par3;
		}
		
		public ISpmodPacket getPacket()
		{
			return packet;
		}
		
		public EntityPlayer getPlayer()
		{
			return player;
		}
		
		public Side getSide()
		{
			return side;
		}
	}
}
