package speiger.src.spmodapi.common.network.packets.client;

import java.io.DataInput;
import java.io.DataOutput;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import speiger.src.api.common.data.packets.ISpmodPacket;
import speiger.src.spmodapi.common.command.CommandRegistry;
import speiger.src.spmodapi.common.command.ISpmodCommand;
import speiger.src.spmodapi.common.command.ISubCommand;
import speiger.src.spmodapi.common.util.proxy.LangProxy;
import cpw.mods.fml.relauncher.Side;

public class CommandPacket implements ISpmodPacket
{
	boolean sub;
	int id;
	int meta;
	String[] args;
	
	public CommandPacket()
	{
		this(false, -1, -1, "");
	}
	
	public CommandPacket(boolean par1, int par2, int par3, String...par4)
	{
		sub = par1;
		id = par2;
		meta = par3;
		args = par4;
	}
	
	@Override
	public void readData(DataInput par1) throws Exception
	{
		sub = par1.readBoolean();
		id = par1.readInt();
		meta = par1.readInt();
		String[] text = new String[par1.readShort()];
		for(int i = 0;i<text.length;i++)
		{
			text[i] = par1.readUTF();
		}
	}
	
	@Override
	public void writeData(DataOutput par1) throws Exception
	{
		par1.writeBoolean(sub);
		par1.writeInt(id);
		par1.writeInt(meta);
		par1.writeShort(args.length);
		for(int i = 0;i<args.length;i++)
		{
			par1.writeUTF(args[i]);
		}
	}
	
	@Override
	public void handlePacket(EntityPlayer par1, Side par2)
	{
		try
		{
			CommandRegistry comRegist = CommandRegistry.getInstance();
			ISpmodCommand com = this.getCommand(comRegist.getCommands(par1));
			if(com != null)
			{
				if(sub)
				{
					ISubCommand sub = getSubCommand(com);
					if(sub != null)
					{
						if(com.isCommandRunnable(par1, false, sub, args))
						{
							com.runCommand(par1, sub, args);
						}
						else
						{
							par1.sendChatToPlayer(LangProxy.getText("You can not Exceute this Command", EnumChatFormatting.RED));
						}
					}
					else
					{
						par1.sendChatToPlayer(LangProxy.getText("SubCommand Not Found", EnumChatFormatting.RED));
					}
				}
				else
				{
					if(com.isCommandRunnable(par1, false, null, args))
					{
						com.runCommand(par1, null, args);
					}
					else
					{
						par1.sendChatToPlayer(LangProxy.getText("You can not Exceute this Command", EnumChatFormatting.RED));
					}
				}
			}
			else
			{
				par1.sendChatToPlayer(LangProxy.getText("Command Not Found", EnumChatFormatting.RED));
			}
		}
		catch(Exception e)
		{
			par1.sendChatToPlayer(LangProxy.getText("Could not Execute Command", EnumChatFormatting.RED));
			e.printStackTrace();
		}
	}
	
	private ISpmodCommand getCommand(List<ISpmodCommand> par1)
	{
		if(par1 != null && par1.size() > id)
		{
			return par1.get(id);
		}
		return null;
	}
	
	private ISubCommand getSubCommand(ISpmodCommand par1)
	{
		List<ISubCommand> com = par1.getSubCommands();
		if(com != null && com.size() > meta)
		{
			return com.get(meta);
		}
		return null;
	}
	
}
