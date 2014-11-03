package speiger.src.spmodapi.client.gui.utils;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import speiger.src.api.blocks.BlockPosition;
import speiger.src.api.packets.SpmodPacketHelper;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.common.blocks.utils.InventoryAccesser;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import speiger.src.tinymodularthings.client.gui.storage.GuiTinyChest;
import cpw.mods.fml.common.network.PacketDispatcher;

public class GuiInventoryAccesser extends GuiInventoryCore
{
	public InventoryAccesser tile;
	public int page = 0;
	public boolean name = false;
	public int choosenTarget = 0;
	public GuiTextField text;
	public GuiInventoryAccesser(InventoryPlayer par1, InventoryAccesser par2)
	{
		super(par2.getInventory(par1));
		tile = par2;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		String s = "Inventory Accesser";
		fontRenderer.drawString(s, -10 + xSize / 2 - fontRenderer.getStringWidth(s) / 2, -10, 0xffffff);
		fontRenderer.drawString(I18n.getString("container.inventory"), 0, ySize - 20 + 2, 0xffffff);
		this.buttonList.clear();
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		
		
		
		if(tile.needPower())
		{
			fontRenderer.drawString("Need Power", 50, 20, 0xffffff);
			return;
		}
		
		if(name)
		{
			if(!tile.isInRange(choosenTarget))
			{
				name = false;
				text = null;
				choosenTarget = 0;
				return;
			}
			BlockPosition pos = tile.getTarget(choosenTarget);
			
			ItemStack stack = pos.getAsBlockStack().getPicketBlock(pos, tile.getSideFromPlayer(mc.thePlayer.username));
			
			this.renderItem(stack, 60, 20);
			
			if(text == null)
			{
				text = new GuiTextField(fontRenderer, 20, 40, 100, 10);
				text.setCanLoseFocus(true);
				text.setFocused(true);
				if(tile.hasCustomName(pos.getAsList()))
				{
					text.setText(tile.getCustomName(pos.getAsList()));
				}
				return;
			}
			text.drawTextBox();
		
			buttonList.add(new GuiButton(8, k+10, l+70, 50, 20, "Back"));
			buttonList.add(new GuiButton(9, k+70, l+70, 50, 20, "Confirm"));
		}
		else
		{
			fontRenderer.drawString(""+page, 72, 7, 0xffffff);
			buttonList.add(new GuiButton(6, k+90, l, 20, 20, "+"));
			buttonList.add(new GuiButton(7, k+40, l, 20, 20, "-"));
			for(int i = 0;i<6;i++)
			{
				if(!tile.isInRange((page*6)+i))
				{
					return;
				}
				BlockPosition pos = tile.getTarget((page*6)+i);
				String name = pos.getAsBlockStack().getPickedBlockDisplayName(pos, tile.getSideFromPlayer(mc.thePlayer.username));
				
				if(tile.hasCustomName(pos.getAsList()))
				{
					name = tile.getCustomName(pos.getAsList());
				}
				buttonList.add(new GuiButton(i, k, l+25+20*i, 145, 20, name));
				ItemStack stack = pos.getAsBlockStack().getPicketBlock(pos, tile.getSideFromPlayer(mc.thePlayer.username));
				if(stack == null)
				{
					continue;
				}
				this.renderItem(stack, -17, 28+(20*i));
			}
		}
	}
	
	
	
	@Override
	protected void keyTyped(char par1, int par2)
	{
		if(text != null)
		{
			text.textboxKeyTyped(par1, par2);
			if(par1 == Character.valueOf('E') || par1 == Character.valueOf('e'))
			{
				return;
			}
		}
		super.keyTyped(par1, par2);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(GuiTinyChest.furnaceGuiTextures);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		ArrayList<Slot> slotToDraw = ((AdvContainer)inventorySlots).getAllSlots();
		defineSlot(176, 4);
		for (int i = 0; i < slotToDraw.size(); i++)
		{
			this.drawSlot(slotToDraw.get(i));
		}
		
	}
	
	private void renderItem(ItemStack stack, int x, int y) {
		GL11.glTranslatef(0.0F, 0.0F, 32.0F);
		this.zLevel = 200.0F;
		itemRenderer.zLevel = 200.0F;
		try 
		{
			itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, this.mc.getTextureManager(), stack, x, y);
			itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.getTextureManager(), stack, x, y);
		} 
		catch (Throwable e) 
		{
		}
		this.zLevel = 0.0F;
		itemRenderer.zLevel = 0.0F;
	}

	@Override
	protected void actionPerformed(GuiButton par1)
	{
		int id = par1.id;
		if(id == 6)
		{
			if(tile.isInRange((page+1)*6))
			{
				page++;
			}
		}
		else if(id == 7)
		{
			if(page > 0)
			{
				page--;
			}
		}
		else if(id == 8)
		{
			name = false;
			text = null;
			choosenTarget = 0;
		}
		else if(id == 9)
		{
			PacketDispatcher.sendPacketToServer(SpmodPacketHelper.getHelper().createNBTPacket(tile, getSpmodCore()).InjectNumbers(0, choosenTarget).injetString(text.getText()).finishPacket());
			name = false;
			text = null;
			choosenTarget = 0;
			
		}
		else
		{
			if(this.isShiftKeyDown())
			{
				choosenTarget = (page*6)+id;
				page = 0;
				name = true;
			}
			else
			{
				PacketDispatcher.sendPacketToServer(SpmodPacketHelper.getHelper().createNBTPacket(tile, getSpmodCore()).InjectNumbers(1, (page*6)+id).injetString(this.mc.thePlayer.username).finishPacket());
			}
		}
	}
	
	
	
}
