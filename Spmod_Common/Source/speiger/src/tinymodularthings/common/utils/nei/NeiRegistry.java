package speiger.src.tinymodularthings.common.utils.nei;

import speiger.src.tinymodularthings.client.gui.machine.PressureFurnaceGui;
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
		}
		catch (Exception e)
		{
		}
	}
}
