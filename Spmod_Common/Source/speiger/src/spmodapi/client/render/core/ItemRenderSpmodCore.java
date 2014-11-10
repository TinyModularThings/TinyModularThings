package speiger.src.spmodapi.client.render.core;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import speiger.src.api.client.render.IMetaItemRender;
import cpw.mods.fml.common.registry.ItemData;

public class ItemRenderSpmodCore implements IItemRenderer
{
	public static ItemRenderSpmodCore instance = new ItemRenderSpmodCore();
	
	
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		return ((IMetaItemRender)item.getItem()).doRenderCustom(item.getItemDamage());
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		IMetaItemRender render = (IMetaItemRender)item.getItem();
		int passes = render.getRenderPasses(item.getItemDamage());
		float[] array = render.getXYZ(type, item.getItemDamage());
		if(array == null || array.length != 3)
		{
			array = new float[]{0.5F,0.5F,0.5F};
		}
		for(int i = 0;i<passes;i++)
		{
			render.onRender(type, item, i, array[0], array[1], array[2], data);
		}

	}
	
}
