package speiger.src.spmodapi.common.blocks.deko;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.spmodapi.common.enums.EnumColor;

public class ItemBlockLightDekoBlock extends ItemBlock
{
	
	public ItemBlockLightDekoBlock(int par1)
	{
		super(par1);
		this.setHasSubtypes(true);
	}
	
	@Override
	public int getMetadata(int par1)
	{
		return par1;
	}
	
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
	{
		int i1 = par3World.getBlockId(par4, par5, par6);
		
		if (i1 == Block.snow.blockID && (par3World.getBlockMetadata(par4, par5, par6) & 7) < 1)
		{
			par7 = 1;
		}
		else if (i1 != Block.vine.blockID && i1 != Block.tallGrass.blockID && i1 != Block.deadBush.blockID && (Block.blocksList[i1] == null || !Block.blocksList[i1].isBlockReplaceable(par3World, par4, par5, par6)))
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
		else if (par5 == 255 && Block.blocksList[this.getBlockID()].blockMaterial.isSolid())
		{
			return false;
		}
		else if (par3World.setBlock(par4, par5, par6, getBlockID()))
		{
			TileEntity tile = par3World.getBlockTileEntity(par4, par5, par6);
			if (tile != null && tile instanceof TileLamp)
			{
				TileLamp lamp = (TileLamp) tile;
				lamp.setFacing(ForgeDirection.getOrientation(par7).getOpposite().ordinal());
				int meta = par1ItemStack.getItemDamage();
				int color = color(meta);
				boolean inverted = inverted(meta);
				int type = type(meta);
				
				lamp.setType(type);
				if (inverted)
				{
					lamp.setInverted();
				}
				
				if (color <= 15)
					lamp.setColor(color);
				else
					lamp.setColor(16);
				
				if (color == 16)
				{
					lamp.setNoneColored();
					if (par1ItemStack.hasTagCompound() && par1ItemStack.getTagCompound().hasKey("Colors") && par1ItemStack.getTagCompound().getTagList("Colors").tagCount() > 0)
					{
						NBTTagList list = par1ItemStack.getTagCompound().getTagList("Colors");
						for (int i = 0; i < list.tagCount(); i++)
						{
							lamp.addColor(EnumColor.values()[((NBTTagInt) list.tagAt(i)).data]);
						}
					}
				}
				else if (color == 17)
					lamp.setAllColored();
				
				lamp.setMetadata(meta);
				par1ItemStack.stackSize--;
				
			}
			
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public int type(int meta)
	{
		return meta / 36;
	}
	
	public int color(int meta)
	{
		return meta % 18;
	}
	
	public boolean inverted(int meta)
	{
		return (meta % 36) >= 18;
	}
	
}
