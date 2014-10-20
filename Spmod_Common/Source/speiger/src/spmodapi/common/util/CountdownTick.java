package speiger.src.spmodapi.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import speiger.src.spmodapi.common.blocks.utils.MobMachine;
import speiger.src.spmodapi.common.blocks.utils.MobMachine.DropType;
import speiger.src.spmodapi.common.items.trades.ItemRandomTrade;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.registry.VillagerRegistry;

public class CountdownTick implements ITickHandler
{
	
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
		
	}
	
	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{
		EntityPlayer player = (EntityPlayer) tickData[0];
		
		
		NBTTagCompound playerNBT = player.getEntityData();
		
		if (playerNBT.hasKey("SpmodAPIData"))
		{
			NBTTagCompound nbt = playerNBT.getCompoundTag("SpmodAPIData");
			if (nbt.hasKey("CountdownTime"))
			{
				int time = nbt.getInteger("CountdownTime");
				time--;
				nbt.setInteger("CountdownTime", time);
				if (time <= 0)
				{
					player.addChatMessage("Countdown is over");
					
					if (time <= -50)
					{
						nbt.removeTag("CountdownTime");
					}
				}
				
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
