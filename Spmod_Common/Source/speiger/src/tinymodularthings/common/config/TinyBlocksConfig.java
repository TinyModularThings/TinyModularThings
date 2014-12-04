package speiger.src.tinymodularthings.common.config;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.proxy.RegisterProxy;
import speiger.src.tinymodularthings.common.blocks.crafting.BlockCrafting;
import speiger.src.tinymodularthings.common.blocks.crafting.CraftingStation;
import speiger.src.tinymodularthings.common.blocks.crafting.ItemBlockCrafting;
import speiger.src.tinymodularthings.common.blocks.crafting.OreCrafter;
import speiger.src.tinymodularthings.common.blocks.crafting.Uncrafter;
import speiger.src.tinymodularthings.common.blocks.machine.BlockMachine;
import speiger.src.tinymodularthings.common.blocks.machine.BucketFillerBasic;
import speiger.src.tinymodularthings.common.blocks.machine.ItemBlockMachine;
import speiger.src.tinymodularthings.common.blocks.machine.OilGenerator;
import speiger.src.tinymodularthings.common.blocks.machine.PressureFurnace;
import speiger.src.tinymodularthings.common.blocks.machine.SelfPoweredBucketFiller;
import speiger.src.tinymodularthings.common.blocks.machine.WaterGenerator;
import speiger.src.tinymodularthings.common.blocks.ores.BlockMultiMineOre;
import speiger.src.tinymodularthings.common.blocks.ores.BlockSpmodOre;
import speiger.src.tinymodularthings.common.blocks.ores.ItemBlockMultiMineOre;
import speiger.src.tinymodularthings.common.blocks.ores.ItemBlockSpmodOre;
import speiger.src.tinymodularthings.common.blocks.pipes.basic.BlockPipe;
import speiger.src.tinymodularthings.common.blocks.pipes.basic.ItemBlockPipe;
import speiger.src.tinymodularthings.common.blocks.storage.AdvTinyChest;
import speiger.src.tinymodularthings.common.blocks.storage.AdvTinyTank;
import speiger.src.tinymodularthings.common.blocks.storage.BlockStorage;
import speiger.src.tinymodularthings.common.blocks.storage.ItemBlockStorage;
import speiger.src.tinymodularthings.common.blocks.storage.TinyChest;
import speiger.src.tinymodularthings.common.blocks.storage.TinyTank;
import speiger.src.tinymodularthings.common.blocks.transport.AdvancedEnderChestReader;
import speiger.src.tinymodularthings.common.blocks.transport.BlockTransport;
import speiger.src.tinymodularthings.common.blocks.transport.EnderChestReader;
import speiger.src.tinymodularthings.common.blocks.transport.ItemBlockTransport;
import speiger.src.tinymodularthings.common.blocks.transport.MultiStructureEnergyInterface;
import speiger.src.tinymodularthings.common.blocks.transport.MultiStructureFluidInterface;
import speiger.src.tinymodularthings.common.blocks.transport.MultiStructureItemInterface;
import speiger.src.tinymodularthings.common.blocks.transport.TinyHopper;
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
		blocks.machine.registerTextures(engine);
		config.block.updateToNextID();
		
		blocks.storageBlock = new BlockStorage(config.block.getCurrentID());
		RegisterProxy.RegisterBlock(blocks.storageBlock, ItemBlockStorage.class, "StorageBlock");
		RegisterProxy.RegisterTile(blocks.storageBlock, 0, TinyChest.class, "TinyChests");
		RegisterProxy.RegisterTile(blocks.storageBlock, 1, TinyTank.class, "TinyTanks");
		RegisterProxy.RegisterTile(blocks.storageBlock, 2, AdvTinyChest.class, "AdvTinyChest");
		RegisterProxy.RegisterTile(blocks.storageBlock, 3, AdvTinyTank.class, "AdvTinyTank");
		engine.setCurrentPath("storage");
		engine.registerTexture(blocks.storageBlock, "TinyTank", "AdvTinyTank");
		config.block.updateToNextID();
		
		blocks.transportBlock = new BlockTransport(config.block.getCurrentID());
		RegisterProxy.RegisterBlock(blocks.transportBlock, ItemBlockTransport.class, "TransportBlock");
		RegisterProxy.RegisterTile(blocks.transportBlock, EnderChestReader.class, "EnderChestProxy");
		RegisterProxy.RegisterTile(blocks.transportBlock, 1, MultiStructureItemInterface.class, "ItemInterface");
		RegisterProxy.RegisterTile(blocks.transportBlock, 2, MultiStructureFluidInterface.class, "FluidInterface");
		RegisterProxy.RegisterTile(blocks.transportBlock, 3, MultiStructureEnergyInterface.class, "EnergyInterface");
		RegisterProxy.RegisterTile(TinyHopper.class, "ModularTinyHopper");
		RegisterProxy.RegisterTile(blocks.transportBlock, 4, AdvancedEnderChestReader.class, "AdvEnderChestProxy");
		blocks.transportBlock.registerTextures(engine);
		config.block.updateToNextID();
		
		blocks.craftingBlock = new BlockCrafting(config.block.getCurrentID());
		RegisterProxy.RegisterBlock(blocks.craftingBlock, ItemBlockCrafting.class, "CraftingBlock");
		RegisterProxy.RegisterTile(blocks.craftingBlock, 1, OreCrafter.class, "OreCrafter");
		RegisterProxy.RegisterTile(blocks.craftingBlock, 2, CraftingStation.class, "CraftingStation");
		RegisterProxy.RegisterTile(blocks.craftingBlock, 3, Uncrafter.class, "Deconstructor");
		blocks.craftingBlock.registerTextures(engine);
		config.block.updateToNextID();
		
	}
}
