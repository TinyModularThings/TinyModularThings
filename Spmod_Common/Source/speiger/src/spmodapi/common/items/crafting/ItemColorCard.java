package speiger.src.spmodapi.common.items.crafting;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.enums.EnumColor;
import speiger.src.spmodapi.common.items.SpmodItem;
import speiger.src.spmodapi.common.util.TextureEngine;
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
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		int meta = par1ItemStack.getItemDamage();
		if (meta > 0)
		{
			EnumColor color = EnumColor.getColorFromWool(meta - 1);
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

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int par1)
	{
		return TextureEngine.getTextures().getTexture(this);
	}
	
	
	
}
