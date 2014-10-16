package speiger.src.tinymodularthings.common.items.energy;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.energy.IBCBattery;
import speiger.src.api.energy.IEnergyProvider;
import speiger.src.api.energy.IEnergySubject;
import speiger.src.api.items.DisplayItem;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.nbt.NBTHelper;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.items.core.TinyItem;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Batteries extends TinyItem implements IBCBattery
{
	String name;
	BatterieType type;
	
	public Batteries(int par1, String par2, BatterieType par3)
	{
		super(par1);
		type = par3;
		name = "mj.battery." + par2;
		this.setMaxDamage(100);
		this.setMaxStackSize(1);
		this.setNoRepair();
		this.setCreativeTab(CreativeTabs.tabFood);
		par3.data = this;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		if(!NBTHelper.nbtCheck(par1ItemStack, "Battery"))
		{
			return;
		}
		par3List.add("Stored MJ: " + getStoredMJ(par1ItemStack) + " MJ / " + getMaxStorage(par1ItemStack) + " MJ");
		par3List.add("Transferlimit: " + getTransferlimit(par1ItemStack) + " MJ");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		ItemStack empty = createBattery(par1, type);
		par3List.add(empty);
		ItemStack full = createBattery(par1, type);
		full.getTagCompound().getCompoundTag("Battery").setInteger("StoredMJ", type.maxStorage);
		par3List.add(full);
	}
	
	public static ItemStack createBattery(int par1, BatterieType par2)
	{
		ItemStack stack = new ItemStack(par1, 1, 0);
		NBTTagCompound nbt = new NBTTagCompound();
		stack.setTagInfo("Battery", nbt);
		return stack;
	}
	
	@Override
	public void registerItems(int id, SpmodMod par0)
	{
		if(!SpmodModRegistry.areModsEqual(par0, getMod()))
		{
			return;
		}
		LanguageRegister.getLanguageName(new DisplayItem(id), name, par0);
	}
	
	@Override
	public int getStoredMJ(ItemStack par1)
	{
		if(!NBTHelper.nbtCheck(par1, "Battery"))
		{
			return 0;
		}
		return par1.getTagCompound().getCompoundTag("Battery").getInteger("StoredMJ");
	}
	
	@Override
	public int getMaxStorage(ItemStack par1)
	{
		return type.getMaxStorage();
	}
	
	@Override
	public int charge(ItemStack par1, int par2, boolean par4, boolean par3)
	{
		if(this.requestEnergy(par1))
		{
			int realAdding = 0;
			if(par4)
			{
				realAdding = par2;
			}
			else
			{
				Math.min(par2, getTransferlimit(par1));
			}
			
			if(getStoredMJ(par1) + realAdding > getMaxStorage(par1))
			{
				int added = realAdding - ((getStoredMJ(par1) + realAdding) - getMaxStorage(par1));
				if(!par3)
				{
					par1.getTagCompound().getCompoundTag("Battery").setInteger("StoredMJ", getMaxStorage(par1));
				}
				return added;
			}
			else
			{
				if(!par3)
				{
					par1.getTagCompound().getCompoundTag("Battery").setInteger("StoredMJ", getStoredMJ(par1) + realAdding);
				}
				
				return realAdding;
			}
		}
		return 0;
	}
	
	@Override
	public int discharge(ItemStack par1, int par2, boolean par4, boolean par3)
	{
		if(!NBTHelper.nbtCheck(par1, "Battery"))
		{
			return 0;
		}
		
		int realRemoving = 0;
		if(par4)
		{
			realRemoving = par2;
		}
		else
		{
			realRemoving = Math.min(getTransferlimit(par1), par2);
		}
		
		if(getStoredMJ(par1) - realRemoving < 0)
		{
			int removed = realRemoving - (realRemoving - getStoredMJ(par1));
			if(!par3)
			{
				par1.getTagCompound().getCompoundTag("Battery").setInteger("StoredMJ", 0);
			}
			return removed;
		}
		else
		{
			if(!par3)
			{
				par1.getTagCompound().getCompoundTag("Battery").setInteger("StoredMJ", getStoredMJ(par1) - realRemoving);
			}
			return realRemoving;
		}
		
	}
	
	@Override
	public int getTransferlimit(ItemStack par1)
	{
		return type.getTransferlimit();
	}
	
	@Override
	public BatteryType getType(ItemStack par1)
	{
		return BatteryType.Battery;
	}
	
	@Override
	public boolean requestEnergy(ItemStack par1)
	{
		if(!NBTHelper.nbtCheck(par1, "Battery"))
		{
			return false;
		}
		return par1.getTagCompound().getCompoundTag("Battery").getBoolean("Activated");
	}
	
	@Override
	public boolean isEmpty(ItemStack par1)
	{
		return getStoredMJ(par1) <= 0;
	}
	
	@Override
	public boolean isFull(ItemStack par1)
	{
		return getStoredMJ(par1) >= getMaxStorage(par1);
	}
	
	@Override
	public int getStoredMJInPercent(ItemStack par1)
	{
		double pro = ((double)getStoredMJ(par1) / (double)getMaxStorage(par1)) * 100;
		return (int)pro;
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod par0)
	{
		return LanguageRegister.getLanguageName(new DisplayItem(par1.getItem()), name, par0);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack par1, World par2, EntityPlayer par3)
	{
		if(!par2.isRemote)
		{
			MovingObjectPosition pos = this.getMovingObjectPositionFromPlayer(par2, par3, true);
			if(isNotImportantBlock(pos, par2))
			{
				InventoryPlayer inv = par3.inventory;
				for(int i = 0;i < inv.getHotbarSize();i++)
				{
					if(inv.currentItem == i)
					{
						continue;
					}
					ItemStack stack = inv.getStackInSlot(i);
					if(stack != null && stack.getItem() instanceof IBCBattery)
					{
						IBCBattery battery = (IBCBattery)stack.getItem();
						if(!battery.isFull(stack) && battery.requestEnergy(stack))
						{
							this.discharge(par1, battery.charge(stack, this.discharge(par1, 10000, false, true), false, false), false, false);
							break;
						}
					}
				}
			}
			else
			{
				TileEntity tile = par2.getBlockTileEntity(pos.blockX, pos.blockY, pos.blockZ);
				if(tile != null)
				{
					boolean flag = false;
					if(tile instanceof IEnergyProvider)
					{
						IEnergySubject provider = ((IEnergyProvider)tile).getEnergyProvider(ForgeDirection.getOrientation(pos.sideHit));
						if(provider != null)
						{
							this.discharge(par1, provider.addEnergy(this.discharge(par1, 10000, false, true), false), false, false);
							flag = true;
						}
					}
					else if(tile instanceof IPowerReceptor)
					{
						PowerReceiver provider = ((IPowerReceptor)tile).getPowerReceiver(ForgeDirection.getOrientation(pos.sideHit));
						if(provider != null)
						{
							this.discharge(par1, new Float(provider.receiveEnergy(Type.STORAGE, this.discharge(par1, 100000, false, true), ForgeDirection.getOrientation(pos.sideHit))).intValue(), false, false);
							flag = true;
						}
					}
					if(flag)
					{
						par3.swingItem();
					}
					
				}
			}
		}
		return par1;
	}
	
	public boolean isNotImportantBlock(MovingObjectPosition par1, World world)
	{
		if(par1 == null)
		{
			return true;
		}
		else
		{
			TileEntity tile = world.getBlockTileEntity(par1.blockX, par1.blockY, par1.blockZ);
			if(tile != null && (tile instanceof IPowerReceptor || tile instanceof IEnergyProvider))
			{
				return false;
			}
		}
		return true;
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
		

		
		public static void registerTextures(TextureEngine par1)
		{
			try
			{
				BatterieType[] types = values();
				par1.setCurrentPath("energy");
				for(int i = 0;i<types.length;i++)
				{
					par1.registerTexture(types[i].data, types[i].texture);
				}
			}
			catch(Exception e)
			{
				for(StackTraceElement el : e.getStackTrace())
				{
					FMLLog.getLogger().info(""+el);
					
				}
				System.exit(0);
			}
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
}
