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


	@Override
	public Object getForgeClasses()
	{
		return null;
	}
	
}
