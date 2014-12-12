package speiger.src.tinymodularthings.client.render.machine;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.client.models.machine.WaterSpenderModel;
import speiger.src.tinymodularthings.common.blocks.machine.MachineWaterSpender;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;

public class RenderWaterSpender extends TileEntitySpecialRenderer
{
	public static ResourceLocation texture = new ResourceLocation(TinyModularThingsLib.ModID.toLowerCase() + ":textures/models/machine/WaterSpender.png");
	public static WaterSpenderModel model = new WaterSpenderModel();
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f)
	{
		if(tileentity != null && tileentity instanceof MachineWaterSpender)
		{
			renderWaterSpender((MachineWaterSpender)tileentity, d0, d1, d2);
		}
	}
	
	public void renderWaterSpender(MachineWaterSpender par1, double x, double y, double z)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5f, (float) y + 1.5f, (float) z + 0.5f);
		this.bindTexture(texture);
		
		
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
		model.renderBase(0.0625F);
		float rotation = par1.onRotation();
		float oppo = rotation + 180 % 360;
		if(par1.speed > 0.0D && par1.tank.getFluidAmount() > 50)
		{
			TinyModularThings.core.spawnParticle(par1.getWorldObj(), par1.xCoord+0.5D, par1.yCoord+0.25D, par1.zCoord+0.5D, par1.speed, rotation);
		}
		GL11.glRotatef(rotation, 0, 180, 0);
		model.renderRotation(0.0625F);
		
		GL11.glPopMatrix();
	}
	
}
