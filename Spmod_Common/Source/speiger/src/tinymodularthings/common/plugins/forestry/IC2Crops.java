package speiger.src.tinymodularthings.common.plugins.forestry;

import ic2.api.crops.CropCard;
import ic2.api.crops.Crops;
import ic2.api.crops.ICropTile;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import forestry.api.farming.ICrop;
import forestry.api.farming.IFarmable;

public class IC2Crops implements IFarmable
{
	
	@Override
	public ICrop getCropAt(World arg0, int arg1, int arg2, int arg3)
	{
		TileEntity tile = arg0.getBlockTileEntity(arg1, arg2, arg3);
		if (tile != null && tile instanceof ICropTile)
		{
			ICropHelper crop = new ICropHelper((ICropTile) tile);
			if (crop.isLoaded() && crop.isFinish())
			{
				return crop;
			}
		}
		return null;
	}
	
	@Override
	public boolean isGermling(ItemStack arg0)
	{
		return false;
	}
	
	@Override
	public boolean isSaplingAt(World arg0, int arg1, int arg2, int arg3)
	{
		ICrop crop = getCropAt(arg0, arg1, arg2, arg3);
		if (crop instanceof ICropHelper)
		{
			ICropHelper crops = (ICropHelper) crop;
			if (crops.isLoaded())
			{
				return crops.isFinish();
			}
		}
		return false;
	}
	
	@Override
	public boolean isWindfall(ItemStack arg0)
	{
		return true;
	}
	
	@Override
	public boolean plantSaplingAt(ItemStack arg0, World arg1, int arg2, int arg3, int arg4)
	{
		return false;
	}
	
	public static class ICropHelper implements ICrop
	{
		ICropTile tile;
		
		public ICropHelper(ICropTile par1)
		{
			tile = par1;
		}
		
		public void trimBack()
		{
			tile.setSize(getCropCard().getSizeAfterHarvest(tile));
		}
		
		public boolean isLoaded()
		{
			return tile.getID() != -1;
		}
		
		public CropCard getCropCard()
		{
			return Crops.instance.getCropList()[tile.getID()];
		}
		
		public boolean isFinish()
		{
			return getCropCard().canBeHarvested(tile);
		}
		
		@Override
		public Collection<ItemStack> harvest()
		{
			Collection<ItemStack> collect = new ArrayList<ItemStack>();
			collect.add(getCropCard().getGain(tile));
			trimBack();
			return collect;
		}
		
	}
	
}
