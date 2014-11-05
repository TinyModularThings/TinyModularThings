package speiger.src.spmodapi.common.items.crafting;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.enums.EnumColor;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.proxy.PathProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SpmodBone extends ItemFood
{
	String name;
	
	public SpmodBone(int itemID, int size, String Name, TextureEngine par1)
	{
		super(itemID, 2, 4F * size, true);
		name = Name;
		OreDictionary.registerOre("bone", this);
		PathProxy.addSRecipe(new ItemStack(Item.dyePowder.itemID, size, EnumColor.WHITE.getAsDye()), new Object[] { new ItemStack(this) });
		this.setCreativeTab(APIUtils.tabCrafing);
		par1.registerTexture(this, "bone_"+Name);
	}
	
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
	}



	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int par1)
	{
		return TextureEngine.getTextures().getTexture(this);
	}
	
	@Override
	public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		if (!par2World.isRemote)
		{
			par3EntityPlayer.attackEntityFrom(DamageSource.drown, 1);
		}
		return super.onEaten(par1ItemStack, par2World, par3EntityPlayer);
	}
	
}
