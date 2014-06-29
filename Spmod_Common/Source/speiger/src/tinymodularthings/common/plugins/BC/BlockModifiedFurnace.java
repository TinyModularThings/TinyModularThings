package speiger.src.tinymodularthings.common.plugins.BC;

import net.minecraft.block.BlockFurnace;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockModifiedFurnace extends BlockFurnace
{
	
	public BlockModifiedFurnace(boolean par2)
	{
		super(par2 ? 62 : 61, par2);
		if (!par2)
		{
			setHardness(3.5F);
			setStepSound(soundStoneFootstep);
			setUnlocalizedName("furnace");
			setCreativeTab(CreativeTabs.tabDecorations);
		}
		else
		{
			setHardness(3.5F);
			setStepSound(soundStoneFootstep);
			setLightValue(0.875F);
			setUnlocalizedName("furnace");
		}
	}
	
	@Override
	public TileEntity createNewTileEntity(World par1World)
	{
		return new TileEntityModifiedFurnace();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int par1, int par2)
	{
		return super.getIcon(par1 == 2 ? 4 : par1, 3);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5)
	{
		int meta = par1iBlockAccess.getBlockMetadata(par2, par3, par4);
		return super.getIcon(par5, meta);
	}
	
}
