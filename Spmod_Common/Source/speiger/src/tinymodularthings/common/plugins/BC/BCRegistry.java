package speiger.src.tinymodularthings.common.plugins.BC;

import mods.railcraft.api.fuel.FuelManager;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import speiger.src.api.common.registry.recipes.output.RecipeOutput;
import speiger.src.api.common.registry.recipes.uncrafter.UncrafterRecipeList;
import speiger.src.spmodapi.common.items.crafting.ItemGear;
import speiger.src.spmodapi.common.items.crafting.ItemGear.GearType;
import speiger.src.spmodapi.common.util.proxy.PathProxy;
import speiger.src.spmodapi.common.util.proxy.RegisterProxy;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.config.TinyConfig;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.enums.EnumIngots;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;
import speiger.src.tinymodularthings.common.pipes.*;
import speiger.src.tinymodularthings.common.plugins.BC.actions.ActionOneSlotChange;
import speiger.src.tinymodularthings.common.plugins.BC.actions.BCActionHelper;
import speiger.src.tinymodularthings.common.plugins.BC.actions.BucketFillerAction;
import speiger.src.tinymodularthings.common.plugins.BC.actions.GateChangeToSlot;
import speiger.src.tinymodularthings.common.plugins.BC.triggers.BCTriggerHelper;
import buildcraft.*;
import buildcraft.api.fuels.IronEngineFuel;
import buildcraft.api.fuels.IronEngineFuel.Fuel;
import buildcraft.api.gates.ActionManager;
import buildcraft.api.recipes.AssemblyRecipe;
import buildcraft.transport.BlockGenericPipe;
import buildcraft.transport.ItemPipe;
import buildcraft.transport.Pipe;
import buildcraft.transport.PipeTransportPower;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;

public class BCRegistry
{
	private static BCRegistry instance = new BCRegistry();
	
	public static boolean overrideVanilla = false;
	private static BCVariables bc;
	
	public static void load()
	{
		overrideVanilla = TinyConfig.getConfig().getBoolean(TinyConfig.getConfig().general, "Vanilla BC Intigration", true).setComment("Make the Vanilla Furnace Compatioble to Gates").getResult(TinyConfig.getConfig().config);
		TinyConfig.getConfig().config.save();
		instance.loadTrigger();
		instance.loadActions();
		instance.loadItems(TinyConfig.getConfig());
		if(overrideVanilla)
		{
			instance.overrideFurnace();
		}
		boolean result = TinyConfig.getBoolean(TinyConfig.general, "BuildCraft 1.4.7 PowerBackport", true).setComment("This thing set the generated power of Oil and Fuel back to 1.4.7 MC mode. That also contains Lava gen and other mods including that, Also lava on other Mods").getResult(TinyConfig.config);
		if(result)
		{
			instance.reload147();
		}
		instance.loadRecipes();
	}
	
