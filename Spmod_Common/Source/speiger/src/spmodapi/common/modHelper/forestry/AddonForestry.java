package speiger.src.spmodapi.common.modHelper.forestry;

import java.util.Collection;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import forestry.api.farming.Farmables;
import forestry.api.farming.IFarmable;
import forestry.api.recipes.RecipeManagers;
import forestry.core.GameMode;
import forestry.core.utils.RecipeUtil;

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
	}
}
