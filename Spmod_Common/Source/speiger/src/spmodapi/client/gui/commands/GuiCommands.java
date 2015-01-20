package speiger.src.spmodapi.client.gui.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import speiger.src.api.common.data.packets.SpmodPacketHelper.ModularPacket;
import speiger.src.api.common.data.packets.SpmodPacketHelper.PacketType;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.command.CommandRegistry;
import speiger.src.spmodapi.common.command.ISpmodCommand;
import speiger.src.spmodapi.common.command.ISubCommand;
import speiger.src.spmodapi.common.lib.SpmodAPILib;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import cpw.mods.fml.common.network.PacketDispatcher;

public class GuiCommands extends GuiContainer
{
	EntityPlayer sender;
	ArrayList<ISpmodCommand> pCommand = new ArrayList<ISpmodCommand>();
	HashMap<ISpmodCommand, ArrayList<ISubCommand>> sub = new HashMap<ISpmodCommand, ArrayList<ISubCommand>>();
	ISpmodCommand[] commands = new ISpmodCommand[4];
	ISpmodCommand subCommand = null;
	ISubCommand[] subCommands = new ISubCommand[4];
	GuiTextField text;
	int totalID = 0;
	int totalSub = 0;
	int choosenCom = -1;
	int choosenSubCom = -1;
	boolean lastStep = false;
	int timer = 0;
	boolean activeInfo = false;
	
	public GuiCommands(InventoryPlayer par1)
	{
		super(new AdvContainer(par1).setOffset(16, 59).setInventory(par1));
		sender = par1.player;
		this.ySize = 224;
		this.xSize = 210;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		pCommand.addAll(CommandRegistry.getInstance().getCommands(sender));
		for (ISpmodCommand cu : pCommand)
		{
			ArrayList<ISubCommand> between = CommandRegistry.getInstance().getSubCommands(sender, cu);
			if (!between.isEmpty())
			{
				sub.put(cu, between);
			}
		}
		
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		text = new GuiTextField(this.fontRenderer, 30, 20, 140, 20);
		
		this.reloadCommands();
	}
	
	public void reloadCommands()
	{
		
		int sub = this.totalSub * 4;
		this.commands = new ISpmodCommand[4];
		this.subCommands = new ISubCommand[4];
		
		if (subCommand != null)
		{
			ArrayList<ISubCommand> com = this.sub.get(subCommand);
			if (sub < com.size())
			{
				
				subCommands[0] = com.get(sub);
			}
			if (sub + 1 < com.size())
			{
				subCommands[1] = com.get(sub + 1);
			}
			if (sub + 2 < com.size())
			{
				subCommands[2] = com.get(sub + 2);
			}
			if (sub + 3 < com.size())
			{
				subCommands[3] = com.get(sub + 3);
			}
		}
		int id = totalID * 4;
		if (id < pCommand.size())
		{
			commands[0] = pCommand.get(id);
		}
		if (id + 1 < pCommand.size())
		{
			commands[1] = pCommand.get(id + 1);
		}
		if (id + 2 < pCommand.size())
		{
			commands[2] = pCommand.get(id + 2);
		}
		if (id + 3 < pCommand.size())
		{
			commands[3] = pCommand.get(id + 3);
		}
		
	}
	
	@Override
	protected void keyTyped(char par1, int par2)
	{
		if (Character.getNumericValue(par1) == -1)
		{
			super.keyTyped(par1, par2);
		}
		if (this.lastStep)
		{
			text.textboxKeyTyped(par1, par2);
		}
	}
	
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		String s = "Command Gui";
		this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		this.fontRenderer.drawString(I18n.getString("container.inventory"), 25, this.ySize - 96 + 2, 4210752);
		
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		// this.fontRenderer.drawString("ID: "+this.totalID, 10, 10, 4210752);
		
		this.buttonList.clear();
		
