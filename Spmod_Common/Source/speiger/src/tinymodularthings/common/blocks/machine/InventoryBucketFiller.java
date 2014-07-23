package speiger.src.tinymodularthings.common.blocks.machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class InventoryBucketFiller extends AdvContainer
{
	AdvTile tile;
	public InventoryBucketFiller(InventoryPlayer par1, BucketFillerBasic par2)
	{
		tile = par2;
		this.addSlotToContainer(new Slot(par2, 0, 52, 41));
		this.addSlotToContainer(new Slot(par2, 1, 104, 41));
        int i;

        for (i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(par1, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(par1, i, 8 + i * 18, 142));
        }
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return true;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
	{
		return null;
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		for (int i = 0; i < crafters.size(); ++i)
		{
			ICrafting icrafting = (ICrafting) crafters.get(i);
			tile.onSendingGuiInfo(this, icrafting);
		}
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2)
	{
		tile.onReciveGuiInfo(par1, par2);
	}
	
	
	
}
