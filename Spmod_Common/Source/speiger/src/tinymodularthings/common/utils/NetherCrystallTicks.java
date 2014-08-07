package speiger.src.tinymodularthings.common.utils;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.Ticks.ITickReader;
import speiger.src.tinymodularthings.TinyModularThings;

public class NetherCrystallTicks implements ITickReader
{
	
	ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
	
	@Override
	public void onTick(SpmodMod sender)
	{
		
	}
	
	@Override
	public SpmodMod getOwner()
	{
		return TinyModularThings.instance;
	}
	
}
