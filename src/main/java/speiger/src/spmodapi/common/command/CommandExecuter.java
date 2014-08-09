package speiger.src.spmodapi.common.command;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.packets.IPacketReciver;
import speiger.src.spmodapi.SpmodAPI;
import cpw.mods.fml.common.network.ByteBufUtils;

public class CommandExecuter implements IPacketReciver
{

	@Override
	public void recivePacket(ByteBuf par1)
	{
		CommandRegistry reg = CommandRegistry.getInstance();
		try
		{
			World world = DimensionManager.getWorld(par1.readInt());
			if(world != null && !world.isRemote)
			{
				EntityPlayer player = world.getPlayerEntityByName(ByteBufUtils.readUTF8String(par1));
				if(player != null)
				{
					boolean sub = par1.readByte() == 1;
					int commandID = par1.readInt();
					int subCommandID = par1.readInt();
					String[] text = new String[par1.readInt()];
					for(int i = 0;i<text.length;i++)
					{
						text[i] = ByteBufUtils.readUTF8String(par1);
					}
					
					
					if(sub)
					{
						ArrayList<ISpmodCommand> list = new ArrayList<ISpmodCommand>();
						if(!reg.getCommands(player).isEmpty())
						{
							list.addAll(reg.getCommands(player));
						}
						if(list != null && !list.isEmpty())
						{
							ISpmodCommand com = list.get(commandID);
							ArrayList<ISubCommand> subCom = CommandRegistry.getInstance().getSubCommands(player, com);
							if(!subCom.isEmpty())
							{
								if(com.isCommandRunnable(player, false, subCom.get(subCommandID), text))
								{
									com.runCommand(player, subCom.get(subCommandID), text);
									return;
								}
								else
								{
									player.addChatMessage(LanguageRegister.createChatMessage("Wrong Arguments. Check the info for right arguments"));
									return;
								}
							}
						}
					}
					else
					{
						ArrayList<ISpmodCommand> list = reg.getCommands(player);
						if(!list.isEmpty())
						{
							ISpmodCommand com = list.get(commandID);
							if(com.isCommandRunnable(player, false, (ISubCommand)null, text))
							{
								com.runCommand(player, (ISubCommand)null, text);
								return;
							}
						}
					}
				}
			}
			SpmodAPI.log.print("Could not execute a command");
		}
		catch (Exception e)
		{
			SpmodAPI.log.print("Could not execute a command");
		}
	}

	@Override
	public String identifier()
	{
		return "Command.Reciver";
	}
	
}
