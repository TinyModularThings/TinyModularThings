package speiger.src.ic2Fixes.common.core;

import ic2.core.IC2;

import java.util.EnumSet;

import net.minecraft.world.World;
import speiger.src.ic2Fixes.common.energy.GlobalEnergyNet;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class TickHandler implements ITickHandler
{

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
		World world = (World)tickData[0];
		IC2.platform.profilerStartSection("EnergyNet");
		GlobalEnergyNet.tickStart(world);
		IC2.platform.profilerEndSection();
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{
		World world = (World)tickData[0];
		IC2.platform.profilerStartSection("EnergyNet");
		GlobalEnergyNet.tickEnd(world);
		IC2.platform.profilerEndSection();
	}

	@Override
	public EnumSet<TickType> ticks()
	{
		return EnumSet.of(TickType.WORLD);
	}

	@Override
	public String getLabel()
	{
		return "IC2EnergyOverride";
	}
	
}
