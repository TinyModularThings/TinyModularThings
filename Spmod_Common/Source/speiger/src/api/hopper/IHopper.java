package speiger.src.api.hopper;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import speiger.src.api.energy.EnergyProvider;
import speiger.src.api.hopper.HopperRegistry.HopperEffect;
import speiger.src.tinymodularthings.common.utils.HopperType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract interface IHopper
{
	/**
	 * @return xCoord of the Hopper
	 */
	public abstract int getXPos();

	/**
	 * @return yCoord of the Hopper
	 */
	public abstract int getYPos();
	
	/**
	 * @return zCoord of the Hopper
	 */
	public abstract int getZPos();
	
	/**
	 * @return HopperWorld
	 */
	public abstract World getWorld();
	
	/**
	 * @return if the hopper has Facing
	 */
	public abstract boolean hasFacing();
	
	/**
	 * @return the HopperFacing
	 */
	public abstract int getFacing();
	
	/**
	 * @return if the Hopper has a Rotation
	 */
	public abstract boolean hasRotation();
	
	/**
	 * @return the Hopper Rotation
	 */
	public abstract int getRotation();
	
	/**
	 * @return if the hopper has a inventory
	 */
	public abstract boolean hasInventory();
	
	/**
	 * @return the Hopper Inventory (can be null) and you can not change it its a fake inventory
	 */
	public abstract IInventory getInventory();
	
	/**
	 * @return the Transferlimit of the Current hopper (items,fluids,energy) nothing == 0
	 */
	public abstract int getTransferlimit(HopperType par1);
	
	/**
	 * @return the Original Transferlimit of the currentHopper
	 */
	public abstract int getClearTransferlimit(HopperType par1);
	
	/**
	 * function for adding transferlimit to the Hopper.
	 */
	public abstract void addTransferlimit(HopperType par1, int amount);
	
	/**
	 * function for removing transferlimit
	 */
	public abstract void removeTransferlimit(HopperType par1, int amount);
	
	/**
	 * functions for Adding Items to the Hopper. Returns what was added.
	 */
	public abstract ItemStack addItemsToHopper(ItemStack paramItemStack);
	
	/**
	 * @return the removed amount
	 */
	public abstract ItemStack removeItemsFromHopper(int slot, int stacksize);
	
	/**
	 * @return if the Hopper has a fluid Tank
	 */
	public abstract boolean hasFluidTank();
	
	/**
	 * @return the tank size (1 tank - 9 tanks)
	 */
	public abstract int getFluidTankSize();
	
	/**
	 * @return return the Fluid Tank from a tankID (can be null)
	 */
	public abstract CopiedFluidTank getTank(int tankID);
	
	/**
	 * @return every tank in this hopper.(can be null / empty)
	 */
	public abstract CopiedFluidTank[] getFluidTank();
	
	/**
	 * Basic Function for adding Fluid. Tring to add it to any tank.
	 * @return added amount. Can be null
	 */
	public abstract FluidStack addFluid(FluidStack fluid);
	
	/**
	 * Function for adding fluids. Choose your tankSlot.
	 * @return added amount. Can be null
	 */
	public abstract FluidStack addFluid(FluidStack fluid, int forcedSlot);
	
	/**
	 * Function for removing Fluid. 
	 * @Simulate If you really want to remove it. True == Draining for real
	 * @return Removed amount. Can be null
	 */
	public abstract FluidStack removeFluid(int amount, int slot, boolean simulate);
	
	/**
	 * @return if the Hopper has a EnergyProvider
	 */
	public abstract boolean hasEnergyProvider();
	
	/**
	 * @return the EnergyProvider, can be null
	 */
	public abstract EnergyProvider getEnergyStorage();
	
	/**
	 * @return installed Upgrades
	 */
	public abstract ArrayList<HopperUpgrade> getUpgrades();
	
	/**
	 * @return that the Hopper is limited to special Upgrades
	 */
	public abstract boolean hasUpgradeLimitation();
	
	/**
	 * @return all allowed Upgrades
	 */
	public abstract ArrayList<HopperUpgrade> getValidUpgrades();
	
	/**
	 * Adding function for Hopper Effects.
	 * @return Returns true if it worked. False mean alread at that state
	 */
	public abstract boolean applyEffect(HopperEffect par1, boolean change);
	
	/**
	 * @return if the effect is applied
	 */
	public abstract boolean hasEffectApplied(HopperEffect par1);
	
	/**
	 * @return all Players that looking at the Hopper.
	 */
	public abstract ArrayList<EntityPlayer> getUsingPlayers();
	
	/**
	 * @Note The FakePlayer Inventory Will be cleared every Tick!
	 * Also he get Healed and potioneffects will be removed every tick!
	 * Do not forget that.
	 * @return get The FakePlayer of the Hopper.
	 */
	public abstract EntityPlayer getFakePlayer();
	
	/**
	 * @return Owner of the Hopper.
	 */
	public abstract String getOwner();
	
	/**
	 * This function tells you if you can cast to IOwner Class. This interface is for more details!
	 * @return provide IOwner
	 */
	public abstract boolean isOwnerInventory();
	
	/**
	 * @return if the Hopper Upgrade got added.
	 */
	public abstract boolean addUpgrade(HopperUpgrade par1);
	
	/**
	 * @return if the Upgrade got removed.
	 */
	public abstract boolean removeUpgrade(HopperUpgrade par1);
	
	/**
	 * @Return what kind of hopper it is
	 */
	public abstract HopperType getHopperType();
	
	/**
	 * @return ModelTexture
	 */
	@SideOnly(Side.CLIENT)
	public abstract ResourceLocation getRenderingTexture();
}