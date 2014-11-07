package speiger.src.spmodapi.client.render.core;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import speiger.src.api.client.render.IMetaItemRender;

public class ItemRenderSpmodCore implements IItemRenderer
{
	public static ItemRenderSpmodCore instance = new ItemRenderSpmodCore();
	
	
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
		IMetaItemRender render = (IMetaItemRender)item.getItem();
		if(render.doRenderCustom(item.getItemDamage()))
		{
			
		}
		else
		{
		}
	}
	
}
