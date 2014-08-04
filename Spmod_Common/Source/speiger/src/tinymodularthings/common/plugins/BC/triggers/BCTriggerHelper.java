package speiger.src.tinymodularthings.common.plugins.BC.triggers;

import java.util.LinkedList;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import speiger.src.tinymodularthings.common.plugins.BC.BCVariables;
import buildcraft.api.gates.ITrigger;
import buildcraft.api.gates.ITriggerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.transport.IPipe;
import forestry.factory.gadgets.MachineFermenter;

public class BCTriggerHelper implements ITriggerProvider
{
	private static BCVariables bc;
	
	@Override
	public LinkedList<ITrigger> getNeighborTriggers(Block block, TileEntity tile)
	{
		LinkedList<ITrigger> triggers = new LinkedList<ITrigger>();
		
		if (tile != null)
		{
			if (tile instanceof TileEntityFurnace)
			{
				triggers.add(BCVariables.noWork);
				triggers.add(BCVariables.hasWork);
				triggers.add(BCVariables.needFuel);
			}
			
			try
			{
				if(tile instanceof MachineFermenter)
				{
					triggers.add(BCVariables.canWork);
				}
				if(tile instanceof IPowerReceptor)
				{
					triggers.add(BCVariables.requestPower);
				}
			}
			catch (Exception e)
			{
			}
		}
		
		return triggers;
	}
	
	@Override
	public LinkedList<ITrigger> getPipeTriggers(IPipe pipe)
	{
		return null;
	}
}
