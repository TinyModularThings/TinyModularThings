package speiger.src.api.common.data.packets;

import java.io.DataInput;
import java.io.DataOutput;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;

/**
 * 
 * @author Speiger
 *
 * The class that extends this interface require an Empty Constructure. If not the game will crash.
 */
public interface ISpmodPacket
{
	/**
	 * reading your data from the Packet that got Received
	 */
	public void readData(DataInput par1) throws Exception;
	
	public void writeData(DataOutput par1) throws Exception;
	
	public void handlePacket(EntityPlayer par1, Side par2);
}
