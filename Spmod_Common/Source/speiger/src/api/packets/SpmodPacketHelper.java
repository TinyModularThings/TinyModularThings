package speiger.src.api.packets;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import cpw.mods.fml.common.FMLLog;

public class SpmodPacketHelper
{
	private static SpmodPacketHelper instance = new SpmodPacketHelper();
	
	public static SpmodPacketHelper getHelper()
	{
		return instance;
	}
	
	public ModularPacket createNBTPacket(TileEntity tile, SpmodMod mod)
	{
		return this.createNBTPacket(tile.worldObj, tile.xCoord, tile.yCoord, tile.zCoord, mod);
	}
	
	public ModularPacket createNBTPacket(int dimID, int x, int y, int z, SpmodMod mod)
	{
		return this.createNBTPacket(DimensionManager.getWorld(dimID), x, y, z, mod);
	}
	
	public ModularPacket createNBTPacket(World world, int x, int y, int z, SpmodMod mod)
	{
		if (world.getBlockTileEntity(x, y, z) == null || !(world.getBlockTileEntity(x, y, z) instanceof IPacketReciver) || !SpmodModRegistry.isModRegistered(mod))
		{
			return null;
		}
		return new ModularPacket(world.provider.dimensionId, x, y, z).injetString(mod.getName());
		
	}
	
	public static class ModularPacket
	{
		DataOutputStream stream;
		ByteArrayOutputStream bytes;
		
		private ModularPacket(int dimID, int x, int y, int z)
		{
			bytes = new ByteArrayOutputStream();
			stream = new DataOutputStream(bytes);
			InjectNumbers(dimID, x, y, z);
		}
		
		public ModularPacket InjectNumber(Object par1)
		{
			try
			{
				if (par1 instanceof Integer)
				{
					stream.writeInt((Integer) par1);
				}
				else if (par1 instanceof Float)
				{
					stream.writeFloat((Float) par1);
				}
				else if (par1 instanceof Double)
				{
					stream.writeDouble((Double) par1);
				}
				else if (par1 instanceof Long)
				{
					stream.writeLong((Long) par1);
				}
				else if (par1 instanceof Short)
				{
					stream.writeShort((Short) par1);
				}
				else if (par1 instanceof Byte)
				{
					stream.writeByte((Byte) par1);
				}
				return this;
			}
			catch (Exception e)
			{
				FMLLog.getLogger().info("Error injecting Data");
				return this;
			}
			
		}
		
		public ModularPacket InjectNumbers(Object... par1)
		{
			ModularPacket packet = this;
			for (Object data : par1)
			{
				packet = InjectNumber(data);
			}
			return packet;
		}
		
		public ModularPacket InjectStrings(String... par1)
		{
			ModularPacket packet = this;
			for (String cu : par1)
			{
				packet = injetString(cu);
			}
			return packet;
		}
		
		public ModularPacket injetString(String par1)
		{
			try
			{
				stream.writeUTF(par1);
				return this;
			}
			catch (Exception e)
			{
				FMLLog.getLogger().info("Error injecting Data");
				return this;
			}
		}
		
		
		public Packet250CustomPayload finishPacket()
		{
			return new Packet250CustomPayload("Spmod", bytes.toByteArray());
		}
	}
	
}
