package speiger.src.api.client.render;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.client.render.core.BlockRendererSpmodCore.BlockRendererHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IBlockRenderer
{
	/*** @BlockSide ***/
	
	/**
	 * This function get called first. If you return false the block get rendered as normal. Else you get your own rendercode.
	 */
	@SideOnly(Side.CLIENT)
	public boolean requiresRenderer(int meta);

	/**
	 * this returns simply if this block needs multibe renderpasses.
	 * It is simply also meta sensitive.
	 */
	@SideOnly(Side.CLIENT)
	public boolean requiresMultibleRenderPasses(int meta);
	
	/**
	 * If you need multible renderPasses then return any number bigger than 0. Else you return 0.
	 * @Note: This function get only called if requiresMultibleRenderPasses is true
	 */
	@SideOnly(Side.CLIENT)
	public int getRenderPasses(int meta);
	
	/**
	 * Your render Function there you add your render Code. That simply it. 
	 */
	@SideOnly(Side.CLIENT)
	public void onRender(IBlockAccess world, int x, int y, int z, RenderBlocks render, BlockStack block, int renderPass);

	/**
	 * IF you simply want no renderer at this meta then you return false. It is simply the same as you would do: getRenderType == -1 but with meta case.
	 * @return
	 */
	@SideOnly(Side.CLIENT)
	public boolean dissableRendering(int meta);
	
	/**
	 * This function simply is for the Registry. If you return false then it does not add it to the list.
	 * @return
	 */
	public boolean requiresRender();
	
	/*** @ItemSide ***/
	
	/**
	 * @return false for dissableing the ItemRenderer Totally. So it will not be renderered.
	 */
	public boolean renderItemBlock(int meta);
	
	/**
	 * @return true for simple block renderring.
	 */
	public boolean renderItemBlockBasic(int meta);
	
	/**
	 * @return a float array with 6 numbers inside (between 0 and 1) 
	 * for the bounds of the block. 
	 * @return null or arraysize < 6 for a normal block renderring. so a full size block.
	 */
	public float[] getBoundingBoxes(int meta);
	
	/**
	 * Simply your own XYZ For ItemRenderer. Thats it.
	 * @Return float Array. Required Lenght == 3
	 */
	public float[] getXYZForItemRenderer(ItemRenderType type, int meta);
	
	/**
	 * @return the Amount of Renderpasses you want to have. (You also can return 0. That will be automaticly set to 1)
	 */
	public int getItemRenderPasses(int meta);
	
	/**
	 * If you want to have CustomRenderer.
	 * @param type: Is the RenderType
	 * @param stack: The Block and the BlockMetadata. No NBTData. Do not even Try
	 * @param renderPass: The Current RenderPass (Start at 0)
	 * @param x: just the x for the ItemRenderring.
	 * @param y: just the y for the ItemRenderring.
	 * @param z: just the z for the ItemRenderring.
	 * @param data: Data for Rendering: 0 = ItemStack (NBT), 1 = RenderBlocks, (Based on Type) 2 = Entity
	 */
	public void onItemRendering(BlockRendererHelper render, ItemRenderType type, BlockStack stack, int renderPass, float x, float y, float z, Object...data);

	
	
}
