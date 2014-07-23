package speiger.src.api.util;

/**
 * 
 * @author Speiger
 * 
 */
public class LogProxy
{
	String mod;
	boolean log = true;
	
	public LogProxy(SpmodMod modID)
	{
		mod = "[" + modID.getName() + "] ";
	}
	
	public void disable()
	{
		log = false;
	}
	
	public void print(String par1)
	{
		if (!log)
		{
			return;
		}
		System.out.println(mod + par1);
	}
	
	public void print(Object par1)
	{
		if (!log)
		{
			return;
		}
		
		System.out.println(mod + par1);
	}
}
