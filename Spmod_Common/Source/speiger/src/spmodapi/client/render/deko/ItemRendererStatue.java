package speiger.src.spmodapi.client.render.deko;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import speiger.src.spmodapi.common.lib.SpmodAPILib;
import cpw.mods.fml.client.FMLClientHandler;

public class ItemRendererStatue implements IItemRenderer
{
	public static ResourceLocation kyroka = new ResourceLocation(SpmodAPILib.ModID.toLowerCase()+":textures/models/armor/ModelKyrokaTheFox.png");
	
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
		switch(type)
		{
			case ENTITY:
			{
				renderKyroka(0.0F, 1.5F, 0.0F);
				break;
			}
			case EQUIPPED_FIRST_PERSON:
			{
				renderKyroka(0.5F, 1.8F, 0.5F);
				break;
			}
			case EQUIPPED:
			{
				renderKyroka(0.5F, 1.5F, 0.0F);
				break;
			}
			case INVENTORY:
			{
				renderKyroka(0.0F, 1.0F, 0.0F);
				break;
			}
			default:
				break;
		}
	}
	
	public void renderKyroka(float x, float y, float z)
	{
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(kyroka);
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, z);
		GL11.glScalef(1F, 1F, 1F);
		GL11.glRotatef(180, 1, 0, 0);
		GL11.glRotatef(90, 0, 1, 0);
		RenderKyroka.kyroka.render(0.0625F, 0);
		GL11.glPopMatrix();
	}
	
}
