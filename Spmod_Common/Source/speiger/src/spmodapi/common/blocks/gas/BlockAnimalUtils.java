package speiger.src.spmodapi.common.blocks.gas;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import speiger.src.spmodapi.common.blocks.cores.SpmodBlockContainerBase;

public class BlockAnimalUtils extends SpmodBlockContainerBase
{

	public BlockAnimalUtils(int par1)
	{
		super(par1, Material.iron);
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		switch(metadata)
		{
			case 0: return new BasicAnimalChunkLoader();
			default: return null;
		}
	}

	@Override
	public boolean hasTileDrops(int meta)
	{
		switch(meta)
		{
			case 0: return true;
		}
		return super.hasTileDrops(meta);
	}
	
	
	
}
