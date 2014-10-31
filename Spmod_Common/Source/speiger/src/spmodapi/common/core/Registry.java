package speiger.src.spmodapi.common.core;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;
import speiger.src.api.items.InfoStack;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.nbt.DataStorage;
import speiger.src.api.packets.SpmodPacketHelper;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.blocks.utils.InventoryAccesser;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.entity.EntityOverridenEnderman;
import speiger.src.spmodapi.common.entity.SpmodFoodStats;
import speiger.src.spmodapi.common.handler.ChatHandler;
import speiger.src.spmodapi.common.handler.PlayerHandler;
import speiger.src.spmodapi.common.items.crafting.ItemGear;
import speiger.src.spmodapi.common.items.crafting.ItemGear.GearType;
import speiger.src.spmodapi.common.recipes.SpmodRecipeRegistry;
import speiger.src.spmodapi.common.tile.MobMachineLoader;
import speiger.src.spmodapi.common.util.ForgeRegister;
import speiger.src.spmodapi.common.util.TileIconMaker;
import buildcraft.BuildCraftCore;
import cpw.mods.fml.common.FMLCommonHandler;
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
		MinecraftForge.addGrassSeed(new ItemStack(APIItems.hempSeed, 1), 5);
		MinecraftForge.addGrassPlant(APIBlocks.blueFlower, 0, 10);
		instance.registerText();
		instance.initOres();
		PlayerHandler tracker = new PlayerHandler();
		GameRegistry.registerPlayerTracker(tracker);
		DataStorage.registerNBTReciver(tracker);
		DataStorage.registerNBTReciver(SpmodFoodStats.food);
		DataStorage.registerNBTReciver(TileIconMaker.getIconMaker().getTileEntityFromClass(InventoryAccesser.class));
		SpmodPacketHelper.getHelper().registerPacketReciver(TileIconMaker.getIconMaker().getTileEntityFromClass(InventoryAccesser.class));
		SpmodRecipeRegistry.loadRecipes();
		EntityRegistry.registerModEntity(EntityOverridenEnderman.class, "newEndermann", 1, SpmodAPI.instance, 256, 3, true);
		MinecraftForge.EVENT_BUS.register(ChatHandler.getInstance());
		FluidContainerRegistry.registerFluidContainer(APIUtils.hempResin, new ItemStack(APIItems.hempResinBucket), new ItemStack(Item.bucketEmpty));
		MobMachineLoader.initMobMachines();
	}
	
	void registerText()
	{
		LanguageRegistry.instance().addStringLocalization("attribute.name.generic.jump", FMLCommonHandler.instance().getCurrentLanguage(), LanguageRegister.getLanguageName(new InfoStack(), "jump.boost", SpmodAPI.instance));
	}
	
	public static void register()
	{
		ForgeRegister.regist(instance);
	}
	
	public void initOres()
	{
		try
		{
			OreDictionary.registerOre("gearCobble", BuildCraftCore.stoneGearItem);
		}
		catch (Exception e)
		{
		}
		OreDictionary.registerOre("gearWood", ItemGear.getGearFromType(GearType.Wood));
		OreDictionary.registerOre("gearCobble", ItemGear.getGearFromType(GearType.Cobblestone));
		OreDictionary.registerOre("gearStone", ItemGear.getGearFromType(GearType.Stone));
		OreDictionary.registerOre("gearIron", ItemGear.getGearFromType(GearType.Iron));
		OreDictionary.registerOre("gearGold", ItemGear.getGearFromType(GearType.Gold));
		OreDictionary.registerOre("gearDiamond", ItemGear.getGearFromType(GearType.Diamond));
		OreDictionary.registerOre("gearRedstone", ItemGear.getGearFromType(GearType.Redstone));
		OreDictionary.registerOre("gearBone", ItemGear.getGearFromType(GearType.Bone));
	}
	
	@ForgeSubscribe
	public void onOreRegister(OreRegisterEvent par0)
	{
		if (par0.Name.equalsIgnoreCase("gearStone"))
		{
			
			try
			{
				if (par0.Ore.itemID == BuildCraftCore.stoneGearItem.itemID)
				{
					OreDictionary.getOres(par0.Name).remove(par0.Ore);
				}
			}
			catch (Exception e)
			{
			}
		}
	}
	
}
