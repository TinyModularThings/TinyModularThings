package speiger.src.spmodapi.common.items.trades;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.village.MerchantRecipe;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import speiger.src.api.common.utils.InventoryUtil;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.common.tile.AdvInventory;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import speiger.src.spmodapi.common.util.slot.SpmodSlot;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BasicTradeInventory extends AdvInventory
{
	MerchantRecipe currentRecipe;
	public BasicTradeInventory()
	{
		super(3);
	}

	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		return null;
	}

	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
	{
		super.setInventorySlotContents(par1, par2ItemStack);
		if(par1 != 2)
		{
			container.onCraftMatrixChanged(this);
		}
	}

	@Override
	public void addContainerSlots(AdvContainer par1)
	{
		par1.addSpmodSlot(this, 0, 30, 50);
		par1.addSpmodSlot(this, 1, 70, 50);
		par1.addSpmodSlotToContainer(new TradeSlot(this, 2, 137, 50));
	}
	
	
	
	@Override
	public String getInvName()
	{
		return "Random Trade";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void drawExtras(GuiInventoryCore par1, int guiX, int guiY, int mouseX, int mouseY)
	{
		par1.setAutoDrawing();
		par1.setTexture(getEngine().getTexture("Objects"));
		par1.defineSlot("ProgBarH");
		par1.drawSlotPros(100, 25, 22, 16);
		par1.drawSlotPros(100, 50, 22, 16);
		GL11.glPushMatrix();
		ItemStack itemstack = currentRecipe.getItemToBuy();
		ItemStack itemstack1 = currentRecipe.getSecondItemToBuy();
		ItemStack itemstack2 = currentRecipe.getItemToSell();
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glEnable(GL11.GL_LIGHTING);
		
		par1.getItemRenderer().zLevel = 100.0F;
		par1.getItemRenderer().renderItemAndEffectIntoGUI(par1.getFontRenderer(), par1.getMC().getTextureManager(), itemstack, guiX + 30, guiY + 24);
		par1.getItemRenderer().renderItemOverlayIntoGUI(par1.getFontRenderer(), par1.getMC().getTextureManager(), itemstack, guiX + 30, guiY + 24);
		
		if (itemstack1 != null)
		{
			par1.getItemRenderer().renderItemAndEffectIntoGUI(par1.getFontRenderer(), par1.getMC().getTextureManager(), itemstack1, guiX + 70, guiY + 24);
			par1.getItemRenderer().renderItemOverlayIntoGUI(par1.getFontRenderer(), par1.getMC().getTextureManager(), itemstack1, guiX + 70, guiY + 24);
		}
		
		par1.getItemRenderer().renderItemAndEffectIntoGUI(par1.getFontRenderer(), par1.getMC().getTextureManager(), itemstack2, guiX + 135, guiY + 24);
		par1.getItemRenderer().renderItemOverlayIntoGUI(par1.getFontRenderer(), par1.getMC().getTextureManager(), itemstack2, guiX + 135, guiY + 24);
		par1.getItemRenderer().zLevel = 0.0F;
		
		if (par1.isPointInRegion(30, 24, 16, 16, mouseX, mouseY))
		{
			par1.drawItemStackTooltip(itemstack, mouseX, mouseY);
		}
		else if (itemstack1 != null && par1.isPointInRegion(70, 24, 16, 16, mouseX, mouseY))
		{
			par1.drawItemStackTooltip(itemstack1, mouseX, mouseY);
		}
		else if (par1.isPointInRegion(135, 24, 16, 16, mouseX, mouseY))
		{
			par1.drawItemStackTooltip(itemstack2, mouseX, mouseY);
		}
		
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	@Override
	public boolean canMergeItem(ItemStack par1, int slotID)
	{
		if(slotID == 0 || slotID == 1)
		{
			return true;
		}
		return super.canMergeItem(par1, slotID);
	}

	public MerchantRecipe getCurrentTrade()
	{
		return currentRecipe;
	}
	
	@Override
	public void onPlayerOpenContainer(EntityPlayer par1)
	{
		super.onPlayerOpenContainer(par1);
		currentRecipe = new MerchantRecipe(ItemRandomTrade.getRecipeFromItem(par1.getCurrentEquippedItem()).writeToTags());
	}
	
	
	
	@Override
	public boolean onSlotClicked(AdvContainer par1, int slotID, int mouseButton, int modifier, EntityPlayer player)
	{
		Slot slot = (Slot)par1.inventorySlots.get(slotID);
		if(slot != null && slot.getHasStack())
		{
			if(slot.getStack().isItemEqual(player.getCurrentEquippedItem()))
			{
				return true;
			}
		}
		return super.onSlotClicked(par1, slotID, mouseButton, modifier, player);
	}

	@Override
	public void onPlayerCloseContainer(EntityPlayer par1)
	{
		super.onPlayerCloseContainer(par1);
		par1.getCurrentEquippedItem().stackSize--;
	}

	@Override
	public void onMatrixChanged(AdvContainer par1, IInventory par2)
	{
		ItemStack key1 = getStackInSlot(0);
		ItemStack key2 = getStackInSlot(1);
		if (!currentRecipe.func_82784_g() && InventoryUtil.isItemEqualSave(currentRecipe.getItemToBuy(), key1) && InventoryUtil.isItemEqualSave(currentRecipe.getSecondItemToBuy(), key2))
		{
			currentRecipe.incrementToolUses();
			setInventorySlotContents(2, currentRecipe.getItemToSell().copy());
		}
		else
		{
			setInventorySlotContents(2, null);
		}
		super.onMatrixChanged(par1, par2);
		
	}



	public class TradeSlot extends SpmodSlot
	{
		public TradeSlot(AdvTile par1, int id, int x, int y)
		{
			super(par1, id, x, y);
		}
		
		@Override
		public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack)
		{
			super.onPickupFromSlot(par1EntityPlayer, par2ItemStack);
			MerchantRecipe recipe = ((BasicTradeInventory)getTile()).getCurrentTrade();
			if (recipe != null)
			{
				ItemStack key1 = recipe.getItemToBuy();
				ItemStack key2 = recipe.getSecondItemToBuy();
				if (key1 != null)
				{
					ItemStack cu = this.inventory.getStackInSlot(0);
					cu.stackSize -= key1.stackSize;
					this.inventory.setInventorySlotContents(0, cu);
				}
				if (key2 != null)
				{
					ItemStack cu = this.inventory.getStackInSlot(1);
					cu.stackSize -= key2.stackSize;
					this.inventory.setInventorySlotContents(1, cu);
				}
			}
		}
		
	}
}
