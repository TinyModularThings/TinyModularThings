package speiger.src.spmodapi.common.items.crafting;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;
import speiger.src.api.items.DisplayItem;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.items.SpmodItem;
import speiger.src.spmodapi.common.lib.SpmodAPILib;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemGear extends SpmodItem
{
	
	public ItemGear()
	{
		this.setHasSubtypes(true);
		this.setCreativeTab(APIUtils.tabCrafing);
	}
	

	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for(GearType type : GearType.values())
		{
			par3List.add(getGearFromType(type));
		}
	}



	@Override
	public void registerItems(Item item, SpmodMod par0)
	{
		if(!SpmodModRegistry.areModsEqual(par0, getMod()))
		{
			return;
		}
		for(int i = 0;i<textures.length;i++)
		{
			LanguageRegister.getLanguageName(new DisplayItem(item), "gear."+GearType.values()[i].getName(), par0);
		}
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod par0)
	{
		return LanguageRegister.getLanguageName(new DisplayItem(par1.getItem()), "gear."+GearType.values()[par1.getItemDamage()].getName(), par0);
	}
	
	IIcon[] textures = new IIcon[GearType.values().length];
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister)
	{
		for(int i = 0;i<textures.length;i++)
		{
			this.textures[i] = par1IconRegister.registerIcon(SpmodAPILib.ModID.toLowerCase()+":crafting/gear."+GearType.values()[i].getName());
		}
	}
	
	public static ItemStack getGearFromType(GearType par1)
	{
		return new ItemStack(APIItems.gears, 1, par1.ordinal());
	}
	
	public static ItemStack getGearFromType(GearType par1, int qty)
	{
		return new ItemStack(APIItems.gears, qty, par1.ordinal());
	}



	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int par1)
	{
		return textures[par1];
	}


	public static enum GearType
	{
		Wood("wood"),
		Cobblestone("cobble"),
		Stone("stone"),
		Iron("iron"),
		Gold("gold"),
		Diamond("diamond"),
		Redstone("redstone"),
		Bone("bone"),
		WoodenRing("part.ring.wood"),
		CobbleRing("part.ring.cobble"),
		StoneRing("part.ring.stone"),
		StoneCorner("part.corner.stone"),
		WoodenCorner("part.corner.wood");
		
		String name;
		private GearType(String name)
		{
			this.name = name;

		}
		
		public String getName()
		{
			return name;
		}
		
	}
	
}
