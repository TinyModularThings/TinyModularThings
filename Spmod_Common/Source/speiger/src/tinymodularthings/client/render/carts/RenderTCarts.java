package speiger.src.tinymodularthings.client.render.carts;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RenderMinecart;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import speiger.src.tinymodularthings.client.models.storage.ModelAdvTinyChest;
import speiger.src.tinymodularthings.client.models.storage.ModelTinyChest;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityAdvTinyChestCart;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;

public class RenderTCarts extends RenderMinecart
{
	public static ResourceLocation tinyChestTexture = new ResourceLocation(TinyModularThingsLib.ModID.toLowerCase() + ":textures/models/storage/ModelTinyChest.png");
	public static ResourceLocation advTCOpenTexture = new ResourceLocation(TinyModularThingsLib.ModID.toLowerCase() + ":textures/models/storage/ModelAdvTinyChest.png");
	public static ResourceLocation advTCClosedTexture = new ResourceLocation(TinyModularThingsLib.ModID.toLowerCase() + ":textures/models/storage/ModelAdvTinyChestClosed.png");
	
	ModelTinyChest tinychest = new ModelTinyChest();
	ModelAdvTinyChest advtinychest = new ModelAdvTinyChest();
	
	@Override
	protected void renderBlockInMinecart(EntityMinecart par1EntityMinecart, float par2, Block par3, int par4)
	{
		super.renderBlockInMinecart(par1EntityMinecart, par2, par3, par4);
		
		if (par3.blockID == TinyBlocks.storageBlock.blockID && par4 == 0)
		{
			renderTinyChest(par1EntityMinecart, par2);
			return;
		}
		else if (par3.blockID == TinyBlocks.storageBlock.blockID && par4 == 2 && par1EntityMinecart instanceof EntityAdvTinyChestCart)
		{
			renderAdvTinyChest((EntityAdvTinyChestCart) par1EntityMinecart, par2);
		}
		
	}
	
	private void renderTinyChest(EntityMinecart par1, float par2)
	{
		
		GL11.glTranslatef(0.0F, 1.0F, 0.0F);
		bindTexture(tinyChestTexture);
		GL11.glRotatef(270, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
		tinychest.render(0.0625F);
		
	}
	
	private void renderAdvTinyChest(EntityAdvTinyChestCart par1, float par2)
	{
		ResourceLocation location = advTCOpenTexture;
		
		if (par1.getStackInSlot(par1.getSizeInventory() - 1) != null)
		{
			location = advTCClosedTexture;
		}
		GL11.glTranslatef(0.0F, 1.0F, 0.0F);
		bindTexture(location);
		GL11.glRotatef(270, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
		advtinychest.render(0.0625F);
	}
	
}
