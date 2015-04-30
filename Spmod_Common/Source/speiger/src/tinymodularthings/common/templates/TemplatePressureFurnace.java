package speiger.src.tinymodularthings.common.templates;

import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.common.world.blocks.BlockPosition;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.api.common.world.tiles.interfaces.IAcceptor;
import speiger.src.api.common.world.tiles.interfaces.IAcceptor.AcceptorType;
import speiger.src.spmodapi.common.interfaces.IAdvTile;
import speiger.src.spmodapi.common.templates.core.BaseTemplate;
import speiger.src.spmodapi.common.templates.core.TemplateBox;
import speiger.src.tinymodularthings.common.blocks.machine.PressureFurnace;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;

public class TemplatePressureFurnace extends BaseTemplate
{
	World world;
	int xCoord;
	int yCoord;
	int zCoord;
	ForgeDirection dir;
	PressureFurnace furnace;
	
	public TemplatePressureFurnace(IAdvTile par1)
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
	public void init()
	{
		BlockPosition pos = new BlockPosition(world, xCoord, yCoord, zCoord).add(dir.getOpposite());
		this.addStructure(new TemplateBox(furnace, new BlockPosition(world, pos.getXCoord() - 1, pos.getYCoord() - 1, pos.getZCoord() - 1), new BlockPosition(world, pos.getXCoord() + 1, pos.getYCoord() + 1, pos.getZCoord() + 1)));	
	}

	@Override
	public boolean match()
	{
		ForgeDirection op = dir.getOpposite();
		int offsetX = op.offsetX;
		int offsetZ = op.offsetZ;
		int fluidInter = 0;
		int itemInter = 0;
		for(int x = -1;x<2;x++)
		{
			for(int y = -1;y<2;y++)
			{
				for(int z = -1;z<2;z++)
				{
					BlockPosition pos = new BlockPosition(world, xCoord + x + offsetX, yCoord + y, zCoord + z + offsetZ);
					if(x == 0 && y == 0 && z == 0)
					{
						if(!pos.isAirBlock())
						{
							return false;
						}
					}
					else if(y == 0 && pos.getXCoord() == xCoord && pos.getZCoord() == zCoord)
					{
						if(!pos.isThisBlock(new BlockStack(TinyBlocks.machine, 0), true))
						{
							return false;
						}
					}
					else
					{
						if((!isValidBlockName(pos.getAsBlockStack().getHiddenName()) && !this.isInterface(pos.getAsBlockStack())) || !pos.getBlock().isBlockNormalCube(world, pos.getXCoord(), pos.getYCoord(), pos.getZCoord()) || !pos.getBlock().isBlockSolidOnSide(world, pos.getXCoord(), pos.getYCoord(), pos.getZCoord(), ForgeDirection.WEST))
						{
							return false;
						}
						BlockStack stack = pos.getAsBlockStack();
						if(this.isInterface(stack))
						{
							if(this.isEnergyInterface(stack))
							{
								return false;
							}
							else if(this.isFluidInterface(stack))
							{
								fluidInter++;
							}
							else if(this.isItemInterface(stack))
							{
								itemInter++;
							}
							
						}
					}
				}
			}
		}
		if(!this.isInterfaceAmountOk(AcceptorType.Items, itemInter) || !this.isInterfaceAmountOk(AcceptorType.Fluids, fluidInter))
		{
			return false;
		}
		if(!colideCheck())
		{
			return false;
		}
		return true;
	}
	
	private boolean isValidBlockName(String par1)
	{
		boolean ore = par1.contains("Ore") || par1.contains("ore");
		if ((!ore && (par1.contains("cobble") || par1.contains("sandStone") || par1.contains("brick") || par1.contains("stone"))))
		{
			return true;
		}
		return false;
	}

	@Override
	public IAcceptor[] getInterfaces(AcceptorType par1, int size)
	{
		IAcceptor[] list = new IAcceptor[size];
		int current = 0;
		ForgeDirection op = dir.getOpposite();
		int xOFF = op.offsetX;
		int zOFF = op.offsetZ;
		for(int x = -1;x<2;x++)
		{
			if(current == size)
			{
				break;
			}
			for(int y = -1;y<2;y++)
			{
				if(current == size)
				{
					break;
				}
				for(int z = -1;z<2;z++)
				{
					if(current == size)
					{
						break;
					}
					BlockPosition pos = new BlockPosition(world, xCoord + x + xOFF, yCoord + y, zCoord + z + zOFF);
					if(pos.doesBlockExsist() && this.isInterface(pos.getAsBlockStack()))
					{
						IAcceptor tile = (IAcceptor)pos.getTileEntity();
						if(tile.getType() == par1 && !tile.hasMaster())
						{
							tile.setMaster(furnace);
							list[current] = tile;
							current++;
						}
					}
				}
			}
		}
		return list;
	}


	
}
