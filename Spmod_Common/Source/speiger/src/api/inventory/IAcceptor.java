package speiger.src.api.inventory;

import net.minecraft.tileentity.TileEntity;
import speiger.src.api.blocks.BlockStack;

public interface IAcceptor
{
	public static enum AcceptorType
	{
		Items, Fluids, Energy;
	}
	
	AcceptorType getType();
	
	// Check Fuction if the interface Block is this Hidding Block!
	boolean isBlock(BlockStack par1);
	
	// Set Block Function
	void setBlock(BlockStack par1);
	
	// Can be null!
	BlockStack getBlock();
	
	void targetLeave(TileEntity tile);
}
