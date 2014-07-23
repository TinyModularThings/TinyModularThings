package speiger.src.api.world;

import java.util.Random;

import net.minecraft.world.World;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.config.IConfigHelper;

public interface ISpmodWorldGen
{
	
	/**
	 * This Function get Called on Minecraft Start so you can Generate some
	 * configs (in my Folder if you wish) It Gets Called at Server Startet so
	 * implement your Gen Code before you make it
	 * 
	 * @param config
	 *            Config Generation.
	 */
	void onMinecraftStart(IConfigHelper config);
	
	/**
	 * Function Checks if you really wont to generate.
	 * 
	 * @param retroGen
	 *            Means does it also happen on Retrogen?
	 * @return true to generate, false to stop generating!
	 */
	boolean doGenerate(boolean retroGen);
	
	/**
	 * Generate Function. same as normal!
	 * 
	 * @param world
	 * @param chunkX
	 * @param chunkZ
	 * @param rand
	 * @param retrogen
	 */
	void generate(World world, int chunkX, int chunkZ, Random rand, boolean retrogen);
	
	/**
	 * Return a name for your gen. Will only Called to make Retrogen Possible
	 * 
	 * @return a Name
	 */
	String getName();
	
	/**
	 * This function validate your Worldgen. If your Mod is not defined as
	 * SpmodMod than it will not work
	 * 
	 * @return The Mod
	 */
	SpmodMod getMod();
}
