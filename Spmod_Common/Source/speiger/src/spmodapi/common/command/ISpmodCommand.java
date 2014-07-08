package speiger.src.spmodapi.common.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommandSender;


public interface ISpmodCommand
{
	/**
	 * @return the Display name of the Command
	 */
	public String getCommandName();
	
	/**
	 * @return the command discription
	 */
	public List<String> getCommandUsage();
	
	/**
	 * @return The subCommands List.
	 */
	public ArrayList<ISubCommand> getSubCommands();
	
	/**
	 * @ICommandSender sender of this Command
	 * @Boolean if the command is valid in general. Just for adding into the gui list.
	 * @ISubCommand == null when this is not a subcommand.
	 * @arguments. Every agrument that come from the Textfield. (like the chat in normal) is only a empty String Array when guiAdding is true.
	 */
	public boolean isCommandRunnable(ICommandSender par1, boolean guiAdding, ISubCommand sub, String[] arg);
	
	/**
	 * This is the function that will run the command. 
	 */
	public void runCommand(ICommandSender par1, ISubCommand sub, String[] arg);
}
