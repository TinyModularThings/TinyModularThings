package speiger.src.spmodapi.common.core;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.oredict.OreDictionary;
import speiger.src.api.common.data.nbt.DataStorage;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.entity.EntityOverridenEnderman;
import speiger.src.spmodapi.common.entity.SpmodFoodStats;
import speiger.src.spmodapi.common.handler.ChatHandler;
import speiger.src.spmodapi.common.handler.GasHandler;
import speiger.src.spmodapi.common.handler.InventoryHandler;
import speiger.src.spmodapi.common.handler.PlayerHandler;
import speiger.src.spmodapi.common.items.crafting.ItemGear;
import speiger.src.spmodapi.common.items.crafting.ItemGear.GearType;
import speiger.src.spmodapi.common.recipes.SpmodRecipeRegistry;
import speiger.src.spmodapi.common.tile.MobMachineLoader;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class Registry
{
	private static Registry instance = new Registry();
	
	private Registry()
	{
		
	};
	
	public static void registerStuff()
	{
		MinecraftForge.EVENT_BUS.register(ChatHandler.getInstance());
		instance.registerText();
		if(SpmodConfig.booleanInfos.get("APIOnly"))
		{
			return;
		}
		
		MinecraftForge.addGrassSeed(new ItemStack(APIItems.hempSeed, 1), 5);
		MinecraftForge.addGrassPlant(APIBlocks.blueFlower, 0, 10);
		instance.initOres();
		PlayerHandler tracker = PlayerHandler.getInstance();
		GameRegistry.registerPlayerTracker(tracker);
		DataStorage.registerNBTReciver(tracker);
		DataStorage.registerNBTReciver(SpmodFoodStats.food);
		SpmodRecipeRegistry.loadRecipes();
		EntityRegistry.registerModEntity(EntityOverridenEnderman.class, "newEndermann", 1, SpmodAPI.instance, 256, 3, true);
		FluidContainerRegistry.registerFluidContainer(APIUtils.hempResin, new ItemStack(APIItems.hempResinBucket), new ItemStack(Item.bucketEmpty));
		MobMachineLoader.initMobMachines();
		GameRegistry.registerFuelHandler(InventoryHandler.instance);
		GameRegistry.registerCraftingHandler(InventoryHandler.instance);
		GasHandler.init();
	}
	
	void registerText()
	{
		LanguageRegistry.instance().addStringLocalization("attribute.name.generic.jump", "Jump Boost");
	}
	
	public void initOres()
	{
		OreDictionary.registerOre("gearWood", ItemGear.getGearFromType(GearType.Wood));
		OreDictionary.registerOre("gearCobble", ItemGear.getGearFromType(GearType.Cobblestone));
		OreDictionary.registerOre("gearStone", ItemGear.getGearFromType(GearType.Stone));
		OreDictionary.registerOre("gearIron", ItemGear.getGearFromType(GearType.Iron));
		OreDictionary.registerOre("gearGold", ItemGear.getGearFromType(GearType.Gold));
		OreDictionary.registerOre("gearDiamond", ItemGear.getGearFromType(GearType.Diamond));
		OreDictionary.registerOre("gearRedstone", ItemGear.getGearFromType(GearType.Redstone));
		OreDictionary.registerOre("gearBone", ItemGear.getGearFromType(GearType.Bone));
	}
	

	
}
