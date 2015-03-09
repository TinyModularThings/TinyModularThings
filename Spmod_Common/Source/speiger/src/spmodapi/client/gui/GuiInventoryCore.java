package speiger.src.spmodapi.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import speiger.src.api.common.inventory.slot.TankSlot;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.client.gui.buttons.SpmodGuiButton;
import speiger.src.spmodapi.common.interfaces.IAdvTile;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import speiger.src.spmodapi.common.util.slot.EnergySlot;
import speiger.src.spmodapi.common.util.slot.PlayerSlot;
import speiger.src.spmodapi.common.util.slot.SpmodSlot;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 
 * @author Speiger
 * 
 *         This gui class is extremly Experimental! The Basic Time it rquires
 *         are 30-40 qm (40000 NanoSeconds) for each client tick Because i
 *         render every slot extra. Which mean less Textures to loadand that
 *         mean less ram usage. And because you have only max 1 gui open at the
 *         time the open that mean it should not effect your performance..
 * 
 */
@SideOnly(Side.CLIENT)
public class GuiInventoryCore extends GuiContainer
{
	public static TextureEngine engine = TextureEngine.getTextures();
	private GuiButton currentButton;
	boolean defined = false;
	boolean autoDrawing = false;
	public int x = 0;
	public int y = 0;
	IAdvTile tile = null;
	NBTTagCompound extraData;
	GuiObject[] extraObjects = new GuiObject[0];
	
	public GuiInventoryCore(InventoryPlayer par1, IAdvTile par2)
	{
		this(par2.getInventory(par1));
	}
	
	public GuiInventoryCore(AdvContainer par1)
	{
		super(par1);
		this.tile = par1.getTile();
		extraData = new NBTTagCompound();
		if(tile != null)
		{
			tile.onGuiConstructed(this);
		}
	}
	
	public GuiInventoryCore setupExtraGuiObjects(int amount)
	{
		extraObjects = new GuiObject[amount];
		for(int i = 0;i < amount;i++)
		{
			extraObjects[i] = new GuiObject();
		}
		return this;
	}
	
	public GuiInventoryCore setAutoDrawing()
	{
		this.autoDrawing = true;
		return this;
	}
	
	public GuiInventoryCore setNoDefiningRequired()
	{
		defined = true;
		return this;
	}
	
	public GuiInventoryCore setAutoDrawingDissabled()
	{
		this.autoDrawing = false;
		return this;
	}
	
	public void defineSlot(int x, int y)
	{
		this.x = x;
		this.y = y;
		defined = true;
	}
	
	public void defineSlot(String key)
	{
		int[] data = engine.getGuiPos(key);
		if(data != null && data.length == 2)
		{
			defineSlot(data[0], data[1]);
		}
	}
	
	public void defineSlot(String key, int x, int y)
	{
		int[] data = engine.getGuiPos(key);
		if(data != null && data.length == 2)
		{
			defineSlot(data[0] + x, data[1] + y);
		}
	}
	
	public void drawSlot(Slot par1)
	{
		this.drawSlot(par1.xDisplayPosition, par1.yDisplayPosition);
	}
	
