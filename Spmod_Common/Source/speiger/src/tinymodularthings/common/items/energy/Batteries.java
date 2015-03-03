package speiger.src.tinymodularthings.common.items.energy;

import java.util.List;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import speiger.src.api.common.data.nbt.NBTHelper;
import speiger.src.api.common.world.items.energy.IBCBattery;
import speiger.src.spmodapi.common.items.core.ItemInventory;
import speiger.src.spmodapi.common.items.core.SpmodInventoryItem;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.tinymodularthings.common.items.energy.Batteries.BatterieType;

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
	public void initExtraData(NBTTagCompound data)
	{
		data.setFloat("Limit", 1F);
		data.setBoolean("Input", true);
		data.setBoolean("AutoSend", false);
		NBTTagCompound net = new NBTTagCompound();
		net.setBoolean("Random", false);
		net.setBoolean("Multi", false);
		net.setFloat("SendMode", 1F);
		net.setFloat("TickRate", 0F);
		data.setCompoundTag("EnergyNet", net);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1, World par2, EntityPlayer par3)
	{
		if(par3.isSneaking())
		{
			return super.onItemRightClick(par1, par2, par3);
		}
		if(!par2.isRemote)
		{
			ItemEnergyNet.getEnergyNet().loadSettingsFromItem(par1).sendEnergyToItems(par1, par3);
		}
		return par1;
	}
	
	

	@Override
	public boolean sneakingStopTick(ItemStack par1)
	{
		return true;
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
		return getItemData(par1).getBoolean("Input");
	}

	@Override
	public int getRequestedAmount(ItemStack par1)
	{
		return getMaxMJStorage(par1) - getStoredMJ(par1);
	}

	@Override
	public boolean wantToSendEnergy(ItemStack par1)
	{
		return energyToSend(par1) > 0;
	}

	@Override
	public int energyToSend(ItemStack par1)
	{
		return getStoredMJ(par1);
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
		if(!getItemData(par1).hasKey("Limit"))
		{
			return type.getTransferlimit();
		}
		return (int)(this.getItemData(par1).getFloat("Limit") * type.getTransferlimit());
	}
	
	
	public static enum BatterieType
	{
		Small(30000, 100, 0.01F, "Small"),
		Medium(100000, 300, 0.003F, "Medium"),
		Big(2500000, 1250, 0.0007F, "Big"),
		Huge(10000000, 3500, 0.00025F, "Huge");
		
		int maxStorage;
		int transferlimit;
		float weelEffect;
		String texture;
		Item data;
		
		private BatterieType(int par1, int par2, float par3, String par4)
		{
			maxStorage = par1;
			transferlimit = par2;
			weelEffect = par3;
			texture = "BCBattery" + par4;
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
		
		public float getWeelEffect()
		{
			return weelEffect;
		}
	}
	
	@Override
	public String getBatteryID(ItemStack par1)
	{
		return getInventoryID(par1);
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

	public BatterieType getType()
	{
		return type;
	}
	
	@Override
	public boolean tickInventory(ItemStack par1)
	{
		return getItemData(par1).getBoolean("AutoSend") && getStoredMJ(par1) > 0;
	}

	@Override
	public boolean hasTickRate(ItemStack par1)
	{
		return getTickRate(par1) > 0;
	}
	
	

	@Override
	public int getTickRate(ItemStack par1)
	{
		return (int)(1000 * getItemData(par1).getCompoundTag("EnergyNet").getFloat("TickRate"));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3)
	{
		ItemStack empty = new ItemStack(this);
		this.discharge(empty, type.getMaxStorage(), false);
		par3.add(empty);
		
		ItemStack full = new ItemStack(this);
		this.charge(full, type.getMaxStorage(), false);
		par3.add(full);
	}

	@Override
	public boolean isDamaged(ItemStack stack)
	{
		return getDisplayDamage(stack) > 0;
	}

	@Override
	public int getDisplayDamage(ItemStack stack)
	{
		int storage = (int)(((double)getStoredMJ(stack) / (double)getMaxMJStorage(stack)) * (double)100);
		return 100 - storage;
	}
	
	
}
