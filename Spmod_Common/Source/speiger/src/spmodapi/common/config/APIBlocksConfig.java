package speiger.src.spmodapi.common.config;

import speiger.src.spmodapi.common.blocks.cores.SpmodBlockBase;
import speiger.src.spmodapi.common.blocks.deko.BlockBlueFlower;
import speiger.src.spmodapi.common.blocks.deko.BlockKyrokaTheFox;
import speiger.src.spmodapi.common.blocks.deko.BlockLightDeko;
import speiger.src.spmodapi.common.blocks.deko.BlockMutliPlate;
import speiger.src.spmodapi.common.blocks.deko.ItemBlockFlower;
import speiger.src.spmodapi.common.blocks.deko.ItemBlockKyrokaTheFox;
import speiger.src.spmodapi.common.blocks.deko.ItemBlockLightDekoBlock;
import speiger.src.spmodapi.common.blocks.deko.ItemBlockMultiPlate;
import speiger.src.spmodapi.common.blocks.deko.KyrokaTheFox;
import speiger.src.spmodapi.common.blocks.deko.MultiPlate;
import speiger.src.spmodapi.common.blocks.deko.TileLamp;
import speiger.src.spmodapi.common.blocks.gas.BasicAnimalChunkLoader;
import speiger.src.spmodapi.common.blocks.gas.BlockAnimalGas;
import speiger.src.spmodapi.common.blocks.gas.BlockAnimalUtils;
import speiger.src.spmodapi.common.blocks.gas.ItemBlockAnimalGas;
import speiger.src.spmodapi.common.blocks.gas.ItemBlockAnimalUtils;
import speiger.src.spmodapi.common.blocks.hemp.BlockHempCrop;
import speiger.src.spmodapi.common.blocks.hemp.BlockHempDeko;
import speiger.src.spmodapi.common.blocks.hemp.BlockHempDekoBase;
import speiger.src.spmodapi.common.blocks.hemp.BlockHempStraw;
import speiger.src.spmodapi.common.blocks.hemp.ItemBlockHempCrop;
import speiger.src.spmodapi.common.blocks.hemp.ItemBlockHempDeko;
import speiger.src.spmodapi.common.blocks.hemp.ItemBlockHempDekoBase;
import speiger.src.spmodapi.common.blocks.hemp.ItemBlockHempStraw;
import speiger.src.spmodapi.common.blocks.utils.BlockUtils;
import speiger.src.spmodapi.common.blocks.utils.ExpStorage;
import speiger.src.spmodapi.common.blocks.utils.InventoryAccesser;
import speiger.src.spmodapi.common.blocks.utils.ItemBlockUtils;
import speiger.src.spmodapi.common.blocks.utils.MobMachine;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.enums.EnumHempBlocks;
import speiger.src.spmodapi.common.fluids.hemp.BlockFluidHempResin;
import speiger.src.spmodapi.common.fluids.hemp.ItemBlockFluidHempResin;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.proxy.RegisterProxy;

public class APIBlocksConfig
{
	private static APIBlocks blocks;
	private static SpmodConfig config;
	
