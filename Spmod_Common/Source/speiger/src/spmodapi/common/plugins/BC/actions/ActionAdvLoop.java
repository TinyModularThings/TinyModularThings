package speiger.src.spmodapi.common.plugins.BC.actions;

import buildcraft.core.triggers.ActionMachineControl;

public class ActionAdvLoop extends ActionMachineControl
{
	boolean all;
	int tickRate;
	
	public ActionAdvLoop(boolean all, int tickRate)
	{
		super(0, Mode.Loop);
		this.all = all;
		this.tickRate = tickRate;
	}
	
	@Override
	public String getUniqueTag()
	{
		String uqine = super.getUniqueTag();
		uqine += "." + tickRate;
		if (all)
		{
			uqine += ".all";
		}
		return uqine;
	}
	
	public boolean allBlocks()
	{
		return all;
	}
	
	public int getTickRate()
	{
		return tickRate;
	}
	
}
