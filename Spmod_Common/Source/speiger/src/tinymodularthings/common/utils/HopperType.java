package speiger.src.tinymodularthings.common.utils;

import net.minecraft.util.ResourceLocation;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;

public enum HopperType
{
	Items(new ResourceLocation(TinyModularThingsLib.ModID.toLowerCase() + ":textures/models/transport/ModelItemsHopper.png"), new ResourceLocation(TinyModularThingsLib.ModID.toLowerCase() + ":textures/models/transport/ModelAdvItemsHopper.png")),
	Fluids(new ResourceLocation(TinyModularThingsLib.ModID.toLowerCase() + ":textures/models/transport/ModelFluidHopper.png"), new ResourceLocation(TinyModularThingsLib.ModID.toLowerCase() + ":textures/models/transport/ModelAdvFluidHopper.png")),
	Energy(new ResourceLocation(TinyModularThingsLib.ModID.toLowerCase() + ":textures/models/transport/ModelEnergyHopper.png"), new ResourceLocation(TinyModularThingsLib.ModID.toLowerCase() + ":textures/models/transport/ModelAdvEnergyHopper.png")),
	Nothing(null, null);
	
	ResourceLocation advTexture;
	ResourceLocation texture;
	
	private HopperType(ResourceLocation par1, ResourceLocation par2)
	{
		texture = par1;
		advTexture = par2;
	}
	
	public ResourceLocation getTexture(boolean adv)
	{
		if(adv)
		{
			return advTexture;
		}
		return texture;
	}
}
