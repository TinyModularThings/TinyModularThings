package speiger.src.tinymodularthings.common.interfaces;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IEntityGuiProvider
{
	public boolean hasGui();
	
	@SideOnly(Side.CLIENT)
	public GuiContainer getGui(InventoryPlayer par0);
	
	public AdvContainer getInventory(InventoryPlayer par0);
	
	public String getInvName();
}
