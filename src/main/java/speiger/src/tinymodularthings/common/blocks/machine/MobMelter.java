package speiger.src.tinymodularthings.common.blocks.machine;

import javax.swing.Icon;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import speiger.src.api.energy.EnergyProvider;
import speiger.src.api.energy.IEnergyProvider;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;

public class MobMelter extends AdvTile implements IEnergyProvider, IPowerReceptor
{
	public EnergyProvider provider = new EnergyProvider(this, 100000);
	
	public static Icon[] textures = new Icon[2];
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		return side < 2 ? textures[0] : textures[1];
	}
	
	

	@Override
	public void registerIcon(IconRegister par1)
	{
		textures[1] = par1.registerIcon(TinyModularThingsLib.ModID.toLowerCase()+":machine/machineFrame");
		textures[0] = par1.registerIcon(TinyModularThingsLib.ModID.toLowerCase()+":machine/macerrator");
	}



	@Override
	public PowerReceiver getPowerReceiver(ForgeDirection side)
	{
		return provider.getSaveBCPowerProvider();
	}

	@Override
	public void doWork(PowerHandler workProvider)
	{
	}

	@Override
	public World getWorld()
	{
		return worldObj;
	}

	@Override
	public EnergyProvider getEnergyProvider(ForgeDirection side)
	{
		return provider;
	}



	public boolean canMelt()
	{
		return provider.getEnergy() > 7;
	}
	
	public void melt()
	{
		provider.useEnergy(7, false);
	}



	public boolean canSendExp()
	{
		return false;
	}



	public void sendExp(int exp)
	{
		
	}
	
}
