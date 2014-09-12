package speiger.src.spmodapi.client.gui.utils;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.common.blocks.utils.MobMachine;
import speiger.src.spmodapi.common.lib.SpmodAPILib;
import speiger.src.spmodapi.common.tile.AdvTile;

public class GuiMobMachine extends GuiInventoryCore
{
	MobMachine mob;
	
	public GuiMobMachine(InventoryPlayer player, AdvTile tile)
	{
		super(tile.getInventory(player));
		mob = (MobMachine) tile;
	}
	
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		String s = "";
		this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		this.fontRenderer.drawString(I18n.getString("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}
	
	ResourceLocation texture = new ResourceLocation(SpmodAPILib.ModID.toLowerCase() + ":textures/gui/utils/mobMachine.png");
	
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(texture);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
		this.defineSlot(176, 0);
		this.drawSlots();
		this.defineSlot(176, 19);
		this.drawSlotPros(13, 7, 4, 43);
		this.drawSlotPros(62, 7, 4, 43);
		if (mob.food > 0 || mob.lifeEssens > 0)
		{
			this.defineSlot(185, 62);
			if (mob.food > 0)
			{
				int amount = -(mob.food / 20);
				this.drawSlotPros(17, 50, -4, Math.max(-43, amount));
			}
			if (mob.lifeEssens > 0)
			{
				int amount = -(mob.lifeEssens / 20);
				this.drawSlotPros(66, 50, -4, Math.max(-43, amount));
			}
		}
		this.defineSlot(176, 64);
		this.drawSlotPros(83, 30, 23, 16);
		this.defineSlot(176, 82);
		if (mob.eP > 0)
		{
			int max = mob.eP / 260;
			this.drawSlotPros(83, 30, max, 16);
		}
	}
	
}
