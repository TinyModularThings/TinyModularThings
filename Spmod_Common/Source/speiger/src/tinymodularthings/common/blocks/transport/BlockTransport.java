package speiger.src.tinymodularthings.common.blocks.transport;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.client.render.core.BlockRendererSpmodCore.BlockRendererHelper;
import speiger.src.spmodapi.common.blocks.cores.SpmodBlockContainerBase;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.TileIconMaker;
import speiger.src.tinymodularthings.common.enums.EnumIDs;
import speiger.src.tinymodularthings.common.utils.HopperType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTransport extends SpmodBlockContainerBase
{
	
	public BlockTransport(int par1)
	{
		super(par1, Material.iron);
		setCreativeTab(CreativeTabs.tabFood);
		this.setHardness(4.0F);
		this.setResistance(4.0F);
		this.dissableDrops();
	}
	
	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return null;
	}
	

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3)
	{
		par3.add(new ItemStack(par1, 1, 0));
		par3.add(new ItemStack(par1, 1, 4));
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
			case 0: return new EnderChestReader();
			case 1: return new MultiStructureItemInterface();
			case 2: return new MultiStructureFluidInterface();
			case 3: return new MultiStructureEnergyInterface();
			case 4: return new AdvancedEnderChestReader();
				
			case 10: return new TinyHopper(HopperType.Items, false);
			case 11: return new TinyHopper(HopperType.Items, true);
			case 12: return new TinyHopper(HopperType.Fluids, false);
			case 13: return new TinyHopper(HopperType.Fluids, true);
			case 14: return new TinyHopper(HopperType.Energy, false);
			case 15: return new TinyHopper(HopperType.Energy, true);
			default: return null;
		}
	}
	
	
	
	@Override
	public void registerTextures(TextureEngine par1)
	{
		par1.setCurrentPath("transport");
		par1.registerTexture(this, 1, "ItemTransport");
		par1.registerTexture(this, 2, "FluidTransport");
		par1.registerTexture(this, 3, "EnergyTransport");
		TileIconMaker.registerIcon(this, par1);
		par1.removePath();
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresRenderer(int meta)
	{
		switch(meta)
		{
			case 1:
			case 2:
			case 3: return true;
		}
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultibleRenderPasses(int meta)
	{
		switch(meta)
		{
			case 1:
			case 2:
			case 3: return true;
		}
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderPasses(int meta)
	{
		switch(meta)
		{
			case 1:
			case 2:
			case 3: return 2;
		}
		return 0;
	}
	
	@Override
	public boolean requiresRenderPass(int meta)
	{
		switch(meta)
		{
			case 1:
			case 2:
			case 3: return true;
		}
		return super.requiresRenderPass(meta);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onRender(IBlockAccess world, int x, int y, int z, RenderBlocks render, BlockStack block, int renderPass)
	{
		int meta = block.getMeta();
		if(meta > 0 && meta < 4)
		{
			AdvTile tile = getAdvTile(world, x, y, z);
			if(tile != null)
			{
				this.setRenderPass(meta, renderPass);
				render.renderStandardBlock(block.getBlock(), x, y, z);
			}
		}
	}
	
	@Override
	public boolean dissableRendering(int meta)
	{
		return meta >= 10;
	}
	
	@Override
	public boolean requiresRender()
	{
		return true;
	}
	
	@Override
	public boolean renderItemBlock(int meta)
	{
		switch(meta)
		{
			case 0:
			case 4: return true;
		}
		return false;
	}
	
	@Override
	public boolean renderItemBlockBasic(int meta)
	{
		switch(meta)
		{
			case 0:
			case 4: return true;
		}
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
