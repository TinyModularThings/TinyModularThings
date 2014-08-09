package speiger.src.spmodapi.common.blocks.utils;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
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
	String[] names = new String[]{"cobble.workbench", "exp.storage", "mob.machine", "entity.lurer"};
	public ItemBlockUtils(Block par1)
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
	public String getUnlocalizedName(ItemStack par1ItemStack)
	{
		return getDisplayName(par1ItemStack, SpmodAPI.instance);
	}

	@Override
	public void registerItems(Item item, SpmodMod par0)
	{
		if(!SpmodModRegistry.areModsEqual(par0, SpmodAPI.instance))
		{
			return;
		}
		for(int i = 0;i<names.length;i++)
		{
			LanguageRegister.getLanguageName(new BlockStack(item, i), names[i], par0);
		}
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod par0)
	{
		return LanguageRegister.getLanguageName(new BlockStack(par1), names[par1.getItemDamage()], par0);
	}

	
	 
	
}
