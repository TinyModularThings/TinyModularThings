package speiger.src.spmodapi.common.plugins.BC.actions;

import java.util.Arrays;
import java.util.LinkedList;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import speiger.src.spmodapi.common.blocks.deko.TileLamp;
import speiger.src.spmodapi.common.enums.EnumColor;
import speiger.src.spmodapi.common.plugins.BC.core.BCAddon;
import buildcraft.BuildCraftCore;
import buildcraft.api.gates.IAction;
import buildcraft.api.gates.IActionProvider;

public class SpmodActionHelper implements IActionProvider
{
	
	@Override
	public LinkedList<IAction> getNeighborActions(Block block, TileEntity tile)
	{
		LinkedList<IAction> actions = new LinkedList<IAction>();
		
		if (tile != null)
		{
			if (tile instanceof TileLamp)
			{
				TileLamp lamp = (TileLamp) tile;
				try
				{
					actions.add(BuildCraftCore.actionOn);
					actions.add(BuildCraftCore.actionOff);
					actions.addAll(Arrays.asList(ActionLoader.deactive));
					if (lamp.isAllColored)
					{
						actions.addAll(Arrays.asList(ActionLoader.colorBlocks));
						actions.addAll(Arrays.asList(ActionLoader.colorAllBlocks));
						actions.addAll(Arrays.asList(ActionLoader.loopRandom));
						actions.addAll(Arrays.asList(ActionLoader.randchange));
						actions.addAll(Arrays.asList(ActionLoader.randAllchange));
						actions.addAll(Arrays.asList(ActionLoader.loopAllRandom));
						actions.addAll(Arrays.asList(ActionLoader.loop));
						actions.addAll(Arrays.asList(ActionLoader.loopAll));
					}
					else if (lamp.isNoneColored)
					{
						for (EnumColor color : lamp.installedColor)
						{
							actions.add(ActionLoader.colorBlocks[color.ordinal()]);
							actions.add(ActionLoader.colorAllBlocks[color.ordinal()]);
						}
						actions.addAll(Arrays.asList(ActionLoader.loopRandom));
						actions.addAll(Arrays.asList(ActionLoader.randchange));
						actions.addAll(Arrays.asList(ActionLoader.randAllchange));
						actions.addAll(Arrays.asList(ActionLoader.loopAllRandom));
						actions.addAll(Arrays.asList(ActionLoader.loop));
						actions.addAll(Arrays.asList(ActionLoader.loopAll));
					}
				}
				catch (Exception e)
				{
				}
			}
		}
		
		return actions;
	}
	
}
