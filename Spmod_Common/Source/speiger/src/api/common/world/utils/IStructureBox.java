package speiger.src.api.common.world.utils;

import net.minecraft.entity.player.EntityPlayer;


public interface IStructureBox
{
	public boolean contains(int x, int y, int z);
	
	public int getMinX();
	
	public int getMinY();
	
	public int getMinZ();
	
	public int getMaxX();
	
	public int getMaxY();
	
	public int getMaxZ();
	
	public void onBlockChange();
	
	public void onBlockBreak();
	
	public void onInteract(EntityPlayer par1);
	
	public boolean isBoxActive();
}
