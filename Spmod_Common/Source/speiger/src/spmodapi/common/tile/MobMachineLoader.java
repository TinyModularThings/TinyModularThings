package speiger.src.spmodapi.common.tile;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import speiger.src.api.items.ItemCollection;
import speiger.src.spmodapi.common.blocks.utils.MobMachine;
import speiger.src.spmodapi.common.blocks.utils.MobMachine.DropType;
import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.enums.EnumColor;
import speiger.src.spmodapi.common.util.proxy.PathProxy;

public class MobMachineLoader
{
	public static Random rand = new Random();
	
	public static void initMobMachines()
	{
		// Not Inited
		MobMachine.createMob(0, false, 0, getMobMachineTextures("Uninitialized"));
		MobMachine.setName(0, "Information");
		
		// Pig
		MobMachine.createMob(1, true, 10, getMobMachineTextures("Pig"));
		MobMachine.setName(1, "Pig");
		MobMachine.addActivators(1, new ItemStack(Item.porkRaw), new ItemStack(Item.porkCooked));
		MobMachine.addFood(1, new ItemStack[] {new ItemStack(Item.carrot), new ItemStack(APIItems.hemp) }, new int[] {500, 50 });
		MobMachine.addDrops(1, DropType.Common, new ItemStack(Item.porkRaw), new ItemStack(Item.porkCooked), new ItemStack(APIItems.bonePig));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(1, DropType.Legendary, new ItemStack(Item.monsterPlacer, 1, 90));
		}
		
