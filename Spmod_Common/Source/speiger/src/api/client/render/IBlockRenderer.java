package speiger.src.api.client.render;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import speiger.src.api.common.world.blocks.BlockStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IBlockRenderer
{
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
}
