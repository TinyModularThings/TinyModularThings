package speiger.src.tinymodularthings.common.utils.nei;

import speiger.src.tinymodularthings.client.gui.machine.PressureFurnaceGui;
import cpw.mods.fml.common.API;

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
