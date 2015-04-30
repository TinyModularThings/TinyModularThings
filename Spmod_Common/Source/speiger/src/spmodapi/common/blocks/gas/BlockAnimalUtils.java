package speiger.src.spmodapi.common.blocks.gas;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import speiger.src.spmodapi.common.blocks.cores.SpmodBlockContainerBase;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.TileIconMaker;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAnimalUtils extends SpmodBlockContainerBase
{

	public BlockAnimalUtils(int par1)
	{
		super(par1, Material.iron);
		this.setCreativeTab(APIUtils.tabGas);
		MinecraftForge.setBlockHarvestLevel(this, 0, "pickaxe", 1);
		MinecraftForge.setBlockHarvestLevel(this, 1, "pickaxe", 1);
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		switch(metadata)
		{
			case 0: return new BasicAnimalChunkLoader();
			case 1: return new Ventil();
			default: return null;
		}
	}

	@Override
	public boolean hasTileDrops(int meta)
	{
		return true;
	}

	@Override
	public void registerTextures(TextureEngine par1)
	{
		super.registerTextures(par1);
		String s = "AnimalChunkLoaderSide_";
		par1.registerTexture(this, "AnimalChunkLoaderTop",s+0, s+1, s+2, s+3, s+4, s+5, s+6, s+7, s+8, s+9);
		TileIconMaker.registerIcon(this, par1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for(int i = 0;i<2;i++)
		{
			par3List.add(new ItemStack(par1, 1, i));
		}
	}

	
	
	
}
