package speiger.src.tinymodularthings.common.blocks.transport;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
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
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.tile.TileFacing;
import speiger.src.spmodapi.common.util.TileIconMaker;
import speiger.src.tinymodularthings.common.enums.EnumIDs;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;
import speiger.src.tinymodularthings.common.utils.HopperType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTransport extends BlockContainer
{
	
	public BlockTransport(int par1)
	{
		super(par1, Material.iron);
		setCreativeTab(CreativeTabs.tabFood);
		this.setHardness(4.0F);
		this.setResistance(4.0F);
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
				case 0: return new EnderChestReader();
				case 1: return new MultiStructureItemInterface();
				case 2: return new MultiStructureFluidInterface();
				case 3: return new MultiStructureEnergyInterface();
				
				case 10: return new TinyHopper(HopperType.Items, false);
				case 11: return new TinyHopper(HopperType.Items, true);
				case 12: return new TinyHopper(HopperType.Fluids, false);
				case 13: return new TinyHopper(HopperType.Fluids, true);
				case 14: return new TinyHopper(HopperType.Energy, false);
				case 15: return new TinyHopper(HopperType.Energy, true);
				default: return null;
			}
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		par3List.add(new ItemStack(par1, 1, 0));
	}
	
	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5, ItemStack par6ItemStack)
	{
		int direction = 2;
		int facing = MathHelper.floor_double(par5.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
		int var8 = Math.round(par5.rotationPitch);
		if (var8 > 57)
		{
			direction = ForgeDirection.UP.ordinal();
		}
		else if (var8 < -57)
		{
			direction = ForgeDirection.DOWN.ordinal();
		}
		else
		{
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
		}
		TileEntity tile = par1World.getBlockTileEntity(par2, par3, par4);
		if (tile != null && tile instanceof AdvTile)
		{
			((AdvTile) tile).onPlaced(direction);
			if (par5 instanceof EntityPlayer)
			{
				((AdvTile) tile).setupUser((EntityPlayer) par5);
			}
		}
	}
	
	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
	{
		if (!par1World.isRemote)
		{
			TileEntity tile = par1World.getBlockTileEntity(par2, par3, par4);
			
			if (tile instanceof TileFacing)
			{
				if (((AdvTile) tile).onClick(par5EntityPlayer.isSneaking(), par5EntityPlayer, this, par6))
				{
					return true;
				}
			}
			
			if (par5EntityPlayer.isSneaking())
			{
				return false;
			}
			
			if (tile != null && tile instanceof AdvTile)
			{
				
				return ((AdvTile) tile).onActivated(par5EntityPlayer);
			}
		}
		
		return true;
	}
	
	@Override
	public int getRenderType()
	{
		return EnumIDs.TransportBlock.getId();
	}
	
	@Override
	public int idDropped(int par1, Random par2Random, int par3)
	{
		return 0;
	}
	
	@Override
	public int damageDropped(int par1)
	{
		return 0;
	}
	
	@Override
	public int quantityDropped(int meta, int fortune, Random random)
	{
		return 0;
	}
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if (tile != null && tile instanceof AdvTile)
		{
			return ((AdvTile) tile).pickBlock(target);
		}
		return super.getPickBlock(target, world, x, y, z);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int par1, int par2)
	{
		if (par2 == 0)
		{
			return Block.endPortal.getIcon(0, 0);
		}
		return null;
	}
	
	Icon[] texture = new Icon[3];
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess par1, int par2, int par3, int par4, int par5)
	{
		int meta = par1.getBlockMetadata(par2, par3, par4);
		if (meta == 0)
		{
			return getIcon(0, 0);
		}
		else
		{
			TileEntity tile = par1.getBlockTileEntity(par2, par3, par4);
			if (tile != null && tile instanceof AdvTile)
			{
				return TileIconMaker.getIconMaker().getIconSafe(((AdvTile) tile).getIconFromSideAndMetadata(par5, 0));
			}
		}
		return null;
	}
	
	public Icon getTextureFromMeta(int meta)
	{
		return texture[meta];
	}
	
	
	
	@Override
	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
	{
		ArrayList<ItemStack> drop = new ArrayList<ItemStack>();
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if(tile != null && tile instanceof AdvTile && ((AdvTile)tile).onDrop(fortune) != null)
		{
			drop.addAll(((AdvTile)tile).onDrop(fortune));
		}
		return drop;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1)
	{
		texture[0] = par1.registerIcon(TinyModularThingsLib.ModID.toLowerCase() + ":transport/ItemTransport");
		texture[1] = par1.registerIcon(TinyModularThingsLib.ModID.toLowerCase() + ":transport/FluidTransport");
		texture[2] = par1.registerIcon(TinyModularThingsLib.ModID.toLowerCase() + ":transport/EnergyTransport");
	}
	
}
