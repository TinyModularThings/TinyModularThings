package speiger.src.tinymodularthings.common.blocks.transport;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import speiger.src.api.blocks.BlockStack;
import speiger.src.api.energy.EnergyProvider;
import speiger.src.api.energy.IEnergyProvider;
import speiger.src.api.inventory.IAcceptor;
import speiger.src.api.inventory.InterfaceAcceptor;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.BlockPosition;
import speiger.src.spmodapi.common.util.data.StructureStorage;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.items.itemblocks.transport.ItemInterfaceBlock;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;

public class MultiStructureEnergyInterface extends AdvTile implements
		IEnergyProvider, IPowerReceptor, IAcceptor
{
	
	public int blockID = -1;
	public int metadata = -1;
	
	// Structure Coords
	public int x = 0;
	public int y = 0;
	public int z = 0;
	
	IEnergyProvider target = null;
	
	public boolean textureUpdate = false;
	
	@Override
	public IIcon getIconFromSideAndMetadata(int side, int renderPass)
	{
		if (blockID != -1 && metadata != -1)
		{
			return new BlockStack(blockID, metadata).getTexture(side);
		}
		return null;
	}
	
	@Override
	public ItemStack pickBlock(MovingObjectPosition target)
	{
		ItemStack stack = new ItemStack(TinyItems.interfaceBlock, 1, 2);
		ItemInterfaceBlock.addBlockToInterface(stack, new BlockStack(blockID, metadata));
		return stack;
	}

	@Override
	public ArrayList<ItemStack> onDrop(int fortune)
	{
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		ItemStack stack = new ItemStack(TinyItems.interfaceBlock, 1, 2);
		ItemInterfaceBlock.addBlockToInterface(stack, new BlockStack(blockID, metadata));
		drops.add(stack);
		return drops;
	}
	
	@Override
	public EnergyProvider getEnergyProvider(ForgeDirection side)
	{
		if (target != null)
		{
			return target.getEnergyProvider(side);
		}
		return null;
	}
	
	@Override
	public PowerReceiver getPowerReceiver(ForgeDirection side)
	{
		if (target != null)
		{
			return target.getEnergyProvider(side).getSaveBCPowerProvider();
		}
		return null;
	}
	
	@Override
	public void doWork(PowerHandler workProvider)
	{
	}
	
	@Override
	public World getWorld()
	{
		return worldObj;
	}
	
	public void removeInventory()
	{
		target = null;
		x = 0;
		y = 0;
		z = 0;
	}
	
	@Override
	public void onTick()
	{
		if (worldObj.isRemote || worldObj.getWorldTime() % 20 != 0)
		{
			return;
		}
		
		if (!worldObj.isRemote && (textureUpdate || worldObj.getWorldTime() % 200 == 0))
		{
			textureUpdate = false;
			PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, getDescriptionPacket());
		}
		
		if (!worldObj.isRemote)
		{
			updateData();
		}
	}
	
	public void updateData()
	{
		if ((x == 0 && y == 0 && z == 0) || !doesExsist() || (doesExsist() && target == null))
		{
			if (!doesExsist())
			{
				if (target == null)
				{
					return;
				}
				removeInventory();
				return;
			}
			findInventory();
		}
	}
	
	public void findInventory()
	{
		if (x == 0 && y == 0 && z == 0)
		{
			BlockPosition pos = StructureStorage.instance.getCorePosition(getPosition());
			if (pos != null && pos.doesBlockExsist() && pos.hasTileEntity() && pos.getTileEntity() instanceof InterfaceAcceptor)
			{
				InterfaceAcceptor inter = (InterfaceAcceptor) pos.getTileEntity();
				if (inter != null && inter.acceptEnergy(this) && inter.addAcceptor(this))
				{
					target = (IEnergyProvider) pos.getTileEntity();
					x = pos.xCoord;
					y = pos.yCoord;
					z = pos.zCoord;
					worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType().blockID);
				}
			}
		}
		else
		{
			BlockPosition pos = new BlockPosition(worldObj, x, y, z);
			if (pos != null && pos.doesBlockExsist() && pos.hasTileEntity() && pos.getTileEntity() instanceof InterfaceAcceptor)
			{
				InterfaceAcceptor inter = (InterfaceAcceptor) pos.getTileEntity();
				if (inter.acceptEnergy(this) && inter.addAcceptor(this))
				{
					target = (IEnergyProvider) pos.getTileEntity();
					worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType().blockID);
				}
			}
		}
	}
	
	public boolean doesExsist()
	{
		if (x == 0 && y == 0 && z == 0)
		{
			return StructureStorage.instance.isRegistered(getPosition());
		}
		else
		{
			BlockPosition pos = new BlockPosition(worldObj, x, y, z);
			return pos != null && pos.doesBlockExsist() && pos.hasTileEntity() && pos.getTileEntity() instanceof InterfaceAcceptor;
		}
	}
	
	@Override
	public AcceptorType getType()
	{
		return AcceptorType.Energy;
	}
	
	@Override
	public boolean isBlock(BlockStack par1)
	{
		return blockID == par1.getBlockID() && metadata == par1.getMeta();
	}
	
	@Override
	public void setBlock(BlockStack par1)
	{
		blockID = par1.getBlockID();
		metadata = par1.getMeta();
		textureUpdate = true;
	}
	
	@Override
	public BlockStack getBlock()
	{
		if (blockID != -1 && metadata != -1)
		{
			return new BlockStack(blockID, metadata);
		}
		return null;
	}
	
	@Override
	public void targetLeave(TileEntity tile)
	{
		if (x == tile.xCoord && y == tile.yCoord && z == tile.zCoord && worldObj.provider.dimensionId == tile.worldObj.provider.dimensionId)
		{
			removeInventory();
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound par1)
	{
		super.readFromNBT(par1);
		int[] array = par1.getIntArray("BlockStack");
		if (array != null && array.length == 2)
		{
			blockID = array[0];
			metadata = array[1];
		}
		array = par1.getIntArray("Coords");
		if (array != null && array.length == 3)
		{
			x = array[0];
			y = array[1];
			z = array[2];
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound par1)
	{
		super.writeToNBT(par1);
		par1.setIntArray("BlockStack", new int[] { blockID, metadata });
		par1.setIntArray("Coords", new int[] { x, y, z });
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, nbt);
	}
	
	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
	{
		readFromNBT(pkt.data);
	}
	
}