package speiger.src.tinymodularthings.common.recipes.recipeMaker;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.util.proxy.PathProxy;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;

public class EnergyRecipes
{
	static HashMap<Integer, ArrayList<ItemStack>> cables = new HashMap<Integer, ArrayList<ItemStack>>();
	static HashMap<Integer, ArrayList<ItemStack>> batteries = new HashMap<Integer, ArrayList<ItemStack>>();
	static HashMap<Integer, ArrayList<ItemStack>> upgrades = new HashMap<Integer, ArrayList<ItemStack>>();
	
	public static void init(PathProxy pp)
	{
		pp.addRecipe(new ShapedOreRecipe(new ItemStack(TinyItems.smallMJBattery), new Object[]{" X ", "CVC", "CVC", 'C', "ingotTin", 'V', Item.redstone, 'X', APIItems.redstoneCable}));
		registerCable(0, new ItemStack(APIItems.redstoneCable));
		registerCable(1, new ItemStack(APIItems.redstoneCable));
		registerUpgrades(0, new ItemStack(APIItems.circuits, 1, 1));
		registerUpgrades(1, new ItemStack(APIItems.circuits, 1, 2));
		registerBatteries(0, new ItemStack(TinyItems.smallMJBattery));
		registerBatteries(1, new ItemStack(TinyItems.mediumMJBattery));
		pp.addRecipe(new ItemStack(TinyItems.hugeMJBattery, 1, 0), new Object[]{" X ", "XYX", " X ", 'X', TinyItems.bigMJBattery, 'Y', APIItems.damageableCircuits.get("StorageLogicDiamond")});
	}
	
	public static void onPostInit()
	{
		for(int i = 0;i<2;i++)
		{
			for(ItemStack x : cables.get(i))
			{
				for(ItemStack y : batteries.get(i))
				{
					for(ItemStack z : upgrades.get(i))
					{
						if(i == 0)
						{
							PathProxy.addRecipe(new ItemStack(TinyItems.mediumMJBattery), new Object[]{"XYX", "XCX", "XVX", 'X', new ItemStack(Item.dyePowder, 1, 4), 'Y', x, 'C', y, 'V', z});
						}
						else
						{
							PathProxy.addRecipe(new ItemStack(TinyItems.bigMJBattery), new Object[]{"XYX", "XCX", "XVX", 'X', new ItemStack(Block.blockLapis), 'Y', x, 'C', y, 'V', z});
						}
					}
				}
			}
		}
	}
	
	public static void registerCable(int tier, ItemStack item)
	{
		if(!cables.containsKey(tier))
		{
			cables.put(tier, new ArrayList<ItemStack>());
		}
		cables.get(tier).add(item);
	}
	
	public static void registerBatteries(int tier, ItemStack item)
	{
		if(!batteries.containsKey(tier))
		{
			batteries.put(tier, new ArrayList<ItemStack>());
		}
		batteries.get(tier).add(item);
	}	
	
	public static void registerUpgrades(int tier, ItemStack item)
	{
		if(!upgrades.containsKey(tier))
		{
			upgrades.put(tier, new ArrayList<ItemStack>());
		}
		upgrades.get(tier).add(item);
	}
	
	
}
