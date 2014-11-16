package speiger.src.spmodapi.common.material;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class GasMaterial extends Material
{

	public GasMaterial()
	{
		super(MapColor.airColor);
		this.setReplaceable();
		this.setBurning();
		this.setNoPushMobility();
	}

	@Override
	public boolean isLiquid()
	{
		return true;
	}

	@Override
	public boolean isSolid()
	{
		return false;
	}

	@Override
	public boolean blocksMovement()
	{
		return false;
	}

	@Override
	public boolean isOpaque()
	{
		return false;
	}
	
}
