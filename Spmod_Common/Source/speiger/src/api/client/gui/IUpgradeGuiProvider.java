package speiger.src.api.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import speiger.src.api.common.world.tiles.interfaces.IHopper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IUpgradeGuiProvider
{
	/**
	 * GuiProvider Class. In this case used for HopperUpgrades. Searches in
	 * Hopper Registry for the name and then opens the gui from it
	 */
	
	@SideOnly(Side.CLIENT)
	public GuiContainer getGui(InventoryPlayer par1, IHopper par2);
	
	public Container getInventory(InventoryPlayer par1, IHopper par2);
}
