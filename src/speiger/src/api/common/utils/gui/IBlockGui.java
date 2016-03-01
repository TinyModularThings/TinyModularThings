package speiger.src.api.common.utils.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import speiger.src.api.common.utils.blocks.BlockPosition;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IBlockGui
{
	@SideOnly(Side.CLIENT)
	public GuiContainer getGui(BlockPosition pos, InventoryPlayer par1);
	
	public Container getContainer(BlockPosition pos, InventoryPlayer par1);
}
