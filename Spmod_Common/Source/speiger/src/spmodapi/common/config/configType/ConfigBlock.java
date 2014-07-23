package speiger.src.spmodapi.common.config.configType;

import net.minecraft.block.Block;
import net.minecraftforge.common.ConfigCategory;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

/**
 * 
 * @author Speiger
 * 
 */
public class ConfigBlock
{
	private int ID;
	private int startID;
	
	public ConfigBlock(int StartID)
	{
		ID = StartID;
		startID = ID;
	}
	
	public ConfigBlock updateToNextID()
	{
		ID++;
		return this;
	}
	
	public int getCurrentID()
	{
		if (Block.blocksList[ID] != null)
		{
			throw new IndexOutOfBoundsException("BlockID is already used: " + ID);
		}
		return ID;
	}
	
	public static int getConfig(Configuration par1, String category, int DefaultID)
	{
		Property id = par1.get(category, "Block Start ID", DefaultID, "StartID for the Blocks");
		return Integer.parseInt(id.getString());
	}
	
	public void setEnd(Configuration config, String cat)
	{
		int endID = ID - startID;
		ConfigCategory cot = config.getCategory(cat);
		if(cot != null && cot.containsKey("Block Start ID"))
		{
			Property items = cot.get("Block Start ID");
			items.comment = String.format("%s%n%s%n%s", items.comment, "You need to Hold "+endID+" BlockIDs Free", "(From: "+startID+" BlockID To: "+ID+" BlockID)");
			cot.put("Block Start ID", items);
		}
	}
	
}
