package speiger.src.spmodapi.common.items.crafting;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
		this.setHasSubtypes(true);
		this.setMaxDamage(max);
		this.setMaxStackSize(1);
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
	
	
	
	
}
