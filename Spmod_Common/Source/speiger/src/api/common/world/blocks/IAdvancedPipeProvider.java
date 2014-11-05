package speiger.src.api.common.world.blocks;

import net.minecraftforge.common.ForgeDirection;

public interface IAdvancedPipeProvider extends IBasicPipeProvider
{
	boolean canConnect(ForgeDirection direction);
}
