package speiger.src.spmodapi.common.modHelper.forestry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import cpw.mods.fml.common.FMLLog;
import forestry.api.farming.Farmables;
import forestry.api.farming.IFarmable;
import forestry.api.recipes.RecipeManagers;
import forestry.core.GameMode;
import forestry.core.utils.RecipeUtil;
import forestry.factory.gadgets.MachineFermenter;
import forestry.factory.gadgets.MachineFermenter.Recipe;

public class AddonForestry
{
	private static AddonForestry addon = new AddonForestry();
	
	public static void loadForestryStuff()
	{
		addon.loadRecipes();
		Collection<IFarmable> list = Farmables.farmables.get("farmWheat");
		list.add(new Hanfaddon());
		Farmables.farmables.put("farmWheat", list);
		
	}
	
	public void loadRecipes()
	{
		RecipeManagers.squeezerManager.addRecipe(10, new ItemStack[] { new ItemStack(APIItems.hempSeed) }, FluidRegistry.getFluidStack("seedoil", 50));
		RecipeManagers.squeezerManager.addRecipe(10, new ItemStack[]{new ItemStack(APIItems.hemp)}, FluidRegistry.getFluidStack("hemp.resin", 25), new ItemStack(APIItems.compressedHemp), 10);
		RecipeManagers.squeezerManager.addRecipe(10, new ItemStack[]{new ItemStack(APIBlocks.hempStraw)}, FluidRegistry.getFluidStack("hemp.resin", 250), new ItemStack(APIItems.compressedHemp), 100);
		try
		{
			RecipeUtil.injectLeveledRecipe(new ItemStack(APIItems.hemp), GameMode.getGameMode().getIntegerSetting("fermenter.yield.sapling"), "biomass");
			RecipeUtil.injectLeveledRecipe(new ItemStack(APIBlocks.hempStraw), GameMode.getGameMode().getIntegerSetting("fermenter.yield.sapling")*10, "biomass");
		}
		catch (Exception e)
		{
			
		}
		
		if(SpmodConfig.forestrySeedOil)
		{
			FMLLog.getLogger().info("Start Overriding Recipes");
			
			FluidStack stack = FluidRegistry.getFluidStack("biomass", 1);
			FluidStack seed = FluidRegistry.getFluidStack("seedoil", 1);
			try
			{
				ArrayList<List<Integer>> keys = new ArrayList<List<Integer>>();
				ArrayList<Recipe> recipesToAdd = new ArrayList<Recipe>();
				if(stack != null)
				{
					ArrayList<Recipe> list = MachineFermenter.RecipeManager.recipes;
					for(int i = 0;i<list.size();i++)
					{
						Recipe core = list.get(i);
						if(core.output.isFluidEqual(stack))
						{
							if(!contains(keys, Arrays.asList(core.resource.itemID, core.resource.getItemDamage())))
							{
								keys.add(Arrays.asList(core.resource.itemID, core.resource.getItemDamage()));
								recipesToAdd.add(new Recipe(core.resource, core.fermentationValue, 1.5F, core.output, seed));
							}
						}
					}
				}
				MachineFermenter.RecipeManager.recipes.addAll(recipesToAdd);
			}
			catch (Exception e)
			{
			}
			
			FMLLog.getLogger().info("Finish Overriding Recipes");
		}
		
	}
	
	public static boolean contains(ArrayList<List<Integer>> par1, List<Integer> par2)
	{
		for(int i = 0;i<par1.size();i++)
		{
			List<Integer> keys = par1.get(i);
			if(keys.get(0) == par2.get(0) && keys.get(1) == par2.get(1))
			{
				return true;
			}
		}
		return false;
	}
}
