package speiger.src.tinymodularthings.common.plugins.BC.actions;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import speiger.src.api.items.InfoStack;
import speiger.src.api.language.LanguageRegister;
import speiger.src.tinymodularthings.TinyModularThings;
import buildcraft.BuildCraftTransport;
import buildcraft.api.gates.IAction;
import buildcraft.api.gates.IActionParameter;
import buildcraft.api.gates.IGate;
import buildcraft.api.gates.IStatement;
import buildcraft.api.gates.IStatementParameter;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ActionOneSlotChange implements IAction
{
	public boolean plus;
	
	public ActionOneSlotChange(boolean plus)
	{
		this.plus = plus;
	}
	
	
	@Override
	public String getUniqueTag()
	{
		return plus ? "change.one.plus" : "change.one.minus";
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon()
	{
		return BuildCraftTransport.actionEnergyPulser.getIcon();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister)
	{
	}
	
	
	@Override
	public String getDescription()
	{
		return plus ? LanguageRegister.getLanguageName(new InfoStack(), "gate.change.plus", TinyModularThings.instance) : LanguageRegister.getLanguageName(new InfoStack(), "gate.change.minus", TinyModularThings.instance);
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
	public IStatement rotateLeft()
	{
		return null;
	}


	@Override
	public void actionActivate(IGate gate, IActionParameter[] parameters)
	{
		
	}
	
}
