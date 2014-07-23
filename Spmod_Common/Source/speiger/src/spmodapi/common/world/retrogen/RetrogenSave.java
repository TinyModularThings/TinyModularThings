package speiger.src.spmodapi.common.world.retrogen;

import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.ChunkDataEvent;

public class RetrogenSave
{
	@ForgeSubscribe
	public void onChunkSave(ChunkDataEvent.Save par1)
	{
		par1.getData().setBoolean("SpmodAPI.Retrogen", true);
	}
}
