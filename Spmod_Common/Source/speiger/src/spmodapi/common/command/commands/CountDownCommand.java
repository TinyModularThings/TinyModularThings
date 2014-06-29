package speiger.src.spmodapi.common.command.commands;

import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import speiger.src.spmodapi.common.command.ISpmodCommand;

public class CountDownCommand implements ISpmodCommand
{
	
	@Override
	public String getCommandActivation()
	{
		return "countdown";
	}
	
	@Override
	public void runCommand(ICommandSender par1, String[] objects)
	{
		World world = par1.getEntityWorld();
		EntityPlayer player = world.getPlayerEntityByName(par1.getCommandSenderName());
		
		if (objects.length == 1)
		{
			NBTTagCompound nbt = player.getEntityData();
			if (nbt.hasKey("SpmodAPIData"))
			{
				NBTTagCompound spmodNBT = nbt.getCompoundTag("SpmodAPIData");
				if (spmodNBT.hasKey("CountdownTime"))
				{
					int time = spmodNBT.getInteger("CountdownTime");
					int hour = (time / 20) / 3600;
					int min = (time / 20) / 60 % 60;
					int sec = (time / 20) % 60;
					
					player.addChatMessage("Countdown Time left: " + hour + ":" + min + ":" + sec);
					
				}
				else
				{
					player.addChatMessage("No Countdown Running");
				}
			}
			else
			{
				player.addChatMessage("No Countdown Running");
			}
		}
		else if (objects.length == 4)
		{
			int Totaltime = 0;
			Totaltime += Integer.parseInt(objects[1]) * 20 * 3600;
			Totaltime += Integer.parseInt(objects[2]) * 20 * 60;
			Totaltime += Integer.parseInt(objects[3]) * 20;
			NBTTagCompound nbt = getSpmodNBTFromPlayer(player);
			nbt.setInteger("CountdownTime", Totaltime);
			NBTTagCompound playerNBT = player.getEntityData();
			playerNBT.setCompoundTag("SpmodAPIData", nbt);
			
			int hour = (Totaltime / 20) / 3600;
			int min = (Totaltime / 20) / 60 % 60;
			int sec = (Totaltime / 20) % 60;
			
			player.addChatMessage("Setting up a countdown: " + hour + ":" + min + ":" + sec);
			
		}
		
	}
	
	@Override
	public boolean isRunable(ICommandSender par1, String[] objects)
	{
		if (objects.length == 1)
		{
			return objects[0].equals("get");
		}
		if (objects.length == 4)
		{
			boolean start = objects[0].equals("set");
			int Totaltime = 0;
			
			Totaltime += Integer.parseInt(objects[1]) * 20 * 3600;
			Totaltime += Integer.parseInt(objects[2]) * 20 * 60;
			Totaltime += Integer.parseInt(objects[3]) * 20;
			
			boolean time = Totaltime != 0;
			
			return start && time;
		}
		
		EntityPlayer player = par1.getEntityWorld().getPlayerEntityByName(par1.getCommandSenderName());
		player.addChatMessage("Wrong arguments");
		player.addChatMessage("get for getting countdown time left");
		player.addChatMessage("set + hours + minutes + seconds");
		
		return false;
	}
	
	@Override
	public int getPermissionLevel()
	{
		return 0;
	}
	
	@Override
	public String getCommandUsage(ICommandSender par1)
	{
		return "/countdown";
	}
	
	@Override
	public List getSubCommands()
	{
		return null;
	}
	
	public NBTTagCompound getSpmodNBTFromPlayer(EntityPlayer player)
	{
		NBTTagCompound nbt = player.getEntityData();
		if (nbt.hasKey("SpmodAPIData"))
		{
			return nbt.getCompoundTag("SpmodAPIData");
		}
		return new NBTTagCompound();
	}
	
}
