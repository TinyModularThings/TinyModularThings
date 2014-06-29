package speiger.src.tinymodularthings.common.blocks.storage;

import net.minecraft.item.ItemStack;
import speiger.src.api.blocks.BlockStack;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;
import speiger.src.tinymodularthings.common.items.core.ItemBlockTinyChest;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;

public class ItemBlockStorage extends ItemBlockTinyChest
{
	String[] names = new String[] { "tinychest", "tinytank", "advtinychest" };
	
	public ItemBlockStorage(int par1)
	{
		super(par1);
		setHasSubtypes(true);
	}
	
	@Override
	public int getMetadata(int par1)
	{
		return par1;
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod start)
	{
		return LanguageRegister.getLanguageName(new BlockStack(par1), names[par1.getItemDamage()], start);
	}
	
	@Override
	public void registerItems(int id, SpmodMod par0)
	{
		if (!par0.getName().equals(TinyModularThingsLib.Name))
		{
			return;
		}
		for (int i = 0; i < names.length; i++)
		{
			LanguageRegister.getLanguageName(new BlockStack(id, i), names[i], par0);
		}
		
	}
	
}
