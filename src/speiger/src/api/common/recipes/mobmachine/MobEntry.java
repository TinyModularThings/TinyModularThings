package speiger.src.api.common.recipes.mobmachine;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public abstract class MobEntry
{
	private final int id;
	private static Item uselessItem;
	
	public MobEntry(int par1)
	{
		id = par1;
	}
	
	public final int getID()
	{
		return id;
	}
	//use that if you have no drop.. It is not forced but use it.
	public final ItemStack getUselessItem(int amount)
	{
		if(uselessItem == null)
		{
			getItem();
		}
		if(uselessItem == null)
		{
			return new ItemStack(Items.carrot_on_a_stick);
		}
		return new ItemStack(uselessItem, amount);
	}
	
	public final ItemStack getUselessItem()
	{
		return getUselessItem(1);
	}
	
	private void getItem()
	{
		try
		{
			uselessItem = (Item)Class.forName("speiger.src.spmodapi.common.config.modObjects.APIItems").getField("uselessItem").get(null);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 *First output. Most often called. If you have multiple Outputs then pick 1 randomly
	 */
	public abstract ItemStack getCommonOutput(Random rand);
	
	/**
	 * Second output. Most often called. If you have multiple Outputs then pick 1 randomly
	 */
	public abstract ItemStack getUncommonOutput(Random rand);
	
	/**
	 * Third output. Most often called. If you have multiple Outputs then pick 1 randomly
	 */
	public abstract ItemStack getRareOutput(Random rand);
	
	/**
	 * @return if this machine uses Exp or produces Exp. False = production, true = consume
	 */
	public abstract boolean usesExp();
	
	/**
	 * return the amount that get produced or drained
	 */
	public abstract int getExp();
	
	/**
	 * returns the ItemIcon and also the Front Icon for the MobMachine.
	 */
	public abstract IIcon getFrontIcon();
	
	/**
	 * @return the Side Icon for the MobMachine
	 */
	public abstract IIcon getSideIcon();
	
	/**
	 * @return the Display Name for the MobMachine
	 */
	public abstract String getName();
	
	/**
	 * This function defines how much food you gain from an Item.
	 * It does not have to be a ItemFood to be fine. It is based on,
	 * what that mob could eat.
	 * @param food is the Item that could be a food
	 * @return the amount of food it gains from the item. 0 means it is not a valid food
	 */
	public abstract int getFoodLevel(ItemStack food);
	
	/**
	 * It is only for a Nei Plugin.
	 * Returning a Empty list will hide all Items
	 * @return the Food Items
	 */
	public abstract List<FoodInfo> getAllFoodItems();
	
	/**
	 * Its only for a Nei Plugin. It does not effect the MobMachine at all
	 * @Note: The chance from the types are every time differend
	 * So Common Uncommon and Rare are split from the Chance. Keep that in mind
	 * @return the items that can come out of the MobMachine
	 */
	public abstract List<DropInfo> getAllDropItems();
	
	
	public static class FoodInfo
	{
		ItemStack item;
		int foodAmount;
		
		public FoodInfo(Block par1, int par2)
		{
			this(new ItemStack(par1), par2);
		}
		
		public FoodInfo(Item par1, int par2)
		{
			this(new ItemStack(par1), par2);
		}
		
		public FoodInfo(ItemStack par1, int par2)
		{
			item = par1;
			foodAmount = par2;
		}
		
		public ItemStack getItem()
		{
			return item;
		}
		
		public int getFoodAmount()
		{
			return foodAmount;
		}
	}
	
	public static class DropInfo
	{
		DropType type;
		ItemStack item;
		int chance;
		
		public DropInfo(Block par1, DropType par2, int par3)
		{
			this(new ItemStack(par1), par2, par3);
		}
		
		public DropInfo(Item par1, DropType par2, int par3)
		{
			this(new ItemStack(par1), par2, par3);
		}
		
		public DropInfo(ItemStack par1, DropType par2, int par3)
		{
			item = par1;
			type = par2;
			chance = par3;
		}
		
		public int getChance()
		{
			return chance;
		}
		
		public ItemStack getItem()
		{
			return item;
		}
		
		public DropType getType()
		{
			return type;
		}
	}
	
	public static enum DropType
	{
		Common,
		Uncommon,
		Rare;
	}
}
