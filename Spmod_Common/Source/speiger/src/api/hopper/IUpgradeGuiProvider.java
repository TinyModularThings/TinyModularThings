package speiger.src.api.hopper;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public interface IUpgradeGuiProvider
{
	/**
	 * GuiProvider Class. In this case used for HopperUpgrades.
	 * Searches in Hopper Registry for the name and then opens the gui from it
	 */
	
	
	
	public GuiContainer getGui(InventoryPlayer par1, IHopper par2);
	
	public Container getInventory(InventoryPlayer par1, IHopper par2);
}
