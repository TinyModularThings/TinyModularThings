package speiger.src.spmodapi.common.world;

import java.util.ArrayList;

import speiger.src.api.world.ISpmodWorldGen;
import speiger.src.api.world.SpmodWorldGenRegistry;

public class WorldLoader
{
	private static WorldLoader instance = new WorldLoader();
	
	public static WorldLoader getInstance()
	{
		return instance;
	}
	
	public void registerOres()
	{
		
		registerAPIOres();
	}
	
	private void registerAPIOres()
	{
		ArrayList<ISpmodWorldGen> genList = SpmodWorldGenRegistry.getRetrogenList();
		
		for (ISpmodWorldGen cu : genList)
		{
			SpmodWorldGen.getWorldGen().registerWorldGen(cu);
		}
	}
}
