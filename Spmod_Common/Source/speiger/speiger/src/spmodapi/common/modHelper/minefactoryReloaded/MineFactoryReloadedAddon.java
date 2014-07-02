package speiger.src.spmodapi.common.modHelper.minefactoryReloaded;

import powercrystals.minefactoryreloaded.api.FactoryRegistry;
import speiger.src.spmodapi.common.blocks.hemp.BlockHempCrop;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;

public class MineFactoryReloadedAddon
{
	public static void loadMFR()
	{
		FactoryRegistry.registerFertilizable((BlockHempCrop) APIBlocks.hempCrop);
		FactoryRegistry.registerHarvestable((BlockHempCrop) APIBlocks.hempCrop);
		FactoryRegistry.registerPlantable((BlockHempCrop) APIBlocks.hempCrop);
	}
}
