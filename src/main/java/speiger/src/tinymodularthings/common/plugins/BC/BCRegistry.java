package speiger.src.tinymodularthings.common.plugins.BC;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import speiger.src.spmodapi.common.items.crafting.ItemGear;
import speiger.src.spmodapi.common.items.crafting.ItemGear.GearType;
import speiger.src.spmodapi.common.util.proxy.PathProxy;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.plugins.BC.actions.ActionOneSlotChange;
import speiger.src.tinymodularthings.common.plugins.BC.actions.BCActionHelper;
import speiger.src.tinymodularthings.common.plugins.BC.actions.BucketFillerAction;
import speiger.src.tinymodularthings.common.plugins.BC.actions.GateChangeToSlot;
import speiger.src.tinymodularthings.common.plugins.BC.triggers.BCTriggerHelper;
import buildcraft.BuildCraftEnergy;
import buildcraft.BuildCraftFactory;
import buildcraft.BuildCraftTransport;
import buildcraft.api.gates.StatementManager;

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
		instance.loadRecipes();
	}
	
	
	public void loadRecipes()
	{
		PathProxy.addRecipe(new ItemStack(TinyBlocks.machine, 1, 1), new Object[]{" X ", "YCY", " V ", 'X', BuildCraftTransport.pipeFluidsWood, 'V', BuildCraftTransport.pipeFluidsGold, 'Y', ItemGear.getGearFromType(GearType.Cobblestone), 'C', BuildCraftFactory.tankBlock});
		PathProxy.addRecipe(new ItemStack(TinyBlocks.machine, 1, 3), new Object[]{"XXX", "CVC", "XXX", 'X', Item.bucketWater, 'C', ItemGear.getGearFromType(GearType.Diamond), 'V', BuildCraftFactory.pumpBlock});
		PathProxy.addRecipe(new ItemStack(TinyBlocks.machine, 1, 2), new Object[]{"GEG", "EFE", "GEG", 'G', ItemGear.getGearFromType(GearType.Redstone), 'E', new ItemStack(BuildCraftEnergy.engineBlock, 1, 0), 'F', new ItemStack(TinyBlocks.machine, 1, 1)});
	}
	public void loadTrigger()
	{
		StatementManager.registerTrigger(BCVariables.needFuel);
		StatementManager.registerTrigger(BCVariables.hasWork);
		StatementManager.registerTrigger(BCVariables.noWork);
		StatementManager.registerTriggerProvider(new BCTriggerHelper());
	}
	
	public void loadActions()
	{
		StatementManager.registerActionProvider(new BCActionHelper());
		for (int i = 0; i < BCVariables.changeToSlot.length; i++)
		{
			GateChangeToSlot action = new GateChangeToSlot(i);
			BCVariables.changeToSlot[i] = action;
			StatementManager.registerAction(action);
		}
		StatementManager.registerAction(BCVariables.changeOneTime[0] = new ActionOneSlotChange(false));
		StatementManager.registerAction(BCVariables.changeOneTime[1] = new ActionOneSlotChange(true));
		StatementManager.registerAction(BCVariables.bcFiller[0] = new BucketFillerAction(false));
		StatementManager.registerAction(BCVariables.bcFiller[1] = new BucketFillerAction(true));
	}
	
	public void overrideFurnace()
	{
		Block.blocksList[61] = null;
		Block.blocksList[62] = null;
		Block.blocksList[61] = new BlockModifiedFurnace(false);
		Block.blocksList[62] = new BlockModifiedFurnace(true);
		TileEntity.addMapping(TileEntityModifiedFurnace.class, "ModifiedFurnace");
	}
}
