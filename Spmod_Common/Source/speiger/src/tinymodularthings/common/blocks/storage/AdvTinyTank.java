package speiger.src.tinymodularthings.common.blocks.storage;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;

public class AdvTinyTank extends TinyTank
{
	public boolean full = false;
	public boolean isEmpty = false;
	
	@Override
	public boolean canConnectToWire(int side)
	{
		return true;
	}
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		return TextureEngine.getTextures().getTexture(getBlockType(), 1);
	}
	
	
	
	@Override
	public void onTick()
	{
		super.onTick();
		if(!worldObj.isRemote && worldObj.getWorldTime() % 10 == 0)
		{
			boolean before = isEmpty;
			isEmpty = this.isTankEmpty();
			if(before != isEmpty)
			{
				this.updateNeighbors(true);
			}
			before = full;
			full = this.isTankFull();
			if(before != full)
			{
				this.updateNeighbors(true);
			}
		}
	}

	@Override
	public ArrayList<ItemStack> getItemDrops(int fortune)
	{
		ArrayList<ItemStack> stack = new ArrayList<ItemStack>();
		if (droped)
		{
			return stack;
		}
		droped = true;
		ItemStack item = new ItemStack(TinyItems.advTinyTank, 1, tankMode);
		if (tank.getFluid() != null)
		{
			if (wrench)
			{
				NBTTagCompound data = new NBTTagCompound();
				data.setInteger("FluidID", tank.getFluid().fluidID);
				data.setInteger("Amount", tank.getFluid().amount);
				if (tank.getFluid().tag != null)
				{
					data.setCompoundTag("Data", tank.getFluid().tag);
				}
				item.setTagInfo("Fluid", data);
			}
			else
			{
				boolean all = this.worldObj.rand.nextBoolean();
				double per = (double) this.worldObj.rand.nextInt(101);
				int fluidAmount = this.getProzentInMilliBuckets(per, this);
				if (fluidAmount > this.tank.getFluidAmount())
				{
					fluidAmount = this.tank.getFluidAmount();
				}
				if (fluidAmount <= 0 || !all)
				{
					stack.add(item);
					return stack;
				}
				NBTTagCompound data = new NBTTagCompound();
				data.setInteger("FluidID", tank.getFluid().fluidID);
				data.setInteger("Amount", fluidAmount);
				if (tank.getFluid().tag != null)
				{
					data.setCompoundTag("Data", tank.getFluid().tag);
				}
				item.setTagInfo("Fluid", data);
			}
		}
		stack.add(item);
		return stack;
	}
	
	@Override
	public int isPowering(int side)
	{
		if (side == 0)
		{
			return isTankFull() ? 15 : 0;
		}
		else if (side == 1)
		{
			return isTankEmpty() ? 15 : 0;
		}
		return 0;
	}
	
	public boolean isTankFull()
	{
		int amount = tank.getCapacity() - (tank.getCapacity() / 100);
		if (tank.getFluidAmount() >= amount)
		{
			return true;
		}
		return false;
	}
	
	@Override
	public ItemStack pickBlock(MovingObjectPosition target)
	{
		return new ItemStack(TinyItems.advTinyTank, 1, tankMode);
	}
	
	@Override
	public boolean shouldCheckWeakPower(int side)
	{
		return true;
	}
	
	public boolean isTankEmpty()
	{
		if (tank.getFluidAmount() == 0)
		{
			return true;
		}
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.isEmpty = nbt.getBoolean("IsEmpty");
		this.full = nbt.getBoolean("IsFull");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setBoolean("IsEmpty", isEmpty);
		nbt.setBoolean("IsFull", full);
	}
	
	
	
}
