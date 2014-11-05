package speiger.src.tinymodularthings.common.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.oredict.OreDictionary;
import speiger.src.tinymodularthings.common.items.core.TinyItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemIngots extends TinyItem
{
	
	String[] ingots = new String[] { "ingotCopper", "ingotTin", "ingotAluminium", "ingotSilver", "ingotLead", "ingotBronze", "ingotIridium" };
	
	Icon[] ingotTextures = new Icon[ingots.length];
	
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
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3)
	{
		for (int i = 0; i < ingots.length; i++)
		{
			par3.add(new ItemStack(par1, 1, i));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int par1)
	{
		return super.getIconFromDamage(par1);
	}

	
	
}
