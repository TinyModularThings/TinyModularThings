package speiger.src.spmodapi.common.util;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraft.village.MerchantRecipe;
import cpw.mods.fml.common.FMLLog;

public class VillagerHelper
{
	public static void loadVillagerRecipes(ArrayList<MerchantRecipe> recipe)
	{
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.wheat), false));
		recipe.addAll(getRecipeFromSize(new ItemStack(Block.cloth), false));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.chickenRaw), false));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.fishCooked), false));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.paper), false));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.book), false));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.writtenBook), false));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.coal), false));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.ingotIron), false));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.ingotGold), false));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.diamond), false));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.porkRaw), false));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.beefRaw), false));
		
		recipe.addAll(getRecipeFromSize(new ItemStack(Block.bookShelf), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.bread), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.melon), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.appleRed), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.cookie), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.shears), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.flintAndSteel), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.chickenCooked), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.arrow), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Block.glass), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.compass), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.pocketSundial), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.eyeOfEnder), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.expBottle), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.redstone), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Block.glowStone), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.swordIron), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.swordDiamond), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.axeIron), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.axeDiamond), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.pickaxeIron), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.pickaxeDiamond), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.shovelIron), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.shovelDiamond), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.hoeIron), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.hoeDiamond), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.bootsIron), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.bootsDiamond), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.helmetIron), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.helmetDiamond), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.plateIron), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.plateDiamond), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.legsIron), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.legsDiamond), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.bootsChain), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.helmetChain), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.plateChain), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.legsChain), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.saddle), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.plateLeather), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.bootsLeather), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.helmetLeather), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.legsLeather), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.porkCooked), true));
		recipe.addAll(getRecipeFromSize(new ItemStack(Item.beefCooked), true));
		
		for(int i = 4;i<6;i++)
		{
			recipe.add(new MerchantRecipe(new ItemStack(Block.gravel, 10), new ItemStack(Item.emerald), new ItemStack(Item.flint.itemID, i, 0)));
		}
		

	}
	
	private static ArrayList<MerchantRecipe> getRecipeFromSize(ItemStack par1, boolean par2)
	{
		ArrayList<MerchantRecipe> recipes = new ArrayList<MerchantRecipe>();
		if(par2)
		{	
			Tuple par3 = (Tuple) EntityVillager.blacksmithSellingList.get(Integer.valueOf(par1.itemID));
			int key = (Integer) par3.getFirst();
			int value = (Integer) par3.getSecond();
			
			ItemStack itemKey = null;
			ItemStack itemValue = null;
			
			if(key < 0 && value < 0)
			{
				key = -key;
				value = -value;
				
				itemKey = new ItemStack(Item.emerald, 1, 0);
				for(int i = value;i<key;i++)
				{
					itemValue = new ItemStack(par1.getItem(), i, par1.getItemDamage());
					recipes.add(new MerchantRecipe(itemKey, itemValue));
				}
			}
			else
			{
				itemValue = new ItemStack(par1.getItem(), 1, par1.getItemDamage());
				for(int i = key;i<value;i++)
				{
					itemKey = new ItemStack(Item.emerald, i, 0);
					recipes.add(new MerchantRecipe(itemKey, itemValue));
				}
			}
			
		}
		else
		{
			Tuple par3 = (Tuple)EntityVillager.villagerStockList.get(Integer.valueOf(par1.itemID));
			FMLLog.getLogger().info("Test: "+par1.getDisplayName());
			int key = (Integer) par3.getFirst();
			int value = (Integer) par3.getSecond();
			
			if(key == value)
			{
				recipes.add(new MerchantRecipe(new ItemStack(par1.getItem(), key, par1.getItemDamage()), new ItemStack(Item.emerald)));
			}
			else
			{
				for(int i = key;i<value;i++)
				{
					recipes.add(new MerchantRecipe(new ItemStack(par1.getItem(), i, par1.getItemDamage()), new ItemStack(Item.emerald)));
				}
			}
		}
		return recipes;
	}
}
