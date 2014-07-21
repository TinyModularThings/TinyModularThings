package speiger.src.spmodapi.common.config;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import speiger.src.api.items.DisplayStack;
import speiger.src.api.items.plates.PlateManager;
import speiger.src.api.language.LanguageRegister;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.fluids.hemp.ItemHempResin;
import speiger.src.spmodapi.common.items.armor.HempArmor;
import speiger.src.spmodapi.common.items.crafting.ItemBlueDye;
import speiger.src.spmodapi.common.items.crafting.ItemColorCard;
import speiger.src.spmodapi.common.items.crafting.ItemGear;
import speiger.src.spmodapi.common.items.crafting.ItemMobMachineHelper;
import speiger.src.spmodapi.common.items.exp.ExpBottle;
import speiger.src.spmodapi.common.items.hemp.ItemCompressedHemp;
import speiger.src.spmodapi.common.items.hemp.ItemHemp;
import speiger.src.spmodapi.common.items.hemp.ItemHempBucket;
import speiger.src.spmodapi.common.items.hemp.ItemHempSeed;
import speiger.src.spmodapi.common.items.hemp.ItemMultiPlate;
import speiger.src.spmodapi.common.lib.SpmodAPILib;
import speiger.src.spmodapi.common.util.proxy.RegisterProxy;

public class APIItemsConfig
{
	private static SpmodConfig config;
	private static APIItems items;
	
	public static void loadItems()
	{
		Items.ender_pearl.setMaxStackSize(64);
		Items.egg.setMaxStackSize(64);

		items.hempSeed = new ItemHempSeed();
		RegisterProxy.RegisterItem(items.hempSeed);

		items.hemp = new ItemHemp();
		RegisterProxy.RegisterItem(items.hemp);

		items.hempBoots = new HempArmor(3);
		RegisterProxy.RegisterItem(items.hempBoots);

		items.hempLeggings = new HempArmor(2);
		RegisterProxy.RegisterItem(items.hempLeggings);

		items.hempChestPlate = new HempArmor(1);
		RegisterProxy.RegisterItem(items.hempChestPlate);

		items.hempHelmet = new HempArmor(0);
		RegisterProxy.RegisterItem(items.hempHelmet);

		items.compressedHemp = new ItemCompressedHemp();
		RegisterProxy.RegisterItem(items.compressedHemp);

		items.hempResin = new ItemHempResin();
		RegisterProxy.RegisterItem(items.hempResin);

		items.gears = new ItemGear();
		RegisterProxy.RegisterItem(items.gears);

		items.blueDye = new ItemBlueDye();
		RegisterProxy.RegisterItem(items.blueDye);

		items.multiPlate = new ItemMultiPlate();
		RegisterProxy.RegisterItem(items.multiPlate);

		items.colorCard = new ItemColorCard();
		RegisterProxy.RegisterItem(items.colorCard);

		items.hempResinBucket = new ItemHempBucket();
		RegisterProxy.RegisterItem(items.hempResinBucket);

		items.expBottles = new ExpBottle();
		RegisterProxy.RegisterItem(items.expBottles);

		items.mobMachineHelper = new ItemMobMachineHelper();
		RegisterProxy.RegisterItem(items.mobMachineHelper);

		initHempPlates();
	}
	
