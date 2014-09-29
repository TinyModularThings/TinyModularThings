package speiger.src.tinymodularthings.common.enums;

import cpw.mods.fml.client.registry.RenderingRegistry;

public enum EnumIDs
{
	
	Pipe(EnumType.Render, RenderingRegistry.getNextAvailableRenderId()),
	StorageBlock(EnumType.Render, RenderingRegistry.getNextAvailableRenderId()),
	LogicBlock(EnumType.Render, RenderingRegistry.getNextAvailableRenderId()),
	TransportBlock(EnumType.Render, RenderingRegistry.getNextAvailableRenderId()),
	ADVTiles(EnumType.Gui, 10),
	Entities(EnumType.Gui, 11),
	Items(EnumType.Gui, 12),
	BCPipes(EnumType.Gui, 13),
	HopperUpgrades(EnumType.Gui, 150, 180);
	
	EnumType type;
	int id;
	int maxID;
	
	private EnumIDs(EnumType par1, int par2)
	{
		type = par1;
		id = par2;
	}
	
	private EnumIDs(EnumType par1, int par2, int par3)
	{
		type = par1;
		id = par2;
		maxID = par3;
	}
	
	public int getMaxID()
	{
		return maxID;
	}
	
	public boolean isInRange(int range)
	{
		return range >= id && range <= maxID;
	}
	
	public EnumType getType()
	{
		return type;
	}
	
	public int getId()
	{
		return id;
	}
	
	public static enum EnumType
	{
		Gui,
		Render;
	}
}
