package speiger.src.spmodapi.common.items.tools;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import speiger.src.spmodapi.common.util.slot.AdvContainer;

public class ContainerPotionBag extends AdvContainer
{
	private int id;
	
	public ContainerPotionBag(InventoryPlayer par1, IInventory par2)
	{
		id = par1.currentItem+par2.getSizeInventory();
		
		
		this.setInventory(par1);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return true;
	}

	@Override
	public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer par4EntityPlayer)
	{
		
		return super.slotClick(par1, par2, par3, par4EntityPlayer);
	}
	
	
}