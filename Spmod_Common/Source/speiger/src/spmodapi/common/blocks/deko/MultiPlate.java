package speiger.src.spmodapi.common.blocks.deko;

import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import speiger.src.api.items.plates.PlateManager;
import speiger.src.spmodapi.common.tile.TileFacing;

public class MultiPlate extends TileFacing
{
	public String identity = "";
	
	@Override
	public boolean isSixSidedFacing()
	{
		return true;
	}
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		return null;
	}
	
	public void setIdentity(String identity)
	{
		this.identity = identity;
	}
	
	public String getIdentity()
	{
		return identity;
	}
	
	@Override
	public boolean isSixSidedRotation()
	{
		return false;
	}
	
	public ResourceLocation getTexture()
	{
		ResourceLocation texture = PlateManager.plates.getTexture(identity);
		if (texture == null)
		{
			texture = new ResourceLocation("Error");
		}
		return texture;
	}
	
}
