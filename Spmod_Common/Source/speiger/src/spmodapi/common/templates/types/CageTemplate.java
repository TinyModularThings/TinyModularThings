package speiger.src.spmodapi.common.templates.types;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import speiger.src.api.common.world.blocks.BlockPosition;
import speiger.src.api.common.world.tiles.interfaces.IAcceptor;
import speiger.src.api.common.world.tiles.interfaces.IAcceptor.AcceptorType;
import speiger.src.spmodapi.common.blocks.utils.EntityCage;
import speiger.src.spmodapi.common.interfaces.IAdvTile;
import speiger.src.spmodapi.common.templates.core.BaseTemplate;
import speiger.src.spmodapi.common.templates.core.TemplateBox;

public class CageTemplate extends BaseTemplate
{
	World world;
	int xCoord;
	int yCoord;
	int zCoord;
	EntityCage cage;
	int size;
	
	public CageTemplate(IAdvTile par1)
	{
		super(par1);
		cage = (EntityCage)par1;
	}

	@Override
	public void setup(World world, int x, int y, int z, int facing)
	{
		this.world = world;
		this.xCoord = x;
		this.yCoord = y;
		this.zCoord = z;
		size = facing;
	}
	
	@Override
	public void setupFacing(int facing)
	{
		
	}
	
	@Override
	public boolean match()
	{
		if(!boundsOk())
		{
			return false;
		}
		boolean wrong = false;
		int[] interfaces = new int[3];
		for(int x = -size;x<size;x++)
		{
			if(wrong)
			{
				break;
			}
			for(int y = -size;y<size;y++)
			{
				if(wrong)
				{
					break;
				}
				for(int z = -size;z<size;z++)
				{
					if(wrong)
					{
						break;
					}
					BlockPosition pos = new BlockPosition(world, xCoord + x, yCoord + y, zCoord + z);
					if(x == -size || x == size || y == -size || y == size || z == -size || z == size)
					{
						if(pos.isAirBlock() || (!pos.getBlock().isBeaconBase(world, pos.getXCoord(), pos.getYCoord(), pos.getZCoord(), xCoord, yCoord, zCoord) && !this.isInterface(pos.getAsBlockStack())))
						{
							wrong = true;
							break;
						}
						if(this.isInterface(pos.getAsBlockStack()))
						{
							interfaces[getInterfaceType(pos.getAsBlockStack())]++;
						}
						continue;
					}
					if(x == 0 && y == 0 && z == 0)
					{
						continue;
					}
					if(!pos.isAirBlock())
					{
						wrong = true;
					}
				}
			}
		}
		for(AcceptorType type : AcceptorType.values())
		{
			if(!this.isInterfaceAmountOk(type, interfaces[type.ordinal()]))
			{
				wrong = true;
				break;
			}
		}
		if(wrong)
		{
			return false;
		}
		return world.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getAABBPool().getAABB(xCoord, yCoord, zCoord, xCoord+1, yCoord+1, zCoord+1).addCoord(size, size, size)).size() <= 0;
	}
	


	private boolean boundsOk()
	{
		int y;
		for(y = yCoord - 1;y> yCoord - size + 1;y--)
		{
			if(!world.isAirBlock(xCoord, y, zCoord))
			{
				return false;
			}
		}
		BlockPosition pos = new BlockPosition(world, xCoord, y, zCoord);
		if(!pos.isAirBlock())
		{
			return pos.getBlock().isBeaconBase(world, xCoord, y, zCoord, xCoord, yCoord, zCoord);
		}
		return false;
	}
	
	@Override
	public IAcceptor[] getInterfaces(AcceptorType par1, int par2)
	{
		IAcceptor[] interfaces = new IAcceptor[par2];
		int state = 0;
		for(int x = -size;x<size;x+=(size*2))
		{
			if(state == par2)
			{
				break;
			}
			for(int y = -size;y<size;y+=(size*2))
			{
				if(state == par2)
				{
					break;
				}
				for(int z = -size;z<size;z+=(size*2))
				{
					if(state == par2)
					{
						break;
					}
					BlockPosition pos = new BlockPosition(world, xCoord + x, yCoord + y, zCoord + z);
					if(x == -size || x == size || y == -size || y == size || z == -size || z == size)
					{
						if(isInterface(pos.getAsBlockStack()))
						{
							IAcceptor accept = (IAcceptor)pos.getTileEntity();
							if(accept.getType() == par1 && !accept.hasMaster())
							{
								accept.setMaster(cage);
								interfaces[state] = accept;
								state++;
							}
						}
					}
				}
			}
		}
		return interfaces;
	}

	@Override
	public void init()
	{
		this.addStructure(new TemplateBox(cage, new BlockPosition(world, xCoord - size, yCoord - size, zCoord - size), new BlockPosition(world, xCoord + size, yCoord + size, zCoord + size)));
	}
	
	
	
}
