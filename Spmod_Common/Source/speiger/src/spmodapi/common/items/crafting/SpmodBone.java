package speiger.src.spmodapi.common.items.crafting;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import speiger.src.api.items.DisplayItem;
import speiger.src.api.items.LanguageItem;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.enums.EnumColor;
import speiger.src.spmodapi.common.lib.SpmodAPILib;
import speiger.src.spmodapi.common.util.proxy.PathProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SpmodBone extends ItemFood implements LanguageItem
{
	String name;
	int Size;
	
	public SpmodBone(int itemID, int size, String Name)
	{
		super(itemID, 2, 4F*size, true);
		name = Name;
		Size = size;
		OreDictionary.registerOre("bone", this);
		PathProxy.addSRecipe(new ItemStack(Item.dyePowder.itemID, size, EnumColor.WHITE.getAsDye()), new Object[]{new ItemStack(this)});
		this.setCreativeTab(APIUtils.tabCrafing);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1)
	{
		this.itemIcon = par1.registerIcon(SpmodAPILib.ModID.toLowerCase()+":crafting/bone_"+name);
	}

	@Override
	public String getItemDisplayName(ItemStack par1)
	{
		return getDisplayName(par1, SpmodAPI.instance);
	}

	@Override
	public String getDisplayName(ItemStack par1, SpmodMod par0)
	{
		return LanguageRegister.getLanguageName(new DisplayItem(par1.getItem()), name+"_bone", par0);
	}

	@Override
	public void registerItems(int id, SpmodMod par0)
	{
		if(!SpmodModRegistry.areModsEqual(par0, SpmodAPI.instance))
		{
			return;
		}
		LanguageRegister.getLanguageName(new DisplayItem(id), name+"_bone", par0);
	}

	@Override
	public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		if(!par2World.isRemote)
		{
			par3EntityPlayer.attackEntityFrom(DamageSource.drown, 1);
		}
		return super.onEaten(par1ItemStack, par2World, par3EntityPlayer);
	}
	
	
	
}
