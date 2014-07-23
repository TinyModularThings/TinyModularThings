package speiger.src.tinymodularthings.common.plugins.Ic2;

import ic2.api.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import cpw.mods.fml.common.Loader;

public class IC2IridiumPlugin
{
	public static void createIridiumShapeRecipe()
	{
		if (Loader.isModLoaded("IC2"))
		{
			if (Loader.isModLoaded("factorization"))
			{
				try
				{
					ItemStack iridum = Items.getItem("iridiumOre");
					Class.forName("factorization.common.TileEntityCrystallizer").getMethod("addRecipe", ItemStack.class, Float.class).invoke(null, new ItemStack(TinyBlocks.ores, 5), iridum, 2.0F, new ItemStack(Item.diamond));
				}
				catch (Exception e)
				{
					TinyModularThings.log.print("Could not load Factorisation Iridium Recipe: " + e.getLocalizedMessage());
				}
			}
			else
			{
				
			}
		}
	}
}
