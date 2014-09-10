package speiger.src.tinymodularthings.common.enums;

public enum HopperUpgradeIDs
{
	Filter(0, "upgrade.basic.filter");
	
	int id;
	String className;
	
	private HopperUpgradeIDs(int par1, String par2)
	{
		id = par1;
		className = par2;
	}
	
	public int getGuiID()
	{
		return EnumIDs.HopperUpgrades.getId()+id;
	}

	public String getUpgradeClass()
	{
		return className;
	}
}
