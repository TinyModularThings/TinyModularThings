package speiger.src.tinymodularthings.common.blocks.transport;

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
import speiger.src.spmodapi.common.blocks.cores.SpmodBlockContainerBase;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.tile.TileFacing;
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
		par1.registerTexture(this, "ItemTransport", "FluidTransport", "EnergyTransport");
		par1.removePath();
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public int getRenderType()
	{
		return EnumIDs.TransportBlock.getId();
	}
	
}
