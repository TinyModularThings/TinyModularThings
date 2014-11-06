package speiger.src.tinymodularthings.common.interfaces;

import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.tinymodularthings.common.utils.PipeInformation;

@Deprecated
public interface IBasicPipe
{
	public ForgeDirection getNextPipeDirection(World world, int x, int y, int z);
	
	public ForgeDirection getNextDirection(IBlockAccess world, int x, int y, int z);
	
	public int getEnergyTransferlimit(World world, int x, int y, int z);
	
	public int getLiquidTransferlimit(World world, int x, int y, int z);
	
	public int getItemTransferlimit(World world, int x, int y, int z);
	
	public PipeInformation getItemInforamtion(World world, int x, int y, int z);
	
	public PipeInformation getItemInformation(ItemStack par1);
	
	public PipeInformation getItemInforamtion(IBlockAccess world, int x, int y, int z);
	
	public World getNextWorld(World world, int x, int y, int z);
}
