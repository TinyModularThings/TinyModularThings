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
import speiger.src.api.hopper.HopperUpgrade;
import speiger.src.api.hopper.IHopper;
import speiger.src.api.util.WorldReading;
import speiger.src.tinymodularthings.common.utils.HopperType;

public class ImportFluids implements HopperUpgrade
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
		TileEntity tile = WorldReading.getTileEntity(world, x, y, z, par1.getFacing());
		if(tile != null && tile instanceof IFluidHandler)
		{
			drainFluids(par1, (IFluidHandler)tile, dir);
		}
	}
	
	public static void drainFluids(IHopper par0, IFluidHandler par1, ForgeDirection par2)
	{
		FluidStack fluid = par1.drain(par2, par0.getTransferlimit(HopperType.Fluids), false);
		
		if(fluid == null || !par1.canDrain(par2, fluid.getFluid()))
		{
			return;
		}
		
		FluidStack added = par0.addFluid(fluid);
		if(added != null && added.amount > 0)
		{
			par1.drain(par2, added, true);
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
		return "Basic Fluid Import";
	}
	
	@Override
	public String getNBTName()
	{
		return "fluids.import.basic";
	}
	
	@Override
	public void getInformationList(EntityPlayer player, List par2)
	{
		par2.add("Import Fluids");
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
