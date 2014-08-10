package speiger.src.spmodapi.common.blocks.hemp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import net.minecraft.block.BlockCrops;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.lib.SpmodAPILib;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.farming.ICrop;

public class BlockHempCrop extends BlockCrops implements ICrop//,IFactoryHarvestable, IFactoryFertilizable, IFactoryPlantable
{
	
	IIcon[] textures = new IIcon[8];
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2)
	{
		return textures[par2];
	}
	
	public Item getSeed()
	{
		return APIItems.hempSeed;
	}

	@Override
	public Item func_149866_i()
	{
		return getSeed();
	}
	
	@Override
	public Item func_149865_P()
	{
		return APIItems.hemp;
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
	{
		ArrayList<ItemStack> list = super.getDrops(world, x, y, z, metadata, fortune);
		if (metadata >= 7)
		{
			for (int n = 0; n < 3 + fortune; n++)
			{
				if (world.rand.nextInt(10) <= metadata)
				{
					list.add(new ItemStack(func_149865_P(), 1, 0));
				}
			}
		}
		return list;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1)
	{
		for (int i = 1; i < 9; i++)
		{
			textures[i - 1] = par1.registerIcon(SpmodAPILib.ModID.toLowerCase() + ":hemp/HempCrop_" + i);
		}
	}
	
	@Override
	public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
	{
		return EnumPlantType.Crop;
	}
	
	@Override
	public Collection<ItemStack> harvest()
	{
		Random rand = new Random();
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(func_149865_P(), rand.nextInt(4), 0));
		drops.add(new ItemStack(getSeed(), rand.nextInt(3) + 1, 0));
		return drops;
	}
	
//	@Override
//	public Block getPlant()
//	{
//		return this;
//	}
	
//	@Override
//	public HarvestType getHarvestType()
//	{
//		return HarvestType.Normal;
//	}
	
//	@Override
//	public boolean breakBlock()
//	{
//		return true;
//	}
	
//	@Override
//	public boolean canBeHarvested(World world, Map<String, Boolean> harvesterSettings, int x, int y, int z)
//	{
//		Random rand = new Random();
//		int meta = world.getBlockMetadata(x, y, z);
//		if (meta >= 7)
//		{
//			if (rand.nextInt(10) == 0)
//			{
//				return true;
//			}
//		}
//		
//		return false;
//	}
	
//	@Override
//	public List<ItemStack> getDrops(World world, Random rand, Map<String, Boolean> harvesterSettings, int x, int y, int z)
//	{
//		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
//		drops.addAll(harvest());
//		return drops;
//	}
	
//	@Override
//	public void preHarvest(World world, int x, int y, int z)
//	{
//	}
	
//	@Override
//	public void postHarvest(World world, int x, int y, int z)
//	{
//	}

//	@Override
//	public boolean canFertilize(World world, int x, int y, int z, FertilizerType type)
//	{
//		if (type == FertilizerType.GrowPlant)
//		{
//			int meta = world.getBlockMetadata(x, y, z);
//			if (meta < 5)
//			{
//				return true;
//			}
//		}
//		return false;
//	}
	
//	@Override
//	public boolean fertilize(World world, Random rand, int x, int y, int z, FertilizerType fertilizerType)
//	{
//		if (fertilizerType == FertilizerType.GrowPlant)
//		{
//			world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) + 1, 2);
//			return true;
//		}
//		return false;
//	}

//	@Override
//	public boolean canBePlanted(ItemStack stack)
//	{
//		return true;
//	}

//	@Override
//	public ReplacementBlock getPlantedBlock(World world, int x, int y, int z, ItemStack stack)
//	{
//		return new ReplacementBlock(this);
//	}
	
//	@Override
//	public boolean canBePlantedHere(World world, int x, int y, int z, ItemStack stack)
//	{
//		return canBlockStay(world, x, y, z);
//	}
	
//	@Override
//	public void prePlant(World world, int x, int y, int z, ItemStack stack)
//	{
//	}
	
//	@Override
//	public void postPlant(World world, int x, int y, int z, ItemStack stack)
//	{
//	}
}
