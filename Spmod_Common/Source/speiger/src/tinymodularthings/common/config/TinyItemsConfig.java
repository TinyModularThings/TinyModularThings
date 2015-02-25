package speiger.src.tinymodularthings.common.config;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.proxy.RegisterProxy;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.items.ItemIngots;
import speiger.src.tinymodularthings.common.items.ItemTinyItem;
import speiger.src.tinymodularthings.common.items.energy.Batteries;
import speiger.src.tinymodularthings.common.items.energy.Batteries.BatterieType;
import speiger.src.tinymodularthings.common.items.itemblocks.storage.ItemAdvTinyChest;
import speiger.src.tinymodularthings.common.items.itemblocks.storage.ItemAdvTinyTank;
import speiger.src.tinymodularthings.common.items.itemblocks.storage.ItemTinyChest;
import speiger.src.tinymodularthings.common.items.itemblocks.storage.ItemTinyTank;
import speiger.src.tinymodularthings.common.items.itemblocks.transport.ItemInterfaceBlock;
import speiger.src.tinymodularthings.common.items.itemblocks.transport.ItemTinyHopper;
import speiger.src.tinymodularthings.common.items.itemblocks.transport.ItemTinyHopper.HopperType;
import speiger.src.tinymodularthings.common.items.minecarts.AdvTinyChestCart;
import speiger.src.tinymodularthings.common.items.minecarts.TinyChestCart;
import speiger.src.tinymodularthings.common.items.tools.ItemCell;
import speiger.src.tinymodularthings.common.items.tools.ItemNetherCrystal;
import speiger.src.tinymodularthings.common.items.tools.ItemPotionBag;
import speiger.src.tinymodularthings.common.items.tools.ItemTinyInfo;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;

public class TinyItemsConfig
{
	private static TinyItems items;
	private static TinyConfig config;
	
