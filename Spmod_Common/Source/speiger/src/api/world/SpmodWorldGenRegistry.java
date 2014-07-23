package speiger.src.api.world;

import java.util.ArrayList;

public class SpmodWorldGenRegistry
{
	private static ArrayList<ISpmodWorldGen> genList = new ArrayList<ISpmodWorldGen>();
	
	/**
	 * Register World Gen. This Also Provide Retrogen
	 * 
	 * @param par0
	 */
	public static void registerWorldGenerator(ISpmodWorldGen par0)
	{
		genList.add(par0);
	}
	
	public static ArrayList<ISpmodWorldGen> getRetrogenList()
	{
		return genList;
	}
	
}
