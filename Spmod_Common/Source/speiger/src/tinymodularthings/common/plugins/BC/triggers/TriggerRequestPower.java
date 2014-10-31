package speiger.src.tinymodularthings.common.plugins.BC.triggers;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import buildcraft.BuildCraftTransport;
import buildcraft.api.gates.ITrigger;
import buildcraft.api.gates.ITriggerParameter;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TriggerRequestPower implements ITrigger
{
	
	@Override
	public int getLegacyId()
	{
		return 0;
	}
	
	@Override
	public String getUniqueTag()
	{
		return "Machine.request.power";
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon()
	{
		return BuildCraftTransport.triggerPipeRequestsEnergy.getIcon();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		
	}
	
	@Override
	public boolean hasParameter()
	{
		return false;
	}
	
	@Override
	public boolean requiresParameter()
	{
		return true;
	}
	
	@Override
	public String getDescription()
	{
		return "Machine Request Power";
	}
	
	@Override
	public boolean isTriggerActive(ForgeDirection side, TileEntity tile, ITriggerParameter parameter)
	{
		if (tile != null)
		{
			if (tile instanceof IPowerReceptor)
			{
				IPowerReceptor power = (IPowerReceptor) tile;
				if (power.getPowerReceiver(side) != null)
				{
					PowerReceiver reciver = power.getPowerReceiver(side);
					return reciver.getEnergyStored() < (reciver.getMaxEnergyStored() - (reciver.getMaxEnergyStored() / 100));
				}
				
			}
		}
		return false;
	}
	
	@Override
	public ITriggerParameter createParameter()
	{
		return null;
	}
	
}
