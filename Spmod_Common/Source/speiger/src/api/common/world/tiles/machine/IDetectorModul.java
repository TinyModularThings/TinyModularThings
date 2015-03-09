package speiger.src.api.common.world.tiles.machine;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;


public interface IDetectorModul
{

	public void onUnloading(IDetector detector);

	public int getTickRate(IDetector detector);

	public boolean doesHaveTileEntityTick(IDetector detector);
	
	public void writeToNBT(NBTTagCompound par1);
	
	public void readFromNBT(NBTTagCompound par1);
	
	public void onTileEntityTick(IDetector par1);
	
	public void onBlockUpdate(IDetector par1);
	
	public void addItemInformation(List par1);
}
