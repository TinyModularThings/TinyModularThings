package speiger.src.tinymodularthings.common.upgrades.hoppers.all;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import speiger.src.api.hopper.HopperUpgrade;
import speiger.src.api.hopper.IHopper;
import speiger.src.tinymodularthings.common.utils.HopperType;

public class UpgradeTransAll implements HopperUpgrade
{
	int item;
	int fluid;
	int energy;
	
	public UpgradeTransAll()
	{
	}
	
	public UpgradeTransAll(int x, int y, int z)
	{
		item = x;
		fluid = y;
		energy = z;
	}
	
	@Override
	public void onTick(IHopper par1)
	{
		
	}
	
	@Override
	public void onNBTWrite(NBTTagCompound nbt)
	{
		nbt.setInteger("Item", item);
		nbt.setInteger("Fluid", fluid);
		nbt.setInteger("Energy", energy);
	}
	
	@Override
	public void onNBTRead(NBTTagCompound nbt)
	{
		item = nbt.getInteger("Item");
		fluid = nbt.getInteger("Fluid");
		energy = nbt.getInteger("Energy");
	}
	
	@Override
	public String getUpgradeName()
	{
		return "Transferlimit Upgrade for All Types";
	}
	
	@Override
	public String getNBTName()
	{
		return "upgrade.basic.transferlimit.all";
	}
	
	@Override
	public void getInformationList(EntityPlayer player, List par2)
	{
		par2.add("Can be used 5 Times");
		par2.add("Transferlimit Boost:");
		par2.add("Item: "+item);
		par2.add("Fluid: "+fluid);
		par2.add("Energy: "+energy);
	}
	
	@Override
	public void onRegisterUpgrade(IHopper par1)
	{
		par1.addTransferlimit(HopperType.Items, item);
		par1.addTransferlimit(HopperType.Fluids, fluid);
		par1.addTransferlimit(HopperType.Energy, energy);
	}
	
	@Override
	public void onRemovingUpgrade(IHopper par1)
	{
		par1.removeTransferlimit(HopperType.Items, item);
		par1.removeTransferlimit(HopperType.Fluids, fluid);
		par1.removeTransferlimit(HopperType.Energy, energy);
	}
	
	@Override
	public int getMaxStackSize()
	{
		return 5;
	}
	
	@Override
	public boolean onClick(boolean sneak, EntityPlayer player, Block block, IHopper hopper, int side)
	{
		return false;
	}
	
	@Override
	public HopperType getUpgradeType()
	{
		return HopperType.Nothing;
	}
	
	
	
}
