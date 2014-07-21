package speiger.src.api.items;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IItemGui
{
	boolean hasGui(ItemStack par1);
	
	@SideOnly(Side.CLIENT)
	public GuiScreen getGui(ItemStack par1);
	
	boolean hasContainer(ItemStack par1);
	
	Container getContainer(ItemStack par1);
}
