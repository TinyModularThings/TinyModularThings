package speiger.src.tinymodularthings.client.gui.transport;

import mods.railcraft.common.util.network.PacketDispatcher;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import speiger.src.api.items.InfoStack;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.packets.SpmodPacketHelper;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.common.util.BlockPosition;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import speiger.src.tinymodularthings.common.blocks.transport.MultiStructureItemInterface;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;

public class ItemInterfaceGui extends GuiInventoryCore
{
	MultiStructureItemInterface tile;
	
	public ItemInterfaceGui(MultiStructureItemInterface par1, InventoryPlayer par2)
	{
		super(new AdvContainer(par2)
		{
			@Override
			public boolean canInteractWith(EntityPlayer entityplayer)
			{
				return true;
			}
		});
		tile = par1;
	}
	
	private static final ResourceLocation furnaceGuiTextures = new ResourceLocation(TinyModularThingsLib.ModID.toLowerCase() + ":textures/gui/storage/StorageGui.png");
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		BlockPosition block = new BlockPosition(tile.worldObj, tile.x, tile.y, tile.z);
		if (block != null && block.doesBlockExsist() && block.hasTileEntity())
		{
			buttonList.add(new GuiButton(0, x + 40, y + 50, 20, 20, "-"));
			buttonList.add(new GuiButton(1, x + 105, y + 50, 20, 20, "+"));
		}
		
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		BlockPosition block = new BlockPosition(tile.worldObj, tile.x, tile.y, tile.z);
		if (block != null && block.doesBlockExsist() && block.hasTileEntity())
		{
			
			String target = block.getAsBlockStack().getBlockDisplayName();
			if (target != null)
			{
				String name = LanguageRegister.getLanguageName(new InfoStack(), "target", getCore());
				fontRenderer.drawString(name + ": " + target, xSize / 2 - fontRenderer.getStringWidth(name + ": " + target) / 2, 20, 4210752);
				String button = LanguageRegister.getLanguageName(this, "slot.selected", getCore());
				fontRenderer.drawString(button, 50, 35, 4210752);
				fontRenderer.drawString("" + tile.choosenSlot, 80, 56, 4210752);
			}
		}
		else
		{
			String name = LanguageRegister.getLanguageName(this, "no.target", getCore());
			fontRenderer.drawString(name, 60, 35, 4210752);
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(furnaceGuiTextures);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
	}
	
	@Override
	protected void actionPerformed(GuiButton par1)
	{
		BlockPosition block = new BlockPosition(tile.worldObj, tile.x, tile.y, tile.z);
		TileEntity newtile = block.getTileEntity();
		
		if (newtile != null && newtile instanceof IInventory)
		{
			IInventory inv = (IInventory) newtile;
			if (par1 != null)
			{
				int id = tile.choosenSlot;
				switch (par1.id)
				{
					case 0:
						if (id - 1 < 0)
						{
							id = inv.getSizeInventory() - 1;
						}
						else
						{
							id--;
						}
						break;
					case 1:
						if (id + 1 == inv.getSizeInventory())
						{
							id = 0;
						}
						else
						{
							id++;
						}
						break;
				}
				sendPacket(id);
			}
		}
	}
	
	public void sendPacket(int newID)
	{
		Packet packet = SpmodPacketHelper.getHelper().createNBTPacket(tile, getCore()).InjectNumber(newID).finishPacket();
		
		if (packet != null)
		{
			PacketDispatcher.sendPacketToServer(packet);
		}
		tile.choosenSlot = newID;
	}
	
}
