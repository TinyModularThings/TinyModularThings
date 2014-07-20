package speiger.src.spmodapi.common.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import speiger.src.api.items.ItemCollection;
import speiger.src.spmodapi.common.blocks.utils.MobMachine;
import speiger.src.spmodapi.common.blocks.utils.MobMachine.DropType;
import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.enums.EnumColor;
import speiger.src.spmodapi.common.lib.SpmodAPILib;
import speiger.src.spmodapi.common.util.proxy.PathProxy;

public class MobMachineLoader 
{
	public static Random rand = new Random();
	public static void initMobMachines()
	{
		//Not Inited
		MobMachine.createMob(0, false, 0, getMobMachineTextures("Uninitialized"));
		
		//Pig
		MobMachine.createMob(1, true, 10, getMobMachineTextures("Pig"));
		MobMachine.setName(1, "Pig");
		MobMachine.addActivators(1, new ItemStack(Items.porkchop), new ItemStack(Items.cooked_porkchop));
		MobMachine.addFood(1, new ItemStack[]{new ItemStack(Items.carrot), new ItemStack(APIItems.hemp)}, new int[]{500, 50});
		MobMachine.addDrops(1, DropType.Common, new ItemStack(Items.porkchop), new ItemStack(Items.cooked_porkchop));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(1, DropType.Legendary, new ItemStack(Items.spawn_egg, 1, 90));
		}
		
