package speiger.src.spmodapi.common.modHelper.BC;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import speiger.src.api.items.InfoStack;
import speiger.src.api.language.LanguageRegister;
import speiger.src.spmodapi.SpmodAPI;
import buildcraft.BuildCraftCore;
import buildcraft.api.gates.IAction;
import buildcraft.api.gates.IActionParameter;
import buildcraft.api.gates.IGate;
import buildcraft.api.gates.IStatementParameter;
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
	public String getUniqueTag()
	{
		if(!on)
		{
			return "AdvOff";
		}
		return "AdvOn";
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon()
	{
		if(on)
		{
			return BuildCraftCore.actionOn.getIcon();
		}
		return BuildCraftCore.actionOff.getIcon();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister)
	{		
	}
	
	
	@Override
	public String getDescription()
	{
		if(on)
		{
			return LanguageRegister.getLanguageName(new InfoStack(), "gate.state.on", SpmodAPI.instance);
		}
		return LanguageRegister.getLanguageName(new InfoStack(), "gate.state.off", SpmodAPI.instance);
	}


	public boolean active()
	{
		return on;
	}

	@Override
	public IAction rotateLeft()
	{
		return this;
	}

	@Override
	public int maxParameters() 
	{
		return 0;
	}

	@Override
	public int minParameters() 
	{
		return 0;
	}

	@Override
	public IStatementParameter createParameter(int index)
	{
		return null;
	}

	@Override
	public void actionActivate(IGate gate, IActionParameter[] parameters) 
	{
		
	}
}
