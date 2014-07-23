package speiger.src.tinymodularthings.client.render.carts;

import net.minecraft.client.model.ModelMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import speiger.src.tinymodularthings.client.models.storage.ModelTinyChest;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;
import cpw.mods.fml.client.FMLClientHandler;

public class CartItemRenderer implements IItemRenderer
{
	ModelMinecart cart = new ModelMinecart();
	ModelTinyChest chest = new ModelTinyChest();
	private static final ResourceLocation minecartTextures = new ResourceLocation("textures/entity/minecart.png");
	private static final ResourceLocation tinyChestTexture = new ResourceLocation(TinyModularThingsLib.ModID.toLowerCase() + ":textures/models/storage/ModelTinyChest.png");
	private static final ResourceLocation advTCOpenTexture = new ResourceLocation(TinyModularThingsLib.ModID.toLowerCase() + ":textures/models/storage/ModelAdvTinyChest.png");
	
	boolean adv;
	
	public CartItemRenderer(boolean advanced)
	{
		adv = advanced;
	}
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		return true;
	}
	
	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return true;
	}
	
	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		switch (type)
		{
			case ENTITY:
			{
				renderMinecart(0.0F, 0F, 0.0F, 0.0F, -1.2F, 0.0F);
				break;
			}
			case EQUIPPED_FIRST_PERSON:
			{
				renderMinecart(0.5F, 1.0F, 0.5F, 0.0F, -1.2F, -0.01F);
				break;
			}
			case EQUIPPED:
			{
				renderMinecart(0.5F, 1F, 0.0F, 0F, -1.2F, 0.0F);
				break;
			}
			case INVENTORY:
			{
				renderMinecart(0.0F, -0.2F, 0.0F, 0.0F, -1.2F, 0.0F);
				break;
			}
			default:
				break;
		}
	}
	
	public void renderMinecart(float x, float y, float z, float x2, float y2, float z2)
	{
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(minecartTextures);
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, z);
		GL11.glRotatef(180, 1, 0, 0);
		GL11.glRotatef(0, 0, 1, 0);
		cart.render(null, 0.0F, 0.0F, 0F, 0.0F, 0.0F, 0.0625F);
		
		if (adv)
		{
			FMLClientHandler.instance().getClient().renderEngine.bindTexture(advTCOpenTexture);
			
		}
		else
		{
			FMLClientHandler.instance().getClient().renderEngine.bindTexture(tinyChestTexture);
		}
		GL11.glTranslatef(x2, y2, z2);
		GL11.glScalef(0.9F, 1, 0.9F);
		chest.render(0.0625F);
		
		GL11.glPopMatrix();
	}
	
}
