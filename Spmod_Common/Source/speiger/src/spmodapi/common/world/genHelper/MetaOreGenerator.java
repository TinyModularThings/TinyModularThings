package speiger.src.spmodapi.common.world.genHelper;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.tinymodularthings.common.interfaces.IMultimineOre;

public class MetaOreGenerator
{
	int size;
	BlockStack ore;
	int replace = Block.stone.blockID;
	boolean callPlaced = false;
	
	public MetaOreGenerator(int size, BlockStack ores)
	{
		this.size = size;
		ore = ores;
	}
	
	public void forcePlacing()
	{
		callPlaced = true;
	}
	
	public void setBlockToReplace(Block block)
	{
		this.setBlockToReplace(block.blockID);
	}
	
	public void setBlockToReplace(int blockID)
	{
		replace = blockID;
	}
	
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5)
	{
		float f = par2Random.nextFloat() * (float) Math.PI;
		double d0 = par3 + 8 + MathHelper.sin(f) * size / 8.0F;
		double d1 = par3 + 8 - MathHelper.sin(f) * size / 8.0F;
		double d2 = par5 + 8 + MathHelper.cos(f) * size / 8.0F;
		double d3 = par5 + 8 - MathHelper.cos(f) * size / 8.0F;
		double d4 = par4 + par2Random.nextInt(3) - 2;
		double d5 = par4 + par2Random.nextInt(3) - 2;
		
		for (int l = 0; l <= size; ++l)
		{
			double d6 = d0 + (d1 - d0) * l / size;
			double d7 = d4 + (d5 - d4) * l / size;
			double d8 = d2 + (d3 - d2) * l / size;
			double d9 = par2Random.nextDouble() * size / 16.0D;
			double d10 = (MathHelper.sin(l * (float) Math.PI / size) + 1.0F) * d9 + 1.0D;
			double d11 = (MathHelper.sin(l * (float) Math.PI / size) + 1.0F) * d9 + 1.0D;
			int i1 = MathHelper.floor_double(d6 - d10 / 2.0D);
			int j1 = MathHelper.floor_double(d7 - d11 / 2.0D);
			int k1 = MathHelper.floor_double(d8 - d10 / 2.0D);
			int l1 = MathHelper.floor_double(d6 + d10 / 2.0D);
			int i2 = MathHelper.floor_double(d7 + d11 / 2.0D);
			int j2 = MathHelper.floor_double(d8 + d10 / 2.0D);
			
			for (int k2 = i1; k2 <= l1; ++k2)
			{
				double d12 = (k2 + 0.5D - d6) / (d10 / 2.0D);
				
				if (d12 * d12 < 1.0D)
				{
					for (int l2 = j1; l2 <= i2; ++l2)
					{
						double d13 = (l2 + 0.5D - d7) / (d11 / 2.0D);
						
						if (d12 * d12 + d13 * d13 < 1.0D)
						{
							for (int i3 = k1; i3 <= j2; ++i3)
							{
								double d14 = (i3 + 0.5D - d8) / (d10 / 2.0D);
								
								Block block = Block.blocksList[par1World.getBlockId(k2, l2, i3)];
								if (d12 * d12 + d13 * d13 + d14 * d14 < 1.0D && (block != null && block.isGenMineableReplaceable(par1World, k2, l2, i3, replace)))
								{
									par1World.setBlock(k2, l2, i3, ore.getBlockID(), ore.getMeta(), 2);
									if (callPlaced && par1World.getBlockId(k2, l2, i3) != 0 && Block.blocksList[par1World.getBlockId(k2, l2, i3)] != null && Block.blocksList[par1World.getBlockId(k2, l2, i3)] instanceof IMultimineOre)
									{
										((IMultimineOre) Block.blocksList[par1World.getBlockId(k2, l2, i3)]).init(par1World, k2, l2, i3);
									}
								}
							}
						}
					}
				}
			}
		}
		
		return true;
	}
}
