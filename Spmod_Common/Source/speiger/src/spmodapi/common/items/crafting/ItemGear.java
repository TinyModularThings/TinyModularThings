package speiger.src.spmodapi.common.items.crafting;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.items.SpmodItem;
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
			textures[i] = "gear." + GearType.values()[i].getName();
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
		Wood("wood"),
		Cobblestone("cobble"),
		Stone("stone"),
		Iron("iron"),
		Gold("gold"),
		Diamond("diamond"),
		Redstone("redstone"),
		Bone("bone"),
		WoodenRing("part.ring.wood"),
		CobbleRing("part.ring.cobble"),
		StoneRing("part.ring.stone"),
		StoneCorner("part.corner.stone"),
		WoodenCorner("part.corner.wood");
		
		String name;
		
		private GearType(String name)
		{
			this.name = name;
			
		}
		
		public String getName()
		{
			return name;
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
	
}
