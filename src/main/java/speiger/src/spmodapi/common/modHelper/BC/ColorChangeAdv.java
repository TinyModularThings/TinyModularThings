package speiger.src.spmodapi.common.modHelper.BC;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import speiger.src.api.items.InfoStack;
import speiger.src.api.language.LanguageRegister;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.enums.EnumColor;
import buildcraft.BuildCraftTransport;
import buildcraft.api.gates.IAction;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class ColorChangeAdv implements IAction
{
	public boolean adv;
	public EnumColor color;
	
	public ColorChangeAdv(EnumColor color, boolean all)
	{
		adv = all;
		this.color = color;
	}

	@Override
	public String getDescription()
	{
		if(adv)
		{
			return LanguageRegister.getLanguageName(new InfoStack(), "color.blocks.all", SpmodAPI.instance);
		}
		return LanguageRegister.getLanguageName(new InfoStack(), "color.blocks", SpmodAPI.instance);
	}

	@Override
	public String getUniqueTag()
	{
		if(adv)
		{
			return "color.blocks.all."+color.ordinal();
		}
		return "color.blocks."+color.ordinal();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon()
	{
		return BuildCraftTransport.actionPipeColor[color.ordinal()].getIcon();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister)
	{
		
	}


	@Override
	public IAction rotateLeft()
	{
		return this;
	}

	@Override
	public boolean hasParameter()
	{
		return false;
	}
}
