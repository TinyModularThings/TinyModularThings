package speiger.src.spmodapi.common.command.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.ChatMessageComponent;
import speiger.src.spmodapi.common.command.ISpmodCommand;

public class ClearDropsCommand implements ISpmodCommand
{
	
	@Override
	public String getCommandActivation()
	{
		return "clearitems";
	}
	
	@Override
	public void runCommand(ICommandSender par1, String[] objects)
	{
		List list = par1.getEntityWorld().getLoadedEntityList();
		ArrayList<EntityItem> items = new ArrayList<EntityItem>();
		for (int i = 0; i < list.size(); i++)
		{
			Entity entity = (Entity) list.get(i);
			if (entity != null && entity instanceof EntityItem)
			{
				items.add((EntityItem) entity);
			}
		}
		
		for (EntityItem item : items)
		{
			item.setDead();
		}
		
		par1.sendChatToPlayer(new ChatMessageComponent().addText("Deleted " + items.size() + " Items"));
		
		items.clear();
		
	}
	
	@Override
	public boolean isRunable(ICommandSender par1, String[] objects)
	{
		par1.sendChatToPlayer(new ChatMessageComponent().addText("Clearing all Items"));
		return true;
	}
	
	@Override
	public int getPermissionLevel()
	{
		return 0;
	}
	
	@Override
	public String getCommandUsage(ICommandSender par1)
	{
		return "/clearItems clear all laying items";
	}
	
	@Override
	public List getSubCommands()
	{
		return null;
	}
	
}
