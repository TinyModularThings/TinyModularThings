package speiger.src.spmodapi.common.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import buildcraft.api.core.SafeTimeTracker;

import net.minecraft.block.Block;
import net.minecraft.block.StepSound;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.common.utils.RedstoneUtils;
import speiger.src.api.common.world.blocks.BlockPosition;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.enums.EnumColor.SpmodColor;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.proxy.CodeProxy;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class AdvTile extends TileEntity
{
	public ArrayList<String> users = new ArrayList<String>();
	public boolean init = false;
	SafeTimeTracker tracker = new SafeTimeTracker();
	private int clock = CodeProxy.getRandom().nextInt();
	
	public void onPlayerOpenContainer(EntityPlayer par1)
	{
		if(!users.contains(par1.username))
		{
			users.add(par1.username);
		}
	}
	
	public int getClockTime()
	{
		return clock;
	}
	
	protected void onClockTick()
	{
		clock++;
	}
	
	public boolean clockCanTick()
	{
		if(getWorldObj() == null)
		{
			setWorldObj(DimensionManager.getWorld(0));
		}
		return tracker.markTimeIfDelay(getWorldObj(), 20);
	}
	
	public boolean hasUsers()
	{
		return !users.isEmpty();
	}
	
	public void init()
	{
		
	}
	
	public void onPlayerCloseContainer(EntityPlayer par1)
	{
		if(users.contains(par1.username))
		{
			users.remove(par1.username);
		}
	}
	
	public int getUserSize()
	{
		return users.size();
	}
	
	public void loadInformation(List par1)
	{
		
	}
	
	@SideOnly(Side.CLIENT)
	public void onItemInformation(EntityPlayer par1, List par2, ItemStack par3)
	{
		
	}
	
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(int side)
	{
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	public SpmodColor getColor()
	{
		return new SpmodColor(16777215);
	}
	
	public StepSound getStepSound()
	{
		return getBlockType().stepSound;
	}
	
	public float getBlockHardness()
	{
		return getBlockType().blockHardness;
	}
	
	public float getExplosionResistance(Entity par1)
	{
		return getBlockType().getExplosionResistance(par1);
	}
	
	public int getBlockLightLevel()
	{
		return 0;
	}
	
	public int getLightOpactiy()
	{
		return 0;
	}
	
	public void updateNeighbors(boolean needSelf)
	{
		if(needSelf)
		{
			worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType().blockID);
		}
		for(ForgeDirection side : ForgeDirection.VALID_DIRECTIONS)
		{
			notifyBlocksOfNeighborChange(side);
		}
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, nbt);
	}
	
	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
	{
		readFromNBT(pkt.data);
	}

	protected void notifyBlocksOfNeighborChange(ForgeDirection side)
	{
		worldObj.notifyBlocksOfNeighborChange(xCoord + side.offsetX, yCoord + side.offsetY, zCoord + side.offsetZ, getBlockType().blockID);
	}
	
	public ArrayList<AxisAlignedBB> getColidingBoxes(Entity par2)
	{
		return null;
	}
	
	public boolean isPowered()
	{
		return RedstoneUtils.isBlockGettingPowered(this);
	}
	
	public AxisAlignedBB getSelectedBoxes()
	{
		return AxisAlignedBB.getAABBPool().getAABB((double)xCoord, (double)yCoord, (double)zCoord, (double)xCoord+1, (double)yCoord+1, (double)zCoord+1);
	}
	
	public AxisAlignedBB getColidingBox()
	{
		return AxisAlignedBB.getAABBPool().getAABB((double)xCoord, (double)yCoord, (double)zCoord, (double)xCoord+1, (double)yCoord+1, (double)zCoord+1);
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		
		if(!SpmodConfig.booleanInfos.get("LoadTileEntities"))
		{
			return;
		}
		
		if(!init)
		{
			init = true;
			init();
		}
		
		if(clockCanTick())
		{
			onClockTick();
		}
		
		onTick();
	}
	
	public void updateBlock()
	{
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		if(worldObj.blockExists(xCoord, yCoord, zCoord))
		{
			worldObj.getChunkFromBlockCoords(xCoord, zCoord).setChunkModified();
		}
	}
	
	public void updateLight()
	{
		this.worldObj.updateAllLightTypes(this.xCoord, this.yCoord, this.zCoord);
	}
	
	public void onTick()
	{
		
	}
	
	public boolean onClick(boolean sneak, EntityPlayer par1, Block par2, int side)
	{
		return false;
	}
	
	public void onBreaking()
	{
	}
	
	public float getHardnessForPlayer(EntityPlayer par1)
	{
		return getBlockHardness();
	}
	
	public int getDroppedExp()
	{
		return -1;
	}
	
	public void onDistroyedByExplosion(Explosion par0)
	{
		
	}
	
	public boolean canPlacedOnSide(ForgeDirection side)
	{
		return side == side.UNKNOWN ? false : canPlaced();
	}
	
	public boolean canPlaced()
	{
		return true;
	}
	
	public boolean onActivated(EntityPlayer par1)
	{
		return false;
	}
	
	public boolean onActivated(EntityPlayer par1, int side)
	{
		return onActivated(par1);
	}
	
	public boolean onOpened(EntityPlayer par1, int side)
	{
		return onActivated(par1, side);
	}
	
	public void onEntityWalk(Entity par1)
	{
		
	}
	
	public void onPlaced(int facing)
	{
		
	}
	
	public int isPowering(int side)
	{
		return 0;
	}
	
	public int isIndirectlyPowering(int side)
	{
		return isPowering(side);
	}
	
	public void onColide(Entity par1)
	{
		
	}
	
	public boolean SolidOnSide(ForgeDirection side)
	{
		return true;
	}
	
	public boolean canBeReplaced()
	{
		return false;
	}
	
	public boolean isAir()
	{
		return false;
	}
	
	public void onIconMakerLoading()
	{
		
	}
	
	public boolean removeAbleByPlayer(EntityPlayer player)
	{
		return true;
	}
	
	public int getFireBurnLenght(ForgeDirection side)
	{
		return 0;
	}
	
	public int getFireSpreadSpeed(ForgeDirection side)
	{
		return 0;
	}
	
	public ArrayList<ItemStack> onDrop(int fortune)
	{
		ArrayList<ItemStack> drop = new ArrayList<ItemStack>();
		if(dropNormalBlock())
		{
			try
			{
				drop.add(new ItemStack(this.getBlockType(), 1, worldObj.getBlockMetadata(xCoord, yCoord, zCoord)));
			}
			catch(Exception e)
			{
			}
		}
		return drop;
	}
	
	public boolean dropNormalBlock()
	{
		return true;
	}
	
	public boolean isSilkHarvestable(EntityPlayer player)
	{
		return false;
	}
	
	public boolean canMonsterSpawn(EnumCreatureType type)
	{
		return true;
	}
	
	public boolean canConnectToWire(int side)
	{
		return false;
	}
	
	public boolean canPlaceTorchOnTop()
	{
		return true;
	}
	
	public boolean canEntityDistroyMe(Entity par1)
	{
		return true;
	}
	
	public boolean shouldCheckWeakPower(int side)
	{
		return false;
	}
	
	public void registerIcon(TextureEngine par1)
	{
		
	}
	
	public abstract Icon getIconFromSideAndMetadata(int side, int renderPass);
	
	public boolean hasContainer()
	{
		return false;
	}
	
	public Container getInventory(InventoryPlayer par1)
	{
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	public GuiContainer getGui(InventoryPlayer par1)
	{
		return null;
	}
	
	public void onReciveGuiInfo(int key, int val)
	{
		
	}
	
	public void onSendingGuiInfo(Container par1, ICrafting par2)
	{
		
	}
	
	public ItemStack slotClicked(Container par1, int slotID, int mouseButton, int modifire, EntityPlayer player)
	{
		return par1.slotClick(slotID, mouseButton, modifire, player);
	}
	
	public ItemStack onItemTransfer(Container par1, EntityPlayer player, int slotID)
	{
		return par1.transferStackInSlot(player, slotID);
	}
	
	public void onAdvPlacing(int rotation, int facing)
	{
		this.onPlaced(facing);
	}
	
	public void setupUser(EntityPlayer player)
	{
		
	}
	
	public ItemStack pickBlock(MovingObjectPosition target)
	{
		return new ItemStack(worldObj.getBlockId(xCoord, yCoord, zCoord), 1, worldObj.getBlockMetadata(xCoord, yCoord, zCoord));
	}
	
	public void setBoundsOnState(Block par1)
	{
		par1.setBlockBounds(0, 0, 0, 1, 1, 1);
	}
	
	public void onClientTick()
	{
	}
	
	public BlockPosition getPosition()
	{
		return new BlockPosition(worldObj, xCoord, yCoord, zCoord);
	}
	
	public Logger getDebugLogger()
	{
		return FMLLog.getLogger();
	}
	
	public int upcastShort(int value)
	{
		if(value < 0)
			value += 65536;
		return value;
	}
	
	public boolean isFireSource(ForgeDirection side)
	{
		return false;
	}
	
	public boolean isNormalCube()
	{
		Block block = this.getBlockType();
		return block.blockMaterial.isOpaque() && block.renderAsNormalBlock() && !block.canProvidePower();
	}
	
	public void onFallen(Entity par5Entity)
	{
		
	}

	public void onBlockChange(Block par1, int par2)
	{
		
	}
	
	@SideOnly(Side.CLIENT)
	public void onRender(RenderBlocks render, BlockStack stack, int renderPass)
	{
		
	}

	public boolean dissableRenderer()
	{
		return false;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound par1)
	{
		users.clear();
		super.readFromNBT(par1);
		NBTTagList list = par1.getTagList("Users");
		for(int i = 0;i<list.tagCount();i++)
		{
			NBTTagString text = (NBTTagString)list.tagAt(i);
			users.add(text.data);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound par1)
	{
		super.writeToNBT(par1);
		NBTTagList list = new NBTTagList();
		for(String user : users)
		{
			list.appendTag(new NBTTagString("Key", user));
		}
		par1.setTag("Users", list);
	}
	
}
