package speiger.src.spmodapi.common.handler;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;

public class BucketHandler
{
	@ForgeSubscribe
	public void onBucketFill(FillBucketEvent evt)
	{
		if(SpmodConfig.booleanInfos.get("APIOnly"))
		{
			return;
		}
		if (evt.world.getBlockId(evt.target.blockX, evt.target.blockY, evt.target.blockZ) == APIBlocks.fluidHempResin.blockID)
		{
			if (evt.world.getBlockMetadata(evt.target.blockX, evt.target.blockY, evt.target.blockZ) == 0)
			{
				evt.result = new ItemStack(APIItems.hempResinBucket);
				evt.setResult(Result.ALLOW);
				evt.world.setBlock(evt.target.blockX, evt.target.blockY, evt.target.blockZ, 0);
			}
		}
	}
}
