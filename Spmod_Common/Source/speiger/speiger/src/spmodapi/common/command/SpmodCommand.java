package speiger.src.spmodapi.common.command;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class SpmodCommand extends CommandBase
{
	
	private ISpmodCommand comand;
	
	public SpmodCommand(ISpmodCommand par1)
	{
		comand = par1;
	}
	
	@Override
	public List getCommandAliases()
	{
		return comand.getSubCommands();
	}
	
	@Override
	public int getRequiredPermissionLevel()
	{
		return comand.getPermissionLevel();
	}
	
	@Override
	public String getCommandName()
	{
		return comand.getCommandActivation();
	}
	
	@Override
	public String getCommandUsage(ICommandSender icommandsender)
	{
		return comand.getCommandUsage(icommandsender);
	}
	
	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring)
	{
		if (comand.isRunable(icommandsender, astring))
		{
			comand.runCommand(icommandsender, astring);
		}
	}
	
}
