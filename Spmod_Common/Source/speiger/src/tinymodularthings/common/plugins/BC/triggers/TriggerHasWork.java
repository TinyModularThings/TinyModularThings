package speiger.src.tinymodularthings.common.plugins.BC.triggers;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.potion.PotionHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
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
			if (tile instanceof TileEntityFurnace)
			{
				if (Active)
				{
					return canSmelt((TileEntityFurnace) tile);
				}
				return !canSmelt((TileEntityFurnace) tile);
			}
			
			if (tile instanceof TileEntityBrewingStand)
			{
				TileEntityBrewingStand brew = (TileEntityBrewingStand) tile;
				if (Active)
				{
					return this.canBrew(brew);
				}
				return !this.canBrew(brew);
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
		return false;
	}
	
	private boolean canBrew(TileEntityBrewingStand par1)
	{
		if (par1.getStackInSlot(3) != null && par1.getStackInSlot(3).stackSize > 0)
		{
			ItemStack itemstack = par1.getStackInSlot(3);
			
			if (!Item.itemsList[itemstack.itemID].isPotionIngredient())
			{
				return false;
			}
			else
			{
				boolean flag = false;
				
				for (int i = 0; i < 3; ++i)
				{
					if (par1.getStackInSlot(i) != null && par1.getStackInSlot(i).getItem() instanceof ItemPotion)
					{
						int j = par1.getStackInSlot(i).getItemDamage();
						int k = this.getPotionResult(j, itemstack);
						
						if (!ItemPotion.isSplash(j) && ItemPotion.isSplash(k))
						{
							flag = true;
							break;
						}
						
						List list = Item.potion.getEffects(j);
						List list1 = Item.potion.getEffects(k);
						
						if ((j <= 0 || list != list1) && (list == null || !list.equals(list1) && list1 != null) && j != k)
						{
							flag = true;
							break;
						}
					}
				}
				
				return flag;
			}
		}
		else
		{
			return false;
		}
	}
	
	private int getPotionResult(int par1, ItemStack par2ItemStack)
	{
		return par2ItemStack == null ? par1 : (Item.itemsList[par2ItemStack.itemID].isPotionIngredient() ? PotionHelper.applyIngredient(par1, Item.itemsList[par2ItemStack.itemID].getPotionEffect()) : par1);
	}

	@Override
	public String getDescription()
	{
		if(Active)return "Has Work";
		return "Work done";
	}
}
