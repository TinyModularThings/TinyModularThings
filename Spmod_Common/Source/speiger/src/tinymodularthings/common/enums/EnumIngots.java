package speiger.src.tinymodularthings.common.enums;

public enum EnumIngots
{
	Copper(0), Tin(1), Aluminum(2), Silver(3), Lead(4), Bronze(5), Iridium(6);
	
	int meta;
	
	private EnumIngots(int meta)
	{
		this.meta = meta;
	}
	
	public int getIngotMeta()
	{
		return meta;
	}
}
