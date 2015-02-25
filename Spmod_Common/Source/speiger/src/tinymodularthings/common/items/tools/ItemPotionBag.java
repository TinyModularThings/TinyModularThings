package speiger.src.tinymodularthings.common.items.tools;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import speiger.src.spmodapi.common.handler.InventoryHandler;
import speiger.src.spmodapi.common.items.core.ItemInventory;
import speiger.src.spmodapi.common.items.core.SpmodInventoryItem;
import speiger.src.spmodapi.common.util.TextureEngine;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPotionBag extends SpmodInventoryItem
{
	public ItemPotionBag(int par1)
	{
		super(par1);
		this.setHasSubtypes(true);
		this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabs.tabFood);
		MinecraftForge.EVENT_BUS.register(this);
		InventoryHandler.registerItemGui(this.itemID, PotionInventory.getInventoryName());
	}

	@Override
	public boolean tickInventory(ItemStack par1)
	{
		NBTTagCompound data = this.getItemData(par1);
		return data.getBoolean("Active");
	}
	@Override
	public boolean hasTickRate(ItemStack par1)
	{
		return true;
	}

	@Override
	public int getTickRate(ItemStack par1)
	{
		NBTTagCompound data = this.getItemData(par1).getCompoundTag("Inventory");
		if(data.getBoolean("Delay"))
		{
			return 40;
		}
		return 0;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1, World par2, EntityPlayer par3)
	{
		if(!par2.isRemote)
		{
			if(par3.isSneaking())
			{
				NBTTagCompound data = this.getItemData(par1);
				data.setBoolean("Active", !data.getBoolean("Active"));
				return par1;
			}
		}
		return super.onItemRightClick(par1, par2, par3);
	}
	

	@Override
	public String getName(ItemStack par1)
	{
		return "Potion Bag";
	}

	@Override
	public ItemInventory createNewInventory(EntityPlayer par1, ItemStack par2)
	{
		return new PotionInventory(par1, par2);
	}

	@Override
	public String createNewInventoryID()
	{
		return "BagID:"+System.currentTimeMillis();
	}

	@Override
	public Icon getTexture(ItemStack stack)
	{
		NBTTagCompound nbt = this.getItemData(stack);
		if(!nbt.getBoolean("Active"))
		{
			return getEngine().getTexture(this, 2);
		}
		PotionInventory inv = (PotionInventory)this.createNewInventory(null, stack);
		if(inv != null && !inv.hasPotions())
		{
			return TextureEngine.getTextures().getTexture(this, 1);
		}
		return TextureEngine.getTextures().getTexture(this, 0);
	}
	
	
	
}