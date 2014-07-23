package speiger.src.tinymodularthings.common.blocks.ores;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import speiger.src.api.blocks.BlockStack;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.items.core.ItemBlockTinyChest;

public class ItemBlockMultiMineOre extends ItemBlockTinyChest
{
	private static HashMap<Block, String> names = new HashMap<Block, String>();
	
	public ItemBlockMultiMineOre(int par1)
	{
		super(par1);
	}
	
	public static void addName(Block par0, String name)
	{
		if (name == null || par0 == null)
		{
			return;
		}
		names.put(par0, name);
		
	}
	
	@Override
	public void registerItems(int id, SpmodMod par0)
	{
		if (!SpmodModRegistry.areModsEqual(par0, TinyModularThings.instance))
		{
			return;
		}
		if (id < Block.blocksList.length && Block.blocksList[id] != null)
		{
			LanguageRegister.getLanguageName(new BlockStack(id), names.get(new BlockStack(id).getBlock()), par0);
		}
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod par0)
	{
		if (par1.itemID < Block.blocksList.length && Block.blocksList[par1.itemID] != null)
		{
			return LanguageRegister.getLanguageName(new BlockStack(par1), names.get(new BlockStack(par1).getBlock()), par0);
		}
		return "Nothing";
	}
	
}
