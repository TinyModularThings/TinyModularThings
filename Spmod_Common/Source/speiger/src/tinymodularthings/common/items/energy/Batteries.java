package speiger.src.tinymodularthings.common.items.energy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import speiger.src.api.common.data.nbt.NBTHelper;
import speiger.src.api.common.world.items.energy.IBCBattery;
import speiger.src.spmodapi.common.items.core.ItemInventory;
import speiger.src.spmodapi.common.items.core.SpmodInventoryItem;
import speiger.src.spmodapi.common.util.TextureEngine;

public class Batteries extends SpmodInventoryItem implements IBCBattery
{
	BatterieType type;
	String name;
	public Batteries(int par1, String name, BatterieType type)
	{
		super(par1);
		this.name = name + "MJ Battery";
		this.type = type;
		this.type.data = this;
		this.setMaxDamage(100);
		this.setMaxStackSize(1);
		this.setNoRepair();
		this.setCreativeTab(CreativeTabs.tabFood);
	}

	@Override
	public String getName(ItemStack par1)
	{
		return name;
	}
	
	@Override
	public void registerTexture(TextureEngine par1)
	{
		par1.setCurrentPath("energy");
		for(BatterieType type : BatterieType.values())
		{
			type.registerTexture(par1);
		}
	}
	
	

	@Override
	public ItemStack onItemRightClick(ItemStack par1, World par2, EntityPlayer par3)
	{
		if(par3.isSneaking())
		{
			return super.onItemRightClick(par1, par2, par3);
		}
		return par1;
	}

	@Override
	public int getStoredMJ(ItemStack par1)
	{
		return NBTHelper.getTag(par1, "Energy").getInteger("Stored");
	}

	@Override
	public int getMaxMJStorage(ItemStack par1)
	{
		return type.getMaxStorage();
	}

	@Override
	public BatteryType getType(ItemStack par1)
	{
		return BatteryType.Storage;
	}

	@Override
	public boolean requestEnergy(ItemStack par1)
	{
		return getRequestedAmount(par1) > 0;
	}

	@Override
	public int getRequestedAmount(ItemStack par1)
	{
		return 0;
	}

	@Override
	public boolean wantToSendEnergy(ItemStack par1)
	{
		return energyToSend(par1) > 0;
	}

	@Override
	public int energyToSend(ItemStack par1)
	{
		return 0;
	}

	@Override
	public boolean isEmpty(ItemStack par1)
	{
		return NBTHelper.getTag(par1, "Energy").getInteger("Stored") <= 0;
	}

	@Override
	public boolean isFull(ItemStack par1)
	{
		return getStoredMJ(par1) >= getMaxMJStorage(par1);
	}

	@Override
	public int charge(ItemStack par1, int amount, boolean simulate)
	{
		int stored = getStoredMJ(par1);
		int newValue = Math.min(stored+amount, getMaxMJStorage(par1));
		if(!simulate)
		{
			NBTHelper.getTag(par1, "Energy").setInteger("Stored", newValue);
		}
		newValue -= stored;
		return newValue;
	}

	@Override
	public int discharge(ItemStack par1, int amount, boolean simulate)
	{
		int stored = getStoredMJ(par1);
		int newValue = Math.max(stored-amount, 0);
		if(!simulate)
		{
			NBTHelper.getTag(par1, "Energy").setInteger("Stored", newValue);
		}
		stored -= newValue;
		return stored;
	}

	@Override
	public int getTransferlimit(ItemStack par1)
	{
		return type.getTransferlimit();
	}
	
	
	public static enum BatterieType
	{
		Small(30000, 100, "Small"),
		Medium(100000, 300, "Medium"),
		Big(2500000, 1250, "Big"),
		Huge(10000000, 3500, "Huge");
		
		int maxStorage;
		int transferlimit;
		String texture;
		Item data;
		
		private BatterieType(int par1, int par2, String par3)
		{
			maxStorage = par1;
			transferlimit = par2;
			texture = "BCBattery" + par3;
		}
		
		public void registerTexture(TextureEngine par1)
		{
			par1.registerTexture(data, texture);
		}
		
		public int getTransferlimit()
		{
			return transferlimit;
		}
		
		public int getMaxStorage()
		{
			return maxStorage;
		}
	}


	@Override
	public ItemInventory createNewInventory(EntityPlayer par1, ItemStack par2)
	{
		return new BatteryData(par1, par2);
	}

	@Override
	public String createNewInventoryID()
	{
		return "Battery:"+System.nanoTime();
	}

	@Override
	public Icon getTexture(ItemStack stack)
	{
		return getEngine().getTexture(this);
	}
	
}
