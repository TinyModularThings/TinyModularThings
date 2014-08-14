package speiger.src.tinymodularthings.client.render.storage;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import speiger.src.tinymodularthings.client.models.storage.ModelTinyChest;
import speiger.src.tinymodularthings.client.models.storage.ModelTinyTank;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;
import cpw.mods.fml.client.FMLClientHandler;

public class ItemRendererStorageBlock implements IItemRenderer
{
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		return true;
	}
	
	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return true;
	}
	
	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		switch (type)
		{
			case ENTITY:
			{
				handleEntityRendering(item);
				break;
			}
			case EQUIPPED_FIRST_PERSON:
			{
				handleFirstPerson(item);
				break;
			}
			case EQUIPPED:
			{
				handleEquicktRendering(item);
				break;
			}
			case INVENTORY:
			{
				handleInventoryRendering(item);
				break;
			}
			default:
				break;
		}
	}
	
	private void handleFirstPerson(ItemStack item)
	{
		if (item == null)
		{
			return;
		}
		
		int id = item.itemID;
		item.getItemDamage();
		if (id == TinyItems.tinyChest.itemID)
		{
			renderTinyChest(0.5F, 1.5F, 0.5F, false);
		}
		else if (id == TinyItems.tinyTank.itemID)
		{
			renderTinyTank(0.5F, 1.5F, 0.5F, false);
		}
		else if (id == TinyItems.advTinyChest.itemID)
		{
			renderAdvTinyChest(0.5F, 1.5F, 0.5F, false);
		}
		else if(id == TinyItems.advTinyTank.itemID)
		{
			renderAdvTinyTank(0.5F, 1.5F, 0.5F, false);
		}
	}
	

	private void handleEntityRendering(ItemStack item)
	{
		if (item == null)
		{
			return;
		}
		
		int id = item.itemID;
		item.getItemDamage();
		if (id == TinyItems.tinyChest.itemID)
		{
			renderTinyChest(0.0F, 1.5F, 0.0F, true);
		}
		else if (id == TinyItems.tinyTank.itemID)
		{
			renderTinyTank(0.0F, 1.5F, 0.0F, true);
		}
		else if (id == TinyItems.advTinyChest.itemID)
		{
			renderAdvTinyChest(0.0F, 1.5F, 0.0F, true);
		}
		else if(id == TinyItems.advTinyTank.itemID)
		{
			renderAdvTinyTank(0.0F, 1.5F, 0.0F, true);
		}
	}
	
	private void handleEquicktRendering(ItemStack item)
	{
		if (item == null)
		{
			return;
		}
		
		int id = item.itemID;
		item.getItemDamage();
		if (id == TinyItems.tinyChest.itemID)
		{
			renderTinyChest(0.5F, 1.5F, 0.0F, false);
		}
		else if (id == TinyItems.tinyTank.itemID)
		{
			renderTinyTank(1.0F, 1.0F, 0.0F, false);
		}
		else if (id == TinyItems.advTinyChest.itemID)
		{
			renderAdvTinyChest(1.0F, 1.0F, 0.0F, false);
		}
		else if(id == TinyItems.advTinyTank.itemID)
		{
			renderAdvTinyTank(1.0F, 1.0F, 0.0F, false);
		}
	}
	
	private void handleInventoryRendering(ItemStack item)
	{
		if (item == null)
		{
			return;
		}
		
		int id = item.itemID;
		item.getItemDamage();
		if (id == TinyItems.tinyChest.itemID)
		{
			renderTinyChest(0.0F, 1.0F, 0.0F, false);
		}
		else if (id == TinyItems.tinyTank.itemID)
		{
			renderTinyTank(0.0F, 1.0F, 0.0F, false);
		}
		else if (id == TinyItems.advTinyChest.itemID)
		{
			renderAdvTinyChest(0.0F, 1.0F, 0.0F, false);
		}
		else if(id == TinyItems.advTinyTank.itemID)
		{
			renderAdvTinyTank(0.0F, 1.0F, 0.0F, false);
		}
	}
	
	private static final ResourceLocation basicTCTexture = new ResourceLocation(TinyModularThingsLib.ModID.toLowerCase() + ":textures/models/storage/ModelTinyChest.png");
	private static final ResourceLocation advTCOpenTexture = new ResourceLocation(TinyModularThingsLib.ModID.toLowerCase() + ":textures/models/storage/ModelAdvTinyChest.png");
	
	ModelTinyTank tinyTank = new ModelTinyTank();
	ModelTinyChest tinyChest = new ModelTinyChest();
	
	public void renderTinyChest(float x, float y, float z, boolean entity)
	{
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(basicTCTexture);
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, z);
		GL11.glRotatef(180, 1, 0, 0);
		GL11.glRotatef(-90, 0, 1, 0);
		tinyChest.render(0.0625F);
		GL11.glPopMatrix();
	}
	
	public void renderTinyTank(float x, float y, float z, boolean entity)
	{
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(basicTCTexture);
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, z);
		GL11.glRotatef(180, 1, 0, 0);
		GL11.glRotatef(-90, 0, 1, 0);
		tinyTank.render(0.0625F, false);
		GL11.glPopMatrix();
	}
	
	public void renderAdvTinyTank(float x, float y, float z, boolean b)
	{
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(advTCOpenTexture);
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, z);
		GL11.glRotatef(180, 1, 0, 0);
		GL11.glRotatef(-90, 0, 1, 0);
		tinyTank.render(0.0625F, false);
		GL11.glPopMatrix();
	}

	
	public void renderAdvTinyChest(float x, float y, float z, boolean entity)
	{
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(advTCOpenTexture);
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, z);
		GL11.glScalef(1F, 1F, 1F);
		GL11.glRotatef(180, 1, 0, 0);
		GL11.glRotatef(-90, 0, 1, 0);
		tinyChest.render(0.0625F);
		GL11.glPopMatrix();
	}
	
}
