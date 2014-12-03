package speiger.src.spmodapi.common.config.ModObjects;

import java.util.HashMap;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import speiger.src.spmodapi.common.items.core.SpmodItem;

public class APIItems
{
	// Hemp
	public static Item hempSeed;
	public static SpmodItem hemp;
	public static SpmodItem compressedHemp;
	public static SpmodItem hempResin;
	
	// Armor
	public static Item hempHelmet;
	public static Item hempChestPlate;
	public static Item hempLeggings;
	public static Item hempBoots;
	
	// Crafting
	public static SpmodItem gears;
	public static SpmodItem blueDye;
	public static SpmodItem colorCard;
	public static SpmodItem multiPlate;
	public static SpmodItem trades;
	public static SpmodItem tinyRedstoneDust;
	
	public static ItemStack[] hempPlates;
	public static Item hempResinBucket;
	
	// ExpBottle
	public static SpmodItem expBottles;
	
	// Mob Machine
	public static SpmodItem mobMachineHelper;
	
	// Bone
	public static Item boneChicken;
	public static Item bonePig;
	public static Item boneCow;
	public static Item boneMooshroom;
	public static Item boneSheep;
	public static Item boneHorse;
	public static SpmodItem accessDebug;
	
	//Gas
	public static SpmodItem gasBucket;
	public static SpmodItem gasCell;
	public static SpmodItem gasCellC;
	
	//Circuits
	public static SpmodItem redstoneCable;
	public static SpmodItem circuits;
	public static HashMap<String, SpmodItem> damageableCircuits = new HashMap<String, SpmodItem>();

}
