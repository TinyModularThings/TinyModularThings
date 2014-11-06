package speiger.src.api.common.world.blocks;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import speiger.src.spmodapi.common.util.proxy.CodeProxy;

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
		this(Block.blocksList[((ItemBlock)par1.getItem()).getBlockID()], par1.getItem().getMetadata(par1.getItemDamage()));
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
	
	public ItemStack getAsDroppedStack()
	{
		if(blocks == null)
		{
			return null;
		}
		int id = blocks.idDropped(meta, CodeProxy.getRandom(), 0);
		if(id <= 0)
		{
			return null;
		}
		return new ItemStack(id, 1, blocks.damageDropped(meta));
	}
	
	public ItemStack getAsDroppedStack(BlockPosition par1)
	{
		if(blocks == null)
		{
			return asItemStack();
		}
		ArrayList<ItemStack> stack = blocks.getBlockDropped(par1.getWorld(), par1.getXCoord(), par1.getYCoord(), par1.getZCoord(), meta, 0);
		if(stack == null)
		{
			return asItemStack();
		}
		return stack.get(0);
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
	
	public String getDroppedBlockDisplayName()
	{
		if(blocks == null)
		{
			return "No Block!";
		}
		int id = blocks.idDropped(meta, CodeProxy.getRandom(), 0);
		if(id <= 0)
		{
			return "No Block!";
		}
		ItemStack result = new ItemStack(id, 1, blocks.damageDropped(meta));
		return result.getDisplayName();
	}
	
	public String getPickedBlockDisplayName(BlockPosition pos, int side)
	{
		return this.getPicketBlock(pos, side).getDisplayName();
	}
	
	public String getDroppedBlockDisplayName(BlockPosition par1)
	{
		if(blocks == null || par1 == null)
		{
			return "No Block!";
		}
		ArrayList<ItemStack> drops = blocks.getBlockDropped(par1.getWorld(), par1.getXCoord(), par1.getYCoord(), par1.getZCoord(), meta, 0);
		if(drops == null || drops.size() <= 0)
		{
			return "No Block!";
		}
		ItemStack stack = drops.get(0);
		if(stack == null)
		{
			return "No Block!";
		}
		return stack.getDisplayName();
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
	
	public ItemStack getPicketBlock(BlockPosition pos, int side)
	{
		return blocks.getPickBlock(new MovingObjectPosition(pos.getXCoord(), pos.getYCoord(), pos.getZCoord(), side, Vec3.createVectorHelper(pos.getXCoord(), pos.getYCoord(), pos.getZCoord())), pos.getWorld(), pos.getXCoord(), pos.getYCoord(), pos.getZCoord());
	}

	@Override
	public boolean equals(Object obj)
	{
		if(obj == null)
		{
			return false;
		}
		if(!(obj instanceof BlockStack))
		{
			return false;
		}
		BlockStack stack = (BlockStack)obj;
		return stack.match(this);
	}

	@Override
	public int hashCode()
	{
		return this.getBlockID() + this.getMeta();
	}
	
}
