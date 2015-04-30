package speiger.src.spmodapi.common.util;

import java.util.*;
import java.util.Map.Entry;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import speiger.src.api.common.data.nbt.DataStorage;
import speiger.src.api.common.registry.helpers.SpmodMod;
import speiger.src.api.common.registry.helpers.Ticks.ITickReader;
import speiger.src.api.common.world.blocks.BlockPosition;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.blocks.cores.SpmodBlockContainerBase;
import speiger.src.spmodapi.common.blocks.cores.SpmodBlockContainerBase.DropEntry;
import speiger.src.spmodapi.common.blocks.utils.MobMachine;
import speiger.src.spmodapi.common.blocks.utils.MobMachine.DropType;
import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.items.trades.ItemRandomTrade;
import speiger.src.spmodapi.common.util.data.AccessConfig;
import speiger.src.spmodapi.common.world.retrogen.RetroGenTickHandler.OreReplacer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.relauncher.Side;

public class TickHelper implements ITickReader
{
	private static TickHelper helper = new TickHelper();
	private static int cDelay = 0;
	private static boolean recipesLoaded = false;
	
	public static TickHelper getInstance()
	{
		return helper;
	}
	
	public ArrayList<OreReplacer> regainOre = new ArrayList<OreReplacer>();
	
	public boolean isCloseToBackup()
	{
		return cDelay + 1000 >= SpmodConfig.integerInfos.get("SavingDelay");
	}
	
	public void tick()
	{
		if(!recipesLoaded)
		{
			recipesLoaded = true;
//			loadRecipes(FakePlayerFactory.getMinecraft(DimensionManager.getWorld(0)));
			MobMachine.addDrops(22, DropType.Common, ItemRandomTrade.getAllTrades());
			AccessConfig config = new AccessConfig(SpmodConfig.getInstance());
			config.loadLaterData();
		}
		
		
		for (int i = 0; i < regainOre.size(); i++)
		{
			OreReplacer cuOre = regainOre.remove(i);
			
			if (!cuOre.generate())
			{
				regainOre.add(cuOre);
			}
		}
		if (cDelay > SpmodConfig.integerInfos.get("SavingDelay"))
		{
			cDelay = 0;
			DataStorage.write(FMLCommonHandler.instance().getMinecraftServerInstance(), true);
		}
		if (cDelay > 40 && DataStorage.hasRequest())
		{
			cDelay -= 40;
			DataStorage.write(FMLCommonHandler.instance().getMinecraftServerInstance(), false);
		}
		if(!SpmodBlockContainerBase.tileDrops.isEmpty())
		{
			for(Entry<List<Integer>, DropEntry> entry : new WeakHashMap<List<Integer>, DropEntry>(SpmodBlockContainerBase.tileDrops).entrySet())
			{
				try
				{
					BlockPosition pos = new BlockPosition(entry.getKey());
					DropEntry value = entry.getValue();
					if(pos.getWorld() != null)
					{
						if(pos.getWorld().getTotalWorldTime() - value.getTimeAdded() > 6000)
						{
							SpmodBlockContainerBase.tileDrops.remove(entry.getKey());
						}
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void onTick(SpmodMod sender, Side side)
	{
		if (side != side.SERVER)
		{
			return;
		}
		tick();
	}
	
	public static void loadRecipes(EntityPlayer player)
	{
		World world = player.worldObj;
		Collection<Integer> recipes = VillagerRegistry.getRegisteredVillagers();
		ArrayList<MerchantRecipe> recipe = new ArrayList<MerchantRecipe>();
		VillagerHelper.loadVillagerRecipes(recipe);
		for (Integer ints : recipes)
		{
			MerchantRecipeList cu = new MerchantRecipeList();
			EntityVillager villager = new EntityVillager(world, ints.intValue());
			cu.addAll(villager.getRecipes(player));
			VillagerRegistry.manageVillagerTrades(cu, villager, ints.intValue(), world.rand);
			recipe.addAll(cu);
		}
		for (int i = 0; i < 6; i++)
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
	public SpmodMod getOwner()
	{
		return SpmodAPI.instance;
	}

	@Override
	public boolean needTick()
	{
		cDelay++;
		return cDelay > SpmodConfig.integerInfos.get("SavingDelay") || cDelay > 40 && DataStorage.hasRequest() || !recipesLoaded || regainOre.size() > 0;
	}
	
}
