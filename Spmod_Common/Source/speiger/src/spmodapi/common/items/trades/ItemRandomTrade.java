package speiger.src.spmodapi.common.items.trades;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.world.World;
import speiger.src.api.items.DisplayStack;
import speiger.src.api.items.IItemGui;
import speiger.src.api.items.InfoStack;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.client.gui.items.trades.GuiTrade;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.enums.EnumGuiIDs;
import speiger.src.spmodapi.common.items.SpmodItem;
import speiger.src.spmodapi.common.lib.SpmodAPILib;
import speiger.src.spmodapi.common.util.CountdownTick;
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
		this.setCreativeTab(APIUtils.tabHemp);
	}
	
	@Override
	public void registerItems(int id, SpmodMod par0)
	{
		
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod par0)
	{
		return LanguageRegister.getLanguageName(new DisplayStack(par1), "trade.random", par0);
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
	public ItemStack onItemRightClick(ItemStack par1, World par2World, EntityPlayer par3EntityPlayer)
	{
		if (!par2World.isRemote)
		{
			if (par3EntityPlayer.isSneaking())
			{
				this.addRecipes(recipeList);
				par3EntityPlayer.sendChatToPlayer(LanguageRegister.createChatMessage("Relefresed Recipe List"));
			}
			else
			{
				par3EntityPlayer.openGui(SpmodAPI.instance, EnumGuiIDs.Items.getID(), par2World, 0, 0, 0);
			}
		}
		return par1;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1, EntityPlayer par2, List par3, boolean par4)
	{
		if (recipeList.isEmpty())
		{
			if (!secondTry)
			{
				secondTry = true;
				CountdownTick.loadRecipes(par2);
			}
			par3.add("No Trades Aviable");
			return;
		}
		
		MerchantRecipe recipe = recipeList.get(par1.getItemDamage());
		boolean first = recipe.getItemToBuy() != null;
		boolean second = recipe.getSecondItemToBuy() != null;
		
		if (first && !second)
		{
			par3.add("Trade: x" + recipe.getItemToBuy().stackSize + " " + recipe.getItemToBuy().getDisplayName() + " = x" + recipe.getItemToSell().stackSize + " " + recipe.getItemToSell().getDisplayName());
		}
		else if (!first && second)
		{
			par3.add("Trade: x" + recipe.getSecondItemToBuy().stackSize + " " + recipe.getSecondItemToBuy().getDisplayName() + " = x" + recipe.getItemToSell().stackSize + " " + recipe.getItemToSell().getDisplayName());
		}
		else
		{
			par3.add("Trade: x" + recipe.getItemToBuy().stackSize + " " + recipe.getItemToBuy().getDisplayName() + " + x" + recipe.getSecondItemToBuy().stackSize + " " + recipe.getSecondItemToBuy().getDisplayName() + " = x" + recipe.getItemToSell().stackSize + " " + recipe.getItemToSell().getDisplayName());
		}
		par3.add(LanguageRegister.getLanguageName(new InfoStack(), "trade.size", SpmodAPI.instance) + " " + recipeList.size());
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
	
}
