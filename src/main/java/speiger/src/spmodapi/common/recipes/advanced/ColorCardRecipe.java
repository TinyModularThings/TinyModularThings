package speiger.src.spmodapi.common.recipes.advanced;

import java.util.ArrayList;
import java.util.HashMap;
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
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class ColorCardRecipe extends ShapelessOreRecipe
{
	public HashMap<ItemStack, EnumColor> colors = new HashMap<ItemStack, EnumColor>();
	public ColorCardRecipe()
	{
		super(new ItemStack(APIItems.colorCard, 1, 0), new ItemStack(APIItems.colorCard, 1, 0));
		ArrayList<ItemStack> total = new ArrayList<ItemStack>();
		ArrayList<ItemStack> list = (ArrayList<ItemStack>) OreDictionary.getOres("dyeRed").clone();
		list = removeCard(list);
		this.addColors(list, EnumColor.RED);
		total.addAll(list);
		list = (ArrayList<ItemStack>) OreDictionary.getOres("dyeBlack").clone();
		list = removeCard(list);
		this.addColors(list, EnumColor.BLACK);
		total.addAll(list);
		list = (ArrayList<ItemStack>) OreDictionary.getOres("dyeGreen").clone();
		list = removeCard(list);
		this.addColors(list, EnumColor.GREEN);
		total.addAll(list);
		list = (ArrayList<ItemStack>) OreDictionary.getOres("dyeBrown").clone();
		list = removeCard(list);
		this.addColors(list, EnumColor.BROWN);
		total.addAll(list);
		list = (ArrayList<ItemStack>) OreDictionary.getOres("dyeBlue").clone();
		list = removeCard(list);
		this.addColors(list, EnumColor.BLUE);
		total.addAll(list);
		list = (ArrayList<ItemStack>) OreDictionary.getOres("dyePurple").clone();
		list = removeCard(list);
		this.addColors(list, EnumColor.PURPLE);
		total.addAll(list);
		list = (ArrayList<ItemStack>) OreDictionary.getOres("dyeCyan").clone();
		list = removeCard(list);
		this.addColors(list, EnumColor.CYAN);
		total.addAll(list);
		list = (ArrayList<ItemStack>) OreDictionary.getOres("dyeLightGray").clone();
		list = removeCard(list);
		this.addColors(list, EnumColor.LIGHTGRAY);
		total.addAll(list);
		list = (ArrayList<ItemStack>) OreDictionary.getOres("dyeGray").clone();
		list = removeCard(list);
		this.addColors(list, EnumColor.GRAY);
		total.addAll(list);
		list = (ArrayList<ItemStack>) OreDictionary.getOres("dyePink").clone();
		list = removeCard(list);
		this.addColors(list, EnumColor.PINK);
		total.addAll(list);
		list = (ArrayList<ItemStack>) OreDictionary.getOres("dyeLime").clone();
		list = removeCard(list);
		this.addColors(list, EnumColor.LIME);
		total.addAll(list);
		list = (ArrayList<ItemStack>) OreDictionary.getOres("dyeYellow").clone();
		list = removeCard(list);
		this.addColors(list, EnumColor.YELLOW);
		total.addAll(list);
		list = (ArrayList<ItemStack>) OreDictionary.getOres("dyeLightBlue").clone();
		list = removeCard(list);
		this.addColors(list, EnumColor.LIGHTBLUE);
		total.addAll(list);
		list = (ArrayList<ItemStack>) OreDictionary.getOres("dyeMagenta").clone();
		list = removeCard(list);
		this.addColors(list, EnumColor.MAGENTA);
		total.addAll(list);
		list = (ArrayList<ItemStack>) OreDictionary.getOres("dyeOrange").clone();
		list = removeCard(list);
		this.addColors(list, EnumColor.ORANGE);
		total.addAll(list);
		list = (ArrayList<ItemStack>) OreDictionary.getOres("dyeWhite").clone();
		list = removeCard(list);
		this.addColors(list, EnumColor.WHITE);
		total.addAll(list);
		this.getInput().add(total);
		FMLCommonHandler.instance().bus().register(this);
	}
	
	
	public ArrayList<ItemStack> removeCard(ArrayList<ItemStack> list)
	{
		// Because ore dictionary list is unmodifiable
		ArrayList<ItemStack> newList = new ArrayList<ItemStack>();
		newList.addAll(list);
		ArrayList<ItemStack> remove = new ArrayList<ItemStack>();
		for(ItemStack stack : list)
		{
			if(stack != null && stack.getItem() == APIItems.colorCard)
			{
				remove.add(stack);
			}
		}
		newList.removeAll(remove);
		return newList;
	}
	
	public void addColors(ArrayList<ItemStack> par1, EnumColor par2)
	{
		for(ItemStack item : par1)
		{
			colors.put(item, par2);
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
				if(item.getItem() != APIItems.colorCard)
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
		output.setItemDamage(colors.get(input).getAsWool()+1);		
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

	
	
	@SubscribeEvent
	public void onCrafting(ItemCraftedEvent event)
	{
		if(!event.player.worldObj.isRemote)
		{
			IInventory inv = event.craftMatrix;
			int l = 0;
			for(int i = 0;i<inv.getSizeInventory();i++)
			{
				ItemStack stack = inv.getStackInSlot(i);
				if(stack != null)
				{
					l++;
				}
			}
			if(event.crafting.getItem() != APIItems.colorCard || l != 2)
			{
				return;
			}
			for(int i = 0;i<inv.getSizeInventory();i++)
			{
				ItemStack stack = inv.getStackInSlot(i);
				if(stack != null)
				{
					if(stack.getItem() == APIItems.colorCard)
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
							InventoryUtil.dropItem(event.player, cop);
							inv.setInventorySlotContents(i, null);
						}
					}
				}
			}
		}
	}
}
