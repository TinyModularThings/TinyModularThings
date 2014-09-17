package speiger.src.api.items;

import net.minecraft.item.ItemStack;
import speiger.src.api.util.SpmodMod;

public interface LanguageItem
{
	
	/**
	 * For people who want a language name from a item of another mod.
	 * 
	 * @param par1
	 *            Item/Block itself
	 * @param par0
	 *            The mod which provide the names
	 * @return the name of an item
	 */
	String getDisplayName(ItemStack par1, SpmodMod par0);
	
	/**
	 * This function is only called to collect everything. Every mod has todo it
	 * himself.
	 * 
	 * @param id
	 *            Item/BlockID
	 * @param par0
	 *            Mod which want to request that
	 */
	void registerItems(int par1, SpmodMod par0);
}
