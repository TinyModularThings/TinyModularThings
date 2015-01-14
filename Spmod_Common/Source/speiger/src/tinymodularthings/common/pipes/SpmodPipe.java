package speiger.src.tinymodularthings.common.pipes;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import speiger.src.api.client.render.BlockRenderHelper;
import speiger.src.api.client.render.IMetaItemRender;
import speiger.src.api.common.world.blocks.BlockStack;
import buildcraft.BuildCraftTransport;
import buildcraft.transport.ItemPipe;

public class SpmodPipe extends ItemPipe implements IMetaItemRender
{
	String languageName;
	
	public SpmodPipe(int i, String name)
	{
		super(i);
		languageName = name;
	}

	@Override
	public String getItemDisplayName(ItemStack itemstack)
	{
		return languageName;
	}

	@Override
	public boolean doRender()
	{
		return true;
	}

	@Override
	public boolean doRenderCustom(int meta)
	{
		return true;
	}

	@Override
	public float[] getXYZ(ItemRenderType type, int meta)
	{
		switch(type)
		{
			case ENTITY: return new float[]{-0.5F, -0.5F, -0.5F};
			case EQUIPPED: return new float[]{-0.4F, 0.50F, 0.35F};
			case EQUIPPED_FIRST_PERSON: return new float[]{-0.4F, 0.50F, 0.35F};
			case INVENTORY: return new float[]{-0.5F, -0.5F, -0.5F};
			default: return null;
			
		}
	}

	public Icon[] getTexture()
	{
		Icon texture = this.getIconFromDamage(0);
		return new Icon[]{texture, texture, texture, texture, texture, texture};
	}
	
	@Override
	public void onRender(ItemRenderType type, ItemStack item, int renderPass, float x, float y, float z, Object... data)
	{
		BuildCraftTransport.genericPipeBlock.setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 1.0F, 0.75F);
		((RenderBlocks)data[0]).setOverrideBlockTexture(getIconFromDamage(item.getItemDamage()));
		BlockRenderHelper.renderInv(new BlockStack(BuildCraftTransport.genericPipeBlock, 0), (RenderBlocks)data[0]);
		BuildCraftTransport.genericPipeBlock.setBlockBoundsForItemRender();
		((RenderBlocks)data[0]).clearOverrideBlockTexture();
	}
	
	
	
}
