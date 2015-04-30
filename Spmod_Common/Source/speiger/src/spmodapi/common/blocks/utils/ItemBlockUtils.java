package speiger.src.spmodapi.common.blocks.utils;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.common.items.core.ItemBlockSpmod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockUtils extends ItemBlockSpmod
{
	
	public ItemBlockUtils(int par1)
	{
		super(par1);
		this.setHasSubtypes(true);
	}
	
	@Override
	public int getMetadata(int par1)
	{
		return par1;
	}

	@Override
	public BlockStack getBlockToPlace(int meta)
	{
		return new BlockStack(getBlockID(), meta);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		super.getSubItems(par1, par2CreativeTabs, par3List);
	}

	@Override
	public String getName(ItemStack par1)
	{
		switch(par1.getItemDamage())
		{
			case 0: return "Cobble Workbench";
			case 1: return "Exp Storage";
			case 2: return "Mob Machine";
			case 3: return "Entity Lurer";
			case 4: return "Inventory Accesser";
			case 5: return "Cobble Storage";
			case 6: return "Mob Machine Spawner Mode";
			case 7: return "Entity Cage";
			default: return "No Name";
		}
	}
	
}
