package speiger.src.spmodapi.common.recipes;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.items.crafting.ItemGear;
import speiger.src.spmodapi.common.items.crafting.ItemGear.GearType;
import speiger.src.spmodapi.common.recipes.basic.BasicRecipes;
import speiger.src.spmodapi.common.recipes.basic.ExpRecipes;
import speiger.src.spmodapi.common.recipes.basic.HempRecipes;
import speiger.src.spmodapi.common.recipes.basic.LampRecipes;
import speiger.src.spmodapi.common.util.proxy.PathProxy;
import buildcraft.BuildCraftCore;
import cpw.mods.fml.common.FMLLog;

public class SpmodRecipeRegistry
{
	public static void loadRecipes()
	{
		PathProxy proxy = new PathProxy();
		BasicRecipes.load(proxy);
		HempRecipes.load(proxy);
		LampRecipes.load(proxy);
		ExpRecipes.load(proxy);
	}
	



}
