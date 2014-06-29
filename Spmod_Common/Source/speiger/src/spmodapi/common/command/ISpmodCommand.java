package speiger.src.spmodapi.common.command;

import java.util.List;

import net.minecraft.command.ICommandSender;

public interface ISpmodCommand
{
	public String getCommandActivation();
	
	public void runCommand(ICommandSender par1, String[] objects);
	
	public boolean isRunable(ICommandSender par1, String[] objects);
	
	public int getPermissionLevel();
	
	public String getCommandUsage(ICommandSender par1);
	
	public List getSubCommands();
}
