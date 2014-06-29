package speiger.src.tinymodularthings.client.models.transport;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.ForgeDirection;

public class ModelTinyHopper extends ModelBase
{
	// fields
	ModelRenderer Top;
	ModelRenderer Kanal;
	ModelRenderer Bottom;
	ModelRenderer Seite_Back;
	ModelRenderer Seite_Front;
	ModelRenderer Seite_Rechts;
	ModelRenderer Seite_Links;
	ModelRenderer Kanal2;
	
	public ModelTinyHopper()
	{
		textureWidth = 64;
		textureHeight = 64;
		
		Top = new ModelRenderer(this, 0, 0);
		Top.addBox(0F, 0F, 0F, 16, 6, 16);
		Top.setRotationPoint(-8F, 24F, 2F);
		Top.setTextureSize(64, 32);
		Top.mirror = true;
		setRotation(Top, 1.570796F, 0F, 0F);
		Kanal = new ModelRenderer(this, 0, 24);
		Kanal.addBox(0F, 0F, 0F, 3, 6, 3);
		Kanal.setRotationPoint(-1.5F, 17.5F, -7F);
		Kanal.setTextureSize(64, 32);
		Kanal.mirror = true;
		setRotation(Kanal, 1.570796F, 0F, 0F);
		Bottom = new ModelRenderer(this, 16, 24);
		Bottom.addBox(0F, 0F, 0F, 10, 1, 10);
		Bottom.setRotationPoint(-5F, 21F, -8F);
		Bottom.setTextureSize(64, 32);
		Bottom.mirror = true;
		setRotation(Bottom, 1.570796F, 0F, 0F);
		Seite_Back = new ModelRenderer(this, 25, 40);
		Seite_Back.addBox(0F, 0F, 0F, 10, 1, 1);
		Seite_Back.setRotationPoint(-5F, 12F, -7F);
		Seite_Back.setTextureSize(64, 32);
		Seite_Back.mirror = true;
		setRotation(Seite_Back, 1.570796F, 0F, 0F);
		Seite_Front = new ModelRenderer(this, 0, 40);
		Seite_Front.addBox(0F, 0F, 0F, 10, 1, 1);
		Seite_Front.setRotationPoint(-5F, 21F, -7F);
		Seite_Front.setTextureSize(64, 32);
		Seite_Front.mirror = true;
		setRotation(Seite_Front, 1.570796F, 0F, 0F);
		Seite_Rechts = new ModelRenderer(this, 0, 47);
		Seite_Rechts.addBox(0F, 0F, 0F, 1, 1, 8);
		Seite_Rechts.setRotationPoint(-5F, 20F, -7F);
		Seite_Rechts.setTextureSize(64, 32);
		Seite_Rechts.mirror = true;
		setRotation(Seite_Rechts, 1.570796F, 0F, 0F);
		Seite_Links = new ModelRenderer(this, 21, 47);
		Seite_Links.addBox(0F, 0F, 0F, 1, 1, 8);
		Seite_Links.setRotationPoint(4F, 20F, -7F);
		Seite_Links.setTextureSize(64, 32);
		Seite_Links.mirror = true;
		setRotation(Seite_Links, 1.570796F, 0F, 0F);
		Kanal2 = new ModelRenderer(this, 42, 47);
		Kanal2.addBox(0F, 0F, 0F, 3, 5, 3);
		Kanal2.setRotationPoint(-1.5F, 17.5F, -1.0F);
		Kanal2.setTextureSize(64, 32);
		Kanal2.mirror = true;
		setRotation(Kanal2, 1.570796F, 0F, 0F);
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		Top.render(f5);
		Kanal.render(f5);
		Bottom.render(f5);
		Seite_Back.render(f5);
		Seite_Front.render(f5);
		Seite_Rechts.render(f5);
		Seite_Links.render(f5);
		Kanal2.render(f5);
	}
	
	public void render(float f5)
	{
		Top.render(f5);
		Kanal.render(f5);
		Bottom.render(f5);
		Seite_Back.render(f5);
		Seite_Front.render(f5);
		Seite_Rechts.render(f5);
		Seite_Links.render(f5);
		Kanal2.render(f5);
	}
	
