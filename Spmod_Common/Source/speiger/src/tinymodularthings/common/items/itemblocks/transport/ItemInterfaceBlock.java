package speiger.src.tinymodularthings.common.items.itemblocks.transport;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import speiger.src.api.client.render.IMetaItemRender;
import speiger.src.api.common.data.nbt.NBTHelper;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.api.common.world.tiles.interfaces.IAcceptor;
import speiger.src.spmodapi.client.render.core.BlockRendererSpmodCore.BlockRendererHelper;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.proxy.LangProxy;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.items.core.TinyPlacerItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemInterfaceBlock extends TinyPlacerItem implements IMetaItemRender
{
	
	public ItemInterfaceBlock(int par1)
	{
		super(par1);
		setHasSubtypes(true);
		setCreativeTab(CreativeTabs.tabFood);
	}
	
	
	public static ItemStack getBlockFromInterface(ItemStack par1)
	{
		NBTTagCompound nbt = par1.getTagCompound().getCompoundTag("Interface");
		return new BlockStack(nbt.getInteger("ID"), nbt.getInteger("Meta")).asItemStack();
	}
	
	public static boolean isValidBlock(ItemStack par1)
	{
		NBTTagCompound nbt = par1.getTagCompound().getCompoundTag("Interface");
		if (Block.blocksList[nbt.getInteger("ID")] != null)
		{
			return true;
		}
		return false;
	}
	
	public static ItemStack addBlockToInterface(ItemStack par1, BlockStack stack)
	{
		if (!par1.hasTagCompound())
		{
			par1.setTagInfo("Interface", new NBTTagCompound());
		}
		NBTTagCompound nbt = par1.getTagCompound().getCompoundTag("Interface");
		nbt.setInteger("ID", stack.getBlockID());
		nbt.setInteger("Meta", stack.getMeta());
		return par1;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3)
	{
		for (int i = 0; i < 3; i++)
		{
			par3.add(createInterface(par1, i));
		}
	}
	
	public static ItemStack createInterface(int id, int meta)
	{
		ItemStack stack = new ItemStack(id, 1, meta);
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("ID", 0);
		nbt.setInteger("Meta", 0);
		NBTTagCompound end = new NBTTagCompound();
		end.setCompoundTag("Interface", nbt);
		stack.setTagCompound(end);
		
		return stack;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int par1)
	{
		return TextureEngine.getTextures().getTexture(new BlockStack(TinyBlocks.transportBlock, par1+1), par1);
	}
	
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1, EntityPlayer par2, List par3, boolean par4)
	{
		if(NBTHelper.nbtCheck(par1, "Interface"))
		{
			NBTTagCompound nbt = NBTHelper.getTag(par1, "Interface");
			int id = nbt.getInteger("ID");
			int meta = nbt.getInteger("Meta");
			if(id > 0)
			{
				BlockStack stack = new BlockStack(id, meta);
				par3.add("Stored Block: "+stack.getBlockDisplayName());
			}
			else
			{
				par3.add("No Block Stored");
			}
		}
	}


	@Override
	@SideOnly(Side.CLIENT)
	public int getSpriteNumber()
	{
		return 0;
	}
	
	public int getMetaFromDamage(ItemStack par1)
	{
		int meta = par1.getItemDamage();
		return meta + 1;
	}
	
	@Override
	public BlockStack getBlockToPlace(int meta)
	{
		return new BlockStack(TinyBlocks.transportBlock, meta+1);
	}
	
	@Override
	public String getName(ItemStack par1)
	{
		switch(par1.getItemDamage())
		{
			case 0: return "Item Interface";
			case 1: return "Fluid Interface";
			case 2: return "Energy Interface";
		}
		return null;
	}
	
	@Override
	public boolean onAfterPlaced(World world, int x, int y, int z, int side, EntityPlayer player, ItemStack item)
	{
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if(tile != null && tile instanceof IAcceptor)
		{
			IAcceptor accept = (IAcceptor)tile;
			boolean flag = false;
			
			if(NBTHelper.nbtCheck(item, "Interface"))
			{
				flag = true;
				NBTTagCompound nbt = NBTHelper.getTag(item, "Interface");
				if(nbt.getInteger("ID") == 0)
				{
					flag = false;
					player.sendChatToPlayer(LangProxy.getText("Need Internal Block", EnumChatFormatting.RED));
				}
				if(flag)
				{
					accept.setBlock(new BlockStack(nbt.getInteger("ID"), nbt.getInteger("Meta")));
					return true;
				}
				world.setBlockToAir(x, y, z);
			}
			
		}
		return false;
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

	
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon[] getTextureArray(int meta)
	{
		Icon texture = TextureEngine.getTextures().getTexture(new BlockStack(TinyBlocks.transportBlock, meta+1), 0);
		return new Icon[]{texture, texture, texture, texture, texture, texture};
	}


	@Override
	public void onRender(ItemRenderType type, ItemStack item, int renderPass, float x, float y, float z, Object... data)
	{
		BlockRendererHelper.renderBlockStandart(getTextureArray(item.getItemDamage()), new float[]{0.35F, 0.35F, 0.35F, 0.65F, 0.65F, 0.65F}, TinyBlocks.transportBlock, new float[]{x,y,z}, ((RenderBlocks)data[0]));
	}
	
}
