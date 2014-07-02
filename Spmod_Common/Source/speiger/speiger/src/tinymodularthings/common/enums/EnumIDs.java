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
	HopperUpgrades(EnumType.Gui, 13);
	
	EnumType type;
	int id;
	
	private EnumIDs(EnumType par1, int par2)
	{
		type = par1;
		id = par2;
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
		Gui, Render;
	}
}
