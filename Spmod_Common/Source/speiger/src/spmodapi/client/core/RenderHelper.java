package speiger.src.spmodapi.client.core;

import net.minecraft.client.renderer.entity.RenderItem;

public class RenderHelper
{
	static RenderItem itemRenderer = new RenderItem();
	
	public static RenderItem getItemRenderer()
	{
		return itemRenderer;
	}
}
