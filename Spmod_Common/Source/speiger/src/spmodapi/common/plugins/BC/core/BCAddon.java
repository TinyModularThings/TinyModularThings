package speiger.src.spmodapi.common.plugins.BC.core;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;
import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.enums.EnumColor;
import speiger.src.spmodapi.common.items.crafting.ItemGear.GearType;
import speiger.src.spmodapi.common.plugins.BC.actions.*;
import speiger.src.spmodapi.common.util.ForgeRegister;
import speiger.src.spmodapi.common.util.proxy.PathProxy;
import buildcraft.BuildCraftCore;
import buildcraft.BuildCraftTransport;
import buildcraft.api.fuels.IronEngineFuel;
import buildcraft.api.gates.ActionManager;
import buildcraft.api.gates.IAction;
import buildcraft.api.transport.FacadeManager;

public class BCAddon
{	
	public static void init()
	{
		for(String oreName : OreDictionary.getOreNames())
		{
			for(ItemStack ore : OreDictionary.getOres(oreName))
			{
				onOreRegister(new OreDictionary.OreRegisterEvent(oreName, ore));
			}
		}
		loadRecipes();
		loadFacades();
		ActionLoader.init();
	}
	
	static void loadFacades()
	{
		for (int i = 0; i < 16; i++)
		{
			FacadeManager.addFacade(new ItemStack(APIBlocks.hempBlock, 1, i));
			FacadeManager.addFacade(new ItemStack(APIBlocks.hempBrick, 1, i));
			FacadeManager.addFacade(new ItemStack(APIBlocks.hempBlockPlated, 1, i));
			FacadeManager.addFacade(new ItemStack(APIBlocks.hempBrickPlated, 1, i));
			FacadeManager.addFacade(new ItemStack(APIBlocks.savedHempBlock, 1, i));
			FacadeManager.addFacade(new ItemStack(APIBlocks.savedHempBrick, 1, i));
			FacadeManager.addFacade(new ItemStack(APIBlocks.savedHempBlockPlated, 1, i));
			FacadeManager.addFacade(new ItemStack(APIBlocks.savedHempBrickPlated, 1, i));
		}
	}
	
	static void loadRecipes()
	{
		OreDictionary.registerOre("gearCobble", BuildCraftCore.stoneGearItem);
		PathProxy.addFurnaceRecipe(new ItemStack(BuildCraftCore.stoneGearItem), GearType.Stone.getItem(), 0.1F);
		PathProxy.addRecipe(new ItemStack(BuildCraftTransport.pipeWaterproof, 16), new Object[] { "XXX", "XYX", "XXX", 'X', APIItems.hemp, 'Y', Item.slimeBall });
		PathProxy.removeRecipeS(new ItemStack(APIItems.colorCard, 1, EnumColor.GREEN.getAsDye()), BuildCraftTransport.pipeWaterproof);
		IronEngineFuel.addFuel(APIUtils.animalGas, 3, 7500);
	}
	
	
	public static void onOreRegister(OreRegisterEvent par0)
	{
		if (par0.Name.equalsIgnoreCase("gearStone"))
		{
			try
			{
				if (par0.Ore.getItem() == GameRegistry.findItem("BuildCraft|Core", "stoneGearItem"))
				{
					OreDictionary.getOres(par0.Name).remove(par0.Ore);
				}
			}
			catch (Exception e)
			{
			}
		}
	}
}
