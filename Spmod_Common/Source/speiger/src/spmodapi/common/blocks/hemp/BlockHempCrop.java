package speiger.src.spmodapi.common.blocks.hemp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.BlockCrops;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import powercrystals.minefactoryreloaded.api.FertilizerType;
import powercrystals.minefactoryreloaded.api.HarvestType;
import powercrystals.minefactoryreloaded.api.IFactoryFertilizable;
import powercrystals.minefactoryreloaded.api.IFactoryHarvestable;
import powercrystals.minefactoryreloaded.api.IFactoryPlantable;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.util.TextureEngine;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.farming.ICrop;

public class BlockHempCrop extends BlockCrops implements ICrop,
		IFactoryHarvestable, IFactoryFertilizable, IFactoryPlantable
{
	public BlockHempCrop(int par1)
	{
		super(par1);
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int par1, int par2)
	{
		return TextureEngine.getTextures().getTexture(this, par2);
	}
	
	@Override
	public int getSeedItem()
	{
		return APIItems.hempSeed.itemID;
	}
	
	@Override
	public int getCropItem()
	{
		return APIItems.hemp.itemID;
	}
	
	@Override
	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
	{
		ArrayList<ItemStack> list = super.getBlockDropped(world, x, y, z, metadata, fortune);
		if (metadata >= 7)
		{
			for (int n = 0; n < 3 + fortune; n++)
			{
				if (world.rand.nextInt(10) <= metadata)
				{
					list.add(new ItemStack(getCropItem(), 1, 0));
				}
			}
		}
		return list;
	}
	
	@Override
	public EnumPlantType getPlantType(World world, int x, int y, int z)
	{
		return EnumPlantType.Crop;
	}
	
	@Override
	public Collection<ItemStack> harvest()
	{
		Random rand = new Random();
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(getCropItem(), rand.nextInt(4), 0));
		drops.add(new ItemStack(getSeedItem(), rand.nextInt(3) + 1, 0));
		return drops;
	}
	
	@Override
	public int getPlantId()
	{
		return blockID;
	}
	
	@Override
	public HarvestType getHarvestType()
	{
		return HarvestType.Normal;
	}
	
	@Override
	public boolean breakBlock()
	{
		return true;
	}
	
	@Override
	public boolean canBeHarvested(World world, Map<String, Boolean> harvesterSettings, int x, int y, int z)
	{
		Random rand = new Random();
		int meta = world.getBlockMetadata(x, y, z);
		if (meta >= 7)
		{
			if (rand.nextInt(10) == 0)
			{
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public List<ItemStack> getDrops(World world, Random rand, Map<String, Boolean> harvesterSettings, int x, int y, int z)
	{
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.addAll(harvest());
		return drops;
	}
	
	@Override
	public void preHarvest(World world, int x, int y, int z)
	{
	}
	
	@Override
	public void postHarvest(World world, int x, int y, int z)
	{
	}
	
	@Override
	public int getFertilizableBlockId()
	{
		return blockID;
	}
	
	@Override
	public boolean canFertilizeBlock(World world, int x, int y, int z, FertilizerType type)
	{
		if (type == FertilizerType.GrowPlant)
		{
			int meta = world.getBlockMetadata(x, y, z);
			if (meta < 5)
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean fertilize(World world, Random rand, int x, int y, int z, FertilizerType fertilizerType)
	{
		if (fertilizerType == FertilizerType.GrowPlant)
		{
			world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) + 1, 2);
			return true;
		}
		return false;
	}
	
	@Override
	public int getSeedId()
	{
		return APIItems.hempSeed.itemID;
	}
	
	@Override
	public int getPlantedBlockId(World world, int x, int y, int z, ItemStack stack)
	{
		return blockID;
	}
	
	@Override
	public int getPlantedBlockMetadata(World world, int x, int y, int z, ItemStack stack)
	{
		return 0;
	}
	
	@Override
	public boolean canBePlantedHere(World world, int x, int y, int z, ItemStack stack)
	{
		return canBlockStay(world, x, y, z);
	}
	
	@Override
	public void prePlant(World world, int x, int y, int z, ItemStack stack)
	{
	}
	
	@Override
	public void postPlant(World world, int x, int y, int z, ItemStack stack)
	{
	}
	
}
