package speiger.src.tinymodularthings.common.blocks.ores;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import speiger.src.api.common.world.blocks.BlockPosition;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.common.blocks.cores.SpmodBlockBase;
import speiger.src.spmodapi.common.util.TickHelper;
import speiger.src.spmodapi.common.world.retrogen.RetroGenTickHandler.OreReplacer;
import speiger.src.tinymodularthings.common.interfaces.IMultimineOre;

public abstract class BlockMultiMineOre extends SpmodBlockBase implements IMultimineOre
{
	int maxMine;
	
	public BlockMultiMineOre(int par1, Material par2Material, int par4)
	{
		super(par1, par2Material);
		maxMine = par4;
	}
	
	@Override
	public void onBlockPreDestroy(World par1World, int par2, int par3, int par4, int par5)
	{
		if (par1World.isRemote)
		{
			return;
		}
		int meta = par1World.getBlockMetadata(par2, par3, par4);
		
		if (meta > 0)
		{
			TickHelper.getInstance().regainOre.add(new OreReplacer(new BlockStack(this, new Random().nextInt(meta)), 2, new BlockPosition(par1World, par2, par3, par4)));
		}
		
	}
	
	public abstract ItemStack getBasicDrop(int fortune, int meta);
	
	public abstract ItemStack[] getDrops(int fortune, int meta);
	
	public abstract ItemStack[] getRareDrops(int fortune, int meta);
	
	@Override
	public int idDropped(int par1, Random par2Random, int par3)
	{
		return getBasicDrop(0, par1) != null ? getBasicDrop(0, par1).itemID : 0;
	}
	
	@Override
	public int damageDropped(int par1)
	{
		return getBasicDrop(0, par1) != null ? getBasicDrop(0, par1).getItemDamage() : 0;
	}
	
	@Override
	public int quantityDropped(int meta, int fortune, Random random)
	{
		return getBasicDrop(fortune, meta) != null ? getBasicDrop(fortune, meta).stackSize : 0;
	}
	
	@Override
	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
	{
		ArrayList<ItemStack> end = new ArrayList<ItemStack>();
		if (getDrops(fortune, metadata) != null && getDrops(fortune, metadata).length > 0)
		{
			for (ItemStack cu : getDrops(fortune, metadata))
			{
				end.add(cu);
			}
		}
		if (getRareDrops(fortune, metadata) != null && getRareDrops(fortune, metadata).length > 0)
		{
			ItemStack[] rare = getRareDrops(fortune, metadata);
			int bonus = 1;
			if (fortune > 0)
			{
				bonus += fortune;
			}
			for (int i = 0; i < bonus; i++)
			{
				if (new Random().nextBoolean())
				{
					end.add(rare[new Random().nextInt(rare.length)]);
				}
			}
		}
		return end;
	}
	
	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack)
	{
		init(par1World, par2, par3, par4);
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return true;
	}
	
	@Override
	public void init(World par0, int x, int y, int z)
	{
		par0.setBlock(x, y, z, blockID, new Random().nextInt(maxMine), 3);
	}

	
	
	
}
