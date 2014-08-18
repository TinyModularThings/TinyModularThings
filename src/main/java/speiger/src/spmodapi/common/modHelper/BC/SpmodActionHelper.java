package speiger.src.spmodapi.common.modHelper.BC;

import java.util.Arrays;
import java.util.LinkedList;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import speiger.src.spmodapi.common.blocks.deko.TileLamp;
import speiger.src.spmodapi.common.enums.EnumColor;
import buildcraft.BuildCraftCore;
import buildcraft.api.gates.IAction;
import buildcraft.api.gates.IActionProvider;

public class SpmodActionHelper implements IActionProvider
{
	
	@Override
	public LinkedList<IAction> getNeighborActions(Block block, TileEntity tile)
	{
		LinkedList<IAction> actions = new LinkedList<IAction>();
		
		if(tile != null)
		{
			if(tile instanceof TileLamp)
			{
				TileLamp lamp = (TileLamp) tile;
				try
				{
					actions.add(BuildCraftCore.actionOn);
					actions.add(BuildCraftCore.actionOff);
					actions.addAll(Arrays.asList(BCAddon.deactive));
					if(lamp.allColored)
					{
						actions.addAll(Arrays.asList(BCAddon.colorBlocks));
						actions.addAll(Arrays.asList(BCAddon.colorAllBlocks));
						actions.addAll(Arrays.asList(BCAddon.loopRandom));
						actions.addAll(Arrays.asList(BCAddon.randchange));
						actions.addAll(Arrays.asList(BCAddon.randAllchange));
						actions.addAll(Arrays.asList(BCAddon.loopAllRandom));
						actions.addAll(Arrays.asList(BCAddon.loop));
						actions.addAll(Arrays.asList(BCAddon.loopAll));
					}
					else if(lamp.noneColored)
					{
						for(EnumColor color : lamp.validColors)
						{
							actions.add(BCAddon.colorBlocks[color.ordinal()]);
							actions.add(BCAddon.colorAllBlocks[color.ordinal()]);
						}
						actions.addAll(Arrays.asList(BCAddon.loopRandom));
						actions.addAll(Arrays.asList(BCAddon.randchange));
						actions.addAll(Arrays.asList(BCAddon.randAllchange));
						actions.addAll(Arrays.asList(BCAddon.loopAllRandom));
						actions.addAll(Arrays.asList(BCAddon.loop));
						actions.addAll(Arrays.asList(BCAddon.loopAll));					
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
