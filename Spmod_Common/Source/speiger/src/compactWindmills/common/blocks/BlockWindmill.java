package speiger.src.compactWindmills.common.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.compactWindmills.common.utils.WindmillType;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.enums.EnumGuiIDs;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.TileIconMaker;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockWindmill extends BlockContainer
{
	
	public BlockWindmill(int par1)
	{
		super(par1, Material.iron);
		setHardness(2.0F);
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return null;
	}
	
	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		return new WindMill(WindmillType.getValidValues()[metadata]);
	}

	@Override
	public int quantityDropped(Random par1Random)
	{
		return 1;
	}
	
	

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int par1, int par2)
	{
		//Accessing simply a Fake TileEntity that does not getUpdated, because its not in the World, That saves a lot of coding because i need to make the textures only 1 time in the TileEntity :P
		WindMill mill = (WindMill) TileIconMaker.getIconMaker().getTileEntityFromClass(WindMill.class);
		if(mill != null)
		{
			mill.type = WindmillType.values()[par2];
			mill.setFacing((short) 3);
			return TileIconMaker.getIconMaker().getIconSafe(mill.getIconFromSideAndMetadata(par1, 0));
		}
		return TileIconMaker.getIconMaker().getIconSafe(null);
	}
	
	

	

	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5, ItemStack par6ItemStack)
	{
		int direction = 2;
		int facing = MathHelper.floor_double(par5.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
		
		if (facing == 0)
		{
			direction = ForgeDirection.NORTH.ordinal();
		}
		else if (facing == 1)
		{
			direction = ForgeDirection.EAST.ordinal();
		}
		else if (facing == 2)
		{
			direction = ForgeDirection.SOUTH.ordinal();
		}
		else if (facing == 3)
		{
			direction = ForgeDirection.WEST.ordinal();
		}
		
		TileEntity tile = par1World.getBlockTileEntity(par2, par3, par4);
		if (tile != null && tile instanceof AdvTile)
		{
			((AdvTile) tile).onPlaced(direction);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for(WindmillType type : WindmillType.getValidValues())
		{
			par3List.add(new ItemStack(par1, 1, type.ordinal()));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5)
	{
		TileEntity tile = par1iBlockAccess.getBlockTileEntity(par2, par3, par4);
		if(tile != null && tile instanceof AdvTile)
		{
			return ((AdvTile)tile).getIconFromSideAndMetadata(par5, 0);
		}
		return TileIconMaker.getIconMaker().getIconSafe(null);
	}

	@Override
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
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
	{
		if(par5EntityPlayer.isSneaking())
		{
			return false;
		}
		
		if(!par1World.isRemote)
		{
			TileEntity tile = par1World.getBlockTileEntity(par2, par3, par4);
			if(tile != null && tile instanceof AdvTile)
			{
				par5EntityPlayer.openGui(SpmodAPI.instance, EnumGuiIDs.Tiles.getID(), par1World, par2, par3, par4);
				return true;
			}
		}
		return true;
	}

	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
	{
		TileEntity tile = par1World.getBlockTileEntity(par2, par3, par4);
		if(tile != null && tile instanceof AdvTile)
		{
			((AdvTile)tile).onBreaking();
		}
		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}
}
