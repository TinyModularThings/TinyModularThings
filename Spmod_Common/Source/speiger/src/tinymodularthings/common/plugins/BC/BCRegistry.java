package speiger.src.tinymodularthings.common.plugins.BC;

import mods.railcraft.api.fuel.FuelManager;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import speiger.src.spmodapi.common.items.crafting.ItemGear;
import speiger.src.spmodapi.common.items.crafting.ItemGear.GearType;
import speiger.src.spmodapi.common.util.proxy.PathProxy;
import speiger.src.tinymodularthings.common.config.TinyConfig;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.plugins.BC.actions.ActionOneSlotChange;
import speiger.src.tinymodularthings.common.plugins.BC.actions.BCActionHelper;
import speiger.src.tinymodularthings.common.plugins.BC.actions.BucketFillerAction;
import speiger.src.tinymodularthings.common.plugins.BC.actions.GateChangeToSlot;
import speiger.src.tinymodularthings.common.plugins.BC.triggers.BCTriggerHelper;
import buildcraft.BuildCraftEnergy;
import buildcraft.BuildCraftFactory;
import buildcraft.BuildCraftTransport;
import buildcraft.api.fuels.IronEngineFuel;
import buildcraft.api.fuels.IronEngineFuel.Fuel;
import buildcraft.api.gates.ActionManager;
import cpw.mods.fml.common.Loader;

public class BCRegistry
{
	private static BCRegistry instance = new BCRegistry();
	
	public static boolean overrideVanilla = false;
	private static BCVariables bc;
	
	public static void load()
	{
		instance.loadTrigger();
		instance.loadActions();
		if (overrideVanilla)
		{
			instance.overrideFurnace();
		}
		boolean result = TinyConfig.getBoolean(TinyConfig.general, "BuildCraft 1.4.7 PowerBackport", true).setComment("This thing set the generated power of Oil and Fuel back to 1.4.7 MC mode. That also contains Lava gen and other mods including that, Also lava on other Mods").getResult(TinyConfig.config);
		if (result)
		{
			instance.reload147();
		}
		instance.loadRecipes();
	}
	
	public void loadRecipes()
	{
		PathProxy.addRecipe(new ItemStack(TinyBlocks.machine, 1, 1), new Object[] { " X ", "YCY", " V ", 'X', BuildCraftTransport.pipeFluidsWood, 'V', BuildCraftTransport.pipeFluidsGold, 'Y', ItemGear.getGearFromType(GearType.Cobblestone), 'C', BuildCraftFactory.tankBlock });
		PathProxy.addRecipe(new ItemStack(TinyBlocks.machine, 1, 3), new Object[] { "XXX", "CVC", "XXX", 'X', Item.bucketWater, 'C', ItemGear.getGearFromType(GearType.Diamond), 'V', BuildCraftFactory.pumpBlock });
		PathProxy.addRecipe(new ItemStack(TinyBlocks.machine, 1, 2), new Object[] { "GEG", "EFE", "GEG", 'G', ItemGear.getGearFromType(GearType.Redstone), 'E', new ItemStack(BuildCraftEnergy.engineBlock, 1, 0), 'F', new ItemStack(TinyBlocks.machine, 1, 1) });
		try
		{
			OreDictionary.registerOre("Facade", new ItemStack(BuildCraftTransport.facadeItem, 1, PathProxy.getRecipeBlankValue()));
		}
		catch(Exception e)
		{
		}
	}
	
	public void loadTrigger()
	{
		ActionManager.registerTrigger(BCVariables.needFuel);
		ActionManager.registerTrigger(BCVariables.hasWork);
		ActionManager.registerTrigger(BCVariables.noWork);
		ActionManager.registerTrigger(BCVariables.requestPower);
		ActionManager.registerTrigger(BCVariables.storedFluid[0]);
		ActionManager.registerTrigger(BCVariables.storedFluid[1]);
		ActionManager.registerTrigger(BCVariables.storedFluid[2]);
		ActionManager.registerTrigger(BCVariables.pipeRequestPower);
		ActionManager.registerTriggerProvider(new BCTriggerHelper());
		if(Loader.isModLoaded("IC2"))
		{
			ActionManager.registerTrigger(BCVariables.energyFlows[0]);
			ActionManager.registerTrigger(BCVariables.energyFlows[1]);
		}
	}
	
	public void loadActions()
	{
		ActionManager.registerActionProvider(new BCActionHelper());
		for (int i = 0; i < BCVariables.changeToSlot.length; i++)
		{
			GateChangeToSlot action = new GateChangeToSlot(i);
			BCVariables.changeToSlot[i] = action;
			ActionManager.registerAction(action);
		}
		ActionManager.registerAction(BCVariables.changeOneTime[0] = new ActionOneSlotChange(false));
		ActionManager.registerAction(BCVariables.changeOneTime[1] = new ActionOneSlotChange(true));
		ActionManager.registerAction(BCVariables.bcFiller[0] = new BucketFillerAction(false));
		ActionManager.registerAction(BCVariables.bcFiller[1] = new BucketFillerAction(true));
	}
	
	public void overrideFurnace()
	{
		Block.blocksList[61] = null;
		Block.blocksList[62] = null;
		Block.blocksList[61] = new BlockModifiedFurnace(false);
		Block.blocksList[62] = new BlockModifiedFurnace(true);
		TileEntity.addMapping(TileEntityModifiedFurnace.class, "ModifiedFurnace");
	}
	
	public void reload147()
	{
		Fuel oil = IronEngineFuel.fuels.get("oil");
		Fuel fuel = IronEngineFuel.fuels.get("fuel");
		
		double oilmulti = (double) oil.totalBurningTime / (double) 5000;
		double fuelmulti = (double) fuel.totalBurningTime / (double) 25000;
		
		IronEngineFuel.addFuel("oil", oil.powerPerCycle, (int) (20000 * oilmulti));
		IronEngineFuel.addFuel("fuel", fuel.powerPerCycle, (int) (100000 * fuelmulti));
		IronEngineFuel.addFuel(FluidRegistry.LAVA, 2, 10000);
		if (Loader.isModLoaded("Railcraft"))
		{
			FuelManager.addBoilerFuel(fuel.liquid, 96000);
			Fluid fluid = FluidRegistry.getFluid("bioethanol");
			if (fluid != null)
			{
				FuelManager.addBoilerFuel(fluid, 32000);
			}
			fluid = FluidRegistry.getFluid("biofuel");
			if (fluid != null)
			{
				FuelManager.addBoilerFuel(fluid, 32000);
			}
		}
		
	}
}
