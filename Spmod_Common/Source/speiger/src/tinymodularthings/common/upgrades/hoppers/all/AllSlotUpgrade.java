package speiger.src.tinymodularthings.common.upgrades.hoppers.all;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import speiger.src.api.hopper.HopperRegistry.HopperEffect;
import speiger.src.api.hopper.HopperUpgrade;
import speiger.src.api.hopper.IHopper;
import speiger.src.tinymodularthings.common.utils.HopperType;

public class AllSlotUpgrade implements HopperUpgrade
{
	
	@Override
	public void onTick(IHopper par1)
	{
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
		return "All Slot Upgrade";
	}
	
	@Override
	public String getNBTName()
	{
		return "upgrade.basic.allSlots";
	}
	
	@Override
	public void getInformationList(EntityPlayer player, List par2)
	{
		par2.add("Add all Slots Transfering");
		par2.add("Can be used only 1 Time");
	}
	
	@Override
	public void onRegisterUpgrade(IHopper par1)
	{
		par1.applyEffect(HopperEffect.AllSlots, true);
	}
	
	@Override
	public void onRemovingUpgrade(IHopper par1)
	{
		par1.applyEffect(HopperEffect.AllSlots, false);
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
		return HopperType.Nothing;
	}
	
}
