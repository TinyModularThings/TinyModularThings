package speiger.src.tinymodularthings.common.items.core;

import speiger.src.api.common.registry.helpers.SpmodMod;
import speiger.src.spmodapi.common.items.SpmodItem;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;

public abstract class TinyItem extends SpmodItem
{
	
	public TinyItem(int par1)
	{
		super(par1);
	}
	
	public SpmodMod getMod()
	{
		return TinyModularThings.instance;
	}
	
	public String getModID()
	{
		return TinyModularThingsLib.ModID.toLowerCase();
	}
	
}
