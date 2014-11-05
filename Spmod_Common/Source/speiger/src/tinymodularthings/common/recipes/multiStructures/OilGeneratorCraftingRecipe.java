package speiger.src.tinymodularthings.common.recipes.multiStructures;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import speiger.src.api.common.utils.config.EntityCounter;
import speiger.src.api.common.world.blocks.BlockPosition;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.api.common.world.gen.IWorldCraftingRecipe;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;

public class OilGeneratorCraftingRecipe implements IWorldCraftingRecipe
{
	
	@Override
	public BlockStack getStartingBlock()
	{
		return new BlockStack(Block.blockEmerald);
	}
	
	@Override
	public boolean canCraftBlockStructure(BlockPosition blockPosition, EntityPlayer crafter)
	{
		return true;
	}
	
	@Override
	public BlockStack getFinsiherBlock()
	{
		return new BlockStack(Block.workbench);
	}
	
	@Override
	public boolean canFinishMultiStructureCrafting(EntityPlayer par1)
	{
		return true;
	}
	
	@Override
	public boolean isStructureFinish(BlockPosition startBlock)
	{
		HashMap<BlockStack, EntityCounter> map = new HashMap<BlockStack, EntityCounter>();
		int matching = 0;
		for(int y = -3;y<4;y++)
		{
			for(int x = -3;x<4;x++)
			{
				for(int z = -3;z<4;z++)
				{
					BlockStack requestedBlock = getBlockAtPosition(x,y,z);
					BlockStack currentBlock = startBlock.add(x,y,z).getAsBlockStack();
					if(requestedBlock.match(currentBlock))
					{
						if(map.containsKey(currentBlock))
						{
							map.get(currentBlock).updateToNextID();
						}
						else
						{
							map.put(currentBlock, new EntityCounter());
						}
						matching++;
					}

				}
			}
		}		
		return matching == 325;
	}
	
	public BlockStack getBlockAtPosition(int x, int y, int z)
	{
		if(x == 0 && y == 0 && z == 0)
		{
			return new BlockStack(Block.blockEmerald);
		}
		if(y == -3 || y == 3)
		{
			if(x>=-1 && x<=1 && z>=-1 && z<=1)
			{
				return new BlockStack(Block.blockRedstone);
			}
		}
		if(x == -3 || x == 3)
		{
			if(y>=-1 && y<=1 && z>=-1 && z<=1)
			{
				return new BlockStack(Block.blockRedstone);
			}

		}
		if(z == -3 || z == 3)
		{
			if(x>=-1 && x<=1 && y>=-1 && y<=1)
			{
				return new BlockStack(Block.blockRedstone);
			}

		}
		if(y == -2 || y == 2)
		{
			if(x>=-1 && x<=1 && z>=-1 && z<=1)
			{
				return new BlockStack(Block.pistonBase, y < 0 ? 1 : 0);
			}
			if(x == 2 || x == -2 && z>-3 && z<3)
			{
				return new BlockStack(Block.obsidian);
			}
			if(z == 2 || z == -2 && x>-3 && x<3)
			{
				return new BlockStack(Block.obsidian);
			}
		}
		if(x == -2 || x == 2)
		{
			if(y>=-1 && y<=1 && z>=-1 && z<=1)
			{
				return new BlockStack(Block.pistonBase, x < 0 ? 5 : 4);
			}
			if(y == 2 || y == -2 && z>-3 && z<3)
			{
				return new BlockStack(Block.obsidian);
			}
			if(z == 2 || z == -2 && y>-3 && y<3)
			{
				return new BlockStack(Block.obsidian);
			}
		}
		if(z == -2 || z == 2)
		{
			if(x>=-1 && x<=1 && y>=-1 && y<=1)
			{
				return new BlockStack(Block.pistonBase, z < 0 ? 3 : 2);
			}
			if(x == 2 || x == -2 && y>-3 && y<3)
			{
				return new BlockStack(Block.obsidian);
			}
			if(y == 2 || y == -2 && x>-3 && x<3)
			{
				return new BlockStack(Block.obsidian);
			}
		}
		if(x<=1 && x>=-1 && y<=1 && y>=-1 && z<=1 && z>=-1)
		{
			return new BlockStack(Block.obsidian);
		}
		
		
		return new BlockStack(Block.brick);
	}
	
	
	@Override
	public boolean isFinishingBlockAtRightPosition(BlockPosition startBlock, BlockPosition finishBlock)
	{
		if(startBlock.worldPositionMatch(finishBlock) && startBlock.yCoord+4 == finishBlock.yCoord)
		{
			return true;
		}
		return false;
	}
	
	@Override
	public void onFinsihedCrafting(BlockPosition startBlock, EntityPlayer crafter)
	{
		for(int x = -3;x<4;x++)
		{
			for(int y = -3;y<4;y++)
			{
				for(int z = -3;z<4;z++)
				{
					startBlock.add(x, y, z).remove();
				}
			}
		}
		startBlock.setBlock(new BlockStack(TinyBlocks.machine, 4));
	}
	
}
