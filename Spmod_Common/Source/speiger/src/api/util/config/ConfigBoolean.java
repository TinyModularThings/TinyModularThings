package speiger.src.api.util.config;

import net.minecraftforge.common.Configuration;

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
	
}
