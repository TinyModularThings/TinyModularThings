package speiger.src.spmodapi.common.plugins.MFR.core;

import powercrystals.minefactoryreloaded.api.FactoryRegistry;
import speiger.src.spmodapi.common.blocks.hemp.BlockHempCrop;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;

public class MFRAddon
{
	public static void init()
	{
		FactoryRegistry.registerFertilizable((BlockHempCrop) APIBlocks.hempCrop);
		FactoryRegistry.registerHarvestable((BlockHempCrop) APIBlocks.hempCrop);
		FactoryRegistry.registerPlantable((BlockHempCrop) APIBlocks.hempCrop);
	}
}
