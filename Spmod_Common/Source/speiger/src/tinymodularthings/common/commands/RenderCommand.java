package speiger.src.tinymodularthings.common.commands;

import java.io.DataInput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

import net.minecraft.command.ICommandSender;
import speiger.src.api.packets.IPacketReciver;
import speiger.src.api.packets.SpmodPacketHelper;
import speiger.src.api.packets.SpmodPacketHelper.ModularPacket;
import speiger.src.api.packets.SpmodPacketHelper.PacketType;
import speiger.src.spmodapi.common.command.CommandRegistry;
import speiger.src.spmodapi.common.command.ISpmodCommand;
import speiger.src.spmodapi.common.command.ISubCommand;
import speiger.src.spmodapi.common.command.commands.BasicSubCommand;
import speiger.src.spmodapi.common.util.TileIconMaker;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.blocks.storage.TinyTank;

public class RenderCommand implements ISpmodCommand, IPacketReciver
{
	public RenderCommand()
	{
		SpmodPacketHelper.getHelper().registerPacketReciver(this);
		CommandRegistry.getInstance().registerCommand(this);
	}
	
	
	@Override
	public String getCommandName()
	{
		return "Render";
	}
	
	@Override
	public List<String> getCommandUsage()
	{
		return Arrays.asList("Change State of Renderring");
	}
	
	@Override
	public ArrayList<ISubCommand> getSubCommands()
	{
		ArrayList<ISubCommand> sub = new ArrayList<ISubCommand>();
		sub.add(new BasicSubCommand("State", "Returns the Info about the Rendering"));
		sub.add(new BasicSubCommand("Change", "Change Render State from Fancy to Basic", "Put a true or false at the end to change", "Possible Rendernames:", "TinyTank"));
		return sub;
	}
	
	@Override
	public boolean isCommandRunnable(ICommandSender par1, boolean guiAdding, ISubCommand sub, String[] arg)
	{
		if(guiAdding)
		{
			return true;
		}
		String name = sub.getSubCommandName();
		if(name.equalsIgnoreCase("State"))
		{
			return arg.length == 1 && arg[0].equalsIgnoreCase("TinyTank");
		}
		else if(name.equalsIgnoreCase("Change"))
		{
			if(arg.length == 2)
			{
				if(arg[1].equalsIgnoreCase("true") || arg[1].equalsIgnoreCase("false"))
				{
					if(arg[0].equalsIgnoreCase("TinyTank"))
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public void runCommand(ICommandSender par1, ISubCommand sub, String[] arg)
	{
		if(sub != null)
		{
			String name = sub.getSubCommandName();
			int id = -1;
			if(name.equalsIgnoreCase("State"))
			{
				id = 0;
			}
			else if(name.equalsIgnoreCase("Change"))
			{
				id = 1;
			}
			
			if(id != -1)
			{
				ModularPacket packet = new ModularPacket(TinyModularThings.instance, PacketType.Custom, this.identifier());
				packet.InjectNumber(id);
				packet.InjectStrings(arg);
				PacketDispatcher.sendPacketToPlayer(packet.finishPacket(), (Player) FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(par1.getCommandSenderName()));
			}
			
		}
	}

	@Override
	public void recivePacket(DataInput par1)
	{
		try
		{
			int id = par1.readInt();
			if(id == 0)
			{
				String key = par1.readUTF();
				if(key.equalsIgnoreCase("TinyTank"))
				{
					boolean active = ((TinyTank)TileIconMaker.getIconMaker().getTileEntityFromClass(TinyTank.class)).dissableRenderer;
					if(active)
					{
						FMLClientHandler.instance().getClient().thePlayer.sendChatMessage("Fancy Models Dissabled");
					}
					else
					{
						FMLClientHandler.instance().getClient().thePlayer.sendChatMessage("Fancy Models enabled");
					}
				}
			}
			else if(id == 1)
			{
				String key = par1.readUTF();
				String value = par1.readUTF();
				if(key.equalsIgnoreCase("TinyTank"))
				{
					boolean par2 = Boolean.parseBoolean(value);
					((TinyTank)TileIconMaker.getIconMaker().getTileEntityFromClass(TinyTank.class)).dissableRenderer = par2;
					
					FMLClientHandler.instance().getClient().thePlayer.sendChatMessage("Set Rendermode to: "+par2);
				}
			}
		}
		catch (Exception e)
		{
			FMLClientHandler.instance().getClient().thePlayer.sendChatMessage("Something did happend on RenderChanging");
		}
	}

	@Override
	public String identifier()
	{
		return "command.render";
	}
	
	
	
}
