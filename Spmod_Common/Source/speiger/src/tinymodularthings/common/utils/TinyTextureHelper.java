package speiger.src.tinymodularthings.common.utils;

import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;

public class TinyTextureHelper
{
	public static String getTextureStringFromName(String par1)
	{
		return TinyModularThingsLib.ModID.toLowerCase() + ":" + par1;
	}
}
