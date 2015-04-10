package speiger.src.spmodapi.common.recipes.helper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import speiger.src.api.common.registry.recipes.IRecipeOverride.RecipeOverriderRegistry;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.items.crafting.ItemGear;
import speiger.src.spmodapi.common.items.crafting.ItemGear.GearType;
import speiger.src.spmodapi.common.util.data.ClassStorage;
import speiger.src.spmodapi.common.util.data.ClassStorage.DataStack;
import speiger.src.spmodapi.common.util.proxy.PathProxy;
import cpw.mods.fml.common.FMLLog;

public class RecipeOverrider
{
	public static void loadTransmutationRecipes()
	{
		ClassStorage clz = ClassStorage.getInstance();
		try
		{
			Class core = Class.forName("buildcraft.BuildCraftCore");
			clz.requestItem(core, Arrays.asList("woodGear", "stoneGear", "ironGear", "goldGear", "diaGear"), Arrays.asList("woodenGearItem", "stoneGearItem", "ironGearItem", "goldGearItem", "diamondGearItem"));
			
		}
		catch(Exception e)
		{
		}
		Map<ItemStack, String> replacements = new HashMap<ItemStack, String>();
		
		replacements.put(ItemGear.getGearFromType(GearType.Wood), "gearWood");
		replacements.put(ItemGear.getGearFromType(GearType.Cobblestone), "gearCobble");
		replacements.put(ItemGear.getGearFromType(GearType.Stone), "gearStone");
		replacements.put(ItemGear.getGearFromType(GearType.Iron), "gearIron");
		replacements.put(ItemGear.getGearFromType(GearType.Gold), "gearGold");
		replacements.put(ItemGear.getGearFromType(GearType.Diamond), "gearDiamond");
		replacements.put(clz.getStack("woodGear").getResult(), "gearWood");
		replacements.put(clz.getStack("stoneGear").getResult(), "gearStone");
		replacements.put(clz.getStack("ironGear").getResult(), "gearIron");
		replacements.put(clz.getStack("goldGear").getResult(), "gearGold");
		replacements.put(clz.getStack("diaGear").getResult(), "gearDiamond");
		replacements.put(new ItemStack(Item.stick), "stickWood");
		replacements.put(new ItemStack(Block.planks), "plankWood");
		replacements.put(new ItemStack(Block.planks, 1, Short.MAX_VALUE), "plankWood");
		replacements.put(new ItemStack(Block.stone), "stone");
		replacements.put(new ItemStack(Block.stone, 1, Short.MAX_VALUE), "stone");
		replacements.put(new ItemStack(Block.cobblestone), "cobblestone");
		replacements.put(new ItemStack(Block.cobblestone, 1, Short.MAX_VALUE), "cobblestone");
		
		ItemStack[] replaceStacks = replacements.keySet().toArray(new ItemStack[replacements.keySet().size()]);
		FMLLog.getLogger().info("Start");
		// Ignore recipes for the following items
		ItemStack[] exclusions = new ItemStack[] 
		{
			ItemGear.getGearFromType(GearType.Wood), 
			ItemGear.getGearFromType(GearType.Cobblestone), 
			ItemGear.getGearFromType(GearType.Stone),
			ItemGear.getGearFromType(GearType.Iron), 
			ItemGear.getGearFromType(GearType.Gold), 
			ItemGear.getGearFromType(GearType.Diamond), 
			clz.getStack("woodGear").getResult(), 
			clz.getStack("stoneGear").getResult(), 
			clz.getStack("goldGear").getResult(), 
			clz.getStack("diaGear").getResult(), 
			new ItemStack(Block.blockLapis), 
			new ItemStack(Item.cookie), 
			new ItemStack(Block.stoneBrick), 
			new ItemStack(Block.stoneSingleSlab), 
			new ItemStack(Block.stairsCobblestone), 
			new ItemStack(Block.cobblestoneWall), 
			new ItemStack(Block.stairsWoodOak), 
			new ItemStack(Block.stairsWoodBirch), 
			new ItemStack(Block.stairsWoodJungle), 
			new ItemStack(Block.stairsWoodSpruce), 
			getBCPipeWire() 
		};
		
		List recipes = CraftingManager.getInstance().getRecipeList();
		List<IRecipe> recipesToRemove = new ArrayList<IRecipe>();
		List<IRecipe> recipesToAdd = new ArrayList<IRecipe>();
		
		for(Object obj : recipes)
		{
			if(!RecipeOverriderRegistry.instance.canOverride((IRecipe)obj))
			{
				continue;
			}
			if(obj instanceof ShapedRecipes)
			{
				ShapedRecipes recipe = (ShapedRecipes)obj;
				ItemStack output = recipe.getRecipeOutput();
				if(output != null && containsMatch(false, exclusions, output))
				{
					continue;
				}
				
				if(containsMatch(true, recipe.recipeItems, replaceStacks))
				{
					ShapedOreRecipe cu = getRecipeChange(new int[] {recipe.recipeWidth, recipe.recipeHeight }, recipe.getRecipeOutput(), recipe.recipeItems, replacements);
					if(cu != null)
					{
						recipesToRemove.add(recipe);
						recipesToAdd.add(cu);
					}
				}
			}
			else if(obj instanceof ShapedOreRecipe)
			{
				ShapedOreRecipe recipe = (ShapedOreRecipe)obj;
				ItemStack output = recipe.getRecipeOutput();
				if(output != null && containsMatch(false, exclusions, output))
				{
					continue;
				}
				Object[] array = recipe.getInput();
				ItemStack[] items = new ItemStack[array.length];
				for(int i = 0;i < array.length;i++)
				{
					Object cu = array[i];
					if(cu != null)
					{
						
						if(cu instanceof ItemStack)
						{
							ItemStack check = (ItemStack)cu;
							items[i] = check;
						}
					}
				}
				
				if(containsMatch(true, items, replaceStacks))
				{
					int length = 0;
					int hight = 0;
					try
					{
						length = getField(ShapedOreRecipe.class, Integer.class, recipe, 4);
						hight = getField(ShapedOreRecipe.class, Integer.class, recipe, 5);
					}
					catch(Exception e)
					{
					}
					if(length != 0 && hight != 0)
					{
						ShapedOreRecipe cu = getRecipeChange(new int[] {length, hight }, recipe.getRecipeOutput(), recipe.getInput(), replacements);
						if(cu != null)
						{
							recipesToRemove.add(recipe);
							recipesToAdd.add(cu);
						}
					}
				}
				
			}
			else if(obj instanceof ShapelessRecipes)
			{
				ShapelessRecipes recipe = (ShapelessRecipes)obj;
				ItemStack output = recipe.getRecipeOutput();
				if(output != null && containsMatch(false, exclusions, output))
				{
					continue;
				}
				
				if(containsMatch(true, (ItemStack[])recipe.recipeItems.toArray(new ItemStack[recipe.recipeItems.size()]), replaceStacks))
				{
					recipesToRemove.add((IRecipe)obj);
					IRecipe newRecipe = getRecipeChange(recipe.getRecipeOutput(), recipe.recipeItems, replacements);
					recipesToAdd.add(newRecipe);
				}
			}
			else if(obj instanceof ShapelessOreRecipe)
			{
				ShapelessOreRecipe recipe = (ShapelessOreRecipe)obj;
				ItemStack output = recipe.getRecipeOutput();
				if(output != null && containsMatch(false, exclusions, output))
				{
					continue;
				}
				ArrayList array = recipe.getInput();
				ArrayList<ItemStack> items = new ArrayList<ItemStack>();
				for(Object cu : array)
				{
					if(cu != null && cu instanceof ItemStack)
					{
						items.add((ItemStack)cu);
					}
				}
				if(containsMatch(true, (ItemStack[])items.toArray(new ItemStack[items.size()]), replaceStacks))
				{
					recipesToRemove.add((IRecipe)obj);
					IRecipe newRecipe = getRecipeChange(recipe.getRecipeOutput(), array, replacements);
					recipesToAdd.add(newRecipe);
				}
				
			}
		}
		
		recipes.removeAll(recipesToRemove);
		recipes.addAll(recipesToAdd);
		if(recipesToRemove.size() > 0)
		{
			SpmodAPI.log.print("Replaced " + recipesToRemove.size() + " ore recipies");
			
		}
	}
	
