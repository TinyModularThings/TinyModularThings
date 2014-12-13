package speiger.src.spmodapi.client.render.deko;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import speiger.src.spmodapi.client.model.ModelLamp;
import speiger.src.spmodapi.common.blocks.deko.TileLamp;
import speiger.src.spmodapi.common.blocks.deko.TileLamp.EnumLampType;
import speiger.src.spmodapi.common.enums.EnumColor.SpmodColor;

public class RenderLamp extends TileEntitySpecialRenderer
{
	
	public static ModelLamp lamp = new ModelLamp();
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f)
	{
		if (tileentity != null)
		{
			if (tileentity instanceof TileLamp)
			{
				renderLamp((TileLamp) tileentity, d0, d1, d2);
			}
		}
	}
	
	private void renderLamp(TileLamp Lamp, double x, double y, double z)
	{
		GL11.glPushMatrix();
		try
		{
			
			float f = 1.0F;
			GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
			EnumLampType type = Lamp.getType();
			
			if (type == null)
			{
				GL11.glPopMatrix();
				return;
			}
			if (type.hasFacing())
			{
				int facing = Lamp.getFacing();
				
				switch (facing)
				{
					case 0:
						break;
					case 1:
						GL11.glRotatef(180, 1, 0, 0);
						break;
					case 2:
						GL11.glRotatef(90, 1, 0, 0);
						break;
					case 3:
						GL11.glRotatef(-90, 1, 0, 0);
						break;
					case 4:
						GL11.glRotatef(-90, 0, 0, 1);
						break;
					case 5:
						GL11.glRotatef(90, 0, 0, 1);
						
				}
				
				GL11.glRotatef(180, 1, 0, 0);
			}
			
			if(type.getTexture() != null)
			{
				this.bindTexture(type.getTexture());
			}
			GL11.glTranslatef(0, -f, 0);
			
			SpmodColor color = Lamp.getColor();
			if (color != null)
			{
				GL11.glColor4d(color.red, color.green, color.blue, 1.0D);
			}
			
			lamp.render(0.0625F, type.getRenderType());
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			lamp.renderAfter(0.0625F, type.getRenderType());
			
		}
		catch(Exception e)
		{
		}
		GL11.glPopMatrix();
	}
	
}
