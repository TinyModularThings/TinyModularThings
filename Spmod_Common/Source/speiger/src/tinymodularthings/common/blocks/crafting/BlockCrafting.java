package speiger.src.tinymodularthings.common.blocks.crafting;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import speiger.src.spmodapi.common.blocks.cores.SpmodBlockContainerBase;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.client.gui.crafting.GuiAdvCrating;
import speiger.src.tinymodularthings.common.enums.EnumIDs;
import speiger.src.tinymodularthings.common.interfaces.IBlockGui;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCrafting extends SpmodBlockContainerBase implements IBlockGui
{

	public BlockCrafting(int par1)
	{
		super(par1, Material.piston);
		this.setCreativeTab(CreativeTabs.tabFood);
		this.setHardness(2F);
	}
	
	
	

	@Override
	public float getBlockHardness(World par1World, int par2, int par3, int par4)
	{
		int meta = par1World.getBlockMetadata(par2, par3, par4);
		if(meta == 0)
		{
			return 1.0F;
		}
		
		return 2.0F;
	}

	@Override
	public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ)
	{
		int meta = world.getBlockMetadata(x, y, z);
		if(meta == 0)
		{
			return 2.0F;
		}
		
		return 4.0F;
	}

	@Override
	public TileEntity createTileEntity(World world, int meta)
	{
		if(meta == 1)
		{
			return new OreCrafter();
		}
		return null;
	}



	@Override
	public boolean onBlockActivated(World par1, int par2, int par3, int par4, EntityPlayer par5, int par6, float par7, float par8, float par9)
	{
		if(par5.isSneaking())
		{
			return false;
		}
		if(!par1.isRemote)
		{
			int meta = par1.getBlockMetadata(par2, par3, par4);
			if(meta == 0)
			{
				par5.openGui(TinyModularThings.instance, EnumIDs.BlockGui.getId(), par1, par2, par3, par4);
				return true;
			}
			TileEntity tile = par1.getBlockTileEntity(par2, par3, par4);
			if(tile != null && tile instanceof AdvTile)
			{
				par5.openGui(TinyModularThings.instance, EnumIDs.ADVTiles.getId(), par1, par2, par3, par4);
				return true;
			}
		}
		return true;
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

	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer getGui(int meta, InventoryPlayer par1)
	{
		if(meta == 0)
		{
			return new GuiAdvCrating(this, meta, par1);
		}
		return null;
	}

	@Override
	public Container getInventory(int meta, InventoryPlayer par1)
	{
		if(meta == 0)
		{
			return new ContainerAdvCrafting(par1);
		}
		return null;
	}
	
	
	
	
	
}
