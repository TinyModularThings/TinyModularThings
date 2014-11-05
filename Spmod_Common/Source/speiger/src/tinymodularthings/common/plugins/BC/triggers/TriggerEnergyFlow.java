package speiger.src.tinymodularthings.common.plugins.BC.triggers;

import ic2.core.block.generator.tileentity.TileEntityWindGenerator;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import buildcraft.api.gates.ITrigger;
import buildcraft.api.gates.ITriggerParameter;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TriggerEnergyFlow implements ITrigger
{
	boolean flow;
	
	public TriggerEnergyFlow(boolean par1)
	{
		flow = par1;
	}
	
	@Override
	public int getLegacyId()
	{
		return 0;
	}
	
	@Override
	public String getUniqueTag()
	{
		if(flow)
		{
			return "trigger.energy.Flow";
		}
		return "trigger.energy.notFlow";
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon()
	{
		return Item.appleGold.getIconFromDamage(0);
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
		return false;
	}
	
	@Override
	public String getDescription()
	{
		if(!flow)
		{
			return "Energy is not flowing through the cable";
		}
		return "Energy Flow through cable";
	}
	
	@Override
	public boolean isTriggerActive(ForgeDirection side, TileEntity tile, ITriggerParameter parameter)
	{
		if(tile != null)
		{
			try
			{
				if(tile instanceof TileEntityWindGenerator)
				{
					TileEntityWindGenerator cable = (TileEntityWindGenerator)tile;
					cable.ticker--;
					return flow ? cable.gainEnergy() : !cable.gainEnergy();
				}
			}
			catch(Exception e)
			{
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
