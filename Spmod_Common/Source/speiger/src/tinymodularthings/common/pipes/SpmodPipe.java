package speiger.src.tinymodularthings.common.pipes;

import net.minecraft.item.ItemStack;
import buildcraft.transport.ItemPipe;

public class SpmodPipe extends ItemPipe
{
	String languageName;
	
	public SpmodPipe(int i, String name)
	{
		super(i);
		languageName = name;
	}

	@Override
	public String getItemDisplayName(ItemStack itemstack)
	{
		return languageName;
	}
	
	
	
}