	public static void loadBlocks()
	{
		TextureEngine engine = TextureEngine.getTextures();
		
		blocks.hempCrop = new BlockHempCrop(config.blockIDs.getCurrentID());
		RegisterProxy.RegisterBlock(blocks.hempCrop, ItemBlockHempCrop.class, "Hemp Crop");
		engine.setCurrentPath("hemp");
		engine.registerTexture(blocks.hempCrop, "HempCrop_1", "HempCrop_2", "HempCrop_3", "HempCrop_4", "HempCrop_5", "HempCrop_6", "HempCrop_7", "HempCrop_8");
		config.blockIDs.updateToNextID();
		
		blocks.hempStraw = new BlockHempStraw(config.blockIDs.getCurrentID());
		RegisterProxy.RegisterBlock(blocks.hempStraw, ItemBlockHempStraw.class, "Hemp Straw");
		engine.registerTexture(blocks.hempStraw, "HempStraw");
		config.blockIDs.updateToNextID();
		
		blocks.hempBlockBase = new BlockHempDekoBase(config.blockIDs.getCurrentID());
		RegisterProxy.RegisterBlock(blocks.hempBlockBase, ItemBlockHempDekoBase.class, "HempDekoBase");
		engine.registerTexture(blocks.hempBlockBase, "HempBase_0", "HempBase_1", "HempBase_2", "HempBase_3");
		config.blockIDs.updateToNextID();
		
		blocks.hempBlock = new BlockHempDeko(config.blockIDs.getCurrentID(), EnumHempBlocks.BasicHemp.getInfos());
		RegisterProxy.RegisterBlock(blocks.hempBlock, ItemBlockHempDeko.class, "HempBlockDeko");
		((SpmodBlockBase)blocks.hempBlock).registerTextures(engine);
		config.blockIDs.updateToNextID();
		
		blocks.hempBrick = new BlockHempDeko(config.blockIDs.getCurrentID(), EnumHempBlocks.BrickHemp.getInfos());
		RegisterProxy.RegisterBlock(blocks.hempBrick, ItemBlockHempDeko.class, "HempBrickDeko");
		((SpmodBlockBase)blocks.hempBrick).registerTextures(engine);
		config.blockIDs.updateToNextID();
		
		blocks.hempBlockPlated = new BlockHempDeko(config.blockIDs.getCurrentID(), EnumHempBlocks.PlatedHemp.getInfos());
		RegisterProxy.RegisterBlock(blocks.hempBlockPlated, ItemBlockHempDeko.class, "PlatedHempBlockDeko");
		((SpmodBlockBase)blocks.hempBlockPlated).registerTextures(engine);
		config.blockIDs.updateToNextID();
		
		blocks.hempBrickPlated = new BlockHempDeko(config.blockIDs.getCurrentID(), EnumHempBlocks.PlatedHempBrick.getInfos());
		RegisterProxy.RegisterBlock(blocks.hempBrickPlated, ItemBlockHempDeko.class, "PlatedHempBrickDeko");
		((SpmodBlockBase)blocks.hempBrickPlated).registerTextures(engine);
		config.blockIDs.updateToNextID();
		
		blocks.savedHempBlock = new BlockHempDeko(config.blockIDs.getCurrentID(), EnumHempBlocks.SaveBasicHemp.getInfos());
		RegisterProxy.RegisterBlock(blocks.savedHempBlock, ItemBlockHempDeko.class, "SaveHempBlockDeko");
		((SpmodBlockBase)blocks.savedHempBlock).registerTextures(engine);
		config.blockIDs.updateToNextID();
		
		blocks.savedHempBrick = new BlockHempDeko(config.blockIDs.getCurrentID(), EnumHempBlocks.SaveBrickHemp.getInfos());
		RegisterProxy.RegisterBlock(blocks.savedHempBrick, ItemBlockHempDeko.class, "SaveHempBrickDeko");
		((SpmodBlockBase)blocks.savedHempBrick).registerTextures(engine);
		config.blockIDs.updateToNextID();
		
		blocks.savedHempBlockPlated = new BlockHempDeko(config.blockIDs.getCurrentID(), EnumHempBlocks.SavePlatedHemp.getInfos());
		RegisterProxy.RegisterBlock(blocks.savedHempBlockPlated, ItemBlockHempDeko.class, "SavePlatedHempBlockDeko");
		((SpmodBlockBase)blocks.savedHempBlockPlated).registerTextures(engine);
		config.blockIDs.updateToNextID();
		
		blocks.savedHempBrickPlated = new BlockHempDeko(config.blockIDs.getCurrentID(), EnumHempBlocks.SavePlatedHempBrick.getInfos());
		RegisterProxy.RegisterBlock(blocks.savedHempBrickPlated, ItemBlockHempDeko.class, "SavePlatedHempBrickDeko");
		((SpmodBlockBase)blocks.savedHempBrickPlated).registerTextures(engine);
		config.blockIDs.updateToNextID();
		
		blocks.fluidHempResin = new BlockFluidHempResin(config.blockIDs.getCurrentID(), APIUtils.hempResin);
		RegisterProxy.RegisterBlock(blocks.fluidHempResin, ItemBlockFluidHempResin.class, "FluidHempResin");
		engine.registerTexture(blocks.fluidHempResin, "hemp.resin");
		config.blockIDs.updateToNextID();
		
		blocks.hempLamp = new BlockLightDeko(config.blockIDs.getCurrentID());
		RegisterProxy.RegisterBlock(blocks.hempLamp, ItemBlockLightDekoBlock.class, "HempLamp");
		RegisterProxy.RegisterTile(blocks.hempLamp, TileLamp.class, "Lamp");
		config.blockIDs.updateToNextID();
		
		blocks.blueFlower = new BlockBlueFlower(config.blockIDs.getCurrentID());
		RegisterProxy.RegisterBlock(blocks.blueFlower, ItemBlockFlower.class, "IngoDye");
		engine.setCurrentPath("flowers");
		engine.registerTexture(blocks.blueFlower, "IngoFlower");
		config.blockIDs.updateToNextID();
		
		blocks.multiPlate = new BlockMutliPlate(config.blockIDs.getCurrentID());
		RegisterProxy.RegisterBlock(blocks.multiPlate, ItemBlockMultiPlate.class, "MultiPlate");
		RegisterProxy.RegisterTile(MultiPlate.class, "MultiPlates");
		config.blockIDs.updateToNextID();
		
		blocks.blockUtils = new BlockUtils(config.blockIDs.getCurrentID());
		RegisterProxy.RegisterBlock(blocks.blockUtils, ItemBlockUtils.class, "Utils Block");
		RegisterProxy.RegisterTile(blocks.blockUtils, 1, ExpStorage.class, "ExpStorage");
		RegisterProxy.RegisterTile(blocks.blockUtils, 2, MobMachine.class, "MobMachine");
		RegisterProxy.RegisterTile(blocks.blockUtils, 4, InventoryAccesser.class, "InventoryAccesser");
		((SpmodBlockBase)blocks.blockUtils).registerTextures(engine);
		config.blockIDs.updateToNextID();
		
		blocks.statues = new BlockKyrokaTheFox(config.blockIDs.getCurrentID());
		RegisterProxy.RegisterBlock(blocks.statues, ItemBlockKyrokaTheFox.class, "Statue");
		RegisterProxy.RegisterTile(blocks.statues, KyrokaTheFox.class, "Kyroka");
		config.blockIDs.updateToNextID();
		
		engine.setCurrentPath("gas");
		blocks.animalGas = new BlockAnimalGas(config.blockIDs.getCurrentID());
		RegisterProxy.RegisterBlock(blocks.animalGas, ItemBlockAnimalGas.class, "AnimalGas");
		engine.registerTexture(blocks.animalGas, "AnimalGas");
		config.blockIDs.updateToNextID();
		
		blocks.animalUtils = new BlockAnimalUtils(config.blockIDs.getCurrentID());
		RegisterProxy.RegisterBlock(blocks.animalUtils, ItemBlockAnimalUtils.class, "AnimalUtils");
		RegisterProxy.RegisterTile(blocks.animalUtils, 0, BasicAnimalChunkLoader.class, "BasisAnimalChunkloader");
		blocks.animalUtils.registerTextures(engine);
		config.blockIDs.updateToNextID();
		
		
		
	}
}