	public void loadItems(TinyConfig tiny)
	{
		if(!TinyConfig.pipesEnabled)
		{
			return;
		}
		try
		{
			PipeTransportPower.powerCapacities.put(PipeEmeraldExtractionPower.class, 2048);
			TinyItems.emeraldPowerPipeE = BuildItem(tiny.pipes.getCurrentID(), PipeEmeraldExtractionPower.class, "Emerald Power Extraction Pipe");
			tiny.pipes.updateToNextID();
			AssemblyRecipe.assemblyRecipes.add(new AssemblyRecipe(new ItemStack[] {new ItemStack(BuildCraftTransport.pipeItemsEmerald, 8), new ItemStack(Item.redstone, 8) }, 10000, new ItemStack(TinyItems.emeraldPowerPipeE, 8)));
			PathProxy.addSRecipe(new ItemStack(BuildCraftTransport.pipeItemsEmerald), new Object[] {TinyItems.emeraldPowerPipeE });
			
			PipeTransportPower.powerCapacities.put(PipeEmeraldPower.class, 2048);
			TinyItems.emeraldPowerPipe = BuildItem(tiny.pipes.getCurrentID(), PipeEmeraldPower.class, "Emerald Power Pipe");
			tiny.pipes.updateToNextID();
			PathProxy.addSRecipe(new ItemStack(TinyItems.emeraldPowerPipe), new Object[] {BuildCraftTransport.pipeItemsEmerald, Item.redstone });
			PathProxy.addSRecipe(new ItemStack(BuildCraftTransport.pipeItemsEmerald), new Object[] {TinyItems.emeraldPowerPipe });
			
			TinyItems.redstoneFluidPipe = BuildItem(tiny.pipes.getCurrentID(), FluidRegstonePipe.class, "Redstone Fluid Pipe");
			tiny.pipes.updateToNextID();
			PathProxy.addSRecipe(new ItemStack(TinyItems.redstoneFluidPipe, 1), new Object[] {new ItemStack(BuildCraftTransport.pipeFluidsGold), new ItemStack(Item.redstone) });
			PathProxy.addSRecipe(new ItemStack(BuildCraftTransport.pipeFluidsGold, 1), new Object[] {TinyItems.redstoneFluidPipe });
			
			PipeTransportPower.powerCapacities.put(RefinedDiamondPowerPipe.class, 512);
			TinyItems.refinedDiamondPowerPipe = BuildItem(tiny.pipes.getCurrentID(), RefinedDiamondPowerPipe.class, "Diamond Power Pipe");
			AssemblyRecipe.assemblyRecipes.add(new AssemblyRecipe(new ItemStack[] {new ItemStack(BuildCraftTransport.pipePowerDiamond), new ItemStack(BuildCraftSilicon.redstoneChipset, 2, 2) }, 1000000, new ItemStack(TinyItems.refinedDiamondPowerPipe)));
			PathProxy.addSRecipe(new ItemStack(BuildCraftTransport.pipePowerDiamond), new Object[] {TinyItems.refinedDiamondPowerPipe });
			PathProxy.addRecipe(new ItemStack(TinyItems.refinedDiamondPowerPipe), new Object[] {"XXX", "XYX", "XXX", 'X', new ItemStack(BuildCraftSilicon.redstoneChipset, 1, 3), 'Y', BuildCraftTransport.pipePowerDiamond });
			tiny.pipes.updateToNextID();
			
			TinyItems.redstoneItemPipe = BuildItem(tiny.pipes.getCurrentID(), ItemRedstonePipe.class, "Item Redstone Pipe");
			tiny.pipes.updateToNextID();
			PathProxy.addSRecipe(new ItemStack(TinyItems.redstoneItemPipe), new Object[] {BuildCraftTransport.pipeItemsGold, Item.redstone });
			PathProxy.addSRecipe(new ItemStack(TinyItems.redstoneFluidPipe), new Object[] {TinyItems.redstoneItemPipe, BuildCraftTransport.pipeWaterproof });
			
			TinyItems.aluPipe = BuildItem(tiny.pipes.getCurrentID(), AluFluidExtractionPipe.class, "Aluminium Fluid Pipe");
			AluFluidExtractionPipe.init(TinyItems.aluPipe.itemID);
			PathProxy.addRecipe(new ItemStack(TinyItems.aluPipe, 8), new Object[] {"XYX", "CVC", "XBX", 'V', Block.glass, 'C', EnumIngots.Aluminum.getIngot(), 'X', BuildCraftTransport.pipeWaterproof, 'B', new ItemStack(BuildCraftSilicon.redstoneChipset, 1, 2), 'Y', new ItemStack(BuildCraftTransport.pipeGateAutarchic, 1, 1) });
			tiny.pipes.updateToNextID();
			
		}
		catch(Exception e)
		{
			
		}
	}
	
