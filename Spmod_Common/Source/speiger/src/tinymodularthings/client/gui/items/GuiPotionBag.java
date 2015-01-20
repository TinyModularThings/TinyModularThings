package speiger.src.tinymodularthings.client.gui.items;

import java.util.ArrayList;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import speiger.src.api.client.gui.IItemGui;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;

public class GuiPotionBag extends GuiInventoryCore
{
	InventoryPlayer inv;
	ItemStack stack;
	
	public GuiPotionBag(InventoryPlayer par1, AdvContainer par2)
	{
		super(par2);
		inv = par1;
		stack = par1.player.getCurrentEquippedItem();
		this.ySize = 221;
	}
	
}
