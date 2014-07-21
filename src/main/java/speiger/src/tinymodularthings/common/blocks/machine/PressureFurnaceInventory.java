package speiger.src.tinymodularthings.common.blocks.machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import speiger.src.tinymodularthings.common.utils.slot.SlotCoal;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PressureFurnaceInventory extends Container
{
	private PressureFurnace tile;
	
	public PressureFurnaceInventory(InventoryPlayer par1, PressureFurnace par2)
	{
		tile = par2;
		addSlotToContainer(new SlotCoal(par2, 0, 13, 53));
		addSlotToContainer(new Slot(par2, 1, 58, 37));
		addSlotToContainer(new Slot(par2, 2, 58, 58));
		addSlotToContainer(new Slot(par2, 3, 101, 20));
		addSlotToContainer(new SlotFurnace(par1.player, par2, 4, 150, 48));
		
		int var3;
		for (var3 = 0; var3 < 3; ++var3)
		{
			for (int var4 = 0; var4 < 9; ++var4)
			{
				addSlotToContainer(new Slot(par1, var4 + var3 * 9 + 9, 28 + var4 * 18, 101 + var3 * 18));
			}
		}
		
		for (var3 = 0; var3 < 9; ++var3)
		{
			addSlotToContainer(new Slot(par1, var3, 28 + var3 * 18, 159));
		}
		
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer var1)
	{
		return tile.isUseableByPlayer(var1);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
	{
		return null;
	}
	
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		for (int i = 0; i < crafters.size(); ++i)
		{
			ICrafting icrafting = (ICrafting) crafters.get(i);
			tile.onSendingGuiInfo(this, icrafting);
		}
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2)
	{
		tile.onReciveGuiInfo(par1, par2);
	}
	
}
