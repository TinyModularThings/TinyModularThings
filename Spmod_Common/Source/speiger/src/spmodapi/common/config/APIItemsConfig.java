package speiger.src.spmodapi.common.config;

import java.util.Arrays;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import speiger.src.api.common.world.items.plates.PlateManager;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.fluids.hemp.ItemHempResin;
import speiger.src.spmodapi.common.items.armor.HempArmor;
import speiger.src.spmodapi.common.items.core.BasicItem;
import speiger.src.spmodapi.common.items.core.SpmodItem;
import speiger.src.spmodapi.common.items.crafting.ItemBlueDye;
import speiger.src.spmodapi.common.items.crafting.ItemCircuit;
import speiger.src.spmodapi.common.items.crafting.ItemColorCard;
import speiger.src.spmodapi.common.items.crafting.ItemDamageableCircuit;
import speiger.src.spmodapi.common.items.crafting.ItemGear;
import speiger.src.spmodapi.common.items.crafting.ItemMobMachineHelper;
import speiger.src.spmodapi.common.items.crafting.RedstoneCable;
import speiger.src.spmodapi.common.items.crafting.SpmodBone;
import speiger.src.spmodapi.common.items.debug.ItemAccessAdder;
import speiger.src.spmodapi.common.items.exp.ExpBottle;
import speiger.src.spmodapi.common.items.gas.ItemGasBucket;
import speiger.src.spmodapi.common.items.gas.ItemGasCell;
import speiger.src.spmodapi.common.items.hemp.ItemCompressedHemp;
import speiger.src.spmodapi.common.items.hemp.ItemHemp;
import speiger.src.spmodapi.common.items.hemp.ItemHempBucket;
import speiger.src.spmodapi.common.items.hemp.ItemHempSeed;
import speiger.src.spmodapi.common.items.hemp.ItemMultiPlate;
import speiger.src.spmodapi.common.items.trades.ItemRandomTrade;
import speiger.src.spmodapi.common.lib.SpmodAPILib;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.proxy.RegisterProxy;

public class APIItemsConfig
{
	private static SpmodConfig config;
	private static APIItems items;
	
