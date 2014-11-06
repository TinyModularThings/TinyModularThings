package speiger.src.spmodapi.common.items.crafting;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.items.core.SpmodItem;
import speiger.src.spmodapi.common.util.TextureEngine;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemGear extends SpmodItem
{
	
	public ItemGear(int id)
	{
		super(id);
		this.setHasSubtypes(true);
		this.setCreativeTab(APIUtils.tabCrafing);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for(GearType type : GearType.values())
		{
			par3List.add(getGearFromType(type));
		}
	}

	
	@Override
	public void registerTexture(TextureEngine par1)
	{
		par1.setCurrentPath("crafting");
		String[] textures = new String[GearType.values().length];
		for(int i = 0;i < textures.length;i++)
		{
			textures[i] = "gear." + GearType.values()[i].getTexture();
		}
		par1.registerTexture(this, textures);
	}
	
	public static ItemStack getGearFromType(GearType par1)
	{
		return new ItemStack(APIItems.gears, 1, par1.ordinal());
	}
	
	public static ItemStack getGearFromType(GearType par1, int qty)
	{
		return new ItemStack(APIItems.gears, qty, par1.ordinal());
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int par1)
	{
		return TextureEngine.getTextures().getTexture(this, par1);
	}
	
	public static enum GearType
	{
		Wood("Wooden Gear", "wood"),
		Cobblestone("Cobblestone Gear", "cobble"),
		Stone("Stone Gear", "stone"),
		Iron("Iron Gear", "iron"),
		Gold("Gold Gear", "gold"),
		Diamond("Diamond Gear", "diamond"),
		Redstone("Redstone Gear", "redstone"),
		Bone("Bone Gear", "bone"),
		WoodenRing("Wooden Ring", "part.ring.wood"),
		CobbleRing("Cobblestone Ring", "part.ring.cobble"),
		StoneRing("Stone Ring", "part.ring.stone"),
		StoneCorner("Stone Corner", "part.corner.stone"),
		WoodenCorner("Wooden Corner", "part.corner.wood");
		
		String name;
		String texture;
		
		private GearType(String name, String texture)
		{
			this.name = name;
			this.texture = texture;
		}
		
		public String getName()
		{
			return name;
		}
		
		public String getTexture()
		{
			return texture;
		}
		
		public ItemStack getItem()
		{
			return new ItemStack(APIItems.gears, 1, this.ordinal());
		}
		
		public ItemStack getItem(int qty)
		{
			return new ItemStack(APIItems.gears, qty, this.ordinal());
		}
		
	}

	@Override
	public String getName(ItemStack par1)
	{
		return GearType.values()[par1.getItemDamage()].getName();
	}
	
}
