package speiger.src.spmodapi.common.command;

import java.util.ArrayList;

import net.minecraft.command.ICommandSender;
import speiger.src.api.common.data.packets.SpmodPacketHelper;
import speiger.src.spmodapi.common.command.commands.ButcherCommand;
import speiger.src.spmodapi.common.command.commands.ClearItems;
import speiger.src.spmodapi.common.command.commands.CommandAccesser;
import speiger.src.spmodapi.common.command.commands.CommandReadTexture;
import speiger.src.spmodapi.common.command.commands.SpeigerCommand;
import speiger.src.spmodapi.common.command.commands.TimerCommand;

public class CommandRegistry
{
	private ArrayList<ISpmodCommand> list = new ArrayList<ISpmodCommand>();
	
	private static CommandRegistry instance = new CommandRegistry();
	
	public static CommandRegistry getInstance()
	{
		return instance;
	}
	
	public void registerCommand(ISpmodCommand par1)
	{
		list.add(par1);
	}
	
	public ArrayList<ISpmodCommand> getCommands(ICommandSender par1)
	{
		ArrayList<ISpmodCommand> com = new ArrayList<ISpmodCommand>();
		for (ISpmodCommand cu : list)
		{
			if (cu.isCommandRunnable(par1, true, (ISubCommand) null, new String[0]))
			{
				com.add(cu);
			}
		}
		return com;
	}
	
	public ArrayList<ISubCommand> getSubCommands(ICommandSender par1, ISpmodCommand par2)
	{
		ArrayList<ISubCommand> com = new ArrayList<ISubCommand>();
		if (!par2.getSubCommands().isEmpty())
		{
			for (ISubCommand cu : par2.getSubCommands())
			{
				if (cu != null && par2.isCommandRunnable(par1, true, cu, new String[0]))
				{
					com.add(cu);
				}
			}
		}
		return com;
	}
	
	public static void init()
	{
		SpmodPacketHelper.getHelper().registerPacketReciver(new CommandExecuter());
		CommandRegistry reg = getInstance();
		reg.registerCommand(new ClearItems());
		reg.registerCommand(new TimerCommand());
		reg.registerCommand(new ButcherCommand());
		reg.registerCommand(new SpeigerCommand());
		reg.registerCommand(new CommandReadTexture());
		reg.registerCommand(new CommandAccesser());
	}
	
	
	
}