	public static void loadItems()
	{
		TextureEngine engine = TextureEngine.getTextures();
		
		Item.enderPearl.setMaxStackSize(64);
		Item.egg.setMaxStackSize(64);
		
		items.hempSeed = new ItemHempSeed(config.itemIDs.getCurrentID());
		RegisterProxy.RegisterItem(items.hempSeed);
		engine.setCurrentPath("hemp");
		engine.registerTexture(items.hempSeed, "HempSeeds");
		config.itemIDs.updateToNextID();
		
		items.hemp = new ItemHemp(config.itemIDs.getCurrentID());
		RegisterProxy.RegisterItem(items.hemp);
		engine.registerTexture(items.hemp, "hempDrop");
		config.itemIDs.updateToNextID();
		
		items.hempBoots = new HempArmor(config.itemIDs.getCurrentID(), 3);
		RegisterProxy.RegisterItem(items.hempBoots);
		engine.registerTexture(items.hempBoots, "hemp_boots");
		config.itemIDs.updateToNextID();
		
		items.hempLeggings = new HempArmor(config.itemIDs.getCurrentID(), 2);
		RegisterProxy.RegisterItem(items.hempLeggings);
		engine.registerTexture(items.hempLeggings, "hemp_leggings");
		config.itemIDs.updateToNextID();
		
		items.hempChestPlate = new HempArmor(config.itemIDs.getCurrentID(), 1);
		RegisterProxy.RegisterItem(items.hempChestPlate);
		engine.registerTexture(items.hempChestPlate, "hemp_chestplate");
		config.itemIDs.updateToNextID();
		
		items.hempHelmet = new HempArmor(config.itemIDs.getCurrentID(), 0);
		RegisterProxy.RegisterItem(items.hempHelmet);
		engine.registerTexture(items.hempHelmet, "hemp_helmet");
		config.itemIDs.updateToNextID();
		
		items.compressedHemp = new ItemCompressedHemp(config.itemIDs.getCurrentID());
		RegisterProxy.RegisterItem(items.compressedHemp);
		engine.registerTexture(items.compressedHemp, "compressedHemp");
		config.itemIDs.updateToNextID();
		
		items.hempResin = new ItemHempResin(config.itemIDs.getCurrentID());
		RegisterProxy.RegisterItem(items.hempResin);
		config.itemIDs.updateToNextID();
		
		items.gears = new ItemGear(config.itemIDs.getCurrentID());
		RegisterProxy.RegisterItem(items.gears);
		((SpmodItem)items.gears).registerTexture(engine);
		config.itemIDs.updateToNextID();
		
		items.blueDye = new ItemBlueDye(config.itemIDs.getCurrentID());
		RegisterProxy.RegisterItem(items.blueDye);
		engine.registerTexture(items.blueDye, "dyeBlue");
		config.itemIDs.updateToNextID();
		
		items.multiPlate = new ItemMultiPlate(config.itemIDs.getCurrentID());
		RegisterProxy.RegisterItem(items.multiPlate);
		((SpmodItem)items.multiPlate).registerTexture(engine);
		config.itemIDs.updateToNextID();
		
		items.colorCard = new ItemColorCard(config.itemIDs.getCurrentID());
		RegisterProxy.RegisterItem(items.colorCard);
		engine.registerTexture(items.colorCard, "colorCard");
		config.itemIDs.updateToNextID();
		
		items.hempResinBucket = new ItemHempBucket(config.itemIDs.getCurrentID());
		RegisterProxy.RegisterItem(items.hempResinBucket);
		engine.setCurrentPath("hemp");
		engine.registerTexture(items.hempResinBucket, "hemp.resin.bucket");
		config.itemIDs.updateToNextID();
		
		items.expBottles = new ExpBottle(config.itemIDs.getCurrentID());
		RegisterProxy.RegisterItem(items.expBottles);
		((SpmodItem)items.expBottles).registerTexture(engine);
		config.itemIDs.updateToNextID();
		
		items.mobMachineHelper = new ItemMobMachineHelper(config.itemIDs.getCurrentID());
		RegisterProxy.RegisterItem(items.mobMachineHelper);
		config.itemIDs.updateToNextID();
		
		engine.setCurrentPath("crafting");
		items.boneChicken = new SpmodBone(config.itemIDs.getCurrentID(), 1, "Chicken", engine);
		RegisterProxy.RegisterItem(items.boneChicken);
		config.itemIDs.updateToNextID();
		
		items.boneSheep = new SpmodBone(config.itemIDs.getCurrentID(), 2, "Sheep", engine);
		RegisterProxy.RegisterItem(items.boneSheep);
		config.itemIDs.updateToNextID();
		
		items.bonePig = new SpmodBone(config.itemIDs.getCurrentID(), 4, "Pig", engine);
		RegisterProxy.RegisterItem(items.bonePig);
		config.itemIDs.updateToNextID();
		
		items.boneCow = new SpmodBone(config.itemIDs.getCurrentID(), 5, "Cow", engine);
		RegisterProxy.RegisterItem(items.boneCow);
		config.itemIDs.updateToNextID();
		
		items.boneMooshroom = new SpmodBone(config.itemIDs.getCurrentID(), 5, "Mushroom", engine);
		RegisterProxy.RegisterItem(items.boneMooshroom);
		config.itemIDs.updateToNextID();
		
		items.boneHorse = new SpmodBone(config.itemIDs.getCurrentID(), 6, "Horse", engine);
		RegisterProxy.RegisterItem(items.boneHorse);
		config.itemIDs.updateToNextID();
		
		items.trades = new ItemRandomTrade(config.itemIDs.getCurrentID());
		RegisterProxy.RegisterItem(items.trades);
		engine.registerTexture(items.trades, "random_Trade");
		config.itemIDs.updateToNextID();
		
		items.redstoneCable = new RedstoneCable(config.itemIDs.getCurrentID());
		RegisterProxy.RegisterItem(items.redstoneCable);
		engine.registerTexture(items.redstoneCable, "redstonecable");
		config.itemIDs.updateToNextID();
		
		items.accessDebug = new ItemAccessAdder(config.itemIDs.getCurrentID());
		RegisterProxy.RegisterItem(items.accessDebug);
		engine.setCurrentPath("debug");
		engine.registerTexture(items.accessDebug, "accesserTool");
		config.itemIDs.updateToNextID();
		
		engine.setCurrentPath("gas");
		items.gasBucket = new ItemGasBucket(config.itemIDs.getCurrentID());
		RegisterProxy.RegisterItem(items.gasBucket);
		engine.registerTexture(items.gasBucket, "GasBucket");
		config.itemIDs.updateToNextID();
		
		items.gasCell = new ItemGasCell(config.itemIDs.getCurrentID(), false);
		RegisterProxy.RegisterItem(items.gasCell);
		engine.registerTexture(items.gasCell, "GasCell");
		config.itemIDs.updateToNextID();
		
		items.gasCellC = new ItemGasCell(config.itemIDs.getCurrentID(), true);
		RegisterProxy.RegisterItem(items.gasCellC);
		engine.registerTexture(items.gasCellC, "CompressedGasCell");
		config.itemIDs.updateToNextID();
		
		items.circuits = new ItemCircuit(config.itemIDs.getCurrentID());
		RegisterProxy.RegisterItem(items.circuits);
		items.circuits.registerTexture(engine);
		config.itemIDs.updateToNextID();
		
		engine.setCurrentPath("crafting");
		items.tinyRedstoneDust = new BasicItem(config.itemIDs.getCurrentID(), "Tiny Redstone Dust");
		items.tinyRedstoneDust.setCreativeTab(APIUtils.tabCrafing);
		RegisterProxy.RegisterItem(items.tinyRedstoneDust);
		engine.registerTexture(items.tinyRedstoneDust, "TinyRedstonePowder");
		config.itemIDs.updateToNextID();
		
		SpmodItem data;
		items.damageableCircuits.put("StorageLogicDiamond", data = new ItemDamageableCircuit(config.itemIDs.getCurrentID(), "Logic Diamond", 100, Arrays.asList("Storage Logic")));
		RegisterProxy.RegisterItem(data);
		engine.registerTexture(data, "StorageLogicDiamond");
		config.itemIDs.updateToNextID();
		
		initHempPlates();
	}
	
