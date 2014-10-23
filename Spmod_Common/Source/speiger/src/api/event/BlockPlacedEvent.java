package speiger.src.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.BlockEvent;
import speiger.src.api.blocks.BlockPosition;

public class BlockPlacedEvent extends BlockEvent
{
	public EntityPlayer placer;
	public BlockPosition blockData;
	
	public BlockPlacedEvent(BlockPosition pos, EntityPlayer placer)
	{
		super(pos.xCoord, pos.yCoord, pos.zCoord, pos.worldID, pos.getBlock(), pos.getBlockMetadata());
		this.placer = placer;
		blockData = pos;
	}

}
