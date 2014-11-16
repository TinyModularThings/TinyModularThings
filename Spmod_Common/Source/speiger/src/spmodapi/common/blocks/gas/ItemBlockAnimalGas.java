package speiger.src.spmodapi.common.blocks.gas;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockAnimalGas extends ItemBlock
{

	public ItemBlockAnimalGas(int par1)
	{
		super(par1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
	}

	@Override
	public int getMetadata(int par1)
	{
		return par1;
	}

	@Override
	public String getItemDisplayName(ItemStack par1ItemStack)
	{
		return "Animal Gas";
	}


	
	
	
	
	
}
