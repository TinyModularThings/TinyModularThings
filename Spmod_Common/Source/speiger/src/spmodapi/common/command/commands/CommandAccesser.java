package speiger.src.spmodapi.common.command.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import speiger.src.spmodapi.common.command.ISpmodCommand;
import speiger.src.spmodapi.common.command.ISubCommand;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.util.data.AccessConfig;
import speiger.src.spmodapi.common.util.proxy.LangProxy;

public class CommandAccesser implements ISpmodCommand
{
	
	@Override
	public String getCommandName()
	{
		return "Accesser Item";
	}
	
	@Override
	public List<String> getCommandUsage()
	{
		return Arrays.asList("You need to be a OP or the Config need to be activated");
	}
	
	@Override
	public ArrayList<ISubCommand> getSubCommands()
	{
		return new ArrayList<ISubCommand>();
	}
	
	@Override
	public boolean isCommandRunnable(ICommandSender par1, boolean guiAdding, ISubCommand sub, String[] arg)
	{
		return true;
	}
	
	@Override
	public void runCommand(ICommandSender par1, ISubCommand sub, String[] arg)
	{
		if(AccessConfig.everyOneCanUse || MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(par1.getCommandSenderName()))
		{
			ChunkCoordinates coords = par1.getPlayerCoordinates();
			EntityItem item = new EntityItem(par1.getEntityWorld(), coords.posX, coords.posY, coords.posZ, new ItemStack(APIItems.accessDebug));
			item.delayBeforeCanPickup = 10;
			if(!par1.getEntityWorld().isRemote)
			{
				par1.getEntityWorld().spawnEntityInWorld(item);
			}
		}
		else
		{
			par1.sendChatToPlayer(LangProxy.getText("You are not Allowed to use this Command"));
		}
	}
	
}
