package speiger.src.api.client.render;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;

public interface IMetaItemRender
{
	/** 
	 * @return true means get Implemented.
	 */
	public boolean doRender();
	
	/**
	 * @return true means you customise your Renderring. False mean standart ItemRendering.
	 */
	public boolean doRenderCustom(int meta);
	
	/**
	 * Simply your own XYZ For ItemRenderer. Thats it.
	 * @Return float Array. Required Lenght == 3
	 */
	public float[] getXYZ(ItemRenderType type, int meta);

	/**
	 * @return the Amount of RenderPasses you want to have.
	 */
	public int getRenderPasses(int meta);
	
	/**
	 * 
	 * @param type: RenderType
	 * @param item: ItemItself
	 * @param renderPass: Current RenderPass (Start at 0)
	 * @param x: just x value for ItemRenderring
	 * @param y: just y value for ItemRenderring
	 * @param z: just z value for ItemRenderring
	 * @param data: Look IItemRenderer
	 */
	public void onRender(ItemRenderType type, ItemStack item, int renderPass, float x, float y, float z, Object...data);
}
