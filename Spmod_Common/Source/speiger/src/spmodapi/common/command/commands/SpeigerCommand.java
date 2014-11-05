package speiger.src.spmodapi.common.command.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.command.ICommandSender;
import speiger.src.spmodapi.common.command.ISpmodCommand;
import speiger.src.spmodapi.common.command.ISubCommand;
import speiger.src.spmodapi.common.entity.SpmodFoodStats;
import speiger.src.spmodapi.common.util.proxy.LangProxy;

public class SpeigerCommand implements ISpmodCommand
{
	
	@Override
	public String getCommandName()
	{
		return "Hardcore Peacefull";
	}
	
	@Override
	public List<String> getCommandUsage()
	{
		return Arrays.asList("Harcore Peacefull removes the Fast healing and activate normal Hunger Mode.", "<Boolean(true/false)>");
	}
	
	@Override
	public ArrayList<ISubCommand> getSubCommands()
	{
		return new ArrayList<ISubCommand>();
	}
	
	@Override
	public boolean isCommandRunnable(ICommandSender par1, boolean guiAdding, ISubCommand sub, String[] arg)
	{
		if(par1.getCommandSenderName().equalsIgnoreCase("Speiger"))
		{
			if(guiAdding)
			{
				return true;
			}
			else
			{
				if(arg.length == 1)
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
		boolean par2 = Boolean.parseBoolean(arg[0]);
		SpmodFoodStats.hardcorePeacefull.put(par1.getCommandSenderName(), par2);
		par1.sendChatToPlayer(LangProxy.getText(par2 ? "Activated Hardcore Peacefull" : "Deactivated Hardcore Peacefull"));
	}
	
}
