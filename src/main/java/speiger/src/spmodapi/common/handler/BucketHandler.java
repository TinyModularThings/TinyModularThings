package speiger.src.spmodapi.common.handler;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BucketHandler
{
	@SubscribeEvent
	public void onBucketFill(FillBucketEvent evt)
	{
		if(evt.world.getBlock(evt.target.blockX, evt.target.blockY, evt.target.blockZ) == APIBlocks.fluidHempResin)
		{
			if(evt.world.getBlockMetadata(evt.target.blockX, evt.target.blockY, evt.target.blockZ) == 0)
			{
				evt.result = new ItemStack(APIItems.hempResinBucket);
				evt.setResult(Result.ALLOW);
				evt.world.setBlock(evt.target.blockX, evt.target.blockY, evt.target.blockZ, Blocks.air);
			}
		}
	}
}
