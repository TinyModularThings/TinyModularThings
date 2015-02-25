package speiger.src.tinymodularthings.common.items.energy;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.common.items.core.ItemInventory;


public class BatteryData extends ItemInventory
{

	public BatteryData(EntityPlayer player, ItemStack provider)
	{
		super(player, provider, 0);
	}

	@Override
	public String getInvName()
	{
		return "BatteryData";
	}
	
	public boolean renderInnerInv()
	{
		return false;
	}
	
	public boolean renderOuterInv()
	{
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	public void onGuiConstructed(GuiInventoryCore par1)
	{
		
	}
	
	@SideOnly(Side.CLIENT)
	public void onGuiLoad(GuiInventoryCore par1, int guiX, int guiY)
	{
		
	}
	
	@SideOnly(Side.CLIENT)
	public void drawExtras(GuiInventoryCore par1, int guiX, int guiY, int mouseX, int mouseY)
	{
		
	}
	
	@SideOnly(Side.CLIENT)
	public void onButtonClick(GuiInventoryCore par1, GuiButton par2)
	{
		
	}
	
	@SideOnly(Side.CLIENT)
	public void drawFrontExtras(GuiInventoryCore par1, int guiX, int guiY, int mouseX, int mouseY)
	{
		
	}
}
