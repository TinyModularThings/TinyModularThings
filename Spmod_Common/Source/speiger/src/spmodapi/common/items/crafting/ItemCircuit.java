package speiger.src.spmodapi.common.items.crafting;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.items.core.SpmodItem;
import speiger.src.spmodapi.common.util.TextureEngine;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCircuit extends SpmodItem
{
	public ItemCircuit(int id)
	{
		super(id);
		this.setHasSubtypes(true);
		this.setCreativeTab(APIUtils.tabCrafing);
	}


	@Override
	public String getName(ItemStack par1)
	{
		switch(par1.getItemDamage())
		{
			case 0: return "Simple Circuit";
			case 1: return "Advanced Circuit";
			default: return "Logic Diamond";
		
		}
	}
	
	

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for(int i = 0;i<3;i++)
		{
			par3List.add(new ItemStack(par1, 1, i));
		}
	}


	@Override
	public void registerTexture(TextureEngine par1)
	{
		super.registerTexture(par1);
		par1.setCurrentPath("circuits");
		par1.registerTexture(this, "BasicCircuit", "AdvancedCircuit", 
		"LogicDiamond");
	}
	
	
	
	
}
