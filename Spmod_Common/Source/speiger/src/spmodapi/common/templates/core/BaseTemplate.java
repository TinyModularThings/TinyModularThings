package speiger.src.spmodapi.common.templates.core;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import speiger.src.api.common.world.blocks.BlockPosition;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.api.common.world.tiles.interfaces.IAcceptor.AcceptorType;
import speiger.src.api.common.world.utils.IStructureBox;
import speiger.src.spmodapi.common.interfaces.IAdvTile;
import speiger.src.spmodapi.common.util.data.StructureStorage;

public abstract class BaseTemplate implements ITemplate
{
	public StructureStorage storage = StructureStorage.instance;
	
	public static BlockStack item;
	public static BlockStack fluid;
	public static BlockStack energy;
	private BlockPosition target;
	private ArrayList<IStructureBox> structure = new ArrayList<IStructureBox>();
	private int[] maxInterfaces = new int[3];
	
	public BaseTemplate(IAdvTile par1)
	{
		target = par1.getPosition();
	}
	
	public BlockStack getItemInterface()
	{
		if(item == null)
		{
			item = new BlockStack();
		}
		return item;
	}
	
	public BlockStack getFluidInterface()
	{
		if(fluid == null)
		{
			fluid = new BlockStack();
		}
		return fluid;
	}
	
	public BlockStack getEnergyInterface()
	{
		if(energy == null)
		{
			energy = new BlockStack();
		}
		return energy;
	}
	
	public boolean isInterface(BlockStack toCompare)
	{
		if(toCompare.match(getItemInterface()) || toCompare.match(getFluidInterface()) || toCompare.match(getEnergyInterface()))
		{
			return true;
		}
		return false;
	}
	
	public boolean isItemInterface(BlockStack toCompare)
	{
		return toCompare.match(getItemInterface());
	}
	
	public boolean isFluidInterface(BlockStack toCompare)
	{
		return toCompare.match(getFluidInterface());
	}
	
	public boolean isEnergyInterface(BlockStack toCompare)
	{
		return toCompare.match(getEnergyInterface());
	}
	
	public int getInterfaceType(BlockStack toCompare)
	{
		if(this.isItemInterface(toCompare))
		{
			return AcceptorType.Items.ordinal();
		}
		if(this.isFluidInterface(toCompare))
		{
			return AcceptorType.Fluids.ordinal();
		}
		if(this.isEnergyInterface(toCompare))
		{
			return AcceptorType.Energy.ordinal();
		}
		return -1;
	}
	
	public boolean colideCheck()
	{
		boolean first = !storage.isAnyInteractionBoxColiding(target.getWorld(), structure.toArray(new IStructureBox[structure.size()]));
		boolean second = !storage.isAnyUpdateBoxColiding(target.getWorld(), structure.toArray(new IStructureBox[structure.size()]));
		return first && second;
	}

	@Override
	public BlockPosition getCore()
	{
		return target;
	}

	@Override
	public void onBreaking()
	{
		for(IStructureBox box : structure)
		{
			storage.removeInteractionBox(target.getWorld(), box);
			storage.removeUpdateBoxes(target.getWorld(), box);
		}
		structure.clear();
	}

	@Override
	public void onUnload()
	{
		onBreaking();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		
	}
	
	public void addStructure(IStructureBox...par1)
	{
		for(IStructureBox box : par1)
		{
			this.structure.add(box);
		}
		this.storage.registerInteractionBoxes(target.getWorld(), par1);
		this.storage.registerUpdateBoxes(target.getWorld(), par1);
	}
	
	@Override
	public void setMaxInterfaces(AcceptorType par1, int size)
	{
		this.maxInterfaces[par1.ordinal()] = size;
	}
	
	@Override
	public void setMaxInterfaces(int size)
	{
		for(AcceptorType type : AcceptorType.values())
		{
			this.setMaxInterfaces(type, size);
		}
	}
	
	public boolean isInterfaceAmountOk(AcceptorType par1, int size)
	{
		return maxInterfaces[par1.ordinal()] >= size;
	}
}
