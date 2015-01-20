package speiger.src.spmodapi.common.templates;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import speiger.src.api.common.world.blocks.BlockPosition;

public interface ITemplate
{
	public void setup(World world, int x, int y, int z, int facing);
	
	public void setupFacing(int facing);
	
	public boolean match();
	
	public int getTotalPatternSize();
	
	public BlockPosition getCore();
	
	public void onBreaking();
	
	public void onUnload();
		
	public void readFromNBT(NBTTagCompound nbt);
	
	public void writeToNBT(NBTTagCompound nbt);
}
