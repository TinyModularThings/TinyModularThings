package speiger.src.tinymodularthings.common.blocks.machine;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.*;
import speiger.src.api.common.utils.WorldReading;
import speiger.src.api.common.world.tiles.energy.EnergyProvider;
import speiger.src.api.common.world.tiles.energy.IEnergyProvider;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.tile.AdvancedFluidTank;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WaterGenerator extends AdvTile implements IFluidHandler,
		IPowerReceptor, IEnergyProvider
{
	public AdvancedFluidTank tank = new AdvancedFluidTank(this, "Water", 50000);
	public EnergyProvider provider = new EnergyProvider(this, 10000);
	
	public WaterGenerator()
	{
		tank.setFluid(new FluidStack(FluidRegistry.WATER, 0));
		provider.setPowerLoss(500);
	}
	
	@Override
	public void onTick()
	{
		super.onTick();
		
		if (!worldObj.isRemote)
		{
			this.provider.update();
			if(provider.getStoredEnergy() >= 1)
			{
				produceWater();
			}
			if(tank.getFluidAmount() > 0)
			{
				sendWater();
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onItemInformation(EntityPlayer par1, List par2, ItemStack par3)
	{
		super.onItemInformation(par1, par2, par3);
		par2.add("Generates Power out of Energy");
		par2.add("50MJ per Bucket, Max Powerusage = 10000MJ");
	}

	@Override
	public float getBlockHardness()
	{
		return 7F;
	}

	@Override
	public float getExplosionResistance(Entity par1)
	{
		return 20F;
	}

	public void sendWater()
	{
		FluidStack toSend = tank.drain(50000, false);
		if (toSend != null)
		{
			FluidStack copySend = toSend.copy();
			for (ForgeDirection cu : ForgeDirection.VALID_DIRECTIONS)
			{
				if (toSend.amount > 0)
				{
					int x = this.xCoord + cu.offsetX;
					int y = this.yCoord + cu.offsetY;
					int z = this.zCoord + cu.offsetZ;
					if (WorldReading.hasTank(worldObj, x, y, z))
					{
						toSend.amount -= WorldReading.getFluidTank(worldObj, x, y, z).fill(cu.getOpposite(), toSend, true);
					}
				}
			}
			copySend.amount -= toSend.amount;
			tank.drain(copySend.amount, true);
		}
		
	}
	
	public void produceWater()
	{
		int energy = this.provider.getStoredEnergy();
		float can = energy / 50;
		if (can > 0)
		{
			int added = tank.fill(new FluidStack(FluidRegistry.WATER, (int)(can * 1000)), false);
			if (added <= 0)
			{
				return;
			}
			double add = (double)added / (double)1000;
			tank.fill(new FluidStack(FluidRegistry.WATER, (int)(can * 1000)), true);
			if (add > 0.0D && add < 1.0D)
			{
				this.provider.useEnergy(1, false);
				return;
			}
			
			this.provider.useEnergy((int)(add*50), false);
		}
		
	}
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		return TextureEngine.getTextures().getTexture(TinyBlocks.machine, 3, side == 0 ? 1 : side == 1 ? 0 : 2);
	}
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		return 0;
	}
	
	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		return this.drain(from, resource.amount, doDrain);
	}
	
	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		return tank.drain(maxDrain, doDrain);
	}
	
	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return false;
	}
	
	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return true;
	}
	
	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		return new FluidTankInfo[] { tank.getInfo() };
	}
	
	@Override
	public EnergyProvider getEnergyProvider(ForgeDirection side)
	{
		return provider;
	}
	
	@Override
	public PowerReceiver getPowerReceiver(ForgeDirection side)
	{
		return provider.getSaveBCPowerProvider();
	}
	
	@Override
	public void doWork(PowerHandler workProvider)
	{
	}
	
	@Override
	public World getWorld()
	{
		return worldObj;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound)
	{
		super.readFromNBT(par1nbtTagCompound);
		this.tank.readFromNBT(par1nbtTagCompound);
		this.provider.readFromNBT(par1nbtTagCompound);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound par1nbtTagCompound)
	{
		super.writeToNBT(par1nbtTagCompound);
		this.tank.writeToNBT(par1nbtTagCompound);
		this.provider.writeToNBT(par1nbtTagCompound);
	}
	
	@Override
	public ItemStack getItemDrop()
	{
		return new ItemStack(TinyBlocks.machine, 1, 3);
	}
	
}
