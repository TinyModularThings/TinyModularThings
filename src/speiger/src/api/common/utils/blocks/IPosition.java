package speiger.src.api.common.utils.blocks;

import net.minecraft.world.World;

public interface IPosition extends ICoord
{
	public World getWorld();
	
	public ICoord toICoords();
}
