package speiger.src.tinymodularthings.client.gui.machine;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import speiger.src.api.language.LanguageRegister;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.tinymodularthings.common.blocks.machine.PressureFurnace;
import speiger.src.tinymodularthings.common.blocks.machine.PressureFurnaceInventory;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;

public class PressureFurnaceGui extends GuiInventoryCore
{
	private PressureFurnace tile;
	
	public PressureFurnaceGui(InventoryPlayer par0, PressureFurnace par1)
	{
		super(new PressureFurnaceInventory(par0, par1));
		ySize = 188;
		xSize = 215;
		tile = par1;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		String name = LanguageRegister.getLanguageName(this, "pressurefurnace", getCore());
		String heat = LanguageRegister.getLanguageName(this, "heat", getCore());
		fontRenderer.drawString(name, xSize / 2 - fontRenderer.getStringWidth(name) / 2, 6, 4210752);
		fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 105 + 2, 4210752);
		fontRenderer.drawString(heat, 33, 50, 4210752);
		int maxHeat = tile.heat / 12;
		fontRenderer.drawString("" + maxHeat + "%", 32, 61, 4210752);
		if (maxHeat < 100)
		{
			String text = LanguageRegister.getLanguageName(this, "furnace.starting", getCore());
			fontRenderer.drawString(text, 80, 65, 4210752);
		}
		else
		{
			if (tile.getRecipeModeFromInventory(tile.inv) > 0)
			{
				int progress = tile.progress / 3;
				String done = LanguageRegister.getLanguageName(this, "furnace.progress", getCore());
				fontRenderer.drawString(done + ": " + progress + "%", 80, 67, 4210752);
			}
			else
			{
				String need = LanguageRegister.getLanguageName(this, "furnace.request", getCore());
				fontRenderer.drawString(need, 80, 67, 4210752);
			}
			
		}
		drawSlotInventory(new Slot(tile, 6, 20, 50));
	}
	
	private static final ResourceLocation gui = new ResourceLocation(TinyModularThingsLib.ModID.toLowerCase() + ":textures/gui/machine/pressuredFurnace.png");
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(gui);
		int var5 = (width - xSize) / 2;
		int var6 = (height - ySize) / 2;
		drawTexturedModalRect(var5, var6, 0, 0, xSize, ySize);
		if (tile.getRecipeModeFromInventory(tile.inv) > 0)
		{
			drawTexturedModalRect(var5 + 119, var6 + 48, 216, 14, 22, 16);
			drawTexturedModalRect(var5 + 106, var6 + 54, 216, 33, 13, 4);
			drawTexturedModalRect(var5 + 93, var6 + 54, 216, 33, 13, 4);
			drawTexturedModalRect(var5 + 80, var6 + 54, 216, 33, 13, 4);
			drawTexturedModalRect(var5 + 77, var6 + 54, 216, 33, 13, 4);
			
			if (tile.getRecipeModeFromInventory(tile.inv) == 2)
			{
				drawTexturedModalRect(var5 + 107, var6 + 39, 216, 39, 4, 15);
			}
		}
		
		if (tile.fuel > 0)
		{
			int fuel = tile.fuel / 500;
			drawTexturedModalRect(var5 + 14, var6 + 38 + 12 - fuel, 216, 13 - fuel, 13, 0 + fuel);
		}
		
	}
	
}
