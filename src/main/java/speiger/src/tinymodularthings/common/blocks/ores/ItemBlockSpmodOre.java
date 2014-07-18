package speiger.src.tinymodularthings.common.blocks.ores;

import net.minecraft.item.ItemStack;
import speiger.src.api.blocks.BlockStack;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.items.core.ItemBlockTinyChest;

public class ItemBlockSpmodOre extends ItemBlockTinyChest
{
	
	private String[] oreNames = new String[] { "oreCopper", "oreTin", "oreSilver", "oreLead", "oreBauxit", "oreIridium" };
	
	public ItemBlockSpmodOre(int par1)
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
		return LanguageRegister.getLanguageName(new BlockStack(par1), oreNames[par1.getItemDamage()], start);
	}
	
	@Override
	public void registerItems(int id, SpmodMod par0)
	{
		if (!SpmodModRegistry.areModsEqual(par0, TinyModularThings.instance))
		{
			return;
		}
		for (int i = 0; i < oreNames.length; i++)
		{
			LanguageRegister.getLanguageName(new BlockStack(id, i), oreNames[i], par0);
		}
		
	}
	
}
