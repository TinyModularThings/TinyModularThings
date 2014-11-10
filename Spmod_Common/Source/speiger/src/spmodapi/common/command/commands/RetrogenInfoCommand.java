package speiger.src.spmodapi.common.command.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;
import speiger.src.api.common.utils.MathUtils;
import speiger.src.spmodapi.common.command.ISpmodCommand;
import speiger.src.spmodapi.common.command.ISubCommand;
import speiger.src.spmodapi.common.util.proxy.LangProxy;
import speiger.src.spmodapi.common.world.retrogen.AdvancedRetrogen;
import speiger.src.spmodapi.common.world.retrogen.AdvancedRetrogen.ChunkData;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class RetrogenInfoCommand implements ISpmodCommand
{
	
	@Override
	public String getCommandName()
	{
		return "Advanced Retrogen";
	}
	
	@Override
	public List<String> getCommandUsage()
	{
		return Arrays.asList("Give you information about the Current Advanced Retrogen",
				"<Boolean (true/false)> to enable to dissable the Advanced Retrogen");
	}
	
	@Override
	public ArrayList<ISubCommand> getSubCommands()
	{
		return new ArrayList<ISubCommand>();
	}
	
	@Override
	public boolean isCommandRunnable(ICommandSender par1, boolean guiAdding, ISubCommand sub, String[] arg)
	{
		if(FMLCommonHandler.instance().getSidedDelegate().getSide() == Side.SERVER)
		{
			if(FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().isPlayerOpped(par1.getCommandSenderName()))
			{
				return true;
			}
			return false;
		}
		
		return true;
	}
	
	@Override
	public void runCommand(ICommandSender par1, ISubCommand sub, String[] arg)
	{
		if(AdvancedRetrogen.getConfigWork())
		{
			if(arg.length == 1)
			{
				if(arg[0].equalsIgnoreCase("true") || arg[0].equalsIgnoreCase("false"))
				{
					boolean result = Boolean.parseBoolean(arg[0]);
					AdvancedRetrogen.setRetrogenState(result);
					par1.sendChatToPlayer(LangProxy.getText(result ? "Enabled Full World Retrogen" : "Dissabled Full World Retrogen"));
				}
				else
				{
					par1.sendChatToPlayer(LangProxy.getText("Nothing Happend. Read Info"));
				}
			}
			else
			{
				AdvancedRetrogen gen = AdvancedRetrogen.getInstance();
				if(gen == null)
				{
					AdvancedRetrogen.setRetrogenState(true);
					gen = AdvancedRetrogen.getInstance();
					AdvancedRetrogen.setRetrogenState(false);
				}
				if(gen == null)
				{
					par1.sendChatToPlayer(LangProxy.getText("Sorry Something Happend While the command loaded the Retrogen data"));
				}
				HashMap<Integer, ArrayList<ChunkData>> list = gen.getData();
				Iterator<Entry<Integer, ArrayList<ChunkData>>> iter = list.entrySet().iterator();
				for(;iter.hasNext();)
				{
					Entry<Integer, ArrayList<ChunkData>> data = iter.next();
					WorldProvider pro = DimensionManager.getProvider(data.getKey());
					par1.sendChatToPlayer(LangProxy.getText("DimensionID: "+data.getKey()+" "+pro.getDimensionName()+" Has Still "+data.getValue().size()+" Chunks to Retrogenerate. It will Take: "+MathUtils.getTicksInTimeShort(data.getValue().size()*gen.getTickSpeed())+"min"));
				}
			}
		}
		else
		{
			par1.sendChatToPlayer(LangProxy.getText("Full World Retrogen is not Running", EnumChatFormatting.DARK_RED));
		}
	}
	
}
