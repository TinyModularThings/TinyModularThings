package speiger.src.api.common.utils.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IItemTileGui
{
	@SideOnly(Side.CLIENT)
	public GuiScreen getGui(ItemStack item, InventoryPlayer player, TileEntity tile);
	
	public Container getContainer(ItemStack item, InventoryPlayer player, TileEntity tile);
}
