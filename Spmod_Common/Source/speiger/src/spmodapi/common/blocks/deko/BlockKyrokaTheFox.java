package speiger.src.spmodapi.common.blocks.deko;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import speiger.src.api.util.WorldReading;
import speiger.src.spmodapi.common.blocks.cores.SpmodBlockContainerBase;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.tile.AdvTile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockKyrokaTheFox extends SpmodBlockContainerBase
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
    
	
}
