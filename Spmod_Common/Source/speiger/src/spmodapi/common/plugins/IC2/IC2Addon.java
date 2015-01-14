package speiger.src.spmodapi.common.plugins.IC2;

import ic2.api.crops.Crops;
import ic2.api.item.Items;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.Recipes;
import ic2.core.block.crop.IC2Crops;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.util.proxy.PathProxy;
import buildcraft.api.fuels.IronEngineCoolant;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;

public class IC2Addon
{
	public static void LoadIC2()
	{
		short ID = Crops.instance.registerCrop(new HempCrop());
		if (ID != -1)
		{
			Crops.instance.registerBaseSeed(new ItemStack(APIItems.hempSeed, 2, PathProxy.getRecipeBlankValue()), ID, 1, 1, 1, 1);
		}
		
		try
		{
			IC2Crops crops = (IC2Crops) Crops.instance;
			crops.registerBaseSeed(new ItemStack(APIBlocks.blueFlower, 4, 0), crops.cropBlueFlower.getId(), 4, 1, 1, 1);
		}
		catch (Exception e)
		{
			FMLLog.getLogger().info("Crash");
		}
		
		ItemStack stack = Items.getItem("cell");
		if(stack != null)
		{
			FluidContainerRegistry.registerFluidContainer(new FluidContainerData(new FluidStack(APIUtils.animalGas, 1000), new ItemStack(APIItems.gasCell), stack));
			FluidContainerRegistry.registerFluidContainer(new FluidContainerData(new FluidStack(APIUtils.animalGas, 5000), new ItemStack(APIItems.gasCellC), new ItemStack(APIItems.gasCell)));
			PathProxy.addSRecipe(new ItemStack(APIItems.gasCell), new Object[]{stack, APIItems.gasBucket});
		}
		Recipes.compressor.addRecipe(new RecipeInputItemStack(new ItemStack(APIItems.gasCell, 5)), null, new ItemStack(APIItems.gasCellC));
		if(Loader.isModLoaded("Buildcraft|Core"))
		{
			IronEngineCoolant.addCoolant(FluidRegistry.getFluid("ic2coolant"), 5F);			
		}
	}
}
