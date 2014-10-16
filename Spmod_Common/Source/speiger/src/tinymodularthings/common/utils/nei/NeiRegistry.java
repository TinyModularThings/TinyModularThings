package speiger.src.tinymodularthings.common.utils.nei;

import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.tinymodularthings.client.gui.machine.PressureFurnaceGui;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import codechicken.nei.api.API;

public class NeiRegistry
{
	private static NeiRegistry instance = new NeiRegistry();
	
	public static NeiRegistry getInstance()
	{
		return instance;
	}
	
	public void init()
	{
		try
		{
			API.registerRecipeHandler(new NeiPressureFurnace());
			API.registerUsageHandler(new NeiPressureFurnace());
			API.registerGuiOverlay(PressureFurnaceGui.class, "smelting", -5, 0);
			API.hideItem(TinyBlocks.storageBlock.blockID);
			API.hideItem(APIBlocks.multiPlate.blockID);
		}
		catch (Exception e)
		{
		}
	}
}
