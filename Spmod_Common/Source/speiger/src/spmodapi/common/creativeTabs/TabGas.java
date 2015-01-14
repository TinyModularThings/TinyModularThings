package speiger.src.spmodapi.common.creativeTabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TabGas extends CreativeTabs
{

	public TabGas()
	{
		super("Gas Utils");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getTranslatedTabLabel()
	{
		return "Spmod Gas";
	}

	@Override
	public ItemStack getIconItemStack()
	{
		return new ItemStack(APIBlocks.animalGas);
	}
	
	
	
}
