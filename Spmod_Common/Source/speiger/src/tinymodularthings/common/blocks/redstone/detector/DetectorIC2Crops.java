package speiger.src.tinymodularthings.common.blocks.redstone.detector;

import ic2.api.crops.CropCard;
import ic2.api.crops.Crops;
import ic2.api.crops.ICropTile;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import speiger.src.api.common.world.tiles.machine.IDetector;
import speiger.src.api.common.world.tiles.machine.IDetectorModul;

public class DetectorIC2Crops implements IDetectorModul
{
	
	@Override
	public void onUnloading(IDetector detector)
	{
		
	}
	
	@Override
	public int getTickRate(IDetector detector)
	{
		return 20;
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
		TileEntity tile = par1.getBlockInfront().getTileEntity();
		if(tile != null && tile instanceof ICropTile)
		{
			ICropTile crop = (ICropTile)tile;
			if(crop.getID() > 0)
			{
				CropCard card = Crops.instance.getCropList()[crop.getID()];
				if(card != null && card.canBeHarvested(crop))
				{
					par1.setRedstoneSignal(15);
					return;
				}
			}
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
		par1.add("Detects if IC2 Crops are Fully Grown");
	}
	
}
