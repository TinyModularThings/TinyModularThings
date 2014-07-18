package speiger.src.spmodapi.common.items.hemp;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import speiger.src.api.items.DisplayItem;
import speiger.src.api.items.InfoStack;
import speiger.src.api.items.LanguageItem;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.lib.SpmodAPILib;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemHempSeed extends ItemSeeds implements LanguageItem
{
	public ItemHempSeed()
	{
		super(APIBlocks.hempCrop, Blocks.farmland);
		this.setCreativeTab(APIUtils.tabHemp);
	}
	
	@Override
	public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
	{
		return EnumPlantType.Crop;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack)
	{
		return getDisplayName(par1ItemStack, SpmodAPI.instance);
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod par0)
	{
		return LanguageRegister.getLanguageName(new DisplayItem(par1.getItem()), "hemp.seed", par0);
	}
	
	@Override
	public void registerItems(Item item, SpmodMod par0)
	{
		if (!SpmodModRegistry.areModsEqual(par0, SpmodAPI.instance))
		{
			return;
		}
		LanguageRegister.getLanguageName(new InfoStack(), "like.mfr", SpmodAPI.instance);
		LanguageRegister.getLanguageName(new InfoStack(), "prefer.forestry", SpmodAPI.instance);
		LanguageRegister.getLanguageName(new DisplayItem(item), "hemp.seed", par0);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister)
	{
		itemIcon = par1IconRegister.registerIcon(SpmodAPILib.ModID + ":hemp/HempSeeds");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		String mfr = LanguageRegister.getLanguageName(new InfoStack(), "like.mfr", SpmodAPI.instance);
		String forestry = LanguageRegister.getLanguageName(new InfoStack(), "prefer.forestry", SpmodAPI.instance);
		
		par3List.add(mfr);
		par3List.add(forestry);
	}
	
}
