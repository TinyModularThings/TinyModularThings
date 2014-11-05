package speiger.src.tinymodularthings.common.upgrades.hoppers.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.common.utils.WorldReading;
import speiger.src.api.common.world.tiles.interfaces.HopperUpgrade;
import speiger.src.api.common.world.tiles.interfaces.IHopper;
import speiger.src.spmodapi.common.lib.bc.ITransactor;
import speiger.src.spmodapi.common.lib.bc.SpmodStackFilter;
import speiger.src.spmodapi.common.lib.bc.Transactor;
import speiger.src.tinymodularthings.common.utils.HopperType;

public class ImportItems implements HopperUpgrade
{
	
	@Override
	public void onTick(IHopper par1)
	{
		World world = par1.getWorld();
		if (world.isRemote)
		{
			return;
		}
		int x = par1.getXPos();
		int y = par1.getYPos();
		int z = par1.getZPos();
		ForgeDirection dir = ForgeDirection.getOrientation(par1.getFacing());
		TileEntity tile = WorldReading.getTileEntity(world, x, y, z, dir.ordinal());
		if (tile != null && tile instanceof IInventory)
		{
			getItems(par1, (IInventory) tile, dir, par1.getTransferlimit(HopperType.Items));
		}
		
	}
	
	public static void getItems(IHopper par1, IInventory par2, ForgeDirection par3, int transferlimit)
	{
		ITransactor trans = Transactor.getTransactorFor(par2);
		if (trans == null)
		{
			return;
		}
		
		ItemStack stack = trans.remove(new SpmodStackFilter(null), par1.getTransferlimit(HopperType.Items), par3, false);
		if (stack != null && stack.stackSize > 0)
		{
			ItemStack added = par1.addItemsToHopper(stack);
			if (added != null && added.stackSize > 0)
			{
				trans.remove(new SpmodStackFilter(null), added.stackSize, par3, true);
			}
		}
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
		return "Basic Item Import";
	}
	
	@Override
	public String getNBTName()
	{
		return "item.import.basic";
	}
	
	@Override
	public void getInformationList(EntityPlayer player, List par2)
	{
		par2.add("Import Items from Inventories");
		par2.add("Only 1 time useable");
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
		return false;
	}
	
	@Override
	public HopperType getUpgradeType()
	{
		return HopperType.Items;
	}
	
}
