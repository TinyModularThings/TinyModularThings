package speiger.src.spmodapi.common.command.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import speiger.src.spmodapi.common.command.ISpmodCommand;
import speiger.src.spmodapi.common.command.ISubCommand;
import speiger.src.spmodapi.common.util.proxy.LangProxy;

public class PlayerGasCommand implements ISpmodCommand
{
	
	@Override
	public String getCommandName()
	{
		return "Player Gas Info";
	}
	
	@Override
	public List<String> getCommandUsage()
	{
		return Arrays.asList("Player Gas Info");
	}
	
	@Override
	public ArrayList<ISubCommand> getSubCommands()
	{
		ArrayList<ISubCommand> commands = new ArrayList<ISubCommand>();
		commands.add(new BasicSubCommand("Kill Create", "Enable/Dissable That you create Gas when you kill Entities"));
		return commands;
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
			if(arg.length == 1 && (arg[0].equalsIgnoreCase("True") || arg[0].equalsIgnoreCase("false")))
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void runCommand(ICommandSender par1, ISubCommand sub, String[] arg)
	{
		EntityPlayer player = par1.getEntityWorld().getPlayerEntityByName(par1.getCommandSenderName());
		if(player != null && player.getEntityData() != null)
		{
			boolean result = Boolean.parseBoolean(arg[0]);
			player.getEntityData().setBoolean("GasDrops", result);
			String test = result ? "Enable" : "Dissable";
			par1.sendChatToPlayer(LangProxy.getText(test+" Gas Drops when you kill Peacefull living Entities", EnumChatFormatting.GREEN));
		}
	}
	
}
