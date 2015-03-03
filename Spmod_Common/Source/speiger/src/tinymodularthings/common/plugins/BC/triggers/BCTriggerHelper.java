package speiger.src.tinymodularthings.common.plugins.BC.triggers;

import ic2.core.block.generator.tileentity.TileEntityWindGenerator;

import java.util.LinkedList;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fluids.IFluidHandler;
import speiger.src.tinymodularthings.common.pipes.PipeEmeraldExtractionPower;
import speiger.src.tinymodularthings.common.plugins.BC.core.BCVariables;
import buildcraft.api.gates.ITrigger;
import buildcraft.api.gates.ITriggerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.transport.IPipe;

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
			
			if (tile instanceof TileEntityBrewingStand)
			{
				triggers.add(BCVariables.hasWork);
				triggers.add(BCVariables.noWork);
			}
			
			if(tile instanceof IFluidHandler)
			{
				triggers.add(BCVariables.storedFluid[0]);
				triggers.add(BCVariables.storedFluid[1]);
				triggers.add(BCVariables.storedFluid[2]);
			}
			
			try
			{
				if (tile instanceof IPowerReceptor)
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
		LinkedList<ITrigger> trigger = new LinkedList<ITrigger>();
		if(pipe != null)
		{
			if(pipe instanceof PipeEmeraldExtractionPower)
			{
				trigger.add(BCVariables.pipeRequestPower);
			}
		}
		
		return trigger;
	}
}
