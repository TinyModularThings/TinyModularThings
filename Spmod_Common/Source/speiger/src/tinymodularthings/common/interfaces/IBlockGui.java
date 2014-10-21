package speiger.src.tinymodularthings.common.interfaces;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IBlockGui
{
	@SideOnly(Side.CLIENT)
	public GuiContainer getGui(int meta, InventoryPlayer par1);
	
	public Container getInventory(int meta, InventoryPlayer par1);
}
