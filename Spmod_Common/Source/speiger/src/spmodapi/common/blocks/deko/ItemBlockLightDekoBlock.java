package speiger.src.spmodapi.common.blocks.deko;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.items.DisplayStack;
import speiger.src.api.items.InfoStack;
import speiger.src.api.items.LanguageItem;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.blocks.deko.TileLamp.EnumLampType;
import speiger.src.spmodapi.common.enums.EnumColor;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockLightDekoBlock extends ItemBlock implements LanguageItem
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

	@Override
	public String getItemDisplayName(ItemStack par1ItemStack)
	{
		return getDisplayName(par1ItemStack, SpmodAPI.instance);
	}

	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1, EntityPlayer par2EntityPlayer, List par3, boolean par4)
	{
		if(this.inverted(par1.getItemDamage()))
		{
			par3.add(LanguageRegister.getLanguageName(new InfoStack(), "inverted", SpmodAPI.instance));
		}
		
		int color = color(par1.getItemDamage());
		if(color == 16)
		{
			int count = 0;
			String text = LanguageRegister.getLanguageName(new InfoStack(), "stored.color", SpmodAPI.instance)+": ";
			if(par1.hasTagCompound() && par1.getTagCompound().hasKey("Colors") && par1.getTagCompound().getTagList("Colors").tagCount() > 0)
			{
				ArrayList<EnumColor> collors = new ArrayList<EnumColor>();
				NBTTagList list = par1.getTagCompound().getTagList("Colors");
				for(int i = 0;i<list.tagCount();i++)
				{
					NBTTagInt colors = (NBTTagInt) list.tagAt(i);
					if(!collors.contains(EnumColor.values()[colors.data]))
					{
						collors.add(EnumColor.values()[colors.data]);
					}
					
				}
				for(int i = 0;i<collors.size();i++)
				{
					count++;
					
					text = text+LanguageRegister.getLanguageName(new InfoStack(), collors.get(i).getName(), SpmodAPI.instance)+" ";
					if(i > 4)
					{
						if(count == 4)
						{
							count = 0;
							par3.add(text);
							text = "";
						}
					}
					else
					{
						if(i == collors.size()-1)
						{
							par3.add(text);
						}
					}
				}
			}
			if(par1.hasTagCompound() && par1.getTagCompound().hasKey("Recipe") && par1.getTagCompound().getBoolean("Recipe"))
			{
				par3.add(LanguageRegister.getLanguageName(new InfoStack(), "Core.Color", SpmodAPI.instance));
			}
		}
	}

	@Override
	public String getDisplayName(ItemStack par1, SpmodMod par0)
	{
		EnumLampType type = EnumLampType.values()[this.type(par1.getItemDamage())];
		int color = color(par1.getItemDamage());
		if(color > 15)
		{
			if(color > 16)
			{
				return LanguageRegister.getLanguageName(new DisplayStack(par1), "lamp."+type.getName()+".all", par0);
			}
			return LanguageRegister.getLanguageName(new DisplayStack(par1), "lamp."+type.getName()+".mixed", par0);
		}
		return LanguageRegister.getLanguageName(new DisplayStack(par1), "lamp."+type.getName()+"."+EnumColor.values()[color].getName(), par0);
	}

	@Override
	public void registerItems(int id, SpmodMod par0)
	{
		if(!SpmodModRegistry.areModsEqual(par0, SpmodAPI.instance))
		{
			return;
		}
		LanguageRegister.getLanguageName(new InfoStack(), "inverted", par0);
		LanguageRegister.getLanguageName(new InfoStack(), "Core.Color", par0);
		for(EnumLampType type : EnumLampType.values())
		{
			for(int i = 0;i<16;i++)
			{
				LanguageRegister.getLanguageName(new DisplayStack(new ItemStack(id, 1, i)), "lamp."+type.getName()+"."+EnumColor.values()[i].getName(), par0);
			}
			LanguageRegister.getLanguageName(new DisplayStack(new ItemStack(id, 1, 16)), "lamp."+type.getName()+".mixed", par0);
			LanguageRegister.getLanguageName(new DisplayStack(new ItemStack(id, 1, 17)), "lamp."+type.getName()+".all", par0);
		}
	}
	
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        int i1 = par3World.getBlockId(par4, par5, par6);

        if (i1 == Block.snow.blockID && (par3World.getBlockMetadata(par4, par5, par6) & 7) < 1)
        {
            par7 = 1;
        }
        else if (i1 != Block.vine.blockID && i1 != Block.tallGrass.blockID && i1 != Block.deadBush.blockID
                && (Block.blocksList[i1] == null || !Block.blocksList[i1].isBlockReplaceable(par3World, par4, par5, par6)))
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
        	if(tile != null && tile instanceof TileLamp)
        	{
        		TileLamp lamp = (TileLamp) tile;
        		lamp.setFacing(ForgeDirection.getOrientation(par7).getOpposite().ordinal());
        		int meta = par1ItemStack.getItemDamage();
        		int color = color(meta);
        		boolean inverted = inverted(meta);
        		int type = type(meta);
        		
        		lamp.setType(type);
        		if(inverted){lamp.setInverted();}
        		
        		if(color <= 15)lamp.setColor(color);
        		else lamp.setColor(16);
        		
        		if(color == 16)
        		{
        			lamp.setNoneColored();
        			if(par1ItemStack.hasTagCompound() && par1ItemStack.getTagCompound().hasKey("Colors") && par1ItemStack.getTagCompound().getTagList("Colors").tagCount() > 0)
        			{
            			NBTTagList list = par1ItemStack.getTagCompound().getTagList("Colors");
            			for(int i = 0;i<list.tagCount();i++)
            			{
            				lamp.addColor(EnumColor.values()[((NBTTagInt)list.tagAt(i)).data]);
            			}
        			}
        		}
        		else if(color == 17) lamp.setAllColored();
        		
        		lamp.setMetadata(meta);
        		
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
