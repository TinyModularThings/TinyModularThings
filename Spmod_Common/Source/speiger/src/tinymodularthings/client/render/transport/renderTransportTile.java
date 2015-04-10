//package speiger.src.tinymodularthings.client.render.transport;
//
//import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.tileentity.TileEntity;
//
//import org.lwjgl.opengl.GL11;
//
//import speiger.src.api.common.utils.RedstoneUtils;
//import speiger.src.api.common.world.tiles.interfaces.IHopper;
//import speiger.src.tinymodularthings.client.models.transport.ModelTinyHopper;
//
//public class renderTransportTile extends TileEntitySpecialRenderer
//{
//	
//	public static ModelTinyHopper hopper = new ModelTinyHopper();
//	private NBTTagCompound data = new NBTTagCompound("RenderData");
//	
//	@Override
//	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f)
//	{
//		if (tileentity != null)
//		{
//			if (tileentity instanceof IHopper)
//			{
//				NBTTagCompound hopper = data.getCompoundTag("TinyHopper");
//				this.renderTinyHopper((IHopper) tileentity, d0, d1, d2, hopper);
//				data.setCompoundTag("TinyHopper", hopper);
//			}
//		}
//	}
//	
//	public void renderTinyHopper(IHopper par0, double x, double y, double z, NBTTagCompound nbt)
//	{
//		GL11.glPushMatrix();
//		GL11.glTranslatef((float) x + 0.5f, (float) y + 1.5f, (float) z + 0.5f);
//		this.bindTexture(par0.getRenderingTexture());
//		
//		if (nbt.hasKey("Facing") && nbt.hasKey("Rotation"))
//		{
//			if (par0.getFacing() != nbt.getInteger("Facing") || par0.getRotation() != nbt.getInteger("Rotation"))
//			{
//				hopper.checkTrunk(par0.getFacing(), par0.getRotation());
//				nbt.setInteger("Facing", par0.getFacing());
//				nbt.setInteger("Rotation", par0.getRotation());
//			}
//		}
//		else
//		{
//			nbt.setInteger("Facing", par0.getFacing());
//			nbt.setInteger("Rotation", par0.getRotation());
//		}
//		
//		switch (par0.getFacing())
//		{
//			case 0:
//				GL11.glRotatef(90, 1.0F, 0.0F, 0.0F);
//				GL11.glTranslatef(0F, 1F, 1F);
//				break;
//			case 1:
//				GL11.glRotatef(-90, 1.0F, 0.0F, 0.0F);
//				GL11.glTranslatef(0F, 1F, -1F);
//				break;
//			case 2:
//				GL11.glRotatef(180, 0.0F, 1.0F, 0.0F);
//				break;
//			case 3:
//				GL11.glRotatef(0, 0.0F, 1.0F, 0.0F);
//				break;
//			case 4:
//				GL11.glRotatef(270, 0.0F, 1.0F, 0.0F);
//				break;
//			case 5:
//				GL11.glRotatef(90, 0.0F, 1.0F, 0.0F);
//				break;
//		}
//		GL11.glScalef(1.0F, 1F, 1F);
//		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
//		hopper.renderHopper(0.0625F);
//		
//		switch (RedstoneUtils.getRotationMatrixForHopper(par0.getFacing(), par0.getRotation()))
//		{
//			case 0:
//				GL11.glRotatef(-90, 1.0F, 0.0F, 0.0F);
//				GL11.glTranslatef(0F, -1F, 1F);
//				break;
//			case 1:
//				GL11.glRotatef(90, 1.0F, 0.0F, 0.0F);
//				GL11.glTranslatef(0F, -1F, -1F);
//				break;
//			case 2:
//				GL11.glRotatef(0, 0.0F, 1.0F, 0.0F);
//				break;
//			case 3:
//				GL11.glRotatef(180, 0.0F, 1.0F, 0.0F);
//				break;
//			case 4:
//				GL11.glRotatef(90, 0.0F, 1.0F, 0.0F);
//				break;
//			case 5:
//				GL11.glRotatef(270, 0.0F, 1.0F, 0.0F);
//				break;
//		}
//		hopper.renderTrunk(0.0625F);
//		GL11.glPopMatrix();
//		
//	}
//	
//}
