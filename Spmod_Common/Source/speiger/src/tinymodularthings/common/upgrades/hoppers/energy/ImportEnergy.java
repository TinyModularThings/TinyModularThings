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

public class ImportEnergy implements HopperUpgrade
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
				importEnergy(par1, provider, dir, par1.getTransferlimit(HopperType.Energy));
			}
		}
	}
	
	public static void importEnergy(IHopper par0, EnergyUsageProvider par1, ForgeDirection par2, int energyTransferlimit)
	{
		if(par1 == null)
		{
			return;
		}
		int usedEnergy = par1.useEnergy(energyTransferlimit);
		if(usedEnergy > 0)
		{
			int addedEnergy = usedEnergy - par1.addEnergy(usedEnergy);
			if(addedEnergy > 0)
			{
				par1.addEnergy(addedEnergy);
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
		return "Import Energy Upgrade";
	}
	
	@Override
	public String getNBTName()
	{
		return "energy.import.basic";
	}
	
	@Override
	public void getInformationList(EntityPlayer player, List par2)
	{
		par2.add("Import Energy");
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
		return HopperType.Energy;
	}
	
}
