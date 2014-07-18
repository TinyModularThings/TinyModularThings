package speiger.src.api.items;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ItemCollection
{
	public static ItemStack[] getAllWool()
	{
		return new ItemStack[]{
			new ItemStack(Blocks.wool, 1, 0), 
			new ItemStack(Blocks.wool, 1, 1), 
			new ItemStack(Blocks.wool, 1, 2), 
			new ItemStack(Blocks.wool, 1, 3), 
			new ItemStack(Blocks.wool, 1, 4),
			new ItemStack(Blocks.wool, 1, 5),
			new ItemStack(Blocks.wool, 1, 6),
			new ItemStack(Blocks.wool, 1, 7),
			new ItemStack(Blocks.wool, 1, 8),
			new ItemStack(Blocks.wool, 1, 9),
			new ItemStack(Blocks.wool, 1, 10),
			new ItemStack(Blocks.wool, 1, 11),
			new ItemStack(Blocks.wool, 1, 12),
			new ItemStack(Blocks.wool, 1, 13),
			new ItemStack(Blocks.wool, 1, 14),
			new ItemStack(Blocks.wool, 1, 15)
		};
	}
	
	public static ItemStack[] getAllDye()
	{
		return new ItemStack[]{
				new ItemStack(Items.dye, 1, 0), 
				new ItemStack(Items.dye, 1, 1), 
				new ItemStack(Items.dye, 1, 2), 
				new ItemStack(Items.dye, 1, 3), 
				new ItemStack(Items.dye, 1, 4),
				new ItemStack(Items.dye, 1, 5),
				new ItemStack(Items.dye, 1, 6),
				new ItemStack(Items.dye, 1, 7),
				new ItemStack(Items.dye, 1, 8),
				new ItemStack(Items.dye, 1, 9),
				new ItemStack(Items.dye, 1, 10),
				new ItemStack(Items.dye, 1, 11),
				new ItemStack(Items.dye, 1, 12),
				new ItemStack(Items.dye, 1, 13),
				new ItemStack(Items.dye, 1, 14),
				new ItemStack(Items.dye, 1, 15)
			};
	}
}
