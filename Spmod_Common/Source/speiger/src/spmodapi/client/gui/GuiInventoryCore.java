package speiger.src.spmodapi.client.gui;

import java.util.ArrayList;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import speiger.src.api.common.registry.helpers.SpmodMod;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import speiger.src.tinymodularthings.TinyModularThings;

public abstract class GuiInventoryCore extends GuiContainer
{
	
	boolean defined = false;
	int x = 0;
	int y = 0;
	
	public SpmodMod getCore()
	{
		return TinyModularThings.instance;
	}
	
	public SpmodMod getSpmodCore()
	{
		return SpmodAPI.instance;
	}
	
	public GuiInventoryCore(Container par1Container)
	{
		super(par1Container);
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
	
	public void drawSlots()
	{
		ArrayList<Slot> slotToDraw = ((AdvContainer) this.inventorySlots).getAllSlots();
		for (int i = 0; i < slotToDraw.size(); i++)
		{
			this.drawSlot(slotToDraw.get(i));
		}
	}
	
}
