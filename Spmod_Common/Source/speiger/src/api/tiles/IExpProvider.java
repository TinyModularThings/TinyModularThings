package speiger.src.api.tiles;

public interface IExpProvider
{
	public int addExp(int amount);
	
	public int removeExp(int amount);
	
	public int getExpStored();
	
	public int getMaxStoredExp();
	
	public boolean absorbDeath();
}
