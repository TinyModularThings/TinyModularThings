package speiger.src.tinymodularthings.common.utils.fluids;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidTank;

public class TinyFluidTank extends FluidTank
{
	private String name;
	
	public TinyFluidTank(String TankName, int capacity, TileEntity tile)
	{
		super(capacity);
		this.tile = tile;
		name = TankName;
	}
	
	public String getName()
	{
		if (name == null)
		{
			return "Unamed Tank";
		}
		return name;
	}
	
	@Override
	public FluidTank readFromNBT(NBTTagCompound nbt)
	{
		if (nbt.hasKey(name))
		{
			NBTTagCompound tank = nbt.getCompoundTag(name);
			super.readFromNBT(tank);
		}
		return this;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		NBTTagCompound tank = new NBTTagCompound();
		
		super.writeToNBT(tank);
		
		nbt.setCompoundTag(name, tank);
		
		return nbt;
	}
	
}
