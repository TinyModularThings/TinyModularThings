package speiger.src.api.common.utils.config;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

public class ConfigBoolean
{
	private String Categorie;
	private String Name;
	private boolean Default;
	
	private String comment;
	
	public ConfigBoolean(String catogrie, String name, boolean defaults)
	{
		Categorie = catogrie;
		Name = name;
		Default = defaults;
	}
	
	public ConfigBoolean setComment(String par1)
	{
		comment = par1;
		return this;
	}
	
	public boolean getResult(Configuration par1)
	{
		if (comment != null && comment.length() > 0)
		{
			return Boolean.parseBoolean(par1.get(Categorie, Name, Default, comment).getString());
		}
		
		return Boolean.parseBoolean(par1.get(Categorie, Name, Default).getString());
		
	}
	
	public void changeResult(Configuration par1, boolean newResult)
	{
		Property prop;
		if (comment != null && comment.length() > 0)
		{
			prop = par1.get(Categorie, Name, Default, comment);
		}
		else
		{
			prop = par1.get(Categorie, Name, Default);
		}
		if(prop != null)
		{
			prop.set(newResult);
		}
	}
	
}
