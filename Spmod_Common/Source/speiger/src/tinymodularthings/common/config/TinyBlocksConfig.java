package speiger.src.tinymodularthings.common.config;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.proxy.RegisterProxy;
import speiger.src.tinymodularthings.common.blocks.crafting.*;
import speiger.src.tinymodularthings.common.blocks.machine.*;
import speiger.src.tinymodularthings.common.blocks.ores.BlockMultiMineOre;
import speiger.src.tinymodularthings.common.blocks.ores.BlockSpmodOre;
import speiger.src.tinymodularthings.common.blocks.ores.ItemBlockMultiMineOre;
import speiger.src.tinymodularthings.common.blocks.ores.ItemBlockSpmodOre;
import speiger.src.tinymodularthings.common.blocks.pipes.basic.BlockPipe;
import speiger.src.tinymodularthings.common.blocks.pipes.basic.ItemBlockPipe;
import speiger.src.tinymodularthings.common.blocks.redstone.BlockRedstone;
import speiger.src.tinymodularthings.common.blocks.redstone.Detector;
import speiger.src.tinymodularthings.common.blocks.redstone.ItemBlockRedstone;
import speiger.src.tinymodularthings.common.blocks.storage.*;
import speiger.src.tinymodularthings.common.blocks.transport.*;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.enums.EnumPipes;

public class TinyBlocksConfig
{
	private static TinyBlocks blocks;
	private static TinyConfig config;
	