		if (this.lastStep)
		{
			this.buttonList.add(new GuiButton(7, 120 + k, 108 + l, 50, 20, "Run"));
			this.buttonList.add(new GuiButton(2, 30 + k, 108 + l, 50, 20, "Back"));
			
			List<String> text = new ArrayList<String>();
			
			if (subCommand != null)
			{
				text.addAll(subCommands[this.choosenSubCom].getSubCommandDescription());
			}
			else
			{
				text.addAll(commands[this.choosenCom].getCommandUsage());
			}
			
			if (!text.isEmpty())
			{
				buttonList.add(new GuiButton(8, 30 + k, 70 + l, 50, 20, "Info"));
				if (this.activeInfo && this.timer > 0)
				{
					timer--;
					if (isInRange(par1 - k, par2 - l))
					{
						this.drawHoveringText(text, par1 - k, par2 - l, this.fontRenderer);
					}
				}
				else if (activeInfo && timer == 0)
				{
					activeInfo = false;
				}
			}
			
		}
		else
		{
			this.buttonList.add(new GuiButton(0, 165 + k, 108 + l, 20, 20, "+"));
			this.buttonList.add(new GuiButton(1, 14 + k, 108 + l, 20, 20, "-"));
			if (this.subCommand != null)
			{
				this.buttonList.add(new GuiButton(2, 75 + k, 108 + l, 50, 20, "Back"));
			}
			
			if (this.subCommand != null)
			{
				if (this.subCommands[0] != null)
				{
					this.buttonList.add(new GuiButton(3, 28 + k, 18 + l, 140, 20, subCommand.getCommandName() + ": " + subCommands[0].getSubCommandName()));
				}
				if (this.subCommands[1] != null)
				{
					this.buttonList.add(new GuiButton(4, 28 + k, 40 + l, 140, 20, subCommand.getCommandName() + ": " + subCommands[1].getSubCommandName()));
				}
				if (this.subCommands[2] != null)
				{
					this.buttonList.add(new GuiButton(5, 28 + k, 62 + l, 140, 20, subCommand.getCommandName() + ": " + subCommands[2].getSubCommandName()));
				}
				if (this.subCommands[3] != null)
				{
					this.buttonList.add(new GuiButton(6, 28 + k, 84 + l, 140, 20, subCommand.getCommandName() + ": " + subCommands[3].getSubCommandName()));
				}
			}
			else
			{
				if (this.commands[0] != null)
				{
					this.buttonList.add(new GuiButton(3, 28 + k, 18 + l, 140, 20, commands[0].getCommandName()));
				}
				if (this.commands[1] != null)
				{
					this.buttonList.add(new GuiButton(4, 28 + k, 40 + l, 140, 20, commands[1].getCommandName()));
				}
				if (this.commands[2] != null)
				{
					this.buttonList.add(new GuiButton(5, 28 + k, 62 + l, 140, 20, commands[2].getCommandName()));
				}
				if (this.commands[3] != null)
				{
					this.buttonList.add(new GuiButton(6, 28 + k, 84 + l, 140, 20, commands[3].getCommandName()));
				}
			}
		}
		if (lastStep)
		{
			text.drawTextBox();
		}
		text.setEnabled(lastStep);
		text.setFocused(lastStep);
		text.setCanLoseFocus(lastStep);
		
	}
	
	public static final ResourceLocation furnaceGuiTextures = new ResourceLocation(SpmodAPILib.ModID.toLowerCase() + ":textures/gui/commands/CommandsGui.png");
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
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
		int id = par1.id;
		
		if (id == 0)
		{
			if (this.subCommand == null)
			{
				if ((this.totalID + 1) * 4 < this.pCommand.size())
				{
					totalID++;
				}
			}
			else
			{
				if ((totalSub + 1) * 4 < this.sub.get(subCommand).size())
				{
					totalSub++;
				}
			}
		}
		else if (id == 1)
		{
			if (subCommand == null)
			{
				if (this.totalID > 0)
				{
					this.totalID--;
				}
			}
			else
			{
				if (this.totalSub > 0)
				{
					totalSub--;
				}
			}
		}
		else if (id == 2)
		{
			if (this.subCommand != null)
			{
				this.subCommand = null;
				this.choosenCom = -1;
				this.choosenSubCom = -1;
				this.totalID = 0;
				this.totalSub = 0;
				this.lastStep = false;
			}
			else
			{
				this.choosenCom = -1;
				this.choosenSubCom = -1;
				this.totalID = 0;
				this.totalSub = 0;
				this.lastStep = false;
				
			}
			text.setText("");
		}
		else if (id == 8)
		{
			this.activeInfo = true;
			this.timer = 1000;
		}
		else if (id == 7)
		{
			String[] string = text.getText().split(" ");
			int length = string.length;
			if (text.getText().length() == 0)
			{
				length = 0;
			}
			ModularPacket packet = new ModularPacket(SpmodAPI.instance, PacketType.Custom, "Command.Reciver");
			packet.InjectNumber(sender.worldObj.provider.dimensionId);
			packet.injetString(sender.username);
			packet.InjectNumbers(this.subCommand == null ? (byte) 0 : (byte) 1, (int) pCommand.indexOf(commands[this.choosenCom]), subCommand == null ? (int) 0 : (int) sub.get(subCommand).indexOf(subCommands[this.choosenSubCom]));
			packet.InjectNumber((Integer) length);
			packet.InjectStrings(string);
			PacketDispatcher.sendPacketToServer(packet.finishPacket());
			super.keyTyped('0', 1);
		}
		else
		{
			int button = id - 3;
			int total = this.totalID * 4;
			if (this.subCommand == null)
			{
				ISpmodCommand com = this.pCommand.get(total + button);
				if (!com.getSubCommands().isEmpty())
				{
					this.subCommand = com;
					this.choosenCom = button;
				}
				else
				{
					this.choosenCom = button;
					this.subCommand = null;
					this.lastStep = true;
				}
			}
			else
			{
				this.choosenSubCom = button;
				this.lastStep = true;
			}
		}
		
		reloadCommands();
	}
	
	public boolean isInRange(int i, int j)
	{
		if (i >= 30 && i <= 79 && j >= 70 && j <= 89)
		{
			return true;
		}
		return false;
	}
	
}
