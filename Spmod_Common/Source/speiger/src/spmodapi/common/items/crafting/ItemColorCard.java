package speiger.src.spmodapi.common.items.crafting;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import speiger.src.api.items.DisplayItem;
import speiger.src.api.items.InfoStack;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.enums.EnumColor;
import speiger.src.spmodapi.common.items.SpmodItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemColorCard extends SpmodItem
{
	
	public ItemColorCard(int par1)
	{
		super(par1);
		this.setCreativeTab(APIUtils.tabCrafing);
		this.setHasSubtypes(true);
		OreDictionary.registerOre("dyeBlack", new ItemStack(this, 1, 16));
		OreDictionary.registerOre("dyeRed", new ItemStack(this, 1, 15));
		OreDictionary.registerOre("dyeGreen", new ItemStack(this, 1, 14));
		OreDictionary.registerOre("dyeBrown", new ItemStack(this, 1, 13));
		OreDictionary.registerOre("dyeBlue", new ItemStack(this, 1, 12));
		OreDictionary.registerOre("dyePurple", new ItemStack(this, 1, 11));
		OreDictionary.registerOre("dyeCyan", new ItemStack(this, 1, 10));
		OreDictionary.registerOre("dyeLightGray", new ItemStack(this, 1, 9));
		OreDictionary.registerOre("dyeGray", new ItemStack(this, 1, 8));
		OreDictionary.registerOre("dyePink", new ItemStack(this, 1, 7));
		OreDictionary.registerOre("dyeLime", new ItemStack(this, 1, 6));
		OreDictionary.registerOre("dyeYellow", new ItemStack(this, 1, 5));
		OreDictionary.registerOre("dyeLightBlue", new ItemStack(this, 1, 4));
		OreDictionary.registerOre("dyeMagenta", new ItemStack(this, 1, 3));
		OreDictionary.registerOre("dyeOrange", new ItemStack(this, 1, 2));
		OreDictionary.registerOre("dyeWhite", new ItemStack(this, 1, 1));
	}
	
	@Override
	public void registerItems(int id, SpmodMod par0)
	{
		if (!SpmodModRegistry.areModsEqual(par0, getMod()))
		{
			return;
		}
		LanguageRegister.getLanguageName(new DisplayItem(id), "color.card", par0);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		int meta = par1ItemStack.getItemDamage();
		if (meta > 0)
		{
			EnumColor color = EnumColor.getColorFromWool(meta - 1);
			par3List.add(LanguageRegister.getLanguageName(new InfoStack(), color.getName(), SpmodAPI.instance));
		}
		else
		{
			if (par1ItemStack.hasTagCompound())
			{
				par3List.add("Copy Input Color");
			}
		}
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod par0)
	{
		return LanguageRegister.getLanguageName(new DisplayItem(par1.getItem()), "color.card", par0);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.itemIcon = par1IconRegister.registerIcon(getModID() + ":crafting/colorCard");
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack par1, World par2, EntityPlayer par3)
	{
		if (!par2.isRemote)
		{
			MovingObjectPosition target = this.getMovingObjectPositionFromPlayer(par2, par3, true);
			if (target != null && target.typeOfHit == target.typeOfHit.TILE)
			{
				int id = par2.getBlockId(target.blockX, target.blockY, target.blockZ);
				int meta = par2.getBlockMetadata(target.blockX, target.blockY, target.blockZ);
				if (id == Block.cloth.blockID)
				{
					if (par1.getItemDamage() == 0)
					{
						par1.setItemDamage(meta + 1);
					}
					else
					{
						if (meta != par1.getItemDamage() - 1)
						{
							par2.setBlockMetadataWithNotify(target.blockX, target.blockY, target.blockZ, par1.getItemDamage() - 1, 3);
							par1.stackSize--;
						}
					}
				}
			}
		}
		return par1;
	}
	
}