	private static void initHempPlates()
	{
		ResourceLocation[] models = new ResourceLocation[] { new ResourceLocation(SpmodAPILib.ModID.toLowerCase() + ":textures/models/plates/HanfSign.png"), new ResourceLocation(SpmodAPILib.ModID.toLowerCase() + ":textures/models/plates/BlackHanfSign.png"), new ResourceLocation(SpmodAPILib.ModID.toLowerCase() + ":textures/models/plates/RedHanfSign.png"), new ResourceLocation(SpmodAPILib.ModID.toLowerCase() + ":textures/models/plates/GreenHanfSign.png"), new ResourceLocation(SpmodAPILib.ModID.toLowerCase() + ":textures/models/plates/BrownHanfSign.png"), new ResourceLocation(SpmodAPILib.ModID.toLowerCase() + ":textures/models/plates/BlueHanfSign.png"), new ResourceLocation(SpmodAPILib.ModID.toLowerCase() + ":textures/models/plates/PurpleHanfSign.png"), new ResourceLocation(SpmodAPILib.ModID.toLowerCase() + ":textures/models/plates/CyanHanfSign.png"), new ResourceLocation(SpmodAPILib.ModID.toLowerCase() + ":textures/models/plates/SilverHanfSign.png"), new ResourceLocation(SpmodAPILib.ModID.toLowerCase() + ":textures/models/plates/GrayHanfSign.png"), new ResourceLocation(SpmodAPILib.ModID.toLowerCase() + ":textures/models/plates/PinkHanfSign.png"), new ResourceLocation(SpmodAPILib.ModID.toLowerCase() + ":textures/models/plates/LimeHanfSign.png"), new ResourceLocation(SpmodAPILib.ModID.toLowerCase() + ":textures/models/plates/YellowHanfSign.png"), new ResourceLocation(SpmodAPILib.ModID.toLowerCase() + ":textures/models/plates/LightBlueHanfSign.png"), new ResourceLocation(SpmodAPILib.ModID.toLowerCase() + ":textures/models/plates/MagentaHanfSign.png"), new ResourceLocation(SpmodAPILib.ModID.toLowerCase() + ":textures/models/plates/OrangeHanfSign.png"), new ResourceLocation(SpmodAPILib.ModID.toLowerCase() + ":textures/models/plates/WhiteHanfSign.png") };
		String[] identifiers = new String[] { "hemp.Sign", "hemp.Sign.black", "hemp.Sign.red", "hemp.Sign.green", "hemp.Sign.brown", "hemp.Sign.blue", "hemp.Sign.purple", "hemp.Sign.cyan", "hemp.Sign.lightGray", "hemp.Sign.gray", "hemp.Sign.pink", "hemp.Sign.lime", "hemp.Sign.yellow", "hemp.Sign.lightBlue", "hemp.Sign.magenta", "hemp.Sign.orange", "hemp.Sign.white"};
		String[] textures = new String[] { SpmodAPILib.ModID.toLowerCase() + ":plates/HanfSign", SpmodAPILib.ModID.toLowerCase() + ":plates/HanfSignBlack", SpmodAPILib.ModID.toLowerCase() + ":plates/HanfSignRed", SpmodAPILib.ModID.toLowerCase() + ":plates/HanfSignGreen", SpmodAPILib.ModID.toLowerCase() + ":plates/HanfSignBrown", SpmodAPILib.ModID.toLowerCase() + ":plates/HanfSignBlue", SpmodAPILib.ModID.toLowerCase() + ":plates/HanfSignPurple", SpmodAPILib.ModID.toLowerCase() + ":plates/HanfSignCyan", SpmodAPILib.ModID.toLowerCase() + ":plates/HanfSignSilver", SpmodAPILib.ModID.toLowerCase() + ":plates/HanfSignGray", SpmodAPILib.ModID.toLowerCase() + ":plates/HanfSignPink", SpmodAPILib.ModID.toLowerCase() + ":plates/HanfSignLime", SpmodAPILib.ModID.toLowerCase() + ":plates/HanfSignYellow", SpmodAPILib.ModID.toLowerCase() + ":plates/HanfSignLightBlue", SpmodAPILib.ModID.toLowerCase() + ":plates/HanfSignMagenta", SpmodAPILib.ModID.toLowerCase() + ":plates/HanfSignOrange", SpmodAPILib.ModID.toLowerCase() + ":plates/HanfSignWhite" };
		String[] names = new String[]{"Hemp Sign",  "Black Hemp Sign", "Red Hemp Sign", "Green Hemp Sign", "Brown Hemp Sign", "Blue Hemp Sign", "Purple Hemp Sign", "Cyan Hemp Sign", "Light Gray Hemp Sign", "Gray Hemp Sign", "Pink Hemp Sign", "Lime Hemp Sign", "Yellow Hemp Sign", "Light Blue Hemp Sign", "Magenta Hemp Sign", "Orange Hemp Sign", "White Hemp Sign"};
		items.hempPlates = new ItemStack[models.length];
		for (int i = 0; i < items.hempPlates.length; i++)
		{
			items.hempPlates[i] = PlateManager.plates.registerPlate(models[i], textures[i], identifiers[i], names[i], 0.2F).getItemStack();
		}
	}
	
}
