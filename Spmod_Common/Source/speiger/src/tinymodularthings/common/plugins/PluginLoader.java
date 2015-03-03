package speiger.src.tinymodularthings.common.plugins;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.MinecraftForge;

import speiger.src.api.common.registry.helpers.IPlugin;
import speiger.src.api.common.registry.recipes.pressureFurnace.PressureRecipeList;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.recipes.pressureFurnace.FurnaceRecipe;
import speiger.src.tinymodularthings.common.recipes.recipeMaker.EnergyRecipes;


public class PluginLoader
{
	public void loadPlugins()
	{
		List<IPlugin> plugins = getPlugins("BC", "Forestry", "IC2", "Railcraft");
		for(IPlugin plugin : plugins)
		{
			initPlugin(plugin);
		}
		PressureRecipeList list = PressureRecipeList.getInstance();
		FurnaceRecipes furnace = FurnaceRecipes.smelting();
		Map<Integer, ItemStack> map = furnace.getSmeltingList();
		for(Entry<Integer, ItemStack> cu : map.entrySet())
		{
			list.addRecipe(new FurnaceRecipe(cu.getKey(), cu.getValue()));
		}
		for(Entry<List<Integer>, ItemStack> cu : furnace.getMetaSmeltingList().entrySet())
		{
			list.addRecipe(new FurnaceRecipe(cu.getKey(), cu.getValue()));
		}
		EnergyRecipes.onPostInit();
		
	}
	
	
	public void initPlugin(IPlugin plugin)
	{
		initPluginForge(plugin.getForgeClasses());
		plugin.init();
	}
	
	public void initPluginForge(Object par1)
	{
		if(par1 == null)
		{
			return;
		}
		Object[] array = null;
		if(par1 instanceof Object[])
		{
			array = (Object[])par1;
		}
		else
		{
			array = new Object[] {par1 };
		}
		for(Object obj : array)
		{
			MinecraftForge.EVENT_BUS.register(obj);
		}
	}
	
	public List<IPlugin> getPlugins(String... par1)
	{
		ArrayList<IPlugin> plugin = new ArrayList<IPlugin>();
		for(String key : par1)
		{
			try
			{
				TinyModularThings.log.print("Load Current Plugin: " + key);
				Class clz = PluginLoader.class.getClassLoader().loadClass("speiger.src.tinymodularthings.common.plugins." + key + ".Plugin" + key);
				if(clz != null)
				{
					IPlugin plug = (IPlugin)clz.newInstance();
					if(plug.canLoad())
					{
						TinyModularThings.log.print("Loaded Current Plugin: " + key);
						plugin.add(plug);
					}
					else
					{
						TinyModularThings.log.print("Current Plugin is Dissabled: " + key);
					}
				}
			}
			catch(Exception e)
			{
				TinyModularThings.log.print("Could not Load Current Plugin: " + key);
			}
		}
		return plugin;
	}
	
}
