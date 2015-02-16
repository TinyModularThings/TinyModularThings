package speiger.src.spmodapi.common.command.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;
import speiger.src.spmodapi.common.command.ISpmodCommand;
import speiger.src.spmodapi.common.command.ISubCommand;
import speiger.src.spmodapi.common.handler.PlayerHandler;
import speiger.src.spmodapi.common.util.proxy.LangProxy;

public class CommandPlayerInfos implements ISpmodCommand
{
	
	@Override
	public String getCommandName()
	{
		return "Player Infos";
	}
	
	@Override
	public List<String> getCommandUsage()
	{
		return Arrays.asList("Player Infos");
	}
	
	@Override
	public ArrayList<ISubCommand> getSubCommands()
	{
		ArrayList<ISubCommand> com = new ArrayList<ISubCommand>();
		com.add(new BasicSubCommand("Kill Create", "<True/False>", "Enable/Dissable Animal Gas Spawn on Living Death, By you"));
		com.add(new BasicSubCommand("Craft Make Hunger", "<True/False>", "Enables that the Player Gets More Hungry"));
		return com;
	}
	
	@Override
	public boolean isCommandRunnable(ICommandSender par1, boolean guiAdding, ISubCommand sub, String[] arg)
	{
		if(guiAdding)
		{
			return true;
		}
		if(sub != null)
		{
			if(sub.getSubCommandName().equalsIgnoreCase("Kill Create"))
			{
				if(arg.length == 1 && (arg[0].equalsIgnoreCase("True") || arg[0].equalsIgnoreCase("false")))
				{
					return true;
				}
			}
			else if(sub.getSubCommandName().equalsIgnoreCase("Craft Make Hunger"))
			{
				if(arg.length == 1 && (arg[0].equalsIgnoreCase("True") || arg[0].equalsIgnoreCase("false")))
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
		if(sub != null)
		{
			if(sub.getSubCommandName().equalsIgnoreCase("Kill Create"))
			{
				boolean result = Boolean.parseBoolean(arg[0]);
				String test = result ? "Enable" : "Dissable";
				par1.sendChatToPlayer(LangProxy.getText(test+" Gas Drops when you kill Peacefull living Entities", EnumChatFormatting.GREEN));
				HashMap<String, Boolean> flag = PlayerHandler.flags.get(par1.getCommandSenderName());
				if(flag == null)
				{
					flag = new HashMap<String, Boolean>();
					PlayerHandler.flags.put(par1.getCommandSenderName(), flag);
				}
				flag.put("GasByKill", result);
			}
			else if(sub.getSubCommandName().equalsIgnoreCase("Craft Make Hunger"))
			{
				boolean result = Boolean.parseBoolean(arg[0]);
				String test = result ? "Enable" : "Dissable";
				par1.sendChatToPlayer(LangProxy.getText(test+" Crafting Make You Hungry", EnumChatFormatting.GREEN));
				HashMap<String, Boolean> flag = PlayerHandler.flags.get(par1.getCommandSenderName());
				if(flag == null)
				{
					flag = new HashMap<String, Boolean>();
					PlayerHandler.flags.put(par1.getCommandSenderName(), flag);
				}
				flag.put("CraftHungry", result);
			}
		}
		
	}
	
}
