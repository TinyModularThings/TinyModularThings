package speiger.src.tinymodularthings.client.render.storage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;

import org.lwjgl.opengl.GL11;

import speiger.src.spmodapi.client.core.RenderHelper;
import speiger.src.spmodapi.common.enums.EnumColor;
import speiger.src.tinymodularthings.client.models.storage.ModelAdvTinyChest;
import speiger.src.tinymodularthings.client.models.storage.ModelTinyChest;
import speiger.src.tinymodularthings.client.models.storage.ModelTinyTank;
import speiger.src.tinymodularthings.common.blocks.storage.*;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;
import cpw.mods.fml.client.FMLClientHandler;

public class RenderStorageBlock extends TileEntitySpecialRenderer
{
	
	public static ResourceLocation basicTCTexture = new ResourceLocation(TinyModularThingsLib.ModID.toLowerCase() + ":textures/models/storage/ModelTinyChest.png");
	public static ResourceLocation advTCOpenTexture = new ResourceLocation(TinyModularThingsLib.ModID.toLowerCase() + ":textures/models/storage/ModelAdvTinyChest.png");
	public static ResourceLocation advTCClosedTexture = new ResourceLocation(TinyModularThingsLib.ModID.toLowerCase() + ":textures/models/storage/ModelAdvTinyChestClosed.png");
	
	public static ModelTinyTank tinytank = new ModelTinyTank();
	public static ModelTinyChest tinychest = new ModelTinyChest();
	public static ModelAdvTinyChest advtinychest = new ModelAdvTinyChest();
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double d0, double d1, double d2, float f)
	{
		if(tile != null)
		{
			if(tile instanceof TinyTank)
			{
				renderTinyTank((TinyTank)tile, d0, d1, d2);
			}
			else if(tile instanceof TinyChest)
			{
				renderTinyChest((TinyChest)tile, d0, d1, d2);
			}
			else if(tile instanceof AdvTinyChest)
			{
				renderAdvTinyChest((AdvTinyChest)tile, d0, d1, d2);
			}
			else if(tile instanceof TinyBarrel)
			{
				renderTinyBarrel((TinyBarrel)tile, d0, d1, d2);
			}
		}
	}
	
	public void renderTinyBarrel(TinyBarrel tile, double x, double y, double z)
	{
		if(tile.hasItem())
		{
			GL11.glPushMatrix();
			
			tile.applyRotation((float)x, (float)y, (float)z);
			tile.applySize(8F, 65D, 75D, -0.001D);
			
			Minecraft mc = FMLClientHandler.instance().getClient();
			if(!ForgeHooksClient.renderInventoryItem(RenderHelper.getBlockRenderer(tile.worldObj), mc.getTextureManager(), tile.getItem(), true, 0.0F, 0.0F, 0.0F))
			{
				RenderHelper.getItemRenderer().renderItemIntoGUI(mc.fontRenderer, mc.getTextureManager(), tile.getItem(), 0, 0);
			}
			
			String barrelString = tile.getBarrelString();
			int lenght = mc.fontRenderer.getStringWidth(barrelString);
			
			GL11.glScalef(0.5F, 0.5F, 1.0F);
			
			GL11.glDepthMask(false);
			GL11.glDisable(2896);
			
			mc.fontRenderer.drawString(barrelString, -lenght / 2 + 15, -10, EnumColor.WHITE.getAsHex());
			
			GL11.glDepthMask(true);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			
			GL11.glPopMatrix();
		}
	}
	
	public void renderTinyTank(TinyTank tile, double x, double y, double z)
	{
		if(tile.renderTank())
		{
			return;
		}
		
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x + 0.5f, (float)y + 1.5f, (float)z + 0.5f);
		if(tile instanceof AdvTinyTank)
		{
			AdvTinyTank tank = (AdvTinyTank)tile;
			boolean win = tank.isTankFull();
			if(win)
			{
				bindTexture(advTCClosedTexture);
				GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
				tinytank.render(0.0625F, tile instanceof AdvTinyTank);
				GL11.glPopMatrix();
				return;
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
		GL11.glTranslatef(0F, -1F, 0F);
		renderFluidInTank(tile, x, y, z);
		GL11.glTranslatef(0F, 1F, 0F);
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
		tinytank.render(0.0625F, tile instanceof AdvTinyTank);
		GL11.glPopMatrix();
	}
	
	private void renderFluidInTank(TinyTank par1, double x, double y, double z)
	{
//		RenderBlocks blocks = RenderHelper.getBlockRenderer(par1.worldObj);
//		FluidTank tank = par1.tank;
//		if(tank.getFluidAmount() > 0)
//		{
//			FluidStack stack = tank.getFluid();
//			if(stack != null && stack.getFluid() != null && par1.renderLiquid() && !par1.renderTank())
//			{
//				Fluid fluid = stack.getFluid();
//				Icon icon = fluid.getIcon(stack);
//				double amount = ((double)stack.amount / (double)tank.getCapacity()) * 75;
//				amount /= 100;
//				Block block = TinyBlocks.storageBlock;
//				block.setBlockBounds((float)0.125, (float)0.125, (float)0.125, (float)0.875, (float)((float)0.125+amount), (float)0.875);
//				blocks.renderAllFaces = true;
//				blocks.setOverrideBlockTexture(icon);
//				blocks.renderBlockAllFaces(Block.stone, par1.xCoord, par1.yCoord, par1.zCoord);
//				blocks.clearOverrideBlockTexture();
//				block.setBlockBounds(0, 0, 0, 1, 1, 1);
//				blocks.renderAllFaces = false;
//			}
//			
//			if(par1.renderTank())
//			{
//				blocks.renderStandardBlock(TinyBlocks.storageBlock, par1.xCoord, par1.yCoord, par1.zCoord);
//			}
//		}
	}
	
	public void renderTinyChest(TinyChest par1, double x, double y, double z)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x + 0.5f, (float)y + 1.5f, (float)z + 0.5f);
		bindTexture(basicTCTexture);
		switch(par1.getFacing())
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
		GL11.glTranslatef((float)x + 0.5f, (float)y + 1.5f, (float)z + 0.5f);
		ResourceLocation texture = advTCOpenTexture;
		
		if(par1.isFull)
		{
			texture = advTCClosedTexture;
		}
		
		bindTexture(texture);
		
		switch(par1.getFacing())
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
