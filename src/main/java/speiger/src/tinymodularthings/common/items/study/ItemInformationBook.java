package speiger.src.tinymodularthings.common.items.study;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import speiger.src.api.items.DisplayItem;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;
import speiger.src.spmodapi.common.util.slot.EmptyContainer;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.client.gui.study.GuiInformations;
import speiger.src.tinymodularthings.common.enums.EnumIDs;
import speiger.src.tinymodularthings.common.interfaces.IItemGui;
import speiger.src.tinymodularthings.common.items.core.TinyItem;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemInformationBook extends TinyItem implements IItemGui
{
	
	public ItemInformationBook(int par1)
	{
		super(par1);
		setCreativeTab(CreativeTabs.tabFood);
	}
	
	@Override
	public void registerItems(int id, SpmodMod par0)
	{
		if (!par0.getName().equals(TinyModularThingsLib.Name))
		{
			return;
		}
		LanguageRegister.getLanguageName(new DisplayItem(id), "information.Book", par0);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack par1, World par2, EntityPlayer par3)
	{
		if (!par2.isRemote)
		{
			
			par3.openGui(TinyModularThings.instance, EnumIDs.Items.getId(), par2, 0, 0, 0);
			return par1;
		}
		return par1;
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod Start)
	{
		return LanguageRegister.getLanguageName(new DisplayItem(par1.getItem()), "information.Book", Start);
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
		return new GuiInformations();
	}
	
	@Override
	public boolean hasContainer(ItemStack par1)
	{
		return true;
	}
	
	@Override
	public Container getContainer(ItemStack par1)
	{
		return new EmptyContainer();
	}
	
}
