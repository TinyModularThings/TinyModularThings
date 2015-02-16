package speiger.src.spmodapi.common.util;

import java.util.EnumSet;
import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import speiger.src.spmodapi.common.entity.SpmodFoodStats;
import speiger.src.spmodapi.common.handler.PlayerHandler;
import speiger.src.spmodapi.common.util.proxy.LangProxy;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class CountdownTick implements ITickHandler
{
	
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
		EntityPlayer player = (EntityPlayer) tickData[0];
		if(SpmodFoodStats.hardcorePeacefull.get(player.username) != null && SpmodFoodStats.hardcorePeacefull.get(player.username))
		{
			if(player.ticksExisted % 20 * 12 == 0)
			{
				player.ticksExisted++;
			}
		}
	}
	
	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{
		EntityPlayer player = (EntityPlayer) tickData[0];
		PlayerHandler handler = PlayerHandler.getInstance();
		HashMap<String, Integer> numb = handler.numbers.get(player.username);
		if(numb != null && numb.containsKey("Counter"))
		{
			int counter = numb.get("Counter");
			counter--;
			if(counter < 0 && counter > -50)
			{
				player.sendChatToPlayer(LangProxy.getText("Countdown is Over", EnumChatFormatting.AQUA));
			}
			else if(counter <= -50)
			{
				numb.remove("Counter");
			}
		}
		
	}
	
	@Override
	public EnumSet<TickType> ticks()
	{
		return EnumSet.of(TickType.PLAYER);
	}
	

	@Override
	public String getLabel()
	{
		return "Countdown TickHandler";
	}
	
}
