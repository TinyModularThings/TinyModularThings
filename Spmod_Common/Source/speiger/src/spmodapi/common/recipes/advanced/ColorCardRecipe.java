package speiger.src.spmodapi.common.recipes.advanced;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import speiger.src.api.util.InventoryUtil;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.enums.EnumColor;
import speiger.src.spmodapi.common.util.proxy.PathProxy;
import cpw.mods.fml.common.ICraftingHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class ColorCardRecipe extends ShapelessOreRecipe implements ICraftingHandler
{
	public HashMap<List<Integer>, EnumColor> colors = new HashMap<List<Integer>, EnumColor>();
	public ColorCardRecipe()
	{
		super(new ItemStack(APIItems.colorCard, 1, 0), new ItemStack(APIItems.colorCard, 1, 0));
		ArrayList<ItemStack> total = new ArrayList<ItemStack>();
		ArrayList<ItemStack> list = (ArrayList<ItemStack>) OreDictionary.getOres("dyeRed").clone();
		removeCard(list);
		this.addColors(list, EnumColor.RED);
		total.addAll(list);
		list = (ArrayList<ItemStack>) OreDictionary.getOres("dyeBlack").clone();
		removeCard(list);
		this.addColors(list, EnumColor.BLACK);
		total.addAll(list);
		list = (ArrayList<ItemStack>) OreDictionary.getOres("dyeGreen").clone();
		removeCard(list);
		this.addColors(list, EnumColor.GREEN);
		total.addAll(list);
		list = (ArrayList<ItemStack>) OreDictionary.getOres("dyeBrown").clone();
		removeCard(list);
		this.addColors(list, EnumColor.BROWN);
		total.addAll(list);
		list = (ArrayList<ItemStack>) OreDictionary.getOres("dyeBlue").clone();
		removeCard(list);
		this.addColors(list, EnumColor.BLUE);
		total.addAll(list);
		list = (ArrayList<ItemStack>) OreDictionary.getOres("dyePurple").clone();
		removeCard(list);
		this.addColors(list, EnumColor.PURPLE);
		total.addAll(list);
		list = (ArrayList<ItemStack>) OreDictionary.getOres("dyeCyan").clone();
		removeCard(list);
		this.addColors(list, EnumColor.CYAN);
		total.addAll(list);
		list = (ArrayList<ItemStack>) OreDictionary.getOres("dyeLightGray").clone();
		removeCard(list);
		this.addColors(list, EnumColor.LIGHTGRAY);
		total.addAll(list);
		list = (ArrayList<ItemStack>) OreDictionary.getOres("dyeGray").clone();
		removeCard(list);
		this.addColors(list, EnumColor.GRAY);
		total.addAll(list);
		list = (ArrayList<ItemStack>) OreDictionary.getOres("dyePink").clone();
		removeCard(list);
		this.addColors(list, EnumColor.PINK);
		total.addAll(list);
		list = (ArrayList<ItemStack>) OreDictionary.getOres("dyeLime").clone();
		removeCard(list);
		this.addColors(list, EnumColor.LIME);
		total.addAll(list);
		list = (ArrayList<ItemStack>) OreDictionary.getOres("dyeYellow").clone();
		removeCard(list);
		this.addColors(list, EnumColor.YELLOW);
		total.addAll(list);
		list = (ArrayList<ItemStack>) OreDictionary.getOres("dyeLightBlue").clone();
		removeCard(list);
		this.addColors(list, EnumColor.LIGHTBLUE);
		total.addAll(list);
		list = (ArrayList<ItemStack>) OreDictionary.getOres("dyeMagenta").clone();
		removeCard(list);
		this.addColors(list, EnumColor.MAGENTA);
		total.addAll(list);
		list = (ArrayList<ItemStack>) OreDictionary.getOres("dyeOrange").clone();
		removeCard(list);
		this.addColors(list, EnumColor.ORANGE);
		total.addAll(list);
		list = (ArrayList<ItemStack>) OreDictionary.getOres("dyeWhite").clone();
		removeCard(list);
		this.addColors(list, EnumColor.WHITE);
		total.addAll(list);
		this.getInput().add(total);
		GameRegistry.registerCraftingHandler(this);
	}
	
	
	public void removeCard(ArrayList<ItemStack> list)
	{
		ArrayList<ItemStack> remove = new ArrayList<ItemStack>();
		for(ItemStack stack : list)
		{
			if(stack != null && stack.itemID == APIItems.colorCard.itemID)
			{
				remove.add(stack);
			}
		}
		list.removeAll(remove);
	}
	
	public void addColors(ArrayList<ItemStack> par1, EnumColor par2)
	{
		for(ItemStack item : par1)
		{
			colors.put(Arrays.asList(item.itemID, item.getItemDamage()), par2);
		}
	}




	@Override
	public ItemStack getCraftingResult(InventoryCrafting par1)
	{
		ItemStack output = this.getRecipeOutput().copy();
		ItemStack input = null;
		ItemStack card = null;
		for(int i = 0;i<par1.getSizeInventory();i++)
		{
			ItemStack item = par1.getStackInSlot(i);
			if(item != null)
			{
				if(item.itemID != APIItems.colorCard.itemID)
				{
					input = item;
				}
				else
				{
					card = item;
				}

			}
		}
		
		if(input == null)
		{
			return null;
		}
		output.setItemDamage(colors.get(Arrays.asList(input.itemID, input.getItemDamage())).getAsWool()+1);		
		output.stackSize = card.stackSize;
		
		return output;
	}




	@Override
	public ItemStack getRecipeOutput()
	{
		ItemStack stack = super.getRecipeOutput().copy();
		stack.stackTagCompound = new NBTTagCompound();
		return stack;
	}

	
	
	@Override
	public void onCrafting(EntityPlayer player, ItemStack item, IInventory inv)
	{
		if(!player.worldObj.isRemote)
		{
			int l = 0;
			for(int i = 0;i<inv.getSizeInventory();i++)
			{
				ItemStack stack = inv.getStackInSlot(i);
				if(stack != null)
				{
					l++;
				}
			}
			if(item.itemID != APIItems.colorCard.itemID || l != 2)
			{
				return;
			}
			for(int i = 0;i<inv.getSizeInventory();i++)
			{
				ItemStack stack = inv.getStackInSlot(i);
				if(stack != null)
				{
					if(stack.itemID == APIItems.colorCard.itemID)
					{
						inv.setInventorySlotContents(i, null);
					}
					else
					{
						boolean stop = false;
						for(FluidContainerData con : PathProxy.getDataFromFluid(FluidRegistry.WATER))
						{
							if(con.filledContainer.isItemEqual(stack))
							{
								stop = true;
							}
						}
						ItemStack cop = stack;
						if(!stop)
						{
							InventoryUtil.dropItem(player, cop);
							inv.setInventorySlotContents(i, null);
						}
					}
				}
			}
		}
	}


	@Override
	public void onSmelting(EntityPlayer player, ItemStack item)
	{
		
	}
	
	
	
}
