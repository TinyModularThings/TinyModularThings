package speiger.src.spmodapi.common.plugins.nei;

import codechicken.nei.api.API;

public class PluginNei
{
	public static void init()
	{
		try
		{
			API.registerRecipeHandler(new MobMachineRecipes());
			API.registerUsageHandler(new MobMachineRecipes());
		}
		catch(Exception e)
		{
		}
	}
}
