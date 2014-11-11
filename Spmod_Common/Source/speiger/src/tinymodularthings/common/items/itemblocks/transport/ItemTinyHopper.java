package speiger.src.tinymodularthings.common.items.itemblocks.transport;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.client.render.IMetaItemRender;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.tinymodularthings.client.render.transport.renderTransportTile;
import speiger.src.tinymodularthings.common.blocks.transport.TinyHopper;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.items.core.TinyPlacerItem;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTinyHopper extends TinyPlacerItem implements IMetaItemRender
{
	HopperType type;
	
	public ItemTinyHopper(int par1, HopperType par2)
	{
		super(par1);
		type = par2;
		setHasSubtypes(true);
		setCreativeTab(CreativeTabs.tabFood);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add(type + " Hopper");
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for(int i = 0;i < 9;i++)
		{
			par3List.add(new ItemStack(par1, 1, i));
		}
	}
	
	
	
	
	
	@Override
	public void onAfterPlaced(World world, int x, int y, int z, int side, EntityPlayer player, ItemStack item)
	{
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if(tile != null && tile instanceof TinyHopper)
		{
			TinyHopper tiny = (TinyHopper)tile;
			tiny.setMode(item.getItemDamage() + 1);
			tiny.updateMode();
			if(player.isSneaking())
			{
				tiny.setRotation(ForgeDirection.getOrientation(side).getOpposite().ordinal());
			}
			this.removeItem(player, item);
		}
	}
	
	public static enum HopperType
	{
		Items,
		AdvItems,
		Fluids,
		AdvFluids,
		Energy,
		AdvancedEnergy;
		
	}
	
	@Override
	public BlockStack getBlockToPlace(int meta)
	{
		return new BlockStack(TinyBlocks.transportBlock, 10 + type.ordinal());
	}
	
	@Override
	public String getName(ItemStack par1)
	{
		return "Tiny Hopper";
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
			case EQUIPPED_FIRST_PERSON: return new float[]{0.5F, 1.5F, 0.5F};
			case EQUIPPED: return new float[]{1.0F, 1.0F, 0.0F};
			case INVENTORY: return new float[]{0.0F, 1.0F, 0.0F};
			default: return new float[3];
		}
	}
	
	public ResourceLocation getTextureFromType()
	{
		switch(type)
		{
			case AdvFluids: return speiger.src.tinymodularthings.common.utils.HopperType.Fluids.getTexture(true);
			case AdvItems: return speiger.src.tinymodularthings.common.utils.HopperType.Items.getTexture(true);
			case AdvancedEnergy: return speiger.src.tinymodularthings.common.utils.HopperType.Energy.getTexture(true);
			case Energy: return speiger.src.tinymodularthings.common.utils.HopperType.Energy.getTexture(false);
			case Fluids: return speiger.src.tinymodularthings.common.utils.HopperType.Fluids.getTexture(false);
			case Items: return speiger.src.tinymodularthings.common.utils.HopperType.Items.getTexture(false);
			default: return null;
		}
	}

	@Override
	public void onRender(ItemRenderType type, ItemStack item, int renderPass, float x, float y, float z, Object... data)
	{
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(getTextureFromType());
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, z);
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
		renderTransportTile.hopper.render(0.0625F);
		GL11.glPopMatrix();
	}
}
