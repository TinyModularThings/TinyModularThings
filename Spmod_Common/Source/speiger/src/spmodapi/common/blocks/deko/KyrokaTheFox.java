package speiger.src.spmodapi.common.blocks.deko;

import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import speiger.src.spmodapi.common.lib.SpmodAPILib;
import speiger.src.spmodapi.common.tile.TileFacing;
import speiger.src.spmodapi.common.util.TextureEngine;
import cpw.mods.fml.common.network.PacketDispatcher;

public class KyrokaTheFox extends TileFacing
{
	int type = 0;
	
	public static HashMap<Integer, ResourceLocation> textures = new HashMap<Integer, ResourceLocation>();
	
	public KyrokaTheFox()
	{
		
	}
	
	public KyrokaTheFox(World world)
	{
		if(world == null)
		{
			type = 0;
		}
		else if(world.provider.isHellWorld)
		{
			type = 1;
		}
		else if(world.provider.dimensionId == 1)
		{
			//There will be a third type soon. I need only to find the right picture :/
			type = 1;
		}
		else
		{
			type = 0;
		}
	}
	
	

	@Override
	public void onPlaced(int facing)
	{
		if(facing < 2)
		{
			facing = 2;
		}
		this.setFacing((short)facing);
	}



	@Override
	public void registerIcon(TextureEngine par1)
	{
		textures.put(0, new ResourceLocation(SpmodAPILib.ModID.toLowerCase()+":textures/models/armor/ModelKyrokaTheFox.png"));
		textures.put(1, new ResourceLocation(SpmodAPILib.ModID.toLowerCase()+":textures/models/armor/ModelKyrokaTheFoxDark.png"));
	}

	public ResourceLocation getTexture()
	{
		ResourceLocation texture = textures.get(type);
		if(texture == null)
		{
			texture = textures.get(0);
		}
		
		if(texture == null)
		{
			texture = new ResourceLocation(SpmodAPILib.ModID.toLowerCase()+":textures/models/armor/ModelKyrokaTheFox.png");
		}
		return texture;
	}

	@Override
	public boolean isSixSidedFacing()
	{
		return false;
	}
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		return null;
	}



	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		type = nbt.getInteger("Type");
	}



	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("Type", type);
	}



	@Override
	public void onTick()
	{
		if(!worldObj.isRemote)
		{
			if(worldObj.getWorldTime() % 200 == 0)
			{
				PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, getDescriptionPacket());
			}
		}
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, nbt);
	}
	
	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
	{
		readFromNBT(pkt.data);
	}
	
}
