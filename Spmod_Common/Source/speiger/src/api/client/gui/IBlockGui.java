package speiger.src.api.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import speiger.src.api.common.world.blocks.BlockPosition;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IBlockGui
{
	@SideOnly(Side.CLIENT)
	public GuiContainer getGui(int meta, InventoryPlayer par1, BlockPosition par2);
	
	public Container getInventory(int meta, InventoryPlayer par1, BlockPosition par2);
}
