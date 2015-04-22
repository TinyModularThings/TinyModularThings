package speiger.src.spmodapi.common.templates;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import speiger.src.api.common.world.blocks.BlockPosition;
import speiger.src.api.common.world.utils.IStructureBox;
import speiger.src.spmodapi.common.util.data.StructureStorage;

public class TemplateBox implements IStructureBox
{
	ITemplateProvider provider;
	World world;
	int minX;
	int minY;
	int minZ;
	int maxX;
	int maxY;
	int maxZ;
	
	
	public TemplateBox(ITemplateProvider par1, BlockPosition min, BlockPosition max)
	{
		if(!(par1 instanceof TileEntity))
		{
			throw new RuntimeException("This template Require a TileEntity");
		}
		provider = par1;
		world = ((TileEntity)par1).getWorldObj();
		minX = min.getXCoord();
		minY = min.getYCoord();
		minZ = min.getZCoord();
		maxX = max.getXCoord();
		maxY = max.getYCoord();
		maxZ = max.getZCoord();
	}
	
	@Override
	public boolean contains(int x, int y, int z)
	{
		if(x >= minX && x <= maxX)
		{
			if(y >= minY && y <= maxY)
			{
				if(z >= minZ && z <= maxZ)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public int getMinX()
	{
		return minX;
	}
	
	@Override
	public int getMinY()
	{
		return minY;
	}
	
	@Override
	public int getMinZ()
	{
		return minZ;
	}
	
	@Override
	public int getMaxX()
	{
		return maxX;
	}
	
	@Override
	public int getMaxY()
	{
		return maxY;
	}
	
	@Override
	public int getMaxZ()
	{
		return maxZ;
	}
	
	@Override
	public void onBlockChange()
	{
		provider.onStructureChange();
	}
	
	@Override
	public void onBlockBreak()
	{
		provider.onStructureChange();
	}
	
	@Override
	public void onInteract(EntityPlayer par1)
	{
		if(!StructureStorage.instance.isInteractionBoxColiding(world, this))
		{
			provider.onInteraction(par1);
		}
	}

	@Override
	public boolean isBoxActive()
	{
		return provider.hasValidTemplate();
	}
	
}
