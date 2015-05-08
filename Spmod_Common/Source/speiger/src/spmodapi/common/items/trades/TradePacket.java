package speiger.src.spmodapi.common.items.trades;

import java.io.DataInput;
import java.io.DataOutput;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.village.MerchantRecipe;
import speiger.src.api.common.data.packets.ISpmodPacket;
import speiger.src.spmodapi.common.items.trades.ItemRandomTrade.Trade;
import cpw.mods.fml.relauncher.Side;

public class TradePacket implements ISpmodPacket
{
	Trade[] trades;
	boolean first;
	
	public TradePacket()
	{
		
	}
	
	public TradePacket(boolean par1, Trade...par2)
	{
		first = par1;
		trades = par2;
	}
	
	@Override
	public void readData(DataInput par1) throws Exception
	{
		first = par1.readBoolean();
		int size = par1.readInt();
		trades = new Trade[size];
		for(int i = 0;i<size;i++)
		{
			int meta = par1.readInt();
			trades[i] = new Trade(new MerchantRecipe((NBTTagCompound)NBTBase.readNamedTag(par1)), meta);
		}
	}

	@Override
	public void writeData(DataOutput par1) throws Exception
	{
		par1.writeBoolean(first);
		par1.writeInt(trades.length);
		for(Trade trade : trades)
		{
			par1.writeInt(trade.getMetadata());
			NBTTagCompound data = trade.getTrade().writeToTags();
			data.writeNamedTag(data, par1);
		}
	}

	@Override
	public void handlePacket(EntityPlayer par1, Side par2)
	{
		if(first)
		{
			ItemRandomTrade.recipeList.clear();
		}
		for(Trade trade : trades)
		{
			ItemRandomTrade.recipeList.add(trade);
		}
	}
	
	
}
