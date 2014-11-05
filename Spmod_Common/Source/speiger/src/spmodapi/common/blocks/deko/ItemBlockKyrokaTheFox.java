package speiger.src.spmodapi.common.blocks.deko;

import net.minecraft.item.ItemBlock;

public class ItemBlockKyrokaTheFox extends ItemBlock
{

	public ItemBlockKyrokaTheFox(int par1)
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
