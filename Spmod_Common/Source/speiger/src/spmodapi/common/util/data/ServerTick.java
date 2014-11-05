package speiger.src.spmodapi.common.util.data;

import java.util.EnumSet;

import speiger.src.api.common.registry.helpers.Ticks;
import speiger.src.spmodapi.SpmodAPI;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;

public class ServerTick implements ITickHandler
{
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
		
	}
	
	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{
		Ticks.tick(SpmodAPI.instance, Side.SERVER);
	}
	
	@Override
	public EnumSet<TickType> ticks()
	{
		return EnumSet.of(TickType.SERVER);
	}
	
	@Override
	public String getLabel()
	{
		return "SpmodAPI Server Tick";
	}
	
}
