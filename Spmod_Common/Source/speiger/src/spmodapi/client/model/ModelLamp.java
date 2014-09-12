package speiger.src.spmodapi.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelLamp extends ModelBase
{
	// fields
	ModelHelper models = new ModelHelper();
	
	public ModelLamp()
	{
		textureWidth = 64;
		textureHeight = 32;
		// FirstLamp
		models.startPart(true);
		models.startModel(this, 0, 0);
		models.addBox(0F, 0F, 0F, 16, 16, 16);
		models.setPosition(-8F, 8F, -8F);
		models.setTextureSize(64, 32);
		models.setMirrored();
		models.finishPart();
		models.finishModel();
		
		textureWidth = 64;
		textureHeight = 64;
		// RedPower2CageLamp
		
		models.startPart(false);
		models.startModel(this, 16, 51);
		models.addBox(0F, 0F, 0F, 12, 1, 12);
		models.setPosition(-6F, 23F, -6F);
		models.setTextureSize(64, 64);
		models.setMirrored();
		models.finishPart();
		models.startPart(true);
		models.startModel(this, 32, 0);
		models.addBox(0F, 0F, 0F, 8, 10, 8);
		models.setPosition(-4F, 13F, -4F);
		models.setTextureSize(64, 64);
		models.setMirrored();
		models.finishPart();
		models.startPart(false);
		models.startModel(this, 0, 0);
		models.addBox(0F, 0F, 0F, 1, 11, 1);
		models.setPosition(4F, 12F, 1F);
		models.setTextureSize(64, 64);
		models.setMirrored();
		models.finishPart();
		models.startPart(false);
		models.startModel(this, 0, 0);
		models.addBox(0F, 0F, 0F, 1, 11, 1);
		models.setPosition(4F, 12F, -2F);
		models.setTextureSize(64, 64);
		models.setMirrored();
		models.finishPart();
		models.startPart(false);
		models.startModel(this, 0, 0);
		models.addBox(0F, 0F, 0F, 1, 11, 1);
		models.setPosition(-5F, 12F, 1F);
		models.setTextureSize(64, 64);
		models.setMirrored();
		models.finishPart();
		models.startPart(false);
		models.startModel(this, 0, 0);
		models.addBox(0F, 0F, 0F, 1, 11, 1);
		models.setPosition(-5F, 12F, -2F);
		models.setTextureSize(64, 64);
		models.setMirrored();
		models.finishPart();
		models.startPart(false);
		models.startModel(this, 0, 0);
		models.addBox(0F, 0F, 0F, 1, 11, 1);
		models.setPosition(1F, 12F, 4F);
		models.setTextureSize(64, 64);
		models.setMirrored();
		models.finishPart();
		models.startPart(false);
		models.startModel(this, 0, 0);
		models.addBox(0F, 0F, 0F, 1, 11, 1);
		models.setPosition(-2F, 12F, 4F);
		models.setTextureSize(64, 64);
		models.setMirrored();
		models.finishPart();
		models.startPart(false);
		models.startModel(this, 0, 0);
		models.addBox(0F, 0F, 0F, 1, 11, 1);
		models.setPosition(1F, 12F, -5F);
		models.setTextureSize(64, 64);
		models.setMirrored();
		models.finishPart();
		models.startPart(false);
		models.startModel(this, 0, 0);
		models.addBox(0F, 0F, 0F, 1, 11, 1);
		models.setPosition(-2F, 12F, -5F);
		models.setTextureSize(64, 64);
		models.setMirrored();
		models.finishPart();
		models.startPart(false);
		models.startModel(this, 42, 48);
		models.addBox(0F, 0F, 0F, 10, 1, 1);
		models.setPosition(-5F, 21F, 4F);
		models.setTextureSize(64, 64);
		models.setMirrored();
		models.finishPart();
		models.startPart(false);
		models.startModel(this, 42, 48);
		models.addBox(0F, 0F, 0F, 10, 1, 1);
		models.setPosition(-5F, 18F, 4F);
		models.setTextureSize(64, 64);
		models.setMirrored();
		models.finishPart();
		models.startPart(false);
		models.startModel(this, 42, 48);
		models.addBox(0F, 0F, 0F, 10, 1, 1);
		models.setPosition(-5F, 15F, 4F);
		models.setTextureSize(64, 64);
		models.setMirrored();
		models.finishPart();
		models.startPart(false);
		models.startModel(this, 42, 48);
		models.addBox(0F, 0F, 0F, 10, 1, 1);
		models.setPosition(-5F, 21F, 4F);
		models.setTextureSize(64, 64);
		models.setMirrored();
		models.finishPart();
		models.startPart(false);
		models.startModel(this, 42, 48);
		models.addBox(0F, 0F, 0F, 10, 1, 1);
		models.setPosition(-5F, 15F, -5F);
		models.setTextureSize(64, 64);
		models.setMirrored();
		models.finishPart();
		models.startPart(false);
		models.startModel(this, 42, 48);
		models.addBox(0F, 0F, 0F, 10, 1, 1);
		models.setPosition(-5F, 18F, -5F);
		models.setTextureSize(64, 64);
		models.setMirrored();
		models.finishPart();
		models.startPart(false);
		models.startModel(this, 42, 48);
		models.addBox(0F, 0F, 0F, 10, 1, 1);
		models.setPosition(-5F, 21F, -5F);
		models.setTextureSize(64, 64);
		models.setMirrored();
		models.finishPart();
		models.startPart(false);
		models.startModel(this, 46, 21);
		models.addBox(0F, 0F, 0F, 1, 1, 8);
		models.setPosition(-5F, 21F, -4F);
		models.setTextureSize(64, 64);
		models.setMirrored();
		models.finishPart();
		models.startPart(false);
		models.startModel(this, 46, 21);
		models.addBox(0F, 0F, 0F, 1, 1, 8);
		models.setPosition(-5F, 18F, -4F);
		models.setTextureSize(64, 64);
		models.setMirrored();
		models.finishPart();
		models.startPart(false);
		models.startModel(this, 46, 21);
		models.addBox(0F, 0F, 0F, 1, 1, 8);
		models.setPosition(-5F, 15F, -4F);
		models.setTextureSize(64, 64);
		models.setMirrored();
		models.finishPart();
		models.startPart(false);
		models.startModel(this, 46, 21);
		models.addBox(0F, 0F, 0F, 1, 1, 8);
		models.setPosition(4F, 21F, -4F);
		models.setTextureSize(64, 64);
		models.setMirrored();
		models.finishPart();
		models.startPart(false);
		models.startModel(this, 46, 21);
		models.addBox(0F, 0F, 0F, 1, 1, 8);
		models.setPosition(4F, 18F, -4F);
		models.setTextureSize(64, 64);
		models.setMirrored();
		models.finishPart();
		models.startPart(false);
		models.startModel(this, 46, 21);
		models.addBox(0F, 0F, 0F, 1, 1, 8);
		models.setPosition(4F, 15F, -4F);
		models.setTextureSize(64, 64);
		models.setMirrored();
		models.finishPart();
		models.startPart(false);
		models.startModel(this, 46, 21);
		models.addBox(0F, 0F, 0F, 1, 1, 8);
		models.setPosition(-2F, 12F, -4F);
		models.setTextureSize(64, 64);
		models.setMirrored();
		models.finishPart();
		models.startPart(false);
		models.startModel(this, 46, 21);
		models.addBox(0F, 0F, 0F, 1, 1, 8);
		models.setPosition(1F, 12F, -4F);
		models.setTextureSize(64, 64);
		models.setMirrored();
		models.finishPart();
		models.startPart(false);
		models.startModel(this, 0, 32);
		models.addBox(0F, 0F, 0F, 8, 1, 1);
		models.setPosition(-4F, 12F, 1F);
		models.setTextureSize(64, 64);
		models.setMirrored();
		models.finishPart();
		models.startPart(false);
		models.startModel(this, 0, 32);
		models.addBox(0F, 0F, 0F, 8, 1, 1);
		models.setPosition(-4F, 12F, -2F);
		models.setTextureSize(64, 64);
		models.setMirrored();
		models.finishPart();
		models.finishModel();
		
	}
	
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		
	}
	
	public void render(float f5, int type)
	{
		models.renderStart(f5, type);
	}
	
	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
	
	public void renderAfter(float f5, int type)
	{
		models.renderEnd(f5, type);
	}
	
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity e)
	{
		super.setRotationAngles(f, f1, f2, f3, f4, f5, e);
	}
	
}