	private static void initHempPlates()
	{
		ResourceLocation[] models = new ResourceLocation[]{
				new ResourceLocation(SpmodAPILib.ModID.toLowerCase()+":textures/models/plates/HanfSign.png"),
				new ResourceLocation(SpmodAPILib.ModID.toLowerCase()+":textures/models/plates/BlackHanfSign.png"),
				new ResourceLocation(SpmodAPILib.ModID.toLowerCase()+":textures/models/plates/RedHanfSign.png"),
				new ResourceLocation(SpmodAPILib.ModID.toLowerCase()+":textures/models/plates/GreenHanfSign.png"),
				new ResourceLocation(SpmodAPILib.ModID.toLowerCase()+":textures/models/plates/BrownHanfSign.png"),
				new ResourceLocation(SpmodAPILib.ModID.toLowerCase()+":textures/models/plates/BlueHanfSign.png"),
				new ResourceLocation(SpmodAPILib.ModID.toLowerCase()+":textures/models/plates/PurpleHanfSign.png"),
				new ResourceLocation(SpmodAPILib.ModID.toLowerCase()+":textures/models/plates/CyanHanfSign.png"),
				new ResourceLocation(SpmodAPILib.ModID.toLowerCase()+":textures/models/plates/SilverHanfSign.png"),
				new ResourceLocation(SpmodAPILib.ModID.toLowerCase()+":textures/models/plates/GrayHanfSign.png"),
				new ResourceLocation(SpmodAPILib.ModID.toLowerCase()+":textures/models/plates/PinkHanfSign.png"),
				new ResourceLocation(SpmodAPILib.ModID.toLowerCase()+":textures/models/plates/LimeHanfSign.png"),
				new ResourceLocation(SpmodAPILib.ModID.toLowerCase()+":textures/models/plates/YellowHanfSign.png"),
				new ResourceLocation(SpmodAPILib.ModID.toLowerCase()+":textures/models/plates/LightBlueHanfSign.png"),
				new ResourceLocation(SpmodAPILib.ModID.toLowerCase()+":textures/models/plates/MagentaHanfSign.png"),
				new ResourceLocation(SpmodAPILib.ModID.toLowerCase()+":textures/models/plates/OrangeHanfSign.png"),
				new ResourceLocation(SpmodAPILib.ModID.toLowerCase()+":textures/models/plates/WhiteHanfSign.png")
		};
		String[] identifiers = new String[]{
			"hemp.Sign",
			"hemp.Sign.black",
			"hemp.Sign.red",
			"hemp.Sign.green",
			"hemp.Sign.brown",
			"hemp.Sign.blue",
			"hemp.Sign.purple",
			"hemp.Sign.cyan",
			"hemp.Sign.lightGray",
			"hemp.Sign.gray",
			"hemp.Sign.pink",
			"hemp.Sign.lime",
			"hemp.Sign.yellow",
			"hemp.Sign.lightBlue",
			"hemp.Sign.magenta",
			"hemp.Sign.orange",
			"hemp.Sign.white",
		};
		String[] textures = new String[]{
				SpmodAPILib.ModID.toLowerCase()+":plates/HanfSign",
				SpmodAPILib.ModID.toLowerCase()+":plates/HanfSignBlack",
				SpmodAPILib.ModID.toLowerCase()+":plates/HanfSignRed",
				SpmodAPILib.ModID.toLowerCase()+":plates/HanfSignGreen",
				SpmodAPILib.ModID.toLowerCase()+":plates/HanfSignBrown",
				SpmodAPILib.ModID.toLowerCase()+":plates/HanfSignBlue",
				SpmodAPILib.ModID.toLowerCase()+":plates/HanfSignPurple",
				SpmodAPILib.ModID.toLowerCase()+":plates/HanfSignCyan",
				SpmodAPILib.ModID.toLowerCase()+":plates/HanfSignSilver",
				SpmodAPILib.ModID.toLowerCase()+":plates/HanfSignGray",
				SpmodAPILib.ModID.toLowerCase()+":plates/HanfSignPink",
				SpmodAPILib.ModID.toLowerCase()+":plates/HanfSignLime",
				SpmodAPILib.ModID.toLowerCase()+":plates/HanfSignYellow",
				SpmodAPILib.ModID.toLowerCase()+":plates/HanfSignLightBlue",
				SpmodAPILib.ModID.toLowerCase()+":plates/HanfSignMagenta",
				SpmodAPILib.ModID.toLowerCase()+":plates/HanfSignOrange",
				SpmodAPILib.ModID.toLowerCase()+":plates/HanfSignWhite"
		};
		items.hempPlates = new ItemStack[models.length];
		for(int i = 0;i<items.hempPlates.length;i++)
		{
			items.hempPlates[i] = PlateManager.plates.registerPlate(models[i], textures[i], identifiers[i], getDisplayName(identifiers[i])).getItemStack();
		}
	}
	
	static String getDisplayName(String par1)
	{
		return LanguageRegister.getLanguageName(new DisplayStack(null, 0), par1, SpmodAPI.instance);
	}
}
