package speiger.src.tinymodularthings.common.plugins.forestry;

import java.util.Collection;

import cpw.mods.fml.common.Loader;
import forestry.api.farming.Farmables;
import forestry.api.farming.IFarmable;

public class ForestryPlugin
{
	public static void initForestryStuff()
	{
		if (Loader.isModLoaded("IC2"))
		{
			loadCrops();
		}
	}
	
	private static void loadCrops()
	{
		Collection<IFarmable> list = Farmables.farmables.get("farmWheat");
		list.add(new IC2Crops());
		Farmables.farmables.put("farmWheat", list);
	}
}
