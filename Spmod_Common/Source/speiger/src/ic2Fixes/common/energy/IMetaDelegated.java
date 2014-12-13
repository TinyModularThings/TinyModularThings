package speiger.src.ic2Fixes.common.energy;

import ic2.api.energy.tile.IEnergyTile;

import java.util.List;

import net.minecraft.tileentity.TileEntity;

public interface IMetaDelegated extends IEnergyTile
{
	public abstract List<TileEntity> getSubTiles();
}
