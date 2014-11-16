package speiger.src.spmodapi.common.config;

import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.item.Item;
import net.minecraftforge.common.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.creativeTabs.TabCrafting;
import speiger.src.spmodapi.common.creativeTabs.TabHemp;
import speiger.src.spmodapi.common.creativeTabs.TabHempDeko;
import speiger.src.spmodapi.common.fluids.gas.FluidAnimalGas;
import speiger.src.spmodapi.common.fluids.hemp.FluidHempResin;
import speiger.src.spmodapi.common.material.GasMaterial;

public class APIUtilsConfig
{
	private static APIUtils utils;
	
	public static void register()
	{
		utils.jumpBoost = new RangedAttribute("generic.jump", 1.0D, 0.0D, 1024.0D).func_111117_a("generic.jump").setShouldWatch(true);
		utils.hempArmor = EnumHelper.addArmorMaterial("Hemp Armor", 0, new int[] { 0, 0, 0, 0 }, 0);
		utils.hempResin = new FluidHempResin();
		utils.tabHemp = new TabHemp();
		utils.tabHempDeko = new TabHempDeko();
		utils.tabCrafing = new TabCrafting();
		utils.gasMaterial = new GasMaterial();
		utils.animalGas = new FluidAnimalGas();
		OreDictionary.registerOre("bone", Item.bone);
		
	}
}
