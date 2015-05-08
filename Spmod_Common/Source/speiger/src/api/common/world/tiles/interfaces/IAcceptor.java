package speiger.src.api.common.world.tiles.interfaces;

import net.minecraft.tileentity.TileEntity;
import speiger.src.api.common.world.blocks.BlockPosition;
import speiger.src.api.common.world.blocks.BlockStack;

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
	
	BlockPosition getPosition();
	
	boolean isBlockPresent(BlockStack par1);
	
	int getSideFromBlock(BlockStack par1);
	
	boolean isTilePressent(Class par1);
	
	public <T> T getTileEntity(Class<T> par1);
	
	public int getSideFromTile(Class par1);
	
	public boolean hasMaster();
	
	public boolean ImMaster(TileEntity par1);
	
	public void setMaster(TileEntity par1);
	
	public void removeMaster();
	
}
