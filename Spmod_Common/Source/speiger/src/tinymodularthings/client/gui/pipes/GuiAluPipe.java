package speiger.src.tinymodularthings.client.gui.pipes;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import speiger.src.api.language.LanguageRegister;
import speiger.src.api.packets.SpmodPacketHelper.ModularPacket;
import speiger.src.api.packets.SpmodPacketHelper.PacketType;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;
import speiger.src.tinymodularthings.common.pipes.AluFluidExtractionPipe;
import cpw.mods.fml.common.network.PacketDispatcher;


public class GuiAluPipe extends GuiContainer
{
	AluFluidExtractionPipe pipe;
	GuiTextField text; 
	boolean loop;
	
	public GuiAluPipe(AluFluidExtractionPipe par1)
	{
		super(new Container(){
			@Override
			public boolean canInteractWith(EntityPlayer entityplayer)
			{
				return true;
			}
		});
		pipe = par1;
	}
	
	private static final ResourceLocation furnaceGuiTextures = new ResourceLocation(TinyModularThingsLib.ModID.toLowerCase() + ":textures/gui/pipes/BronzePipe.png");
	
	
	
	
	@Override
	public void initGui()
	{
		super.initGui();
		loop = pipe.continueless;
		text = new GuiTextField(this.fontRenderer, 60, 20, 50, 20);
		text.setText(""+pipe.FluidSetup);
		text.setEnabled(true);
		text.setFocused(true);
		text.setCanLoseFocus(true);
	}




	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		text.drawTextBox();
		this.buttonList.clear();
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.buttonList.add(new GuiButton(0, 10+k, 50+l, 40, 20, "Cancel"));
		this.buttonList.add(new GuiButton(2, 120+k, 50+l, 40, 20, "Setup"));
		this.buttonList.add(new GuiButton(1, 60+k, 50+l, 50, 20, loop ? "Loop" : "Not Loop"));
	}

	


	@Override
	protected void keyTyped(char par1, int par2)
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_BACK))
		{
			text.textboxKeyTyped(par1, par2);
		}
		if(Character.getNumericValue(par1) == -1 || Character.isLetter(par1))
		{
			super.keyTyped(par1, par2);
		}
		else
		{
			text.textboxKeyTyped(par1, par2);
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
		if(par1.id == 0)
		{
			this.mc.displayGuiScreen(null);
		}
		else if(par1.id == 1)
		{
			loop = !loop;
		}
		else if(par1.id == 2)
		{
			if(text.getText().length() == 0)
			{
				this.mc.thePlayer.sendChatToPlayer(LanguageRegister.createChatMessage("You need a number"));
				return;
			}
			int number = Integer.parseInt(text.getText());
			if(number > 1000 || number < 1)
			{
				this.mc.thePlayer.sendChatToPlayer(LanguageRegister.createChatMessage("Number must be between 1 and 1000"));
			}
			else
			{
				finishPacket();
			}
		}
	}
	
	public void finishPacket()
	{
		ModularPacket modul = new ModularPacket(TinyModularThings.instance, PacketType.Custom, "TMTAluExtractionPipe");
		modul.InjectNumbers(pipe.getWorld().provider.dimensionId, pipe.getContainer().xCoord, pipe.getContainer().yCoord, pipe.getContainer().zCoord);
		modul.injectBoolean(loop);
		modul.InjectNumber(Integer.parseInt(text.getText()));
		
		PacketDispatcher.sendPacketToServer(modul.finishPacket());
		this.mc.thePlayer.sendChatToPlayer(LanguageRegister.createChatMessage("Setted up extracting: "+Integer.parseInt(text.getText())+"mB "));
		this.mc.thePlayer.sendChatToPlayer(LanguageRegister.createChatMessage(loop ? "AutoLoop Active" : "AutoLoop Deactive"));
		this.mc.thePlayer.closeScreen();
	}
	
}
