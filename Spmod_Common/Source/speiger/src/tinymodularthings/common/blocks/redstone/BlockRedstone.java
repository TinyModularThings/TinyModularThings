package speiger.src.tinymodularthings.common.blocks.redstone;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import speiger.src.spmodapi.common.blocks.cores.SpmodBlockContainerBase;

public class BlockRedstone extends SpmodBlockContainerBase
{

	public BlockRedstone(int par1)
	{
		super(par1, Material.circuits);
		this.setCreativeTab(CreativeTabs.tabFood);
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		switch(metadata)
		{
			case 0: return new Detector();
		}
		return null;
	}

	@Override
	public boolean canProvidePower()
	{
		return true;
	}
	
	
}
