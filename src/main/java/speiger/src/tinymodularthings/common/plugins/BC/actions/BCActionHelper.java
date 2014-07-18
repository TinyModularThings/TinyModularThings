package speiger.src.tinymodularthings.common.plugins.BC.actions;

import java.util.Arrays;
import java.util.LinkedList;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.blocks.machine.BucketFillerBasic;
import speiger.src.tinymodularthings.common.blocks.machine.PressureFurnace;
import speiger.src.tinymodularthings.common.blocks.transport.MultiStructureItemInterface;
import speiger.src.tinymodularthings.common.plugins.BC.BCVariables;
import speiger.src.tinymodularthings.common.plugins.BC.TileEntityModifiedFurnace;
import buildcraft.BuildCraftCore;
import buildcraft.api.gates.IAction;
import buildcraft.api.gates.IActionProvider;

public class BCActionHelper implements IActionProvider
{
	private static BCVariables bc;
	
	@Override
	public LinkedList<IAction> getNeighborActions(Block block, TileEntity tile)
	{
		LinkedList<IAction> list = new LinkedList<IAction>();
		if (tile != null)
		{
			if (tile instanceof TileEntityModifiedFurnace || tile instanceof PressureFurnace)
			{
				
				try
				{
					list.add(BuildCraftCore.actionOff);
					list.add(BuildCraftCore.actionOn);
				}
				catch (Exception e)
				{
					TinyModularThings.log.print("Error with intigrate the Furnace");
				}
			}
			if (tile instanceof MultiStructureItemInterface)
			{
				MultiStructureItemInterface multi = (MultiStructureItemInterface) tile;
				if (multi.hasTarget() && multi.target.getSizeInventory() > 1)
				{
					int size = multi.target.getSizeInventory();
					for (int i = 0; i < size; i++)
					{
						list.add(BCVariables.changeToSlot[i]);
					}
					list.add(BCVariables.changeOneTime[0]);
					list.add(BCVariables.changeOneTime[1]);
				}
			}
			if(tile instanceof BucketFillerBasic)
			{
				try
				{
					list.addAll(Arrays.asList(BCVariables.bcFiller));
				}
				catch (Exception e)
				{
					
				}
			}
		}
		
		return list;
	}
	
}
