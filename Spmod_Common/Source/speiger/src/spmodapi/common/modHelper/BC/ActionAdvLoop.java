package speiger.src.spmodapi.common.modHelper.BC;

import speiger.src.api.items.InfoStack;
import speiger.src.api.language.LanguageRegister;
import speiger.src.spmodapi.SpmodAPI;
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
		uqine+="."+tickRate;
		if(all)
		{
			uqine+=".all";
		}
		return uqine;
	}
	@Override
	public String getDescription()
	{
		if(all)
		{
			return LanguageRegister.getLanguageName(new InfoStack(), "loop.all", SpmodAPI.instance)+": "+tickRate+" Ticks";
		}
		return LanguageRegister.getLanguageName(new InfoStack(), "loop", SpmodAPI.instance)+": "+tickRate+" Ticks";
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
