package speiger.src.tinymodularthings.client.render.storage;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import speiger.src.tinymodularthings.client.models.storage.ModelAdvTinyChest;
import speiger.src.tinymodularthings.client.models.storage.ModelTinyChest;
import speiger.src.tinymodularthings.client.models.storage.ModelTinyTank;
import speiger.src.tinymodularthings.common.blocks.storage.AdvTinyChest;
import speiger.src.tinymodularthings.common.blocks.storage.AdvTinyTank;
import speiger.src.tinymodularthings.common.blocks.storage.TinyChest;
import speiger.src.tinymodularthings.common.blocks.storage.TinyTank;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;

public class RenderStorageBlock extends TileEntitySpecialRenderer
{
	
	public static ResourceLocation basicTCTexture = new ResourceLocation(TinyModularThingsLib.ModID.toLowerCase() + ":textures/models/storage/ModelTinyChest.png");
	public static ResourceLocation advTCOpenTexture = new ResourceLocation(TinyModularThingsLib.ModID.toLowerCase() + ":textures/models/storage/ModelAdvTinyChest.png");
	public static ResourceLocation advTCClosedTexture = new ResourceLocation(TinyModularThingsLib.ModID.toLowerCase() + ":textures/models/storage/ModelAdvTinyChestClosed.png");
	
	ModelTinyTank tinytank = new ModelTinyTank();
	ModelTinyChest tinychest = new ModelTinyChest();
	ModelAdvTinyChest advtinychest = new ModelAdvTinyChest();
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double d0, double d1, double d2, float f)
	{
		if (tile != null)
		{
			if (tile instanceof TinyTank)
			{
				renderTinyTank((TinyTank) tile, d0, d1, d2);
			}
			else if (tile instanceof TinyChest)
			{
				renderTinyChest((TinyChest) tile, d0, d1, d2);
			}
			else if (tile instanceof AdvTinyChest)
			{
				renderAdvTinyChest((AdvTinyChest) tile, d0, d1, d2);
			}
		}
	}
	
	public void renderTinyTank(TinyTank tile, double x, double y, double z)
	{
		if(tile.renderTank())
		{
			return;
		}
		
		
		
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5f, (float) y + 1.5f, (float) z + 0.5f);
		if(tile instanceof AdvTinyTank)
		{
			AdvTinyTank tank = (AdvTinyTank) tile;
			boolean win = tank.isTankFull();
			if(win)
			{
				bindTexture(advTCClosedTexture);
			}
			else
			{
				bindTexture(advTCOpenTexture);
			}
		}
		else
		{
			bindTexture(basicTCTexture);
		}
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
		tinytank.render(0.0625F, tile instanceof AdvTinyTank);
		GL11.glPopMatrix();
	}
	
	public void renderTinyChest(TinyChest par1, double x, double y, double z)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5f, (float) y + 1.5f, (float) z + 0.5f);
		bindTexture(basicTCTexture);
		switch (par1.getFacing())
		{
			case 2:
				GL11.glRotatef(0, 0.0F, 1.0F, 0.0F);
				break;
			case 3:
				GL11.glRotatef(180, 0.0F, 1.0F, 0.0F);
				break;
			case 4:
				GL11.glRotatef(90, 0.0F, 1.0F, 0.0F);
				break;
			case 5:
				GL11.glRotatef(270, 0.0F, 1.0F, 0.0F);
				break;
		}
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
		tinychest.render(0.0625F);
		GL11.glPopMatrix();
	}
	
	public void renderAdvTinyChest(AdvTinyChest par1, double x, double y, double z)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5f, (float) y + 1.5f, (float) z + 0.5f);
		ResourceLocation texture = advTCOpenTexture;
		
		if (par1.isFull)
		{
			texture = advTCClosedTexture;
		}
		
		bindTexture(texture);
		
		switch (par1.getFacing())
		{
			case 2:
				GL11.glRotatef(0, 0.0F, 1.0F, 0.0F);
				break;
			case 3:
				GL11.glRotatef(180, 0.0F, 1.0F, 0.0F);
				break;
			case 4:
				GL11.glRotatef(90, 0.0F, 1.0F, 0.0F);
				break;
			case 5:
				GL11.glRotatef(270, 0.0F, 1.0F, 0.0F);
				break;
		}
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
		advtinychest.render(0.0625F);
		GL11.glPopMatrix();
	}
	
}
