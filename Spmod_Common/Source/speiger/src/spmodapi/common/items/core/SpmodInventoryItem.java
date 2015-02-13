package speiger.src.spmodapi.common.items.core;

import java.util.HashMap;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import speiger.src.tinymodularthings.common.items.tools.PotionInventory;

public abstract class SpmodInventoryItem extends SpmodItem implements IItemGui
{
	static HashMap<String, EntityCounter> tickCounters = new HashMap<String, EntityCounter>();
	
	public SpmodInventoryItem(int par1)
	{
		super(par1);
	}
	
	
	public abstract ItemInventory createNewInventory(EntityPlayer par1, ItemStack par2);
	
	public String getInventoryID(ItemStack par1)
	{
		return NBTHelper.getTag(par1, "Data").getString("ID");
	}
	
	public abstract String createNewInventoryID();
	
	public void initExtraData(NBTTagCompound data)
	{
		
	}
	
	public ItemStack createItem(int meta)
	{
		ItemStack itemStack = new ItemStack(this.itemID, 1, meta);
		NBTTagCompound data = new NBTTagCompound();
		initExtraData(data);
		data.setString("ID", createNewInventoryID());
		itemStack.setTagInfo("Data", data);
		return itemStack;
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

	public boolean tickInventory(ItemStack par1)
	{
		return false;
	}
	
	public boolean tickArmor(ItemStack par1)
	{
		return false;
	}
	
	public boolean hasTickRate(ItemStack par1)
	{
		return false;
	}
	
	public int getTickRate(ItemStack par1)
	{
		return 0;
	}
	
	boolean tickRateReady(ItemStack par1)
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
		if(counter.getCurrentID() >= getTickRate(par1))
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
		if(par2.isRemote || !tickInventory(par1) || (hasTickRate(par1) && !tickRateReady(par1)))
		{
			return;
		}
		
		if(par3 != null && par3 instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)par3;
			ItemInventory inv = createNewInventory(player, par1);
			if(inv.stopTickingOnGuiOpen() && player.openContainer != null && player.openContainer instanceof AdvContainer && ((AdvContainer)player.openContainer).getInvName().equals(inv.getInvName()))
			{
				return;
			}
			inv.onTick();
			inv.onInventoryChanged();
		}
	}

	@Override
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack)
	{
		if(world.isRemote || !tickArmor(itemStack) || (hasTickRate(itemStack) && !tickRateReady(itemStack)))
		{
			return;
		}
		ItemInventory inv = createNewInventory(player, itemStack);
		if(inv.stopTickingOnGuiOpen() && player.openContainer != null && player.openContainer instanceof AdvContainer && ((AdvContainer)player.openContainer).getInvName().equals(inv.getInvName()))
		{
			return;
		}
		FMLLog.getLogger().info("Test");
		inv.onTick();
		inv.onInventoryChanged();
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
		return getTexture(stack);
	}
	
	public abstract Icon getTexture(ItemStack stack);
	
	public NBTTagCompound getItemData(ItemStack par1)
	{
		return NBTHelper.getTag(par1, "Data");
	}
	
}
