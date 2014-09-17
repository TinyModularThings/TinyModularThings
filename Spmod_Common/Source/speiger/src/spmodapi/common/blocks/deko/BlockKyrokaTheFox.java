package speiger.src.spmodapi.common.blocks.deko;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import speiger.src.api.util.WorldReading;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.tile.AdvTile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockKyrokaTheFox extends BlockContainer
{

	public BlockKyrokaTheFox(int par1)
	{
		super(par1, Material.iron);
		this.setCreativeTab(APIUtils.tabCrafing);
		this.setHardness(0.5F);
		this.setResistance(10000F);
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new KyrokaTheFox(world);
	}
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}
	
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
    
	@SideOnly(Side.CLIENT)
	@Override
    public int getRenderType()
	{
		return -1;
	}
	
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return this.blockID;
    }

	@Override
	public int damageDropped(int par1)
	{
		return par1;
	}

	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack)
	{
		int rotation = WorldReading.getLookingDirectionFromEnitty(par5EntityLivingBase);
		TileEntity tile = par1World.getBlockTileEntity(par2, par3, par4);
		if(tile != null)
		{
			if(tile instanceof AdvTile)
			{
				((AdvTile)tile).onPlaced(rotation);
			}
		}
	}
    
	
}
