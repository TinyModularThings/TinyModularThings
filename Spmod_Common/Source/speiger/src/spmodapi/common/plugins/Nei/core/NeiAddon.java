package speiger.src.spmodapi.common.plugins.Nei.core;

import speiger.src.spmodapi.common.blocks.utils.MobMachine;
import speiger.src.spmodapi.common.plugins.Nei.gui.GuiMobMachine;
import codechicken.nei.api.API;

public class NeiAddon
{
	public static void init()
	{
		try
		{
			API.registerRecipeHandler(new MobMachineRecipes());
			API.registerUsageHandler(new MobMachineRecipes());
			MobMachine.guiClass = GuiMobMachine.class;
		}
		catch(Exception e)
		{
		}
	}
}
