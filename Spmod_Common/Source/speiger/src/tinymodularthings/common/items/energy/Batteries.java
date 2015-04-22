package speiger.src.tinymodularthings.common.items.energy;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import speiger.src.api.common.data.nbt.NBTHelper;
import speiger.src.api.common.world.items.energy.IBCBattery;
import speiger.src.api.common.world.items.energy.ItemEnergyNet;
import speiger.src.spmodapi.common.items.core.ItemInventory;
import speiger.src.spmodapi.common.items.core.SpmodInventoryItem;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.proxy.LangProxy;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
	
	public NBTTagCompound getBatteryData(ItemStack par1)
	{
		return getItemData(par1).getCompoundTag("BatteryData");
	}
	
	@Override
	public void initExtraData(NBTTagCompound data)
	{
		data.setInteger("Mode", 0);
		NBTTagCompound net = new NBTTagCompound();
		net.setBoolean("Random", false);
		net.setBoolean("Multi", false);
		net.setFloat("SendMode", 1F);
		net.setFloat("ReqMode", 1F);
		data.setCompoundTag("EnergyNet", net);
		NBTTagCompound battery = new NBTTagCompound();
		battery.setFloat("TickRate", 0F);
		battery.setFloat("Transferlimit", 1F);
		battery.setFloat("MaxIn", 1F);
		battery.setFloat("MaxOut", 1F);
		battery.setBoolean("AutoOut", false);
		battery.setBoolean("AutoIn", false);
		battery.setBoolean("AllowIn", true);
		battery.setBoolean("AllowOut", true);
		data.setCompoundTag("BatteryData", battery);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1, World par2, EntityPlayer par3)
	{
		if(!par2.isRemote)
		{
			int mode = getItemData(par1).getInteger("Mode");
			if(par3.isSneaking())
			{
				
				mode++;
				if(mode > 2)
				{
					mode = 0;
				}
				getItemData(par1).setInteger("Mode", mode);
				par3.sendChatToPlayer(LangProxy.getText("Mode: "+getMode(mode)));
				return par1;
			}
			else
			{
				if(mode == 0)
				{
					return super.onItemRightClick(par1, par2, par3);
				}
				if(mode == 1)
				{
					boolean auto = !getBatteryData(par1).getBoolean("AutoOut");
					getBatteryData(par1).setBoolean("AutoOut", auto);
					par3.sendChatToPlayer(LangProxy.getText(auto ? "Activating Auto Exporting" : "Deactivating Auto Exporting"));
					if(auto && getBatteryData(par1).getBoolean("AllowIn"))
					{
						getBatteryData(par1).setBoolean("AllowIn", false);
						par3.sendChatToPlayer(LangProxy.getText("Dissabling Power Accepting."));
					}
					return par1;
				}
				if(mode == 2)
				{
					ItemEnergyNet.getEnergyNet().loadSettingsFromItem(par1).sendEnergyToItems(par1, par3);
					par3.inventoryContainer.detectAndSendChanges();
					return par1;
				}
			}
		}
		return par1;
	}
	
	public String getMode(int mode)
	{
		switch(mode)
		{
			case 0: return "Options";
			case 1: return "Automate Control";
			case 2: return "Manual Control";
			default: return "Nothing";
		}
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
		return getBatteryData(par1).getBoolean("AllowIn");
	}

	@Override
	public int getRequestedAmount(ItemStack par1)
	{
		return Math.min(getMaxMJStorage(par1) - getStoredMJ(par1), (int)((float)this.type.getTransferlimit() * getBatteryData(par1).getFloat("MaxIn")));
	}

	@Override
	public boolean wantToSendEnergy(ItemStack par1)
	{
		return getBatteryData(par1).getBoolean("AllowOut");
	}

	@Override
	public int energyToSend(ItemStack par1)
	{
		return Math.min(10000, (int)((float)this.type.getTransferlimit() * getBatteryData(par1).getFloat("MaxOut")));
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
		if(!getBatteryData(par1).hasKey("Transferlimit"))
		{
			return type.getTransferlimit();
		}
		return (int)(this.getBatteryData(par1).getFloat("Transferlimit") * type.getTransferlimit());
	}
	
	@Override
	public int getMaxTransferlimt(ItemStack par1)
	{
		return type.getTransferlimit();
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
	public String createNewInventoryID(int meta)
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
	public boolean canTick(ItemStack par1, ItemTickType par2)
	{
		return getBatteryData(par1).getBoolean("AutoOut") && getStoredMJ(par1) > 0;
	}
	
	@Override
	public int getTickRate(ItemStack par1, ItemTickType par2)
	{
		return (int)(1000 * getBatteryData(par1).getFloat("TickRate"));
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

	@Override
	public void onTick(ItemStack par1, World par2, EntityPlayer par3, ItemTickType par4, boolean par5)
	{
		ItemEnergyNet.getEnergyNet().loadSettingsFromItem(par1).sendEnergyToItems(par1, par3);
	}

	@Override
	public AdvContainer getContainer(InventoryPlayer par1, ItemStack par2)
	{
		return new AdvContainer(par1).setTile(this.createNewInventory(par1.player, par2)).setRequireFakeInv().setInventory(par1);
	}
	
	
}
