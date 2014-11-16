package speiger.src.spmodapi.common.fluids.gas;

import net.minecraft.util.Icon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.util.TextureEngine;

public class FluidAnimalGas extends Fluid
{

	public FluidAnimalGas()
	{
		super("animalgas");
		FluidRegistry.registerFluid(this);
	}

	@Override
	public Icon getStillIcon()
	{
		return TextureEngine.getTextures().getTexture(APIBlocks.animalGas);
	}

	@Override
	public Icon getFlowingIcon()
	{
		return TextureEngine.getTextures().getTexture(APIBlocks.animalGas);	
	}
	
	
}
