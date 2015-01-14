package speiger.src.spmodapi.common.blocks.deko;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;

import org.lwjgl.opengl.GL11;

import speiger.src.api.client.render.IMetaItemRender;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.client.render.deko.RenderKyroka;
import speiger.src.spmodapi.common.items.core.ItemBlockSpmod;
import speiger.src.spmodapi.common.lib.SpmodAPILib;
import cpw.mods.fml.client.FMLClientHandler;

public class ItemBlockKyrokaTheFox extends ItemBlockSpmod implements IMetaItemRender
{
	public static ResourceLocation kyroka = new ResourceLocation(SpmodAPILib.ModID.toLowerCase()+":textures/models/armor/ModelKyrokaTheFox.png");
	
	
	public ItemBlockKyrokaTheFox(int par1)
	{
		super(par1);
		this.setHasSubtypes(true);
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
		return "Kyroka The Fox Statue";
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
			case ENTITY: return new float[]{0.0F, 1.5F, 0.0F};
			case EQUIPPED_FIRST_PERSON: return new float[]{0.5F, 1.8F, 0.5F};
			case EQUIPPED: return new float[]{0.5F, 1.5F, 0.0F};
			case INVENTORY: return new float[]{0.0F, 1.0F, 0.0F};
			default: return new float[3];
		}
	}
	
	@Override
	public void onRender(ItemRenderType type, ItemStack item, int renderPass, float x, float y, float z, Object... data)
	{
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(kyroka);
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, z);
		GL11.glScalef(1F, 1F, 1F);
		GL11.glRotatef(180, 1, 0, 0);
		if(type == type.INVENTORY)
		{
			GL11.glRotatef(-90, 0, 1, 0);
		}
		else
		{
			GL11.glRotatef(90, 0, 1, 0);
		}
		RenderKyroka.kyroka.render(0.0625F, 0);
		GL11.glPopMatrix();
	}
	
}
