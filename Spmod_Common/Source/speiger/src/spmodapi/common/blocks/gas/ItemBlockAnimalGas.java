package speiger.src.spmodapi.common.blocks.gas;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.common.items.core.ItemBlockSpmod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockAnimalGas extends ItemBlockSpmod
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
	public String getName(ItemStack par1)
	{
		return "Animal Gas";
	}

	@Override
	public BlockStack getBlockToPlace(int meta)
	{
		return new BlockStack(getBlockID(), meta);
	}


	
	
	
	
	
}
