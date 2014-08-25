package speiger.src.compactWindmills.client.render;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import speiger.src.api.items.IRotorItem.IRotorModel;
import speiger.src.compactWindmills.common.blocks.WindMill;
import speiger.src.compactWindmills.common.items.ItemRotor.BasicRotorType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 
 * @author Aroma1997
 * @Redesigned Speiger
 * 
 */
@SideOnly(Side.CLIENT)
public class WindmillRenderer extends TileEntitySpecialRenderer
{
	
	private IRotorModel model;
	
	public void renderBlockRotor(WindMill tile, World world, int posX, int posY, int posZ)
	{
		if (!tile.hasRotor() || tile.getType() == null || tile.getRotor().getRenderTexture(tile.inv[0]) == null)
		{
			model = null;
			return;
		}
		if(model == null)
		{
			IRotorModel rotor = tile.getRotor().getCustomModel(tile.inv[0], tile.getType().getRadius());
			if(rotor == null)
			{
				model = new ModelRotor(tile.getType().getRadius());
			}
			else
			{
				model = rotor;
			}
		}
		
		if(model == null)
		{
			return;
		}
		
		Tessellator tessellator = Tessellator.instance;
		float brightness = world.getBlockLightValue(posX, posY, posZ);
		int skyBrightness = world.getLightBrightnessForSkyBlocks(posX, posY + 1, posZ, 0);
		int skyBrightness1 = skyBrightness % 65536;
		int skyBrightness2 = skyBrightness / 65536;
		tessellator.setColorOpaque_F(brightness, brightness, brightness);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, skyBrightness1, skyBrightness2);
		
		int facing = tile.getFacing();
		
		GL11.glPushMatrix();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		if (facing == 2 || facing == 3 || facing == 4 || facing == 5)
		{
			int dir = facing == 4 ? 0 : facing == 2 ? 1 : facing == 5 ? 2 : facing;
			GL11.glRotatef(dir * -90F, 0F, 1F, 0F);
		}
		else if (facing == 1)
		{
			GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
		}
		GL11.glRotatef(360 - System.currentTimeMillis() / 30 % 360, 1.0F, 0.0F, 0.0F);
		GL11.glTranslatef(-0.25F, 0.0F, 0.0F);
		
		bindTexture(tile.getRotor().getRenderTexture(tile.inv[0]));
		
		
		model.render(0.0625F);
		GL11.glPopMatrix();
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double posX, double posY, double posZ, float f)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef((float) posX, (float) posY, (float) posZ);
		renderBlockRotor((WindMill)tileEntity, tileEntity.worldObj, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
		GL11.glPopMatrix();
	}
	
}
