package speiger.src.tinymodularthings.common.plugins.BC.triggers;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.spmodapi.common.util.TileIconMaker;
import buildcraft.api.gates.ITrigger;
import buildcraft.api.gates.ITriggerParameter;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.core.triggers.ForestryTrigger;
import forestry.factory.gadgets.MachineFermenter;
import forestry.factory.gadgets.MachineFermenter.RecipeManager;
import forestry.farming.gadgets.TileGearbox;

public class CanWorkTrigger implements ITrigger
{
	
	@Override
	public int getLegacyId()
	{
		return 0;
	}
	
	@Override
	public String getUniqueTag()
	{
		return "trigger.fermenter.tinychest";
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon()
	{
		try
		{
			return ForestryTrigger.hasWork.getIcon();
		}
		catch (Exception e)
		{
		}
		return TileIconMaker.getIconMaker().getIconSafe(null);
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
		return "Can Work";
	}
	
	@Override
	public boolean isTriggerActive(ForgeDirection side, TileEntity tile, ITriggerParameter parameter)
	{
		try
		{
			if(tile != null)
			{
				if(tile instanceof MachineFermenter)
				{
					MachineFermenter machine = (MachineFermenter) tile;
					if(machine.fuelBurnTime > 0 && machine.fermentationTime > 0 && machine.resourceTank.getFluidAmount() > 0)
					{
						if(RecipeManager.findMatchingRecipe(machine.getStackInSlot(0), machine.resourceTank.getFluid()) != null)
						{
							if(machine.productTank.getFluidAmount() < machine.productTank.getCapacity())
							{
								if(machine.productTank.getFluidAmount() > (machine.productTank.getCapacity() - 100))
								{
									return false;
								}
								else
								{
									return true;
								}
							}
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			FMLLog.getLogger().info("Exeption: "+e.fillInStackTrace());
		}
		
		return false;
	}
	
	@Override
	public ITriggerParameter createParameter()
	{
		return null;
	}
	
}
