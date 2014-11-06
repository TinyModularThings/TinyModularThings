package speiger.src.spmodapi.common.blocks.deko;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.common.data.nbt.NBTHelper;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.common.blocks.deko.TileLamp.EnumLampType;
import speiger.src.spmodapi.common.enums.EnumColor;
import speiger.src.spmodapi.common.items.core.ItemBlockSpmod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockLightDekoBlock extends ItemBlockSpmod
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

	@Override
	public BlockStack getBlockToPlace(int meta)
	{
		return new BlockStack(this.getBlockID(), inverted(meta) ? 3 : 0);
	}

	@Override
	public void onAfterPlaced(World world, int x, int y, int z, int side, EntityPlayer player, ItemStack item)
	{
		int meta = item.getItemDamage();
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if(tile != null && tile instanceof TileLamp)
		{
			TileLamp lamp = (TileLamp)tile;
			lamp.setFacing(ForgeDirection.getOrientation(side).getOpposite().ordinal());
			lamp.setMetadata(meta);
			lamp.setType(type(meta));
			lamp.setInverted(inverted(meta));
			int color = color(meta);
			if(!player.capabilities.isCreativeMode)item.stackSize--;
			if(color <= 15)lamp.setColor(color);
			else if(color == 16)
			{
				lamp.setNoneColored();
				if(NBTHelper.nbtCheck(item, "Colors"))
				{
					NBTTagList list = item.getTagCompound().getTagList("Colors");
					for(int i = 0;i<list.tagCount();i++)
					{
						lamp.addColor(((NBTTagInt)list.tagAt(i)).data);
					}
				}
			}
			else lamp.setAllColored();
		}
	}

	@Override
	public String getName(ItemStack par1)
	{
		int meta = par1.getItemDamage();
		EnumLampType type = EnumLampType.values()[type(meta)];
		int color = color(meta);
		if(color > 15)
		{
			if(color > 16)
			{
				return "All Colored "+type.getName();
			}
			return "None Colored "+type.getName();
		}
		return EnumColor.values()[color].getNameBig()+" "+type.getName();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1, EntityPlayer par2, List par3, boolean par4)
	{
		int meta = par1.getItemDamage();
		if(inverted(meta))
		{
			par3.add("Is Inverted");
		}
		int color = color(meta);
		if(color == 16)
		{
			if(NBTHelper.nbtCheck(par1, "Colors"))
			{
				ArrayList<EnumColor> colors = new ArrayList<EnumColor>();
				NBTTagList list = par1.getTagCompound().getTagList("Colors");
				for(int i = 0;i<list.tagCount();i++)
				{
					EnumColor data = EnumColor.values()[((NBTTagInt)list.tagAt(i)).data];
					if(!colors.contains(data))
					{
						colors.add(data);
					}
				}
				String text = "Stored Color: ";
				int count = 0;
				for(int i = 0;i<colors.size();i++)
				{
					count++;
					text = text +" "+ colors.get(i).getNameBig();
					if(count > 4)
					{
						par3.add(text);
						text = "";
						count = 0;
					}
					else
					{
						if(i == colors.size()-1)
						{
							par3.add(text);
						}
					}
				}
			}
		}
		if(NBTHelper.nbtCheck(par1, "Recipe") && par1.getTagCompound().getBoolean("Recipe"))
		{
			par3.add("Copies the Color of the Lamp in the Middle");
		}
	}
	
	
}
