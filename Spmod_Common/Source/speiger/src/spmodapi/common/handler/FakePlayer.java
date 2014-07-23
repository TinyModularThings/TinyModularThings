package speiger.src.spmodapi.common.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class FakePlayer extends EntityPlayer
{
	ChunkCoordinates coords;
	public FakePlayer(World world, int x, int y, int z)
	{
		super(world, "[SpmodFakePlayer]");
		coords = new ChunkCoordinates(x, y, z);
	}
	
	@Override
	public void sendChatToPlayer(ChatMessageComponent chatmessagecomponent)
	{		
	}
	
	@Override
	public boolean canCommandSenderUseCommand(int i, String s)
	{
		return false;
	}
	
	@Override
	public ChunkCoordinates getPlayerCoordinates()
	{
		return coords;
	}
	
	
	
}
