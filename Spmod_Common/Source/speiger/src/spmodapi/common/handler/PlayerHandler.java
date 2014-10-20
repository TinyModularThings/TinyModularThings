package speiger.src.spmodapi.common.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import speiger.src.spmodapi.common.entity.SpmodFoodStats;
import speiger.src.spmodapi.common.util.proxy.CodeProxy;
import cpw.mods.fml.common.IPlayerTracker;

public class PlayerHandler implements IPlayerTracker
{
	
	@Override
	public void onPlayerLogin(EntityPlayer player)
	{
		if(player.username.equalsIgnoreCase("Speiger"))
		{
			this.initSpeiger(player);
		}
	}
	
	@Override
	public void onPlayerLogout(EntityPlayer player)
	{
		
	}
	
	@Override
	public void onPlayerChangedDimension(EntityPlayer player)
	{
		
	}
	
	@Override
	public void onPlayerRespawn(EntityPlayer player)
	{
		if(player.username.equalsIgnoreCase("Speiger"))
		{
			this.initSpeiger(player);
		}
	}
	
	public void initSpeiger(EntityPlayer player)
	{
		try
		{
			FoodStats food = CodeProxy.getField(EntityPlayer.class, FoodStats.class, player, 5);
			food = new SpmodFoodStats(food);
			CodeProxy.setField(EntityPlayer.class, player, 5, food);
		}
		catch(Exception e)
		{
		}
	}
	
}
