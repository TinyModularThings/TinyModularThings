package speiger.src.spmodapi.common.command.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import speiger.src.api.common.registry.helpers.SpmodMod;
import speiger.src.api.common.registry.helpers.Ticks.ITickReader;
import speiger.src.api.common.utils.MathUtils;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.command.CommandRegistry;
import speiger.src.spmodapi.common.command.ISpmodCommand;
import speiger.src.spmodapi.common.command.ISubCommand;
import speiger.src.spmodapi.common.util.proxy.LangProxy;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class CommandServerTimer implements ISpmodCommand, ITickReader
{
	private long lastTime = 0;
	public String password = "";
	public boolean isActive = false;
	public int time = 0;
	
	public CommandServerTimer()
	{
		CommandRegistry.getInstance().registerCommand(this);
	}
	
	@Override
	public String getCommandName()
	{
		return "Shutdown Timer";
	}
	
	@Override
	public List<String> getCommandUsage()
	{
		return Arrays.asList("Set the timer to shutdown the server automaticly");
	}
	
	@Override
	public ArrayList<ISubCommand> getSubCommands()
	{
		ArrayList<ISubCommand> sub = new ArrayList<ISubCommand>();
		sub.add(new BasicSubCommand("Remove", "Removes the current shutdown sequens", "<password if setted up>"));
		sub.add(new BasicSubCommand("Set", "set a Shutdown sequenz", "<Ticks>", "<Hours, Minutes, Seconds>", "<Ticks, Password>", "<Hours, Minutes, Seconds, Password>"));
		sub.add(new BasicSubCommand("Info", "Shows only how much time is left if a counter runs"));
		return sub;
	}
	
	@Override
	public boolean isCommandRunnable(ICommandSender par1, boolean guiAdding, ISubCommand sub, String[] arg)
	{
		if (guiAdding)
		{
			return true;
		}
		
		if (sub != null)
		{
			String name = sub.getSubCommandName();
			if (name.equalsIgnoreCase("Remove"))
			{
				if (isActive)
				{
					if ((password.equalsIgnoreCase("")) || (!password.equalsIgnoreCase("") && arg.length == 1 && password.equalsIgnoreCase(arg[0])))
					{
						return true;
					}
				}
			}
			else if (name.equalsIgnoreCase("Info"))
			{
				return true;
			}
			else if (name.equalsIgnoreCase("Set"))
			{
				if (arg.length > 0 && arg.length <= 4)
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public void runCommand(ICommandSender par1, ISubCommand sub, String[] arg)
	{
		ServerConfigurationManager manager = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager();
		
		if (sub != null && manager.isPlayerOpped(par1.getCommandSenderName()))
		{
			String name = sub.getSubCommandName();
			
			if (name.equalsIgnoreCase("Set"))
			{
				int setup = 0;
				String key = "";
				switch (arg.length)
				{
					case 1:
						setup = Integer.parseInt(arg[0]);
						break;
					case 2:
						setup = Integer.parseInt(arg[0]);
						key = arg[1];
						break;
					case 3:
						int h = Integer.parseInt(arg[0]);
						int min = Integer.parseInt(arg[1]);
						int sek = Integer.parseInt(arg[2]);
						setup = ((sek + (min * 60) + (h * 3600)) * 20);
						break;
					case 4:
						int hour = Integer.parseInt(arg[0]);
						int minutes = Integer.parseInt(arg[1]);
						int sekund = Integer.parseInt(arg[2]);
						setup = ((sekund + (minutes * 60) + (hour * 3600)) * 20);
						key = arg[3];
						break;
				}
				
				if (setup > 0)
				{
					time = setup;
					password = key;
					isActive = true;
					manager.sendChatMsg(LangProxy.getText(par1.getCommandSenderName() + " Setted up a Shutdown timer: " + MathUtils.getTicksInTime(setup)));
					Set players = manager.getOps();
					
					if (players != null)
					{
						Iterator iter = players.iterator();
						for (; iter.hasNext();)
						{
							EntityPlayerMP mp = manager.getPlayerForUsername((String) iter.next());
							if (mp != null)
							{
								if (key.equalsIgnoreCase(""))
								{
									mp.playerNetServerHandler.sendPacketToPlayer(new Packet3Chat(LangProxy.getText("No Password was setted Up"), true));
								}
								else
								{
									mp.playerNetServerHandler.sendPacketToPlayer(new Packet3Chat(LangProxy.getText("Password: " + key), true));
								}
							}
						}
					}
				}
			}
			else if (name.equalsIgnoreCase("Remove"))
			{
				boolean flag = false;
				if (password.equalsIgnoreCase(""))
				{
					isActive = false;
					password = "";
					time = 0;
					flag = true;
				}
				else
				{
					if (arg.length == 1 && password.equalsIgnoreCase(arg[0]))
					{
						isActive = false;
						password = "";
						time = 0;
						flag = true;
					}
				}
				if (flag)
				{
					manager.sendChatMsg(LangProxy.getText(par1.getCommandSenderName() + " Removed Timer"));
				}
			}
			else if (name.equalsIgnoreCase("Info"))
			{
				if (isActive)
				{
					par1.sendChatToPlayer(LangProxy.getText("Time left: " + MathUtils.getTicksInTime(time)));
					if (manager.isPlayerOpped(par1.getCommandSenderName()))
					{
						par1.sendChatToPlayer(LangProxy.getText("Password: " + password));
					}
				}
				else
				{
					par1.sendChatToPlayer(LangProxy.getText("No Countdown is running"));
				}
			}
		}
		
	}
	
	@Override
	public void onTick(SpmodMod sender, Side side)
	{
		if (side != side.SERVER)
		{
			return;
		}
		
		if (isActive)
		{
			if (time > 0)
			{
				time--;
				if (time <= 0)
				{
					MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
					server.initiateShutdown();
				}
			}
		}
	}
	
	@Override
	public SpmodMod getOwner()
	{
		return SpmodAPI.instance;
	}

	@Override
	public boolean needTick()
	{
		return isActive;
	}
	
}
