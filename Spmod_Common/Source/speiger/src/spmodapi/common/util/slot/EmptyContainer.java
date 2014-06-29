package speiger.src.spmodapi.common.util.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class EmptyContainer extends Container
{
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return true;
	}
	
}
