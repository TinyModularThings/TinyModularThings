package speiger.src.spmodapi.common.command.commands;

import java.util.ArrayList;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import speiger.src.api.language.LanguageRegister;
import speiger.src.spmodapi.common.command.ISpmodCommand;
import speiger.src.spmodapi.common.command.ISubCommand;

public class TimerCommand implements ISpmodCommand
{
	
	@Override
	public String getCommandName()
	{
		return "Timer";
	}
	
	@Override
	public String getCommandUsage()
	{
		return "Timers";
	}
	
	@Override
	public ArrayList<ISubCommand> getSubCommands()
	{
		ArrayList<ISubCommand> com = new ArrayList<ISubCommand>();
		com.add(new BasicSubCommand("set", "Set Time"));
		com.add(new BasicSubCommand("Stop", "Stop Timer"));
		com.add(new BasicSubCommand("check", "Check Current Time"));
		com.add(new BasicSubCommand("add", "Add time to the Counter"));
		com.add(new BasicSubCommand("remove Time", "Removing time from counter"));
		return com;
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
			String key = sub.getSubCommandName();
			EntityPlayer player = par1.getEntityWorld().getPlayerEntityByName(par1.getCommandSenderName());
			NBTTagCompound nbt = player.getEntityData();
			if(key.equalsIgnoreCase("Stop"))
			{
				return true;
			}
			else if(key.equalsIgnoreCase("check"))
			{
				return true;
			}
			else if(key.equalsIgnoreCase("add"))
			{
				if(guiAdding)
				{
					return true;
				}
				if(arg.length == 3 || arg.length == 1)
				{
					return true;
				}
			}
			else if(key.equalsIgnoreCase("set"))
			{
				if(guiAdding)
				{
					return true;
				}
				if(arg.length == 3 || arg.length == 1)
				{
					return true;
				}
			}
			else if(key.equalsIgnoreCase("remove Time"))
			{
				if(guiAdding)
				{
					return true;
				}
				if(arg.length == 3 || arg.length == 1)
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
			EntityPlayer player = par1.getEntityWorld().getPlayerEntityByName(par1.getCommandSenderName());
			NBTTagCompound nbt = player.getEntityData();
			String key = sub.getSubCommandName();
			if(key.equalsIgnoreCase("check"))
			{
				int totalTime = 0;
				boolean flag = false;
				if(nbt.hasKey("SpmodAPIData") && nbt.getCompoundTag("SpmodAPIData").hasKey("CountdownTime") && nbt.getCompoundTag("SpmodAPIData").getInteger("CountdownTime") > 0)
				{
					totalTime = nbt.getCompoundTag("SpmodAPIData").getInteger("CountdownTime");
					flag = true;
				}
				
				if(flag)
				{
					int TotalTime = totalTime / 20;
				    int Sekunde = TotalTime % 60;
				    int Minute = TotalTime / 60 % 60;
				    int Stunde = TotalTime / 60 / 60 % 24;
				    String time = "TotalTime: "+Stunde+":"+Minute+":"+Sekunde;
				    par1.sendChatToPlayer(LanguageRegister.createChatMessage(time));
				}
				else
				{
					par1.sendChatToPlayer(LanguageRegister.createChatMessage("No Timer running"));
				}
			}
			else if(key.equalsIgnoreCase("remove Time"))
			{
				int total = 0;
				if(arg.length == 1)
				{
					try
					{
						total = Integer.parseInt(arg[0]);
					}
					catch (Exception e)
					{
						par1.sendChatToPlayer(LanguageRegister.createChatMessage("Text is not a valid Number"));
						return;
					}
					
				}
				else if(arg.length == 3)
				{
					try
					{
						int sek = Integer.parseInt(arg[0]);
						int min = Integer.parseInt(arg[1]);
						int h = Integer.parseInt(arg[2]);
						total = (sek + (min*60)+ (h*3600))*20;
					}
					catch (Exception e)
					{
						par1.sendChatToPlayer(LanguageRegister.createChatMessage("Text is not a valid Numbers, Valid Row: Sek, Min, Hour"));
						return;
					}
				}
				else
				{
					par1.sendChatToPlayer(LanguageRegister.createChatMessage("Not Valid String lenght"));
					return;
				}
				
				if(total > 0)
				{
					if(nbt.hasKey("SpmodAPIData") && nbt.getCompoundTag("SpmodAPIData").hasKey("CountdownTime"))
					{
						int totalTime = nbt.getCompoundTag("SpmodAPIData").getInteger("CountdownTime") -total;
						if(totalTime <= 0)
						{
							nbt.getCompoundTag("SpmodAPIData").removeTag("CountdownTime");
							par1.sendChatToPlayer(LanguageRegister.createChatMessage("Removed Timer"));
						}
						else
						{
							int result = total - totalTime;
							int TotalTime = result / 20;
						    int Sekunde = TotalTime % 60;
						    int Minute = TotalTime / 60 % 60;
						    int Stunde = TotalTime / 60 / 60 % 24;
						    String time = "Set TotalTime to: "+Stunde+":"+Minute+":"+Sekunde+" ("+result+" Total Ticks)";
						    nbt.getCompoundTag("SpmodAPIData").setInteger("CountdownTime", result);
						    par1.sendChatToPlayer(LanguageRegister.createChatMessage(time));
						}
					}
					else
					{
						
					}
				}
				else
				{
					par1.sendChatToPlayer(LanguageRegister.createChatMessage("Time should be bigger than 0"));
				}
			}
			else if(key.equalsIgnoreCase("Stop"))
			{
				if(nbt.hasKey("SpmodAPIData") && nbt.getCompoundTag("SpmodAPIData").hasKey("CountdownTime"))
				{
					nbt.getCompoundTag("SpmodAPIData").removeTag("CountdownTime");
					par1.sendChatToPlayer(LanguageRegister.createChatMessage("Removed Timer"));
				}
				else
				{
					par1.sendChatToPlayer(LanguageRegister.createChatMessage("Nothing to Remove"));
				}
			}
			else if(key.equalsIgnoreCase("add"))
			{
				int total = 0;
				if(arg.length == 1)
				{
					try
					{
						total = Integer.parseInt(arg[0]);
					}
					catch (Exception e)
					{
						par1.sendChatToPlayer(LanguageRegister.createChatMessage("Text is not a valid Number"));
						return;
					}
					
				}
				else if(arg.length == 3)
				{
					try
					{
						int sek = Integer.parseInt(arg[0]);
						int min = Integer.parseInt(arg[1]);
						int h = Integer.parseInt(arg[2]);
						total = (sek + (min*60)+ (h*3600))*20;
					}
					catch (Exception e)
					{
						par1.sendChatToPlayer(LanguageRegister.createChatMessage("Text is not a valid Numbers, Valid Row: Sek, Min, Hour"));
						return;
					}
				}
				else
				{
					par1.sendChatToPlayer(LanguageRegister.createChatMessage("Not Valid String lenght"));
					return;
				}
				
				if(total > 0)
				{
					int cont = 0;
					if(nbt.hasKey("SpmodAPIData"))
					{
						cont = nbt.getCompoundTag("SpmodAPIData").getInteger("CountdownTime");
						nbt.getCompoundTag("SpmodAPIData").setInteger("CountdownTime", cont+total);
						par1.sendChatToPlayer(LanguageRegister.createChatMessage("Total Ticks Added: "+total));
					}
					else
					{
						NBTTagCompound time = new NBTTagCompound();
						time.setInteger("CountdownTime", total);
						nbt.setTag("SpmodAPIData", time);
						par1.sendChatToPlayer(LanguageRegister.createChatMessage("Total Ticks Added: "+total));
					}
				}
				else
				{
					par1.sendChatToPlayer(LanguageRegister.createChatMessage("Time should be bigger than 0"));
				}
			}
			else if(key.equalsIgnoreCase("set"))
			{
				int total = 0;
				if(arg.length == 1)
				{
					try
					{
						total = Integer.parseInt(arg[0]);
					}
					catch (Exception e)
					{
						par1.sendChatToPlayer(LanguageRegister.createChatMessage("Text is not a valid Number"));
						return;
					}
					
				}
				else if(arg.length == 3)
				{
					try
					{
						int sek = Integer.parseInt(arg[0]);
						int min = Integer.parseInt(arg[1]);
						int h = Integer.parseInt(arg[2]);
						total = (sek + (min*60)+ (h*3600))*20;
					}
					catch (Exception e)
					{
						par1.sendChatToPlayer(LanguageRegister.createChatMessage("Text is not a valid Numbers, Valid Row: Sek, Min, Hour"));
						return;
					}
				}
				else
				{
					par1.sendChatToPlayer(LanguageRegister.createChatMessage("Not Valid String lenght"));
					return;
				}
				
				if(total > 0)
				{
					if(nbt.hasKey("SpmodAPIData"))
					{
						nbt.getCompoundTag("SpmodAPIData").setInteger("CountdownTime", total);
						par1.sendChatToPlayer(LanguageRegister.createChatMessage("Set Total Time to: "+total));
					}
					else
					{
						NBTTagCompound time = new NBTTagCompound();
						time.setInteger("CountdownTime", total);
						nbt.setTag("SpmodAPIData", time);
						par1.sendChatToPlayer(LanguageRegister.createChatMessage("Set Total Time to: "+total));
					}
				}
				else
				{
					par1.sendChatToPlayer(LanguageRegister.createChatMessage("Time should be bigger than 0"));
				}
			}
			
		}
	}
	
}
