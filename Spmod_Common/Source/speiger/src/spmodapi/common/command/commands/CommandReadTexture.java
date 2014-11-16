package speiger.src.spmodapi.common.command.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import speiger.src.api.common.data.utils.BlockData;
import speiger.src.api.common.data.utils.ItemData;
import speiger.src.spmodapi.common.command.ISpmodCommand;
import speiger.src.spmodapi.common.command.ISubCommand;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.proxy.LangProxy;

public class CommandReadTexture implements ISpmodCommand
{
	
	@Override
	public String getCommandName()
	{
		return "Texture Info";
	}
	
	@Override
	public List<String> getCommandUsage()
	{
		return Arrays.asList("Laest dich Texturen Namen sehen",
				"<ID(Zahl), Meta(Zahl), Block/Item (Text)>",
				"Block/Item einfach so  schreiben. Damit ich weis wonach du suchst",
				"Fals die Meta groeser als 0 ist (auch wenn das Item eine groessere Meta hat)",
				"Trage dann stattdessen 0 ein da alle Texturen in dieser Meta gespeichert wurden.");
	}
	
	@Override
	public ArrayList<ISubCommand> getSubCommands()
	{
		return new ArrayList<ISubCommand>();
	}
	
	@Override
	public boolean isCommandRunnable(ICommandSender par1, boolean guiAdding, ISubCommand sub, String[] arg)
	{
		if(par1.getCommandSenderName().equalsIgnoreCase("Speiger") || par1.getCommandSenderName().equalsIgnoreCase("AlexZockerify"))
		{
			return true;
		}
		return false;
	}
	
	@Override
	public void runCommand(ICommandSender par1, ISubCommand sub, String[] arg)
	{
		if(arg.length == 3)
		{
			int id = Integer.parseInt(arg[0]);
			int meta = Integer.parseInt(arg[1]);
			String result = arg[2];
			if(result.equalsIgnoreCase("Block"))
			{
				BlockData data = new BlockData(Block.blocksList[id], meta);
				String[]text = TextureEngine.getTextures().getTextureNames(data);	
				String end = "";
				for(String key : text)
				{
					end = end + " : "+key;
				}
				par1.sendChatToPlayer(LangProxy.getText("Texture Info: "+end));
			}
			else if(result.equalsIgnoreCase("Item"))
			{
				ItemData data = new ItemData(Item.itemsList[id], meta);
				String[]text = TextureEngine.getTextures().getTextureNames(data);	
				String end = "";
				for(String key : text)
				{
					end = end + " : "+key;
				}
				
				par1.sendChatToPlayer(LangProxy.getText("Texture Info: "+end));
			}
			else
			{
				par1.sendChatToPlayer(LangProxy.getText("argument 3 heist nicht Block oder Item"));
			}
		}
		else
		{
			par1.sendChatToPlayer(LangProxy.getText("3 Argumente, ID, Meta, Block/Item"));
		}
	}
	
}
