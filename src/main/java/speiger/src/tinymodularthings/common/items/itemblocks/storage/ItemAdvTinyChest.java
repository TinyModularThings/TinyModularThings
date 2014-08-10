package speiger.src.tinymodularthings.common.items.itemblocks.storage;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import speiger.src.api.blocks.BlockStack;
import speiger.src.api.items.DisplayItem;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;
import speiger.src.spmodapi.common.util.proxy.LangProxy;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.blocks.storage.AdvTinyChest;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.items.core.TinyItem;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemAdvTinyChest extends TinyItem
{
	
	public ItemAdvTinyChest()
	{
		super();
		setHasSubtypes(true);
		setCreativeTab(CreativeTabs.tabFood);
	}
	
	@Override
	public void registerItems(Item id, SpmodMod par0)
	{
		if (!par0.getName().equals(TinyModularThingsLib.Name))
		{
			return;
		}
		
		LanguageRegister.getLanguageName(new DisplayItem(id), "adv.tinychest", par0);
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod Start)
	{
		return LanguageRegister.getLanguageName(new DisplayItem(par1.getItem()), "adv.tinychest", Start);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1, EntityPlayer par2, List par3, boolean par4)
	{
		String slots = LangProxy.getSlot(TinyModularThings.instance, par1.getItemDamage() == 0);
		par3.add((1 + par1.getItemDamage()) + " " + slots);
	}
	
	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
	{
		BlockStack i1 = new BlockStack(par3World, par4, par5, par6);
		
		if ((i1.getBlock() == Blocks.snow) && ((par3World.getBlockMetadata(par4, par5, par6) & 0x7) < 1))
		{
			par7 = 1;
		}
		else if ((i1.getBlock() != Blocks.vine) && (i1.getBlock() != Blocks.tallgrass) && (i1.getBlock() != Blocks.deadbush) && ((i1.getBlock() == null) || (!i1.getBlock().isReplaceable(par3World, par4, par5, par6))))
		{
			if (par7 == 0)
			{
				--par5;
			}
			
			if (par7 == 1)
			{
				++par5;
			}
			
			if (par7 == 2)
			{
				--par6;
			}
			
			if (par7 == 3)
			{
				++par6;
			}
			
			if (par7 == 4)
			{
				--par4;
			}
			
			if (par7 == 5)
			{
				++par4;
			}
		}
		
		if (par1ItemStack.stackSize == 0)
		{
			return false;
		}
		else if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack))
		{
			return false;
		}
		else if (par5 == 255)
		{
			return false;
		}
		else
		{
			if (par3World.setBlock(par4, par5, par6, TinyBlocks.storageBlock, 2, 3))
			{
				TileEntity tile = par3World.getTileEntity(par4, par5, par6);
				if (tile != null && tile instanceof AdvTinyChest)
				{
					((AdvTinyChest) tile).setMode(par1ItemStack.getItemDamage() + 1);
					i1.getBlock().onBlockPlacedBy(par3World, par4, par5, par6, par2EntityPlayer, par1ItemStack);
					par1ItemStack.stackSize--;
					return true;
				}
				
			}
			
			return false;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int i = 0; i < 9; i++)
		{
			par3List.add(new ItemStack(par1, 1, i));
		}
	}
	
}
