package speiger.src.tinymodularthings.common.recipes.recipeHelper;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;
import speiger.src.tinymodularthings.common.interfaces.IUpgradeRecipeHelper;

public class TinyUpgradeRecipe extends ShapedOreRecipe
{
	IUpgradeRecipeHelper par1;
	int upgrade;
	
	public TinyUpgradeRecipe(ItemStack input, String ore, IUpgradeRecipeHelper helper, int up)
	{
		super(input, new Object[]{"XXX", "XYX", "XXX", 'Y', input, 'X', ore});
		par1 = helper;
		upgrade = up;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1)
	{
		ItemStack slot = var1.getStackInSlot(4).copy();
		slot.stackSize = 1;
		par1.handleUpgrade(slot, upgrade);
		return slot;
	}
	
	@Override
	public boolean matches(InventoryCrafting inv, World world)
	{
		boolean matches = super.matches(inv, world);
		ItemStack slot = inv.getStackInSlot(4);
		return matches && slot != null && par1.canBeUpgraded(slot, upgrade);
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		ItemStack result = ItemStack.copyItemStack(super.getRecipeOutput());
		if(result != null)
		{
			NBTTagCompound data = new NBTTagCompound();
			NBTTagList list = new NBTTagList();
			list.appendTag(new NBTTagString("Nothing", "Upgrades Item/Block"+EnumChatFormatting.AQUA));
			data.setTag("Lore", list);
			result.setTagInfo("display", data);
		}
		return result;
	}
}
