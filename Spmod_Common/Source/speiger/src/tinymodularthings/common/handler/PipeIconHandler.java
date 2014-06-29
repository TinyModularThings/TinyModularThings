package speiger.src.tinymodularthings.common.handler;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;
import buildcraft.api.core.IIconProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PipeIconHandler implements IIconProvider
{
	public enum PipeTypes
	{
		EmeraldPipe(modID()+":pipes/bc/EmeraldPowerPipe"),
		EmeraldExtractorPipe(modID()+":pipes/bc/EmeraldPowerPipeFull");
		String name;
		Icon icon;
		
		private PipeTypes(String par1)
		{
			name = par1;
		}
		
		public Icon getIcon()
		{
			return icon;
		}
		
		public void registerIcon(IconRegister par1)
		{
			icon = par1.registerIcon(name);
		}
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int iconIndex)
	{
		try
		{
			return PipeTypes.values()[iconIndex].getIcon();
		}
		catch (Exception e)
		{
			return null;
		}

	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		for(int i = 0;i<PipeTypes.values().length;i++)
		{
			PipeTypes.values()[i].registerIcon(iconRegister);
		}
	}
	
	private static String modID()
	{
		return TinyModularThingsLib.ModID;
	}
	
}
