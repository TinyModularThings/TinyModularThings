package speiger.src.spmodapi.common.plugins.BC;

import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;
import speiger.src.api.common.registry.helpers.IPlugin;
import speiger.src.spmodapi.common.plugins.BC.core.BCAddon;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;


public class PluginBC implements IPlugin
{
	
	@Override
	public boolean canLoad()
	{
		return Loader.isModLoaded("BuildCraft|Core");
	}
	
	@Override
	public void init()
	{
		BCAddon.init();
	}

	
	
	@ForgeSubscribe
	public void onOreRegister(OreRegisterEvent par0)
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

	@Override
	public Object getForgeClasses()
	{
		return new BCAddon();
	}
	
}
