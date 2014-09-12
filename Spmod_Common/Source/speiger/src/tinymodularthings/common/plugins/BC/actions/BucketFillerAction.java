package speiger.src.tinymodularthings.common.plugins.BC.actions;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import speiger.src.api.items.InfoStack;
import speiger.src.api.language.LanguageRegister;
import speiger.src.tinymodularthings.TinyModularThings;
import buildcraft.api.gates.IAction;
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
	public int getLegacyId()
	{
		return 0;
	}
	
	@Override
	public String getUniqueTag()
	{
		return fill ? "fill.something" : "drain.something";
	}
	
	Icon tex;
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon()
	{
		return tex;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		tex = iconRegister.registerIcon("buildcraft:triggers/guitriggers_3_4");
	}
	
	@Override
	public boolean hasParameter()
	{
		return false;
	}
	
	@Override
	public String getDescription()
	{
		return fill ? LanguageRegister.getLanguageName(new InfoStack(), "bucketFiller.fill", TinyModularThings.instance) : LanguageRegister.getLanguageName(new InfoStack(), "bucketFiller.drain", TinyModularThings.instance);
	}
	
}
