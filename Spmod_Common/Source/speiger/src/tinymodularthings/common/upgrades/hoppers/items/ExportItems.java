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
import speiger.src.api.common.registry.hopper.HopperRegistry.HopperEffect;
import speiger.src.api.common.world.tiles.interfaces.HopperUpgrade;
import speiger.src.api.common.world.tiles.interfaces.IHopper;
import speiger.src.spmodapi.common.lib.bc.ITransactor;
import speiger.src.spmodapi.common.lib.bc.Transactor;
import speiger.src.tinymodularthings.common.utils.HopperType;

public class ExportItems implements HopperUpgrade
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
		ForgeDirection head = ForgeDirection.getOrientation(par1.getRotation());
		TileEntity tile = world.getBlockTileEntity(x + head.offsetX, y + head.offsetY, z + head.offsetZ);
		if (tile != null && tile instanceof IInventory)
		{
			IInventory hopper = par1.getInventory();
			for (int slot = 0; slot < hopper.getSizeInventory(); slot++)
			{
				if (sendItems(par1, (IInventory) tile, head.getOpposite(), par1.removeItemsFromHopper(slot, par1.getTransferlimit(HopperType.Items))))
				{
					if (par1.hasEffectApplied(HopperEffect.AllSlots))
					{
						continue;
					}
					return;
				}
			}
			
		}
		
	}
	
	public static boolean sendItems(IHopper par0, IInventory par1, ForgeDirection par2, ItemStack stack)
	{
		if (stack == null)
		{
			return false;
		}
		
		ITransactor trans = Transactor.getTransactorFor(par1);
		if (trans == null)
		{
			par0.addItemsToHopper(stack);
			return false;
		}
		ItemStack copy = stack.copy();
		ItemStack added = trans.add(stack, par2, true);
		
		if (added != null)
		{
			copy.stackSize -= added.stackSize;
			if (copy.stackSize > 0)
			{
				par0.addItemsToHopper(copy);
			}
			return true;
		}
		else
		{
			par0.addItemsToHopper(copy);
		}
		return false;
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
		return "Basic Export Items";
	}
	
	@Override
	public String getNBTName()
	{
		return "item.export.basic";
	}
	
	@Override
	public void getInformationList(EntityPlayer player, List par2)
	{
		par2.add("Transfer Items");
		par2.add("Only 1 Time useable");
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
