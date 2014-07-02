package speiger.src.spmodapi.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelHanfSign extends ModelBase
{
  //fields
    ModelRenderer Shape1;
  
  public ModelHanfSign()
  {
    textureWidth = 32;
    textureHeight = 32;
    
      Shape1 = new ModelRenderer(this, 0, 0);
      Shape1.addBox(0F, 0F, 0F, 12, 10, 1);
      Shape1.setRotationPoint(-6F, 11F, 7F);
      Shape1.setTextureSize(32, 32);
      Shape1.mirror = true;
      setRotation(Shape1, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    Shape1.render(f5);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void render(float f)
  {
	  Shape1.render(f);
  }
  
  
  
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity f6)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, f6);
  }

}
