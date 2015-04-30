package speiger.src.spmodapi.common.items.crafting;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.items.core.SpmodItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemDamageableCircuit extends SpmodItem
{
	String name;
	public List<String> info;
	public ItemDamageableCircuit(int par1, String name, int max, List<String> data)
	{
		super(par1);
		this.setMaxDamage(max);
		this.setMaxStackSize(1);
		this.setCreativeTab(APIUtils.tabCrafing);
		this.setContainerItem(this);
		this.name = name;
		info = data;
	}
	
	@Override
	public String getName(ItemStack par1)
	{
		return name;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1, EntityPlayer par2, List par3, boolean par4)
	{
		par3.addAll(info);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int par1)
	{
		return super.getIconFromDamage(0);
	}

	@Override
	public ItemStack getContainerItemStack(ItemStack itemStack)
	{
		ItemStack result = ItemStack.copyItemStack(itemStack);
		result.setItemDamage(result.getItemDamage()+1);
		if(result.getItemDamage() > result.getMaxDamage())
		{
			result = null;
		}
		return result;
	}
	
	
	
	
}
