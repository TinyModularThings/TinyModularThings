package speiger.src.api.common.world.tiles.interfaces.wirelessredstone;

public interface IWirelessObject
{
	/**
	 * Filler Class just to have multible things in one and do not have to care about TileEntities.
	 * This system is not caring about TileOnly also allows FakeTileEntities to work with.
	 */
	public int getFrequency(WirelessMode par1);
	
	public boolean isGlobal();
	
	public static enum WirelessMode
	{
		Receiver,
		Source;
	}
	
}
