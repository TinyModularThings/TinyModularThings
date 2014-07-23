package speiger.src.tinymodularthings.common.interfaces;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public interface IEntityGuiProvider
{
	public boolean hasGui();
	
	public GuiContainer getGui(InventoryPlayer par0);
	
	public Container getInventory(InventoryPlayer par0);
}
