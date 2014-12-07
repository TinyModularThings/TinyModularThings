package speiger.src.tinymodularthings.client.render.machine;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
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
		
	
		if(par1.onRotation() > 360)
		{
			par1.rotation = 0;
		}
		
		GL11.glRotatef(par1.rotation, 0, 180, 0);
		model.renderRotation(0.0625F);
		
		GL11.glPopMatrix();
	}
	
}
