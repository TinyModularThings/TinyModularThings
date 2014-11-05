package speiger.src.tinymodularthings.common.upgrades.hoppers.fluids;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import speiger.src.api.common.world.tiles.interfaces.HopperUpgrade;
import speiger.src.api.common.world.tiles.interfaces.IHopper;
import speiger.src.tinymodularthings.common.utils.HopperType;

public class UpgradeTransFluid implements HopperUpgrade
{
	int qty;
	
	public UpgradeTransFluid()
	{
	}
	
	public UpgradeTransFluid(int par1)
	{
		qty = par1;
	}
	
	@Override
	public void onTick(IHopper par1)
	{
		
	}
	
	@Override
	public void onNBTWrite(NBTTagCompound nbt)
	{
		nbt.setInteger("Transferlimit", qty);
	}
	
	@Override
	public void onNBTRead(NBTTagCompound nbt)
	{
		qty = nbt.getInteger("Transferlimit");
	}
	
	@Override
	public String getUpgradeName()
	{
		return "Transferlimit Upgrade for Fluid Types";
	}
	
	@Override
	public String getNBTName()
	{
		return "upgrade.basic.transferlimit.fluid";
	}
	
	@Override
	public void getInformationList(EntityPlayer player, List par2)
	{
		par2.add("Can be used 16 Times");
		par2.add("Fluid Transport Upgrade: " + qty);
	}
	
	@Override
	public void onRegisterUpgrade(IHopper par1)
	{
		par1.addTransferlimit(HopperType.Fluids, qty);
	}
	
	@Override
	public void onRemovingUpgrade(IHopper par1)
	{
		par1.removeTransferlimit(HopperType.Fluids, qty);
	}
	
	@Override
	public int getMaxStackSize()
	{
		return 16;
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
