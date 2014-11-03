package speiger.src.tinymodularthings.common.blocks.machine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import speiger.src.api.blocks.BlockStack;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.nbt.INBTReciver;
import speiger.src.api.util.MathUtils;
import speiger.src.api.util.SpmodMod;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.utils.fluids.TinyFluidTank;
import cpw.mods.fml.common.FMLLog;
import forestry.api.core.ItemInterface;
import forestry.core.config.ForestryItem;

public class OilGenerator extends AdvTile implements ISidedInventory, INBTReciver, IFluidHandler
{
	public FluidTank lavaTank = new TinyFluidTank("lava", 10000, this);
	public FluidTank oilTank = new TinyFluidTank("oil", 10000, this);
	public static int TotalTime = 1728000;
	public long lastInjection = 0;
	public float StoredOil = 0.0F;
	
	public static HashMap<List<Integer>, ArrayList<OilEntry>> TodoList = new HashMap<List<Integer>, ArrayList<OilEntry>>();

	@Override
	public int getSizeInventory()
	{
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int i)
	{
		return null;
	}

	@Override
	public ItemStack decrStackSize(int i, int j)
	{
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i)
	{
		return null;
	}

	@Override
	public void setInventorySlotContents(int z, ItemStack itemstack)
	{
		if(itemstack != null)
		{
			if(TodoList.get(this.getPosition().getAsList()) == null)
			{
				TodoList.put(this.getPosition().getAsList(), new ArrayList<OilEntry>());
			}
			
			int result = Math.min(TotalTime, (int)worldObj.getTotalWorldTime() - (int)lastInjection);
			if(lastInjection == 0)
			{
				result = TotalTime;
			}
			if(result <= 0)
			{
				result = 5;
			}
			TodoList.get(this.getPosition().getAsList()).add(new OilEntry(itemstack.stackSize, result));
			lastInjection = worldObj.getTotalWorldTime();
		}
	}

	@Override
	public String getInvName()
	{
		return "Oil Generator";
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 1000;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer)
	{
		return true;
	}

	@Override
	public void openChest()
	{
		
	}

	@Override
	public void closeChest()
	{
		
	}
	
	@Override
	public boolean onActivated(EntityPlayer par1)
	{
		if(!worldObj.isRemote)
		{
			ArrayList<OilEntry> oil = TodoList.get(this.getPosition().getAsList());
			if(oil != null)
			{
				if(oil.isEmpty())
				{
					par1.sendChatToPlayer(LanguageRegister.createChatMessage("No Work at the Moment"));
				}
				else
				{
					if(par1.isSneaking())
					{
						par1.sendChatToPlayer(LanguageRegister.createChatMessage("Stored Lava: "+lavaTank.getFluidAmount()+ "mB / "+lavaTank.getCapacity()+"mB"));
						par1.sendChatToPlayer(LanguageRegister.createChatMessage("Stored Oil: "+oilTank.getFluidAmount()+ "mB / "+oilTank.getCapacity()+"mB"));
					}
					else
					{
						par1.sendChatToPlayer(LanguageRegister.createChatMessage("Total Tasks: "+oil.size()));
						String key = MathUtils.getTicksInTimeShort(oil.get(0).tick);
						if(key.length() == 2)
						{
							key = "00:"+key;
						}
						par1.sendChatToPlayer(LanguageRegister.createChatMessage("Current Task need: "+key+" Minutes"));
					}
				}
			}
			else
			{
				if(par1.isSneaking())
				{
					par1.sendChatToPlayer(LanguageRegister.createChatMessage("Stored Lava: "+lavaTank.getFluidAmount()+ "mB / "+lavaTank.getCapacity()+"mB"));
					par1.sendChatToPlayer(LanguageRegister.createChatMessage("Stored Oil: "+oilTank.getFluidAmount()+ "mB / "+oilTank.getCapacity()+"mB"));
				}
				else
				{
					par1.sendChatToPlayer(LanguageRegister.createChatMessage("No Work at the Moment"));
				}
			}
		}
		return true;
	}
	
