package speiger.src.spmodapi.common.blocks.gas;

import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import speiger.src.spmodapi.common.blocks.cores.SpmodBlockBase;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;

public class BlockAnimalGas extends SpmodBlockBase implements IFluidBlock
{

	public BlockAnimalGas(int par1)
	{
		super(par1, Material.air);
	}

	@Override
	public Fluid getFluid()
	{
		return APIUtils.animalGas;
	}

	@Override
	public FluidStack drain(World world, int x, int y, int z, boolean doDrain)
	{
		int meta = world.getBlockMetadata(x, y, z);
		if(doDrain)
		{
			world.setBlockToAir(x, y, z);
		}
		
		return new FluidStack(getFluid(), meta*100);
	}

	@Override
	public boolean canDrain(World world, int x, int y, int z)
	{
		return true;
	}
	
	
	
	
}
