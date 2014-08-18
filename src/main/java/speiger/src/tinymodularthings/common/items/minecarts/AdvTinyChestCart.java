package speiger.src.tinymodularthings.common.items.minecarts;

import java.util.List;

import net.minecraft.block.BlockRailBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import speiger.src.api.blocks.BlockStack;
import speiger.src.api.items.DisplayItem;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import speiger.src.spmodapi.common.util.proxy.LangProxy;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityAdvTinyChestCart.EightSlotAdvTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityAdvTinyChestCart.FiveSlotAdvTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityAdvTinyChestCart.FourSlotAdvTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityAdvTinyChestCart.NineSlotAdvTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityAdvTinyChestCart.OneSlotAdvTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityAdvTinyChestCart.SevenSlotAdvTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityAdvTinyChestCart.SixSlotAdvTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityAdvTinyChestCart.ThreeSlotAdvTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityAdvTinyChestCart.TwoSlotAdvTinyChestCart;
import speiger.src.tinymodularthings.common.items.core.TinyItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AdvTinyChestCart extends TinyItem
{
	
	public AdvTinyChestCart()
	{
		setCreativeTab(CreativeTabs.tabFood);
		setHasSubtypes(true);
	}
	
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1, EntityPlayer par2, List par3, boolean par4)
	{
		String name = LangProxy.getSlot(getMod(), par1.getItemDamage() == 0);
		par3.add(name+": "+par1.getItemDamage());
	}



	@Override
	public void registerItems(Item id, SpmodMod par0)
	{
		if (!SpmodModRegistry.areModsEqual(par0, TinyModularThings.instance))
		{
			return;
		}
		LanguageRegister.getLanguageName(new DisplayItem(id), "advTinyCart", par0);
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod Start)
	{
		return LanguageRegister.getLanguageName(new DisplayItem(par1.getItem()), "advTinyCart", Start);
	}
	
	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
	{
		BlockStack stack = new BlockStack(par3World, par4, par5, par6);
		
		if (BlockRailBase.func_150051_a(stack.getBlock()))
		{
			if (!par3World.isRemote)
			{
				EntityMinecart entityminecart = getMinecartFromMeta(par3World, par4, par5, par6, par1ItemStack.getItemDamage());
				
				if (par1ItemStack.hasDisplayName())
				{
					entityminecart.setMinecartName(par1ItemStack.getDisplayName());
				}
				
				par3World.spawnEntityInWorld(entityminecart);
			}
			
			--par1ItemStack.stackSize;
			return true;
		}
		else
		{
			return false;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3)
	{
		for (int i = 0; i < 9; i++)
		{
			par3.add(new ItemStack(par1, 1, i));
		}
	}
	
	public EntityMinecart getMinecartFromMeta(World world, int x, int y, int z, int meta)
	{
		switch (meta)
		{
			case 0:
				return new OneSlotAdvTinyChestCart(world, x, y, z);
			case 1:
				return new TwoSlotAdvTinyChestCart(world, x, y, z);
			case 2:
				return new ThreeSlotAdvTinyChestCart(world, x, y, z);
			case 3:
				return new FourSlotAdvTinyChestCart(world, x, y, z);
			case 4:
				return new FiveSlotAdvTinyChestCart(world, x, y, z);
			case 5:
				return new SixSlotAdvTinyChestCart(world, x, y, z);
			case 6:
				return new SevenSlotAdvTinyChestCart(world, x, y, z);
			case 7:
				return new EightSlotAdvTinyChestCart(world, x, y, z);
			case 8:
				return new NineSlotAdvTinyChestCart(world, x, y, z);
			default:
				return EntityMinecart.createMinecart(world, x, y, z, 0);
		}
	}
}
