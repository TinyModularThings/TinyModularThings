package speiger.src.spmodapi.common.world.retrogen;

import net.minecraftforge.event.world.ChunkDataEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class RetrogenSave
{
	@SubscribeEvent
	public void onChunkSave(ChunkDataEvent.Save par1)
	{
		par1.getData().setBoolean("SpmodAPI.Retrogen", true);
	}
}
