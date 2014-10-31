package speiger.src.spmodapi.common.blocks.utils;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import speiger.src.api.blocks.BlockStack;
import speiger.src.api.items.LanguageItem;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import speiger.src.spmodapi.SpmodAPI;

public class ItemBlockUtils extends ItemBlock implements LanguageItem
{
	String[] names = new String[] { "cobble.workbench", "exp.storage", "mob.machine", "entity.lurer", "Inventory Accesser"};
	
	public ItemBlockUtils(int par1)
	{
		super(par1);
		this.setHasSubtypes(true);
	}
	
	@Override
	public int getMetadata(int par1)
	{
		return par1;
	}
	
	@Override
	public String getItemDisplayName(ItemStack par1ItemStack)
	{
		return getDisplayName(par1ItemStack, SpmodAPI.instance);
	}
	
	@Override
	public void registerItems(int id, SpmodMod par0)
	{
		if (!SpmodModRegistry.areModsEqual(par0, SpmodAPI.instance))
		{
			return;
		}
		for (int i = 0; i < names.length; i++)
		{
			LanguageRegister.getLanguageName(new BlockStack(id, i), names[i], par0);
		}
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod par0)
	{
		return LanguageRegister.getLanguageName(new BlockStack(par1), names[par1.getItemDamage()], par0);
	}
	
}
