package speiger.src.spmodapi.common.modHelper.BC;

import net.minecraft.item.ItemStack;
import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.enums.EnumColor;
import buildcraft.api.gates.IAction;
import buildcraft.api.gates.StatementManager;
import cpw.mods.fml.common.event.FMLInterModComms;

public class BCAddon
{
	public static IAction[] loopRandom = new IAction[0];
	public static IAction[] randchange = new IAction[0];
	public static IAction[] randAllchange = new IAction[0];
	public static IAction[] loopAllRandom = new IAction[0];
	public static IAction[] loop = new IAction[0];
	public static IAction[] loopAll = new IAction[0];
	public static IAction[] deactive = new IAction[2];
	public static IAction[] colorBlocks = new IAction[16];
	public static IAction[] colorAllBlocks = new IAction[16];
	
	public static void loadBC()
	{
		for (int i = 0; i < 16; i++)
		{
			addFacade(new ItemStack(APIBlocks.hempBlock, 1, i));
			addFacade(new ItemStack(APIBlocks.hempBrick, 1, i));
			addFacade(new ItemStack(APIBlocks.hempBlockPlated, 1, i));
			addFacade(new ItemStack(APIBlocks.hempBrickPlated, 1, i));
			addFacade(new ItemStack(APIBlocks.savedHempBlock, 1, i));
			addFacade(new ItemStack(APIBlocks.savedHempBrick, 1, i));
			addFacade(new ItemStack(APIBlocks.savedHempBlockPlated, 1, i));
			addFacade(new ItemStack(APIBlocks.savedHempBrickPlated, 1, i));
		}
		
		int ticks = SpmodConfig.ticks.length;
		
		loopRandom = new IAction[ticks];
		randchange = new IAction[ticks];
		randAllchange = new IAction[ticks];
		loopAllRandom = new IAction[ticks];
		loop = new IAction[ticks];
		loopAll = new IAction[ticks];
		StatementManager.registerStatement(deactive[0] = new ActionChange(false));
		StatementManager.registerStatement(deactive[1] = new ActionChange(true));
		
		for(int i = 0;i<16;i++)
		{
			StatementManager.registerStatement(colorBlocks[i] = new ColorChangeAdv(EnumColor.values()[i], false));
			StatementManager.registerStatement(colorAllBlocks[i] = new ColorChangeAdv(EnumColor.values()[i], true));
		}
		
		for(int i = 0;i<ticks;i++)
		{
			int tick = SpmodConfig.ticks[i];
			StatementManager.registerStatement(loopRandom[i] = new ActionRandomLoop(false, tick));
			StatementManager.registerStatement(randchange[i] = new ActionRandomLoop(true, tick));
			StatementManager.registerStatement(loopAllRandom[i] = new ActionRandomLoop(false, tick).setAll());
			StatementManager.registerStatement(randAllchange[i] = new ActionRandomLoop(true, tick).setAll());
			StatementManager.registerStatement(loop[i] = new ActionAdvLoop(false, tick));
			StatementManager.registerStatement(loopAll[i] = new ActionAdvLoop(true, tick));
		}
		
		StatementManager.registerActionProvider(new SpmodActionHelper());
	}

	private static void addFacade(ItemStack facadeStack)
	{
		FMLInterModComms.sendMessage("BuildCraft|Transport", "add-facade", facadeStack);
	}
}
