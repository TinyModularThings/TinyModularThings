package speiger.src.tinymodularthings.common.blocks.crafting;

import java.io.DataInput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import speiger.src.api.common.data.packets.IPacketReciver;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.common.tile.FacedInventory;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.proxy.PathProxy;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.config.TinyConfig;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class OreCrafter extends FacedInventory implements IPacketReciver, ISidedInventory
{
	public OreCrafter()
	{
		super(48);
	}

	ArrayList<ItemStack> currentItems = new ArrayList<ItemStack>();
	boolean packetRequired = false;
	
	@Override
	public boolean isSixSidedFacing()
	{
		return false;
	}
	
	@Override
	public ItemStack getItemDrop()
	{
		return new ItemStack(TinyBlocks.craftingBlock, 1, 1);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onItemInformation(EntityPlayer par1, List par2, ItemStack par3)
	{
		super.onItemInformation(par1, par2, par3);
		par2.add("Item exchanger that uses the Oredictionary");
	}
	
	@Override
	public void registerIcon(TextureEngine par1, Block par2)
	{
		par1.registerTexture(new BlockStack(par2, 1), "craftingTableBottom", "OreCrafter_top", "OreCrafter_side", "OreCrafter_side2");
	}
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		TextureEngine engine = TextureEngine.getTextures();
		if(side >= 2)
		{
			int data = facing == 2 || facing == 3 ? side == 5 || side == 4 ? 2 : 3 : side == 5 || side == 4 ? 3 : 2;
			return engine.getTexture(TinyBlocks.craftingBlock, 1, data);
		}
		else
		{
			return engine.getTexture(TinyBlocks.craftingBlock, 1, side);
		}
	}
	
	@Override
	public float getBlockHardness()
	{
		return 3F;
	}

	@Override
	public ArrayList<ItemStack> getItemDrops(int fortune)
	{
		ArrayList<ItemStack> stack = super.getItemDrops(fortune);
		for(int i = 10;i<inv.length;i++)
		{
			ItemStack data = getStackInSlot(i);
			if(data != null)
			{
				stack.add(data);
			}
		}
		return stack;
	}
	
	@Override
	public float getExplosionResistance(Entity par1)
	{
		return 5F;
	}

	public void oreUpdate()
	{
		if(inv[13] == null)
		{
			if(!currentItems.isEmpty())
			{
				currentItems.clear();
			}
		}
		else
		{
			if(currentItems.isEmpty())
			{
				updateOre();
			}
			else
			{
				if(OreDictionary.getOreID(inv[13]) != OreDictionary.getOreID(currentItems.get(0)))
				{
					currentItems.clear();
					updateOre();
				}
			}
		}
	}
	
	public boolean addItemsToInventory(ItemStack par1)
	{
		if(par1 != null)
		{
			if(inv[14] == null)
			{
				inv[14] = par1.copy();
				return true;
			}
			else if(inv[14] != null && inv[14].isItemEqual(par1) && inv[14].stackSize + par1.stackSize <= par1.getMaxStackSize())
			{
				inv[14].stackSize += par1.stackSize;
				return true;
			}
			else if(inv[15] == null)
			{
				inv[15] = par1.copy();
				return true;				
			}
			else if(inv[15] != null && inv[15].isItemEqual(par1) && inv[15].stackSize + par1.stackSize <= par1.getMaxStackSize())
			{
				inv[15].stackSize += par1.stackSize;
				return true;				
			}
			else if(inv[16] == null)
			{
				inv[16] = par1.copy();
				return true;
			}
			else if(inv[16] != null && inv[16].isItemEqual(par1) && inv[16].stackSize + par1.stackSize <= par1.getMaxStackSize())
			{
				inv[16].stackSize += par1.stackSize;
				return true;
			}
		}
		return false;
	}

	public void updateOre()
	{
		int id = OreDictionary.getOreID(inv[13]);
		
		if(id != -1 && TinyConfig.allowedOres.isAllowed(OreDictionary.getOreName(id)))
		{
			if(worldObj.getWorldTime() % 20 == 0)
			{
				FMLLog.getLogger().info("CurrentItems: ");
			}
			ArrayList<ItemStack> stacks = (ArrayList<ItemStack>)OreDictionary.getOres(id).clone();
			for(ItemStack stack : stacks)
			{
				if(stack.getItemDamage() == PathProxy.getRecipeBlankValue())
				{
					ArrayList<ItemStack> list = new ArrayList<ItemStack>();
					stack.getItem().getSubItems(stack.itemID, null, list);
					currentItems.addAll(list);
				}
				else
				{
					currentItems.add(stack);
				}
			}
			for(int i = 0;i<currentItems.size();i++)
			{
				ItemStack stack = currentItems.get(i);
				if(stack.isItemEqual(inv[13]))
				{
					currentItems.remove(i);
				}
			}
		}

	}
	
	public void handleOpenInventory()
	{
		if(worldObj.getWorldTime() % 30 == 0)
		{
			if(!currentItems.isEmpty())
			{
				if(currentItems.size() > 9)
				{
					currentItems.add(currentItems.remove(0));
				}
			}
		}
		for(int i = 0;i<10;i++)
		{
			if(i < currentItems.size())
			{
				inv[i] = currentItems.get(i);
			}
			else
			{
				inv[i] = null;
			}
		}
	}
	
	public void handleIncomingItems()
	{
		for(int i = 0;i<3;i++)
		{
			if(inv[10+i] != null)
			{
				ItemStack copy = inv[10+i].copy();
				copy = transformItem(copy);
				if(this.addItemsToInventory(copy))
				{
					inv[10+i] = null;
					this.onInventoryChanged();
				}
			}
		}
	}
	
	public ItemStack transformItem(ItemStack copy)
	{
		HashMap<Integer, Integer> ints = new HashMap<Integer, Integer>();
		for(int i = 17;i<this.inv.length;i++)
		{
			ItemStack stack = this.getStackInSlot(i);
			if(stack != null)
			{
				ints.put(OreDictionary.getOreID(stack), i);
			}
		}
		if(ints.get(OreDictionary.getOreID(copy)) != null)
		{
			int id = OreDictionary.getOreID(copy);
			if(TinyConfig.allowedOres.isAllowed(OreDictionary.getOreName(id)))
			{
				int result = ints.get(id);
				ItemStack itemResult = inv[result].copy();
				itemResult.stackSize = copy.stackSize;
				return itemResult;
			}
		}
		
		return copy;
	}

	@Override
	public void onTick()
	{
		if(!worldObj.isRemote)
		{
			oreUpdate();
			handleOpenInventory();
			handleIncomingItems();
			if(packetRequired)
			{
				packetRequired = false;
				PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, getDescriptionPacket());
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
		}
	}

	@Override
	public void recivePacket(DataInput par1)
	{
		try
		{
			int slot = par1.readInt();
			this.slotClick(slot);
		}
		catch(Exception e)
		{
		}
	}
	
	public void slotClick(int slot)
	{
		if(inv[13] != null && inv[slot] != null)
		{
			ItemStack stack = inv[slot].copy();
			stack.stackSize = inv[13].stackSize;
			if(this.addItemsToInventory(stack))
			{
				inv[13] = null;
				this.onInventoryChanged();
			}
		}
	}
	
	@Override
	public String identifier()
	{
		return null;
	}
	
	@Override
	public String getInvName()
	{
		return "Ore Crafter";
	}
	
	@Override
	public boolean hasContainer()
	{
		return true;
	}

	
	@Override
	public void addContainerSlots(AdvContainer par1)
	{	
		par1.setOffset(14, 59);
		int slot = 0;
		for(int i = 0;i<10;i++)
		{
			par1.addSpmodSlot(this, slot++, 10 + i * 18, 20).setMaxStackSize(1).addUsage("Choosing Slot", "Click on the Slot to the Item you want to exchange");
		}
		for(int i = 0;i<3;i++)
		{
			par1.addSpmodSlot(this, slot++, 20 + i * 18, 48);
		}
		par1.addSpmodSlot(this, slot++, 93, 48).setMaxStackSize(1).addUsage("Ore Slot", "Main Slot where you can put a item in to exchange it manually");
		for(int i = 0;i<3;i++)
		{
			par1.addSpmodSlot(this, slot++, 130 + i * 18, 48);
		}
		for(int z = 0;z<3;z++)
		{
			for(int i = 0;i<10;i++)
			{
				par1.addSpmodSlot(this, slot++, 12 + i * 18, 75 + z * 18).setMaxStackSize(1).addUsage("Auto Exchanging Slot", "Put Item as filter in");
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ResourceLocation getTexture()
	{
		return getEngine().getTexture("BigFrame");
	}

	@Override
	public boolean canMergeItem(ItemStack par1, int slotID)
	{
		if(slotID == 13 || slotID > 16)
		{
			return OreDictionary.getOreID(par1) != -1;
		}
		if(slotID < 10)
		{
			return false;
		}
		if(slotID > 9 && slotID <= 12 || slotID > 13 && slotID <= 16)
		{
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onSlotClicked(AdvContainer par1, int slotID, int mouseButton, int modifier, EntityPlayer player)
	{
		if(slotID >= 0 && slotID < 10)
		{
			this.slotClick(slotID);
			sendPacketToServer(createBasicPacket(TinyModularThings.instance).InjectNumber(slotID).finishPacket());
			return true;
		}
		return super.onSlotClicked(par1, slotID, mouseButton, modifier, player);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onGuiConstructed(GuiInventoryCore par1)
	{
		par1.setX(210);
		par1.setY(225);
	}
	
	@Override
	public int[] getAccessibleSlotsFromSide(int var1)
	{
		return new int[]{10, 11, 12, 14, 15, 16};
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j)
	{
		if(i >= 10 && i <=12)
		{
			return true;
		}
		return false;
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j)
	{
		if(i >= 14 && i <= 16)
		{
			return true;
		}
		return false;
	}
	
}
