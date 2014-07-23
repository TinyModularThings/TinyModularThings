package speiger.src.tinymodularthings.common.config;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import speiger.src.spmodapi.common.util.proxy.RegisterProxy;
import speiger.src.tinymodularthings.common.blocks.machine.BlockMachine;
import speiger.src.tinymodularthings.common.blocks.machine.BucketFillerBasic;
import speiger.src.tinymodularthings.common.blocks.machine.ItemBlockMachine;
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
import speiger.src.tinymodularthings.common.blocks.storage.BlockStorage;
import speiger.src.tinymodularthings.common.blocks.storage.ItemBlockStorage;
import speiger.src.tinymodularthings.common.blocks.storage.TinyChest;
import speiger.src.tinymodularthings.common.blocks.storage.TinyTank;
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
import speiger.src.tinymodularthings.common.utils.TinyTextureHelper;

public class TinyBlocksConfig
{
	private static TinyBlocks blocks;
	private static TinyConfig config;
	
	public static void initBlocks()
	{
		TinyBlocks.ores = new BlockSpmodOre(TinyConfig.block.getCurrentID());
		RegisterProxy.RegisterBlock(TinyBlocks.ores, ItemBlockSpmodOre.class, "TinyOres");
		TinyConfig.block.updateToNextID();
		TinyBlocks.bauxitOre = new BlockMultiMineOre(TinyConfig.block.getCurrentID(), Material.grass, TinyTextureHelper.getTextureStringFromName("ores/oreBauxit"), 8)
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
		blocks.bauxitOre.setHardness(0.3F);
		blocks.bauxitOre.setStepSound(Block.soundGravelFootstep);
		blocks.bauxitOre.setCreativeTab(CreativeTabs.tabFood);
		RegisterProxy.RegisterBlock(TinyBlocks.bauxitOre, ItemBlockMultiMineOre.class, "BauxitOreTiny");
		ItemBlockMultiMineOre.addName(TinyBlocks.bauxitOre, "oreBauxit");
		TinyConfig.block.updateToNextID();
		
		blocks.smallPipe = new BlockPipe(TinyConfig.block.getCurrentID(), EnumPipes.Small.getPipeInformation());
		RegisterProxy.RegisterBlock(TinyBlocks.smallPipe, ItemBlockPipe.class, "SmallPipe");
		TinyConfig.block.updateToNextID();
		
		blocks.normalPipe = new BlockPipe(TinyConfig.block.getCurrentID(), EnumPipes.Normal.getPipeInformation());
		RegisterProxy.RegisterBlock(TinyBlocks.normalPipe, ItemBlockPipe.class, "NormalPipe");
		TinyConfig.block.updateToNextID();
		
		blocks.mediumPipe = new BlockPipe(TinyConfig.block.getCurrentID(), EnumPipes.Medium.getPipeInformation());
		RegisterProxy.RegisterBlock(TinyBlocks.mediumPipe, ItemBlockPipe.class, "MediumPipe");
		TinyConfig.block.updateToNextID();
		
		blocks.bigPipe = new BlockPipe(TinyConfig.block.getCurrentID(), EnumPipes.Big.getPipeInformation());
		RegisterProxy.RegisterBlock(TinyBlocks.bigPipe, ItemBlockPipe.class, "BigPipe");
		TinyConfig.block.updateToNextID();
		
		blocks.biggerPipe = new BlockPipe(TinyConfig.block.getCurrentID(), EnumPipes.Bigger.getPipeInformation());
		RegisterProxy.RegisterBlock(TinyBlocks.biggerPipe, ItemBlockPipe.class, "BiggerPipe");
		TinyConfig.block.updateToNextID();
		
		blocks.largePipe = new BlockPipe(TinyConfig.block.getCurrentID(), EnumPipes.Large.getPipeInformation());
		RegisterProxy.RegisterBlock(TinyBlocks.largePipe, ItemBlockPipe.class, "LargePipe");
		TinyConfig.block.updateToNextID();
		
		blocks.hugePipe = new BlockPipe(TinyConfig.block.getCurrentID(), EnumPipes.Huge.getPipeInformation());
		RegisterProxy.RegisterBlock(TinyBlocks.hugePipe, ItemBlockPipe.class, "HugePipe");
		TinyConfig.block.updateToNextID();
		
		blocks.machine = new BlockMachine(TinyConfig.block.getCurrentID());
		RegisterProxy.RegisterBlock(TinyBlocks.machine, ItemBlockMachine.class, "BlockMachines");
		RegisterProxy.RegisterTile(PressureFurnace.class, "PressureFurnace");
		RegisterProxy.RegisterTile(BucketFillerBasic.class, "BucketFiller");
		RegisterProxy.RegisterTile(SelfPoweredBucketFiller.class, "SelfBucketFiller");
		RegisterProxy.RegisterTile(WaterGenerator.class, "WaterGen");
		TinyConfig.block.updateToNextID();
		
		blocks.storageBlock = new BlockStorage(TinyConfig.block.getCurrentID());
		RegisterProxy.RegisterBlock(TinyBlocks.storageBlock, ItemBlockStorage.class, "StorageBlock");
		RegisterProxy.RegisterTile(TinyChest.class, "TinyChests");
		RegisterProxy.RegisterTile(TinyTank.class, "TinyTanks");
		RegisterProxy.RegisterTile(AdvTinyChest.class, "AdvTinyChest");
		TinyConfig.block.updateToNextID();
		
		blocks.transportBlock = new BlockTransport(TinyConfig.block.getCurrentID());
		RegisterProxy.RegisterBlock(TinyBlocks.transportBlock, ItemBlockTransport.class, "TransportBlock");
		RegisterProxy.RegisterTile(EnderChestReader.class, "EnderChestProxy");
		RegisterProxy.RegisterTile(MultiStructureItemInterface.class, "ItemInterface");
		RegisterProxy.RegisterTile(MultiStructureFluidInterface.class, "FluidInterface");
		RegisterProxy.RegisterTile(MultiStructureEnergyInterface.class, "EnergyInterface");
		RegisterProxy.RegisterTile(TinyHopper.class, "ModularTinyHopper");
		TinyConfig.block.updateToNextID();
		
	}
}
