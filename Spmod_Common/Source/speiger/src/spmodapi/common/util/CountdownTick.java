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
import speiger.src.api.util.Ticks;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.blocks.utils.MobMachine;
import speiger.src.spmodapi.common.blocks.utils.MobMachine.DropType;
import speiger.src.spmodapi.common.items.trades.ItemRandomTrade;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.relauncher.Side;

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
			loadRecipes(player);

			
			MobMachine.addDrops(22, DropType.Common, ItemRandomTrade.getAllTrades());
		

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
		
		
	}
	
	@Override
	public EnumSet<TickType> ticks()
	{
		return EnumSet.of(TickType.PLAYER);
	}
	
	public static void loadRecipes(EntityPlayer player)
	{
		World world = player.worldObj;
		Collection<Integer> recipes = VillagerRegistry.getRegisteredVillagers();
		ArrayList<MerchantRecipe> recipe = new ArrayList<MerchantRecipe>();
		VillagerHelper.loadVillagerRecipes(recipe);
		for(Integer ints : recipes)
		{
			MerchantRecipeList cu = new MerchantRecipeList();
			EntityVillager villager = new EntityVillager(world, ints.intValue());
			cu.addAll(villager.getRecipes(player));
			VillagerRegistry.manageVillagerTrades(cu, villager, ints.intValue(), world.rand);
			recipe.addAll(cu);
		}
		for(int i = 0;i<6;i++)
		{
			MerchantRecipeList cu = new MerchantRecipeList();
			EntityVillager villager = new EntityVillager(world, i);
			cu.addAll(villager.getRecipes(player));
			VillagerRegistry.manageVillagerTrades(cu, villager, i, world.rand);
			recipe.addAll(cu);
		}
		
		
		ItemRandomTrade.addRecipes(recipe);
	}

	
	@Override
	public String getLabel()
	{
		return "Countdown TickHandler";
	}
	
}
