package speiger.src.spmodapi.common.network.packets.base;

import java.io.DataInput;
import java.io.DataOutput;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import speiger.src.api.common.data.packets.ISpmodPacket;
import speiger.src.spmodapi.common.items.core.ItemInventory;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;

public abstract class ItemInventoryPacket implements ISpmodPacket
{
	String player;
	String id;
	
	public ItemInventoryPacket()
	{
	}
	
	public ItemInventoryPacket(ItemInventory par1)
	{
		player = par1.player.username;
		id = par1.getInvName();
	}
	
	
	@Override
	public void readData(DataInput par1) throws Exception
	{
		player = par1.readUTF();
		id = par1.readUTF();
	}
	
	@Override
	public void writeData(DataOutput par1) throws Exception
	{
		par1.writeUTF(player);
		par1.writeUTF(id);
	}
	
	public ItemInventory getInventory(Side par1)
	{
		if(par1 == Side.CLIENT)
		{
			EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;
			if(player != null && player.openContainer instanceof AdvContainer)
			{
				AdvContainer container = (AdvContainer)player.openContainer;
				if(container.getInvName().equalsIgnoreCase(id))
				{
					return container.getTile(ItemInventory.class);
				}
			}
		}
		else
		{
			EntityPlayer targetPlayer = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(player);
			if(targetPlayer != null && targetPlayer.openContainer instanceof AdvContainer)
			{
				AdvContainer container = (AdvContainer)targetPlayer.openContainer;
				if(container.getInvName().equalsIgnoreCase(id))
				{
					return container.getTile(ItemInventory.class);
				}
			}
		}
		
		return null;
		
	}
	
}
