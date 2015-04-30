package speiger.src.spmodapi.common.items.trades;

import java.io.DataInput;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.village.MerchantRecipe;
import speiger.src.api.common.data.packets.IPacketReciver;
import speiger.src.api.common.data.packets.SpmodPacketHelper;
import speiger.src.api.common.data.packets.SpmodPacketHelper.ModularPacket;
import speiger.src.api.common.data.packets.SpmodPacketHelper.PacketType;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.items.trades.ItemRandomTrade.Trade;
import speiger.src.spmodapi.common.util.TickHelper;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class TradePacket implements IPacketReciver
{
	public static TradePacket instance = new TradePacket();
	
	public TradePacket()
	{
		SpmodPacketHelper.getHelper().registerPacketReciver(this);
	}

	@Override
	public void recivePacket(DataInput par1)
	{
		try
		{
			ItemRandomTrade.recipeList.clear();
			NBTTagCompound nbt = (NBTTagCompound)NBTBase.func_130104_b(par1, 0);
			NBTTagList list = nbt.getTagList("TradeList");
			for(int i = 0;i<list.tagCount();i++)
			{
				NBTTagCompound data = (NBTTagCompound)list.tagAt(i);
				int id = data.getInteger("TradeID");
				MerchantRecipe recipe = new MerchantRecipe(data.getCompoundTag("Trade"));
				ItemRandomTrade.recipeList.add(new Trade(recipe, id));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public String identifier()
	{
		return "Trade Packet";
	}
	
	public void sendPacketToClient(EntityPlayer par1)
	{
		if(!ItemRandomTrade.secondTry)
		{
			ItemRandomTrade.secondTry = true;
			TickHelper.loadRecipes(par1);
		}
		List<Trade> trades = ItemRandomTrade.recipeList;
		NBTTagList list = new NBTTagList();
		for(int i = 0;i<trades.size();i++)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			Trade trade = trades.get(i);
			nbt.setInteger("TradeID", trade.getMetadata());
			nbt.setCompoundTag("Trade", trade.getTrade().writeToTags());
			list.appendTag(nbt);
			if(list.tagCount() > 50)
			{
				NBTTagCompound data = new NBTTagCompound();
				data.setTag("TradeList", list);
				list = new NBTTagList();
				ModularPacket packet = new ModularPacket(SpmodAPI.instance, PacketType.Custom, identifier());
				packet.InjectNBT(data);
				PacketDispatcher.sendPacketToPlayer(packet.finishPacket(), (Player)par1);
			}
		}
		
		NBTTagCompound data = new NBTTagCompound();
		data.setTag("TradeList", list);
		ModularPacket packet = new ModularPacket(SpmodAPI.instance, PacketType.Custom, identifier());
		packet.InjectNBT(data);
		PacketDispatcher.sendPacketToPlayer(packet.finishPacket(), (Player)par1);
	}

	
}
