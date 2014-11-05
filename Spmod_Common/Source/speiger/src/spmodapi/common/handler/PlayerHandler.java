package speiger.src.spmodapi.common.handler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.FoodStats;
import speiger.src.api.common.data.nbt.INBTReciver;
import speiger.src.api.common.registry.helpers.SpmodMod;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.entity.SpmodFoodStats;
import speiger.src.spmodapi.common.util.proxy.CodeProxy;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.IPlayerTracker;

public class PlayerHandler implements IPlayerTracker, INBTReciver
{
	public static HashMap<String, InventoryEnderChest> enderInventory = new HashMap<String, InventoryEnderChest>();
	
	@Override
	public void onPlayerLogin(EntityPlayer player)
	{
		if(player.username.equalsIgnoreCase("Speiger"))
		{
			this.initSpeiger(player);
		}
		if(enderInventory.containsKey(player.username))
		{
			InventoryEnderChest inv = enderInventory.remove(player.username);
			player.getInventoryEnderChest().loadInventoryFromNBT(inv.saveInventoryToNBT());
		}
	}
	
	@Override
	public void onPlayerLogout(EntityPlayer player)
	{
		enderInventory.put(player.username, player.getInventoryEnderChest());
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

	@Override
	public void loadFromNBT(NBTTagCompound par1)
	{
		enderInventory.clear();
		NBTTagList list = par1.getTagList("Data");
		for(int i = 0;i<list.tagCount();i++)
		{
			NBTTagCompound nbt = (NBTTagCompound)list.tagAt(i);
			InventoryEnderChest chest = new InventoryEnderChest();
			chest.loadInventoryFromNBT(nbt.getTagList("Value"));
			enderInventory.put(nbt.getString("Key"), chest);
		}
	}

	@Override
	public void saveToNBT(NBTTagCompound par1)
	{
		NBTTagList list = new NBTTagList();
		Iterator<Entry<String, InventoryEnderChest>> iter = enderInventory.entrySet().iterator();
		for(;iter.hasNext();)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			Entry<String, InventoryEnderChest> entry = iter.next();
			nbt.setString("Key", entry.getKey());
			nbt.setTag("Value", entry.getValue().saveInventoryToNBT());
			list.appendTag(nbt);
		}
		par1.setTag("Data", list);
	}

	@Override
	public void finishLoading()
	{
		
	}

	@Override
	public SpmodMod getOwner()
	{
		return SpmodAPI.instance;
	}

	@Override
	public String getID()
	{
		return "enderstorage";
	}
	
	public static InventoryEnderChest getEnderChestFromUsername(String par1)
	{
		if(enderInventory.containsKey(par1))
		{
			return enderInventory.get(par1);
		}
		EntityPlayer player = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(par1);
		if(player != null)
		{
			return player.getInventoryEnderChest();
		}
		return null;
	}
	
}
