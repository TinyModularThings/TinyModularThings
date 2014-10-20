package speiger.src.spmodapi.client.render.deko;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import speiger.src.spmodapi.client.model.ModelKyroka;
import speiger.src.spmodapi.common.blocks.deko.KyrokaTheFox;

public class RenderKyroka extends TileEntitySpecialRenderer
{
	
	public static ModelKyroka kyroka = new ModelKyroka();
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double d0, double d1, double d2, float f)
	{
		if(tile != null)
		{
			if(tile instanceof KyrokaTheFox)
			{
				KyrokaTheFox kyroka = (KyrokaTheFox)tile;
				renderKyroka(kyroka, d0, d1, d2);
			}
		}
	}
	
	public void renderKyroka(KyrokaTheFox par1, double x, double y, double z)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5f, (float) y + 1.5f, (float) z + 0.5f);
		
		this.bindTexture(par1.getTexture());
		switch(par1.getFacing())
		{
	    	case 2: GL11.glRotatef(0, 0.0F, 1.0F, 0.0F); break; 
	    	case 3: GL11.glRotatef(180, 0.0F, 1.0F, 0.0F); break;
	    	case 4: GL11.glRotatef(90, 0.0F, 1.0F, 0.0F); break;
	    	case 5: GL11.glRotatef(270, 0.0F, 1.0F, 0.0F); break;
		}
		GL11.glScalef(1.0F, 1F, 1F);
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
		kyroka.render(0.0625F, 0);
		GL11.glPopMatrix();
	}
	
}
