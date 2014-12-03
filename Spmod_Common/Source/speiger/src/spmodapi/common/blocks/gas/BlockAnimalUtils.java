package speiger.src.spmodapi.common.blocks.gas;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import speiger.src.spmodapi.common.blocks.cores.SpmodBlockContainerBase;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.util.TextureEngine;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAnimalUtils extends SpmodBlockContainerBase
{

	public BlockAnimalUtils(int par1)
	{
		super(par1, Material.iron);
		this.setCreativeTab(APIUtils.tabGas);
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

	@Override
	public void registerTextures(TextureEngine par1)
	{
		super.registerTextures(par1);
		String s = "AnimalChunkLoaderSide_";
		par1.registerTexture(this, "AnimalChunkLoaderTop",s+0, s+1, s+2, s+3, s+4, s+5, s+6, s+7, s+8, s+9);
	}

	
	
	
}
