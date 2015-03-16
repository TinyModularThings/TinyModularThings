package speiger.src.tinymodularthings.common.plugins.Waila.logics;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import speiger.src.api.common.utils.MathUtils;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.tinymodularthings.common.blocks.machine.IC2CropFarm;
import speiger.src.tinymodularthings.common.blocks.machine.PressureFurnace;
import speiger.src.tinymodularthings.common.blocks.machine.WaterGenerator;

public class MachineBlockLogic implements IWailaDataProvider
{
	
	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return null;
	}
	
	@Override
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return currenttip;
	}
	
	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		TileEntity tile = accessor.getTileEntity();
		if(tile != null && tile instanceof AdvTile)
		{
			int metadata = accessor.getMetadata();
			if(metadata == 0)
			{
				PressureFurnace furnace = (PressureFurnace)tile;
				int maxHeat = (int)(((double)furnace.heat / (double)furnace.MaxHeat) * 100);
				currenttip.add("Heat Level: "+maxHeat);
				currenttip.add("Fuel last Maximum: "+MathUtils.getTicksInTimeShort(furnace.getTotalFuel()));
			}
			else if(metadata == 3)
			{
				WaterGenerator water = (WaterGenerator)tile;
				currenttip.add("Stored Energy: "+water.getEnergyProvider(null).getStoredEnergy()+" MJ");
				currenttip.add("Stored Water: "+water.tank.getFluidAmount()+"mB");
			}
			else if(metadata == 6)
			{
				IC2CropFarm farm = (IC2CropFarm)tile;
				currenttip.add("Fails: "+farm.fails+" / 4");
				if(farm.fails == 4)
				{
					currenttip.add("Next Fail will distroy Crops!");
				}
			}
		}
		return currenttip;
	}
	
	@Override
	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return currenttip;
	}
	
}
