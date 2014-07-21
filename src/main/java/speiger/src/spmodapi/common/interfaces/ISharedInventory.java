package speiger.src.spmodapi.common.interfaces;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface ISharedInventory
{
	@SideOnly(Side.CLIENT)
	public GuiContainer getGui(InventoryPlayer par1);
	
	public Container getInventory(InventoryPlayer par1);
	
	public IInventory getIInventory();
	
	public boolean isEntity();
}
