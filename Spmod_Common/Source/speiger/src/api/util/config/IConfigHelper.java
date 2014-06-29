package speiger.src.api.util.config;

import net.minecraftforge.common.Configuration;

public interface IConfigHelper
{
	public String getWorldGenCategorie();
	
	public String getRetrogenCategorie();
	
	public Configuration getConfiguration();
	
	public ConfigBoolean getConfigBoolean(String categorie, String name, boolean defaults);
}
