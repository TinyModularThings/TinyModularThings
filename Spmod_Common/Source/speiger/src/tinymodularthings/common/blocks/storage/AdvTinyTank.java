package speiger.src.tinymodularthings.common.blocks.storage;

import java.util.ArrayList;

import cpw.mods.fml.common.FMLLog;

import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class AdvTinyTank extends TinyTank
{

	@Override
	public boolean canConnectToWire()
	{
		return true;
	}

	@Override
	public ArrayList<ItemStack> onDrop(int fortune)
	{
		ArrayList<ItemStack> stack = new ArrayList<ItemStack>();
		if(droped)
		{
			return stack;
		}
		droped = true;
		ItemStack item = new ItemStack(TinyItems.tinyTank, 1, tankMode);
		if(tank.getFluid() != null)
		{
			if(wrench)
			{
				NBTTagCompound data = new NBTTagCompound();
				data.setInteger("FluidID", tank.getFluid().fluidID);
				data.setInteger("Amount", tank.getFluid().amount);
				if(tank.getFluid().tag != null)
				{
					data.setCompoundTag("Data", tank.getFluid().tag);
				}
				item.setTagInfo("Fluid", data);
			}
			else
			{
				boolean all = this.worldObj.rand.nextBoolean();
				double per = (double)this.worldObj.rand.nextInt(101);
				int fluidAmount = this.getProzentInMilliBuckets(per, this);
				if(fluidAmount > this.tank.getFluidAmount())
				{
					fluidAmount = this.tank.getFluidAmount();
				}
				if(fluidAmount <= 0 || !all)
				{
					stack.add(item);
					return stack;
				}
				NBTTagCompound data = new NBTTagCompound();
				data.setInteger("FluidID", tank.getFluid().fluidID);
				data.setInteger("Amount", fluidAmount);
				if(tank.getFluid().tag != null)
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
		if(side == 0)
		{
			return isTankFull();
		}
		else if(side == 1)
		{
			return isTankEmpty();
		}
		return 0;
	}
	
	public int isTankFull()
	{
		int amount = tank.getCapacity() - (tank.getCapacity() / 100);
		if(tank.getFluidAmount() >= amount)
		{
			return 15;
		}
		return 0;
	}
	
	
	
	@Override
	public boolean shouldCheckWeakPower()
	{
		return true;
	}

	public int isTankEmpty()
	{
		if(tank.getFluidAmount() == 0)
		{
			return 15;
		}
		return 0;
	}

	@Override
	public void registerIcon(IconRegister par1)
	{
		texture = par1.registerIcon(TinyModularThingsLib.ModID.toLowerCase()+":storage/AdvTinyTank");
	}
	
	
	
}
