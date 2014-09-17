package speiger.src.spmodapi.common.config;

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
import speiger.src.spmodapi.common.blocks.utils.ItemBlockUtils;
import speiger.src.spmodapi.common.blocks.utils.MobMachine;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.enums.EnumHempBlocks;
import speiger.src.spmodapi.common.fluids.hemp.BlockFluidHempResin;
import speiger.src.spmodapi.common.fluids.hemp.ItemBlockFluidHempResin;
import speiger.src.spmodapi.common.util.proxy.RegisterProxy;

public class APIBlocksConfig
{
	private static APIBlocks blocks;
	private static SpmodConfig config;
	
	public static void loadBlocks()
	{
		APIBlocks.hempCrop = new BlockHempCrop(SpmodConfig.blockIDs.getCurrentID());
		RegisterProxy.RegisterBlock(APIBlocks.hempCrop, ItemBlockHempCrop.class, "Hemp Crop");
		SpmodConfig.blockIDs.updateToNextID();
		
		APIBlocks.hempStraw = new BlockHempStraw(SpmodConfig.blockIDs.getCurrentID());
		RegisterProxy.RegisterBlock(APIBlocks.hempStraw, ItemBlockHempStraw.class, "Hemp Straw");
		SpmodConfig.blockIDs.updateToNextID();
		
		blocks.hempBlockBase = new BlockHempDekoBase(config.blockIDs.getCurrentID());
		RegisterProxy.RegisterBlock(blocks.hempBlockBase, ItemBlockHempDekoBase.class, "HempDekoBase");
		config.blockIDs.updateToNextID();
		
		APIBlocks.hempBlock = new BlockHempDeko(SpmodConfig.blockIDs.getCurrentID(), EnumHempBlocks.BasicHemp.getInfos());
		RegisterProxy.RegisterBlock(APIBlocks.hempBlock, ItemBlockHempDeko.class, "HempBlockDeko");
		SpmodConfig.blockIDs.updateToNextID();
		
		APIBlocks.hempBrick = new BlockHempDeko(SpmodConfig.blockIDs.getCurrentID(), EnumHempBlocks.BrickHemp.getInfos());
		RegisterProxy.RegisterBlock(APIBlocks.hempBrick, ItemBlockHempDeko.class, "HempBrickDeko");
		SpmodConfig.blockIDs.updateToNextID();
		
		APIBlocks.hempBlockPlated = new BlockHempDeko(SpmodConfig.blockIDs.getCurrentID(), EnumHempBlocks.PlatedHemp.getInfos());
		RegisterProxy.RegisterBlock(APIBlocks.hempBlockPlated, ItemBlockHempDeko.class, "PlatedHempBlockDeko");
		SpmodConfig.blockIDs.updateToNextID();
		
		APIBlocks.hempBrickPlated = new BlockHempDeko(SpmodConfig.blockIDs.getCurrentID(), EnumHempBlocks.PlatedHempBrick.getInfos());
		RegisterProxy.RegisterBlock(APIBlocks.hempBrickPlated, ItemBlockHempDeko.class, "PlatedHempBrickDeko");
		SpmodConfig.blockIDs.updateToNextID();
		
		APIBlocks.savedHempBlock = new BlockHempDeko(SpmodConfig.blockIDs.getCurrentID(), EnumHempBlocks.SaveBasicHemp.getInfos());
		RegisterProxy.RegisterBlock(APIBlocks.savedHempBlock, ItemBlockHempDeko.class, "SaveHempBlockDeko");
		SpmodConfig.blockIDs.updateToNextID();
		
		APIBlocks.savedHempBrick = new BlockHempDeko(SpmodConfig.blockIDs.getCurrentID(), EnumHempBlocks.SaveBrickHemp.getInfos());
		RegisterProxy.RegisterBlock(APIBlocks.savedHempBrick, ItemBlockHempDeko.class, "SaveHempBrickDeko");
		SpmodConfig.blockIDs.updateToNextID();
		
		APIBlocks.savedHempBlockPlated = new BlockHempDeko(SpmodConfig.blockIDs.getCurrentID(), EnumHempBlocks.SavePlatedHemp.getInfos());
		RegisterProxy.RegisterBlock(APIBlocks.savedHempBlockPlated, ItemBlockHempDeko.class, "SavePlatedHempBlockDeko");
		SpmodConfig.blockIDs.updateToNextID();
		
		APIBlocks.savedHempBrickPlated = new BlockHempDeko(SpmodConfig.blockIDs.getCurrentID(), EnumHempBlocks.SavePlatedHempBrick.getInfos());
		RegisterProxy.RegisterBlock(APIBlocks.savedHempBrickPlated, ItemBlockHempDeko.class, "SavePlatedHempBrickDeko");
		SpmodConfig.blockIDs.updateToNextID();
		
		blocks.fluidHempResin = new BlockFluidHempResin(config.blockIDs.getCurrentID(), APIUtils.hempResin);
		RegisterProxy.RegisterBlock(blocks.fluidHempResin, ItemBlockFluidHempResin.class, "FluidHempResin");
		config.blockIDs.updateToNextID();
		
		blocks.hempLamp = new BlockLightDeko(config.blockIDs.getCurrentID());
		RegisterProxy.RegisterBlock(blocks.hempLamp, ItemBlockLightDekoBlock.class, "HempLamp");
		RegisterProxy.RegisterTile(blocks.hempLamp, TileLamp.class, "Lamp");
		config.blockIDs.updateToNextID();
		
		blocks.blueFlower = new BlockBlueFlower(config.blockIDs.getCurrentID());
		RegisterProxy.RegisterBlock(blocks.blueFlower, ItemBlockFlower.class, "IngoDye");
		config.blockIDs.updateToNextID();
		
		blocks.multiPlate = new BlockMutliPlate(config.blockIDs.getCurrentID());
		RegisterProxy.RegisterBlock(blocks.multiPlate, ItemBlockMultiPlate.class, "MultiPlate");
		RegisterProxy.RegisterTile(MultiPlate.class, "MultiPlates");
		config.blockIDs.updateToNextID();
		
		blocks.blockUtils = new BlockUtils(config.blockIDs.getCurrentID());
		RegisterProxy.RegisterBlock(blocks.blockUtils, ItemBlockUtils.class, "Utils Block");
		RegisterProxy.RegisterTile(blocks.blockUtils, ExpStorage.class, "ExpStorage");
		RegisterProxy.RegisterTile(blocks.blockUtils, MobMachine.class, "MobMachine");
		config.blockIDs.updateToNextID();
		
		blocks.statues = new BlockKyrokaTheFox(config.blockIDs.getCurrentID());
		RegisterProxy.RegisterBlock(blocks.statues, ItemBlockKyrokaTheFox.class, "Statue");
		RegisterProxy.RegisterTile(blocks.statues, KyrokaTheFox.class, "Kyroka");
		config.blockIDs.updateToNextID();
		
		
	}
}
