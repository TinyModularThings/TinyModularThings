package speiger.src.tinymodularthings.common.upgrades.hoppers.all;

import java.util.List;

import cpw.mods.fml.common.FMLLog;

import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import speiger.src.api.hopper.HopperUpgrade;
import speiger.src.api.hopper.IHopper;
import speiger.src.api.hopper.IHopperInventory;
import speiger.src.api.hopper.IUpgradeGuiProvider;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.client.gui.transport.TinyHopperGui;
import speiger.src.tinymodularthings.common.blocks.transport.TinyHopperInventory;
import speiger.src.tinymodularthings.common.enums.HopperUpgradeIDs;
import speiger.src.tinymodularthings.common.utils.HopperType;

public class FilterUpgrade implements HopperUpgrade, IUpgradeGuiProvider
{
	
	@Override
	public void onTick(IHopper par1)
	{
		
	}
	
	@Override
	public void onNBTWrite(NBTTagCompound nbt)
	{
		
	}
	
	@Override
	public void onNBTRead(NBTTagCompound nbt)
	{
		
	}
	
	@Override
	public String getUpgradeName()
	{
		return "Filter Upgrade";
	}
	
	@Override
	public String getNBTName()
	{
		return "upgrade.basic.filter";
	}
	
	@Override
	public void getInformationList(EntityPlayer player, List par2)
	{
		par2.add("Adds Filter to a Hopper");
		par2.add("Can be used only 1 time");
	}
	
	@Override
	public void onRegisterUpgrade(IHopper par1)
	{
		
	}
	
	@Override
	public void onRemovingUpgrade(IHopper par1)
	{
		
	}
	
	@Override
	public int getMaxStackSize()
	{
		return 1;
	}
	
	@Override
	public boolean onClick(boolean sneak, EntityPlayer player, Block block, IHopper hopper, int side)
	{
		if(!hopper.getWorld().isRemote)
		{
			ItemStack stack = player.getCurrentEquippedItem();
			if(stack == null && player.isSneaking())
			{
				player.openGui(TinyModularThings.instance, HopperUpgradeIDs.Filter.getGuiID(), hopper.getWorld(), hopper.getXPos(), hopper.getYPos(), hopper.getZPos());
				return true;
			}
		}
		return false;
	}
	
	@Override
	public HopperType getUpgradeType()
	{
		return HopperType.Nothing;
	}

	@Override
	public GuiContainer getGui(InventoryPlayer par1, IHopper par2)
	{
		return new TinyHopperGui(par1, (AdvTile) par2);
	}

	@Override
	public Container getInventory(InventoryPlayer par1, IHopper par2)
	{
		return new TinyHopperInventory(par1, (IHopperInventory) par2, true);
	}
	
}