	public static void initBlocks()
	{
		TextureEngine engine = TextureEngine.getTextures();
		
		engine.setCurrentPath("ores");
		blocks.ores = new BlockSpmodOre(config.block.getCurrentID());
		RegisterProxy.RegisterBlock(blocks.ores, ItemBlockSpmodOre.class, "TinyOres");
		blocks.ores.registerTextures(engine);
		config.block.updateToNextID();
		
		
		blocks.bauxitOre = new BlockMultiMineOre(config.block.getCurrentID(), Material.grass, 8)
		{
			@Override
			public ItemStack getBasicDrop(int fortune, int meta)
			{
				return null;
			}
			
			@Override
			public ItemStack[] getDrops(int fortune, int meta)
			{
				return new ItemStack[] { new ItemStack(TinyItems.bauxitDust, 2) };
			}
			
			@Override
			public ItemStack[] getRareDrops(int fortune, int meta)
			{
				return new ItemStack[] { new ItemStack(TinyItems.bauxitDust, 3), new ItemStack(TinyItems.bauxitDust, 4), new ItemStack(TinyItems.bauxitDust, 5), new ItemStack(TinyItems.bauxitDust, 6) };
			}
		};
		engine.registerTexture(blocks.bauxitOre, "oreBauxit");
		blocks.bauxitOre.setHardness(0.3F);
		blocks.bauxitOre.setStepSound(Block.soundGravelFootstep);
		blocks.bauxitOre.setCreativeTab(CreativeTabs.tabFood);
		RegisterProxy.RegisterBlock(blocks.bauxitOre, ItemBlockMultiMineOre.class, "BauxitOreTiny");
		ItemBlockMultiMineOre.addName(blocks.bauxitOre, "Bauxit Ore");
		MinecraftForge.setBlockHarvestLevel(blocks.bauxitOre, "shovel", 0);
		config.block.updateToNextID();
		
		
		engine.setCurrentPath("pipes");
		blocks.smallPipe = new BlockPipe(config.block.getCurrentID(), EnumPipes.Small.getPipeInformation(), engine);
		RegisterProxy.RegisterBlock(blocks.smallPipe, ItemBlockPipe.class, "SmallPipe");
		config.block.updateToNextID();
		
		blocks.normalPipe = new BlockPipe(config.block.getCurrentID(), EnumPipes.Normal.getPipeInformation(), engine);
		RegisterProxy.RegisterBlock(blocks.normalPipe, ItemBlockPipe.class, "NormalPipe");
		config.block.updateToNextID();
		
		blocks.mediumPipe = new BlockPipe(config.block.getCurrentID(), EnumPipes.Medium.getPipeInformation(), engine);
		RegisterProxy.RegisterBlock(blocks.mediumPipe, ItemBlockPipe.class, "MediumPipe");
		config.block.updateToNextID();
		
		blocks.bigPipe = new BlockPipe(config.block.getCurrentID(), EnumPipes.Big.getPipeInformation(), engine);
		RegisterProxy.RegisterBlock(blocks.bigPipe, ItemBlockPipe.class, "BigPipe");
		config.block.updateToNextID();
		
		blocks.biggerPipe = new BlockPipe(config.block.getCurrentID(), EnumPipes.Bigger.getPipeInformation(), engine);
		RegisterProxy.RegisterBlock(blocks.biggerPipe, ItemBlockPipe.class, "BiggerPipe");
		config.block.updateToNextID();
		
		blocks.largePipe = new BlockPipe(config.block.getCurrentID(), EnumPipes.Large.getPipeInformation(), engine);
		RegisterProxy.RegisterBlock(blocks.largePipe, ItemBlockPipe.class, "LargePipe");
		config.block.updateToNextID();
		
		blocks.hugePipe = new BlockPipe(config.block.getCurrentID(), EnumPipes.Huge.getPipeInformation(), engine);
		RegisterProxy.RegisterBlock(blocks.hugePipe, ItemBlockPipe.class, "HugePipe");
		config.block.updateToNextID();
		
		blocks.machine = new BlockMachine(config.block.getCurrentID());
		RegisterProxy.RegisterBlock(blocks.machine, ItemBlockMachine.class, "BlockMachines");
		RegisterProxy.RegisterTile(blocks.machine, 0, PressureFurnace.class, "PressureFurnace");
		RegisterProxy.RegisterTile(blocks.machine, 1, BucketFillerBasic.class, "BucketFiller");
		RegisterProxy.RegisterTile(blocks.machine, 2, SelfPoweredBucketFiller.class, "SelfBucketFiller");
		RegisterProxy.RegisterTile(blocks.machine, 3, WaterGenerator.class, "WaterGen");
		RegisterProxy.RegisterTile(blocks.machine, 4, OilGenerator.class, "OilGenerator");
		RegisterProxy.RegisterTile(blocks.machine, 5, MachineWaterSpender.class, "WaterSpender");
		RegisterProxy.RegisterTile(blocks.machine, 6, IC2CropFarm.class, "IC2CropFarm");
		blocks.machine.registerTextures(engine);
		config.block.updateToNextID();
		
		blocks.storageBlock = new BlockStorage(config.block.getCurrentID());
		RegisterProxy.RegisterBlock(blocks.storageBlock, ItemBlockStorage.class, "StorageBlock");
		RegisterProxy.RegisterTile(blocks.storageBlock, 0, TinyChest.class, "TinyChests");
		RegisterProxy.RegisterTile(blocks.storageBlock, 1, TinyTank.class, "TinyTanks");
		RegisterProxy.RegisterTile(blocks.storageBlock, 2, AdvTinyChest.class, "AdvTinyChest");
		RegisterProxy.RegisterTile(blocks.storageBlock, 3, AdvTinyTank.class, "AdvTinyTank");
		RegisterProxy.RegisterTile(blocks.storageBlock, 4, TinyBarrel.class, "TMTBarrel");
		engine.setCurrentPath("storage");
		engine.registerTexture(blocks.storageBlock, "TinyTank", "AdvTinyTank");
		engine.registerTexture(blocks.storageBlock, 4, "TinyBarrel_Front", "TinyBarrel_Bottom", "TinyBarrel_Side");

		config.block.updateToNextID();
		
		blocks.transportBlock = new BlockTransport(config.block.getCurrentID());
		RegisterProxy.RegisterBlock(blocks.transportBlock, ItemBlockTransport.class, "TransportBlock");
		RegisterProxy.RegisterTile(blocks.transportBlock, EnderChestReader.class, "EnderChestProxy");
		RegisterProxy.RegisterTile(blocks.transportBlock, 1, MultiStructureItemInterface.class, "ItemInterface");
		RegisterProxy.RegisterTile(blocks.transportBlock, 2, MultiStructureFluidInterface.class, "FluidInterface");
		RegisterProxy.RegisterTile(blocks.transportBlock, 3, MultiStructureEnergyInterface.class, "EnergyInterface");
		RegisterProxy.RegisterTile(TinyHopper.class, "ModularTinyHopper");
		RegisterProxy.RegisterTile(blocks.transportBlock, 4, AdvancedEnderChestReader.class, "AdvEnderChestProxy");
		RegisterProxy.RegisterTile(blocks.transportBlock, 5, ChargingBench.class, "BCEnergyChargingBench");
		RegisterProxy.RegisterTile(blocks.transportBlock, 6, BatteryStation.class, "BCEnergyBatteryStation");
		blocks.transportBlock.registerTextures(engine);
		config.block.updateToNextID();
		
		blocks.craftingBlock = new BlockCrafting(config.block.getCurrentID());
		RegisterProxy.RegisterBlock(blocks.craftingBlock, ItemBlockCrafting.class, "CraftingBlock");
		RegisterProxy.RegisterTile(blocks.craftingBlock, 1, OreCrafter.class, "OreCrafter");
		RegisterProxy.RegisterTile(blocks.craftingBlock, 2, CraftingStation.class, "SpmodCraftingStation");
		RegisterProxy.RegisterTile(blocks.craftingBlock, 3, Uncrafter.class, "Deconstructor");
		blocks.craftingBlock.registerTextures(engine);
		config.block.updateToNextID();
		
		blocks.redstoneBlock = new BlockRedstone(config.block.getCurrentID());
		RegisterProxy.RegisterBlock(blocks.redstoneBlock, ItemBlockRedstone.class, "RedstoneLogicalBlock");
		RegisterProxy.RegisterTile(blocks.redstoneBlock, 0, Detector.class, "TMT Detector");
		blocks.redstoneBlock.registerTextures(engine);
		config.block.updateToNextID();
	}
}
