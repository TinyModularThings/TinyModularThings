package speiger.src.spmodapi.common.command.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import org.apache.commons.lang3.StringUtils;

import cpw.mods.fml.common.FMLCommonHandler;

import speiger.src.api.language.LanguageRegister;
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
		list.add(new BasicSubCommand("Everything", "Kill Every Entity", "<Entity>", "<Nothing>", "If no Filter he kills everything than EntityPlayer"));
		list.add(new BasicSubCommand("Custom", "Kill Every Entity You want", "<Range(Full Number), Entities>", "<Entity>", "Entities can be more than one!"));
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
				return FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().isPlayerOpped(par1.getCommandSenderName());
			}
			else if(name.equalsIgnoreCase("Custom"))
			{
				if(guiAdding)
				{
					return true;
				}
				
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
			ChunkCoordinates chunk = par1.getPlayerCoordinates();
			if(world != null && chunk != null)
			{
				EntityPlayer player = world.getPlayerEntityByName(par1.getCommandSenderName());
				if(player != null)
				{
					String com = sub.getSubCommandName();
					int killed = 0;
					if(com.equalsIgnoreCase("Everything"))
					{
						
						if(arg != null && arg.length > 0)
						{
							Collection<String> keys = EntityList.classToStringMapping.values();
							for(String key : keys)
							{
								for(String req : arg)
								{
									if(req.equalsIgnoreCase(key))
									{
										killed += this.killEntity(true, (Class<? extends Entity>)EntityList.stringToClassMapping.get(key), world, chunk, 0);
									}
								}
							}
						}
						else
						{
							Collection<Class<? extends Entity>> en = EntityList.stringToClassMapping.values();
							for(Class<? extends Entity> cu : en)
							{
								if(cu != null)
								{
									killed += killEntity(true, cu, world, chunk, 0);
								}
							}
						}
					}
					else if(com.equalsIgnoreCase("Custom"))
					{
						if(arg.length > 1)
						{
							if(StringUtils.isNumeric(arg[0]))
							{
								Integer num = Integer.valueOf(arg[0]);
								if(num != null)
								{
									for(int i = 1;i<arg.length;i++)
									{
										String entityKey = arg[i];
										Collection<String> keys = EntityList.classToStringMapping.values();
										for(String key : keys)
										{
											if(key.equalsIgnoreCase(entityKey))
											{
												if(EntityList.stringToClassMapping.get(key) != null)
												{
													Class<? extends Entity> entity = (Class<? extends Entity>) EntityList.stringToClassMapping.get(entityKey);
													killed += this.killEntity(false, entity, world, chunk, num.intValue());
												}
											}
										}
									}
								}
							}
						}
						else
						{
							Collection<String> keys = EntityList.classToStringMapping.values();
							for(String key : keys)
							{
								if(key.equalsIgnoreCase(arg[0]))
								{
									if(EntityList.stringToClassMapping.get(key) != null)
									{
										Class<? extends Entity> entity = (Class<? extends Entity>) EntityList.stringToClassMapping.get(key);
										killed += this.killEntity(false, entity, world, chunk, 10);
									}
								}
							}
						}
					}
					par1.sendChatToPlayer(LanguageRegister.createChatMessage("Killed: "+killed+" Entities"));
				}
			}
		
		}
	}
	
	public int killEntity(boolean everything, Class<? extends Entity> entity, World world, ChunkCoordinates chunk, int range)
	{
		int killed = 0;
		if(everything)
		{
			List<Entity> entities = world.loadedEntityList;
			for(Entity en : entities)
			{
				if(entity.getSimpleName().equalsIgnoreCase(en.getClass().getSimpleName()))
				{
					en.setDead();
					killed++;
				}
			}
		}
		else
		{
			List<Entity> entities = world.getEntitiesWithinAABB(entity, AxisAlignedBB.getAABBPool().getAABB(chunk.posX-range, chunk.posY-range, chunk.posZ-range, chunk.posX+range, chunk.posY+range, chunk.posZ+range));
			for(Entity en : entities)
			{
				en.setDead();
				killed++;
			}
		}
		return killed;
	}
	
}
