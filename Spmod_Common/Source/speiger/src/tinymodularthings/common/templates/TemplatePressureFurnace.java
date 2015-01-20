package speiger.src.tinymodularthings.common.templates;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.common.world.blocks.BlockPosition;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.common.templates.BaseTemplate;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.tinymodularthings.common.blocks.machine.PressureFurnace;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import cpw.mods.fml.common.FMLLog;

public class TemplatePressureFurnace extends BaseTemplate
{


	World world;
	int xCoord;
	int yCoord;
	int zCoord;
	ForgeDirection dir;
	PressureFurnace furnace;
	
	public TemplatePressureFurnace(AdvTile par1)
	{
		super(par1);
		furnace = (PressureFurnace)par1;
	}
	
	
	@Override
	public void setup(World world, int x, int y, int z, int facing)
	{
		this.world = world;
		xCoord = x;
		yCoord = y;
		zCoord = z;
		dir = ForgeDirection.getOrientation(facing);
	}
	
	@Override
	public void setupFacing(int facing)
	{
		dir = ForgeDirection.getOrientation(facing);
	}
	
	@Override
	public boolean match()
	{
		boolean flag = this.getTotalPatternSize() == 27 ? true : false;
		ForgeDirection of = dir.getOpposite();
		BlockPosition start = new BlockPosition(xCoord+of.offsetX, yCoord+of.offsetY-1, zCoord+of.offsetZ);
		List<BlockPosition> match = new ArrayList<BlockPosition>();
		List<BlockPosition> inner = new ArrayList<BlockPosition>();
		
		for(int posX = -1;posX<2;posX++)
		{
			for(int posY = 0;posY<3;posY++)
			{
				for(int posZ = -1;posZ<2;posZ++)
				{
					int x = start.getXCoord() + posX;
					int y = start.getYCoord() + posY;
					int z = start.getZCoord() + posZ;
					BlockStack compare = getBlockFromPos(posX, posY, posZ);
					BlockPosition block = new BlockPosition(world, x, y, z);
					BlockStack realBlock = new BlockStack(world, x, y, z, true);
					if(compare.match(realBlock) || isInterface(realBlock))
					{
						if(isInterface(realBlock))
						{
							inner.add(block);
						}
						match.add(block);
					}
					else if(realBlock.getBlock() != null && compare.getBlockID() == Block.cobblestone.blockID)
					{
						if(isValidBlockName(realBlock.getHiddenName()) && realBlock.getBlock().isBlockNormalCube(world, x, y, z) && realBlock.getBlock().isBlockSolidOnSide(world, x, y, z, ForgeDirection.WEST))
						{
							match.add(block);
						}
					}
				}
			}
		}
		
		if(inner.size() > 4)
		{
			match.removeAll(inner);
		}
		if(flag)
		{
			match.retainAll(this.getStructure());
		}
		
		if(match.size() == 27)
		{
			if(this.checkStorage(match))
			{
				this.addToStorage(match);
			}
			return true;
		}
		else
		{
			this.removeFromStorage(match);
		}
		return false;
	}
	
	public BlockStack getBlockFromPos(int x, int y, int z)
	{
		if(x == 0 && y == 1 && z == 0)
		{
			return new BlockStack();
		}
		if(x == dir.offsetX && y == 1 && z == dir.offsetZ)
		{
			return new BlockStack(TinyBlocks.machine, 0);
		}
		return new BlockStack(Block.cobblestone);
	}
	
	public boolean isValidBlockName(String par1)
	{
		boolean ore = par1.contains("Ore") || par1.contains("ore");
		if ((!ore && (par1.contains("cobble") || par1.contains("sandStone") || par1.contains("brick") || par1.contains("stone"))))
		{
			return true;
		}
		return false;
	}
}
