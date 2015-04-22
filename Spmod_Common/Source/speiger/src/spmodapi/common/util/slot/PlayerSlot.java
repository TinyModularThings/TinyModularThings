package speiger.src.spmodapi.common.util.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

public class PlayerSlot extends Slot
{
	public EntityPlayer player;
	public boolean fake = false;
	public PlayerSlot(InventoryPlayer par1, int par2, int par3, int par4)
	{
		super(par1, par2, par3, par4);
		setupPlayer(par1.player);
	}
	
	public PlayerSlot(EntityPlayer par1, int par2, int par3, int par4)
	{
		this(par1.inventory, par2, par3, par4);
	}
	
	public PlayerSlot setFake()
	{
		fake = true;
		return this;
	}
	
	public boolean isFake()
	{
		return fake;
	}
	
	public void setupPlayer(EntityPlayer par1)
	{
		player = par1;
	}
	
	public EntityPlayer getPlayer()
	{
		return player;
	}
}