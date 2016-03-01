package speiger.src.api.common.recipes.mobmachine;

import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import speiger.src.api.common.recipes.mobmachine.MobEntry.FoodInfo;

public class MobRegistry
{
	public static IMobRegistry registry;
	
	public static interface IMobRegistry
	{
		//256 IDs is the Limit for now
		
		public boolean registerMobEntry(MobEntry entry);
		
		public MobEntry getEntryForID(int id);
		
		public Map<Integer, MobEntry> getEntries();

		public boolean registerActivationItem(int id, ItemStack item);
		
		public List<ItemStack> getActivationItem(int id);
		
		public Map<Integer, List<ItemStack>> getActivationItems();
		
		public void registerMobFoodModifier(IMobFoodModifier mob);
	}
	
	public static interface IMobFoodModifier
	{
		/**
		 * This is for NotEnoughItems so please return here all values you support for that mob is requested.
		 */
		public List<FoodInfo> getFoodInfoForMob(MobEntry entry);
		
		/**
		 * Mob Machine ask for food. If you use a Modifier it allows you to add Extra Food
		 * 0 Means no food value
		 */
		public int getFoodValue(MobEntry entry, ItemStack food);
	}
}