	public void renderHopper(float f5)
	{
		Top.render(f5);
		Kanal2.render(f5);
	}
	
	public void checkTrunk(int facing, int rotation)
	{
		setDefault();
		if (facing != rotation || ForgeDirection.getOrientation(facing).getOpposite().ordinal() != rotation)
		{
			setRotated(facing, rotation);
		}
		else
		{
			setDefault();
		}
	}
	
	private void setRotated(int facing, int rotation)
	{
		switch (facing)
		{
			case 0:
				switch (rotation)
				{
					case 2:
						Bottom.setRotationPoint(-5F, 18F, -8F);
						Seite_Back.setRotationPoint(-5F, 18F, -7F);
						Seite_Front.setRotationPoint(-5F, 9F, -7F);
						Seite_Links.setRotationPoint(4F, 17F, -7F);
						Seite_Rechts.setRotationPoint(-5F, 17F, -7F);
						setRotation(Top, 1.570796F, 0F, 0F);
						Kanal = new ModelRenderer(this, 0, 24);
						Kanal.addBox(0F, 0F, 0F, 3, 9, 3);
						Kanal.setRotationPoint(-1F, 14F, -7.5F);
						Kanal.setTextureSize(64, 32);
						Kanal.mirror = true;
						setRotation(Kanal, 1.570796F, 0F, 0F);
						Kanal2.setRotationPoint(-1.0F, 17.5F, -2F);
						break;
					case 3:
						Bottom.setRotationPoint(-5F, 24F, -8F);
						Seite_Back.setRotationPoint(-5F, 24F, -7F);
						Seite_Front.setRotationPoint(-5F, 15F, -7F);
						Seite_Links.setRotationPoint(4F, 23F, -7F);
						Seite_Rechts.setRotationPoint(-5F, 23F, -7F);
						setRotation(Top, 1.570796F, 0F, 0F);
						Kanal = new ModelRenderer(this, 0, 24);
						Kanal.addBox(0F, 0F, 0F, 3, 9, 3);
						Kanal.setRotationPoint(-1F, 20.5F, -7.5F);
						Kanal.setTextureSize(64, 32);
						Kanal.mirror = true;
						setRotation(Kanal, 1.570796F, 0F, 0F);
						Kanal2.setRotationPoint(-1.0F, 17.5F, -2F);
						break;
					case 4:
						Bottom.setRotationPoint(-8F, 21F, -8F);
						Seite_Back.setRotationPoint(-8F, 21F, -7F);
						Seite_Front.setRotationPoint(-8F, 12F, -7F);
						Seite_Links.setRotationPoint(1F, 20F, -7F);
						Seite_Rechts.setRotationPoint(-8F, 20F, -7F);
						setRotation(Top, 1.570796F, 0F, 0F);
						Kanal = new ModelRenderer(this, 0, 24);
						Kanal.addBox(0F, 0F, 0F, 3, 9, 3);
						Kanal.setRotationPoint(-5F, 17.5F, -7.99F);
						Kanal.setTextureSize(64, 32);
						Kanal.mirror = true;
						setRotation(Kanal, 1.570796F, 0F, 0F);
						Kanal2.setRotationPoint(-1.0F, 17.5F, -2F);
						break;
					case 5:
						Bottom.setRotationPoint(-2F, 21F, -8F);
						Seite_Back.setRotationPoint(-2F, 21F, -7F);
						Seite_Front.setRotationPoint(-2F, 12F, -7F);
						Seite_Links.setRotationPoint(7F, 20F, -7F);
						Seite_Rechts.setRotationPoint(-2F, 20F, -7F);
						setRotation(Top, 1.570796F, 0F, 0F);
						Kanal = new ModelRenderer(this, 0, 24);
						Kanal.addBox(0F, 0F, 0F, 3, 9, 3);
						Kanal.setRotationPoint(2F, 17.5F, -7F);
						Kanal.setTextureSize(64, 32);
						Kanal.mirror = true;
						setRotation(Kanal, 1.570796F, 0F, 0F);
						Kanal2.setRotationPoint(-1.0F, 17.5F, -2F);
						break;
				}
				break;
			case 1:
				switch (rotation)
				{
					case 2:
						Bottom.setRotationPoint(-5F, 24F, -8F);
						Seite_Back.setRotationPoint(-5F, 24F, -7F);
						Seite_Front.setRotationPoint(-5F, 15F, -7F);
						Seite_Links.setRotationPoint(4F, 23F, -7F);
						Seite_Rechts.setRotationPoint(-5F, 23F, -7F);
						setRotation(Top, 1.570796F, 0F, 0F);
						Kanal = new ModelRenderer(this, 0, 24);
						Kanal.addBox(0F, 0F, 0F, 3, 9, 3);
						Kanal.setRotationPoint(-1F, 20.5F, -7.5F);
						Kanal.setTextureSize(64, 32);
						Kanal.mirror = true;
						setRotation(Kanal, 1.570796F, 0F, 0F);
						Kanal2.setRotationPoint(-1.0F, 17.5F, -2F);
						break;
					case 3:
						Bottom.setRotationPoint(-5F, 18F, -8F);
						Seite_Back.setRotationPoint(-5F, 18F, -7F);
						Seite_Front.setRotationPoint(-5F, 9F, -7F);
						Seite_Links.setRotationPoint(4F, 17F, -7F);
						Seite_Rechts.setRotationPoint(-5F, 17F, -7F);
						setRotation(Top, 1.570796F, 0F, 0F);
						Kanal = new ModelRenderer(this, 0, 24);
						Kanal.addBox(0F, 0F, 0F, 3, 9, 3);
						Kanal.setRotationPoint(-1F, 14F, -7.5F);
						Kanal.setTextureSize(64, 32);
						Kanal.mirror = true;
						setRotation(Kanal, 1.570796F, 0F, 0F);
						Kanal2.setRotationPoint(-1.0F, 17.5F, -2F);
						break;
					case 4:
						Bottom.setRotationPoint(-8F, 21F, -8F);
						Seite_Back.setRotationPoint(-8F, 21F, -7F);
						Seite_Front.setRotationPoint(-8F, 12F, -7F);
						Seite_Links.setRotationPoint(1F, 20F, -7F);
						Seite_Rechts.setRotationPoint(-8F, 20F, -7F);
						setRotation(Top, 1.570796F, 0F, 0F);
						Kanal = new ModelRenderer(this, 0, 24);
						Kanal.addBox(0F, 0F, 0F, 3, 9, 3);
						Kanal.setRotationPoint(-5F, 17.5F, -7.99F);
						Kanal.setTextureSize(64, 32);
						Kanal.mirror = true;
						setRotation(Kanal, 1.570796F, 0F, 0F);
						Kanal2.setRotationPoint(-1.0F, 17.5F, -2F);
						break;
					case 5:
						Bottom.setRotationPoint(-2F, 21F, -8F);
						Seite_Back.setRotationPoint(-2F, 21F, -7F);
						Seite_Front.setRotationPoint(-2F, 12F, -7F);
						Seite_Links.setRotationPoint(7F, 20F, -7F);
						Seite_Rechts.setRotationPoint(-2F, 20F, -7F);
						setRotation(Top, 1.570796F, 0F, 0F);
						Kanal = new ModelRenderer(this, 0, 24);
						Kanal.addBox(0F, 0F, 0F, 3, 9, 3);
						Kanal.setRotationPoint(2F, 17.5F, -7F);
						Kanal.setTextureSize(64, 32);
						Kanal.mirror = true;
						setRotation(Kanal, 1.570796F, 0F, 0F);
						Kanal2.setRotationPoint(-1.0F, 17.5F, -2F);
						break;
					default:
						setDefault();
				}
				break;
			case 2:
				switch (rotation)
				{
					case 0:
						setRotation(Top, 1.570796F, 0F, 0F);
						Kanal = new ModelRenderer(this, 0, 24);
						Kanal.addBox(0F, 0F, 0F, 3, 9, 3);
						Kanal.setRotationPoint(-1.5F, 14.5F, -7.5F);
						Kanal.setTextureSize(64, 32);
						Kanal.mirror = true;
						setRotation(Kanal, 1.570796F, 0F, 0F);
						Bottom.setRotationPoint(-5F, 18F, -8F);
						Seite_Back.setRotationPoint(-5F, 9F, -7F);
						Seite_Front.setRotationPoint(-5F, 18F, -7F);
						Seite_Rechts.setRotationPoint(-5F, 17F, -7F);
						Seite_Links.setRotationPoint(4F, 17F, -7F);
						Kanal2.setRotationPoint(-1.5F, 17.5F, -1.5F);
						break;
					case 1:
						setRotation(Top, 1.570796F, 0F, 0F);
						Kanal = new ModelRenderer(this, 0, 24);
						Kanal.addBox(0F, 0F, 0F, 3, 9, 3);
						Kanal.setRotationPoint(-1.5F, 20.5F, -7.5F);
						Kanal.setTextureSize(64, 32);
						Kanal.mirror = true;
						setRotation(Kanal, 1.570796F, 0F, 0F);
						Bottom.setRotationPoint(-5F, 24F, -8F);
						Seite_Back.setRotationPoint(-5F, 15F, -7F);
						Seite_Front.setRotationPoint(-5F, 24F, -7F);
						Seite_Rechts.setRotationPoint(-5F, 23F, -7F);
						Seite_Links.setRotationPoint(4F, 23F, -7F);
						Kanal2.setRotationPoint(-1.5F, 17.5F, -1.5F);
						break;
					case 4:
						setRotation(Top, 1.570796F, 0F, 0F);
						Kanal = new ModelRenderer(this, 0, 24);
						Kanal.addBox(0F, 0F, 0F, 3, 9, 3);
						Kanal.setRotationPoint(1.5F, 17.5F, -7F);
						Kanal.setTextureSize(64, 32);
						Kanal.mirror = true;
						setRotation(Kanal, 1.570796F, 0F, 0F);
						Bottom.setRotationPoint(-2F, 21F, -8F);
						Seite_Back.setRotationPoint(-2F, 12F, -7F);
						Seite_Front.setRotationPoint(-2F, 21F, -7F);
						Seite_Rechts.setRotationPoint(-2F, 20F, -7F);
						Seite_Links.setRotationPoint(7F, 20F, -7F);
						Kanal2.setRotationPoint(-1.0F, 17.5F, -1.5F);
						break;
					case 5:
						setRotation(Top, 1.570796F, 0F, 0F);
						Kanal = new ModelRenderer(this, 0, 24);
						Kanal.addBox(0F, 0F, 0F, 3, 9, 3);
						Kanal.setRotationPoint(-4.5F, 17.5F, -7F);
						Kanal.setTextureSize(64, 32);
						Kanal.mirror = true;
						setRotation(Kanal, 1.570796F, 0F, 0F);
						Bottom.setRotationPoint(-8F, 21F, -8F);
						Seite_Back.setRotationPoint(-8F, 12F, -7F);
						Seite_Front.setRotationPoint(-8F, 21F, -7F);
						Seite_Rechts.setRotationPoint(-8F, 20F, -7F);
						Seite_Links.setRotationPoint(1F, 20F, -7F);
						Kanal2.setRotationPoint(-2.0F, 17.5F, -1.5F);
						break;
				}
				break;
			case 3:
			{
				switch (rotation)
				{
					case 0:
						setRotation(Top, 1.570796F, 0F, 0F);
						Kanal = new ModelRenderer(this, 0, 24);
						Kanal.addBox(0F, 0F, 0F, 3, 9, 3);
						Kanal.setRotationPoint(-1.5F, 14.5F, -7.5F);
						Kanal.setTextureSize(64, 32);
						Kanal.mirror = true;
						setRotation(Kanal, 1.570796F, 0F, 0F);
						Bottom.setRotationPoint(-5F, 18F, -8F);
						Seite_Back.setRotationPoint(-5F, 9F, -7F);
						Seite_Front.setRotationPoint(-5F, 18F, -7F);
						Seite_Rechts.setRotationPoint(-5F, 17F, -7F);
						Seite_Links.setRotationPoint(4F, 17F, -7F);
						Kanal2.setRotationPoint(-1.5F, 17.5F, -1.5F);
						break;
					case 1:
					{
						setRotation(Top, 1.570796F, 0F, 0F);
						Kanal = new ModelRenderer(this, 0, 24);
						Kanal.addBox(0F, 0F, 0F, 3, 9, 3);
						Kanal.setRotationPoint(-1.5F, 20.5F, -7.5F);
						Kanal.setTextureSize(64, 32);
						Kanal.mirror = true;
						setRotation(Kanal, 1.570796F, 0F, 0F);
						Bottom.setRotationPoint(-5F, 24F, -8F);
						Seite_Back.setRotationPoint(-5F, 15F, -7F);
						Seite_Front.setRotationPoint(-5F, 24F, -7F);
						Seite_Rechts.setRotationPoint(-5F, 23F, -7F);
						Seite_Links.setRotationPoint(4F, 23F, -7F);
						Kanal2.setRotationPoint(-1.5F, 17.5F, -1.5F);
						break;
					}
					case 4:
					{
						setRotation(Top, 1.570796F, 0F, 0F);
						Kanal = new ModelRenderer(this, 0, 24);
						Kanal.addBox(0F, 0F, 0F, 3, 9, 3);
						Kanal.setRotationPoint(-4.5F, 17.5F, -7.5F);
						Kanal.setTextureSize(64, 32);
						Kanal.mirror = true;
						setRotation(Kanal, 1.570796F, 0F, 0F);
						Bottom.setRotationPoint(-8F, 21F, -8F);
						Seite_Back.setRotationPoint(-8F, 12F, -7F);
						Seite_Front.setRotationPoint(-8F, 21F, -7F);
						Seite_Rechts.setRotationPoint(-8F, 20F, -7F);
						Seite_Links.setRotationPoint(1F, 20F, -7F);
						Kanal2.setRotationPoint(-1.5F, 17.5F, -1.5F);
						break;
					}
					case 5:
					{
						setRotation(Top, 1.570796F, 0F, 0F);
						Kanal = new ModelRenderer(this, 0, 24);
						Kanal.addBox(0F, 0F, 0F, 3, 9, 3);
						Kanal.setRotationPoint(1.5F, 17.5F, -7.5F);
						Kanal.setTextureSize(64, 32);
						Kanal.mirror = true;
						setRotation(Kanal, 1.570796F, 0F, 0F);
						Bottom.setRotationPoint(-2F, 21F, -8F);
						Seite_Back.setRotationPoint(-2F, 12F, -7F);
						Seite_Front.setRotationPoint(-2F, 21F, -7F);
						Seite_Rechts.setRotationPoint(-2F, 20F, -7F);
						Seite_Links.setRotationPoint(7F, 20F, -7F);
						Kanal2.setRotationPoint(-1.5F, 17.5F, -1.5F);
						break;
					}
				}
				break;
			}
			case 4:
			{
				switch (rotation)
				{
					case 0:
					{
						setRotation(Top, 1.570796F, 0F, 0F);
						Kanal = new ModelRenderer(this, 0, 24);
						Kanal.addBox(0F, 0F, 0F, 3, 9, 3);
						Kanal.setRotationPoint(-1.5F, 14.5F, -7.5F);
						Kanal.setTextureSize(64, 32);
						Kanal.mirror = true;
						setRotation(Kanal, 1.570796F, 0F, 0F);
						Bottom.setRotationPoint(-5F, 18F, -8F);
						Seite_Back.setRotationPoint(-5F, 9F, -7F);
						Seite_Front.setRotationPoint(-5F, 18F, -7F);
						Seite_Rechts.setRotationPoint(-5F, 17F, -7F);
						Seite_Links.setRotationPoint(4F, 17F, -7F);
						Kanal2.setRotationPoint(-1.5F, 17.5F, -1.5F);
						break;
					}
					case 1:
					{
						setRotation(Top, 1.570796F, 0F, 0F);
						Kanal = new ModelRenderer(this, 0, 24);
						Kanal.addBox(0F, 0F, 0F, 3, 9, 3);
						Kanal.setRotationPoint(-1.5F, 20.5F, -7.5F);
						Kanal.setTextureSize(64, 32);
						Kanal.mirror = true;
						setRotation(Kanal, 1.570796F, 0F, 0F);
						Bottom.setRotationPoint(-5F, 24F, -8F);
						Seite_Back.setRotationPoint(-5F, 15F, -7F);
						Seite_Front.setRotationPoint(-5F, 24F, -7F);
						Seite_Rechts.setRotationPoint(-5F, 23F, -7F);
						Seite_Links.setRotationPoint(4F, 23F, -7F);
						Kanal2.setRotationPoint(-1.5F, 17.5F, -1.5F);
						break;
						
					}
					case 2:
					{
						setRotation(Top, 1.570796F, 0F, 0F);
						Kanal = new ModelRenderer(this, 0, 24);
						Kanal.addBox(0F, 0F, 0F, 3, 9, 3);
						Kanal.setRotationPoint(-4.5F, 17.5F, -7.5F);
						Kanal.setTextureSize(64, 32);
						Kanal.mirror = true;
						setRotation(Kanal, 1.570796F, 0F, 0F);
						Bottom.setRotationPoint(-8F, 21F, -8F);
						Seite_Back.setRotationPoint(-8F, 12F, -7F);
						Seite_Front.setRotationPoint(-8F, 21F, -7F);
						Seite_Rechts.setRotationPoint(-8F, 20F, -7F);
						Seite_Links.setRotationPoint(1F, 20F, -7F);
						Kanal2.setRotationPoint(-1.5F, 17.5F, -1.5F);
						break;
					}
					case 3:
					{
						setRotation(Top, 1.570796F, 0F, 0F);
						Kanal = new ModelRenderer(this, 0, 24);
						Kanal.addBox(0F, 0F, 0F, 3, 9, 3);
						Kanal.setRotationPoint(1.5F, 17.5F, -7.5F);
						Kanal.setTextureSize(64, 32);
						Kanal.mirror = true;
						setRotation(Kanal, 1.570796F, 0F, 0F);
						Bottom.setRotationPoint(-2F, 21F, -8F);
						Seite_Back.setRotationPoint(-2F, 12F, -7F);
						Seite_Front.setRotationPoint(-2F, 21F, -7F);
						Seite_Rechts.setRotationPoint(-2F, 20F, -7F);
						Seite_Links.setRotationPoint(7F, 20F, -7F);
						Kanal2.setRotationPoint(-1.5F, 17.5F, -1.5F);
						break;
					}
				}
				break;
			}
			case 5:
			{
				switch (rotation)
				{
					case 0:
					{
						setRotation(Top, 1.570796F, 0F, 0F);
						Kanal = new ModelRenderer(this, 0, 24);
						Kanal.addBox(0F, 0F, 0F, 3, 9, 3);
						Kanal.setRotationPoint(-1.5F, 14.5F, -7.5F);
						Kanal.setTextureSize(64, 32);
						Kanal.mirror = true;
						setRotation(Kanal, 1.570796F, 0F, 0F);
						Bottom.setRotationPoint(-5F, 18F, -8F);
						Seite_Back.setRotationPoint(-5F, 9F, -7F);
						Seite_Front.setRotationPoint(-5F, 18F, -7F);
						Seite_Rechts.setRotationPoint(-5F, 17F, -7F);
						Seite_Links.setRotationPoint(4F, 17F, -7F);
						Kanal2.setRotationPoint(-1.5F, 17.5F, -1.5F);
						break;
					}
					case 1:
					{
						setRotation(Top, 1.570796F, 0F, 0F);
						Kanal = new ModelRenderer(this, 0, 24);
						Kanal.addBox(0F, 0F, 0F, 3, 9, 3);
						Kanal.setRotationPoint(-1.5F, 20.5F, -7.5F);
						Kanal.setTextureSize(64, 32);
						Kanal.mirror = true;
						setRotation(Kanal, 1.570796F, 0F, 0F);
						Bottom.setRotationPoint(-5F, 24F, -8F);
						Seite_Back.setRotationPoint(-5F, 15F, -7F);
						Seite_Front.setRotationPoint(-5F, 24F, -7F);
						Seite_Rechts.setRotationPoint(-5F, 23F, -7F);
						Seite_Links.setRotationPoint(4F, 23F, -7F);
						Kanal2.setRotationPoint(-1.5F, 17.5F, -1.5F);
						break;
					}
					case 2:
					{
						setRotation(Top, 1.570796F, 0F, 0F);
						Kanal = new ModelRenderer(this, 0, 24);
						Kanal.addBox(0F, 0F, 0F, 3, 9, 3);
						Kanal.setRotationPoint(1.5F, 17.5F, -7.5F);
						Kanal.setTextureSize(64, 32);
						Kanal.mirror = true;
						setRotation(Kanal, 1.570796F, 0F, 0F);
						Bottom.setRotationPoint(-2F, 21F, -8F);
						Seite_Back.setRotationPoint(-2F, 12F, -7F);
						Seite_Front.setRotationPoint(-2F, 21F, -7F);
						Seite_Rechts.setRotationPoint(-2F, 20F, -7F);
						Seite_Links.setRotationPoint(7F, 20F, -7F);
						Kanal2.setRotationPoint(-1.5F, 17.5F, -1.5F);
						break;
					}
					case 3:
					{
						setRotation(Top, 1.570796F, 0F, 0F);
						Kanal = new ModelRenderer(this, 0, 24);
						Kanal.addBox(0F, 0F, 0F, 3, 9, 3);
						Kanal.setRotationPoint(-4.5F, 17.5F, -7.5F);
						Kanal.setTextureSize(64, 32);
						Kanal.mirror = true;
						setRotation(Kanal, 1.570796F, 0F, 0F);
						Bottom.setRotationPoint(-8F, 21F, -8F);
						Seite_Back.setRotationPoint(-8F, 12F, -7F);
						Seite_Front.setRotationPoint(-8F, 21F, -7F);
						Seite_Rechts.setRotationPoint(-8F, 20F, -7F);
						Seite_Links.setRotationPoint(1F, 20F, -7F);
						Kanal2.setRotationPoint(-1.5F, 17.5F, -1.5F);
						break;
					}
				}
				break;
			}
		}
	}
	
	private void setDefault()
	{
		setRotation(Top, 1.570796F, 0F, 0F);
		Kanal = new ModelRenderer(this, 0, 24);
		Kanal.addBox(0F, 0F, 0F, 3, 6, 3);
		Kanal.setRotationPoint(-1.5F, 17.5F, -7F);
		Kanal.setTextureSize(64, 32);
		Kanal.mirror = true;
		setRotation(Kanal, 1.570796F, 0F, 0F);
		Bottom.setRotationPoint(-5F, 21F, -8F);
		Seite_Back.setRotationPoint(-5F, 12F, -7F);
		Seite_Front.setRotationPoint(-5F, 21F, -7F);
		Seite_Rechts.setRotationPoint(-5F, 20F, -7F);
		Seite_Links.setRotationPoint(4F, 20F, -7F);
		Kanal2.setRotationPoint(-1.5F, 17.5F, -1.0F);
	}
	
	public void renderTrunk(float f5)
	{
		Kanal.render(f5);
		Bottom.render(f5);
		Seite_Back.render(f5);
		Seite_Front.render(f5);
		Seite_Rechts.render(f5);
		Seite_Links.render(f5);
	}
	
	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
	
	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
	{
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}
	
}
