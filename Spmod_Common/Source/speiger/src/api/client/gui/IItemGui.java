package speiger.src.api.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IItemGui
{
	boolean hasGui(ItemStack par1);
	
	@SideOnly(Side.CLIENT)
	public GuiContainer getGui(InventoryPlayer par1, ItemStack par2);
	
	boolean hasContainer(ItemStack par1);
	
	AdvContainer getContainer(InventoryPlayer par1, ItemStack par2);
}
