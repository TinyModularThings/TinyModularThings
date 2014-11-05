package speiger.src.spmodapi.common.blocks.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.client.gui.inventory.GuiContainer;
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
import speiger.src.api.common.utils.InventoryUtil;
import speiger.src.api.common.utils.MathUtils;
import speiger.src.api.common.utils.config.EntityCounter;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.api.common.world.items.IEssens;
import speiger.src.api.common.world.items.IExpBottle;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.client.gui.utils.GuiMobMachine;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.enums.EnumGuiIDs;
import speiger.src.spmodapi.common.interfaces.ITextureRequester;
import speiger.src.spmodapi.common.lib.SpmodAPILib;
import speiger.src.spmodapi.common.tile.TileFacing;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.proxy.LangProxy;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MobMachine extends TileFacing implements ISidedInventory,
		ITextureRequester
{
	// Static variables
	public static HashMap<Integer, HashMap<DropType, List<ItemStack>>> allDrops = new HashMap<Integer, HashMap<DropType, List<ItemStack>>>();
	public static HashMap<Integer, HashMap<List<Integer>, Integer>> foodList = new HashMap<Integer, HashMap<List<Integer>, Integer>>();
	public static HashMap<Integer, Boolean> needExp = new HashMap<Integer, Boolean>();
	public static HashMap<Integer, Integer> neededExp = new HashMap<Integer, Integer>();
	public static HashMap<Integer, String[]> texture = new HashMap<Integer, String[]>();
	public static HashMap<Integer, String> names = new HashMap<Integer, String>();
	public static HashMap<List<Integer>, Integer> activators = new HashMap<List<Integer>, Integer>();
	public static int totalTicks = 12000;
	
	// None Static variables
	public int type = 0;
	public ItemStack[] inv = new ItemStack[12];
	
	// Progresses
	public int eP = 0;// Exp Progress
	public int cP = 0;// Common Drops Progress
	public int rP = 0;// Rare Drops Progress
	public int lP = 0;// Legendary Drops Progress
	
	// Food and Co
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
	
	public static void setName(int id, String name)
	{
		names.put(Integer.valueOf(id), name);
	}
	
	public static String getName(int id)
	{
		return names.get(Integer.valueOf(id));
	}
	
	public static void addActivators(int id, ItemStack... par1)
	{
		for(ItemStack cu : par1)
		{
			if(cu != null)
			{
				addActivator(id, cu);
			}
		}
	}
	
	public static void addActivator(int id, ItemStack par1)
	{
		List<Integer> item = Arrays.asList(par1.itemID, par1.getItemDamage());
		activators.put(item, id);
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
			for(int i = 0;i < par1.length;i++)
			{
				addFood(id, par1[i], par2[i]);
			}
		}
	}
	
	public static void addDrops(int id, DropType type, ItemStack drop)
	{
		HashMap<DropType, List<ItemStack>> droplist = allDrops.get(Integer.valueOf(id));
		boolean flag = false;
		if(droplist == null)
		{
			droplist = new HashMap<DropType, List<ItemStack>>();
			flag = true;
		}
		if(droplist.get(type) == null)
		{
			droplist.put(type, new ArrayList<ItemStack>());
		}
		droplist.get(type).add(drop);
		if(flag)
		{
			allDrops.put(Integer.valueOf(id), droplist);
		}
	}
	
	public static void addDrops(int id, DropType type, ItemStack... drop)
	{
		for(ItemStack par2 : drop)
		{
			addDrops(id, type, par2);
		}
	}
	
	public static int[] getMobMachineResultItem(ItemStack par1)
	{
		ArrayList<Integer> ints = new ArrayList<Integer>();
		Iterator<Entry<Integer, HashMap<DropType, List<ItemStack>>>> iter = allDrops.entrySet().iterator();
		for(;iter.hasNext();)
		{
			Entry<Integer, HashMap<DropType, List<ItemStack>>> entry = iter.next();
			Integer key = entry.getKey();
			Collection<List<ItemStack>> value = entry.getValue().values();
			boolean got = false;
			Iterator<List<ItemStack>> stacks = value.iterator();
			for(;!got && stacks.hasNext();)
			{
				for(ItemStack stack : stacks.next())
				{
					if(InventoryUtil.isItemEqual(stack, par1))
					{
						ints.add(key);
						got = true;
						break;
					}
				}
			}
		}
		

		return MathUtils.getArrayFromList(ints);
	}
	
	@Override
	public void onIconMakerLoading()
	{
		this.setFacing(5);
	}

	public static int[] getMobMachineFromFoodItem(ItemStack par1)
	{
		ArrayList<Integer> ints = new ArrayList<Integer>();
		Iterator<Entry<Integer, HashMap<List<Integer>, Integer>>> foodIter = foodList.entrySet().iterator();
		for(;foodIter.hasNext();)
		{
			Entry<Integer, HashMap<List<Integer>, Integer>> entry = foodIter.next();
			Integer key = entry.getKey();
			Set<List<Integer>> value = entry.getValue().keySet();
			boolean got = false;
			Iterator<List<Integer>> items = value.iterator();
			for(;!got && items.hasNext();)
			{
				List<Integer> item = items.next();
				if(InventoryUtil.isItemEqual(new ItemStack(item.get(0), 1, item.get(1)), par1))
				{
					got = true;
					ints.add(key);
				}
			}
		}
		return MathUtils.getArrayFromList(ints);
	}
	
	public static ArrayList<ItemStack> getResults(int MobMachineType)
	{
		ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
		
		HashMap<DropType, List<ItemStack>> type = allDrops.get(Integer.valueOf(MobMachineType));
		if(type != null)
		{
			List<ItemStack> drops = type.get(DropType.Common);
			if(drops != null && drops.size() > 0)
			{
				ArrayList<ItemStack> between = new ArrayList<ItemStack>();
				between.addAll(drops);
				HashMap<Integer, EntityCounter> types = new HashMap<Integer, EntityCounter>();
				for(int i = 0;i<between.size();i++)
				{
					ItemStack par1 = between.get(i);
					if(types.get(par1.itemID) == null)
					{
						types.put(par1.itemID, new EntityCounter());
					}
					else
					{
						types.get(par1.itemID).updateToNextID();
						between.remove(--i);
					}
				}
				for(ItemStack stack : between)
				{
					int size = types.get(stack.itemID).getCurrentID();
					List<String> list = Arrays.asList("Common Drop");
					if(size > 1)
					{
						list = Arrays.asList("Common Drop", size+" Differend Types of this Item");
					}
					stacks.add(LangProxy.getItemStackWithInfo(stack.copy(), list));
				}
				
			}
			drops = type.get(DropType.Rare);
			if(drops != null && drops.size() > 0)
			{
				ArrayList<ItemStack> between = new ArrayList<ItemStack>();
				between.addAll(drops);
				HashMap<Integer, EntityCounter> types = new HashMap<Integer, EntityCounter>();
				for(int i = 0;i<between.size();i++)
				{
					ItemStack par1 = between.get(i);
					if(types.get(par1.itemID) == null)
					{
						types.put(par1.itemID, new EntityCounter());
					}
					else
					{
						types.get(par1.itemID).updateToNextID();
						between.remove(--i);
					}
				}
				for(ItemStack stack : between)
				{
					int size = types.get(stack.itemID).getCurrentID();
					List<String> list = Arrays.asList("Rare Drop");
					if(size > 1)
					{
						list = Arrays.asList("Rare Drop", size+" Differend Types of this Item");
					}
					stacks.add(LangProxy.getItemStackWithInfo(stack.copy(), list));
				}
			}
			drops = type.get(DropType.Legendary);
			if(drops != null && drops.size() > 0)
			{
				ArrayList<ItemStack> between = new ArrayList<ItemStack>();
				between.addAll(drops);
				HashMap<Integer, EntityCounter> types = new HashMap<Integer, EntityCounter>();
				for(int i = 0;i<between.size();i++)
				{
					ItemStack par1 = between.get(i);
					if(types.get(par1.itemID) == null)
					{
						types.put(par1.itemID, new EntityCounter());
					}
					else
					{
						types.get(par1.itemID).updateToNextID();
						between.remove(i--);
					}
				}
				for(ItemStack stack : between)
				{
					int size = types.get(stack.itemID).getCurrentID();
					List<String> list = Arrays.asList("Legendary Drop");
					if(size > 1)
					{
						list = Arrays.asList("Legendary Drop", size+" Differend Types of this Item");
					}
					stacks.add(LangProxy.getItemStackWithInfo(stack.copy(), list));
				}
			}
		}
		return stacks;
	}
	
	public static ArrayList<ItemStack> getAllFoodItems(int MobMachineType)
	{
		ArrayList<ItemStack> stack = new ArrayList<ItemStack>();
		HashMap<List<Integer>, Integer> food = foodList.get(MobMachineType);
		if(food != null)
		{
			Iterator<List<Integer>> iter = food.keySet().iterator();
			for(;iter.hasNext();)
			{
				List<Integer> key = iter.next();
				stack.add(new ItemStack(key.get(0), 1, key.get(1)));
			}
		}
		
		return stack;
	}
	
	@Override
	public boolean isSixSidedFacing()
	{
		return false;
	}
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		Icon[] array = TextureEngine.getIcon(APIBlocks.blockUtils, 2);
		if(side == this.getFacing())
		{
			return array[(type * 2) + 1];
		}
		return array[type * 2];
	}
	
	@Override
	public void registerIcon(TextureEngine par1)
	{
		par1.markForDelay(this);
	}
	
	@Override
	public boolean onTextureAfterRegister(TextureEngine par1)
	{
		boolean flag = false;
		par1.setCurrentMod(SpmodAPILib.ModID.toLowerCase());
		par1.setCurrentPath("mobmachine");
		ArrayList<String> textures = new ArrayList<String>();
		for(int i = 0;i<texture.size();i++)
		{
			String[] array = texture.get(i);
			
			textures.add(array[0]);
			textures.add(array[1]);
			flag = true;
		}
		par1.registerTexture(new BlockStack(APIBlocks.blockUtils, 2), textures.toArray(new String[0]));
		par1.removePath();
		par1.finishMod();
		return flag;
	}
	
	public static int getBiggestNumber(Set<Integer> par1)
	{
		int total = 0;
		for(Integer id : par1)
		{
			total = Math.max(id, total);
		}
		total++;
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
		if(!worldObj.isRemote && worldObj.getWorldTime() % 10 == 0)
		{
			this.transferItem(0, 8, true);
			this.transferItem(0, 7, true);
			this.transferItem(0, 6, true);
			this.transferItem(0, 5, true);
			this.transferItem(0, 4, true);
			this.transferItem(0, 3, true);
			this.transferItem(0, 2, true);
			this.transferItem(0, 1, true);
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
						eP -= totalTicks;
						handleExp();
					}
					if(cP >= totalTicks * 2)
					{
						cP -= totalTicks * 2;
						this.handleDrops(DropType.Common);
					}
					if(rP >= totalTicks * 5)
					{
						rP -= totalTicks * 5;
						this.handleDrops(DropType.Rare);
					}
					if(lP >= totalTicks * 7)
					{
						lP -= totalTicks * 9;
						this.handleDrops(DropType.Legendary);
					}
				}
				tryRefuelFood();
				tryRefuelLifeEssens();
				tryHandleExp();
				emptyArray();
				if(worldObj.getWorldTime() % 10 == 0)
				{
					this.sortInventory();
				}
				
			}
			
			if(worldObj.getWorldTime() % 10 == 0)
			{
				this.updateBlock();
				PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, getDescriptionPacket());
			}
		}
		
	}
	
	@Override
	public ArrayList<ItemStack> onDrop(int fortune)
	{
		ArrayList<ItemStack> stack = super.onDrop(fortune);
		for(ItemStack data : inv)
		{
			if(data != null)
			{
				stack.add(data);
			}
		}
		stack.add(new ItemStack(APIItems.mobMachineHelper, 1, type));
		return stack;
	}
	
	public void tryHandleExp()
	{
		boolean needExp = this.needExp.get(Integer.valueOf(type));
		
		if(needExp)
		{
			if(Exp < 100)
			{
				int needed = 500 - Exp;
				if(inv[11] != null && inv[11].getItem() instanceof IExpBottle)
				{
					IExpBottle exp = (IExpBottle)inv[11].getItem();
					Exp += exp.discharge(inv[11], needed);
				}
			}
		}
		else
		{
			if(Exp > 100)
			{
				Exp -= 100;
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
			if(inv[10] != null && inv[10].getItem() instanceof IEssens)
			{
				IEssens es = (IEssens)inv[10].getItem();
				if(es != null && !es.isEssensEmpty(inv[10]))
				{
					lifeEssens += es.getEssensValue(inv[10]);
					es.drainEssens(inv[10], 1);
				}
			}
		}
	}
	
	public void emptyArray()
	{
		if(inv[0] == null && !drops.isEmpty())
		{
			if(!worldObj.isRemote)
			{
				ItemStack stack = drops.removeFirst();
				this.inv[0] = stack.copy();
				return;
			}
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
		HashMap<DropType, List<ItemStack>> dropTypes = this.allDrops.get(Integer.valueOf(type));
		List<ItemStack> drop = null;
		if(dropTypes != null && dropTypes.get(types) != null)
		{
			drop = dropTypes.get(types);
		}
		
		if(this.worldObj.rand.nextInt(100) <= types.getChance())
		{
			return;
		}
		
		if(drop == null || drop.isEmpty())
		{
			switch(types)
			{
				case Common:
					rP += totalTicks / 2;
					lP += totalTicks / 4;
					break;
				case Rare:
					cP += totalTicks;
					lP += totalTicks / 2;
					break;
				case Legendary:
					cP += totalTicks * 2;
					rP += totalTicks / 2;
					break;
			}
		}
		else
		{
			ItemStack stack = drop.get(worldObj.rand.nextInt(drop.size()));
			drops.add(stack);
		}
	}
	
	public boolean isValid()
	{
		return type > 0;
	}
	
	public boolean canWork()
	{
		if(lifeEssens == 0)
		{
			lifeEssens = 1000;
		}
		if(inv[0] != null)
		{
			return false;
		}
		if(isValid())
		{
			return food > 0 && (needExp.get(Integer.valueOf(type)) ? Exp > 0 : Exp >= 0) && lifeEssens > 0;
		}
		return false;
	}
	
	public boolean isValidFood()
	{
		if(isValid() && inv[9] != null)
		{
			return this.foodList.get(Integer.valueOf(type)).get(Arrays.asList(inv[9].itemID, inv[9].getItemDamage())) != null && this.foodList.get(Integer.valueOf(type)).get(Arrays.asList(inv[9].itemID, inv[9].getItemDamage())) > 0;
		}
		return false;
	}
	
	public static enum DropType
	{
		Common(90),
		Rare(50),
		Legendary(25);
		int chance;
		
		private DropType(int chance)
		{
			this.chance = chance;
		}
		
		public int getChance()
		{
			return this.chance;
		}
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
		if(this.inv[par1] != null)
		{
			ItemStack itemstack;
			
			if(this.inv[par1].stackSize <= par2)
			{
				itemstack = this.inv[par1];
				this.inv[par1] = null;
				return itemstack;
			}
			else
			{
				itemstack = this.inv[par1].splitStack(par2);
				
				if(this.inv[par1].stackSize == 0)
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
		if(this.inv[par1] != null)
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
		
		if(par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
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
		if(!this.isValid())
		{
			return new int[0];
		}
		return new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8 };
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
			if(this.isValid())
			{
				par1.openGui(SpmodAPI.instance, EnumGuiIDs.Tiles.getID(), worldObj, xCoord, yCoord, zCoord);
				return true;
			}
			else
			{
				ItemStack current = par1.getCurrentEquippedItem();
				if(current != null)
				{
					List<Integer> key = Arrays.asList(current.itemID, current.getItemDamage());
					if(activators.containsKey(key))
					{
						this.type = activators.get(key);
						current.stackSize--;
						par1.sendChatToPlayer(LangProxy.getText("Initizialized MobMachine to: " + this.names.get(type)));
						this.updateBlock();
						this.worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
						this.worldObj.notifyBlockChange(xCoord, yCoord, zCoord, getBlockType().blockID);
						PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 40, worldObj.provider.dimensionId, getDescriptionPacket());
						return true;
					}
					else if(current.itemID == APIItems.mobMachineHelper.itemID)
					{
						type = current.getItemDamage();
						current.stackSize--;
						par1.sendChatToPlayer(LangProxy.getText("Initizialized MobMachine to: " + this.names.get(type)));
						this.updateBlock();
						this.worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
						this.worldObj.notifyBlockChange(xCoord, yCoord, zCoord, getBlockType().blockID);
						PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 40, worldObj.provider.dimensionId, getDescriptionPacket());
						return true;
					}
					else
					{
						par1.sendChatToPlayer(LangProxy.getText("Not a valid init Item/Block"));
						return true;
					}
				}
				else
				{
					par1.sendChatToPlayer(LangProxy.getText("Need to be Initizialized"));
				}
			}
		}
		return true;
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
		for(int i = 0;i < nbttaglist.tagCount();++i)
		{
			NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");
			
			if(b0 >= 0 && b0 < this.inv.length)
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
		for(int i = 0;i < this.inv.length;++i)
		{
			if(this.inv[i] != null)
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
	
	public void transferItem(int slot0, int slot1, boolean nbt)
	{
		if(inv[slot0] != null)
		{
			if(inv[slot1] == null)
			{
				inv[slot1] = inv[slot0].copy();
				inv[slot0] = null;
			}
			else if(inv[slot1] != null && inv[slot1].isItemEqual(inv[slot0]) && (!nbt || (nbt && ItemStack.areItemStackTagsEqual(inv[slot0], inv[slot1]))))
			{
				if(inv[slot1].stackSize + inv[slot0].stackSize <= inv[slot1].getMaxStackSize())
				{
					inv[slot1].stackSize += inv[slot0].stackSize;
					inv[slot0] = null;
				}
				else
				{
					int stacksize = (inv[slot1].stackSize + inv[slot0].stackSize) - inv[slot1].getMaxStackSize();
					inv[slot1].stackSize = inv[slot1].getMaxStackSize();
					inv[slot0].stackSize = stacksize;
					if(inv[slot0].stackSize <= 0)
					{
						inv[slot0] = null;
					}
				}
			}
		}
	}
	
	@Override
	public void onBreaking()
	{
		if(!worldObj.isRemote)
		{
			InventoryUtil.dropInventory(worldObj, xCoord, yCoord, zCoord, this);
			if(this.isValid())
			{
				ItemStack item = new ItemStack(APIItems.mobMachineHelper, 1, type);
				InventoryUtil.dropItem(this, item);
			}
		}
	}
	
}
