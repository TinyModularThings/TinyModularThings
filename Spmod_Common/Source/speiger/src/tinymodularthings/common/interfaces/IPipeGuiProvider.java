package speiger.src.tinymodularthings.common.interfaces;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public interface IPipeGuiProvider
{
	public GuiContainer getGui(InventoryPlayer par1);
	
	public Container getInventory(InventoryPlayer par1);
}