		//Sheep
		MobMachine.createMob(2, true, 20, getMobMachineTextures("Sheep"));
		MobMachine.setName(2, "Sheep");
		MobMachine.addActivators(2, ItemCollection.getAllWool());
		MobMachine.addFood(2, new ItemStack[]{new ItemStack(Items.wheat), new ItemStack(APIItems.hemp)}, new int[]{500, 50});
		MobMachine.addDrops(2, DropType.Common, ItemCollection.getAllWool());
		MobMachine.addDrops(2, DropType.Rare, ItemCollection.getAllDye());
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(2, DropType.Legendary, new ItemStack(Items.spawn_egg, 1, 91));
		}
		
		//Chicken
		MobMachine.createMob(3, true, 5, getMobMachineTextures("Chicken"));
		MobMachine.setName(3, "Chicken");
		MobMachine.addActivators(3, new ItemStack(Items.egg), new ItemStack(Items.chicken), new ItemStack(Items.cooked_chicken));
		MobMachine.addFood(3, new ItemStack[]{new ItemStack(Items.wheat_seeds), new ItemStack(APIItems.hempSeed)}, new int[]{500});
		MobMachine.addDrops(3, DropType.Common, new ItemStack(Items.egg), new ItemStack(Items.feather), new ItemStack(Items.chicken), new ItemStack(Items.cooked_chicken));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(3, DropType.Legendary, new ItemStack(Items.spawn_egg, 1, 93));
		}
		
		//Cow
		MobMachine.createMob(4, true, 15, getMobMachineTextures("Cow"));
		MobMachine.setName(4, "Cow");
		MobMachine.addActivators(4, new ItemStack(Items.leather), new ItemStack(Items.beef), new ItemStack(Items.cooked_beef));
		MobMachine.addFood(4, new ItemStack[]{new ItemStack(Items.wheat), new ItemStack(APIItems.hemp)}, new int[]{500, 50});
		MobMachine.addDrops(4, DropType.Common, new ItemStack(Items.leather), new ItemStack(Items.beef), new ItemStack(Items.cooked_beef));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(4, DropType.Legendary, new ItemStack(Items.spawn_egg, 1, 92));
		}
		
		//Mooshrooms
		MobMachine.createMob(5, true, 25, getMobMachineTextures("Mooshroom"));
		MobMachine.addActivators(5, new ItemStack(Blocks.brown_mushroom), new ItemStack(Blocks.red_mushroom), new ItemStack(Items.bowl));
		MobMachine.addFood(5, new ItemStack[]{new ItemStack(Items.wheat), new ItemStack(APIItems.hemp)}, new int[]{500, 50});
		MobMachine.addDrops(5, DropType.Common, new ItemStack(Items.leather), new ItemStack(Items.beef), new ItemStack(Items.cooked_beef), new ItemStack(Blocks.red_mushroom), new ItemStack(Blocks.brown_mushroom));
		MobMachine.addDrops(5, DropType.Rare, new ItemStack(Items.bowl), new ItemStack(Blocks.brown_mushroom_block), new ItemStack(Blocks.red_mushroom_block));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(5, DropType.Legendary, new ItemStack(Items.spawn_egg, 1, 96));
		}
		
		//Squid
		MobMachine.createMob(6, true, 7, getMobMachineTextures("Squid"));
		MobMachine.setName(6, "Squid");
		MobMachine.addActivators(6, OreDictionary.getOres("dyeBlack").toArray(new ItemStack[OreDictionary.getOres("dyeBlack").size()]));
		MobMachine.addFood(6, new ItemStack(Items.fish), 1500);
		MobMachine.addFood(6, PathProxy.getFluidContainerItems(FluidRegistry.WATER), new int[]{500});
		MobMachine.addDrops(6, DropType.Common, new ItemStack(Items.dye, 1, EnumColor.BLACK.getAsDye()));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(6, DropType.Legendary, new ItemStack(Items.spawn_egg, 1, 94));
		}
		
		//Zombie
		MobMachine.createMob(7, false, 5, getMobMachineTextures("Zombey"));
		MobMachine.setName(7, "Zombey");
		MobMachine.addActivator(7, new ItemStack(Items.rotten_flesh));
		MobMachine.addFood(7, new ItemStack[]{new ItemStack(Items.porkchop), new ItemStack(Items.beef), new ItemStack(Items.chicken), new ItemStack(Items.cooked_porkchop), new ItemStack(Items.cooked_beef), new ItemStack(Items.cooked_chicken)}, new int[]{900, 900, 900, 2500, 2500, 2500});
		MobMachine.addDrops(7, DropType.Common, new ItemStack(Items.rotten_flesh), new ItemStack(Items.poisonous_potato));
		MobMachine.addDrops(7, DropType.Rare, new ItemStack(Items.bone), new ItemStack(Items.potato), new ItemStack(Items.carrot), new ItemStack(Items.iron_ingot), new ItemStack(Items.skull, 1, 2));
		MobMachine.addDrops(7, DropType.Rare, getItem(5, Items.iron_shovel));
		MobMachine.addDrops(7, DropType.Rare, getItem(5, Items.iron_sword));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(7, DropType.Legendary, new ItemStack(Items.spawn_egg, 1, 54));
		}
	
		//Skelete
		MobMachine.createMob(8, false, 4, getMobMachineTextures("Skeletor"));
		MobMachine.setName(8, "Skeletor");
		MobMachine.addActivators(8, new ItemStack(Items.bone), new ItemStack(Items.arrow));
		MobMachine.addFood(8, new ItemStack(Items.feather), 1000);
		MobMachine.addDrops(8, DropType.Common, new ItemStack(Items.arrow), new ItemStack(Items.bone), new ItemStack(Items.skull));
		MobMachine.addDrops(8, DropType.Rare, getItem(20, Items.bow));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(8, DropType.Legendary, new ItemStack(Items.spawn_egg, 1, 51));
		}
		
		//Spider
		MobMachine.createMob(9, false, 3, getMobMachineTextures("Spider"));
		MobMachine.setName(9, "Spider");
		MobMachine.addActivators(9, new ItemStack(Items.spider_eye), new ItemStack(Items.string));
		MobMachine.addFood(9, new ItemStack[]{new ItemStack(Items.porkchop), new ItemStack(Items.beef), new ItemStack(Items.chicken), new ItemStack(Items.fish)}, new int[]{1000, 1000, 1000, 2000});
		MobMachine.addDrops(9, DropType.Common, new ItemStack(Items.string), new ItemStack(Items.spider_eye), new ItemStack(Items.fermented_spider_eye));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(9, DropType.Legendary, new ItemStack(Items.spawn_egg, 1, 52));
		}
		
		//Cave Spider
		MobMachine.createMob(10, false, 4, getMobMachineTextures("CaveSpider"));
		MobMachine.setName(10, "Cave Spider");
		MobMachine.addActivators(10, new ItemStack(Items.fermented_spider_eye), new ItemStack(Blocks.web));
		MobMachine.addFood(10, new ItemStack[]{new ItemStack(Items.porkchop), new ItemStack(Items.beef), new ItemStack(Items.chicken), new ItemStack(Items.fish), new ItemStack(Blocks.brown_mushroom), new ItemStack(Blocks.red_mushroom)}, new int[]{1000, 1000, 1000, 2000, 2500, 2500});
		MobMachine.addDrops(10, DropType.Common, new ItemStack(Items.string), new ItemStack(Items.spider_eye), new ItemStack(Items.fermented_spider_eye), new ItemStack(Blocks.web));
		MobMachine.addDrops(10, DropType.Rare, new ItemStack(Items.potionitem, 1, 8196), new ItemStack(Items.potionitem, 1, 8260), new ItemStack(Items.potionitem, 1, 8228));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(10, DropType.Legendary, new ItemStack(Items.spawn_egg, 1, 59));
		}
		
		//Pig Zombey
		MobMachine.createMob(11, false, 8, getMobMachineTextures("PigZombey"));
		MobMachine.setName(11, "Pig Zombey");
		MobMachine.addActivators(11, new ItemStack(Items.gold_nugget), new ItemStack(Blocks.netherrack));
		MobMachine.addFood(11, new ItemStack[]{new ItemStack(Items.apple), new ItemStack(Items.bread), new ItemStack(Items.bowl), new ItemStack(Items.melon), new ItemStack(Blocks.melon_block)}, new int[]{900, 1200, 5000, 200, 1800});
		MobMachine.addDrops(11, DropType.Common, new ItemStack(Items.gold_nugget), new ItemStack(Items.gold_nugget, 5), new ItemStack(Items.gold_ingot), new ItemStack(Items.golden_sword));
		MobMachine.addDrops(11, DropType.Rare, getItem(35, Items.golden_sword));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(11, DropType.Legendary, new ItemStack(Items.spawn_egg, 1, 57));
		}
		
		//Creeper
		MobMachine.createMob(12, false, 12, getMobMachineTextures("Creeper"));
		MobMachine.setName(12, "Creeper");
		MobMachine.addActivators(12, new ItemStack(Items.gunpowder), new ItemStack(Blocks.tnt));
		MobMachine.addFood(12, new ItemStack[]{new ItemStack(Items.iron_ingot), new ItemStack(Items.gold_nugget), new ItemStack(Items.gold_ingot), new ItemStack(Items.diamond), new ItemStack(Items.emerald), new ItemStack(Blocks.lapis_block), new ItemStack(Blocks.iron_block), new ItemStack(Blocks.gold_block), new ItemStack(Blocks.diamond_block), new ItemStack(Blocks.emerald_block)}, new int[]{500, 600, 2400, 3000, 4000, 5000, 4500, 10000, 15000, 20000});
		MobMachine.addDrops(12, DropType.Common, new ItemStack(Items.gunpowder));
		MobMachine.addDrops(12, DropType.Rare, new ItemStack(Items.skull, 1, 4));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(12, DropType.Legendary, new ItemStack(Items.spawn_egg, 1, 50));
		}
		
		//Ghast
		MobMachine.createMob(13, false, 20, getMobMachineTextures("Ghast"));
		MobMachine.setName(13, "Ghast");
		MobMachine.addActivators(13, new ItemStack(Items.ghast_tear), new ItemStack(Blocks.nether_brick));
		MobMachine.addFood(13, PathProxy.getFluidContainerItems(FluidRegistry.LAVA), new int[]{2000});
		MobMachine.addDrops(13, DropType.Common, new ItemStack(Items.gunpowder), new ItemStack(Items.ghast_tear));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(13, DropType.Legendary, new ItemStack(Items.spawn_egg, 1, 56));
		}
		
		//Witch
		MobMachine.createMob(14, false, 18, getMobMachineTextures("Witch"));
		MobMachine.setName(14, "Witch");
		MobMachine.addActivator(14, new ItemStack(Items.nether_wart));
		MobMachine.addFood(14, new ItemStack[]{new ItemStack(Items.sugar), new ItemStack(Blocks.brown_mushroom), new ItemStack(Blocks.red_mushroom), new ItemStack(Items.bread)}, new int[]{250, 1500, 1700, 1200}); 
		MobMachine.addDrops(14, DropType.Common, getPotions(false, Items.potionitem));
		MobMachine.addDrops(14, DropType.Rare, getPotions(true, Items.potionitem));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(14, DropType.Legendary, new ItemStack(Items.spawn_egg, 1, 66));
		}
		
		//Wither Skelete
		MobMachine.createMob(15, false, 25, getMobMachineTextures("WitherSkeletor"));
		MobMachine.setName(15, "Wither Skelete");
		MobMachine.addActivators(15, new ItemStack(Items.skull), new ItemStack(Items.coal));
		MobMachine.addFood(15, new ItemStack[]{new ItemStack(Items.feather), new ItemStack(Items.bone), new ItemStack(Items.coal, 1, 1)}, new int[]{1750, 1500, 1000});
		MobMachine.addDrops(15, DropType.Common, new ItemStack(Items.bone), new ItemStack(Items.arrow), new ItemStack(Items.stone_sword));
		MobMachine.addDrops(16, DropType.Rare, new ItemStack(Items.coal), new ItemStack(Items.skull, 1, 1));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(15, DropType.Legendary, new ItemStack(Items.spawn_egg, 1, 51));
		}
		
		//Endermann
		MobMachine.createMob(16, false, 32, getMobMachineTextures("Endermann"));
		MobMachine.setName(16, "Enderman");
		MobMachine.addActivator(16, new ItemStack(Items.ender_pearl));
		MobMachine.addFood(16, new ItemStack[]{new ItemStack(Items.porkchop), new ItemStack(Items.cooked_porkchop)}, new int[]{2000, 4000});
		MobMachine.addDrops(16, DropType.Common, new ItemStack(Items.ender_pearl));
		MobMachine.addDrops(16, DropType.Rare, new ItemStack(Items.ender_pearl, 2), new ItemStack(Items.ender_eye));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(15, DropType.Legendary, new ItemStack(Items.spawn_egg, 1, 58));
		}
		
		//Ender Dragon
		MobMachine.createMob(17, false, 55, getMobMachineTextures("EnderDragon"));
		MobMachine.setName(17, "Ender Dragon");
		MobMachine.addActivator(17, new ItemStack(Items.ender_eye));
		MobMachine.addFood(17, new ItemStack(Items.ender_pearl), 2000);
		MobMachine.addDrops(17, DropType.Legendary, new ItemStack(Blocks.dragon_egg));
		
		//Wither
		MobMachine.createMob(18, false, 40, getMobMachineTextures("Wither"));
		MobMachine.setName(18, "Wither");
		MobMachine.addActivator(18, new ItemStack(Items.skull, 1, 1));
		MobMachine.addFood(18, new ItemStack[]{new ItemStack(Items.diamond), new ItemStack(Blocks.dragon_egg)}, new int[]{2500, 10000});
		MobMachine.addDrops(18, DropType.Legendary, new ItemStack(Items.nether_star));
		
		//Blaze
		MobMachine.createMob(19, false, 15, getMobMachineTextures("Blaze"));
		MobMachine.setName(19, "Blaze");
		MobMachine.addActivators(19, new ItemStack(Items.blaze_rod), new ItemStack(Items.blaze_powder));
		MobMachine.addFood(19, new ItemStack(Items.stick), 1000);
		MobMachine.addDrops(19, DropType.Common, new ItemStack(Items.blaze_rod));
		MobMachine.addDrops(19, DropType.Rare, new ItemStack(Items.blaze_rod, 2), new ItemStack(Items.blaze_powder, 4));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(19, DropType.Legendary, new ItemStack(Items.spawn_egg, 1, 61));
		}
	
		MobMachine.createMob(20, false, 10, getMobMachineTextures("Slime"));
		MobMachine.setName(20, "Slime");
		MobMachine.addActivators(20, new ItemStack(Items.slime_ball), new ItemStack(Items.dye, 1, EnumColor.GREEN.getAsDye()));
		MobMachine.addFood(20, PathProxy.getFluidContainerItems(FluidRegistry.WATER), new int[]{1000});
		MobMachine.addFood(20, new ItemStack[]{new ItemStack(Items.dye, 1, EnumColor.GREEN.getAsDye()), new ItemStack(Blocks.brown_mushroom), new ItemStack(Blocks.red_mushroom)}, new int[]{500, 750, 750});
		MobMachine.addDrops(20, DropType.Common, new ItemStack(Items.slime_ball));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(20, DropType.Legendary, new ItemStack(Items.spawn_egg, 1, 55));
		}
		MobMachine.createMob(21, false, 20, getMobMachineTextures("MagmaCube"));
		MobMachine.setName(21, "Magma Cube");
		MobMachine.addActivator(21, new ItemStack(Items.magma_cream));
		MobMachine.addFood(21, PathProxy.getFluidContainerItems(FluidRegistry.LAVA), new int[]{2000});
		MobMachine.addFood(21, new ItemStack(Items.blaze_rod), 8000);
		MobMachine.addDrops(21, DropType.Common, new ItemStack(Items.magma_cream));
		if(SpmodConfig.MobMachineEggs)
		{
			MobMachine.addDrops(21, DropType.Legendary, new ItemStack(Items.spawn_egg, 1, 62));
		}
	
	}
	
	
	private static String[] getMobMachineTextures(String name)
	{
		String[] tex = new String[2];
		tex[0] = SpmodAPILib.ModID.toLowerCase()+":mobmachine/MobMachine_"+name+"_Side";
		tex[1] = SpmodAPILib.ModID.toLowerCase()+":mobmachine/MobMachine_"+name+"_Front";
		return tex;
	}
	
	public static ItemStack[] getItem(int types, Item item)
	{
		ItemStack[] array = new ItemStack[types];
		ItemStack stack = null;
		for(int i = 0;i<types;i++)
		{
			stack = new ItemStack(item);
			EnchantmentHelper.addRandomEnchantment(rand, stack, rand.nextInt(types));
			array[i] = stack.copy();
		}
		return array;
	}
	public static ItemStack[] getPotions(boolean good, ItemPotion potion)
	{
		ArrayList<ItemStack> item = new ArrayList<ItemStack>();
		potion.getSubItems(potion, CreativeTabs.tabBrewing, item);
		ArrayList<ItemStack> end = new ArrayList<ItemStack>();
		for(ItemStack cu : item)
		{
			if(potion.hasEffect(cu))
			{
				List list = potion.getEffects(cu);
				boolean flag = true;
				for(int i = 0;i<list.size();i++)
				{
					PotionEffect effect = (PotionEffect) list.get(i);
					if(Potion.potionTypes[effect.getPotionID()].isBadEffect())
					{
						flag = false;
					}
				}
				if(good == flag)
				{
					end.add(cu);
				}
			}
		}
		return end.toArray(new ItemStack[end.size()]);
	}
}
