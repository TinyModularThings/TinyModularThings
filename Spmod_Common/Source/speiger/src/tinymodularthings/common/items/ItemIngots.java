package speiger.src.tinymodularthings.common.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import speiger.src.tinymodularthings.common.items.core.TinyItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemIngots extends TinyItem
{	
	public ItemIngots(int par1)
	{
		super(par1);
		setHasSubtypes(true);
		setMaxStackSize(64);
		setCreativeTab(CreativeTabs.tabFood);
		OreDictionary.registerOre("ingotCopper", new ItemStack(this, 1, 0));
		OreDictionary.registerOre("ingotTin", new ItemStack(this, 1, 1));
		OreDictionary.registerOre("ingotSilver", new ItemStack(this, 1, 3));
		OreDictionary.registerOre("ingotLead", new ItemStack(this, 1, 4));
		OreDictionary.registerOre("ingotBronze", new ItemStack(this, 1, 5));
	}
	
	@Override
	public String getName(ItemStack par1)
	{
		switch(par1.getItemDamage())
		{
			case 0: return "Copper Ingot";
			case 1: return "Tin Ingot";
			case 2: return "Aluminium Ingot";
			case 3: return "Silver Ingot";
			case 4: return "Lead Ingot";
			case 5: return "Bronze Ingot";
			case 6: return "Iridium Ingot";
		}
		return null;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3)
	{
		for (int i = 0; i < 7; i++)
		{
			par3.add(new ItemStack(par1, 1, i));
		}
	}
}
