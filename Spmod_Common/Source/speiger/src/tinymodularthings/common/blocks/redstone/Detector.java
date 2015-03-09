package speiger.src.tinymodularthings.common.blocks.redstone;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.common.world.blocks.BlockPosition;
import speiger.src.api.common.world.items.IDetectorModulItem;
import speiger.src.api.common.world.tiles.machine.IDetector;
import speiger.src.api.common.world.tiles.machine.IDetectorModul;
import speiger.src.spmodapi.common.tile.FacedInventory;
import speiger.src.spmodapi.common.util.slot.AdvContainer;

public class Detector extends FacedInventory implements IDetector
{
	IDetectorModul modul;
	int tickRate = 0;
	boolean ticking = false;
	
	public Detector()
	{
		super(1);
	}
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		return null;
	}

	@Override
	public boolean isSixSidedFacing()
	{
		return true;
	}

	@Override
	public void onInventoryChanged()
	{
		super.onInventoryChanged();
		ItemStack stack = this.getStackInSlot(0);
		if(stack != null)
		{
			if(stack.getItem() instanceof IDetectorModulItem)
			{
				if(modul == null)
				{
					setModul(((IDetectorModulItem)stack.getItem()).createDetectorModul(stack, this));
				}
				else
				{
					IDetectorModul data = ((IDetectorModulItem)stack.getItem()).createDetectorModul(stack, this);
					if(modul != data)
					{
						setModul(data);
					}
				}
			}
		}
		else if(modul != null)
		{
			modul.onUnloading(this);
			setModul(null);
		}
	}
	
	public void setModul(IDetectorModul par1)
	{
		modul = par1;
		if(modul != null)
		{
			tickRate = modul.getTickRate(this);
			ticking = modul.doesHaveTileEntityTick(this);
		}
		else
		{
			this.setRedstoneSignal(0);
		}
	}

	@Override
	public int getTickRate()
	{
		return tickRate;
	}

	@Override
	public void requestTickRateUpdate()
	{
		if(modul != null)
		{
			tickRate = modul.getTickRate(this);
			ticking = modul.doesHaveTileEntityTick(this);
		}
	}

	@Override
	public BlockPosition getBlockInfront()
	{
		return getPosition().add(ForgeDirection.getOrientation(getFacing()));
	}

	@Override
	public List<Entity> getEntitiesInfront(Class par1)
	{
		BlockPosition pos = this.getPosition().add(ForgeDirection.getOrientation(getFacing()));
		if(par1 == null)
		{
			return worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getAABBPool().getAABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX()+1, pos.getY(), pos.getZ()+1));
		}
		return worldObj.getEntitiesWithinAABB(par1, AxisAlignedBB.getAABBPool().getAABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX()+1, pos.getY(), pos.getZ()+1));
	}

	@Override
	public void setRedstoneSignal(int total)
	{
		super.setRedstoneSignal(total);
	}

	@Override
	public void setRedstoneSignal(int side, int total)
	{
		super.setRedstoneSignal(side, total);
	}

	@Override
	public void onBlockChange(Block par1, int par2)
	{
		super.onBlockChange(par1, par2);
		if(modul != null)
		{
			modul.onBlockUpdate(this);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound par1)
	{
		super.readFromNBT(par1);
		if(modul == null)
		{
			ItemStack stack = this.getStackInSlot(0);
			if(stack != null && stack.getItem() instanceof IDetectorModulItem)
			{
				setModul(((IDetectorModulItem)stack.getItem()).createDetectorModul(stack, this));
			}
		}
		
		if(modul != null)
		{
			if(par1.hasKey("ModulData"))
			{
				NBTTagCompound data = par1.getCompoundTag("ModulData");
				modul.readFromNBT(data);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound par1)
	{
		super.writeToNBT(par1);
		if(modul != null)
		{
			NBTTagCompound modulData = new NBTTagCompound();
			modul.writeToNBT(modulData);
			par1.setCompoundTag("ModulData", modulData);
		}
	}

	@Override
	public void onTick()
	{
		super.onTick();
		if(worldObj.isRemote || modul == null)
		{
			return;
		}

		if(!this.ticking || tickRate < 0)
		{
			return;
		}
		boolean flag = true;
		if(tickRate == 0)
		{
			flag = false;
		}
		if(flag && getClockTime() % tickRate != 0)
		{
			return;
		}
		modul.onTileEntityTick(this);
	}

	@Override
	public boolean hasContainer()
	{
		return true;
	}

	@Override
	public void addContainerSlots(AdvContainer par1)
	{
		par1.addSpmodSlot(this, 0, 75, 35);
	}

	@Override
	public boolean canMergeItem(ItemStack par1, int slotID)
	{
		return par1 != null && par1.getItem() instanceof IDetectorModulItem;
	}

	@Override
	public boolean shouldCheckWeakPower(int side)
	{
		return true;
	}
	
	
}
