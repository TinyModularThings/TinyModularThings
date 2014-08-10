package speiger.src.tinymodularthings.common.plugins.BC.actions;

import javax.swing.Icon;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import speiger.src.api.items.InfoStack;
import speiger.src.api.language.LanguageRegister;
import speiger.src.tinymodularthings.TinyModularThings;
import buildcraft.api.gates.IAction;
import buildcraft.api.gates.IActionParameter;
import buildcraft.api.gates.IGate;
import buildcraft.api.gates.IStatement;
import buildcraft.api.gates.IStatementParameter;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BucketFillerAction implements IAction
{
	
	public boolean fill;
	
	public BucketFillerAction(boolean par1)
	{
		fill = par1;
	}
	
	
	@Override
	public String getUniqueTag()
	{
		return fill ? "fill.something" : "drain.something";
	}
	
	IIcon tex; 
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon()
	{
		return tex;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister)
	{
		tex = iconRegister.registerIcon("buildcraft:triggers/guitriggers_3_4");
	}
	
	@Override
	public String getDescription()
	{
		return fill ? LanguageRegister.getLanguageName(new InfoStack(), "bucketFiller.fill", TinyModularThings.instance) : LanguageRegister.getLanguageName(new InfoStack(), "bucketFiller.drain", TinyModularThings.instance);
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
