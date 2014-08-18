package speiger.src.spmodapi.common.modHelper.minefactoryReloaded;

import powercrystals.minefactoryreloaded.api.FactoryRegistry;
import powercrystals.minefactoryreloaded.api.IFactoryFertilizable;
import powercrystals.minefactoryreloaded.api.IFactoryHarvestable;
import powercrystals.minefactoryreloaded.api.IFactoryPlantable;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;

public class MineFactoryReloadedAddon
{
	public static void loadMFR()
	{
		// Although not needed, the casts ensure the objects are of the correct type
		FactoryRegistry.sendMessage("registerFertilizable", (IFactoryFertilizable) APIBlocks.hempCrop);
		FactoryRegistry.sendMessage("registerHarvestable", (IFactoryHarvestable) APIBlocks.hempCrop);
		FactoryRegistry.sendMessage("registerPlantable", (IFactoryPlantable) APIBlocks.hempCrop);
	}
}
