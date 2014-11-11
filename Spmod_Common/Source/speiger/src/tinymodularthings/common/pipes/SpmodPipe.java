package speiger.src.tinymodularthings.common.pipes;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import speiger.src.api.client.render.IMetaItemRender;
import speiger.src.spmodapi.client.render.core.BlockRendererSpmodCore.BlockRendererHelper;
import buildcraft.BuildCraftTransport;
import buildcraft.core.CoreConstants;
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
		BlockRendererHelper.renderBlockStandart(getTexture(), new float[]{CoreConstants.PIPE_MIN_POS, 0.0F, CoreConstants.PIPE_MIN_POS, CoreConstants.PIPE_MAX_POS, 1.0F, CoreConstants.PIPE_MAX_POS}, BuildCraftTransport.genericPipeBlock, new float[]{x,y,z}, (RenderBlocks)data[0]);
	}
	
	
	
}
