package speiger.src.compactWindmills.common.blocks;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import speiger.src.api.items.DisplayStack;
import speiger.src.api.items.LanguageItem;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import speiger.src.compactWindmills.CompactWindmills;
import speiger.src.compactWindmills.common.utils.WindmillType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockWindmill extends ItemBlock implements LanguageItem
{

	public ItemBlockWindmill(int par1)
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
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		WindmillType type = WindmillType.values()[par1ItemStack.getItemDamage()];
		if(type != null)
		{
			par3List.add("Max. Output: "+type.getOutput()+"EU/t");
		}
	}

	@Override
	public String getItemDisplayName(ItemStack par1ItemStack)
	{
		return getDisplayName(par1ItemStack, CompactWindmills.instance);
	}

	@Override
	public String getDisplayName(ItemStack par1, SpmodMod par0)
	{
		return LanguageRegister.getLanguageName(new DisplayStack(par1), "windmill.type."+WindmillType.getValidValues()[par1.getItemDamage()].getTextureName(), par0);
	}

	@Override
	public void registerItems(int id, SpmodMod par0)
	{
		if(!SpmodModRegistry.areModsEqual(par0, CompactWindmills.instance))
		{
			return;
		}
		for(int i = 0;i<WindmillType.getValidValues().length;i++)
		{
			WindmillType type = WindmillType.getValidValues()[i];
			LanguageRegister.getLanguageName(new DisplayStack(new ItemStack(id, 1, i)), "windmill.type."+type.getTextureName(), par0);
		}
	}
	
	
	
	
	
	
	
}
