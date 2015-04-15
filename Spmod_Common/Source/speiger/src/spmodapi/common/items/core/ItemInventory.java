package speiger.src.spmodapi.common.items.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Icon;
import speiger.src.api.common.data.nbt.NBTHelper;
import speiger.src.api.common.data.packets.SpmodPacketHelper;
import speiger.src.api.common.data.packets.SpmodPacketHelper.ModularPacket;
import speiger.src.api.common.registry.helpers.SpmodMod;
import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.tile.AdvInventory;

public abstract class ItemInventory extends AdvInventory
{
	int itemID;
	public EntityPlayer player;
	public String id;
	
	public ItemInventory(EntityPlayer player, ItemStack provider, int inventorySize)
	{
		super(inventorySize);
		NBTTagCompound data = NBTHelper.getTag(provider, "Data");
		this.player = player;
		id = data.getString("ID");
		itemID = provider.itemID;
		readInventory(data.getCompoundTag("Inventory"));
		readFromNBT(data);
	}
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		return null;
	}
	
	
	
	@Override
	public abstract String getInvName();

	@Override
	public void readFromNBT(NBTTagCompound par1)
	{
		
	}

	@Override
	public void writeToNBT(NBTTagCompound par1)
	{
		
	}
	
	public void readInventory(NBTTagCompound par1)
	{
		inv = new ItemStack[getSizeInventory()];
		NBTTagList list = par1.getTagList("Inventory");
		for(int i = 0;i<list.tagCount();i++)
		{
			NBTTagCompound data = (NBTTagCompound)list.tagAt(i);
			byte slotID = data.getByte("SlotID");
			if(slotID >= 0 && slotID < getSizeInventory())
			{
				inv[slotID] = ItemStack.loadItemStackFromNBT(data);
			}
		}
	}
	
	public void writeInventory(NBTTagCompound par1)
	{
		NBTTagList list = new NBTTagList();
		for(int i = 0;i<getSizeInventory();i++)
		{
			ItemStack stack = inv[i];
			if(stack != null)
			{
				NBTTagCompound data = new NBTTagCompound();
				data.setByte("SlotID", (byte)i);
				stack.writeToNBT(data);
				list.appendTag(data);
			}
		}
		par1.setTag("Inventory", list);
	}

	@Override
	public void closeChest()
	{
		this.onInventoryChanged();
	}

	@Override
	public void onPlayerCloseContainer(EntityPlayer par1)
	{
		this.closeChest();
		super.onPlayerCloseContainer(par1);
		
	}
	
	public boolean stopTickingOnGuiOpen()
	{
		return false;
	}
	
	public void onItemTick()
	{
		
	}
	
	public ModularPacket createItemPacket(SpmodMod par1)
	{
		return SpmodPacketHelper.getHelper().createPlayerTilePacket(player, par1);
	}

	@Override
	public void setupUser(EntityPlayer player)
	{
		this.player = player;
	}

	@Override
	public void onTick()
	{
		if(!SpmodConfig.booleanInfos.get("LoadTileEntities"))
		{
			return;
		}
		onItemTick();
	}

	@Override
	public void onInventoryChanged()
	{
		ItemStack inventory = getInventory();
		if(inventory != null && !player.worldObj.isRemote)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			writeInventory(nbt);
			NBTTagCompound data = NBTHelper.getTag(inventory, "Data");
			writeToNBT(data);
			data.setCompoundTag("Inventory", nbt);
		}
	}
	
	public ItemStack getInventory()
	{
		InventoryPlayer inv = player.inventory;
		for(int i = 0;i<inv.getSizeInventory();i++)
		{
			ItemStack cu = inv.getStackInSlot(i);
			if(cu != null && cu.itemID == itemID)
			{
				String id = NBTHelper.getTag(cu, "Data").getString("ID");
				if(id.equalsIgnoreCase(this.id))
				{
					return cu;
				}
			}
		}
		if(inv.getCurrentItem() != null && inv.getCurrentItem().itemID == itemID)
		{
			String may = NBTHelper.getTag(inv.getCurrentItem(), "Data").getString("ID");
			if(may.equalsIgnoreCase(id))
			{
				return inv.getCurrentItem();
			}
		}
		return null;
	
	}
	
	@Override
	public ItemStack getItemDrop()
	{
		return null;
	}
	
}
