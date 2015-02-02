package speiger.src.spmodapi.common.plugins.BC.actions;

import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.enums.EnumColor;
import buildcraft.api.gates.ActionManager;
import buildcraft.api.gates.IAction;

public class ActionLoader
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
	
	public static void init()
	{
		int ticks = SpmodConfig.ticks.length;
		
		loopRandom = new IAction[ticks];
		randchange = new IAction[ticks];
		randAllchange = new IAction[ticks];
		loopAllRandom = new IAction[ticks];
		loop = new IAction[ticks];
		loopAll = new IAction[ticks];
		ActionManager.registerAction(deactive[0] = new ActionChange(false));
		ActionManager.registerAction(deactive[1] = new ActionChange(true));
		
		for (int i = 0; i < 16; i++)
		{
			ActionManager.registerAction(colorBlocks[i] = new ActionColorChangeAdv(EnumColor.values()[i], false));
			ActionManager.registerAction(colorAllBlocks[i] = new ActionColorChangeAdv(EnumColor.values()[i], true));
		}
		
		for (int i = 0; i < ticks; i++)
		{
			int tick = SpmodConfig.ticks[i];
			ActionManager.registerAction(loopRandom[i] = new ActionRandomLoop(false, tick));
			ActionManager.registerAction(randchange[i] = new ActionRandomLoop(true, tick));
			ActionManager.registerAction(loopAllRandom[i] = new ActionRandomLoop(false, tick).setAll());
			ActionManager.registerAction(randAllchange[i] = new ActionRandomLoop(true, tick).setAll());
			ActionManager.registerAction(loop[i] = new ActionAdvLoop(false, tick));
			ActionManager.registerAction(loopAll[i] = new ActionAdvLoop(true, tick));
		}
		
		ActionManager.registerActionProvider(new SpmodActionHelper());
	}
}
