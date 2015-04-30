package speiger.src.spmodapi.common.handler;

import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.FoodStats;
import speiger.src.api.common.data.nbt.INBTReciver;
import speiger.src.api.common.registry.helpers.SpmodMod;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.entity.SpmodFoodStats;
import speiger.src.spmodapi.common.items.trades.TradePacket;
import speiger.src.spmodapi.common.util.proxy.CodeProxy;
import speiger.src.spmodapi.common.util.proxy.LangProxy;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.common.Loader;

public class PlayerHandler implements IPlayerTracker, INBTReciver
{
	public static HashMap<String, InventoryEnderChest> enderInventory = new HashMap<String, InventoryEnderChest>();
	static PlayerHandler instance = new PlayerHandler();
	static HashMap<String, HashMap<String, Integer>> numbers = new HashMap<String, HashMap<String, Integer>>();
	static HashMap<String, HashMap<String, Boolean>> flags = new HashMap<String, HashMap<String, Boolean>>();
	
	
	
	public static PlayerHandler getInstance()
	{
		return instance;
	}
	
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
		this.clearData(player);
		this.loadDataFromPlayer(player);
		
		if(Loader.isModLoaded("inventorytweaks"))
		{
			player.sendChatToPlayer(LangProxy.getText("You have Inventory Tweaks installed, That means that you can not use Shiftlicking Items into a Inventory in any of the mods that require SpmodAPI!", EnumChatFormatting.RED));
		}
		TradePacket.instance.sendPacketToClient(player);
	}
	
	@Override
	public void onPlayerLogout(EntityPlayer player)
	{
		enderInventory.put(player.username, player.getInventoryEnderChest());
		this.writeDataToPlayer(player);
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
	
	public void setPlayerNumber(EntityPlayer par1, String req, int value)
	{
		setPlayerNumber(par1.username, req, value);
	}
	
	public void setPlayerNumber(String par1, String req, int value)
	{
		checkValidNumberData(par1, req);
		numbers.get(par1).put(req, value);
	}
	
	
	public int getPlayerNumber(EntityPlayer par1, String req)
	{
		return getPlayerNumber(par1.username, req);
	}
	
	public int getPlayerNumber(String par1, String req)
	{
		checkValidNumberData(par1, req);
		return numbers.get(par1).get(req);
	}
	
	private void checkValidNumberData(String par1, String req)
	{
		HashMap<String, Integer> data = numbers.get(par1);
		if(data == null)
		{
			data = new HashMap<String, Integer>();
			numbers.put(par1, data);
		}
		if(!data.containsKey(req))
		{
			loadDefaultValue(par1, req, true);
		}
	}
	
	public void setPlayerFlag(EntityPlayer par1, String flag, boolean value)
	{
		setPlayerFlag(par1.username, flag, value);
	}
	
	public void setPlayerFlag(String par1, String flag, boolean value)
	{
		this.checkValidFlagData(par1, flag);
		flags.get(par1).put(flag, value);
	}
	
	public boolean getPlayerFlag(EntityPlayer par1, String flag)
	{
		return getPlayerFlag(par1.username, flag);
	}
	
	public boolean getPlayerFlag(String par1, String flag)
	{
		checkValidFlagData(par1, flag);
		return flags.get(par1).get(flag);
	}
	
	private void checkValidFlagData(String par1, String flag)
	{
		HashMap<String, Boolean> data = flags.get(par1);
		if(data == null)
		{
			data = new HashMap<String, Boolean>();
			flags.put(par1, data);
		}
		if(!data.containsKey(flag))
		{
			loadDefaultValue(par1, flag, false);
		}
	}
	
	
	public void clearData(EntityPlayer par1)
	{
		if(numbers.containsKey(par1.username))
		{
			numbers.remove(par1.username);
		}
		
		if(flags.containsKey(par1.username))
		{
			flags.remove(par1.username);
		}
	}
	
	public void loadDataFromPlayer(EntityPlayer par1)
	{
		checkPlayerData(par1);
		NBTTagCompound data = par1.getEntityData().getCompoundTag("SpmodAPIData").getCompoundTag("PlayerData");
		NBTTagList list = data.getTagList("Numbers");
		HashMap<String, Integer> nrs = new HashMap<String, Integer>();
		for(int i = 0;i<list.tagCount();i++)
		{
			NBTTagCompound nbt = (NBTTagCompound)list.tagAt(i);
			String key = nbt.getString("Key");
			int value = nbt.getInteger("Value");
			nrs.put(key, value);
		}
		numbers.put(par1.username, nrs);
		list = data.getTagList("Flags");
		HashMap<String, Boolean> flag = new HashMap<String, Boolean>();
		for(int i = 0;i<list.tagCount();i++)
		{
			NBTTagCompound nbt = (NBTTagCompound)list.tagAt(i);
			String key = nbt.getString("Key");
			boolean value = nbt.getBoolean("Value");
			flag.put(key, value);
		}
		flags.put(par1.username, flag);
	}
	
	public void checkPlayerData(EntityPlayer par1)
	{
		if(!par1.getEntityData().hasKey("SpmodAPIData"))
		{
			par1.getEntityData().setCompoundTag("SpmodAPIData", new NBTTagCompound());
		}
	}
	
	public void writeDataToPlayer(EntityPlayer par1)
	{
		checkPlayerData(par1);
		NBTTagCompound data = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		if(numbers.containsKey(par1.username))
		{
			HashMap<String, Integer> ints = numbers.get(par1.username);
			if(ints != null)
			{
				for(Entry<String, Integer> entry : ints.entrySet())
				{
					NBTTagCompound nbt = new NBTTagCompound();
					nbt.setString("Key", entry.getKey());
					nbt.setInteger("Value", entry.getValue());
					list.appendTag(nbt);
				}
			}
		}
		data.setTag("Numbers", list);
		list = new NBTTagList();
		if(flags.containsKey(par1.username))
		{
			HashMap<String, Boolean> flag = flags.get(par1.username);
			if(flag != null)
			{
				for(Entry<String, Boolean> entry : flag.entrySet())
				{
					NBTTagCompound nbt = new NBTTagCompound();
					nbt.setString("Key", entry.getKey());
					nbt.setBoolean("Value", entry.getValue());
					list.appendTag(nbt);
				}
			}
		}
		data.setTag("Flags", list);
		par1.getEntityData().getCompoundTag("SpmodAPIData").setCompoundTag("PlayerData", data);
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
		NBTTagList list = par1.getTagList("EnderData");
		for(int i = 0;i<list.tagCount();i++)
		{
			NBTTagCompound nbt = (NBTTagCompound)list.tagAt(i);
			InventoryEnderChest chest = new InventoryEnderChest();
			chest.loadInventoryFromNBT(nbt.getTagList("Value"));
			enderInventory.put(nbt.getString("Key"), chest);
		}
		list = par1.getTagList("NumberData");
		
		for(int i = 0;i<list.tagCount();i++)
		{
			NBTTagCompound data = (NBTTagCompound)list.tagAt(i);
			HashMap<String, Integer> nrs = new HashMap<String, Integer>();
			NBTTagList valueList = data.getTagList("Value");
			for(int z = 0;z<valueList.tagCount();z++)
			{
				NBTTagCompound valueData = (NBTTagCompound)valueList.tagAt(z);
				String key = valueData.getString("Key");
				int value = valueData.getInteger("Value");
				nrs.put(key, value);
			}
			numbers.put(data.getString("Key"), nrs);
		}
		list = par1.getTagList("FlagData");
		for(int i = 0;i<list.tagCount();i++)
		{
			NBTTagCompound data = (NBTTagCompound)list.tagAt(i);
			NBTTagList valueList = data.getTagList("Value");
			HashMap<String, Boolean> flag = new HashMap<String, Boolean>();
			
			for(int z = 0;z<valueList.tagCount();z++)
			{
				NBTTagCompound valueData = (NBTTagCompound)valueList.tagAt(i);
				flag.put(valueData.getString("Key"), valueData.getBoolean("Value"));
			}
			flags.put(data.getString("Value"), flag);
		}
	}

	@Override
	public void saveToNBT(NBTTagCompound par1)
	{
		NBTTagList list = new NBTTagList();
		for(Entry<String, InventoryEnderChest> entry : enderInventory.entrySet())
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("Key", entry.getKey());
			nbt.setTag("Value", entry.getValue().saveInventoryToNBT());
			list.appendTag(nbt);
		}
		par1.setTag("EnderData", list);
		
		list = new NBTTagList();
		for(Entry<String, HashMap<String, Integer>> entry : numbers.entrySet())
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("Key", entry.getKey());
			
			NBTTagList valueList = new NBTTagList();
			for(Entry<String, Integer> subEntry : entry.getValue().entrySet())
			{
				NBTTagCompound NBTData = new NBTTagCompound();
				NBTData.setString("Key", subEntry.getKey());
				NBTData.setInteger("Value", subEntry.getValue());
				valueList.appendTag(NBTData);
			}
			
			nbt.setTag("Value", valueList);
			list.appendTag(nbt);
		}
		par1.setTag("NumberData", list);
		
		list = new NBTTagList();
		for(Entry<String, HashMap<String, Boolean>> entry : flags.entrySet())
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("Key", entry.getKey());
			NBTTagList valueList = new NBTTagList();
			for(Entry<String, Boolean> subEntry : entry.getValue().entrySet())
			{
				NBTTagCompound data = new NBTTagCompound();
				data.setString("Key", subEntry.getKey());
				data.setBoolean("Value", subEntry.getValue());
				valueList.appendTag(data);
			}
			nbt.setTag("Value", valueList);
			list.appendTag(nbt);
		}
		par1.setTag("FlagData", list);
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
	
	
	void loadDefaultValue(String username, String key, boolean number)
	{
		if(number)
		{
			HashMap<String, Integer> data = numbers.get(username);
			int defaultValue = 0;
			if(key.equalsIgnoreCase("Counter"))
			{
				defaultValue = -50;
			}
			data.put(key, defaultValue);
		}
		else
		{
			HashMap<String, Boolean> data = flags.get(username);
			boolean defaultValue = false;
			
			data.put(key, defaultValue);
		}
	}
	
}
