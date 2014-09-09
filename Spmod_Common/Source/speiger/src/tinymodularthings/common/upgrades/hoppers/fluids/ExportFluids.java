package speiger.src.tinymodularthings.common.upgrades.hoppers.fluids;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import speiger.src.api.hopper.CopiedFluidTank;
import speiger.src.api.hopper.HopperRegistry.HopperEffect;
import speiger.src.api.hopper.HopperUpgrade;
import speiger.src.api.hopper.IHopper;
import speiger.src.api.util.WorldReading;
import speiger.src.tinymodularthings.common.utils.HopperType;

public class ExportFluids implements HopperUpgrade
{
	
	@Override
	public void onTick(IHopper par1)
	{
		World world = par1.getWorld();
		if(world.isRemote)
		{
			return;
		}
		int x = par1.getXPos();
		int y = par1.getYPos();
		int z = par1.getZPos();
		ForgeDirection dir = ForgeDirection.getOrientation(par1.getRotation()).getOpposite();
		TileEntity tile = WorldReading.getTileEntity(world, x, y, z, par1.getRotation());
		if(tile != null && tile instanceof IFluidHandler)
		{
			CopiedFluidTank[] tank = par1.getFluidTank();
			IFluidHandler target = (IFluidHandler) tile;
			for(int i = 0;i<tank.length;i++)
			{
				if(exportFluids(par1, target, dir, par1.removeFluid(par1.getTransferlimit(HopperType.Fluids), i, false), i))
				{
					if(par1.hasEffectApplied(HopperEffect.AllSlots))
					{
						continue;
					}
					return;
				}
			}
		}
	}
	
	public static boolean exportFluids(IHopper par0, IFluidHandler par1, ForgeDirection par2, FluidStack fluid, int tankSlot)
	{
		if(par1 == null || fluid == null)
		{
			return false;
		}
		
		if(par1.canFill(par2, fluid.getFluid()))
		{
			int added = par1.fill(par2, fluid, true);
			if(added > 0)
			{
				par0.removeFluid(added, tankSlot, true);
				return true;
			}
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
		return "Basic Fluid Export";
	}
	
	@Override
	public String getNBTName()
	{
		return "fluids.export.basic";
	}
	
	@Override
	public void getInformationList(EntityPlayer player, List par2)
	{
		par2.add("Export Fluids");
		par2.add("Can be used 1 time");
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
		return HopperType.Fluids;
	}
	
}
