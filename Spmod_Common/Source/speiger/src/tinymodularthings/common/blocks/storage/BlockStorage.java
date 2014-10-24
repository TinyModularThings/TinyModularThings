package speiger.src.tinymodularthings.common.blocks.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import speiger.src.spmodapi.common.blocks.cores.SpmodBlockContainerBase;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.enums.EnumIDs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockStorage extends SpmodBlockContainerBase
{
	
	public BlockStorage(int par1)
	{
		super(par1, Material.iron);
		setHardness(4.0F);
		setCreativeTab(CreativeTabs.tabFood);
		MinecraftForge.setBlockHarvestLevel(this, 0, "pickaxe", 0);
		MinecraftForge.setBlockHarvestLevel(this, 0, "axe", 0);
		MinecraftForge.setBlockHarvestLevel(this, 1, "pickaxe", 0);
		MinecraftForge.setBlockHarvestLevel(this, 1, "axe", 0);
		MinecraftForge.setBlockHarvestLevel(this, 2, "pickaxe", 0);
		MinecraftForge.setBlockHarvestLevel(this, 2, "axe", 0);
		MinecraftForge.setBlockHarvestLevel(this, 3, "pickaxe", 0);
		MinecraftForge.setBlockHarvestLevel(this, 3, "axe", 0);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3)
	{
		
	}
	
	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return null;
	}
	
	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		try
		{
			switch (metadata)
			{
				case 0:
					return new TinyChest();
				case 1:
					return new TinyTank();
				case 2:
					return new AdvTinyChest();
				case 3:
					return new AdvTinyTank();
				default:
					return null;
			}
		}
		catch (Exception e)
		{
			TinyModularThings.log.print("Could not Load TileEntity for meta: " + metadata + " Please call the ModAuthor to let it fix");
			return null;
		}
	}
	
	@Override
	public int getRenderType()
	{
		return EnumIDs.StorageBlock.getId();
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
	
	@Override
	public boolean canProvidePower()
	{
		return true;
	}
	
}
