package speiger.src.tinymodularthings.common.items.itemblocks.storage;

import java.util.List;
import java.util.Locale;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import speiger.src.api.client.render.IMetaItemRender;
import speiger.src.api.common.data.nbt.NBTHelper;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.tinymodularthings.client.render.storage.RenderStorageBlock;
import speiger.src.tinymodularthings.common.blocks.storage.TinyTank;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.items.core.TinyPlacerItem;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTinyTank extends TinyPlacerItem implements IMetaItemRender
{
	
	public ItemTinyTank(int par1)
	{
		super(par1);
		setCreativeTab(CreativeTabs.tabFood);
		setHasSubtypes(true);
	}
	

	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int i = 0; i < 9; i++)
		{
			par3List.add(new ItemStack(par1, 1, i));
		}
	}

	@Override
	public BlockStack getBlockToPlace(int meta)
	{
		return new BlockStack(TinyBlocks.storageBlock, 1);
	}

	@Override
	public String getName(ItemStack par1)
	{
		return "Tiny Tank";
	}
	
	@Override
	public boolean onAfterPlaced(World world, int x, int y, int z, int side, EntityPlayer player, ItemStack item)
	{
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if(tile != null && tile instanceof TinyTank)
		{
			TinyTank tank = (TinyTank)tile;
			tank.setTankMode(item.getItemDamage());
			tank.initTank();
			if(NBTHelper.nbtCheck(item, "Fluid"))
			{
				NBTTagCompound nbt = NBTHelper.getTag(item, "Fluid");
				FluidStack fluid = new FluidStack(nbt.getInteger("FluidID"), nbt.getInteger("Amount"));
				if(nbt.hasKey("Data"))
				{
					fluid.tag = nbt.getCompoundTag("Data");
				}
				tank.tank.setFluid(fluid);
			}
			return true;
		}
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1, EntityPlayer par2, List par3, boolean par4)
	{
		int[] tankSizes = new int[] {1000, 2000, 4000, 8000, 12000, 16000, 24000, 32000, 64000};
		par3.add("Tank Size: "+tankSizes[par1.getItemDamage()]+"mB");
		if(NBTHelper.nbtCheck(par1, "Fluid"))
		{
			NBTTagCompound nbt = NBTHelper.getTag(par1, "Fluid");
			FluidStack fluid = new FluidStack(nbt.getInteger("FluidID"), nbt.getInteger("Amount"));
			if (fluid != null)
			{
				par3.add("Stored Fluid: " + fluid.getFluid().getName().toUpperCase(Locale.GERMAN));
				par3.add("Amount: " + fluid.amount + "mB");
			}
		}
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
			case EQUIPPED: return new float[]{1.0F, 1.0F, 0.0F};
			case EQUIPPED_FIRST_PERSON: return new float[]{0.5F, 1.5F, 0.5F};
			case INVENTORY: return new float[]{0.0F, 1.0F, 0.0F};
			default: return null;
		}
	}

	@Override
	public void onRender(ItemRenderType type, ItemStack item, int renderPass, float x, float y, float z, Object... data)
	{
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(RenderStorageBlock.basicTCTexture);
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, z);
		GL11.glRotatef(180, 1, 0, 0);
		GL11.glRotatef(-90, 0, 1, 0);
		RenderStorageBlock.tinytank.render(0.0625F, false);
		GL11.glPopMatrix();
	}
}
