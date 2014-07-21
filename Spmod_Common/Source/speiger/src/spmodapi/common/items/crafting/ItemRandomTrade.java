package speiger.src.spmodapi.common.items.crafting;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import speiger.src.api.items.IItemGui;
import speiger.src.api.util.SpmodMod;
import speiger.src.spmodapi.common.items.SpmodItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemRandomTrade extends SpmodItem implements IItemGui
{
	
	static ArrayList<MerchantRecipe> recipeList = new ArrayList<MerchantRecipe>();
	
	public ItemRandomTrade(int par1)
	{
		super(par1);
		this.setHasSubtypes(true);
	}

	@Override
	public void registerItems(int id, SpmodMod par0)
	{
		
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod par0)
	{
		return null;
	}

	@Override
	public boolean hasGui(ItemStack par1)
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiScreen getGui(ItemStack par1)
	{
		return null;
	}

	@Override
	public boolean hasContainer(ItemStack par1)
	{
		return true;
	}

	@Override
	public Container getContainer(ItemStack par1)
	{
		return null;
	}
	
	public static void addRecipes(ArrayList<MerchantRecipe> par1)
	{
		recipeList.addAll(par1);
	}
	
	
}
