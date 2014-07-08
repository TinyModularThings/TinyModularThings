package speiger.src.spmodapi.common.command.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import speiger.src.spmodapi.common.command.ISpmodCommand;
import speiger.src.spmodapi.common.command.ISubCommand;

public class ButcherCommand implements ISpmodCommand
{

	@Override
	public String getCommandName()
	{
		return "Butcher";
	}

	@Override
	public List<String> getCommandUsage()
	{
		return Arrays.asList("Butcher Entities");
	}

	@Override
	public ArrayList<ISubCommand> getSubCommands()
	{
		ArrayList<ISubCommand> list = new ArrayList<ISubCommand>();
		list.add(new BasicSubCommand("Everything", "Kill Every Entity"));
		list.add(new BasicSubCommand("Custom", "Kill Every Entity You want", "<Range(Full Number), Entities>", "Entitie", "Entities can be more than one!"));
		return list;
	}

	@Override
	public boolean isCommandRunnable(ICommandSender par1, boolean guiAdding, ISubCommand sub, String[] arg)
	{
		if(guiAdding && sub == null)
		{
			return true;
		}
		
		if(sub != null)
		{
			String name = sub.getSubCommandName();
			if(name.equalsIgnoreCase("Everything"))
			{
				return MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(par1.getCommandSenderName());
			}
			else if(name.equalsIgnoreCase("Custom"))
			{
				if(arg.length >= 1)
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
			World world = par1.getEntityWorld();
			EntityPlayer player = world.getPlayerEntityByName(par1.getCommandSenderName());
		}
	}
	
}
