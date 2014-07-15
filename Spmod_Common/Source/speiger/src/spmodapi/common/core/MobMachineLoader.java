package speiger.src.spmodapi.common.core;

import speiger.src.spmodapi.common.blocks.utils.MobMachine;
import speiger.src.spmodapi.common.lib.SpmodAPILib;

public class MobMachineLoader 
{
	public static void initMobMachines()
	{
		//Not Inited
		MobMachine.createMob(0, false, 0, getMobMachineTextures("Uninitialized"));
		
		//Pig
		MobMachine.createMob(1, true, 10, getMobMachineTextures("Pig"));
		
		//Sheep
		MobMachine.createMob(2, true, 20, getMobMachineTextures("Sheep"));
		
		//Chicken
		MobMachine.createMob(3, true, 5, getMobMachineTextures("Chicken"));
		
		//Cow
		MobMachine.createMob(4, true, 15, getMobMachineTextures("Cow"));
		
		//Mooshrooms
		MobMachine.createMob(5, true, 25, getMobMachineTextures("Mooshroom"));
		
		//Squid
		MobMachine.createMob(6, true, 7, getMobMachineTextures("Squid"));
		
		
	}
	
	
	private static String[] getMobMachineTextures(String name)
	{
		String[] tex = new String[2];
		tex[0] = SpmodAPILib.ModID.toLowerCase()+":mobmachine/MobMachine_"+name+"_Side";
		tex[1] = SpmodAPILib.ModID.toLowerCase()+":mobmachine/MobMachine_"+name+"_Front";
		return tex;
	}
}
