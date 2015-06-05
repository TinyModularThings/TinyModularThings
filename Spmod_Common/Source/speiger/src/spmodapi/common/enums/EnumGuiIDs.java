package speiger.src.spmodapi.common.enums;

public enum EnumGuiIDs
{
	BlockGui(10),
	Tiles(11),
	Commands(12),
	Items(13),
	Pipes(14),
	Entities(15),
	Sound(16);
	
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