	public void loadRecipes()
	{
		PathProxy.addRecipe(new ItemStack(TinyBlocks.machine, 1, 1), new Object[] {" X ", "YCY", " V ", 'X', BuildCraftTransport.pipeFluidsWood, 'V', BuildCraftTransport.pipeFluidsGold, 'Y', ItemGear.getGearFromType(GearType.Cobblestone), 'C', BuildCraftFactory.tankBlock });
		PathProxy.addRecipe(new ItemStack(TinyBlocks.machine, 1, 3), new Object[] {"XXX", "CVC", "XXX", 'X', Item.bucketWater, 'C', ItemGear.getGearFromType(GearType.Diamond), 'V', BuildCraftFactory.pumpBlock });
		PathProxy.addRecipe(new ItemStack(TinyBlocks.machine, 1, 2), new Object[] {"GEG", "EFE", "GEG", 'G', ItemGear.getGearFromType(GearType.Redstone), 'E', new ItemStack(BuildCraftEnergy.engineBlock, 1, 0), 'F', new ItemStack(TinyBlocks.machine, 1, 1) });
		try
		{
			OreDictionary.registerOre("Facade", new ItemStack(BuildCraftTransport.facadeItem, 1, PathProxy.getRecipeBlankValue()));
		}
		catch(Exception e)
		{
		}
		UncrafterRecipeList un = UncrafterRecipeList.getInstance();
		un.addUncraftingRecipe(new ItemStack(BuildCraftEnergy.engineBlock), new RecipeOutput(Block.pistonBase, 80));
		un.addUncraftingRecipe(new ItemStack(BuildCraftEnergy.engineBlock), new RecipeOutput(Block.pistonBase, 60));
		un.addUncraftingRecipe(new ItemStack(BuildCraftEnergy.engineBlock), new RecipeOutput(Block.pistonBase, 40), new RecipeOutput(BuildCraftCore.ironGearItem, 40), new RecipeOutput(BuildCraftCore.ironGearItem, 40));
		un.addUncraftingRecipe(new ItemStack(BuildCraftBuilders.architectBlock), new RecipeOutput(BuildCraftCore.goldGearItem, 60), new RecipeOutput(Block.workbench, 95), new RecipeOutput(Block.chest, 95));
		un.addUncraftingRecipe(new ItemStack(BuildCraftFactory.hopperBlock), new RecipeOutput(new ItemStack(Item.ingotIron, 2), 75), new RecipeOutput(Item.ingotIron, 50), new RecipeOutput(Item.ingotIron, 50));
		un.addUncraftingRecipe(new ItemStack(BuildCraftFactory.refineryBlock), new RecipeOutput(Item.diamond, 75), new RecipeOutput(new ItemStack(BuildCraftFactory.tankBlock, 2), 80), new RecipeOutput(BuildCraftFactory.tankBlock, 60));
		un.addUncraftingRecipe(new ItemStack(BuildCraftFactory.miningWellBlock), new RecipeOutput(BuildCraftCore.ironGearItem, 75), new RecipeOutput(Item.pickaxeIron, 50));
		un.addUncraftingRecipe(new ItemStack(BuildCraftFactory.pumpBlock), new RecipeOutput(BuildCraftFactory.miningWellBlock, 95), new RecipeOutput(BuildCraftFactory.tankBlock, 25));
		un.addUncraftingRecipe(new ItemStack(BuildCraftFactory.floodGateBlock), new RecipeOutput(BuildCraftCore.ironGearItem, 85), new RecipeOutput(BuildCraftFactory.tankBlock, 75), new RecipeOutput(new ItemStack(Item.ingotIron, 2), 50), new RecipeOutput(new ItemStack(Item.ingotIron, 2), 50));
		un.addUncraftingRecipe(new ItemStack(BuildCraftFactory.quarryBlock), new RecipeOutput(new ItemStack(BuildCraftCore.diamondGearItem, 2), 65), new RecipeOutput(new ItemStack(BuildCraftCore.goldGearItem, 2), 75), new RecipeOutput(new ItemStack(BuildCraftCore.ironGearItem, 2), 90), new RecipeOutput(Item.pickaxeDiamond, 75));
		un.addUncraftingRecipe(new ItemStack(BuildCraftTransport.filteredBufferBlock), new RecipeOutput(Block.pistonBase, 75), new RecipeOutput(BuildCraftTransport.pipeItemsDiamond, 60), new RecipeOutput(Block.chest, 85));
		un.addUncraftingRecipe(new ItemStack(BuildCraftSilicon.laserBlock), new RecipeOutput(new ItemStack(Item.diamond, 2), 50), new RecipeOutput(new ItemStack(Item.redstone, 3), 75), new RecipeOutput(Block.obsidian, 80));
		un.addUncraftingRecipe(new ItemStack(BuildCraftSilicon.assemblyTableBlock, 1, 0), new RecipeOutput(new ItemStack(Block.obsidian, 3), 50), new RecipeOutput(new ItemStack(Block.obsidian, 3), 50), new RecipeOutput(Item.diamond, 60), new RecipeOutput(BuildCraftCore.diamondGearItem, 40));
		un.addUncraftingRecipe(new ItemStack(BuildCraftSilicon.assemblyTableBlock, 1, 1), new RecipeOutput(new ItemStack(Block.obsidian, 3), 50), new RecipeOutput(new ItemStack(Block.obsidian, 3), 50), new RecipeOutput(Block.workbench, 87), new RecipeOutput(Block.chest, 80));
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
		for(int i = 0;i < BCVariables.changeToSlot.length;i++)
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
		
		double oilmulti = (double)oil.totalBurningTime / (double)5000;
		double fuelmulti = (double)fuel.totalBurningTime / (double)25000;
		
		IronEngineFuel.addFuel("oil", oil.powerPerCycle, (int)(20000 * oilmulti));
		IronEngineFuel.addFuel("fuel", fuel.powerPerCycle, (int)(100000 * fuelmulti));
		IronEngineFuel.addFuel(FluidRegistry.LAVA, 2, 10000);
		if(Loader.isModLoaded("Railcraft"))
		{
			FuelManager.addBoilerFuel(fuel.liquid, 96000);
			Fluid fluid = FluidRegistry.getFluid("bioethanol");
			if(fluid != null)
			{
				FuelManager.addBoilerFuel(fluid, 32000);
			}
			fluid = FluidRegistry.getFluid("biofuel");
			if(fluid != null)
			{
				FuelManager.addBoilerFuel(fluid, 32000);
			}
		}
	}
	
	public static Item BuildItem(int defaultID, Class<? extends Pipe> clas, String descr)
	{
		try
		{
			ItemPipe res = new SpmodPipe(defaultID, descr);
			RegisterProxy.RegisterItem(TinyModularThingsLib.ModID, res);
			if(res != null)
			{
				BlockGenericPipe.pipes.put(res.itemID, clas);
				TinyModularThings.core.loadPipe(res, res.itemID, clas);
			}
			
			return res;
		}
		catch(Exception e)
		{
			for(int i = 0;i < e.getStackTrace().length;i++)
			{
				FMLLog.getLogger().info("" + e.getStackTrace()[i]);
			}
			return null;
		}
	}
}