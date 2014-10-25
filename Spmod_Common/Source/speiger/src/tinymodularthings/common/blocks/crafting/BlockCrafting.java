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
import speiger.src.api.blocks.BlockPosition;
import speiger.src.api.blocks.IBlockGui;
import speiger.src.api.util.SpmodMod;
import speiger.src.spmodapi.common.blocks.cores.SpmodBlockContainerBase;
import speiger.src.spmodapi.common.enums.EnumGuiIDs;
import speiger.src.spmodapi.common.tile.AdvTile;
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
	public float getBlockResistance(Entity par1, int meta)
	{
		switch(meta)
		{
			case 0: return 2F;
		}
		return super.getBlockResistance(par1, meta);
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
	public int getGuiIDForMeta(int meta)
	{
		switch(meta)
		{
			case 0: return EnumGuiIDs.BlockGui.getID();
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
		for(int i = 0;i<2;i++)
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
	public Container getInventory(int meta, InventoryPlayer par1, BlockPosition par2)
	{
		if(meta == 0)
		{
			return new ContainerAdvCrafting(par1);
		}
		return null;
	}
}
