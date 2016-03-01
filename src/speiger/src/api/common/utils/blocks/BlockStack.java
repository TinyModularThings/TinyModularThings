package speiger.src.api.common.utils.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStack
{
	static Random rand = new Random();
	Block block;
	int meta;
	
	public BlockStack()
	{
		this(Blocks.air);
	}
	
	public BlockStack(NBTTagCompound nbt)
	{
		this(Block.getBlockFromName(nbt.getString("Block")), nbt.getInteger("Metadata"));
	}
	
	public BlockStack(TileEntity par1)
	{
		this(par1.getBlockType(), par1.getBlockMetadata());
	}
	
	public BlockStack(ItemStack par1)
	{
		this(Block.getBlockFromItem(par1.getItem()), par1.getItem().getMetadata(par1.getItemDamage()));
	}
	
	public BlockStack(IBlockAccess world, int x, int y, int z)
	{
		this(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z));
	}
	
	public BlockStack(BlockStack stack)
	{
		this(stack.getBlock(), stack.getMeta());
	}
	
	public BlockStack(Block block)
	{
		this(block, 0);
	}
	
	public BlockStack(int id, int meta)
	{
		this(Block.getBlockById(id), meta);
	}
	
	public BlockStack(Block block, int meta)
	{
		this.block = block;
		this.meta = meta;
	}
	
	public Block getBlock()
	{
		return block;
	}
	
	public int getMeta()
	{
		return meta;
	}
	
	public int getBlockID()
	{
		return Block.getIdFromBlock(getBlock());
	}

	@Override
	public int hashCode()
	{
		return getBlock().hashCode() + getMeta();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof BlockStack))
		{
			return false;
		}
		BlockStack blockstack = (BlockStack)obj;
		return getBlock() == blockstack.getBlock() && getMeta() == blockstack.getMeta();
	}
	
	public <T> T getCastedBlock(Class<T> claz)
	{
		if(claz.isAssignableFrom(getBlock().getClass()))
		{
			return (T)getBlock();
		}
		return null;
	}
	
	public boolean hasCast(Class clz)
	{
		return clz.isAssignableFrom(getBlock().getClass());
	}
	
	public boolean match(Block toCompare)
	{
		return getBlock() == toCompare;
	}
	
	public boolean match(Block toCompare, int meta)
	{
		return match(toCompare) && getMeta() == meta;
	}
	
	public boolean match(BlockStack stack)
	{
		return equals(stack);
	}
	
	public TileEntity getTileEntity()
	{
		return getTileEntity(null);
	}
	
	public TileEntity getTileEntity(World world)
	{
		try
		{
			return getBlock().createTileEntity(world, getMeta());
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public Item toItem()
	{
		return Item.getItemFromBlock(getBlock());
	}
	
	public ItemStack toStack()
	{
		return toStack(1);
	}
	
	public ItemStack toStack(int count)
	{
		return new ItemStack(getBlock(), count, getMeta());
	}
	
	public String getDisplayName()
	{
		return toStack().getDisplayName();
	}
	
	public String getHiddenName()
	{
		return toStack().getUnlocalizedName();
	}

	public IIcon getIcon(int side)
	{
		return getBlock().getIcon(side, getMeta());
	}
	
	public ItemStack getItemDrop()
	{
		return getItemDrop(0);
	}
	
	public ItemStack getItemDrop(int fortune)
	{
		return new ItemStack(getBlock().getItemDropped(getMeta(), rand, fortune), getBlock().quantityDropped(getMeta(), fortune, rand), getBlock().damageDropped(getMeta()));
	}
	
	public NBTTagCompound toNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("Block", Block.blockRegistry.getNameForObject(block));
		nbt.setInteger("Metadata", meta);
		return nbt;
	}
}
