package speiger.src.spmodapi.common.config.configType;

import net.minecraft.item.Item;
import net.minecraftforge.common.ConfigCategory;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

public class ConfigItem
{
	private int ID;
	private int startID;
	
	public ConfigItem(int StartID)
	{
		ID = StartID - 256;
		startID = ID;
	}
	
	public ConfigItem updateToNextID()
	{
		ID++;
		return this;
	}
	
	public int getCurrentID()
	{
		if (Item.itemsList[ID + 256] != null)
		{
			int end = ID+256;
			throw new IndexOutOfBoundsException("ItemID is already used: " + end);
		}
		return ID;
	}
	
	public static int getConfig(Configuration config, String categorie, int defaultID)
	{
		Property id = config.get(categorie, "Item StartID", defaultID, "StartID for the Items");
		return Integer.parseInt(id.getString());
	}
	
	public static int getConfig(Configuration config, String name, String categorie, int defaultID)
	{
		Property id = config.get(categorie, name, defaultID, "StartID for the Items");
		return Integer.parseInt(id.getString());
	}
	
	public void setEnd(Configuration config, String cat)
	{
		int endID = ID - startID;
		int startIDs = startID + 256;
		int IDs = ID + 256;
		ConfigCategory cot = config.getCategory(cat);
		if(cot != null && cot.containsKey("Item StartID"))
		{
			Property items = cot.get("Item StartID");
			items.comment = String.format("%s%n%s%n%s", items.comment, "You need to Hold "+endID+" ItemIDs Free", "(From: "+startIDs+" ItemID To: "+IDs+" ItemID)");
			cot.put("Item StartID", items);
		}
	}
	
}
