package speiger.src.tinymodularthings.common.blocks.storage;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import powercrystals.minefactoryreloaded.api.IDeepStorageUnit;
import speiger.src.api.client.render.BlockRenderHelper;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.common.tile.TileFacing;

public class TinyBarrel extends TileFacing implements IDeepStorageUnit
{
	
	public ItemStack templateItem;
	public ItemStack renderItem;
	
	public int storedAmount;
	
	@Override
	public boolean isSixSidedFacing()
	{
		return false;
	}
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		return null;
	}

	@Override
	public ItemStack getStoredItemType()
	{
		return templateItem;
	}

	@Override
	public void setStoredItemCount(int amount)
	{
		storedAmount = amount;
	}

	@Override
	public void setStoredItemType(ItemStack type, int amount)
	{
		templateItem = type;
		
	}

	@Override
	public int getMaxStoredCount()
	{
		return storedAmount;
	}
	
	
	@SideOnly(Side.CLIENT)
	public void onRenderInv(BlockStack stack, RenderBlocks render)
	{
	}
	@SideOnly(Side.CLIENT)
	public void onRenderWorld(Block block, RenderBlocks renderer)
	{
		
	}
	
}
