package speiger.src.spmodapi.common.blocks.deko;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import speiger.src.spmodapi.common.blocks.cores.SpmodBlockContainerBase;

public class BlockMutliPlate extends SpmodBlockContainerBase
{
	
	public BlockMutliPlate(int par1)
	{
		super(par1, Material.wood);
		this.dissableDrops();
		this.setHardness(4F);
		this.setResistance(4F);
		this.setIgnoreRightClick(0);
		MinecraftForge.setBlockHarvestLevel(this, "axe", 0);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new MultiPlate();
	}


	@Override
	public boolean hasTileDrops(int meta)
	{
		return true;
	}

	@Override
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
	{
		return false;
	}

	

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public int getRenderType()
	{
		return -1;
	}
	
}
