package speiger.src.spmodapi.common.config;

import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.init.Items;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.creativeTabs.TabCrafting;
import speiger.src.spmodapi.common.creativeTabs.TabHemp;
import speiger.src.spmodapi.common.creativeTabs.TabHempDeko;
import speiger.src.spmodapi.common.fluids.hemp.FluidHempResin;

public class APIUtilsConfig
{
	private static APIUtils utils;
	
	public static void register()
	{
		APIUtils.jumpBoost = new RangedAttribute("generic.jump", 1.0D, 0.0D, 1024.0D).setDescription("generic.jump").setShouldWatch(true);
		APIUtils.hempArmor = EnumHelper.addArmorMaterial("Hemp Armor", 0, new int[] { 0, 0, 0, 0 }, 0);
		utils.hempResin = new FluidHempResin();
		utils.tabHemp = new TabHemp();
		utils.tabHempDeko = new TabHempDeko();
		utils.tabCrafing = new TabCrafting();
		OreDictionary.registerOre("bone", Items.bone);
		
	}
}
