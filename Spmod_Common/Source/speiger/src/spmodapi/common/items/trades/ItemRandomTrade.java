package speiger.src.spmodapi.common.items.trades;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.village.MerchantRecipe;
import speiger.src.api.client.gui.IItemGui;
import speiger.src.spmodapi.client.gui.items.trades.GuiTrade;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.items.core.SpmodItem;
import speiger.src.spmodapi.common.util.TickHelper;
import speiger.src.spmodapi.common.util.proxy.CodeProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemRandomTrade extends SpmodItem implements IItemGui
{
	static ArrayList<MerchantRecipe> recipeList = new ArrayList<MerchantRecipe>();
	static boolean secondTry = false;
	
	public ItemRandomTrade(int par1)
	{
		super(par1);
		this.setHasSubtypes(true);
//		this.setCreativeTab(APIUtils.tabHemp);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int par1)
	{
		return super.getIconFromDamage(0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3)
	{
		par3.add(new ItemStack(par1, 1, 0));
		for (int i = 1; i < recipeList.size(); i++)
		{
			par3.add(new ItemStack(par1, 1, i));
		}
	}
	
	@Override
	public boolean hasGui(ItemStack par1)
	{
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiScreen getGui(InventoryPlayer par1, ItemStack par2)
	{
		return new GuiTrade(par1, (IItemGui) par2.getItem());
	}
	
	@Override
	public boolean hasContainer(ItemStack par1)
	{
		return true;
	}
	
	@Override
	public Container getContainer(InventoryPlayer par1, ItemStack par2)
	{
		return new TradeInventory(par1, new BasicTradeInventory(3));
	}
	
	public static void addRecipes(ArrayList<MerchantRecipe> par1)
	{
		for (int i = 0; i < par1.size(); i++)
		{
			MerchantRecipe recipe = par1.get(i);
			if (!recipeList.contains(recipe))
			{
				recipeList.add(recipe);
			}
			
		}
		for (int i = 0; i < recipeList.size(); i++)
		{
			NBTTagCompound nbt = recipeList.get(i).writeToTags();
			/**
			 * @Note Need to find a balance of the Max Trades.
			 */
			nbt.setInteger("maxUses", 3);
			recipeList.get(i).readFromTags(nbt);
		}
	}
	
	public static ItemStack[] getAllTrades()
	{
		ItemStack[] array = new ItemStack[recipeList.size()];
		for (int i = 0; i < array.length; i++)
		{
			array[i] = new ItemStack(APIItems.trades, 1, i);
		}
		return array;
	}
	
	public static MerchantRecipe getRecipeFromItem(ItemStack par1)
	{
		if (par1.itemID == APIItems.trades.itemID)
		{
			return recipeList.get(par1.getItemDamage());
		}
		return new MerchantRecipe((ItemStack) null, (ItemStack) null);
	}

	public static ItemStack getRandomTrade()
	{
		return new ItemStack(APIItems.trades, 1, CodeProxy.getRandom().nextInt(recipeList.size()));
	}
	
	@Override
	public String getName(ItemStack par1)
	{
		return "Random Trade";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1, EntityPlayer par2, List par3, boolean par4)
	{
		if(recipeList.isEmpty())
		{
			if(!secondTry)
			{
				secondTry = true;
				TickHelper.loadRecipes(par2);
			}
			par3.add("No Trades Aviable");
			return;
		}
		
		if(recipeList.size() > par1.getItemDamage())
		{
			MerchantRecipe trade = recipeList.get(par1.getItemDamage());
			String text = "Trade: "+trade.getItemToBuy().stackSize+"x"+trade.getItemToBuy().getDisplayName();
			
			if(trade.hasSecondItemToBuy())
			{
				text = text+" + "+trade.getSecondItemToBuy().stackSize+"x"+trade.getSecondItemToBuy().getDisplayName();
			}
			text = text+" = "+trade.getItemToSell().stackSize+"x"+trade.getItemToSell().getDisplayName();
			par3.add(text);
		}
	}
	
	
}
