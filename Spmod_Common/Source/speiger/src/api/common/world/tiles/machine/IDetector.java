package speiger.src.api.common.world.tiles.machine;

import java.util.List;

import net.minecraft.entity.Entity;
import speiger.src.api.common.world.blocks.BlockPosition;


public interface IDetector
{
	public int getTickRate();
	
	public void requestTickRateUpdate();
	
	/**
	 * @return the Block in front of him. You have to detect the Block Yourself (TileEntity and everything is supported)
	 */
	public BlockPosition getBlockInfront();
	
	/** if you turn the Argument to null it list every Entity it finds **/
	public List<Entity> getEntitiesInfront(Class par1);
	
	/**
	 * set the redstone signal Strenght on all sides to the number you say.
	 */
	public void setRedstoneSignal(int total);
	
	/**
	 * set the redstone Signal Strengh to the side what you chosed.
	 */
	public void setRedstoneSignal(int side, int total);
	
	public short getFacing();
}
