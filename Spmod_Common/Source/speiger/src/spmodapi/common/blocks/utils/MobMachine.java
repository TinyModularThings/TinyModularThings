package speiger.src.spmodapi.common.blocks.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.util.Icon;
import speiger.src.api.inventory.InventorySorter;
import speiger.src.api.items.IEssens;
import speiger.src.api.items.IExpBottle;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.client.gui.utils.GuiMobMachine;
import speiger.src.spmodapi.common.enums.EnumGuiIDs;
import speiger.src.spmodapi.common.tile.TileFacing;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MobMachine extends TileFacing implements ISidedInventory
{
	//Static variables
	public static Icon[][] textures;
	public static HashMap<Integer, HashMap<DropType, List<ItemStack>>> allDrops = new HashMap<Integer, HashMap<DropType, List<ItemStack>>>();
	public static HashMap<Integer, HashMap<List<Integer>, Integer>> foodList = new HashMap<Integer, HashMap<List<Integer>, Integer>>();
	public static HashMap<Integer, Boolean> needExp = new HashMap<Integer, Boolean>();
	public static HashMap<Integer, Integer> neededExp = new HashMap<Integer, Integer>();
	public static HashMap<Integer, String[]> texture = new HashMap<Integer, String[]>();
	public static int totalTicks = 6000;
	
	
	//None Static variables
	public int type = 0;
	public ItemStack[] inv = new ItemStack[12];
	
	//Progresses
	public int eP = 0;//Exp Progress
	public int cP = 0;//Common Drops Progress
	public int rP = 0;//Rare Drops Progress
	public int lP = 0;//Legendary Drops Progress
	
	
	//Food and Co
	public int lifeEssens = 0;
	public int Exp = 0;
	public int food = 0;
	
	LinkedList<ItemStack> drops = new LinkedList<ItemStack>();
	
	public static void createMob(int id, boolean needexp, int exp, String[] textur)
	{
		Integer ID = Integer.valueOf(id);
		needExp.put(ID, needexp);
		neededExp.put(ID, exp);
		texture.put(ID, textur);
	}
	
	public static void addFood(int id, ItemStack par1, int foodValue)
	{
		HashMap<List<Integer>, Integer> list = foodList.get(Integer.valueOf(id));
		if(list == null)
		{
			list = new HashMap<List<Integer>, Integer>();
		}
		list.put(Arrays.asList(par1.itemID, par1.getItemDamage()), Integer.valueOf(foodValue));
		foodList.put(Integer.valueOf(id), list);
	}
	
	public static void addFood(int id, ItemStack[] par1, int[] par2)
	{
		if(par2.length != par1.length && par2.length != 1)
		{
			return;
		}
		if(par2.length == 1)
		{
			for(ItemStack item : par1)
			{
				addFood(id, item, par2[0]);
			}
		}
		else
		{
			for(int i = 0;i<par1.length;i++)
			{
				addFood(id, par1[i], par2[i]);
			}
		}
	}
	
	public static void addDrops(int id, DropType type, ItemStack drop)
	{
		HashMap<DropType, List<ItemStack>> droplist = allDrops.get(Integer.valueOf(id));
		if(droplist == null)
		{
			droplist = new HashMap<DropType, List<ItemStack>>();
		}
		if(droplist.get(type) == null)
		{
			droplist.put(type, new ArrayList<ItemStack>());
		}
		droplist.get(type).add(drop);
	}
	
	public static void addDrops(int id, DropType type, ItemStack...drop)
	{
		for(ItemStack par2 : drop)
		{
			addDrops(id, type, par2);
		}
	}
	

	@Override
	public boolean isSixSidedFacing()
	{
		return false;
	}

	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		if(renderPass == 0)
		{
			if(side == 4)
			{
				return textures[type][1];
			}
			return textures[type][0];
		}
		if(side == 2)
		{
			return textures[type][1];
		}
		return textures[type][0];
	}
	
	
	
	@Override
	public void registerIcon(IconRegister par1)
	{
		textures = new Icon[getBiggestNumber(texture.keySet())][2];
		for(Entry<Integer, String[]> par2 : texture.entrySet())
		{
			textures[par2.getKey()][0] = par1.registerIcon(par2.getValue()[0]);
			textures[par2.getKey()][1] = par1.registerIcon(par2.getValue()[1]);
		}
	}
	
	public static int getBiggestNumber(Set<Integer> par1)
	{
		int total = 0;
		for(Integer id : par1)
		{
			total = Math.max(id, total);
		}
		return total;
	}

	public boolean hasFood()
	{
		return food > 0;
	}
	
	public boolean hasLifeEssens()
	{
		return lifeEssens > 0;
	}
	
	public boolean hasProgress()
	{
		return eP > 0;
	}
	
	public void sortInventory()
	{
		ItemStack[] dropSorts = new ItemStack[9];
		for(int i = 0;i<dropSorts.length;i++)
		{
			dropSorts[i] = inv[i];
		}
		InventorySorter sorter = new InventorySorter(dropSorts);
		sorter.sortEverythingUp(true);
		dropSorts = sorter.finishSorting();
		for(int i = 0;i<dropSorts.length;i++)
		{
			inv[i] = dropSorts[i];
		}
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, nbt);
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
	{
		this.readFromNBT(pkt.data);
	}
	
	
	
	
	
	@Override
	public void onTick()
	{
		if(!worldObj.isRemote)
		{
			if(isValid())
			{
				if(canWork())
				{
					if(worldObj.getWorldTime() % 20 == 0)
					{
						food--;
					}
					lifeEssens--;
					eP++;
					cP++;
					rP++;
					lP++;
					if(eP >= totalTicks)
					{
						eP-=totalTicks;
						handleExp();
					}
					if(cP >= totalTicks)
					{
						cP-=totalTicks;
						this.handleDrops(DropType.Common);
					}
					if(rP >= totalTicks*3)
					{
						rP-=totalTicks*3;
						this.handleDrops(DropType.Rare);
					}
					if(lP >= totalTicks*7)
					{
						lP-=totalTicks*7;
						this.handleDrops(DropType.Legendary);
					}
				}
				else
				{
					tryRefuelFood();
					tryRefuelLifeEssens();
				}
				tryHandleExp();
				this.sortInventory();
				emptyArray();
			}
			
			if(worldObj.getWorldTime() % 10 == 0)
			{
				this.updateBlock();
				PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, getDescriptionPacket());
			}
		}
		
	}
	
	public void tryHandleExp()
	{
		boolean needExp = this.needExp.get(Integer.valueOf(type));
		if(needExp)
		{
			if(Exp < 100)
			{
				int needed = 500 - Exp;
				if(inv[10] != null && inv[10].getItem() instanceof IExpBottle)
				{
					IExpBottle exp = (IExpBottle) inv[10].getItem();
					Exp+= exp.discharge(inv[10], needed);
				}
			}
		}
		else
		{
			if(Exp > 100)
			{
				Exp -=100;
				drops.add(new ItemStack(Item.expBottle));
			}
		}
	}
	
	public void tryRefuelFood()
	{
		if(food < 10)
		{
			if(isValidFood())
			{
				food += this.foodList.get(type).get(Arrays.asList(inv[9].itemID, inv[9].getItemDamage()));
				this.inv[9].stackSize--;
				if(inv[9].stackSize <= 0)
				{
					inv[9] = null;
				}
			}
		}
	}
	
	public void tryRefuelLifeEssens()
	{
		if(lifeEssens < 100)
		{
			if(inv[11] != null && inv[11].getItem() instanceof IEssens)
			{
				IEssens es = (IEssens) inv[11].getItem();
				if(es != null && !es.isEssensEmpty(inv[11]))
				{
					lifeEssens+= es.getEssensValue(inv[11]);
					es.drainEssens(inv[11], 1);
				}
			}
		}
	}
	
	public void emptyArray()
	{
		if(inv[0] == null && !drops.isEmpty())
		{
			inv[0] = drops.removeFirst().copy();
		}
	}
	
	public void handleExp()
	{
		if(this.needExp.get(Integer.valueOf(type)))
		{
			Exp -= neededExp.get(Integer.valueOf(type));
		}
		else
		{
			Exp += neededExp.get(Integer.valueOf(type));
		}
	}
	
	public void handleDrops(DropType types)
	{
		 List<ItemStack> drop = this.allDrops.get(Integer.valueOf(type)).get(types);
		 if(drop == null || drop.isEmpty())
		 {
			 switch(types)
			 {
				case Common:
					rP+= totalTicks/4;
					lP+= totalTicks/8;
					break;
				case Rare:
					cP+= totalTicks/4;
					lP+= totalTicks/4;
					break;
				case Legendary:
					cP+= totalTicks/2;
					rP+= totalTicks/4;
					break;
			 }
		 }
		 else
		 {
			 drops.add(drop.get(worldObj.rand.nextInt(drop.size())));
		 }
	}
	
	public boolean isValid()
	{
		return type > 0;
	}


	public boolean canWork()
	{
		if(isValid())
		{
			return food > 0 && needExp.get(Integer.valueOf(type)) ? Exp > 0 : Exp >= 0 && lifeEssens > 0;
		}
		return false;
	}
	
	public boolean isValidFood()
	{
		if(isValid() && inv[9] != null)
		{
			return this.foodList.get(Integer.valueOf(type)).get(Arrays.asList(inv[9].itemID, inv[9].getItemDamage())) > 0;
		}
		return false;
	}
	
	

	public static enum DropType
	{
		Common,
		Rare,
		Legendary;
	}



	@Override
	public int getSizeInventory()
	{
		return inv.length;
	}

    public ItemStack getStackInSlot(int par1)
    {
        return this.inv[par1];
    }

    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.inv[par1] != null)
        {
            ItemStack itemstack;

            if (this.inv[par1].stackSize <= par2)
            {
                itemstack = this.inv[par1];
                this.inv[par1] = null;
                return itemstack;
            }
            else
            {
                itemstack = this.inv[par1].splitStack(par2);

                if (this.inv[par1].stackSize == 0)
                {
                    this.inv[par1] = null;
                }

                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.inv[par1] != null)
        {
            ItemStack itemstack = this.inv[par1];
            this.inv[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.inv[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }

	@Override
	public String getInvName()
	{
		return "MobMachine";
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
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
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1)
	{
		return new int[]{0,1,2,3,4,5,6,7,8};
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j)
	{
		return false;
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j)
	{
		return i < 9;
	}

	@Override
	public boolean onActivated(EntityPlayer par1)
	{
		if(!worldObj.isRemote)
		{
			par1.openGui(SpmodAPI.instance, EnumGuiIDs.Tiles.getID(), worldObj, xCoord, yCoord, zCoord);
			return true;
		}
		return false;
	}

	@Override
	public void onPlaced(int facing)
	{
		this.setFacing(facing);
	}

	@Override
	public boolean hasContainer()
	{
		return true;
	}

	@Override
	public Container getInventory(InventoryPlayer par1)
	{
		return new MobMachineInventory(par1, this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer getGui(InventoryPlayer par1)
	{
		return new GuiMobMachine(par1, this);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
        NBTTagList nbttaglist = nbt.getTagList("Items");
        this.inv = new ItemStack[this.getSizeInventory()];
        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.inv.length)
            {
                this.inv[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
        cP = nbt.getInteger("CP");
        eP = nbt.getInteger("EP");
        lP = nbt.getInteger("LP");
        rP = nbt.getInteger("RP");
        Exp = nbt.getInteger("Exp");
        food = nbt.getInteger("Food");
        lifeEssens = nbt.getInteger("Life");
        type = nbt.getInteger("Type");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.inv.length; ++i)
        {
            if (this.inv[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.inv[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
        nbt.setTag("Items", nbttaglist);
        nbt.setInteger("CP", cP);
        nbt.setInteger("EP", eP);
        nbt.setInteger("LP", lP);
        nbt.setInteger("RP", rP);
        nbt.setInteger("Exp", Exp);
        nbt.setInteger("Food", food);
        nbt.setInteger("Life", lifeEssens);
        nbt.setInteger("Type", type);
	}
	
	
	
	
}
