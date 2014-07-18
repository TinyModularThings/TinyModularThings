package speiger.src.spmodapi.common.config;

import speiger.src.spmodapi.common.blocks.deko.BlockBlueFlower;
import speiger.src.spmodapi.common.blocks.deko.BlockLightDeko;
import speiger.src.spmodapi.common.blocks.deko.BlockMutliPlate;
import speiger.src.spmodapi.common.blocks.deko.ItemBlockFlower;
import speiger.src.spmodapi.common.blocks.deko.ItemBlockLightDekoBlock;
import speiger.src.spmodapi.common.blocks.deko.ItemBlockMultiPlate;
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
		APIBlocks.hempCrop = new BlockHempCrop();
		RegisterProxy.RegisterBlock(APIBlocks.hempCrop, ItemBlockHempCrop.class, "Hemp Crop");

		APIBlocks.hempStraw = new BlockHempStraw();
		RegisterProxy.RegisterBlock(APIBlocks.hempStraw, ItemBlockHempStraw.class, "Hemp Straw");

		blocks.hempBlockBase = new BlockHempDekoBase();
		RegisterProxy.RegisterBlock(blocks.hempBlockBase, ItemBlockHempDekoBase.class, "HempDekoBase");

		APIBlocks.hempBlock = new BlockHempDeko(EnumHempBlocks.BasicHemp.getInfos());
		RegisterProxy.RegisterBlock(APIBlocks.hempBlock, ItemBlockHempDeko.class, "HempBlockDeko");

		APIBlocks.hempBrick = new BlockHempDeko(EnumHempBlocks.BrickHemp.getInfos());
		RegisterProxy.RegisterBlock(APIBlocks.hempBrick, ItemBlockHempDeko.class, "HempBrickDeko");

		APIBlocks.hempBlockPlated = new BlockHempDeko(EnumHempBlocks.PlatedHemp.getInfos());
		RegisterProxy.RegisterBlock(APIBlocks.hempBlockPlated, ItemBlockHempDeko.class, "PlatedHempBlockDeko");

		APIBlocks.hempBrickPlated = new BlockHempDeko(EnumHempBlocks.PlatedHempBrick.getInfos());
		RegisterProxy.RegisterBlock(APIBlocks.hempBrickPlated, ItemBlockHempDeko.class, "PlatedHempBrickDeko");

		APIBlocks.savedHempBlock = new BlockHempDeko(EnumHempBlocks.SaveBasicHemp.getInfos());
		RegisterProxy.RegisterBlock(APIBlocks.savedHempBlock, ItemBlockHempDeko.class, "SaveHempBlockDeko");

		APIBlocks.savedHempBrick = new BlockHempDeko(EnumHempBlocks.SaveBrickHemp.getInfos());
		RegisterProxy.RegisterBlock(APIBlocks.savedHempBrick, ItemBlockHempDeko.class, "SaveHempBrickDeko");

		APIBlocks.savedHempBlockPlated = new BlockHempDeko(EnumHempBlocks.SavePlatedHemp.getInfos());
		RegisterProxy.RegisterBlock(APIBlocks.savedHempBlockPlated, ItemBlockHempDeko.class, "SavePlatedHempBlockDeko");

		APIBlocks.savedHempBrickPlated = new BlockHempDeko(EnumHempBlocks.SavePlatedHempBrick.getInfos());
		RegisterProxy.RegisterBlock(APIBlocks.savedHempBrickPlated, ItemBlockHempDeko.class, "SavePlatedHempBrickDeko");

		blocks.fluidHempResin = new BlockFluidHempResin(APIUtils.hempResin);
		RegisterProxy.RegisterBlock(blocks.fluidHempResin, ItemBlockFluidHempResin.class, "FluidHempResin");

		blocks.hempLamp = new BlockLightDeko();
		RegisterProxy.RegisterBlock(blocks.hempLamp, ItemBlockLightDekoBlock.class, "HempLamp");
		RegisterProxy.RegisterTile(blocks.hempLamp, TileLamp.class, "Lamp");

		blocks.blueFlower = new BlockBlueFlower();
		RegisterProxy.RegisterBlock(blocks.blueFlower, ItemBlockFlower.class, "IngoDye");

		blocks.multiPlate = new BlockMutliPlate();
		RegisterProxy.RegisterBlock(blocks.multiPlate, ItemBlockMultiPlate.class, "MultiPlate");
		RegisterProxy.RegisterTile(MultiPlate.class, "MultiPlates");

		blocks.blockUtils = new BlockUtils();
		RegisterProxy.RegisterBlock(blocks.blockUtils, ItemBlockUtils.class, "Utils Block");
		RegisterProxy.RegisterTile(blocks.blockUtils, ExpStorage.class, "ExpStorage");
		RegisterProxy.RegisterTile(blocks.blockUtils, MobMachine.class, "MobMachine");

	}
}
