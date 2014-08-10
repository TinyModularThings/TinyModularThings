package speiger.src.tinymodularthings.common.entity.minecarts;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityMinecartContainer;
import net.minecraft.world.World;
import speiger.src.api.blocks.BlockStack;
import speiger.src.tinymodularthings.common.interfaces.IEntityGuiProvider;

public abstract class TCarts extends EntityMinecartContainer implements
		IEntityGuiProvider
{
	
	public TCarts(World world)
	{
		super(world);
	}
	
	public TCarts(World par1World, double par2, double par4, double par6)
	{
		super(par1World, par2, par4, par6);
	}
	
	@Override
	public int getMinecartType()
	{
		return -1;
	}
	
	@Override
	public Block func_145817_o()
	{
		return getRenderedBlock().getBlock();
	}
	
	@Override
	public int getDisplayTileData()
	{
		return getRenderedBlock().getMeta();
	}
	
	public abstract BlockStack getRenderedBlock();
	
	@Override
	protected void applyDrag()
	{
		motionX *= 0.991999979019165D;
		motionY *= 0.0D;
		motionZ *= 0.991999979019165D;
	}
	
}
