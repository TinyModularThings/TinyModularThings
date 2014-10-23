package speiger.src.tinymodularthings.client.gui.machine;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import speiger.src.api.language.LanguageRegister;
import speiger.src.api.packets.SpmodPacketHelper;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.blocks.machine.BucketFillerBasic;
import speiger.src.tinymodularthings.common.blocks.machine.InventoryBucketFiller;
import speiger.src.tinymodularthings.common.blocks.machine.SelfPoweredBucketFiller;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;
import cpw.mods.fml.common.network.PacketDispatcher;

public class BucketFillerGui extends GuiInventoryCore
{
	BucketFillerBasic basic;
	
	public BucketFillerGui(InventoryPlayer par1, BucketFillerBasic par2)
	{
		super(new InventoryBucketFiller(par1, par2));
		basic = par2;
	}
	
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		String name = LanguageRegister.getLanguageName(this, "bucketFiller.Basic", getCore());
		if (basic instanceof SelfPoweredBucketFiller)
		{
			name = LanguageRegister.getLanguageName(this, "self.bucketfiller", getCore());
		}
		
		this.fontRenderer.drawString(name, this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2, 6, 4210752);
		this.fontRenderer.drawString(I18n.getString("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
		
		this.buttonList.clear();
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.buttonList.add(new GuiButton(0, k + 15, l + 35, 30, 20, basic.drain ? "Drain" : "Fill"));
		
		int textID = getText(par1 - k, par2 - l);
		if(textID == 1)
		{
			FluidStack fluid = basic.tank.getFluid();
			List<String> text = Arrays.asList("");
			if(fluid == null || fluid.getFluid() == null)
			{
				text = Arrays.asList("Empty Tank", "0mB / "+basic.tank.getCapacity()+"mB");
			}
			else
			{
				String fluidName = fluid.getFluid().getName();
				String first = fluidName.substring(0, 1);
				fluidName = first.toUpperCase() + fluidName.substring(1);
				text = Arrays.asList("Stored Fluid: "+fluidName, fluid.amount+"mB / "+basic.tank.getCapacity()+"mB");
			}
			this.drawHoveringText(text, par1-k-80, par2-l+25, this.fontRenderer);
		}
	}
	
	private int getText(int x, int y)
	{
		if(x >= 122 && x <= 139 && y >= 18 && y <= 77)
		{
			return 1;
		}
		return 0;
	}

	public static final ResourceLocation gui = new ResourceLocation(TinyModularThingsLib.ModID.toLowerCase() + ":textures/gui/machine/bucket_filler_gui.png");
	private static final ResourceLocation BLOCK_TEXTURE = TextureMap.locationBlocksTexture;
	
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(gui);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
		
		displayGauge(k, l, 19, 123, basic.tank.getFluidAmount() / 275, basic.tank.getFluid());
		
		int ptotal = this.basic.progress / (basic instanceof SelfPoweredBucketFiller ? 7 : 9);
		
		this.drawTexturedModalRect(k + 75, l + 41, 176, 60, ptotal + 1, 16);
		
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		this.buttonList.clear();
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.buttonList.add(new GuiButton(0, k + 15, l + 35, 30, 20, basic.drain ? "Drain" : "Fill"));
		
	}
	
	@Override
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		super.actionPerformed(par1GuiButton);
		
		if (par1GuiButton.id == 0)
		{
			boolean oppo = !this.basic.drain;
			Packet packet = SpmodPacketHelper.getHelper().createNBTPacket(basic, TinyModularThings.instance).InjectNumber(oppo ? 0 : 1).finishPacket();
			if (packet != null)
			{
				PacketDispatcher.sendPacketToServer(packet);
			}
		}
		this.initGui();
	}
	
	private void displayGauge(int j, int k, int line, int col, int squaled, FluidStack liquid)
	{
		if (liquid == null)
		{
			return;
		}
		
		int start = 0;
		
		Icon liquidIcon = null;
		Fluid fluid = liquid.getFluid();
		if ((fluid != null) && (fluid.getStillIcon() != null))
		{
			liquidIcon = fluid.getStillIcon();
		}
		
		mc.renderEngine.bindTexture(BLOCK_TEXTURE);
		
		if (liquidIcon != null)
		{
			while (true)
			{
				int x;
				if (squaled > 16)
				{
					x = 16;
					squaled -= 16;
				}
				else
				{
					x = squaled;
					squaled = 0;
				}
				
				drawTexturedModelRectFromIcon(j + col, k + line + 58 - x - start, liquidIcon, 16, 16 - (16 - x));
				start += 16;
				
				if ((x == 0) || (squaled == 0))
				{
					break;
				}
			}
		}
		mc.renderEngine.bindTexture(gui);
		drawTexturedModalRect(j + col, k + line, 176, 0, 16, 60);
	}
	
}
