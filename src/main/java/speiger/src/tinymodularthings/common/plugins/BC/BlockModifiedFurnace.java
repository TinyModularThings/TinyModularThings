package speiger.src.tinymodularthings.common.plugins.BC;

import net.minecraft.block.BlockFurnace;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockModifiedFurnace extends BlockFurnace
{
	
	public BlockModifiedFurnace(boolean par2)
	{
		super(par2);
		if (!par2)
		{
			setHardness(3.5F);
			setStepSound(soundTypeStone);
			setBlockName("furnace");
			setCreativeTab(CreativeTabs.tabDecorations);
		}
		else
		{
			setHardness(3.5F);
			setStepSound(soundTypeStone);
			setLightLevel(0.875F);
			setBlockName("furnace");
		}
	}
	
	@Override
	public TileEntity createNewTileEntity(World par1World, int meta)
	{
		return new TileEntityModifiedFurnace();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2)
	{
		return super.getIcon(par1 == 2 ? 4 : par1, 3);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5)
	{
		int meta = par1iBlockAccess.getBlockMetadata(par2, par3, par4);
		return super.getIcon(par5, meta);
	}
	
}
