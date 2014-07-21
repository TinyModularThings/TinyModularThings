package speiger.src.spmodapi.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;

import speiger.src.spmodapi.common.items.crafting.ItemRandomTrade;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.world.World;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.registry.VillagerRegistry;

public class CountdownTick implements ITickHandler
{
	private static boolean loadedRecipes = false;
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
		
	}
	
	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{
		EntityPlayer player = (EntityPlayer) tickData[0];
		
		if(!loadedRecipes)
		{
			loadedRecipes = true;
			World world = player.worldObj;
			
			Collection<Integer> recipes = VillagerRegistry.getRegisteredVillagers();
			recipes.add(Integer.valueOf(0));
			recipes.add(Integer.valueOf(1));
			recipes.add(Integer.valueOf(2));
			recipes.add(Integer.valueOf(3));
			recipes.add(Integer.valueOf(4));
			recipes.add(Integer.valueOf(5));
			ArrayList<MerchantRecipe> recipe = new ArrayList<MerchantRecipe>();
			for(Integer ints : recipes)
			{
				EntityVillager villager = new EntityVillager(world, ints.intValue());
				recipe.addAll(villager.getRecipes(player));
			}
			ItemRandomTrade.addRecipes(recipe);
		}
		
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
		
		TickHelper.getInstance().tick();
		
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
