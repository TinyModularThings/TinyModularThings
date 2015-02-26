package speiger.src.api.common.data.packets;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import speiger.src.api.common.registry.helpers.SpmodMod;
import speiger.src.api.common.registry.helpers.SpmodModRegistry;

import com.google.common.base.Strings;

import cpw.mods.fml.common.FMLLog;

public class SpmodPacketHelper
{
	private static SpmodPacketHelper instance = new SpmodPacketHelper();
	
	public static SpmodPacketHelper getHelper()
	{
		return instance;
	}
	
	private HashMap<String, IPacketReciver> packets = new HashMap<String, IPacketReciver>();
	
	public void registerPacketReciver(IPacketReciver par1)
	{
		if (!Strings.isNullOrEmpty(par1.identifier()))
		{
			packets.put(par1.identifier(), par1);
		}
	}
	
	public IPacketReciver getReciverFromName(String par1)
	{
		if (!Strings.isNullOrEmpty(par1))
		{
			return packets.get(par1);
		}
		return null;
	}
	
	public ModularPacket createPlayerTilePacket(EntityPlayer player, SpmodMod mod)
	{
		return new ModularPacket(mod, player);
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
		return new ModularPacket(mod, world.provider.dimensionId, x, y, z);
		
	}
	
	public static enum PacketType
	{
		TileEntity, 
		ItemInventoryGui,
		Custom;
	}
	
	public static class ModularPacket
	{
		DataOutputStream stream;
		ByteArrayOutputStream bytes;
		
		/**
		 * Mostly used for Custom Packets. That simply allow that you can send
		 * everything that you want. At least that the reciver is declared!
		 */
		public ModularPacket(SpmodMod par0, PacketType type, String key)
		{
			this(par0, (byte) type.ordinal());
			this.injectString(key);
		}
		
		private ModularPacket(SpmodMod mod, int dimID, int x, int y, int z)
		{
			this(mod, (byte) PacketType.TileEntity.ordinal());
			InjectNumbers(dimID, x, y, z);
		}
		
		private ModularPacket(SpmodMod mod, EntityPlayer par1)
		{
			this(mod, (byte)PacketType.ItemInventoryGui.ordinal());
			InjectNumber(par1.worldObj.provider.dimensionId);
			injectString(par1.username);
		}
		
		private ModularPacket(SpmodMod par0, byte type)
		{
			bytes = new ByteArrayOutputStream();
			stream = new DataOutputStream(bytes);
			this.injectString(par0.getName());
			this.InjectNumber(type);
		}
		
		public ModularPacket InjectNBT(NBTBase par1)
		{
			try
			{
				NBTBase.writeNamedTag(par1, stream);
			}
			catch(Exception e)
			{
				FMLLog.getLogger().info("Error Injecting NBTData");
			}
			return this;
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
				packet = injectString(cu);
			}
			return packet;
		}
		
		public ModularPacket injectString(String par1)
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
		
		public ModularPacket injectBoolean(boolean par1)
		{
			try
			{
				stream.writeBoolean(par1);
				return this;
			}
			catch (Exception e)
			{
				FMLLog.getLogger().info("Error injecting Data");
				return this;
			}
		}
		
		public ModularPacket InjectBooleans(boolean... par1)
		{
			ModularPacket packet = this;
			for (boolean cu : par1)
			{
				packet = injectBoolean(cu);
			}
			return packet;
		}
		
		public Packet250CustomPayload finishPacket()
		{
			return new Packet250CustomPayload("Spmod", bytes.toByteArray());
		}
	}
	
}
