package speiger.src.spmodapi.common.blocks.hemp;

import net.minecraft.item.ItemBlock;

public class ItemBlockHempDekoBase extends ItemBlock
{
	String[] names = new String[] { "hemp.base", "hemp.brick", "hemp.base.nice", "hemp.brick.nice" };
	
	public ItemBlockHempDekoBase(int par1)
	{
		super(par1);
		this.setHasSubtypes(true);
	}
	
	@Override
	public int getMetadata(int par1)
	{
		return par1;
	}
	
}
