package speiger.src.tinymodularthings.common.blocks.redstone.detector;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import speiger.src.api.common.world.blocks.BlockPosition;
import speiger.src.api.common.world.tiles.machine.IDetector;
import speiger.src.api.common.world.tiles.machine.IDetectorModul;

public class DetectorBase implements IDetectorModul
{
	
	@Override
	public void onUnloading(IDetector detector)
	{
		
	}
	
	@Override
	public int getTickRate(IDetector detector)
	{
		return 0;
	}
	
	@Override
	public boolean doesHaveTileEntityTick(IDetector detector)
	{
		return true;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound par1)
	{
	}
	
	@Override
	public void readFromNBT(NBTTagCompound par1)
	{
		
	}
	
	@Override
	public void onTileEntityTick(IDetector par1)
	{
		List<Entity> entities = par1.getEntitiesInfront(null);
		if(entities.size() > 0)
		{
			par1.setRedstoneSignal(15);
			return;
		}
		BlockPosition pos = par1.getBlockInfront();
		if(!pos.isAirBlock())
		{
			par1.setRedstoneSignal(15);
			return;
		}
		par1.setRedstoneSignal(0);
	}
	
	@Override
	public void onBlockUpdate(IDetector par1)
	{
		
	}

	@Override
	public void addItemInformation(List par1)
	{
		par1.add("Detects if it something infront of him");
	}	
}
