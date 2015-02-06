package speiger.src.spmodapi.common.blocks.utils;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.tile.FacedInventory;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.slot.AdvContainer;

public class CobbleChest extends FacedInventory
{
	
	public CobbleChest()
	{
		super(6);
	}

	@Override
	public boolean isSixSidedFacing()
	{
		return true;
	}
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		if(side == getFacing() && renderPass == 1)
		{
			return getEngine().getTexture(APIBlocks.blockUtils, 5, 0);
		}
		return Block.cobblestone.getBlockTextureFromSide(0);
	}

	@Override
	public boolean canMergeItem(ItemStack par1, int slotID)
	{
		return true;
	}

	@Override
	public boolean canUpdate()
	{
		return false;
	}

	@Override
	public void registerIcon(TextureEngine par1, Block par2)
	{
		par1.registerTexture(par2, 5, "Cobble.Storage.Front");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onRenderInv(BlockStack stack, RenderBlocks render)
	{
		this.setRenderPass(0);
		super.onRenderInv(stack, render);
		this.setRenderPass(1);
		Block block = stack.getBlock();
		Tessellator tessler = Tessellator.instance;
		
	    GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
	    tessler.startDrawingQuads();
	    tessler.setNormal(0.0F, 0.0F, 1.0F);
	    render.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, getEngine().getIconSafe(block.getIcon(3, stack.getMeta())));
	    tessler.draw();
	    GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onRenderWorld(Block block, RenderBlocks renderer)
	{
		for(int i = 0;i<2;i++)
		{
			this.setRenderPass(i);
			super.onRenderWorld(block, renderer);
		}
	}

	@Override
	public void addContainerSlots(AdvContainer par1)
	{
		par1.addSpmodSlot(this, 0, 62, 28);
		par1.addSpmodSlot(this, 1, 80, 28);
		par1.addSpmodSlot(this, 2, 98, 28);
		par1.addSpmodSlot(this, 3, 62, 46);
		par1.addSpmodSlot(this, 4, 80, 46);
		par1.addSpmodSlot(this, 5, 98, 46);
	}

	@Override
	public boolean hasContainer()
	{
		return true;
	}
	
	
	
}
