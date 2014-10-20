package speiger.src.tinymodularthings.common.plugins.BC.triggers;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.TileIconMaker;
import buildcraft.BuildCraftCore;
import buildcraft.api.gates.ITrigger;
import buildcraft.api.gates.ITriggerParameter;
import buildcraft.api.gates.TriggerParameter;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TriggerTank implements ITrigger
{
	public int type;
	
	public TriggerTank(int type)
	{
		this.type = type;
	}
	
	
	@Override
	public int getLegacyId()
	{
		return 0;
	}
	
	@Override
	public String getUniqueTag()
	{
		return "trigger.fluid."+type;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon()
	{
		switch(type)
		{
			case 0: return BuildCraftCore.triggerInventoryBelow25.getIcon();
			case 1: return BuildCraftCore.triggerInventoryBelow50.getIcon();
			case 2: return BuildCraftCore.triggerInventoryBelow75.getIcon();
			default: return TextureEngine.getTextures().getIconSafe();
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
	}
	
	@Override
	public boolean hasParameter()
	{
		return true;
	}
	
	@Override
	public boolean requiresParameter()
	{
		return false;
	}
	
	@Override
	public String getDescription()
	{
		switch(type)
		{
			case 0: return "Contains Fluid < 25%";
			case 1: return "Contains Fluid < 50%";
			case 2: return "Contains Fluid < 75%";
			default: return "Error";
		}
	}
	
	@Override
	public boolean isTriggerActive(ForgeDirection side, TileEntity tile, ITriggerParameter par2)
	{
		if(tile != null)
		{
			if(tile instanceof IFluidHandler)
			{
				IFluidHandler fluid = (IFluidHandler)tile;
				FluidTankInfo[] info = fluid.getTankInfo(side);
				if(info != null && info.length > 0)
				{
					int stored = 0;
					int space = 0;
				
					if(par2 == null || FluidContainerRegistry.getFluidForFilledItem(par2.getItemStack()) == null)
					{
						if(par2 != null && par2.getItemStack() != null && FluidContainerRegistry.getFluidForFilledItem(par2.getItemStack()) == null)
						{
							return false;
						}
						
						for(FluidTankInfo cu : info)
						{
							space += cu.capacity;
							if(cu.fluid != null)
							{
								stored += cu.fluid.amount;
							}
						}
						
					}
					else
					{
						FluidStack stack = FluidContainerRegistry.getFluidForFilledItem(par2.getItemStack());
						if(stack != null)
						{
							//TODO Find a cleaner way to check how full filtered tanks are. At the moment checking only tanks that contain the searched fluid.
							for(FluidTankInfo cu : info)
							{
								if(cu.fluid != null && cu.fluid.isFluidEqual(stack))
								{
									stored += cu.fluid.amount;
									space += cu.capacity;
								}
							}
						}
					}
					if(stored <= space && space > 0)
					{
						double per = ((double)stored / (double)space) * 100;
						if(per < (25D + (type * 25D)))
						{
							return true;
						}
					}
				}
			}
		}
		
		
		return false;
	}
	
	@Override
	public ITriggerParameter createParameter()
	{
		return new TriggerParameter();
	}
	
}
