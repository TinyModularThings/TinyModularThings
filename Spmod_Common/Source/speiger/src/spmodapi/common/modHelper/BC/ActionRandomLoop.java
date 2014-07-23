package speiger.src.spmodapi.common.modHelper.BC;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import speiger.src.api.items.InfoStack;
import speiger.src.api.language.LanguageRegister;
import speiger.src.spmodapi.SpmodAPI;
import buildcraft.BuildCraftCore;
import buildcraft.api.gates.IAction;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ActionRandomLoop implements IAction
{
	
	public boolean all = false;
	public boolean randOnly;
	public int delay;
	
	public ActionRandomLoop(boolean par1)
	{
		randOnly = par1;
		delay = 1;
	}
	
	public ActionRandomLoop setAll()
	{
		all = true;
		return this;
	}
	
	public ActionRandomLoop(boolean par1, int ticks)
	{
		randOnly = par1;
		delay = ticks;
	}
	
	public boolean allBlocks()
	{
		return all;
	}
	
	public int getDelay()
	{
		return delay;
	}
	
	public boolean randomOnly()
	{
		return randOnly;
	}
	
	@Override
	public int getLegacyId()
	{
		return 0;
	}
	
	@Override
	public String getUniqueTag()
	{
		String alls = all ? "all" : "";
		return randOnly ? "rand.rand"+delay+alls : "rand.roundRoubin"+delay+alls;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon()
	{
		return BuildCraftCore.actionLoop.getIcon();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
	}
	
	@Override
	public boolean hasParameter()
	{
		return false;
	}
	
	@Override
	public String getDescription()
	{
		if(randOnly)
		{
			return LanguageRegister.getLanguageName(new InfoStack(), "action.random.color.change", SpmodAPI.instance)+" "+delay+" Ticks";
		}
		else
		{
			return LanguageRegister.getLanguageName(new InfoStack(), "action.randRoubin.color.change", SpmodAPI.instance)+" "+delay+" Ticks";
		}
	}
	
}
