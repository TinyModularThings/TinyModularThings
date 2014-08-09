package speiger.src.spmodapi.common.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;

public class CountdownTick
{
	@SubscribeEvent
	public void tickEnd(TickEvent.PlayerTickEvent event)
	{
		if (event.side != Side.SERVER || event.phase != TickEvent.Phase.END)
		{
			return;
		}

		NBTTagCompound playerNBT = event.player.getEntityData();
		
		if (playerNBT.hasKey("SpmodAPIData"))
		{
			NBTTagCompound nbt = playerNBT.getCompoundTag("SpmodAPIData");
			if (nbt.hasKey("CountdownTime"))
			{
				int time = nbt.getInteger("CountdownTime");
				time--;
				nbt.setInteger("CountdownTime", time);
				if (time <= 0)
				{
					event.player.addChatMessage(new ChatComponentText("Countdown is over"));
					
					if (time <= -50)
					{
						nbt.removeTag("CountdownTime");
					}
				}
				
			}
		}
		
		TickHelper.getInstance().tick();
		
	}
}