		// Sheep
		MobMachine.createMob(2, true, 20, getMobMachineTextures("Sheep"));
		MobMachine.setName(2, "Sheep");
		MobMachine.addActivators(2, ItemCollection.getAllWool());
		MobMachine.addFood(2, new ItemStack[] {new ItemStack(Item.wheat), new ItemStack(APIItems.hemp) }, new int[] {500, 50 });
		MobMachine.addDrops(2, DropType.Common, ItemCollection.getAllWool());
		MobMachine.addDrops(2, DropType.Common, new ItemStack(APIItems.boneSheep));
		MobMachine.addDrops(2, DropType.Rare, ItemCollection.getAllDye());
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(2, DropType.Legendary, new ItemStack(Item.monsterPlacer, 1, 91));
		}
		
		// Chicken
		MobMachine.createMob(3, true, 5, getMobMachineTextures("Chicken"));
		MobMachine.setName(3, "Chicken");
		MobMachine.addActivators(3, new ItemStack(Item.egg), new ItemStack(Item.chickenRaw), new ItemStack(Item.chickenCooked));
		MobMachine.addFood(3, new ItemStack[] {new ItemStack(Item.seeds), new ItemStack(APIItems.hempSeed) }, new int[] {500 });
		MobMachine.addDrops(3, DropType.Common, new ItemStack(Item.egg), new ItemStack(Item.feather), new ItemStack(Item.chickenRaw), new ItemStack(Item.chickenCooked), new ItemStack(APIItems.boneChicken));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(3, DropType.Legendary, new ItemStack(Item.monsterPlacer, 1, 93));
		}
		
		// Cow
		MobMachine.createMob(4, true, 15, getMobMachineTextures("Cow"));
		MobMachine.setName(4, "Cow");
		MobMachine.addActivators(4, new ItemStack(Item.leather), new ItemStack(Item.beefRaw), new ItemStack(Item.beefCooked));
		MobMachine.addFood(4, new ItemStack[] {new ItemStack(Item.wheat), new ItemStack(APIItems.hemp) }, new int[] {500, 50 });
		MobMachine.addDrops(4, DropType.Common, new ItemStack(Item.leather), new ItemStack(Item.beefRaw), new ItemStack(Item.beefCooked), new ItemStack(APIItems.boneCow));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(4, DropType.Legendary, new ItemStack(Item.monsterPlacer, 1, 92));
		}
		
		// Mooshrooms
		MobMachine.createMob(5, true, 25, getMobMachineTextures("Mooshroom"));
		MobMachine.setName(5, "Mooshroom");
		MobMachine.addActivators(5, new ItemStack(Block.mushroomBrown), new ItemStack(Block.mushroomRed), new ItemStack(Item.bowlSoup));
		MobMachine.addFood(5, new ItemStack[] {new ItemStack(Item.wheat), new ItemStack(APIItems.hemp) }, new int[] {500, 50 });
		MobMachine.addDrops(5, DropType.Common, new ItemStack(Item.leather), new ItemStack(Item.beefRaw), new ItemStack(Item.beefCooked), new ItemStack(Block.mushroomRed), new ItemStack(Block.mushroomBrown), new ItemStack(APIItems.boneMooshroom));
		MobMachine.addDrops(5, DropType.Rare, new ItemStack(Item.bowlSoup), new ItemStack(Block.mushroomCapBrown), new ItemStack(Block.mushroomCapRed));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(5, DropType.Legendary, new ItemStack(Item.monsterPlacer, 1, 96));
		}
		
		// Squid
		MobMachine.createMob(6, true, 7, getMobMachineTextures("Squid"));
		MobMachine.setName(6, "Squid");
		MobMachine.addActivators(6, OreDictionary.getOres("dyeBlack").toArray(new ItemStack[OreDictionary.getOres("dyeBlack").size()]));
		MobMachine.addFood(6, new ItemStack(Item.fishRaw), 1500);
		MobMachine.addFood(6, PathProxy.getFluidContainerItems(FluidRegistry.WATER), new int[] {500 });
		MobMachine.addDrops(6, DropType.Common, new ItemStack(Item.dyePowder, 1, EnumColor.BLACK.getAsDye()));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(6, DropType.Legendary, new ItemStack(Item.monsterPlacer, 1, 94));
		}
		
		// Zombie
		MobMachine.createMob(7, false, 5, getMobMachineTextures("Zombey"));
		MobMachine.setName(7, "Zombey");
		MobMachine.addActivator(7, new ItemStack(Item.rottenFlesh));
		MobMachine.addFood(7, new ItemStack[] {new ItemStack(Item.porkRaw), new ItemStack(Item.beefRaw), new ItemStack(Item.chickenRaw), new ItemStack(Item.porkCooked), new ItemStack(Item.beefCooked), new ItemStack(Item.chickenCooked) }, new int[] {900, 900, 900, 2500, 2500, 2500 });
		MobMachine.addDrops(7, DropType.Common, new ItemStack(Item.rottenFlesh), new ItemStack(Item.poisonousPotato));
		MobMachine.addDrops(7, DropType.Rare, new ItemStack(Item.bone), new ItemStack(Item.potato), new ItemStack(Item.carrot), new ItemStack(Item.ingotIron), new ItemStack(Item.skull, 1, 2));
		MobMachine.addDrops(7, DropType.Rare, getItem(5, Item.shovelIron));
		MobMachine.addDrops(7, DropType.Rare, getItem(5, Item.swordIron));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(7, DropType.Legendary, new ItemStack(Item.monsterPlacer, 1, 54));
		}
		
		// Skelete
		MobMachine.createMob(8, false, 4, getMobMachineTextures("Skeletor"));
		MobMachine.setName(8, "Skeletor");
		MobMachine.addActivators(8, new ItemStack(Item.bone), new ItemStack(Item.arrow));
		MobMachine.addFood(8, new ItemStack(Item.feather), 1000);
		MobMachine.addDrops(8, DropType.Common, new ItemStack(Item.arrow), new ItemStack(Item.bone), new ItemStack(Item.skull));
		MobMachine.addDrops(8, DropType.Rare, getItem(20, Item.bow));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(8, DropType.Legendary, new ItemStack(Item.monsterPlacer, 1, 51));
		}
		
		// Spider
		MobMachine.createMob(9, false, 3, getMobMachineTextures("Spider"));
		MobMachine.setName(9, "Spider");
		MobMachine.addActivators(9, new ItemStack(Item.spiderEye), new ItemStack(Item.silk));
		MobMachine.addFood(9, new ItemStack[] {new ItemStack(Item.porkRaw), new ItemStack(Item.beefRaw), new ItemStack(Item.chickenRaw), new ItemStack(Item.fishRaw) }, new int[] {1000, 1000, 1000, 2000 });
		MobMachine.addDrops(9, DropType.Common, new ItemStack(Item.silk), new ItemStack(Item.spiderEye), new ItemStack(Item.fermentedSpiderEye));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(9, DropType.Legendary, new ItemStack(Item.monsterPlacer, 1, 52));
		}
		
		// Cave Spider
		MobMachine.createMob(10, false, 4, getMobMachineTextures("CaveSpider"));
		MobMachine.setName(10, "Cave Spider");
		MobMachine.addActivators(10, new ItemStack(Item.fermentedSpiderEye), new ItemStack(Block.web));
		MobMachine.addFood(10, new ItemStack[] {new ItemStack(Item.porkRaw), new ItemStack(Item.beefRaw), new ItemStack(Item.chickenRaw), new ItemStack(Item.fishRaw), new ItemStack(Block.mushroomBrown), new ItemStack(Block.mushroomRed) }, new int[] {1000, 1000, 1000, 2000, 2500, 2500 });
		MobMachine.addDrops(10, DropType.Common, new ItemStack(Item.silk), new ItemStack(Item.spiderEye), new ItemStack(Item.fermentedSpiderEye), new ItemStack(Block.web));
		MobMachine.addDrops(10, DropType.Rare, new ItemStack(Item.potion, 1, 8196), new ItemStack(Item.potion, 1, 8260), new ItemStack(Item.potion, 1, 8228), new ItemStack(Item.bone, 3));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(10, DropType.Legendary, new ItemStack(Item.monsterPlacer, 1, 59));
		}
		
		// Pig Zombey
		MobMachine.createMob(11, false, 8, getMobMachineTextures("PigZombey"));
		MobMachine.setName(11, "Pig Zombey");
		MobMachine.addActivators(11, new ItemStack(Item.goldNugget), new ItemStack(Block.netherrack));
		MobMachine.addFood(11, new ItemStack[] {new ItemStack(Item.appleRed), new ItemStack(Item.bread), new ItemStack(Item.melon), new ItemStack(Block.melon) }, new int[] {900, 1200, 5000, 200, 1800 });
		MobMachine.addDrops(11, DropType.Common, new ItemStack(Item.goldNugget), new ItemStack(Item.goldNugget, 5), new ItemStack(Item.ingotGold), new ItemStack(Item.swordGold));
		MobMachine.addDrops(11, DropType.Rare, getItem(35, Item.swordGold));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(11, DropType.Legendary, new ItemStack(Item.monsterPlacer, 1, 57));
		}
		
		// Creeper
		MobMachine.createMob(12, false, 12, getMobMachineTextures("Creeper"));
		MobMachine.setName(12, "Creeper");
		MobMachine.addActivators(12, new ItemStack(Item.gunpowder), new ItemStack(Block.tnt));
		MobMachine.addFood(12, new ItemStack[] {new ItemStack(Item.ingotIron), new ItemStack(Item.goldNugget), new ItemStack(Item.ingotGold), new ItemStack(Item.diamond), new ItemStack(Item.emerald), new ItemStack(Block.blockLapis), new ItemStack(Block.blockIron), new ItemStack(Block.blockGold), new ItemStack(Block.blockDiamond), new ItemStack(Block.blockEmerald) }, new int[] {500, 600, 2400, 3000, 4000, 5000, 4500, 10000, 15000, 20000 });
		MobMachine.addDrops(12, DropType.Common, new ItemStack(Item.gunpowder));
		MobMachine.addDrops(12, DropType.Rare, new ItemStack(Item.skull, 1, 4));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(12, DropType.Legendary, new ItemStack(Item.monsterPlacer, 1, 50));
		}
		
		// Ghast
		MobMachine.createMob(13, false, 20, getMobMachineTextures("Ghast"));
		MobMachine.setName(13, "Ghast");
		MobMachine.addActivators(13, new ItemStack(Item.ghastTear), new ItemStack(Block.netherBrick));
		MobMachine.addFood(13, PathProxy.getFluidContainerItems(FluidRegistry.LAVA), new int[] {2000 });
		MobMachine.addDrops(13, DropType.Common, new ItemStack(Item.gunpowder), new ItemStack(Item.ghastTear));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(13, DropType.Legendary, new ItemStack(Item.monsterPlacer, 1, 56));
		}
		
		// Witch
		MobMachine.createMob(14, false, 18, getMobMachineTextures("Witch"));
		MobMachine.setName(14, "Witch");
		MobMachine.addActivator(14, new ItemStack(Item.netherStalkSeeds));
		MobMachine.addFood(14, new ItemStack[] {new ItemStack(Item.sugar), new ItemStack(Block.mushroomBrown), new ItemStack(Block.mushroomRed), new ItemStack(Item.bread) }, new int[] {250, 1500, 1700, 1200 });
		MobMachine.addDrops(14, DropType.Common, getPotions(false, Item.potion));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(14, DropType.Legendary, new ItemStack(Item.monsterPlacer, 1, 66));
		}
		
		// Wither Skelete
		MobMachine.createMob(15, false, 25, getMobMachineTextures("WitherSkeletor"));
		MobMachine.setName(15, "Wither Skelete");
		MobMachine.addActivators(15, new ItemStack(Item.skull), new ItemStack(Item.coal));
		MobMachine.addFood(15, new ItemStack[] {new ItemStack(Item.feather), new ItemStack(Item.bone), new ItemStack(Item.coal, 1, 1) }, new int[] {1750, 1500, 1000 });
		MobMachine.addDrops(15, DropType.Common, new ItemStack(Item.bone), new ItemStack(Item.arrow), new ItemStack(Item.swordStone));
		MobMachine.addDrops(15, DropType.Rare, new ItemStack(Item.coal), new ItemStack(Item.skull, 1, 1));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(15, DropType.Legendary, new ItemStack(Item.monsterPlacer, 1, 51));
		}
		
		// Endermann
		MobMachine.createMob(16, false, 32, getMobMachineTextures("Endermann"));
		MobMachine.setName(16, "Enderman");
		MobMachine.addActivator(16, new ItemStack(Item.enderPearl));
		MobMachine.addFood(16, new ItemStack[] {new ItemStack(Item.porkRaw), new ItemStack(Item.porkCooked) }, new int[] {2000, 4000 });
		MobMachine.addDrops(16, DropType.Common, new ItemStack(Item.enderPearl));
		MobMachine.addDrops(16, DropType.Rare, new ItemStack(Item.enderPearl, 2), new ItemStack(Item.eyeOfEnder));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(16, DropType.Legendary, new ItemStack(Item.monsterPlacer, 1, 58));
		}
		
		// Ender Dragon
		MobMachine.createMob(17, false, 55, getMobMachineTextures("EnderDragon"));
		MobMachine.setName(17, "Ender Dragon");
		MobMachine.addActivator(17, new ItemStack(Item.eyeOfEnder));
		MobMachine.addFood(17, new ItemStack(Item.enderPearl), 2000);
		MobMachine.addDrops(17, DropType.Legendary, new ItemStack(Block.dragonEgg));
		
		// Wither
		MobMachine.createMob(18, false, 40, getMobMachineTextures("Wither"));
		MobMachine.setName(18, "Wither");
		MobMachine.addActivator(18, new ItemStack(Item.skull, 1, 1));
		MobMachine.addFood(18, new ItemStack[] {new ItemStack(Item.diamond), new ItemStack(Block.dragonEgg) }, new int[] {2500, 10000 });
		MobMachine.addDrops(18, DropType.Legendary, new ItemStack(Item.netherStar));
		
		// Blaze
		MobMachine.createMob(19, false, 15, getMobMachineTextures("Blaze"));
		MobMachine.setName(19, "Blaze");
		MobMachine.addActivators(19, new ItemStack(Item.blazeRod), new ItemStack(Item.blazePowder));
		MobMachine.addFood(19, new ItemStack(Item.stick), 1000);
		MobMachine.addDrops(19, DropType.Common, new ItemStack(Item.blazeRod));
		MobMachine.addDrops(19, DropType.Rare, new ItemStack(Item.blazeRod, 2), new ItemStack(Item.blazePowder, 4));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(19, DropType.Legendary, new ItemStack(Item.monsterPlacer, 1, 61));
		}
		
		// Slime
		MobMachine.createMob(20, false, 10, getMobMachineTextures("Slime"));
		MobMachine.setName(20, "Slime");
		MobMachine.addActivators(20, new ItemStack(Item.slimeBall), new ItemStack(Item.dyePowder, 1, EnumColor.GREEN.getAsDye()));
		MobMachine.addFood(20, PathProxy.getFluidContainerItems(FluidRegistry.WATER), new int[] {1000 });
		MobMachine.addFood(20, new ItemStack[] {new ItemStack(Item.dyePowder, 1, EnumColor.GREEN.getAsDye()), new ItemStack(Block.mushroomBrown), new ItemStack(Block.mushroomRed) }, new int[] {500, 750, 750 });
		MobMachine.addDrops(20, DropType.Common, new ItemStack(Item.slimeBall));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(20, DropType.Legendary, new ItemStack(Item.monsterPlacer, 1, 55));
		}
		
		// Magma Slime
		MobMachine.createMob(21, false, 20, getMobMachineTextures("MagmaCube"));
		MobMachine.setName(21, "Magma Cube");
		MobMachine.addActivator(21, new ItemStack(Item.magmaCream));
		MobMachine.addFood(21, PathProxy.getFluidContainerItems(FluidRegistry.LAVA), new int[] {2000 });
		MobMachine.addFood(21, new ItemStack(Item.blazeRod), 8000);
		MobMachine.addDrops(21, DropType.Common, new ItemStack(Item.magmaCream));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(21, DropType.Legendary, new ItemStack(Item.monsterPlacer, 1, 62));
		}
		
		// Villager
		MobMachine.createMob(22, true, 5, getMobMachineTextures("Villager"));
		MobMachine.setName(22, "Villager");
		MobMachine.addActivator(22, new ItemStack(Item.emerald));
		MobMachine.addFood(22, new ItemStack[] {new ItemStack(Item.bread), new ItemStack(Item.carrot), new ItemStack(Item.bakedPotato) }, new int[] {2000, 1500, 4000 });
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(22, DropType.Legendary, new ItemStack(Item.monsterPlacer, 1, 120));
		}
		
		
	}
	
	private static String[] getMobMachineTextures(String name)
	{
		String[] tex = new String[2];
		tex[0] = "MobMachine_" + name + "_Side";
		tex[1] = "MobMachine_" + name + "_Front";
		return tex;
	}
	
	public static ItemStack[] getItem(int types, Item item)
	{
		ItemStack[] array = new ItemStack[types];
		ItemStack stack = null;
		for(int i = 0;i < types;i++)
		{
			stack = new ItemStack(item);
			EnchantmentHelper.addRandomEnchantment(rand, stack, rand.nextInt(types));
			array[i] = stack.copy();
		}
		
		return array;
	}
	
	public static ItemStack[] getPotions(boolean good, ItemPotion potion)
	{
		int[] meta = new int[] {8193, 8194, 8195, 8196, 8197, 8198, 
				8200, 8201, 8202, 8204, 8206, 8225, 8226, 8228, 8229, 
				8233, 8236, 8257, 8258, 8259, 8260, 8262, 8264, 8265, 
				8266, 8270, 16384, 16385, 16386, 16387, 16388, 16389, 
				16390, 16392, 16393, 16394, 16396, 16398, 16417, 
				16418, 16420, 16421, 163425, 16428, 16449, 16450, 
				16451, 16452, 16454, 16456, 16457, 16458, 16462};
		ItemStack[] potions = new ItemStack[meta.length];
		
		for(int i = 0;i<meta.length;i++)
		{
			potions[i] = new ItemStack(potion, 1, meta[i]);
		}
		return potions;
	}
	
}
