package speiger.src.tinymodularthings.common.items.minecarts;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.BlockRailBase;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import speiger.src.api.client.render.IMetaItemRender;
import speiger.src.tinymodularthings.client.models.storage.ModelTinyChest;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityTinyChestCart.EightSlotTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityTinyChestCart.FiveSlotTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityTinyChestCart.FourSlotTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityTinyChestCart.NineSlotTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityTinyChestCart.OneSlotTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityTinyChestCart.SevenSlotTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityTinyChestCart.SixSlotTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityTinyChestCart.ThreeSlotTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityTinyChestCart.TwoSlotTinyChestCart;
import speiger.src.tinymodularthings.common.items.core.TinyItem;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TinyChestCart extends TinyItem implements IMetaItemRender
{
	
	public TinyChestCart(int par1)
	{
		super(par1);
		setCreativeTab(CreativeTabs.tabFood);
		setHasSubtypes(true);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1, EntityPlayer par2, List par3, boolean par4)
	{
		par3.add((1+par1.getItemDamage())+ (par1.getItemDamage() > 0 ? " Slots" : " Slot"));
	}
	
	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
	{
		int i1 = par3World.getBlockId(par4, par5, par6);
		
		if (BlockRailBase.isRailBlock(i1))
		{
			if (!par3World.isRemote)
			{
				EntityMinecart entityminecart = getMinecartFromMeta(par3World, par4, par5, par6, par1ItemStack.getItemDamage());
				
				if (par1ItemStack.hasDisplayName())
				{
					entityminecart.setMinecartName(par1ItemStack.getDisplayName());
				}
				
				par3World.spawnEntityInWorld(entityminecart);
			}
			
			--par1ItemStack.stackSize;
			return true;
		}
		else
		{
			return false;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3)
	{
		for (int i = 0; i < 9; i++)
		{
			par3.add(new ItemStack(par1, 1, i));
		}
	}
	
	public EntityMinecart getMinecartFromMeta(World world, int x, int y, int z, int meta)
	{
		switch (meta)
		{
			case 0:
				return new OneSlotTinyChestCart(world, x, y, z);
			case 1:
				return new TwoSlotTinyChestCart(world, x, y, z);
			case 2:
				return new ThreeSlotTinyChestCart(world, x, y, z);
			case 3:
				return new FourSlotTinyChestCart(world, x, y, z);
			case 4:
				return new FiveSlotTinyChestCart(world, x, y, z);
			case 5:
				return new SixSlotTinyChestCart(world, x, y, z);
			case 6:
				return new SevenSlotTinyChestCart(world, x, y, z);
			case 7:
				return new EightSlotTinyChestCart(world, x, y, z);
			case 8:
				return new NineSlotTinyChestCart(world, x, y, z);
			default:
				return EntityMinecart.createMinecart(world, x, y, z, 0);
		}
	}

	@Override
	public String getName(ItemStack par1)
	{
		return "TinyChest Cart";
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
			case ENTITY: return new float[]{0.0F, 0F, 0.0F};
			case EQUIPPED: return new float[]{0.5F, 1F, 0.0F};
			case EQUIPPED_FIRST_PERSON: return new float[]{0.5F, 1.0F, 0.5F};
			case INVENTORY: return new float[]{0.0F, -0.2F, 0.0F};
			default: return null;
		}
	}
	
	public float[] getSecondXYZ(ItemRenderType type)
	{
		switch(type)
		{
			case ENTITY: return new float[]{0.0F, -1.2F, 0.0F};
			case EQUIPPED: return new float[]{0F, -1.2F, 0.0F};
			case EQUIPPED_FIRST_PERSON: return new float[]{0.0F, -1.2F, -0.01F};
			case INVENTORY: return new float[]{0.0F, -1.2F, 0.0F};
			default: return null;
		}
	}

	ModelMinecart cart = new ModelMinecart();
	ModelTinyChest chest = new ModelTinyChest();
	private static final ResourceLocation minecartTextures = new ResourceLocation("textures/entity/minecart.png");
	private static final ResourceLocation tinyChestTexture = new ResourceLocation(TinyModularThingsLib.ModID.toLowerCase() + ":textures/models/storage/ModelTinyChest.png");
	
	
	@Override
	public void onRender(ItemRenderType type, ItemStack item, int renderPass, float x, float y, float z, Object... data)
	{
		float[] second = getSecondXYZ(type);
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(minecartTextures);
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, z);
		GL11.glRotatef(180, 1, 0, 0);
		GL11.glRotatef(0, 0, 1, 0);
		cart.render(null, 0.0F, 0.0F, 0F, 0.0F, 0.0F, 0.0625F);
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(tinyChestTexture);
		GL11.glTranslatef(second[0], second[1], second[2]);
		GL11.glScalef(0.9F, 1, 0.9F);
		chest.render(0.0625F);
		GL11.glPopMatrix();
		
	}
	
}
