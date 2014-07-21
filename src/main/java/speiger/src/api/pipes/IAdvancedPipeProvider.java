package speiger.src.api.pipes;

import net.minecraftforge.common.util.ForgeDirection;

public interface IAdvancedPipeProvider extends IBasicPipeProvider
{
	boolean canConnect(ForgeDirection direction);
}
