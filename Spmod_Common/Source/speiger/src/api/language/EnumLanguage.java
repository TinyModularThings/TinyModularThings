package speiger.src.api.language;

/**
 * 
 * @author Speiger
 * 
 */
public enum EnumLanguage
{
	Deutsch("de_DE"), Englisch("en_US");
	
	private String languageName;
	
	private EnumLanguage(String name)
	{
		languageName = name;
	}
	
	public String getLanguage()
	{
		return languageName;
	}
}
