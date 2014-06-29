package speiger.src.spmodapi.client.model;

import java.util.ArrayList;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelHelper
{
	//Models and renderer
	ArrayList<ModelRenderer[]> models = new ArrayList<ModelRenderer[]>();
	ArrayList<boolean[]> basicInfo = new ArrayList<boolean[]>();
	
	ArrayList<ModelPart> parts = new ArrayList<ModelPart>();
	
	ModelPart cuPart = null;
	
	public void startPart(boolean begin)
	{
		cuPart = new ModelPart(begin);
	}
	
	public void startModel(ModelBase par1, int x, int y)
	{
		cuPart.startModel(par1, x, y);
	}
	
	public void addBox(float oX, float oY, float oZ, int x, int y, int z)
	{
		cuPart.addBox(oX, oY, oZ, x, y, z);
	}
	
	public void setPosition(float x, float y, float z)
	{
		cuPart.setPosition(x, y, z);
	}
	
	public void setTextureSize(int x, int y)
	{
		cuPart.setTextureSize(x, y);
	}
	
	public void setTextureOffset(int x, int y)
	{
		cuPart.setTextureOffset(x, y);
	}
	
	public void finishPart()
	{
		cuPart.setRotation(0F, 0F, 0F);
		parts.add(cuPart);
		cuPart = null;
	}
	
	public void finishModel()
	{
		ModelRenderer[] model = new ModelRenderer[parts.size()];
		boolean[] first = new boolean[parts.size()];
		for(int i = 0;i<parts.size();i++)
		{
			ModelPart part = parts.get(i);
			model[i] = part.finishModelPart();
			first[i] = part.getMode();
		}
		models.add(model);
		basicInfo.add(first);
		parts.clear();
	}
	
	public void setMirrored()
	{
		cuPart.setMirrored();
	}
	
	public void renderStart(float par1, int b)
	{
		
		boolean[] allow = this.basicInfo.get(b);
		for(int i = 0;i<allow.length;i++)
		{
			if(allow[i])
			{
				this.models.get(b)[i].render(par1);
			}
		}
	}
	
	public void renderEnd(float par1, int b)
	{
		boolean[] allow = this.basicInfo.get(b);
		for(int i = 0;i<allow.length;i++)
		{
			if(!allow[i])
			{
				 this.models.get(b)[i].render(par1);
			}
		}
	}
	
	
	
	
	
	
	
	public static class ModelPart
	{
		ModelRenderer renderer = null;
		boolean Start;
		
		public ModelPart(boolean start)
		{
			Start = start;
		}
		
		public void startModel(ModelBase par1, int x, int y)
		{
			renderer = new ModelRenderer(par1, x, y);
		}
		
		public void setMirrored()
		{
			renderer.mirror = true;
		}
		
		public void addBox(float oX, float oY, float oZ, int x, int y, int z)
		{
			renderer.addBox(oX, oY, oZ, x, y, z);
		}
		
		public void setPosition(float x, float y, float z)
		{
			renderer.setRotationPoint(x, y, z);
		}
		
		public void setTextureSize(int x, int y)
		{
			renderer.setTextureSize(x, y);
		}
		
		public void setTextureOffset(int x, int y)
		{
			renderer.setTextureOffset(x, y);
		}
		
		public void setRotation(float x, float y, float z)
		{
			renderer.rotateAngleX = x;
			renderer.rotateAngleY = y;
			renderer.rotateAngleZ = z;
		}
		
		public ModelRenderer finishModelPart()
		{
			return renderer;
		}
		
		public boolean getMode()
		{
			return Start;
		}
	}
}
