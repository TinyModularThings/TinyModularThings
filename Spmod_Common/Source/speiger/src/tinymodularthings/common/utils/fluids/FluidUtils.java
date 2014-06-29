package speiger.src.tinymodularthings.common.utils.fluids;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class FluidUtils
{
	public static final ResourceLocation BlockTexture = TextureMap.locationBlocksTexture;
	
	public static ResourceLocation getSheetFromFluid(FluidStack par1)
	{
		if (par1 == null)
		{
			return BlockTexture;
		}
		return getSheetFromFluid(par1.getFluid());
	}
	
	public static ResourceLocation getSheetFromFluid(Fluid par1)
	{
		return BlockTexture;
	}
}
