package speiger.src.tinymodularthings.common.blocks.transport;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;

import org.lwjgl.opengl.GL11;

import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.items.core.ItemBlockTinyChest;

public class ItemBlockTransport extends ItemBlockTinyChest
{
	
	public ItemBlockTransport(int par1)
	{
		super(par1);
		setHasSubtypes(true);
		setMaxDamage(0);
	}
	
	@Override
	public int getMetadata(int par1)
	{
		return par1;
	}

	@Override
	public BlockStack getBlockToPlace(int meta)
	{
		return new BlockStack(getBlockID(), meta);
	}

	@Override
	public String getName(ItemStack par1)
	{
		switch(par1.getItemDamage())
		{
			case 0: return "EnderChest Proxy";
			case 1: return "Item Multistructure Interface";
			case 2: return "Fluid Multistructure Interface";
			case 3: return "Energy Multistructure Interface";
			case 4: return "Advanced EnderChest Proxy";
			
			case 10: return "Tiny Hopper";
			case 11: return "Advanced Tiny Hopper";
			case 12: return "Fluid Hopper";
			case 13: return "Advanced Fluid Hopper";
			case 14: return "Energy Hopper";
			case 15: return "Advanced Energy Hopper";
		}
		return null;
	}


	

	
}
