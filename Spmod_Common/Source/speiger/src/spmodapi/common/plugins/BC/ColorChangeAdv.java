package speiger.src.spmodapi.common.plugins.BC;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
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
	public String getUniqueTag()
	{
		if (adv)
		{
			return "color.blocks.all." + color.ordinal();
		}
		return "color.blocks." + color.ordinal();
	}
	
	@Override
	public int getLegacyId()
	{
		return 0;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon()
	{
		return BuildCraftTransport.actionPipeColor[color.ordinal()].getIcon();
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
	
}
