package speiger.src.api.common.data.packets.receivers;

import net.minecraft.item.ItemStack;
import speiger.src.api.common.data.packets.SpmodPacketHelper.SpmodPacket;

public interface IItemPacketAcceptor
{
	public void onSpmodPacket(SpmodPacket par1, ItemStack par2);
}
