package speiger.src.spmodapi.common.command.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import speiger.src.api.common.utils.MathUtils;
import speiger.src.spmodapi.common.command.ISpmodCommand;
import speiger.src.spmodapi.common.command.ISubCommand;
import speiger.src.spmodapi.common.util.proxy.LangProxy;

public class TimerCommand implements ISpmodCommand
{
	
	@Override
	public String getCommandName()
	{
		return "Timer";
	}
	
	@Override
	public List<String> getCommandUsage()
	{
		return Arrays.asList("Timers");
	}
	
	@Override
	public ArrayList<ISubCommand> getSubCommands()
	{
		ArrayList<ISubCommand> com = new ArrayList<ISubCommand>();
		com.add(new BasicSubCommand("set", "Set Time", "<Sek, Min, Hour>", "<Total Ticks>"));
		com.add(new BasicSubCommand("Stop", "Stop Timer"));
		com.add(new BasicSubCommand("check", "Check Current Time"));
		com.add(new BasicSubCommand("add", "Add time to the Counter", "<Sek, Min, Hour>", "<Total Ticks>"));
		com.add(new BasicSubCommand("remove Time", "Removing time from counter", "<Sek, Min, Hour>", "<Total Ticks>"));
		return com;
	}
	
	@Override
	public boolean isCommandRunnable(ICommandSender par1, boolean guiAdding, ISubCommand sub, String[] arg)
	{
		if (guiAdding && sub == null)
		{
			return true;
		}
		
		if (sub != null)
		{
			String key = sub.getSubCommandName();
			EntityPlayer player = par1.getEntityWorld().getPlayerEntityByName(par1.getCommandSenderName());
			NBTTagCompound nbt = player.getEntityData();
			if (key.equalsIgnoreCase("Stop"))
			{
				return true;
			}
			else if (key.equalsIgnoreCase("check"))
			{
				return true;
			}
			else if (key.equalsIgnoreCase("add"))
			{
				if (guiAdding)
				{
					return true;
				}
				if (arg.length == 3 || arg.length == 1)
				{
					return true;
				}
			}
			else if (key.equalsIgnoreCase("set"))
			{
				if (guiAdding)
				{
					return true;
				}
				if (arg.length == 3 || arg.length == 1)
				{
					return true;
				}
			}
			else if (key.equalsIgnoreCase("remove Time"))
			{
				
				if (guiAdding)
				{
					return true;
				}
				if (arg.length == 3 || arg.length == 1)
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
		if (sub != null)
		{
			EntityPlayer player = par1.getEntityWorld().getPlayerEntityByName(par1.getCommandSenderName());
			NBTTagCompound nbt = player.getEntityData();
			String key = sub.getSubCommandName();
			
			if (key.equalsIgnoreCase("check"))
			{
				int totalTime = 0;
				boolean flag = false;
				if (nbt.hasKey("SpmodAPIData") && nbt.getCompoundTag("SpmodAPIData").hasKey("CountdownTime") && nbt.getCompoundTag("SpmodAPIData").getInteger("CountdownTime") > 0)
				{
					totalTime = nbt.getCompoundTag("SpmodAPIData").getInteger("CountdownTime");
					flag = true;
				}
				
				if (flag)
				{
					String time = "TotalTime: "+MathUtils.getTicksInTimeShort(totalTime);
					par1.sendChatToPlayer(LangProxy.getText(time));
				}
				else
				{
					par1.sendChatToPlayer(LangProxy.getText("No Timer running", EnumChatFormatting.RED));
				}
			}
			else if (key.equalsIgnoreCase("remove Time"))
			{
				int total = 0;
				if (arg.length == 1)
				{
					try
					{
						total = Integer.parseInt(arg[0]);
					}
					catch (Exception e)
					{
						par1.sendChatToPlayer(LangProxy.getText("Text is not a valid Number", EnumChatFormatting.YELLOW));
						return;
					}
					
				}
				else if (arg.length == 3)
				{
					try
					{
						int sek = Integer.parseInt(arg[0]);
						int min = Integer.parseInt(arg[1]);
						int h = Integer.parseInt(arg[2]);
						total = (sek + (min * 60) + (h * 3600)) * 20;
					}
					catch (Exception e)
					{
						par1.sendChatToPlayer(LangProxy.getText("Text is not a valid Numbers, Valid Row: Sek, Min, Hour", EnumChatFormatting.DARK_RED));
						return;
					}
				}
				else
				{
					par1.sendChatToPlayer(LangProxy.getText("Argument Lenght is not valid", EnumChatFormatting.RED));
					return;
				}
				
				if (total > 0)
				{
					if (nbt.hasKey("SpmodAPIData") && nbt.getCompoundTag("SpmodAPIData").hasKey("CountdownTime"))
					{
						int totalTime = nbt.getCompoundTag("SpmodAPIData").getInteger("CountdownTime") - total;
						if (totalTime <= 0)
						{
							nbt.getCompoundTag("SpmodAPIData").removeTag("CountdownTime");
							par1.sendChatToPlayer(LangProxy.getText("Removed Timer", EnumChatFormatting.BLUE));
						}
						else
						{
							int result = total - totalTime;
							String time = "Set TotalTime to: "+MathUtils.getTicksInTimeShort(result)+" Total Ticks: "+result;
							nbt.getCompoundTag("SpmodAPIData").setInteger("CountdownTime", result);
							par1.sendChatToPlayer(LangProxy.getText(time, EnumChatFormatting.AQUA));
						}
					}
					else
					{
						par1.sendChatToPlayer(LangProxy.getText("Never Used a Timer"));
					}
				}
				else
				{
					par1.sendChatToPlayer(LangProxy.getText("Time should be bigger than 0", EnumChatFormatting.GREEN));
				}
			}
			else if (key.equalsIgnoreCase("Stop"))
			{
				if (nbt.hasKey("SpmodAPIData") && nbt.getCompoundTag("SpmodAPIData").hasKey("CountdownTime"))
				{
					nbt.getCompoundTag("SpmodAPIData").removeTag("CountdownTime");
					par1.sendChatToPlayer(LangProxy.getText("Removed Timer", EnumChatFormatting.GREEN));
				}
				else
				{
					par1.sendChatToPlayer(LangProxy.getText("Nothing to Remove"));
				}
			}
			else if (key.equalsIgnoreCase("add"))
			{
				int total = 0;
				if (arg.length == 1)
				{
					try
					{
						total = Integer.parseInt(arg[0]);
					}
					catch (Exception e)
					{
						par1.sendChatToPlayer(LangProxy.getText("Text is not a valid Number", EnumChatFormatting.RED));
						return;
					}
					
				}
				else if (arg.length == 3)
				{
					try
					{
						int sek = Integer.parseInt(arg[0]);
						int min = Integer.parseInt(arg[1]);
						int h = Integer.parseInt(arg[2]);
						total = (sek + (min * 60) + (h * 3600)) * 20;
					}
					catch (Exception e)
					{
						par1.sendChatToPlayer(LangProxy.getText("Text is not a valid Numbers, Valid Row: Sek, Min, Hour", EnumChatFormatting.RED));
						return;
					}
				}
				else
				{
					par1.sendChatToPlayer(LangProxy.getText("Not Valid String lenght", EnumChatFormatting.RED));
					return;
				}
				
				if (total > 0)
				{
					int cont = 0;
					if (nbt.hasKey("SpmodAPIData"))
					{
						cont = nbt.getCompoundTag("SpmodAPIData").getInteger("CountdownTime");
						nbt.getCompoundTag("SpmodAPIData").setInteger("CountdownTime", cont + total);
						par1.sendChatToPlayer(LangProxy.getText("Total Ticks Added: " + total));
					}
					else
					{
						NBTTagCompound time = new NBTTagCompound();
						time.setInteger("CountdownTime", total);
						nbt.setTag("SpmodAPIData", time);
						par1.sendChatToPlayer(LangProxy.getText("Total Ticks Added: " + total));
					}
				}
				else
				{
					par1.sendChatToPlayer(LangProxy.getText("Time should be bigger than 0"));
				}
			}
			else if (key.equalsIgnoreCase("set"))
			{
				int total = 0;
				if (arg.length == 1)
				{
					try
					{
						total = Integer.parseInt(arg[0]);
					}
					catch (Exception e)
					{
						par1.sendChatToPlayer(LangProxy.getText("Text is not a valid Number"));
						return;
					}
					
				}
				else if (arg.length == 3)
				{
					try
					{
						int sek = Integer.parseInt(arg[0]);
						int min = Integer.parseInt(arg[1]);
						int h = Integer.parseInt(arg[2]);
						total = (sek + (min * 60) + (h * 3600)) * 20;
					}
					catch (Exception e)
					{
						par1.sendChatToPlayer(LangProxy.getText("Text is not a valid Numbers, Valid Row: Sek, Min, Hour"));
						return;
					}
				}
				else
				{
					par1.sendChatToPlayer(LangProxy.getText("Not Valid String lenght"));
					return;
				}
				
				if (total > 0)
				{
					if (nbt.hasKey("SpmodAPIData"))
					{
						nbt.getCompoundTag("SpmodAPIData").setInteger("CountdownTime", total);
						par1.sendChatToPlayer(LangProxy.getText("Set Total Time to: " + total));
					}
					else
					{
						NBTTagCompound time = new NBTTagCompound();
						time.setInteger("CountdownTime", total);
						nbt.setTag("SpmodAPIData", time);
						par1.sendChatToPlayer(LangProxy.getText("Set Total Time to: " + total));
					}
				}
				else
				{
					par1.sendChatToPlayer(LangProxy.getText("Time should be bigger than 0"));
				}
			}
			
		}
	}
	
}
