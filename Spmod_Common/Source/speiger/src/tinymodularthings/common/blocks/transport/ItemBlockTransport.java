package speiger.src.tinymodularthings.common.blocks.transport;

import net.minecraft.item.ItemStack;
import speiger.src.api.blocks.BlockStack;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.items.core.ItemBlockTinyChest;

public class ItemBlockTransport extends ItemBlockTinyChest
{
	
	public ItemBlockTransport(int par1)
	{
		super(par1);
		setHasSubtypes(true);
		setMaxDamage(0);
	}
	
	@Override
	public int getMetadata(int par1)
	{
		return par1;
	}
	
	@Override
	public void registerItems(int id, SpmodMod par0)
	{
		if (!SpmodModRegistry.areModsEqual(par0, TinyModularThings.instance))
		{
			return;
		}
		
		LanguageRegister.getLanguageName(new BlockStack(id, 0), "enderchest.proxy", par0);
		LanguageRegister.getLanguageName(new BlockStack(id, 1), "interface.item", par0);
		LanguageRegister.getLanguageName(new BlockStack(id, 2), "interface.fluid", par0);
		LanguageRegister.getLanguageName(new BlockStack(id, 3), "interface.energy", par0);
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod par0)
	{
		switch (par1.getItemDamage())
		{
			case 0:
				return LanguageRegister.getLanguageName(new BlockStack(par1), "enderchest.proxy", par0);
			case 1:
				return LanguageRegister.getLanguageName(new BlockStack(par1), "interface.item", par0);
			case 2:
				return LanguageRegister.getLanguageName(new BlockStack(par1), "interface.fluid", par0);
			case 3:
				return LanguageRegister.getLanguageName(new BlockStack(par1), "interface.energy", par0);
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				return "TinyHopper";
			default:
				return "";
		}
	}
	
}
