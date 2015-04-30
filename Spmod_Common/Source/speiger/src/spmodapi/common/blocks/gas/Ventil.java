package speiger.src.spmodapi.common.blocks.gas;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.*;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.tile.AdvancedFluidTank;
import speiger.src.spmodapi.common.tile.TileDataBuffer;
import speiger.src.spmodapi.common.util.TextureEngine;

public class Ventil extends AdvTile implements IFluidHandler
{
	FluidTank tank = new AdvancedFluidTank(this, "Gas Tank", 10000);
	
	
	@Override
	public ItemStack getItemDrop()
	{
		return new ItemStack(APIBlocks.animalUtils, 1, 1);
	}
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{		
		if(resource != null && canFill(from, resource.getFluid()))
		{
			return tank.fill(resource, doFill);
		}
		return 0;
	}
	
	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		return drain(from, resource.amount, doDrain);
	}
	
	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		return tank.drain(maxDrain, doDrain);
	}
	
	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return fluid != null && fluid.getID() == APIUtils.animalGas.getID();
	}
	
	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return true;
	}
	
	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		return new FluidTankInfo[]{tank.getInfo()};
	}
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		if(side < 2)
		{
			if(side == 1)
			{
				return getEngine().getTexture(APIBlocks.animalUtils, 1, 2);
			}
			return getEngine().getTexture(APIBlocks.animalUtils, 1, 0);
		}
		return getEngine().getTexture(APIBlocks.animalUtils, 1, 1);
	}

	@Override
	public void registerIcon(TextureEngine par1, Block par2)
	{
		par1.registerTexture(par2, 1, "Ventil_Top", "Ventil_Side", "Ventil_Top_Active");
	}

	@Override
	public void onTick()
	{
		if(worldObj.isRemote)
		{
			return;
		}
		if(getClockTime() % 10 == 0)
		{
			TileDataBuffer buffer = getBuffer(0);
			int id = buffer.getBlockID();
			if(id == APIBlocks.animalGas.blockID)
			{
				int meta = buffer.getMeta();
				int amount = tank.fill(new FluidStack(APIUtils.animalGas, (100 * meta)), false);
				if(amount == (meta * 100))
				{
					tank.fill(new FluidStack(APIUtils.animalGas, (100 * meta)), true);
					worldObj.setBlockToAir(xCoord, yCoord - 1, zCoord);
					buffer.refresh();
				}
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound par1)
	{
		super.readFromNBT(par1);
		tank.readFromNBT(par1);
	}

	@Override
	public void writeToNBT(NBTTagCompound par1)
	{
		super.writeToNBT(par1);
		tank.writeToNBT(par1);
	}

	@Override
	public boolean isFireSource(ForgeDirection side)
	{
		if(side == ForgeDirection.UP)
		{
			boolean flag = tank.getFluidAmount() > 0;
			if(flag)
			{
				tank.drain(1, true);
			}
			return flag;
		}
		return false;
	}
	
	
	
}
