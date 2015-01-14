package speiger.src.spmodapi.client.gui;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import org.lwjgl.opengl.GL11;

import speiger.src.api.common.inventory.slot.TankSlot;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import speiger.src.spmodapi.common.util.slot.SpmodSlot;

public class GuiInventoryCore extends GuiContainer
{
	public static TextureEngine engine = TextureEngine.getTextures();
	boolean defined = false;
	boolean autoDrawing = false;
	int x = 0;
	int y = 0;
	AdvTile tile = null;
	
	
	public GuiInventoryCore(InventoryPlayer par1, AdvTile par2)
	{
		this(par2.getInventory(par1));
	}
	
	public GuiInventoryCore(AdvContainer par1)
	{
		super(par1);
		this.tile = par1.getTile();
	}
	
	public GuiInventoryCore setAutoDrawing()
	{
		this.autoDrawing = true;
		return this;
	}
	
	public void defineSlot(int x, int y)
	{
		this.x = x;
		this.y = y;
		defined = true;
	}
	
	public void drawSlot(Slot par1)
	{
		this.drawSlot(par1.xDisplayPosition, par1.yDisplayPosition);
	}
	
	public void drawTankSlot(TankSlot par1)
	{
		this.drawSlot(par1.getXCoord(), par1.getYCoord(), 18, 60);
		drawFluidInTank(par1, 18, 60);
	}
	
	public void drawFluidInTank(TankSlot par1, int x, int y)
	{
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		FluidTank tank = par1.getTank();
		if(tank == null)
		{
			return;
		}
		int prepair = tank.getCapacity() / 58;
		int squaled = tank.getFluidAmount() / prepair;
		FluidStack liquid = tank.getFluid();
		if(liquid == null)
		{
			return;
		}
		
		int start = 0;
		Icon texture = null;
		Fluid fluid = liquid.getFluid();
		if(fluid != null && fluid.getStillIcon() != null)
		{
			texture = fluid.getStillIcon();
		}
		this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
		if (texture != null)
		{
			while (true)
			{
				int z;
				if (squaled > 16)
				{
					z = 16;
					squaled -= 16;
				}
				else
				{
					z = squaled;
					squaled = 0;
				}
				
				drawTexturedModelRectFromIcon(k + x, l + y + 58 - x - start, texture, 16, 16 - (16 - z));
				start += 16;
				
				if ((z == 0) || (squaled == 0))
				{
					break;
				}
			}
		}
		mc.getTextureManager().bindTexture(engine.getTexture("TankOverlayTexture"));
		drawTexturedModalRect(k + x, l + y, this.x, this.y, 18, 60);
	}
	
	public void drawSlot(int SlotX, int SlotY)
	{
		if (defined)
		{
			this.drawSlot(SlotX, SlotY, x, y);
		}
		else
		{
			SpmodAPI.log.print("Slot Texture is not defined");
		}
	}
	
	public void drawSlot(Slot par1, int xCoord, int yCoord)
	{
		this.drawSlot(par1.xDisplayPosition, par1.yDisplayPosition, xCoord, yCoord);
	}
	
	public void drawSlot(int SlotX, int SlotY, int xCoord, int yCoord)
	{
		int var5 = (width - xSize) / 2;
		int var6 = (height - ySize) / 2;
		
		drawTexturedModalRect(var5 + SlotX - 1, var6 + SlotY - 1, xCoord, yCoord, 18, 18);
	}
	
	public void drawSlotPros(int SlotX, int SlotY, int xSize, int ySize)
	{
		if (defined)
		{
			int var5 = (width - this.xSize) / 2;
			int var6 = (height - this.ySize) / 2;
			drawTexturedModalRect(var5 + SlotX - 1, var6 + SlotY - 1, x, y, xSize, ySize);
		}
		else
		{
			SpmodAPI.log.print("Slot Texture is not defined");
		}
	}
	
	public void drawSlots(int x, int y)
	{
		this.mc.getTextureManager().bindTexture(engine.getTexture("SlotTexture"));
		ArrayList<SpmodSlot> slotToDraw = ((AdvContainer) this.inventorySlots).getAllSlots();
		for (int i = 0; i < slotToDraw.size(); i++)
		{
			SpmodSlot slot = slotToDraw.get(i);
			this.drawSlot(slot);
			if(this.isMouseOverSlot(slot, x, y) && isCtrlKeyDown() && slot.hasUsage())
			{
				this.drawHoveringText(slot.getUsage(), x, y, fontRenderer);
			}
		}
		this.mc.getTextureManager().bindTexture(engine.getTexture("TankSlotTexture"));
		ArrayList<TankSlot> tankSlots = ((AdvContainer)this.inventorySlots).getTanks();
		for(int i = 0;i<tankSlots.size();i++)
		{
			TankSlot slot = tankSlots.get(i);
			this.drawTankSlot(slot);
			if(isMouseOverTankSlot(slot, x, y))
			{
				this.drawHoveringText(slot.getTankInfo(), x, y, fontRenderer);
			}
		}
		
	}
	
	@Override
	public void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		ResourceLocation resource = tile.getTexture();
		if(resource != null)
		{
			this.mc.getTextureManager().bindTexture(resource);
			this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
		}
		if(autoDrawing)
		{
			this.defineSlot(0, 0);
			drawSlots(par2, par3);
		}
		if(resource != null && autoDrawing)
		{
			this.mc.getTextureManager().bindTexture(resource);
		}
		tile.drawExtras(this, k, l, par2, par3);
	}
	
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		String s = tile.getInvName();
		this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		this.fontRenderer.drawString(I18n.getString("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		tile.drawFrontExtras(this, k, l, par1, par2);
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		tile.onGuiLoad(this, k, l);
	}

	protected boolean isMouseOverSlot(SpmodSlot par1, int x, int y)
	{
		return this.isPointInRegion(par1.xDisplayPosition, par1.yDisplayPosition, 16, 16, x, y);
	}
	
	protected boolean isMouseOverTankSlot(TankSlot par1, int x, int y)
	{
		return this.isPointInRegion(par1.getXCoord(), par1.getYCoord(), 18, 60, x, y);
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		tile.onButtonClick(this, par1GuiButton);
	}
	
	
	
}
