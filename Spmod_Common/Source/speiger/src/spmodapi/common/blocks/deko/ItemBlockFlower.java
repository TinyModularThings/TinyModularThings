package speiger.src.spmodapi.common.blocks.deko;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import speiger.src.api.blocks.BlockStack;
import speiger.src.api.items.LanguageItem;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import speiger.src.spmodapi.SpmodAPI;

public class ItemBlockFlower extends ItemBlock implements LanguageItem
{

	public ItemBlockFlower(int par1)
	{
		super(par1);
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
	public String getDisplayName(ItemStack par1, SpmodMod par0)
	{
		return LanguageRegister.getLanguageName(new BlockStack(par1).getBlock(), "dye.blue", par0);
	}

	@Override
	public void registerItems(int id, SpmodMod par0)
	{
		if(!SpmodModRegistry.areModsEqual(par0, SpmodAPI.instance))
		{
			return;
		}
		
		LanguageRegister.getLanguageName(new BlockStack(id).getBlock(), "dye.blue", par0);
	}
	
	
	
	
}
