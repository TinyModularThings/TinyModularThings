package speiger.src.tinymodularthings.common.pipes;

import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import buildcraft.BuildCraftTransport;
import buildcraft.api.core.IIconProvider;
import buildcraft.api.mj.IBatteryIOObject;
import buildcraft.api.mj.IBatteryObject;
import buildcraft.api.mj.IOMode;
import buildcraft.api.mj.MjAPI;
import buildcraft.api.mj.MjBattery;
import buildcraft.api.power.IPowerEmitter;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;
import buildcraft.api.transport.IPipeTile;
import buildcraft.transport.IPipeTransportPowerHook;
import buildcraft.transport.Pipe;
import buildcraft.transport.PipeIconProvider;
import buildcraft.transport.PipeTransportPower;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PipeEmeraldExtractionPower extends Pipe<PipeTransportPower> implements IPowerReceptor, IPipeTransportPowerHook
{
	public final boolean[] powerSources = new boolean[6];

	protected int standardIconIndex = PipeIconProvider.TYPE.PipePowerWood_Standard.ordinal();
	protected int solidIconIndex = PipeIconProvider.TYPE.PipeAllWood_Solid.ordinal();

	@MjBattery(mode = IOMode.Receive, maxCapacity = Short.MAX_VALUE, maxReceivedPerCycle = 500, minimumConsumption = 0)
	private double mjStored = 0;
	private boolean full;

	private PowerHandler powerHandler;

	public PipeEmeraldExtractionPower(Item item) {
		super(new PipeTransportPower(), item);
		powerHandler = new PowerHandler(this, Type.PIPE);
		powerHandler.configurePowerPerdition(0, 0);
		transport.initFromPipe(getClass());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIconProvider getIconProvider() {
		return BuildCraftTransport.instance.pipeIconProvider;
	}

	@Override
	public int getIconIndex(ForgeDirection direction) {
		return standardIconIndex;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		if (container.getWorldObj().isRemote) {
			return;
		}

		int sources = 0;

		for (ForgeDirection o : ForgeDirection.VALID_DIRECTIONS) {
			if (!container.isPipeConnected(o)) {
				powerSources[o.ordinal()] = false;
				continue;
			}

			TileEntity tile = container.getTile(o);

			if (powerSources[o.ordinal()] = isPowerSource(tile, o)) {
				sources++;
			}
		}

		if (sources <= 0) {
			mjStored = mjStored > 5 ? mjStored - 5 : 0;
			return;
		}

		if (mjStored == 0) {
			return;
		}

		double energyToRemove;

		if (mjStored > 40) {
			energyToRemove = mjStored / 20 + 4;
		} else if (mjStored > 10) {
			energyToRemove = mjStored / 10;
		} else {
			energyToRemove = 1;
		}
		energyToRemove /= sources;

		for (ForgeDirection o : ForgeDirection.VALID_DIRECTIONS) {
			if (!powerSources[o.ordinal()]) {
				continue;
			}

			double energyUsable = mjStored > energyToRemove ? energyToRemove : mjStored;
			double energySent = transport.receiveEnergy(o, energyUsable);

			if (energySent > 0) {
				mjStored -= energySent;
			}
		}
	}

	public boolean requestsPower() {
		if (full) {
			boolean request = mjStored < 1500 / 2;

			if (request) {
				full = false;
			}

			return request;
		}

		full = mjStored >= 1500 - 10;

		return !full;
	}

	@Override
	public void writeToNBT(NBTTagCompound data) {
		super.writeToNBT(data);
		data.setDouble("mj", mjStored);

		for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++) {
			data.setBoolean("powerSources[" + i + "]", powerSources[i]);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound data) {
		super.readFromNBT(data);
		mjStored = data.getDouble("mj");

		for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++) {
			powerSources[i] = data.getBoolean("powerSources[" + i + "]");
		}
	}

	@Override
	public double receiveEnergy(ForgeDirection from, double val) {
		return -1;
	}

	@Override
	public double requestEnergy(ForgeDirection from, double amount) {
		if (container.getTile(from) instanceof IPipeTile) {
			return amount;
		} else {
			return 0;
		}
	}

	public boolean isPowerSource(TileEntity tile, ForgeDirection from) {
		if (tile instanceof IPowerEmitter && ((IPowerEmitter) tile).canEmitPowerFrom(from.getOpposite())) {
			return true;
		}
		IBatteryObject battery = MjAPI.getMjBattery(tile, MjAPI.DEFAULT_POWER_FRAMEWORK, from.getOpposite());
		return battery instanceof IBatteryIOObject && ((IBatteryIOObject) battery).canSend();
	}

	@Override
	public PowerReceiver getPowerReceiver(ForgeDirection side) {
		return powerHandler.getPowerReceiver();
	}

	@Override
	public void doWork(PowerHandler workProvider) {

	}
	
}
