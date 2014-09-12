package speiger.src.api.tiles;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;

public interface IWindmill
{
	/**
	 * @Return the Windmill Itself. Need a cast but its only for Addon Friendly
	 *         Stuff.
	 */
	public TileEntity getWindmill();
	
	/**
	 * @Return Not really Required but it returns the ChunkCoordinates of the
	 *         Windmill
	 */
	public ChunkCoordinates getChunkCoordinates();
	
	/**
	 * @Return Tier of the Windmill
	 */
	public int getTier();
	
	/**
	 * @Return the Checkradius of the Windmill
	 */
	public int getRadius();
	
	/**
	 * @Info this function get called if the Rotor is to much damaged or
	 *       something. It makes the is away. Only Reason calling it is at
	 *       Damage Rotor.
	 */
	public void distroyRotor();
	
	/**
	 * @Return the Facing of the Rotor
	 */
	public short getFacing();
}