	public void renderItem(ItemStack par1, int x, int y)
	{
		GL11.glTranslatef(0.0F, 0.0F, 32.0F);
		this.zLevel = 200.0F;
		itemRenderer.zLevel = 200.0F;
		try
		{
			itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, this.mc.getTextureManager(), par1, x, y);
			itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.getTextureManager(), par1, x, y);
		}
		catch(Throwable e)
		{
		}
		this.zLevel = 0.0F;
		itemRenderer.zLevel = 0.0F;
	}
	
	public void drawTankSlot(TankSlot par1, int x, int y)
	{
		this.defineSlot("Tank");
		this.drawSlotPros(par1.getXCoord(), par1.getYCoord(), 18, 61);
		drawFluidInTank(par1, 18, 60);
	}
	
	public void drawFluidInTank(TankSlot par1, int x, int y)
	{
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		FluidTank tank = par1.getTank();
		int tankX = par1.getXCoord();
		int tankY = par1.getYCoord();
		if(tank == null)
		{
			return;
		}
		int prepair = tank.getCapacity() / 58;
		int squaled = tank.getFluidAmount() / prepair;
		FluidStack liquid = tank.getFluid();
		if(liquid == null || liquid.getFluid() == null)
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
		if(texture != null)
		{
			while(true)
			{
				int z;
				if(squaled > 16)
				{
					z = 16;
					squaled -= 16;
				}
				else
				{
					z = squaled;
					squaled = 0;
				}
				
				drawTexturedModelRectFromIcon(k + tankX, l + tankY + 58 - z - start, texture, 16, 16 - (16 - z));
				start += 16;
				
				if((z == 0) || (squaled == 0))
				{
					break;
				}
			}
		}
		setTexture(engine.getTexture("Objects"));
		this.defineSlot("TankOverlay");
		drawTexturedModalRect(k + tankX - 2, l + tankY - 1, this.x, this.y, x, y);
	}
	
	public void drawSlot(int SlotX, int SlotY)
	{
		if(defined)
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
		if(defined)
		{
			int var5 = (width - this.xSize) / 2;
			int var6 = (height - this.ySize) / 2;
			drawTexturedModalRect(var5 + SlotX - 1, var6 + SlotY - 1, this.x, this.y, xSize, ySize);
		}
		else
		{
			SpmodAPI.log.print("Slot Texture is not defined");
		}
	}
	
	public void drawSlotPros(int SlotX, int SlotY, int offsetX, int offsetY, int xSize, int ySize)
	{
		if(defined)
		{
			int var5 = (width - this.xSize) / 2;
			int var6 = (height - this.ySize) / 2;
			drawTexturedModalRect(var5 + SlotX - 1, var6 + SlotY - 1, this.x + offsetX, this.y + offsetY, xSize, ySize);
		}
		else
		{
			SpmodAPI.log.print("Slot Texture is not defined");
		}
	}
	
	public void drawSlots(int x, int y)
	{
		setTexture(engine.getTexture("Objects"));
		List<SpmodSlot> slotToDraw = ((AdvContainer)this.inventorySlots).getAllSlots();
		for(int i = 0;i < slotToDraw.size();i++)
		{
			SpmodSlot slot = slotToDraw.get(i);
			this.drawSlot(slot);
		}
		List<PlayerSlot> playerSlot = ((AdvContainer)this.inventorySlots).getPlayerSlots();
		for(int i = 0;i < playerSlot.size();i++)
		{
			PlayerSlot slot = playerSlot.get(i);
			this.drawSlot(slot);
		}
		ArrayList<TankSlot> tankSlots = ((AdvContainer)this.inventorySlots).getTanks();
		for(int i = 0;i < tankSlots.size();i++)
		{
			TankSlot slot = tankSlots.get(i);
			this.drawTankSlot(slot, x, y);
		}
		ArrayList<EnergySlot> energySlots = ((AdvContainer)this.inventorySlots).getEnergyBuffers();
		for(int i = 0;i<energySlots.size();i++)
		{
			EnergySlot slot = energySlots.get(i);
			this.drawEnergySlot(slot, x, y);
		}
	}
	
	private void drawEnergySlot(EnergySlot slot, int x2, int y2)
	{
		
	}

	protected void drawSlotInfo(int x, int y)
	{
		List<SpmodSlot> slotToDraw = ((AdvContainer)this.inventorySlots).getAllSlots();
		for(int i = 0;i < slotToDraw.size();i++)
		{
			SpmodSlot slot = slotToDraw.get(i);
			if(this.isMouseOverSlot(slot, x, y) && isCtrlKeyDown() && slot.hasUsage())
			{
				this.drawHoveringText(slot.getUsage(), x, y, fontRenderer);
			}
		}
		ArrayList<TankSlot> tankSlots = ((AdvContainer)this.inventorySlots).getTanks();
		for(int i = 0;i < tankSlots.size();i++)
		{
			TankSlot slot = tankSlots.get(i);
			if(isMouseOverTankSlot(slot, x, y))
			{
				this.drawHoveringText(slot.getTankInfo(), x, y, fontRenderer);
			}
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		ResourceLocation resource = getGuiTexture();
		setTexture(resource);
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
		if(autoDrawing)
		{
			this.defineSlot("Slot");
			drawSlots(par2, par3);
		}
		if(autoDrawing)
		{
			setTexture(resource);
		}
		if(tile != null)
		{
			tile.drawExtras(this, k, l, par2, par3);
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		String s = "";
		if(tile != null)
		{
			s = tile.getInvName();
		}
		int offsetX = 0;
		int offsetY = 0;
		int offsetCX = 0;
		int offsetCY = 0;
		int nameColor = 0;
		
		if(tile != null)
		{
			offsetX = tile.getNameXOffset();
			offsetY = tile.getNameYOffset();
			offsetCX = tile.getInvNameXOffset();
			offsetCY = tile.getInvNameYOffset();
			nameColor = tile.getNameColor();
		}
		this.fontRenderer.drawString(s, (this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2) + offsetX, 4 + offsetY, nameColor);
		if(tile.renderInnerInv() || tile.renderOuterInv())
		{
			this.fontRenderer.drawString(I18n.getString("container.inventory"), 8 + offsetCX, this.ySize - 96 + 2 + offsetCY, nameColor);
		}
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		if(tile != null)
		{
			tile.drawFrontExtras(this, k, l, par1, par2);
		}
        GL11.glTranslatef((float)-k, (float)-l, 0.0F);
		drawSlotInfo(par1, par2);
		GL11.glTranslatef((float)k, (float)l, 0.0F);
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		if(tile != null)
		{
			tile.onGuiLoad(this, k, l);
		}
	}
	
	@Override
	protected void keyTyped(char par1, int par2)
	{
		if(tile != null && tile.onKeyTyped(this, par1, par2))
		{
			return;
		}
		super.keyTyped(par1, par2);
	}
	
	
	
	@Override
	public void handleMouseInput()
	{
		super.handleMouseInput();
        int i = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int j = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        for(int k = 0;k<this.buttonList.size();k++)
        {
        	GuiButton button = (GuiButton)this.buttonList.get(k);
        	if(button != null && button instanceof SpmodGuiButton)
        	{
        		SpmodGuiButton spmodButton = (SpmodGuiButton)button;
        		if(spmodButton.isMouseOverMe(i, j))
        		{
        			spmodButton.handleMouseInput(i, j);
        		}
        	}
        }
	}

	protected boolean isMouseOverSlot(SpmodSlot par1, int x, int y)
	{
		return this.isPointInRegion(par1.xDisplayPosition, par1.yDisplayPosition, 16, 16, x, y);
	}
	
	protected boolean isMouseOverTankSlot(TankSlot par1, int x, int y)
	{
		return this.isPointInRegion(par1.getXCoord(), par1.getYCoord(), 18, 60, x, y);
	}
	
	public void drawItemStackTooltip(ItemStack par1, int x, int y)
	{
		super.drawItemStackTooltip(par1, x, y);
	}
	
	@Override
	public boolean isPointInRegion(int par1, int par2, int par3, int par4, int par5, int par6)
	{
		return super.isPointInRegion(par1, par2, par3, par4, par5, par6);
	}
	
	@Override
	public void actionPerformed(GuiButton par1GuiButton)
	{
		currentButton = par1GuiButton;
		if(tile != null)
		{
			tile.onButtonClick(this, par1GuiButton);
		}
		
	}
	
	public void onButtonUpdate(GuiButton par1)
	{
		if(tile != null)
		{
			tile.onButtonUpdate(this, par1);
		}
	}
	
	@Override
	protected void mouseMovedOrUp(int par1, int par2, int par3)
	{
		super.mouseMovedOrUp(par1, par2, par3);
        if (this.currentButton != null && par3 == 0)
        {
            this.currentButton.mouseReleased(par1, par2);
            this.currentButton = null;
        }
	}
	
	public void releaseButton(GuiButton par1)
	{
		if(tile != null)
		{
			tile.onButtonReleased(this, par1);
		}
	}

	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int getGuiX()
	{
		return (this.width - this.xSize) / 2;
	}
	
	public int getGuiY()
	{
		return (this.height - this.ySize) / 2;
	}
	
	public FontRenderer getFontRenderer()
	{
		return this.fontRenderer;
	}
	
	public RenderItem getItemRenderer()
	{
		return this.itemRenderer;
	}
	
	public void setTexture(ResourceLocation par1)
	{
		if(par1 == null)
		{
			return;
		}
		this.mc.getTextureManager().bindTexture(par1);
	}
	
	public void setX(int x)
	{
		this.xSize = x;
	}
	
	public void setY(int y)
	{
		this.ySize = y;
	}
	
	public Minecraft getMC()
	{
		return this.mc;
	}
	
	public List<GuiButton> getButtonsList()
	{
		return this.buttonList;
	}
	
	/**
	 * instead of declaring new variables inside of the TileEntity/New GuiClass
	 * that is based on this you use the NBTData which you can access in the gui
	 * which is only client based!
	 */
	public NBTTagCompound getExtraData()
	{
		return extraData;
	}
	
	public EntityPlayer getPlayer()
	{
		return this.mc.thePlayer;
	}
	
	public IAdvTile getTile()
	{
		return tile;
	}
	
	public GuiObject getGuiObject(int par1)
	{
		return extraObjects[par1];
	}
	
	public GuiObject[] getGuiObjects()
	{
		return extraObjects;
	}
	
	public ResourceLocation getGuiTexture()
	{
		if(tile != null)
		{
			return tile.getTexture();
		}
		return null;
	}
	
	public static class GuiObject
	{
		Gui object;
		
		public void setObject(Gui par1)
		{
			object = par1;
		}
		
		public Gui getObject()
		{
			return object;
		}
		
		public <T> T getObject(Class<T> par1)
		{
			return (T)object;
		}
	}
	
}
