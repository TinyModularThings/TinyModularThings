package speiger.src.tinymodularthings.common.plugins.BC.triggers;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.items.InfoStack;
import speiger.src.api.language.LanguageRegister;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.blocks.machine.PressureFurnace;
import buildcraft.api.gates.ITrigger;
import buildcraft.api.gates.ITriggerParameter;
import buildcraft.core.triggers.ActionTriggerIconProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TriggerHasWork implements ITrigger
{
	
	boolean Active;
	
	public TriggerHasWork(boolean active)
	{
		Active = active;
	}
	
	@Override
	public int getLegacyId()
	{
		return 0;
	}
	
	@Override
	public String getUniqueTag()
	{
		if (Active)
		{
			return "trigger.has.work";
		}
		return "trigger.no.work";
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon()
	{
		if (Active)
		{
			return ActionTriggerIconProvider.INSTANCE.getIcon(ActionTriggerIconProvider.Trigger_Machine_Active);
		}
		return ActionTriggerIconProvider.INSTANCE.getIcon(ActionTriggerIconProvider.Trigger_Machine_Inactive);
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
	public String getDescription()
	{
		if (Active)
		{
			return LanguageRegister.getLanguageName(new InfoStack(), "has.work", TinyModularThings.instance);
		}
		return LanguageRegister.getLanguageName(new InfoStack(), "no.work", TinyModularThings.instance);
	}
	
	@Override
	public boolean isTriggerActive(ForgeDirection side, TileEntity tile, ITriggerParameter parameter)
	{
		if (tile != null)
		{
			if (tile instanceof PressureFurnace)
			{
				if (Active)
				{
					return ((PressureFurnace) tile).currentRecipe != null;
				}
				else
				{
					return ((PressureFurnace) tile).currentRecipe == null;
				}
			}
			else if (tile instanceof TileEntityFurnace)
			{
				if (Active)
				{
					return canSmelt((TileEntityFurnace) tile);
				}
				return !canSmelt((TileEntityFurnace) tile);
			}
		}
		
		return false;
	}
	
	@Override
	public ITriggerParameter createParameter()
	{
		return null;
	}
	
	public boolean canSmelt(TileEntityFurnace par1)
	{
		
		if (par1.getStackInSlot(0) == null)
		{
			return false;
		}
		else
		{
			ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(par1.getStackInSlot(0));
			if (itemstack == null)
			{
				return false;
			}
			if (par1.getStackInSlot(2) == null)
			{
				return true;
			}
			if (!par1.getStackInSlot(2).isItemEqual(itemstack))
			{
				return false;
			}
			int result = par1.getStackInSlot(2).stackSize + itemstack.stackSize;
			return (result <= par1.getInventoryStackLimit() && result <= itemstack.getMaxStackSize());
		}
	}

	@Override
	public boolean requiresParameter()
	{
		// TODO Auto-generated method stub
		return false;
	}
	
}