	public static void initItems()
	{
		TextureEngine engine = TextureEngine.getTextures();
		
		engine.setCurrentPath("ingots");
		items.ingots = new ItemIngots(config.item.getCurrentID());
		RegisterItem(items.ingots, "Ingots");
		engine.registerTexture(items.ingots, "IngotCopper", "IngotTin", "IngotAluminium", "IngotSilver", "IngotLead", "IngotBronze", "IngotIridium");
		config.item.updateToNextID();
		
		items.IridiumDrop = new ItemTinyItem(config.item.getCurrentID(), "Unrefined Iridium");
		RegisterItem(items.IridiumDrop, "IridiumDrop");
		engine.registerTexture(items.IridiumDrop, "UnrefinedIridium");
		config.item.updateToNextID();
		
		items.bauxitDust = new ItemTinyItem(config.item.getCurrentID(), "Bauxit Dust").setUnlocalizedName("BauxiteDust");
		RegisterItem(items.bauxitDust, "BaxitDust");
		engine.removePath();
		engine.registerTexture(items.bauxitDust, "dusts/BauxitDust");
		config.item.updateToNextID();
		
		//Free ID Slot.
		config.item.updateToNextID();
		
		items.tinyChest = new ItemTinyChest(config.item.getCurrentID());
		RegisterItem(items.tinyChest, "TinyChests");
		config.item.updateToNextID();
		
		items.tinyTank = new ItemTinyTank(config.item.getCurrentID());
		RegisterItem(items.tinyTank, "TinyTanks");
		config.item.updateToNextID();
		
		items.tinyStorageCart = new TinyChestCart(TinyConfig.item.getCurrentID());
		RegisterItem(items.tinyStorageCart, "TinyChestCart");
		config.item.updateToNextID();
		
		items.advTinyChest = new ItemAdvTinyChest(TinyConfig.item.getCurrentID());
		RegisterItem(items.advTinyChest, "AdvTinyChests");
		config.item.updateToNextID();
		
		items.advTinyStorageCart = new AdvTinyChestCart(TinyConfig.item.getCurrentID());
		RegisterItem(items.advTinyStorageCart, "AdvTinyChestCart");
		config.item.updateToNextID();
		
		items.playerGame = new ItemTinyInfo(TinyConfig.item.getCurrentID());
		RegisterItem(items.playerGame, "PlayerChanger");
		config.item.updateToNextID();
		
		items.interfaceBlock = new ItemInterfaceBlock(TinyConfig.item.getCurrentID());
		RegisterItem(items.interfaceBlock, "InterfaceBlock");
		config.item.updateToNextID();
		
		TinyItems.tinyHopper = new ItemTinyHopper(config.item.getCurrentID(), HopperType.Items);
		RegisterItem(items.tinyHopper, "TinyHopper");
		config.item.updateToNextID();
		
		engine.setCurrentPath("tools");
		items.netherCrystal = new ItemNetherCrystal(config.item.getCurrentID());
		RegisterItem(items.netherCrystal, "NetherCrystal");
		engine.registerTexture(items.netherCrystal, "NetherCrystal", "NetherCrystal_Charged", "netherCrystal_discharged");
		FluidContainerRegistry.registerFluidContainer(new FluidContainerData(new FluidStack(FluidRegistry.LAVA, 1000), new ItemStack(items.netherCrystal, 1, 3), null, true));
		config.item.updateToNextID();

		items.advTinyTank = new ItemAdvTinyTank(config.item.getCurrentID());
		RegisterItem(items.advTinyTank, "AdvTinyTank");
		config.item.updateToNextID();

		items.smallMJBattery = new Batteries(config.item.getCurrentID(), "Small", BatterieType.Small);
		RegisterItem(items.smallMJBattery, "SmallMJBattery");
		config.item.updateToNextID();

		items.mediumMJBattery = new Batteries(config.item.getCurrentID(), "Medium", BatterieType.Medium);
		RegisterItem(items.mediumMJBattery, "MediumMJBattery");
		config.item.updateToNextID();

		items.bigMJBattery = new Batteries(config.item.getCurrentID(), "Big", BatterieType.Big);
		RegisterItem(items.bigMJBattery, "BigMJBattery");
		config.item.updateToNextID();
		
		items.hugeMJBattery = new Batteries(config.item.getCurrentID(), "Huge", BatterieType.Huge);
		RegisterItem(items.hugeMJBattery, "HugeMJBattery");
		items.hugeMJBattery.registerTexture(engine);
		config.item.updateToNextID();

		items.advTinyHopper = new ItemTinyHopper(config.item.getCurrentID(), HopperType.AdvItems);
		RegisterItem(items.advTinyHopper, "AdvancedTinyHopper");
		config.item.updateToNextID();

		items.fluidHopper = new ItemTinyHopper(config.item.getCurrentID(), HopperType.Fluids);
		RegisterItem(items.fluidHopper, "FluidHopper");
		config.item.updateToNextID();

		items.advFluidHopper = new ItemTinyHopper(config.item.getCurrentID(), HopperType.AdvFluids);
		RegisterItem(items.advFluidHopper, "AdvancedFluidHopper");
		config.item.updateToNextID();
		
		items.energyHopper = new ItemTinyHopper(config.item.getCurrentID(), HopperType.Energy);
		RegisterItem(items.energyHopper, "EnergyHopper");
		config.item.updateToNextID();
		
		items.advEnergyHopper = new ItemTinyHopper(config.item.getCurrentID(), HopperType.AdvancedEnergy);
		RegisterItem(items.advEnergyHopper, "AdvancedEnergyHopper");
		config.item.updateToNextID();
		
		engine.setCurrentPath("tools");
		items.cell = new ItemCell(config.item.getCurrentID(), 5000);
		RegisterItem(items.cell, "SmallCell");
		engine.registerTexture(items.cell, "tinyCell_fg", "tinyCell_bg", "tinyCell_empty");
		config.item.updateToNextID();
		
		items.potionBag = new ItemPotionBag(config.item.getCurrentID());
		RegisterItem(items.potionBag, "PotionBag");
		engine.registerTexture(items.potionBag, "potionBag", "potionBag_noPotions", "potionBag_inactive");
		config.item.updateToNextID();
		
	}
	
	public static void RegisterItem(Item par1, String name)
	{
		RegisterProxy.RegisterItem(TinyModularThingsLib.ModID, name, par1);
	}
}
