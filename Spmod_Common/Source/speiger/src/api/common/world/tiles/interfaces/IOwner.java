package speiger.src.api.common.world.tiles.interfaces;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * 
 * @author Speiger
 * 
 */
public interface IOwner
{
	/**
	 * My Owner Interface which is like ThermalExpansions thinig there. But
	 * functions should selfExplain
	 */
	
	String getOwner();
	
	void setOwner(String par1);
	
	boolean addFriend(String par1);
	
	ArrayList<String> getFriends();
	
	boolean isAlowedUser(String par1);
	
	boolean isAlowedUser(EntityPlayer par1);
	
	void saveToNBT(NBTTagCompound nbt);
	
	void loadFromNBT(NBTTagCompound nbt);
	
}
