package speiger.src.tinymodularthings.common.recipes.recipeHelper;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import speiger.src.api.blocks.BlockStack;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.items.itemblocks.transport.ItemInterfaceBlock;

public class InterfaceRecipe implements IRecipe
{
	
	@Override
	public boolean matches(InventoryCrafting inv, World world)
	{
		ItemStack block = null;
		ItemStack interfaces = null;
		
		for (int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if (stack != null)
			{
				if (stack.itemID == TinyItems.interfaceBlock.itemID)
				{
					if (interfaces != null)
					{
						return false;
					}
					interfaces = stack;
				}
				else if (isValidBlock(stack) || stack.itemID == Item.stick.itemID)
				{
					if (block != null)
					{
						return false;
					}
					block = stack;
				}
				else
				{
					return false;
				}
			}
		}
		
		if (block != null && interfaces != null)
		{
			return true;
		}
		
		return false;
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		ItemStack stack = null;
		int meta = -1;
		for (int i = 0; i < inv.getSizeInventory(); i++)
		{
			if (stack != null && meta != -1)
			{
				break;
			}
			ItemStack cu = inv.getStackInSlot(i);
			if (cu != null)
			{
				if (isValidBlock(cu))
				{
					stack = cu;
				}
				else if (cu.itemID == TinyItems.interfaceBlock.itemID)
				{
					meta = cu.getItemDamage();
				}
				else if (cu.itemID == Item.stick.itemID)
				{
					stack = cu;
				}
				
			}
		}
		
		if (meta != -1 && stack != null)
		{
			if (stack.itemID == Item.stick.itemID)
			{
				return ItemInterfaceBlock.createInterface(TinyItems.interfaceBlock.itemID, meta);
			}
			else
			{
				ItemStack inter = ItemInterfaceBlock.createInterface(TinyItems.interfaceBlock.itemID, meta);
				ItemStack interEnd = ItemInterfaceBlock.addBlockToInterface(inter, new BlockStack(stack));
				return interEnd;
			}
		}
		
		return null;
	}
	
	@Override
	public int getRecipeSize()
	{
		return 2;
	}
	
	@Override
	public ItemStack getRecipeOutput()
	{
		return null;
	}
	
	public boolean isValidBlock(ItemStack par1)
	{
		if (par1 != null && par1.getItem() instanceof ItemBlock)
		{
			Block blocks = Block.blocksList[((ItemBlock) par1.getItem()).getBlockID()];
			if (blocks != null)
			{
				return blocks.isOpaqueCube() && blocks.renderAsNormalBlock();
			}
		}
		return false;
	}
	
}
