package speiger.src.spmodapi.common.util.slot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cpw.mods.fml.common.Loader;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import speiger.src.spmodapi.common.tile.AdvTile;

public class SpmodSlot extends Slot
{
	ArrayList<String> usage = new ArrayList<String>();
	AdvTile tile;
	int maxStackSize = 64;
	boolean blocked = false;
	boolean onlyPick = false;
	
	public SpmodSlot setAdvTile(AdvTile par1)
	{
		tile = par1;
		return this;
	}
	
	public SpmodSlot setOnlyPicking()
	{
		onlyPick = true;
		return this;
	}
	
	public SpmodSlot setMaxStackSize(int amount)
	{
		maxStackSize = amount;
		return this;
	}
	
	public SpmodSlot(IInventory par1, int id, int x, int y)
	{
		super(par1, id, x, y);
	}
	
	public SpmodSlot(AdvTile tile, int id, int x, int y)
	{
		this(tile instanceof IInventory ? (IInventory)tile : null, id, x, y);
		this.setAdvTile(tile);
	}
	
	public SpmodSlot blockSlot()
	{
		blocked = true;
		return this;
	}
	
	public void addUsage(String...par1)
	{
		usage.addAll(Arrays.asList(par1));
	}
	
	public void setUsage(String...par1)
	{
		usage.clear();
		usage.addAll(Arrays.asList(par1));
	}
	
	public List<String> getUsage()
	{
		return usage;
	}
	
	public boolean hasUsage()
	{
		return !usage.isEmpty();
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack)
	{
		if(blocked || onlyPick)
		{
			return false;
		}
		if(tile != null)
		{
			return tile.canMergeItem(par1ItemStack, getSlotIndex());
		}
		return super.isItemValid(par1ItemStack);
	}
	
	
	
	@Override
	public boolean canTakeStack(EntityPlayer par1EntityPlayer)
	{
		if(blocked)
		{
			return false;
		}
		return super.canTakeStack(par1EntityPlayer);
	}

	public AdvTile getTile()
	{
		return tile;
	}

	@Override
	public int getSlotStackLimit()
	{
		return maxStackSize;
	}
	
	
}