	@Override
	public boolean hasContainer()
	{
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return true;
	}
	
	@Override
	public int[] getAccessibleSlotsFromSide(int var1)
	{
		return new int[]{0};
	}
	
	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j)
	{
		if(lavaTank.getFluidAmount() <= 0)
		{
			return false;
		}
		
		if(FluidRegistry.isFluidRegistered("oil"))
		{
			int itemID = itemstack.itemID;
			if(itemID == Item.porkRaw.itemID || itemID == Item.beefRaw.itemID || itemID == Item.chickenRaw.itemID)
			{
				return true;
			}
			try
			{
				if(itemID == ItemInterface.getItem("beeDroneGE").itemID || itemID == ItemInterface.getItem("beeLarvaeGE").itemID || itemID == ItemInterface.getItem("beePrincessGE").itemID || itemID == ItemInterface.getItem("beeQueenGE").itemID)
				{
					return true;
				}
			}
			catch(Exception e)
			{
			}
		}
		return false;
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j)
	{
		return false;
	}

	@Override
	public void onTick()
	{
		if(!worldObj.isRemote)
		{
			if(TodoList.get(this.getPosition().getAsList()) != null && !TodoList.get(this.getPosition().getAsList()).isEmpty())
			{
				if(lavaTank.drain(1, false) != null && lavaTank.drain(1, false).amount == 1)
				{
					if(worldObj.getWorldTime() % 20 == 0)
					{
						lavaTank.drain(1, true);
					}
					OilEntry oil = TodoList.get(this.getPosition().getAsList()).get(0);
					if(!oil.hasWork())
					{
						TodoList.get(this.getPosition().getAsList()).remove(0);
						float toAdd = oil.amount;
						toAdd /= 10;
						StoredOil += toAdd;
					}
				}
				else
				{
					OilEntry oil = TodoList.get(this.getPosition().getAsList()).get(0);
					if(oil.canNotWork())
					{
						TodoList.remove(0);
						if(TodoList.size() > 0)
						{
							TodoList.get(this.getPosition().getAsList()).get(0).tick+=TotalTime;
						}
					}
				}
			}
			
			for(;StoredOil > 1.0F && oilTank.fill(FluidRegistry.getFluidStack("oil", 1), false) == 1;)
			{
				oilTank.fill(FluidRegistry.getFluidStack("oil", 1), true);
				StoredOil-=1F;
			}
		}
	}

	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		TextureEngine engine = TextureEngine.getTextures();
		ArrayList<OilEntry> oil = TodoList.get(getPosition().getAsList());
		if(oil != null && oil.size() > 0)
		{
			if(side == 0)
			{
				return engine.getTexture(TinyBlocks.machine, 4, 0);
			}
			else if(side == 1)
			{
				return engine.getTexture(TinyBlocks.machine, 4, 2);
			}
			else
			{
				return engine.getTexture(TinyBlocks.machine, 4, 4);
			}
		}
		else
		{
			if(side == 0)
			{
				return engine.getTexture(TinyBlocks.machine, 4, 0);
			}
			else if(side == 1)
			{
				return engine.getTexture(TinyBlocks.machine, 4, 1);
			}
			else
			{
				return engine.getTexture(TinyBlocks.machine, 4, 3);
			}
		}
	}
	
	
	
	@Override
	public void onBreaking()
	{
		super.onBreaking();
		TodoList.remove(this.getPosition().getAsList());
	}

	@Override
	public boolean SolidOnSide(ForgeDirection side)
	{
		return true;
	}

	@Override
	public boolean isNormalCube()
	{
		return true;
	}

	public class OilEntry
	{
		int amount;
		int tick = 0;
		int remove = 0;
		
		public OilEntry(NBTTagCompound nbt)
		{
			readFromNBT(nbt);
		}
		
		public OilEntry(int amount, int time)
		{
			this.amount = amount;
			tick = time;
		}
		
		public boolean hasWork()
		{
			if(remove > 0)
			{
				remove--;
				return true;
			}
			tick--;;
			if(tick <= 0)
			{
				return false;
			}
			return true;
		}
		
		public boolean canNotWork()
		{
			remove++;
			if(remove > 2000)
			{
				return true;
			}
			return false;
		}
		
		public void readFromNBT(NBTTagCompound nbt)
		{
			amount = nbt.getInteger("Amount");
			tick = nbt.getInteger("Progress");
			remove = nbt.getInteger("Miss");
		}
		
		public void writeToNBT(NBTTagCompound nbt)
		{
			nbt.setInteger("Amount", amount);
			nbt.setInteger("Progress", tick);
			nbt.setInteger("Miss", remove);
		}
	}
	
	

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		StoredOil = nbt.getFloat("StoredOil");
		this.lastInjection = nbt.getLong("LastInjection");
		lavaTank.readFromNBT(nbt.getCompoundTag("Lava"));
		oilTank.readFromNBT(nbt.getCompoundTag("Oil"));
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setFloat("StoredOil", StoredOil);
		nbt.setLong("LastInjection", lastInjection);
		nbt.setCompoundTag("Lava", lavaTank.writeToNBT(new NBTTagCompound()));
		nbt.setCompoundTag("Oil", oilTank.writeToNBT(new NBTTagCompound()));
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		if(FluidRegistry.LAVA.getID() == resource.fluidID)
		{
			return lavaTank.fill(resource, doFill);
		}
		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		return this.drain(from, resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		return oilTank.drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return true;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		return new FluidTankInfo[]{oilTank.getInfo(), lavaTank.getInfo()};
	}

	@Override
	public void loadFromNBT(NBTTagCompound par1)
	{
		TodoList.clear();
		NBTTagList list = par1.getTagList("Data");
		for(int i = 0;i<list.tagCount();i++)
		{
			NBTTagCompound nbt = (NBTTagCompound)list.tagAt(i);
			int[] key = nbt.getIntArray("Key");
			NBTTagList oil = nbt.getTagList("Value");
			ArrayList<OilEntry> entries = new ArrayList<OilEntry>();
			for(int z = 0;z<oil.tagCount();z++)
			{
				NBTTagCompound oilData = (NBTTagCompound)oil.tagAt(z);
				entries.add(new OilEntry(oilData));
			}
			TodoList.put(Arrays.asList(key[0], key[1], key[2], key[3]), entries);
		}
	}

	@Override
	public void saveToNBT(NBTTagCompound par1)
	{
		NBTTagList list = new NBTTagList();
		Iterator<Entry<List<Integer>, ArrayList<OilEntry>>> iter = TodoList.entrySet().iterator();
		for(;iter.hasNext();)
		{
			Entry<List<Integer>, ArrayList<OilEntry>> entry = iter.next();
			NBTTagCompound nbt = new NBTTagCompound();
			List<Integer> key = entry.getKey();
			nbt.setIntArray("Key", new int[]{key.get(0), key.get(1), key.get(2), key.get(3)});
			ArrayList<OilEntry> oil = entry.getValue();
			NBTTagList data = new NBTTagList();
			for(int i = 0;i<oil.size();i++)
			{
				NBTTagCompound oilData = new NBTTagCompound();
				oil.get(i).writeToNBT(oilData);
				data.appendTag(oilData);
			}
			nbt.setTag("Value", data);
			list.appendTag(nbt);
		}
		par1.setTag("Data", list);
	}

	@Override
	public void finishLoading()
	{
		
	}

	@Override
	public SpmodMod getOwner()
	{
		return TinyModularThings.instance;
	}

	@Override
	public String getID()
	{
		return "oilGenerator";
	}

}
