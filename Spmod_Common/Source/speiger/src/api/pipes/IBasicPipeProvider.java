package speiger.src.api.pipes;

import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;

public interface IBasicPipeProvider
{
	public ForgeDirection getConnectionSide(IBlockAccess par0, int x, int y, int z);
}
