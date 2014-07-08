package speiger.src.spmodapi.common.command.commands;

import java.util.ArrayList;
import java.util.List;

import speiger.src.spmodapi.common.command.ISubCommand;

public class BasicSubCommand implements ISubCommand
{
	private String par2;
	private String[] par3;
	
	public BasicSubCommand(String name, String... desc)
	{
		par2 = name;
		par3 = desc;
	}
	
	@Override
	public String getSubCommandName()
	{
		return par2;
	}
	
	@Override
	public List<String> getSubCommandDescription()
	{
		ArrayList<String> text = new ArrayList<String>();
		for(String par1 : par3)
		{
			text.add(par1);
		}
		return text;
	}
	
}
