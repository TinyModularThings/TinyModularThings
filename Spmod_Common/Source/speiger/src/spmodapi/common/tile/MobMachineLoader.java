package speiger.src.spmodapi.common.tile;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import speiger.src.api.items.ItemCollection;
import speiger.src.spmodapi.common.blocks.utils.MobMachine;
import speiger.src.spmodapi.common.blocks.utils.MobMachine.DropType;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.lib.SpmodAPILib;

public class MobMachineLoader 
{
	public static void initMobMachines()
	{
		//Not Inited
		MobMachine.createMob(0, false, 0, getMobMachineTextures("Uninitialized"));
		
		//Pig
		MobMachine.createMob(1, true, 10, getMobMachineTextures("Pig"));
		MobMachine.setName(1, "Pig");
		MobMachine.addActivators(1, new ItemStack(Item.porkRaw), new ItemStack(Item.porkCooked));
		MobMachine.addFood(1, new ItemStack[]{new ItemStack(Item.carrot), new ItemStack(APIItems.hemp)}, new int[]{500, 50});
		MobMachine.addDrops(1, DropType.Common, new ItemStack(Item.porkRaw), new ItemStack(Item.porkCooked));
		MobMachine.addDrops(1, DropType.Legendary, new ItemStack(Item.monsterPlacer, 1, 90));
		
		//Sheep
		MobMachine.createMob(2, true, 20, getMobMachineTextures("Sheep"));
		MobMachine.setName(2, "Sheep");
		MobMachine.addActivators(2, ItemCollection.getAllWool());
		MobMachine.addFood(2, new ItemStack[]{new ItemStack(Item.wheat), new ItemStack(APIItems.hemp)}, new int[]{500, 50});
		MobMachine.addDrops(2, DropType.Common, ItemCollection.getAllWool());
		MobMachine.addDrops(2, DropType.Rare, ItemCollection.getAllDye());
		MobMachine.addDrops(2, DropType.Legendary, new ItemStack(Item.monsterPlacer, 1, 91));
		
		//Chicken
		MobMachine.createMob(3, true, 5, getMobMachineTextures("Chicken"));
		MobMachine.setName(3, "Chicken");
		MobMachine.addActivators(3, new ItemStack(Item.egg), new ItemStack(Item.chickenRaw), new ItemStack(Item.chickenCooked));
		MobMachine.addFood(3, new ItemStack[]{new ItemStack(Item.seeds), new ItemStack(APIItems.hempSeed)}, new int[]{500});
		MobMachine.addDrops(3, DropType.Common, new ItemStack(Item.egg), new ItemStack(Item.feather), new ItemStack(Item.chickenRaw), new ItemStack(Item.chickenCooked));
		MobMachine.addDrops(3, DropType.Legendary, new ItemStack(Item.monsterPlacer, 1, 93));
		
		//Cow
		MobMachine.createMob(4, true, 15, getMobMachineTextures("Cow"));
		MobMachine.setName(4, "Cow");
		MobMachine.addActivators(4, new ItemStack(Item.leather), new ItemStack(Item.beefRaw), new ItemStack(Item.beefCooked));
		MobMachine.addFood(4, new ItemStack[]{new ItemStack(Item.wheat), new ItemStack(APIItems.hemp)}, new int[]{500, 50});
		MobMachine.addDrops(4, DropType.Common, new ItemStack(Item.leather), new ItemStack(Item.beefRaw), new ItemStack(Item.beefCooked));
		MobMachine.addDrops(4, DropType.Legendary, new ItemStack(Item.monsterPlacer, 1, 92));
		
		//Mooshrooms
		MobMachine.createMob(5, true, 25, getMobMachineTextures("Mooshroom"));
		MobMachine.addActivators(5, new ItemStack(Block.mushroomBrown), new ItemStack(Block.mushroomRed), new ItemStack(Item.bowlSoup));
		MobMachine.addFood(5, new ItemStack[]{new ItemStack(Item.wheat), new ItemStack(APIItems.hemp)}, new int[]{500, 50});
		MobMachine.addDrops(5, DropType.Common, new ItemStack(Item.leather), new ItemStack(Item.beefRaw), new ItemStack(Item.beefCooked), new ItemStack(Block.mushroomRed), new ItemStack(Block.mushroomBrown));
		MobMachine.addDrops(5, DropType.Rare, new ItemStack(Item.bowlSoup), new ItemStack(Block.mushroomCapBrown), new ItemStack(Block.mushroomCapRed));
		MobMachine.addDrops(5, DropType.Legendary, new ItemStack(Item.monsterPlacer, 1, 96));
		
		//Squid
		MobMachine.createMob(6, true, 7, getMobMachineTextures("Squid"));
		MobMachine.setName(6, "Squid");
		MobMachine.addActivators(6, OreDictionary.getOres("dyeBlack").toArray(new ItemStack[OreDictionary.getOres("dyeBlack").size()]));
		
		//Zombie
		MobMachine.createMob(7, false, 5, getMobMachineTextures("Zombey"));
	
		//Skelete
		MobMachine.createMob(8, false, 4, getMobMachineTextures("Skeletor"));
		
		//Spider
		MobMachine.createMob(9, false, 3, getMobMachineTextures("Spider"));
		
		//Cave Spider
		MobMachine.createMob(10, false, 4, getMobMachineTextures("CaveSpider"));
		
		//Pig Zombey
		MobMachine.createMob(11, false, 8, getMobMachineTextures("PigZombey"));
		
		//Creeper
		MobMachine.createMob(12, false, 12, getMobMachineTextures("Creeper"));
		
		//Ghast
		MobMachine.createMob(13, false, 20, getMobMachineTextures("Ghast"));
		
		//Witch
		MobMachine.createMob(14, false, 18, getMobMachineTextures("Witch"));
		
		//Wither Skelete
		MobMachine.createMob(15, false, 25, getMobMachineTextures("WitherSkeletor"));
		
		//Endermann
		MobMachine.createMob(16, false, 32, getMobMachineTextures("Endermann"));
		
		//Ender Dragon
		MobMachine.createMob(17, false, 55, getMobMachineTextures("EnderDragon"));
		
		//Wither
		MobMachine.createMob(18, false, 40, getMobMachineTextures("Wither"));
		
		//Blaze
		MobMachine.createMob(19, false, 15, getMobMachineTextures("Blaze"));
	
	
	}
	
	
	private static String[] getMobMachineTextures(String name)
	{
		String[] tex = new String[2];
		tex[0] = SpmodAPILib.ModID.toLowerCase()+":mobmachine/MobMachine_"+name+"_Side";
		tex[1] = SpmodAPILib.ModID.toLowerCase()+":mobmachine/MobMachine_"+name+"_Front";
		return tex;
	}
	
}
