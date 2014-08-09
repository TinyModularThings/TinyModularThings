package speiger.src.spmodapi.client.render.deko;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import speiger.src.spmodapi.client.model.ModelLamp;
import speiger.src.spmodapi.common.blocks.deko.ItemBlockLightDekoBlock;
import speiger.src.spmodapi.common.blocks.deko.TileLamp.EnumLampType;
import speiger.src.spmodapi.common.enums.EnumColor;
import speiger.src.spmodapi.common.enums.EnumColor.SpmodColor;
import cpw.mods.fml.client.FMLClientHandler;

public class ItemRendererLamp implements IItemRenderer
{
	
	ModelLamp lamp = new ModelLamp();
	
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
				renderLamp(0.0F, 1.5F, 0.0F, item);
				break;
			}
			case EQUIPPED_FIRST_PERSON:
			{
				renderLamp(0.5F, 1.8F, 0.5F, item);
				break;
			}
			case EQUIPPED:
			{
				renderLamp(0.5F, 1.5F, 0.0F, item);
				break;
			}
			case INVENTORY:
			{
				renderLamp(0.0F, 1.0F, 0.0F, item);
				break;
			}
			default:
				break;
		}
		
	}
	
	public void renderLamp(float x, float y, float z, ItemStack item)
	{
		int meta = item.getItemDamage();
		ItemBlockLightDekoBlock block = (ItemBlockLightDekoBlock)item.getItem();
		boolean inverted = block.inverted(meta);
		EnumLampType type = EnumLampType.values()[block.type(meta)];
		int Color = block.color(meta);
		if(Color > 15)
		{
			Color = 16;
		}
		
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(type.getTexture());
		SpmodColor color = new SpmodColor(EnumColor.values()[Color].getAsHex().intValue());
		if(!inverted)
		{
			color = color.add(0.3D);
		}
		else
		{
			color = color.add(1.4D);
		}
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x, (float) y, (float) z);
		GL11.glRotatef(180, 1, 0, 0);
		if(Color != 16)
		{
			GL11.glColor4d(color.red, color.green, color.blue, 1.0D);
		}
		lamp.render(0.0625F, type.getRenderType());
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		lamp.renderAfter(0.0625F, type.getRenderType());
		GL11.glPopMatrix();
		
	}
	
}
