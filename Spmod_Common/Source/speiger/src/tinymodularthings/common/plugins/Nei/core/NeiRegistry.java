package speiger.src.tinymodularthings.common.plugins.Nei.core;

import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.tinymodularthings.common.blocks.machine.PressureFurnace;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.plugins.Nei.gui.PressureFurnaceGui;
import codechicken.nei.api.API;

public class NeiRegistry
{
	
	public static void init()
	{
		API.registerRecipeHandler(new NeiPressureFurnace());
		API.registerUsageHandler(new NeiPressureFurnace());
		PressureFurnace.guiClass = PressureFurnaceGui.class;
		API.registerGuiOverlay(PressureFurnaceGui.class, "smelting", -5, 0);
		API.hideItem(TinyBlocks.storageBlock.blockID);
		API.hideItem(APIBlocks.multiPlate.blockID);
	}
}
