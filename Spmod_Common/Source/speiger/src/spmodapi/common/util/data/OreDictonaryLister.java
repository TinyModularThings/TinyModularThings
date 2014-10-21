package speiger.src.spmodapi.common.util.data;

import cpw.mods.fml.common.FMLLog;

public class OreDictonaryLister
{
	String[] blackList = new String[0];
	String[] whiteList = new String[0];
	
	public OreDictonaryLister addWhiteList(String...par1)
	{
		whiteList = par1;
		if(par1.length == 1 && par1[0].length() == 0)
		{
			whiteList = new String[0];
		}
		return this;
	}
	
	public OreDictonaryLister addBlackList(String...par1)
	{
		blackList = par1;
		if(par1.length == 1 && par1[0].length() == 0)
		{
			blackList = new String[0];
		}
		return this;
	}
	
	
	public boolean isAllowed(String par1)
	{
		if(whiteList.length > 0)
		{
			for(String key : whiteList)
			{
				if(key.equals(par1))
				{
					return true;
				}
			}
			return false;
		}
		else
		{
			if(blackList.length > 0)
			{
				for(String key : blackList)
				{
					if(key.equals(par1))
					{
						return false;
					}
				}
			}
			return true;
		}
	}
}
