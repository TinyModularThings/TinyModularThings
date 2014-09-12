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

public class ActionChange implements IAction
{
	public boolean on;
	
	public ActionChange(boolean on)
	{
		this.on = on;
	}
	
	@Override
	public int getLegacyId()
	{
		return 0;
	}
	
	@Override
	public String getUniqueTag()
	{
		if (!on)
		{
			return "AdvOff";
		}
		return "AdvOn";
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon()
	{
		if (on)
		{
			return BuildCraftCore.actionOn.getIcon();
		}
		return BuildCraftCore.actionOff.getIcon();
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
		if (on)
		{
			return LanguageRegister.getLanguageName(new InfoStack(), "gate.state.on", SpmodAPI.instance);
		}
		return LanguageRegister.getLanguageName(new InfoStack(), "gate.state.off", SpmodAPI.instance);
	}
	
	public boolean active()
	{
		return on;
	}
	
}
