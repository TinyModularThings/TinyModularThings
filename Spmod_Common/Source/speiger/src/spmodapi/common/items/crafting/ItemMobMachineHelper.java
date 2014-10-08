package speiger.src.spmodapi.common.items.crafting;

import java.util.HashMap;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import speiger.src.api.util.SpmodMod;
import speiger.src.spmodapi.common.blocks.utils.MobMachine;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.items.SpmodItem;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.TileIconMaker;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMobMachineHelper extends SpmodItem
{
	private MobMachine mobs;
	
	public ItemMobMachineHelper(int par1)
	{
		super(par1);
		mobs = (MobMachine) TileIconMaker.getIconMaker().getTileEntityFromClass(MobMachine.class);
		this.setHasSubtypes(true);
		this.setCreativeTab(APIUtils.tabCrafing);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		HashMap<Integer, String> names = mobs.names;
		for (Integer ints : names.keySet())
		{
			par3List.add(new ItemStack(par1, 1, ints.intValue()));
		}
	}
	
	@Override
	public void registerItems(int id, SpmodMod par0)
	{
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod par0)
	{
		return "MobMachine Modul";
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1, EntityPlayer par2, List par3, boolean par4)
	{
		par3.add("Type: " + mobs.getName(par1.getItemDamage()));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getSpriteNumber()
	{
		return 0;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int par1)
	{
		return TextureEngine.getIcon(APIBlocks.blockUtils, 2)[(par1*2)+1];
	}
	
}
