package speiger.src.api.common.utils.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPosition implements IPosition
{
	World world;
	int xCoord;
	int yCoord;
	int zCoord;
	
	@SideOnly(Side.CLIENT)
	public BlockPosition(int x, int y, int z)
	{
		this(Minecraft.getMinecraft().theWorld, x, y, z);
	}
	
	@SideOnly(Side.SERVER)
	public BlockPosition(List<Integer> par1)
	{
		this(par1.get(0), par1.get(1), par1.get(2), par1.get(3));
	}
	
	public BlockPosition(NBTTagCompound par1)
	{
		this(par1.getInteger("World"), par1.getInteger("xCoord"), par1.getInteger("yCoord"), par1.getInteger("zCoord"));
	}
	
	public BlockPosition(int dim, int x, int y, int z)
	{
		this(DimensionManager.getWorld(dim), x, y, z);
	}
	
	public BlockPosition(Entity entity)
	{
		this(entity.worldObj, MathHelper.floor_double(entity.posX), MathHelper.floor_double(entity.posY), MathHelper.floor_double(entity.posZ));
	}
	
	public BlockPosition(TileEntity tile)
	{
		this(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);
	}
	
	public BlockPosition(World worldObj, int x, int y, int z)
	{
		world = worldObj;
		xCoord = x;
		yCoord = y;
		zCoord = z;
	}
	
	public BlockPosition applyDir(ForgeDirection dir)
	{
		return new BlockPosition(world, xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
	}
	
	public World getWorld()
	{
		return world;
	}
	
	public int getX()
	{
		return xCoord;
	}
	
	public int getY()
	{
		return yCoord;
	}
	
	public int getZ()
	{
		return zCoord;
	}
	
	public ChunkCoordinates toCoords()
	{
		return new ChunkCoordinates(xCoord, yCoord, zCoord);
	}
	
	@Override
	public ICoord toICoords()
	{
		return new BlockCoord(this);
	}
	
	public BlockStack toBlockStack()
	{
		return new BlockStack(world, xCoord, yCoord, zCoord);
	}
	
	public ItemStack toItemStack()
	{
		return toBlockStack().toStack();
	}
	
	public Item toItem()
	{
		return toBlockStack().toItem();
	}
	
	public Block getBlock()
	{
		return world.getBlock(xCoord, yCoord, zCoord);
	}
	
	public int getMeta()
	{
		return world.getBlockMetadata(xCoord, yCoord, zCoord);
	}
	
	public TileEntity getTile()
	{
		return world.getTileEntity(xCoord, yCoord, zCoord);
	}
	
	public <T> T getCastedTile(Class<T> clz)
	{
		TileEntity tile = getTile();
		if(clz.isAssignableFrom(tile.getClass()))
		{
			return (T)tile;
		}
		return null;
	}
	
	public boolean hasTileCast(Class clz)
	{
		return clz.isAssignableFrom(getTile().getClass());
	}
	
	public boolean hasTile()
	{
		return world.getTileEntity(xCoord, yCoord, zCoord) != null;
	}

	public void removeBlock()
	{
		world.setBlockToAir(xCoord, yCoord, zCoord);
	}
	
	public boolean isAir()
	{
		return world.isAirBlock(xCoord, yCoord, zCoord);
	}
	
	@Override
	public int hashCode()
	{
		return world.provider.dimensionId + xCoord + yCoord + zCoord;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof BlockPosition)
		{
			BlockPosition pos = (BlockPosition)obj;
			if(pos.world.provider.dimensionId != world.provider.dimensionId)
			{
				return false;
			}
			if(xCoord != pos.xCoord || yCoord != pos.yCoord || zCoord != pos.zCoord)
			{
				return false;
			}
			return true;
		}
		return false;
	}
	
	public NBTTagCompound toNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("World", world == null ? 0 : world.provider.dimensionId);
		nbt.setInteger("xCoord", xCoord);
		nbt.setInteger("yCoord", yCoord);
		nbt.setInteger("zCoord", zCoord);
		return nbt;
	}
	
	@Override
	public String toString()
	{
		return "World: "+(world == null ? 0 : world.provider.dimensionId)+":"+xCoord+":"+yCoord+":"+zCoord;
	}
}