	static ShapelessOreRecipe getRecipeChange(ItemStack output, List recipe, Map<ItemStack, String> repl)
	{
		ArrayList<Object> input = new ArrayList<Object>();
		for(Object ingred : recipe)
		{
			Object finalObj = ingred;
			if(ingred instanceof ItemStack)
			{
				ItemStack cu = (ItemStack)ingred;
				for(Entry<ItemStack, String> replace : repl.entrySet())
				{
					if(OreDictionary.itemMatches(replace.getKey(), cu, false))
					{
						finalObj = replace.getValue();
						break;
					}
				}
			}
			else if(ingred instanceof ArrayList)
			{
				finalObj = OreDictionary.getOreName(OreDictionary.getOreID((ItemStack)((ArrayList)ingred).get(0)));
			}
			input.add(finalObj);
		}
		return new ShapelessOreRecipe(output, input.toArray(new Object[input.size()]));
	}
	
	static ShapedOreRecipe getRecipeChange(int[] size, ItemStack output, Object[] recipe, Map<ItemStack, String> repl)
	{
		ClassStorage clzst = ClassStorage.getInstance();
		ItemStack[] att = new ItemStack[recipe.length];
		for(int i = 0;i < recipe.length;i++)
		{
			Object cu = recipe[i];
			if(cu != null)
			{
				
				if(cu instanceof ArrayList)
				{
					ArrayList<ItemStack> list = (ArrayList<ItemStack>)cu;
					repl.put(list.get(0), OreDictionary.getOreName(OreDictionary.getOreID(list.get(0))));
					att[i] = list.get(0);
					continue;
				}
				ItemStack current = (ItemStack)cu;
				att[i] = current;
			}
		}
		
		ShapedRecipes cu = new ShapedRecipes(size[0], size[1], att, output);
		
		ShapedOreRecipe shape = null;
		
		if(cu != null)
		{
			try
			{
				Class clz = Class.forName("net.minecraftforge.oredict.ShapedOreRecipe");
				Constructor cons = clz.getDeclaredConstructor(cu.getClass(), Map.class);
				cons.setAccessible(true);
				shape = (ShapedOreRecipe)cons.newInstance(cu, repl);
			}
			catch(Exception e)
			{
				FMLLog.getLogger().info("Test: " + e);
				String string = "";
				for(StackTraceElement el : e.getStackTrace())
				{
					string = String.format("%s%n%s", string, el);
				}
				FMLLog.getLogger().info("Test: " + string);
			}
		}
		
		return shape;
		
	}
	
	private static <T> T getField(Class<?> class1, Class<T> fieldType, Object instance, int fieldIndex) throws IllegalArgumentException, IllegalAccessException
	{
		Field field = class1.getDeclaredFields()[fieldIndex];
		field.setAccessible(true);
		return (T)field.get(instance);
	}
	
	private static boolean containsMatch(boolean strict, ItemStack[] inputs, ItemStack... targets)
	{
		for(ItemStack input : inputs)
		{
			for(ItemStack target : targets)
			{
				if(itemMatches(target, input, strict))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean itemMatches(ItemStack target, ItemStack input, boolean strict)
	{
		if(input == null && target != null || input != null && target == null)
		{
			return false;
		}
		return (target.itemID == input.itemID && ((target.getItemDamage() == PathProxy.getRecipeBlankValue() && !strict) || target.getItemDamage() == input.getItemDamage()));
	}
	
	public static ItemStack getBCPipeWire()
	{
		try
		{
			return new DataStack(Class.forName("buildcraft.BuildCraftTransport"), "pipeWaterproof").getResult();
		}
		catch(Exception e)
		{
			return new ItemStack(Block.blockLapis);
		}
	}
	
}
