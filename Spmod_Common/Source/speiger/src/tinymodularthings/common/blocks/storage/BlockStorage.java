package speiger.src.tinymodularthings.common.blocks.storage;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.client.render.core.BlockRendererSpmodCore.BlockRendererHelper;
import speiger.src.spmodapi.common.blocks.cores.SpmodBlockContainerBase;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.tinymodularthings.common.enums.EnumIDs;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockStorage extends SpmodBlockContainerBase
{
	
	public BlockStorage(int par1)
	{
		super(par1, Material.iron);
		setHardness(4.0F);
		setCreativeTab(CreativeTabs.tabFood);
		this.setIgnoreRighClick();
		this.dissableDrops();
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
	public boolean hasTileDrops(int meta)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		switch (metadata)
		{
			case 0: return new TinyChest();
			case 1: return new TinyTank();
			case 2: return new AdvTinyChest();
			case 3: return new AdvTinyTank();
			default: return null;
		}
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
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresRenderer(int meta)
	{
		switch(meta)
		{
			case 1:
			case 3: return true;
		}
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultibleRenderPasses(int meta)
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderPasses(int meta)
	{
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onRender(IBlockAccess world, int x, int y, int z, RenderBlocks render, BlockStack block, int renderPass)
	{
		AdvTile tile = getAdvTile(world, x, y, z);
		if(tile != null && tile instanceof TinyTank)
		{
			TinyTank tank = (TinyTank)tile;
			FluidStack stack = tank.tank.getFluid();
			if(stack != null && stack.getFluid() != null && tank.renderLiquid() && !tank.renderTank())
			{
				Fluid fluid = stack.getFluid();
				Icon icon = fluid.getIcon(stack);
				double amount = ((double)stack.amount / (double)tank.tank.getCapacity()) * 75;
				amount /= 100;
				
				render.setRenderBounds(0.125, 0.125, 0.125, 0.875, 0.125+amount, 0.875);
				render.setOverrideBlockTexture(icon);
				render.renderAllFaces = true;
				render.renderStandardBlock(this, x, y, z);
				render.clearOverrideBlockTexture();
				render.renderAllFaces = false;
			}
			
			if(tank.renderTank())
			{
				render.renderStandardBlock(this, x, y, z);
			}
		}
	}

	@Override
	public boolean dissableRendering(int meta)
	{
		return true;
	}

	@Override
	public boolean requiresRender()
	{
		return true;
	}

	@Override
	public boolean renderItemBlock(int meta)
	{
		return false;
	}

	@Override
	public boolean renderItemBlockBasic(int meta)
	{
		return false;
	}

	@Override
	public float[] getBoundingBoxes(int meta)
	{
		return null;
	}

	@Override
	public float[] getXYZForItemRenderer(ItemRenderType type, int meta)
	{
		return null;
	}

	@Override
	public int getItemRenderPasses(int meta)
	{
		return 0;
	}

	@Override
	public void onItemRendering(BlockRendererHelper render, ItemRenderType type, BlockStack stack, int renderPass, float x, float y, float z, Object... data)
	{
		
	}
	
}
