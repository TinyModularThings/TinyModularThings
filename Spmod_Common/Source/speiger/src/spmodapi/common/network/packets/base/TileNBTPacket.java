package speiger.src.spmodapi.common.network.packets.base;

import java.io.DataInput;
import java.io.DataOutput;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import speiger.src.api.common.data.packets.SpmodPacketHelper.SpmodPacket;
import speiger.src.api.common.data.packets.receivers.ITilePacketAcceptor;
import cpw.mods.fml.relauncher.Side;

public class TileNBTPacket extends PositionPacket
{
	public NBTTagCompound nbt = new NBTTagCompound();
	
	
	public TileNBTPacket()
	{
	}
	
	public TileNBTPacket(TileEntity par1)
	{
		super(par1.getWorldObj(), par1.xCoord, par1.yCoord, par1.zCoord);
	}
	
	public void setNBT(NBTTagCompound par1)
	{
		nbt = par1;
	}
	
	@Override
	public void readData(DataInput par1) throws Exception
	{
		super.readData(par1);
		nbt = (NBTTagCompound)NBTBase.readNamedTag(par1);
	}

	@Override
	public void writeData(DataOutput par1) throws Exception
	{
		super.writeData(par1);
		NBTBase.writeNamedTag(nbt, par1);
	}

	@Override
	public void handlePacket(EntityPlayer par1, Side par2)
	{
		TileEntity tile = this.getTileEntity(par2);
		if(tile != null && tile instanceof ITilePacketAcceptor)
		{
			((ITilePacketAcceptor)tile).onSpmodPacket(new SpmodPacket(this, par1, par2));
		}
	}
	
	public NBTTagCompound getData()
	{
		return nbt;
	}
	
}
