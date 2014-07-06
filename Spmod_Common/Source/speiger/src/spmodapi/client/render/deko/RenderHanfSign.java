package speiger.src.spmodapi.client.render.deko;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import speiger.src.spmodapi.client.model.ModelHanfSign;
import speiger.src.spmodapi.common.blocks.deko.MultiPlate;

public class RenderHanfSign extends TileEntitySpecialRenderer
{
	public ModelHanfSign sign = new ModelHanfSign();
	
	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8)
	{
		renderSign((MultiPlate)var1, var2, var4, var6, var8);
	}
	
	public void renderSign(MultiPlate par0, double par1, double par2, double par3, float par4)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef((float) par1 + 0.5f, (float) par2 + 1.5f, (float) par3 + 0.5f);
		this.bindTexture(par0.getTexture());
		
		switch(par0.getFacing())
		{
			case 0: 
				GL11.glRotatef(-90, 1.0F, 0.0F, 0.0F); 
				GL11.glTranslatef(0F, 1F, -1F); 
				switch(par0.getRotation())
				{
					case 0:
						GL11.glRotatef(180, 0.0F, 0.0F, 1.0F);
						GL11.glTranslatef(0.0F, 2.0F, 0.0F);
		
						break;
					case 1:
						GL11.glRotatef(90, 0.0F, 0.0F, 1.0F);
						GL11.glTranslatef(-1.0F, 1.0F, 0.0F);
						break;
					case 2:
						GL11.glRotatef(0, 1.0F, 0.0F, 0.0F);
						break;
					case 3:
						GL11.glRotatef(270, 0.0F, 0.0F, 1.0F);
						GL11.glTranslatef(1.0F, 1.0F, 0.0F);
						break;
				}
				break;
			case 1:
				GL11.glRotatef(90, -1.0F, 0.0F, 0.0F); 
				GL11.glTranslatef(0F, 1F, 1F); 
				switch(par0.getRotation())
				{
					case 0:
						GL11.glRotatef(-180, 1.0F, 0.0F, 0.0F);
						GL11.glTranslatef(0F, 2F, 2F); 
						break;
					case 1:
						GL11.glRotatef(180, -1.0F, 1.0F, 0.0F);
						GL11.glTranslatef(1F, 1F, 2F); 

						break;
					case 2:
						GL11.glRotatef(180, 0.0F, 1.0F, 0.0F);
						GL11.glTranslatef(0F, 0F, 2F); 
						break;
					case 3:
						GL11.glRotatef(180, 1.0F, 1.0F, 0.0F);
						GL11.glTranslatef(-1F, 1F, 2F); 
						break;
				}
				break;
	    	case 2: 
	    		GL11.glRotatef(0, 0.0F, 1.0F, 0.0F); 
	    		switch(par0.getRotation())
	    		{
	    			case 0:
	    				break;
	    			case 1:
	    				GL11.glRotatef(90, 0.0F, 0.0F, -1.0F);
	    				GL11.glTranslatef(1F, 1F, 0F);
	    				break;
	    			case 2:
	    				GL11.glRotatef(180, 0.0F, 0.0F, 1.0F);
	    				GL11.glTranslatef(0F, 2F, 0F);
	    				break;
	    			case 3:
	    				GL11.glRotatef(270, 0.0F, 0.0F, -1.0F);
	    				GL11.glTranslatef(-1F, 1F, 0F);
	    				break;
	    		}
	    		break; 
	    	case 3:
	    		GL11.glRotatef(180, 0.0F, 1.0F, 0.0F); 
	    		switch(par0.getRotation())
	    		{
	    			case 0:
	    				break;
	    			case 1:
	    				GL11.glRotatef(90, 0.0F, 0.0F, -1.0F);
	    				GL11.glTranslatef(1F, 1F, 0F);
	    				break;
	    			case 2:
	    				GL11.glRotatef(180, 0.0F, 0.0F, 1.0F);
	    				GL11.glTranslatef(0F, 2F, 0F);
	    				break;
	    			case 3:
	    				GL11.glRotatef(270, 0.0F, 0.0F, -1.0F);
	    				GL11.glTranslatef(-1F, 1F, 0F);
	    				break;
	    		}
	    		break;
	    	case 4: 
	    		GL11.glRotatef(90, 0.0F, 1.0F, 0.0F); 
	    		switch(par0.getRotation())
	    		{
	    			case 0:
	    				break;
	    			case 1:
	    				GL11.glRotatef(90, 0.0F, 0.0F, -1.0F);
	    				GL11.glTranslatef(1F, 1F, 0F);
	    				break;
	    			case 2:
	    				GL11.glRotatef(180, 0.0F, 0.0F, 1.0F);
	    				GL11.glTranslatef(0F, 2F, 0F);
	    				break;
	    			case 3:
	    				GL11.glRotatef(270, 0.0F, 0.0F, -1.0F);
	    				GL11.glTranslatef(-1F, 1F, 0F);
	    				break;
	    		}
	    		break;
	    	case 5: 
	    		GL11.glRotatef(270, 0.0F, 1.0F, 0.0F); 
	    		switch(par0.getRotation())
	    		{
	    			case 0:
	    				break;
	    			case 1:
	    				GL11.glRotatef(90, 0.0F, 0.0F, -1.0F);
	    				GL11.glTranslatef(1F, 1F, 0F);
	    				break;
	    			case 2:
	    				GL11.glRotatef(180, 0.0F, 0.0F, 1.0F);
	    				GL11.glTranslatef(0F, 2F, 0F);
	    				break;
	    			case 3:
	    				GL11.glRotatef(270, 0.0F, 0.0F, -1.0F);
	    				GL11.glTranslatef(-1F, 1F, 0F);
	    				break;
	    		}
	    		break;
		}
		GL11.glScalef(1.0F, 1F, 1F);
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
		sign.render(0.0625F);
		GL11.glPopMatrix();
	}
	
}
