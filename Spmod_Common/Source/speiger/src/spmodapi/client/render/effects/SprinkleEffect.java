package speiger.src.spmodapi.client.render.effects;

import net.minecraft.client.particle.EntityRainFX;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SprinkleEffect extends EntityRainFX
{

	public SprinkleEffect(World par1World, double par2, double par4, double par6)
	{
		super(par1World, par2, par4, par6);
		this.setSize(1F, 1F);
		this.particleScale = 4F;
		this.particleMaxAge = 50;
	}
	
	public void setSize(float par1, float par2)
	{
		super.setSize(par1, par2);
	}
	
}
