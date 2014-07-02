package speiger.src.spmodapi.common.command;

import java.util.ArrayList;

import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.command.commands.ClearDropsCommand;
import speiger.src.spmodapi.common.command.commands.CountDownCommand;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

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
	
	public void registerComands(FMLServerStartingEvent par1)
	{
		for (int i = 0; i < list.size(); i++)
		{
			ISpmodCommand current = list.get(i);
			
			par1.registerServerCommand(new SpmodCommand(current));
		}
		
		SpmodAPI.log.print("Registered " + list.size() + " Commands");
	}
	
	static
	{
		instance.registerCommand(new ClearDropsCommand());
		instance.registerCommand(new CountDownCommand());
	}
	
}
