package speiger.src.tinymodularthings.client.render.transport;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import speiger.src.tinymodularthings.client.models.transport.ModelTinyHopper;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.utils.HopperType;
import cpw.mods.fml.client.FMLClientHandler;

public class ItemRenderTransportTile implements IItemRenderer
{
	
	ModelTinyHopper model = new ModelTinyHopper();
	
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
				handleEntityRendering(item, data);
				break;
			}
			case EQUIPPED_FIRST_PERSON:
			{
				handleFirstPerson(item, data);
				break;
			}
			case EQUIPPED:
			{
				handleEquicktRendering(item, data);
				break;
			}
			case INVENTORY:
			{
				handleInventoryRendering(item, data);
				break;
			}
			default:
				break;
		}
	}
	
	private void handleFirstPerson(ItemStack item, Object... data)
	{
		if (item == null)
		{
			return;
		}
		
		int id = item.itemID;
		int damage = item.getItemDamage();
		
		if (id == TinyBlocks.transportBlock.blockID && damage == 0)
		{
			renderEnderProxy((RenderBlocks) data[0], -0.4f, 0.50f, 0.35f);
		}
		else if (id == TinyItems.interfaceBlock.itemID)
		{
			renderInterfaces((RenderBlocks) data[0], -0.4f, 0.50f, 0.35f, item);
		}
		else if (this.isHopperID(id))
		{
			renderTinyHopper(0.5F, 1.5F, 0.5F, getTextureFromID(this.getHopperIDFromID(id)));
		}
		
	}
	
	private void handleEntityRendering(ItemStack item, Object... data)
	{
		if (item == null)
		{
			return;
		}
		
		int id = item.itemID;
		int damage = item.getItemDamage();
		if (id == TinyBlocks.transportBlock.blockID && damage == 0)
		{
			renderEnderProxy((RenderBlocks) data[0], -0.5f, -0.5f, -0.5f);
		}
		else if (id == TinyItems.interfaceBlock.itemID)
		{
			renderInterfaces((RenderBlocks) data[0], -0.5f, -0.5f, -0.5f, item);
		}
		else if (this.isHopperID(id))
		{
			renderTinyHopper(0.0F, 1.5F, 0.0F, getTextureFromID(this.getHopperIDFromID(id)));
		}
	}
	
	private void handleEquicktRendering(ItemStack item, Object... data)
	{
		if (item == null)
		{
			return;
		}
		
		int id = item.itemID;
		int damage = item.getItemDamage();
		
		if (id == TinyBlocks.transportBlock.blockID && damage == 0)
		{
			renderEnderProxy((RenderBlocks) data[0], -0.4f, 0.50f, 0.35f);
		}
		else if (id == TinyItems.interfaceBlock.itemID)
		{
			renderInterfaces((RenderBlocks) data[0], -0.4f, 0.50f, 0.35f, item);
		}
		else if (this.isHopperID(id))
		{
			renderTinyHopper(1.0F, 1.0F, 0.0F, getTextureFromID(this.getHopperIDFromID(id)));
		}
	}
	
	private void handleInventoryRendering(ItemStack item, Object... data)
	{
		if (item == null)
		{
			return;
		}
		
		int id = item.itemID;
		int damage = item.getItemDamage();
		
		if (id == TinyBlocks.transportBlock.blockID && damage == 0)
		{
			renderEnderProxy((RenderBlocks) data[0], -0.5f, -0.5f, -0.5f);
		}
		else if (id == TinyItems.interfaceBlock.itemID)
		{
			renderInterfaces((RenderBlocks) data[0], -0.5f, -0.5f, -0.5f, item);
		}
		else if (this.isHopperID(id))
		{
			renderTinyHopper(0.0F, 1.0F, 0.0F, getTextureFromID(this.getHopperIDFromID(id)));
		}
		
	}
	
	private void renderEnderProxy(RenderBlocks render, float x, float y, float z)
	{
		Tessellator tessellator = Tessellator.instance;
		Block block = TinyBlocks.transportBlock;
		block.setBlockBounds(0F, 0F, 0.0F, 1.0F, 1.0F, 1.0F);
		block.setBlockBoundsForItemRender();
		render.setRenderBoundsFromBlock(block);
		
		GL11.glTranslatef(x, y, z);
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1F, 0.0F);
		render.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, render.getIconSafe(block.getIcon(0, 0)));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		render.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, render.getIconSafe(block.getIcon(1, 0)));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, -1F);
		render.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, render.getIconSafe(block.getIcon(2, 0)));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		render.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, render.getIconSafe(block.getIcon(3, 0)));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(-1F, 0.0F, 0.0F);
		render.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, render.getIconSafe(block.getIcon(4, 0)));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		render.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, render.getIconSafe(block.getIcon(5, 0)));
		tessellator.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
	
	private void renderInterfaces(RenderBlocks render, float x, float y, float z, ItemStack stack)
	{
		Tessellator tessellator = Tessellator.instance;
		Block block = TinyBlocks.transportBlock;
		block.setBlockBounds(0.35F, 0.35F, 0.35F, 0.65F, 0.65F, 0.65F);
		block.setBlockBoundsForItemRender();
		render.setRenderBoundsFromBlock(block);
		Icon texture = render.getIconSafe(stack.getItem().getIconFromDamage(stack.getItemDamage()));
		
		GL11.glTranslatef(x, y, z);
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1F, 0.0F);
		render.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, texture);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		render.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, texture);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, -1F);
		render.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, texture);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		render.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, texture);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(-1F, 0.0F, 0.0F);
		render.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, texture);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		render.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, texture);
		tessellator.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
	
	private void renderTinyHopper(float x, float y, float z, ResourceLocation location)
	{
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(location);
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, z);
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
		this.model.render(0.0625F);
		GL11.glPopMatrix();
	}
	
	private ResourceLocation getTextureFromID(int i)
	{
		boolean adv = i % 2 == 1;
		return HopperType.values()[(i / 2)].getTexture(adv);
	}
	
	public boolean isHopperID(int id)
	{
		if (id == TinyItems.advEnergyHopper.itemID)
		{
			return true;
		}
		else if (id == TinyItems.advFluidHopper.itemID)
		{
			return true;
		}
		else if (id == TinyItems.advTinyHopper.itemID)
		{
			return true;
		}
		else if (id == TinyItems.fluidHopper.itemID)
		{
			return true;
		}
		else if (id == TinyItems.energyHopper.itemID)
		{
			return true;
		}
		else if (id == TinyItems.tinyHopper.itemID)
		{
			return true;
		}
		return false;
	}
	
	public int getHopperIDFromID(int id)
	{
		if (id == TinyItems.advEnergyHopper.itemID)
		{
			return 5;
		}
		else if (id == TinyItems.advFluidHopper.itemID)
		{
			return 3;
		}
		else if (id == TinyItems.advTinyHopper.itemID)
		{
			return 1;
		}
		else if (id == TinyItems.fluidHopper.itemID)
		{
			return 2;
		}
		else if (id == TinyItems.energyHopper.itemID)
		{
			return 4;
		}
		else if (id == TinyItems.tinyHopper.itemID)
		{
			return 0;
		}
		return -1;
	}
	
}
