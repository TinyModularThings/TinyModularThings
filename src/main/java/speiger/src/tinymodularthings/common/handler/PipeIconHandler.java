package speiger.src.tinymodularthings.common.handler;

import javax.swing.Icon;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
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
		IIcon icon;
		
		private PipeTypes(String par1)
		{
			name = par1;
		}
		
		public IIcon getIcon()
		{
			return icon;
		}
		
		public void registerIcon(IIconRegister par1)
		{
			icon = par1.registerIcon(name);
		}
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int iconIndex)
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
	public void registerIcons(IIconRegister iconRegister)
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
