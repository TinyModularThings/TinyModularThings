package speiger.src.tinymodularthings.common.items.minecarts;

import java.util.List;

import net.minecraft.block.BlockRailBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import speiger.src.api.items.DisplayItem;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import speiger.src.spmodapi.common.util.proxy.LangProxy;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityTinyChestCart.EightSlotTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityTinyChestCart.FiveSlotTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityTinyChestCart.FourSlotTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityTinyChestCart.NineSlotTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityTinyChestCart.OneSlotTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityTinyChestCart.SevenSlotTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityTinyChestCart.SixSlotTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityTinyChestCart.ThreeSlotTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityTinyChestCart.TwoSlotTinyChestCart;
import speiger.src.tinymodularthings.common.items.core.TinyItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TinyChestCart extends TinyItem
{
	
	public TinyChestCart(int par1)
	{
		super(par1);
		setCreativeTab(CreativeTabs.tabFood);
		setHasSubtypes(true);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1, EntityPlayer par2, List par3, boolean par4)
	{
		String name = LangProxy.getSlot(getMod(), par1.getItemDamage() == 0);
		int id = par1.getItemDamage() + 1;
		par3.add(name + ": " + id);
	}
	
	@Override
	public void registerItems(int id, SpmodMod par0)
	{
		if (!SpmodModRegistry.areModsEqual(par0, TinyModularThings.instance))
		{
			return;
		}
		LanguageRegister.getLanguageName(new DisplayItem(id), "TinyCart", par0);
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod Start)
	{
		return LanguageRegister.getLanguageName(new DisplayItem(par1.itemID), "TinyCart", Start);
	}
	
	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
	{
		int i1 = par3World.getBlockId(par4, par5, par6);
		
		if (BlockRailBase.isRailBlock(i1))
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
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3)
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
				return new OneSlotTinyChestCart(world, x, y, z);
			case 1:
				return new TwoSlotTinyChestCart(world, x, y, z);
			case 2:
				return new ThreeSlotTinyChestCart(world, x, y, z);
			case 3:
				return new FourSlotTinyChestCart(world, x, y, z);
			case 4:
				return new FiveSlotTinyChestCart(world, x, y, z);
			case 5:
				return new SixSlotTinyChestCart(world, x, y, z);
			case 6:
				return new SevenSlotTinyChestCart(world, x, y, z);
			case 7:
				return new EightSlotTinyChestCart(world, x, y, z);
			case 8:
				return new NineSlotTinyChestCart(world, x, y, z);
			default:
				return EntityMinecart.createMinecart(world, x, y, z, 0);
		}
	}
	
}
