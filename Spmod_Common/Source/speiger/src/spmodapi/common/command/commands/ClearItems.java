package speiger.src.spmodapi.common.command.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import speiger.src.api.language.LanguageRegister;
import speiger.src.spmodapi.common.command.ISpmodCommand;
import speiger.src.spmodapi.common.command.ISubCommand;

public class ClearItems implements ISpmodCommand
{
	
	@Override
	public String getCommandName()
	{
		return "Clear Items";
	}
	
	@Override
	public List<String> getCommandUsage()
	{
		return Arrays.asList("Clears All Items");
	}
	
	@Override
	public ArrayList<ISubCommand> getSubCommands()
	{
		ArrayList<ISubCommand> list = new ArrayList<ISubCommand>();
		list.add(new BasicSubCommand("All", "Clear All Entity Items"));
		list.add(new BasicSubCommand("Around", "Clear Entities Around me", "Possible Text: Range (Any Full Number)"));
		return list;
	}
	
	@Override
	public boolean isCommandRunnable(ICommandSender par1, boolean guiAdding, ISubCommand sub, String[] arg)
	{
		if(sub == null && guiAdding)
		{
			return true;
		}
		
		if(sub != null)
		{
			if(sub.getSubCommandName().equalsIgnoreCase("All"))
			{
				if(FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().isPlayerOpped(par1.getCommandSenderName()))
				{
					return true;
				}
				return false;
			}
			else if(sub.getSubCommandName().equalsIgnoreCase("Around"))
			{
				if(arg.length == 1)
				{
					Integer in = Integer.parseInt(arg[0]);
					if(in != null && in.intValue() > 50 && !FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().isPlayerOpped(par1.getCommandSenderName()))
					{
						return false;
					}
				}
			}
			
		}
		return true;
	}
	
	@Override
	public void runCommand(ICommandSender par1, ISubCommand sub, String[] arg)
	{
		if(sub == null)
		{
			par1.sendChatToPlayer(LanguageRegister.createChatMessage("Could not delete items"));
			return;
		}
		
		if(sub.getSubCommandName().equalsIgnoreCase("All"))
		{
			if(!FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().isPlayerOpped(par1.getCommandSenderName()))
			{
				par1.sendChatToPlayer(LanguageRegister.createChatMessage("You are not allowed to use this command"));
				return;
			}
			par1.sendChatToPlayer(LanguageRegister.createChatMessage("Clearing All Entity Items in "+par1.getEntityWorld().provider.getDimensionName()));
			List<Entity> entities = par1.getEntityWorld().loadedEntityList;
			for(Entity en : entities)
			{
				if(en != null && en instanceof EntityItem)
				{
					en.setDead();
				}
			}
		}
		else if(sub.getSubCommandName().equalsIgnoreCase("Around"))
		{
			if(arg.length == 1)
			{
				Integer in = Integer.parseInt(arg[0]);
				if(in != null && in.intValue() > 50 && !FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().isPlayerOpped(par1.getCommandSenderName()))
				{
					par1.sendChatToPlayer(LanguageRegister.createChatMessage("You are not allowed to clearing items in a that big Range"));
					return;
				}
				if(in == null)
				{
					in = 10;
				}
				par1.sendChatToPlayer(LanguageRegister.createChatMessage("Removing Entities in a "+in+" Blocks Radius"));
				List<EntityItem> items = par1.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getAABBPool().getAABB(par1.getPlayerCoordinates().posX, par1.getPlayerCoordinates().posY, par1.getPlayerCoordinates().posZ, par1.getPlayerCoordinates().posX, par1.getPlayerCoordinates().posY, par1.getPlayerCoordinates().posZ).expand(in, in, in));
				for(EntityItem item : items)
				{
					if(item != null)
					{
						item.setDead();
					}
				}
			}
			else
			{
				par1.sendChatToPlayer(LanguageRegister.createChatMessage("Removing Entities in a 10 Blocks Radius"));
				List<EntityItem> items = par1.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getAABBPool().getAABB(par1.getPlayerCoordinates().posX, par1.getPlayerCoordinates().posY, par1.getPlayerCoordinates().posZ, par1.getPlayerCoordinates().posX, par1.getPlayerCoordinates().posY, par1.getPlayerCoordinates().posZ).expand(10, 10, 10));
				for(EntityItem item : items)
				{
					if(item != null)
					{
						item.setDead();
					}
				}
			}
		}
	}
	
}
