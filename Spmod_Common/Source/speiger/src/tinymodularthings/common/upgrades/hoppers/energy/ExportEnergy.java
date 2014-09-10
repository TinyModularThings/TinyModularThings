package speiger.src.tinymodularthings.common.upgrades.hoppers.energy;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.energy.EnergyUsageProvider;
import speiger.src.api.hopper.HopperUpgrade;
import speiger.src.api.hopper.IHopper;
import speiger.src.api.util.WorldReading;
import speiger.src.tinymodularthings.common.utils.HopperType;

public class ExportEnergy implements HopperUpgrade
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
		if(tile != null)
		{
			EnergyUsageProvider provider = EnergyUsageProvider.createUsageProvider(tile, dir.ordinal());
			if(provider != null)
			{
				exportEnergy(par1, provider, dir, par1.getTransferlimit(HopperType.Energy));
			}
		}
	}
	
	public static void exportEnergy(IHopper par1, EnergyUsageProvider par2, ForgeDirection par3, int transferlimit)
	{
		if(par2 == null)
		{
			return;
		}
		int added = par1.getEnergyStorage().useEnergy(transferlimit, true);
		if(added > 0)
		{
			int used = par2.addEnergy(added);
			if(used > 0)
			{
				par1.getEnergyStorage().useEnergy(used, false);
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
		return "Export Basic Energy";
	}
	
	@Override
	public String getNBTName()
	{
		return "energy.export.basic";
	}
	
	@Override
	public void getInformationList(EntityPlayer player, List par2)
	{
		par2.add("Transfer Energy");
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
		return HopperType.Energy;
	}
	
}
