package speiger.src.api.common.world.tiles.interfaces;

import net.minecraft.item.ItemStack;
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
	 * Note: Tiers are only for Energy Production and has no longer something todo with,
	 * the Damage Calculation
	 */
	public int getTier();
	
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
	
	/**
	 * if you return true then he do not try to get data.
	 */
	public boolean isFake();
	
	/**
	 * Its adding speed to the Rotor. 1.0 is normal Speed.
	 * Note: It will not add the speed instant it will recalculate the speed after that.
	 * So you say its 5 Speed and the real Speed = 1. It will slowly go high up. So keep that in mind.
	 * Weight is also a thing that you have to keep in mind.
	 */
	
	public void setNewSpeed(float amount);
	
	/**
	 * @Returns the IC2 WindSpeed calaculated to 0 to 100 and let the weather flow into that.
	 * Also that will be reduced by the amount of blocks blocking it.
	 */
	public float getWindSpeed();
	
	/**
	 * @Returns the current Speed of the Rotor. Not the Requested!
	 */
	public float getActualSpeed();
}
