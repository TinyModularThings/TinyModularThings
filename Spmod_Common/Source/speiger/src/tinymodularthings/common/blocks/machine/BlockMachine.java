package speiger.src.tinymodularthings.common.blocks.machine;

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
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import speiger.src.api.blocks.BlockStack;
import speiger.src.spmodapi.common.blocks.cores.SpmodBlockContainerBase;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.TileIconMaker;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMachine extends SpmodBlockContainerBase
{
	
	public BlockMachine(int par1)
	{
		super(par1, Material.iron);
		setHardness(4.0F);
		MinecraftForge.setBlockHarvestLevel(this, "pickaxe", 1);
		setCreativeTab(CreativeTabs.tabFood);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3)
	{
		for (int i = 0; i < 5; i++)
		{
			par3.add(new ItemStack(par1, 1, i));
		}
	}
	
	@Override
	public TileEntity createTileEntity(World par1, int metadata)
	{
		switch (metadata)
		{
			case 0: return new PressureFurnace();
			case 1: return new BucketFillerBasic();
			case 2: return new SelfPoweredBucketFiller();
			case 3: return new WaterGenerator();
			case 4: return new OilGenerator();
			default: return null;
		}
	}
	
	@Override
	public void registerTextures(TextureEngine par1)
	{
		par1.removePath();
		par1.finishMod();
		par1.registerTexture(this, "cobblestone", "furnace_front_off", "furnace_front_on");
		par1.setCurrentMod(TinyModularThingsLib.ModID.toLowerCase());
		par1.setCurrentPath("machine");
		par1.registerTexture(new BlockStack(this, 1), "basicBucketFiller_top", "basicBucketFiller_bottom", "basicBucketFiller_side");
		par1.registerTexture(new BlockStack(this, 2), "SelfPoweredBucketFiller_top", "SelfPoweredBucketFiller_bottom", "SelfPoweredBucketFiller_side");
		par1.registerTexture(new BlockStack(this, 3), "waterGenerator_top", "waterGenerator_bottom", "waterGenerator_side");
	
	}
}
