package speiger.src.api.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IItemGui
{
	boolean hasGui(ItemStack par1);
	
	@SideOnly(Side.CLIENT)
	public GuiScreen getGui(InventoryPlayer par1, ItemStack par2);
	
	boolean hasContainer(ItemStack par1);
	
	Container getContainer(InventoryPlayer par1, ItemStack par2);
}
