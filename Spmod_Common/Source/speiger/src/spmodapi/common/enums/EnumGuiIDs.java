package speiger.src.spmodapi.common.enums;

public enum EnumGuiIDs
{
	WorkBench(10), Tiles(11), Commands(12), Items(13);
	
	int id;
	
	private EnumGuiIDs(int par1)
	{
		id = par1;
	}
	
	public int getID()
	{
		return id;
	}
}
