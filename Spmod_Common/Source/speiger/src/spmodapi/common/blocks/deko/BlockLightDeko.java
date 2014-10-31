package speiger.src.spmodapi.common.blocks.deko;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import speiger.src.api.util.InventoryUtil;
import speiger.src.spmodapi.common.blocks.cores.SpmodBlockContainerBase;
import speiger.src.spmodapi.common.blocks.deko.TileLamp.EnumLampType;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.enums.EnumColor;
import speiger.src.spmodapi.common.enums.EnumColor.SpmodColor;
import speiger.src.spmodapi.common.tile.AdvTile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockLightDeko extends SpmodBlockContainerBase
{	
	public BlockLightDeko(int par1)
	{
		super(par1, Material.redstoneLight);
		setCreativeTab(APIUtils.tabHempDeko);
		this.dissableDrops();
		this.setIsAbnormal();
		this.setIgnoreRighClick();
		this.setRightClickDoesNothing();
		MinecraftForge.setBlockHarvestLevel(this, "pickaxe", 0);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileLamp();
	}
	
	@Override
	public void setBlockBoundsForItemRender()
	{
		this.setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
	}
	
	@Override
	public int getRenderType()
	{
		return -1;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int par1, int par2)
	{
		return Block.redstoneLampActive.getIcon(0, 0);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	// Item Side
	public int getRenderColor(int par1)
	{
		return EnumColor.values()[par1].getAsHex().intValue();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int i = 0; i < 72; i++)
		{
			par3List.add(new ItemStack(par1, 1, i));
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
	public boolean hasTileDrops(int meta)
	{
		return true;
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);
		return meta == 1 ? 15 : 0;
	}
	
	
}
