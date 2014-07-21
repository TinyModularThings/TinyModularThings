package speiger.src.spmodapi.common.handler;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public class FakePlayer extends EntityPlayer
{
	ChunkCoordinates coords;
	public FakePlayer(World world, int x, int y, int z)
	{
		super(world, new GameProfile(UUID.nameUUIDFromBytes("speiger.src.spmodapi".getBytes()), "[SpmodFakePlayer]"));
		coords = new ChunkCoordinates(x, y, z);
	}
	
	@Override
	public void addChatMessage(IChatComponent chatmessagecomponent)
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
