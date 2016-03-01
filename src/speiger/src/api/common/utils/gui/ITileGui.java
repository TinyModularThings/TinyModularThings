package speiger.src.api.common.utils.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface ITileGui
{
	@SideOnly(Side.CLIENT)
	public GuiContainer getGui(InventoryPlayer par1);
	
	public Container getContainer(InventoryPlayer par1);
}
