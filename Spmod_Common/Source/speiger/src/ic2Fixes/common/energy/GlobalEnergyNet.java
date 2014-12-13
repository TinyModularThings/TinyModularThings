package speiger.src.ic2Fixes.common.energy;

import ic2.api.energy.IEnergyNet;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.core.IC2;
import ic2.core.energy.EventHandler;

import java.util.HashMap;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;

public class GlobalEnergyNet implements IEnergyNet
{
	public static HashMap<Integer, LocalEnergyNet> energyNets = new HashMap<Integer, LocalEnergyNet>();
	private static boolean isDebugMode = false;
	private static boolean replaceConflicting = false;
	
	public GlobalEnergyNet()
	{
		EventHandler evt = new EventHandler();
		MinecraftForge.EVENT_BUS.unregister(evt);
		MinecraftForge.EVENT_BUS.register(this);
		
	}
	
	@Override
	public TileEntity getTileEntity(World world, int x, int y, int z)
	{
		return energyNets.get(world.provider.dimensionId).getTileEntity(x, y, z);
	}
	
	@Override
	public TileEntity getNeighbor(TileEntity te, ForgeDirection dir)
	{
		return energyNets.get(te.getWorldObj().provider.dimensionId).getNeighbor(te, dir);
	}
	
	@Override
	public long getTotalEnergyEmitted(TileEntity tileEntity)
	{
		return energyNets.get(tileEntity.getWorldObj().provider.dimensionId).getTotalEnergyEmitted(tileEntity);
	}
	
	@Override
	public long getTotalEnergySunken(TileEntity tileEntity)
	{
		return energyNets.get(tileEntity.getWorldObj().provider.dimensionId).getTotalSunkenEnergy(tileEntity);
	}
	
	@Override
	public int getPowerFromTier(int tier)
	{
	    if (tier < 14) {
	        return 8 << tier * 2;
	      }
	      return (int)(8.0D * Math.pow(4.0D, tier));
	}
	
	@ForgeSubscribe
	public void onEnergyTileLoad(EnergyTileLoadEvent evt)
	{
		if(!IC2.platform.isSimulating())
		{
			return;
		}
		energyNets.get(evt.world.provider.dimensionId).addTileEntity((TileEntity)evt.energyTile);
	}
	
	@ForgeSubscribe
	public void onEnergyTileUnload(EnergyTileUnloadEvent evt)
	{
		if(!IC2.platform.isSimulating())
		{
			return;
		}
		energyNets.get(evt.world.provider.dimensionId).removeTileEntity((TileEntity)evt.energyTile);
	}
	
	protected static boolean isDebugMode()
	{
		return isDebugMode;
	}
	
	protected static boolean replaceConflicting()
	{
		return replaceConflicting;
	}
	
	public static int getTierFromPower(double power)
	{
	    if (power <= 0.0D) return 0;

	    return (int)Math.ceil(Math.log(power / 8.0D) / Math.log(4.0D));
	}
	
}
