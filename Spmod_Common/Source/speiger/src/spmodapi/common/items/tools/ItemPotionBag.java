package speiger.src.spmodapi.common.items.tools;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import speiger.src.api.items.DisplayItem;
import speiger.src.api.items.IItemGui;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import speiger.src.spmodapi.common.items.SpmodItem;

public class ItemPotionBag extends SpmodItem implements IItemGui
{
	
	public ItemPotionBag(int par1)
	{
		super(par1);
		this.setHasSubtypes(true);
	}

	@Override
	public void registerItems(int id, SpmodMod par0)
	{
		if(!SpmodModRegistry.areModsEqual(getMod(), par0))
		{
			return;
		}
		LanguageRegister.getLanguageName(new DisplayItem(id), "potion.bag", par0);
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod par0)
	{
		return LanguageRegister.getLanguageName(new DisplayItem(par1.itemID), "potion.bag", par0);
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
		return null;
	}

	@Override
	public boolean hasContainer(ItemStack par1)
	{
		return true;
	}

	@Override
	public Container getContainer(InventoryPlayer par1, ItemStack par2)
	{
		return null;
	}
	
	
	
}
