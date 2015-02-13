package speiger.src.api.common.world.tiles.interfaces;

import net.minecraft.util.ResourceLocation;

public enum HopperType
{
	Items(new ResourceLocation("TinyModularThings:textures/models/transport/ModelItemsHopper.png"), new ResourceLocation("TinyModularThings:textures/models/transport/ModelAdvItemsHopper.png")),
	Fluids(new ResourceLocation("TinyModularThings:textures/models/transport/ModelFluidHopper.png"), new ResourceLocation("TinyModularThings:textures/models/transport/ModelAdvFluidHopper.png")),
	Energy(new ResourceLocation("TinyModularThings:textures/models/transport/ModelEnergyHopper.png"), new ResourceLocation("TinyModularThings:textures/models/transport/ModelAdvEnergyHopper.png")),
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
