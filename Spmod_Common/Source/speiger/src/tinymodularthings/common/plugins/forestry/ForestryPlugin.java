package speiger.src.tinymodularthings.common.plugins.forestry;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Loader;
import forestry.api.circuits.ChipsetManager;
import forestry.api.core.ItemInterface;
import forestry.api.farming.Farmables;
import forestry.api.farming.IFarmable;
import forestry.core.circuits.Circuit;

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
		Collection<IFarmable> list = Farmables.farmables.get("IC2Crops");
		if(list == null)
		{
			list = new ArrayList<IFarmable>();
		}
		list.add(new IC2Crops());
		Farmables.farmables.put("IC2Crops", list);
		Circuit ic2Crops = new SpmodCircuitFarmLogic("ic2crops", FarmLogicIC2Crops.class, new String[]{"IC2 Crops"}).setManual(); 
		ChipsetManager.solderManager.addRecipe(ChipsetManager.circuitRegistry.getLayout("forestry.farms.manual"), new ItemStack(ItemInterface.getItem("tubes").itemID, 1, 9), ic2Crops);
	}
}
