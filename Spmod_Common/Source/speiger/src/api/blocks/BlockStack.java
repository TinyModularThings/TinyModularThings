package speiger.src.api.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
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
	
	public BlockStack(TileEntity par1)
	{
		this(par1.getBlockType(), par1.getBlockMetadata());
	}
	
	public BlockStack(ItemStack par1)
	{
		this(Block.blocksList[par1.itemID], par1.getItemDamage());
	}
	
	public BlockStack(Block block)
	{
		this(block, 0);
	}
	
	public BlockStack(int id)
	{
		this(Block.blocksList[id]);
	}
	
	public BlockStack(IBlockAccess world, int x, int y, int z)
	{
		this(world.getBlockId(x, y, z), world.getBlockMetadata(x, y, z));
	}
	
	public BlockStack(int id, int meta)
	{
		this(Block.blocksList[id], meta);
	}
	
	public BlockStack(BlockStack block)
	{
		this(block.blocks, block.meta);
	}
	
	public BlockStack(ItemBlock par1)
	{
		this(par1.getBlockID());
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
		if (blocks == null)
		{
			return 0;
		}
		return blocks.blockID;
	}
	
	public int getMeta()
	{
		return meta;
	}
	
	public Icon getTexture(int side)
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
		return item.getItemDisplayName(stack);
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
		return world.setBlock(x, y, z, blocks.blockID, meta, 3);
	}
	
}
