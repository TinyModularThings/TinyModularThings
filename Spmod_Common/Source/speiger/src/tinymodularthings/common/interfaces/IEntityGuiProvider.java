package speiger.src.tinymodularthings.common.interfaces;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import speiger.src.spmodapi.common.util.slot.AdvContainer;

public interface IEntityGuiProvider
{
	public boolean hasGui();
	
	public GuiContainer getGui(InventoryPlayer par0);
	
	public AdvContainer getInventory(InventoryPlayer par0);
	
	public String getInvName();
}
