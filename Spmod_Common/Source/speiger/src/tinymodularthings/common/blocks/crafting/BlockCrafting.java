package speiger.src.tinymodularthings.common.blocks.crafting;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import speiger.src.api.client.gui.IBlockGui;
import speiger.src.api.common.registry.helpers.SpmodMod;
import speiger.src.api.common.world.blocks.BlockPosition;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.common.blocks.cores.SpmodBlockContainerBase;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.TileIconMaker;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.client.gui.crafting.GuiAdvCrafting;
import speiger.src.tinymodularthings.common.enums.EnumIDs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCrafting extends SpmodBlockContainerBase implements IBlockGui
{

	public BlockCrafting(int par1)
	{
		super(par1, Material.piston);
		this.setCreativeTab(CreativeTabs.tabFood);
		this.setHardness(2F);
		this.setResistance(4F);
		this.dissableDrops(1);
		this.dissableDrops(2);
	}
	
	@Override
	public float getBlockHardness(int meta)
	{
		switch(meta)
		{
			case 0: return 2F;
		}
		return super.getBlockHardness(meta);
	}

	@Override
	public Icon getTexture(TextureEngine par1, int meta, int side)
	{
		switch(meta)
		{
			case 0:
			case 2: return side == 0 ? par1.getTexture(this, 0) : side == 1 ? par1.getTexture(this, 1) : par1.getTexture(this, 2);
		}
		return super.getTexture(par1, meta, side);
	}
	
	@Override
	public boolean hasTileEntity(int metadata)
	{
		switch(metadata)
		{
			case 0: return false;
		}
		return super.hasTileEntity(metadata);
	}

	@Override
	public float getBlockResistance(Entity par1, int meta)
	{
		switch(meta)
		{
			case 0: return 2F;
		}
		return super.getBlockResistance(par1, meta);
	}

	@Override
	public void registerTextures(TextureEngine par1)
	{
		par1.setCurrentPath("crafting");
		par1.registerTexture(new BlockStack(this, 0), "craftingTableBottom", "craftingTableTop", "craftingTableSide");
		TileIconMaker.registerIcon(this, par1);
		par1.removePath();
	}
	
	@Override
	public TileEntity createTileEntity(World world, int meta)
	{
		switch(meta)
		{
			case 1: return new OreCrafter();
			case 2: return new CraftingStation();
			case 3: return new Uncrafter();
		}
		return null;
	}

	@Override
	public int getGuiIDForMeta(int meta)
	{
		switch(meta)
		{
			case 0: return EnumIDs.BlockGui.getId();
		}
		return super.getGuiIDForMeta(meta);
	}
	
	@Override
	public SpmodMod getModInstance(int meta)
	{
		switch(meta)
		{
			case 0: return TinyModularThings.instance;
		}
		return super.getModInstance(meta);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for(int i = 0;i<4;i++)
		{
			par3List.add(new ItemStack(par1, 1, i));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer getGui(int meta, InventoryPlayer par1, BlockPosition par2)
	{
		if(meta == 0)
		{
			return new GuiAdvCrafting(this, meta, par1, par2);
		}
		return null;
	}

	@Override
	public boolean hasTileDrops(int meta)
	{
		switch(meta)
		{
			case 1:
			case 3:
			case 2: return true;
		}
		return super.hasTileDrops(meta);
	}

	@Override
	public Container getInventory(int meta, InventoryPlayer par1, BlockPosition par2)
	{
		if(meta == 0)
		{
			return new ContainerAdvCrafting(par1);
		}
		return null;
	}
}
