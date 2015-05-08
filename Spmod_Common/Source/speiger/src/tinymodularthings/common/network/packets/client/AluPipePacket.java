package speiger.src.tinymodularthings.common.network.packets.client;

import java.io.DataInput;
import java.io.DataOutput;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import speiger.src.spmodapi.common.network.packets.base.PositionPacket;
import speiger.src.tinymodularthings.common.pipes.AluFluidExtractionPipe;
import buildcraft.transport.TileGenericPipe;
import cpw.mods.fml.relauncher.Side;

public class AluPipePacket extends PositionPacket
{
	int amount;
	boolean auto;
	
	public AluPipePacket()
	{
		
	}
	
	public AluPipePacket(TileEntity par1, int par2, boolean par3)
	{
		super(par1.getWorldObj(), par1.xCoord, par1.yCoord, par1.zCoord);
		amount = par2;
		auto = par3;
	}
	
	
	
	@Override
	public void readData(DataInput par1) throws Exception
	{
		super.readData(par1);
		amount = par1.readInt();
		auto = par1.readBoolean();
	}

	@Override
	public void writeData(DataOutput par1) throws Exception
	{
		super.writeData(par1);
		par1.writeInt(amount);
		par1.writeBoolean(auto);
	}

	@Override
	public void handlePacket(EntityPlayer par1, Side par2)
	{
		TileEntity tile = getTileEntity(par2);
		if(tile != null && tile instanceof TileGenericPipe)
		{
			TileGenericPipe pipe = (TileGenericPipe)tile;
			if(pipe != null && pipe.getPipe() instanceof AluFluidExtractionPipe)
			{
				AluFluidExtractionPipe alu = (AluFluidExtractionPipe)pipe.getPipe();
				alu.delay = amount;
				alu.continueless = auto;
			}
		}
	}
	
}
