package speiger.src.api.common.utils.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IItemGui
{
	@SideOnly(Side.CLIENT)
	public GuiContainer getGui(ItemStack item, InventoryPlayer par1);
	
	public Container getContainer(ItemStack item, InventoryPlayer par1);
}
