package speiger.src.api.hopper;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import speiger.src.tinymodularthings.common.utils.HopperType;

public abstract interface HopperUpgrade
{
	/**
	 * @Info MainTick!
	 */
	public abstract void onTick(IHopper paramIHopper);
	
	/**
	 * save Function. So you can save your data
	 */
	public abstract void onNBTWrite(NBTTagCompound paramNBTTagCompound);
	
	/**
	 * reading Function. Reading your NBTData
	 */
	public abstract void onNBTRead(NBTTagCompound paramNBTTagCompound);
	
	/**
	 * DisplayName.
	 */
	public abstract String getUpgradeName();
	
	/**
	 * Register/NBTName. Really Important!
	 */
	public abstract String getNBTName();
	
	/**
	 * If the Player makes a ShiftClick while he looking at the Tab this allows to you to add information.
	 */
	public abstract void getInformationList(EntityPlayer player, List par2);
	
	/**
	 * Function will be called when the Hopper is adding Your Upgrade
	 */
	public abstract void onRegisterUpgrade(IHopper par1);
	
	/**
	 * Function will be called when the upgrade get removed!
	 */
	public abstract void onRemovingUpgrade(IHopper par1);
	
	/**
	 * this function tells how often you can Apply this Upgrade to the Hopper!
	 */
	public abstract int getMaxStackSize();
	
	/**
	 * When someone Clicks with a item that is not a wrench/emptyHand/HopperUpgrade you can make something.
	 * Return true prevent gui opening.
	 */
	public abstract boolean onClick(boolean sneak, EntityPlayer player, Block block, IHopper hopper, int side);
	
	/**
	 * This function says for which type of the Hopper the upgrade is. Nothing does mean null. 
	 */
	public abstract HopperType getUpgradeType();
	
}