package speiger.src.spmodapi.common.templates.core;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import speiger.src.api.common.world.blocks.BlockPosition;
import speiger.src.api.common.world.tiles.interfaces.IAcceptor;
import speiger.src.api.common.world.tiles.interfaces.IAcceptor.AcceptorType;

public interface ITemplate
{
	public void setup(World world, int x, int y, int z, int facing);
	
	public void setupFacing(int facing);
	
	public void setMaxInterfaces(AcceptorType par1, int size);
	
	public void setMaxInterfaces(int size);
	
	public boolean match();
		
	public BlockPosition getCore();
	
	public void init();
	
	public void onBreaking();
	
	public void onUnload();
	
	public IAcceptor[] getInterfaces(AcceptorType par1, int size);
		
	public void readFromNBT(NBTTagCompound nbt);
	
	public void writeToNBT(NBTTagCompound nbt);
}
