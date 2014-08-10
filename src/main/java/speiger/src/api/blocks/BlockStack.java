package speiger.src.api.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * 
 * @author Speiger
 * 
 */
public class BlockStack
{
	Block blocks;
	int meta;
	
	/**
	 * Stack Version of Blocks (Like ItemStacks it has his special abilities
	 */
	
	public BlockStack()
	{
		blocks = null;
		meta = 0;
	}
	
	public BlockStack(ItemStack par1)
	{
		this(Item.getIdFromItem(par1.getItem()), par1.getItemDamage());
	}

	public BlockStack(Item item)
	{
		this(item, 0);
	}

	public BlockStack(Item item, int meta)
	{
		this(Item.getIdFromItem(item), meta);
	}

	public BlockStack(Block block)
	{
		this(block, 0);
	}
	
	@Deprecated
	public BlockStack(int id)
	{
		this(id, 0);
	}
	
	public BlockStack(IBlockAccess world, int x, int y, int z)
	{
		this(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z));
	}
	//I leave this at it is but we will remove the @Deprecated because its required for NBTData
	@Deprecated
	public BlockStack(int id, int meta)
	{
		this(Block.getBlockById(id), meta);
	}
	
	public BlockStack(BlockStack block)
	{
		this(block.blocks, block.meta);
	}
	
	public BlockStack(ItemBlock par1)
	{
		this(Block.getBlockFromItem(par1));
	}
	
	public BlockStack(Block block, int metadata)
	{
		blocks = block;
		meta = metadata;
	}

	public ItemStack asItemStack()
	{
		return new ItemStack(blocks, 1, meta);
	}
	
	public Block getBlock()
	{
		return blocks;
	}
	
	public int getBlockID()
	{
		return Block.getIdFromBlock(blocks);
	}

	public int getMeta()
	{
		return meta;
	}
	
	public IIcon getTexture(int side)
	{
		return blocks.getIcon(side, meta);
	}
	
	public String getBlockDisplayName()
	{
		if (blocks == null)
		{
			return "No Block!";
		}
		ItemStack stack = new ItemStack(blocks, 1, meta);
		Item item = stack.getItem();
		return item.getItemStackDisplayName(stack);
	}
	
	public String getHiddenName()
	{
		if (blocks == null)
		{
			return "No Block!";
		}
		ItemStack stack = new ItemStack(blocks, 1, meta);
		return stack.getUnlocalizedName();
	}
	
	public boolean match(BlockStack block)
	{
		if (block.blocks == blocks && block.meta == meta)
		{
			return true;
		}
		return false;
	}
	
	public boolean placeBlock(World world, int x, int y, int z)
	{
		return world.setBlock(x, y, z, blocks, meta, 3);
	}
	
	public boolean hasBlock()
	{
		return blocks != null;
	}
	
	public boolean hasTileEntity()
	{
		return blocks.hasTileEntity(meta);
	}
	
}
