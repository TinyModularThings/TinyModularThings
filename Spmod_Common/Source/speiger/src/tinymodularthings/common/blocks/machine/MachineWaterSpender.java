package speiger.src.tinymodularthings.common.blocks.machine;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.*;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.tinymodularthings.common.utils.fluids.TinyFluidTank;

import com.google.common.math.DoubleMath;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MachineWaterSpender extends AdvTile implements IFluidHandler
{
	public TinyFluidTank tank = new TinyFluidTank("Water", 5000, this);
	public float rotation = 0;
	public float speed = 0.0F;

	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		return Block.stone.getBlockTextureFromSide(0);
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
		return null;
	}

	
	
	@Override
	public boolean isNormalCube()
	{
		return false;
	}


	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		return null;
	}


	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return fluid.getID() == FluidRegistry.WATER.getID();
	}


	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return false;
	}


	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		return new FluidTankInfo[]{tank.getInfo()};
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

	public float onRotation()
	{
		float data = tank.getFluidAmount() / 1000;
		if(speed < data)
		{
			speed+=0.01F;
		}
		else if(speed > data)
		{
			speed-=0.01F;
		}
		rotation+=speed;
		if(rotation > 360F)
		{
			rotation =- 360F;
		}
		
		return rotation;
	}


	@Override
	public void onTick()
	{
		super.onTick();
		if(worldObj.isRemote)
		{
			return;
		}
		onRotation();
		if(getClockTime() % 100 == 0 && tank.getFluidAmount() > 10)
		{
			checkBlocks();
		}
		tank.drain(1, true);
		
	}
	
		
	
	public void checkBlocks()
	{
		int range = DoubleMath.roundToInt((double)speed, RoundingMode.HALF_EVEN);
		switch(range)
		{
			case 0:
				spreyWater(new int[]{0, 0, 0, 0, 0});
				break;
			case 1:
				spreyWater(new int[]{0, 1, 1, 2, 2});
				break;
			case 2:
				spreyWater(new int[]{1, 1, 2, 2, 3});
				break;
			case 3:
				spreyWater(new int[]{1, 2, 3, 4, 5});
				break;
			case 4:
				spreyWater(new int[]{2, 3, 4, 5, 6});
				break;
			case 5:
				spreyWater(new int[]{2, 4, 6, 8, 10});
				break;
		}
	}
	
	public void spreyWater(int[] maxRange)
	{
		for(int y = 2;y<7;y++)
		{
			int maxX = -maxRange[y-2];
			for(int x = maxX;x<=-maxX;x++)
			{
				for(int z = maxX;z<=-maxX;z++)
				{
					int id = worldObj.getBlockId(xCoord+x, yCoord-y, zCoord+z);
					int meta = worldObj.getBlockMetadata(xCoord+x, yCoord-y, zCoord+z);
					if(id > 0 && id == Block.tilledField.blockID && meta < 7)
					{
						worldObj.setBlockMetadataWithNotify(xCoord+x, yCoord-y, zCoord+z, 7, 3);
					}
				}
			}
		}
	}
	


	@Override
	public ArrayList<AxisAlignedBB> getColidingBoxes(Entity par2)
	{
		ArrayList<AxisAlignedBB> aabb = new ArrayList<AxisAlignedBB>();
		return aabb;
	}


	@Override
	public AxisAlignedBB getSelectedBoxes()
	{
		return AxisAlignedBB.getAABBPool().getAABB(xCoord + 0.25D, yCoord + 0.4D, zCoord + 0.25D, xCoord + 0.75D, yCoord + 1D, zCoord + 0.75D);
	}


	@Override
	public AxisAlignedBB getColidingBox()
	{
		return AxisAlignedBB.getAABBPool().getAABB(xCoord + 0.25D, yCoord + 0.4D, zCoord + 0.25D, xCoord + 0.75D, yCoord + 1D, zCoord + 0.75D);
	}


	@Override
	@SideOnly(Side.CLIENT)
	public void onItemInformation(EntityPlayer par1, List par2, ItemStack par3)
	{
		super.onItemInformation(par1, par2, par3);
		par2.add("Make Farmlend Wet. It has a Huge Rang (10 Block Radius is Max)");
		par2.add("Require 5 mB per tick and do need To Speed Up. The range changes with the speed");
		par2.add("Speed and Rotation resets on Leaving game");
	}


	@Override
	public void onRenderWorld(Block block, RenderBlocks renderer)
	{
	}



	
	
	
	
}
