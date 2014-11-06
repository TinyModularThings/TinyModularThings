package speiger.src.spmodapi.common.plugins.BC;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
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
	
	
	public boolean active()
	{
		return on;
	}

	@Override
	public String getDescription()
	{
		if(on)
		{
			return "Turn all Blocks on";
		}
		return "Turn all Blocks off";
	}
	
}
