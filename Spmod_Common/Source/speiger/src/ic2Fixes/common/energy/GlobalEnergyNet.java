package speiger.src.ic2Fixes.common.energy;

import ic2.api.energy.EnergyNet;
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
import speiger.src.ic2Fixes.common.core.TickHandler;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class GlobalEnergyNet implements IEnergyNet
{
	public static HashMap<Integer, LocalEnergyNet> energyNets = new HashMap<Integer, LocalEnergyNet>();
	private static boolean isDebugMode = false;
	private static boolean replaceConflicting = false;
	
	public GlobalEnergyNet()
	{
		TickRegistry.registerTickHandler(new TickHandler(), Side.SERVER);
		EventHandler evt = new EventHandler();
		MinecraftForge.EVENT_BUS.unregister(evt);
		MinecraftForge.EVENT_BUS.register(this);
		EnergyNet.instance = this;
	}
	
	@Override
	public TileEntity getTileEntity(World world, int x, int y, int z)
	{
		return getEnergyNet(world).getTileEntity(x, y, z);
	}
	
	@Override
	public TileEntity getNeighbor(TileEntity te, ForgeDirection dir)
	{
		return getEnergyNet(te.getWorldObj()).getNeighbor(te, dir);
	}
	
	@Override
	public long getTotalEnergyEmitted(TileEntity tileEntity)
	{
		return getEnergyNet(tileEntity.getWorldObj()).getTotalEnergyEmitted(tileEntity);
	}
	
	@Override
	public long getTotalEnergySunken(TileEntity tileEntity)
	{
		return getEnergyNet(tileEntity.getWorldObj()).getTotalSunkenEnergy(tileEntity);
	}
	
	static LocalEnergyNet getEnergyNet(World world)
	{
		int id = world.provider.dimensionId;
		LocalEnergyNet net = energyNets.get(Integer.valueOf(id));
		if(net == null)
		{
			net = new LocalEnergyNet(world);
			energyNets.put(Integer.valueOf(id), net);
		}
		return net;
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
		if(!IC2.platform.isSimulating() || evt.world.isRemote)
		{
			return;
		}
		getEnergyNet(evt.world).addTileEntity((TileEntity)evt.energyTile);
	}
	
	@ForgeSubscribe
	public void onEnergyTileUnload(EnergyTileUnloadEvent evt)
	{
		if(!IC2.platform.isSimulating() || evt.world.isRemote)
		{
			return;
		}
		getEnergyNet(evt.world).removeTileEntity((TileEntity)evt.energyTile);
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

	public static void tickEnd(World world)
	{
		LocalEnergyNet net = getEnergyNet(world);
		if(net != null)
		{
			net.onTickEnd();
		}
	}
	
	public static void tickStart(World world)
	{
		LocalEnergyNet net = getEnergyNet(world);
		if(net != null)
		{
			net.onTickStart();
		}
	}
	
}
