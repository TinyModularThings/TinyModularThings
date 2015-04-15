package speiger.src.spmodapi.common.items.core;

import java.util.HashMap;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import speiger.src.api.client.gui.IItemGui;
import speiger.src.api.common.data.nbt.NBTHelper;
import speiger.src.api.common.utils.config.EntityCounter;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.common.enums.EnumGuiIDs;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class SpmodInventoryItem extends SpmodItem implements IItemGui
{
	protected static HashMap<String, EntityCounter> tickCounters = new HashMap<String, EntityCounter>();
	
	public SpmodInventoryItem(int par1)
	{
		super(par1);
	}
	
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
//		if(par4)
//		{
//			par3List.add("InventoryID: "+getInventoryID(par1ItemStack));
//		}
	}



	public abstract ItemInventory createNewInventory(EntityPlayer par1, ItemStack par2);
	
	public String getInventoryID(ItemStack par1)
	{
		if(!getItemData(par1).hasKey("Inited"))
		{
			initData(par1);
			getItemData(par1).setBoolean("Inited", true);
		}
		
		return NBTHelper.getTag(par1, "Data").getString("ID");
	}
	
	public abstract String createNewInventoryID(int meta);
	
	public void initExtraData(NBTTagCompound data)
	{
		
	}

	protected void initData(ItemStack par1)
	{
		NBTTagCompound data = new NBTTagCompound();
		data.setInteger("ItemMetaData", par1.getItemDamage());
		initExtraData(data);
		data.setString("ID", createNewInventoryID(par1.getItemDamage()));
		par1.setTagInfo("Data", data);
	}
	
	@Override
	public boolean hasGui(ItemStack par1)
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer getGui(InventoryPlayer par1, ItemStack par2)
	{
		return new GuiInventoryCore(getContainer(par1, par2)).setAutoDrawing();
	}

	@Override
	public boolean hasContainer(ItemStack par1)
	{
		return true;
	}

	@Override
	public AdvContainer getContainer(InventoryPlayer par1, ItemStack par2)
	{
		return new AdvContainer(par1, this.createNewInventory(par1.player, par2));
	}
	
	public boolean canBeArmor(ItemStack par1, int armorType)
	{
		return false;
	}
	
    public boolean isValidArmor(ItemStack stack, int armorType, Entity entity)
    {
    	if(canBeArmor(stack, armorType))
    	{
    		return true;
    	}
    	return super.isValidArmor(stack, armorType, entity);
    }
	
	@Override
	public ItemStack onItemRightClick(ItemStack par1, World par2, EntityPlayer par3)
	{
		if(!par2.isRemote)
		{
			par3.openGui(SpmodAPI.instance, EnumGuiIDs.Items.getID(), par2, 0, 0, 0);
		}
		return par1;
	}

	public boolean canTick(ItemStack par1, ItemTickType par2)
	{
		return false;
	}
	
	public boolean hasTickRate(ItemStack par1, ItemTickType par2)
	{
		return getTickRate(par1, par2) > 0;
	}
	
	public int getTickRate(ItemStack par1, ItemTickType par2)
	{
		return 0;
	}
	
	boolean tickRateReady(ItemStack par1, ItemTickType par2)
	{
		String id = getInventoryID(par1);
		EntityCounter counter = null;
		if(!tickCounters.containsKey(id))
		{
			counter = new EntityCounter(0);
			tickCounters.put(id, counter);
		}
		else
		{
			counter = tickCounters.get(id);
		}
		counter.updateToNextID();
		if(counter.getCurrentID() >= getTickRate(par1, par2))
		{
			counter.resetCounter();
			tickCounters.put(id, counter);
			return true;
		}
		tickCounters.put(id, counter);
		return false;
	}

	@Override
	public void onUpdate(ItemStack par1, World par2, Entity par3, int par4, boolean par5)
	{
		ItemTickType type = ItemTickType.Inventory;
		type.setSlot(par4);
		if(!(par3 instanceof EntityPlayer) || !canTick(par1, type) || hasTickRate(par1, type) && !tickRateReady(par1, type) || !getItemData(par1).hasKey("Inited"))
		{
			if(!getItemData(par1).hasKey("Inited"))
			{
				initData(par1);
				getItemData(par1).setBoolean("Inited", true);
			}
			return;
		}
		onTick(par1, par2, (EntityPlayer)par3, type, par5);
	}

	@Override
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack item)
	{
		ItemTickType type = ItemTickType.Inventory;
		if(!canTick(item, type) || hasTickRate(item, type) && !tickRateReady(item, type) || !getItemData(item).hasKey("Inited"))
		{
			if(!getItemData(item).hasKey("Inited"))
			{
				initData(item);
				getItemData(item).setBoolean("Inited", true);
			}
			return;
		}
		onTick(item, world, player, type, false);
	}
	
	public void onTick(ItemStack par1, World par2, EntityPlayer par3, ItemTickType par4, boolean par5)
	{
		
	}
	
	public void tickInventory(ItemStack par1, EntityPlayer par2)
	{
		ItemInventory inv = createNewInventory(par2, par1);
		if(inv != null)
		{
			if(inv.stopTickingOnGuiOpen() && par2.openContainer != null && par2.openContainer instanceof AdvContainer && ((AdvContainer)par2.openContainer).getInvName().equals(inv.getInvName()))
			{
				return;
			}
			inv.onTick();
			inv.onInventoryChanged();
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}
	
	@Override
	public int getRenderPasses(int metadata)
	{
		return 1;
	}

	@Override
	public Icon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
	{
		return getIcon(stack, renderPass);
	}

	@Override
	public Icon getIcon(ItemStack stack, int pass)
	{
		if(!getItemData(stack).hasKey("Inited"))
		{
			initData(stack);
			getItemData(stack).setBoolean("Inited", true);
		}
		
		return getEngine().getIconSafe(getTexture(stack));
	}
	
	public abstract Icon getTexture(ItemStack stack);
	
	public NBTTagCompound getItemData(ItemStack par1)
	{
		return NBTHelper.getTag(par1, "Data");
	}
	
	public static enum ItemTickType
	{
		Inventory,
		Armor;
		
		int slotID;
		
		public void setSlot(int id)
		{
			slotID = id;
		}
		
		public int getSlot()
		{
			return slotID;
		}
		
	}
	
}
