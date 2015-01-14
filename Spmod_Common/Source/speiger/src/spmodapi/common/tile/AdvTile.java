package speiger.src.spmodapi.common.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.StepSound;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.Explosion;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.client.render.BlockRenderHelper;
import speiger.src.api.common.utils.RedstoneUtils;
import speiger.src.api.common.world.blocks.BlockPosition;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.enums.EnumColor.SpmodColor;
import speiger.src.spmodapi.common.enums.EnumGuiIDs;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.TileIconMaker;
import speiger.src.spmodapi.common.util.proxy.CodeProxy;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import buildcraft.api.core.SafeTimeTracker;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class AdvTile extends TileEntity
{
	public ArrayList<String> users = new ArrayList<String>();
	public boolean init = false;
	String owner = "";
	SafeTimeTracker tracker = new SafeTimeTracker();
	private int clock = CodeProxy.getRandom().nextInt();
	private int renderPass = 0;
	
	
	
	//Custom Functions TODO
	
	public boolean clockCanTick()
	{
		if(getWorldObj() == null)
		{
			setWorldObj(DimensionManager.getWorld(0));
		}
		return tracker.markTimeIfDelay(getWorldObj(), 1);
	}
	
	public int getClockTime()
	{
		return clock;
	}
	
	protected void onClockTick()
	{
		clock++;
	}
	
	public void loadInformation(List par1)
	{
		
	}
	
	public boolean hasContainer()
	{
		return false;
	}
	
	public AdvContainer getInventory(InventoryPlayer par1)
	{
		return new AdvContainer(par1, this);
	}
	
	@SideOnly(Side.CLIENT)
	public GuiInventoryCore getGui(InventoryPlayer par1)
	{
		return new GuiInventoryCore(par1, this);
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
	
	public boolean isPowered()
	{
		return RedstoneUtils.isBlockGettingPowered(this);
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
		{
			value += 65536;
		}
		return value;
	}
	
	public void setupUser(EntityPlayer player)
	{
		owner = player.username;
	}
	
	//TileEntity functions TODO
	
	public void init()
	{
		
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
	
	public void onTick()
	{
		
	}
	
	public void onIconMakerLoading()
	{
		
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
	
	@Override
	public void readFromNBT(NBTTagCompound par1)
	{
		users.clear();
		super.readFromNBT(par1);
		owner = par1.getString("Owner");
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
		par1.setString("Owner", owner);
		NBTTagList list = new NBTTagList();
		for(String user : users)
		{
			list.appendTag(new NBTTagString("Key", user));
		}
		par1.setTag("Users", list);
	}
	
	//Gui Functions TODO
	
	@SideOnly(Side.CLIENT)
	public void onGuiLoad(GuiInventoryCore par1, int guiX, int guiY)
	{
		
	}
	
	@SideOnly(Side.CLIENT)
	public void drawExtras(GuiInventoryCore par1, int guiX, int guiY, int mouseX, int mouseY)
	{
		
	}
	
	@SideOnly(Side.CLIENT)
	public void onButtonClick(GuiInventoryCore par1, GuiButton par2)
	{
		
	}
	
	@SideOnly(Side.CLIENT)
	public void drawFrontExtras(GuiInventoryCore par1, int guiX, int guiY, int mouseX, int mouseY)
	{
		
	}
	
	public String getInvName()
	{
		return "";
	}
	
	@SideOnly(Side.CLIENT)
	public ResourceLocation getTexture()
	{
		return null;
	}
	
	//Container Functions TODO
	
	public void onPlayerOpenContainer(EntityPlayer par1)
	{
		if(!users.contains(par1.username))
		{
			users.add(par1.username);
		}
	}
	
	public void onPlayerCloseContainer(EntityPlayer par1)
	{
		if(users.contains(par1.username))
		{
			users.remove(par1.username);
		}
	}
	
	public boolean hasUsers()
	{
		return !users.isEmpty();
	}
	
	public int getUserSize()
	{
		return users.size();
	}
	
	public boolean isUseableByPlayer(EntityPlayer par1)
	{
		return true;
	}
	
	public void onReciveGuiInfo(int key, int val)
	{
		
	}
	
	public void onSendingGuiInfo(Container par1, ICrafting par2)
	{
		
	}
	
	public void addContainerSlots(AdvContainer par1)
	{
		
	}
	
	public boolean onSlotClicked(AdvContainer par1, int slotID, int mouseButton, int modifier, EntityPlayer player)
	{
		return false;
	}
	
	public boolean canMergeItem(ItemStack par1, int slotID)
	{
		return false;
	}
	
	public void onMatrixChanged(IInventory par1)
	{
		
	}
	
	public int getSizeInventory()
	{
		return 0;
	}
	
	//Block Functions TODO
	
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(int side)
	{
		return true;
	}
	
	public boolean dropNormalBlock()
	{
		return true;
	}
	
	public void onBlockChange(Block par1, int par2)
	{
		
	}
	
	protected void notifyBlocksOfNeighborChange(ForgeDirection side)
	{
		worldObj.notifyBlocksOfNeighborChange(xCoord + side.offsetX, yCoord + side.offsetY, zCoord + side.offsetZ, getBlockType().blockID);
	}
	
	public boolean SolidOnSide(ForgeDirection side)
	{
		return true;
	}
	
	public boolean canPlacedOnSide(ForgeDirection side)
	{
		return side == side.UNKNOWN ? false : canPlaced();
	}
	
	public boolean canPlaced()
	{
		return true;
	}
	
	public StepSound getStepSound()
	{
		return getBlockType().stepSound;
	}
	
	public void onPlaced(int facing)
	{
		
	}
	
	public void onAdvPlacing(int rotation, int facing)
	{
		this.onPlaced(facing);
	}
	
	public boolean canBeReplaced()
	{
		return false;
	}
	
	public boolean isAir()
	{
		return false;
	}
	
	public float getBlockHardness()
	{
		return getBlockType().blockHardness;
	}
	
	public float getHardnessForPlayer(EntityPlayer par1)
	{
		return getBlockHardness();
	}
	
	public float getExplosionResistance(Entity par1)
	{
		return getBlockType().getExplosionResistance(par1);
	}
	
	public boolean canMonsterSpawn(EnumCreatureType type)
	{
		return true;
	}
	
	public void onEntityWalk(Entity par1)
	{
		
	}
	
	public void onColide(Entity par1)
	{
		
	}
	
	public void onFallen(Entity par5Entity)
	{
		
	}
	
	public boolean onActivated(EntityPlayer par1)
	{
		if(hasContainer())
		{
			if(!worldObj.isRemote)
			{
				par1.openGui(SpmodAPI.instance, EnumGuiIDs.Tiles.getID(), worldObj, xCoord, yCoord, zCoord);
			}
			return true;
		}
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
	
	public boolean onClick(boolean sneak, EntityPlayer par1, Block par2, int side)
	{
		return false;
	}
	
	public boolean canPlaceTorchOnTop()
	{
		return true;
	}
	
	public int getBlockLightLevel()
	{
		return 0;
	}
	
	public int getLightOpactiy()
	{
		return 0;
	}
	
	public boolean isNormalCube()
	{
		Block block = this.getBlockType();
		return block.blockMaterial.isOpaque() && block.renderAsNormalBlock() && !block.canProvidePower();
	}
	
	@SideOnly(Side.CLIENT)
	public SpmodColor getColor()
	{
		return new SpmodColor(16777215);
	}
	
	public ArrayList<AxisAlignedBB> getColidingBoxes(Entity par2)
	{
		return null;
	}
	
	public AxisAlignedBB getSelectedBoxes()
	{
		return AxisAlignedBB.getAABBPool().getAABB((double)xCoord, (double)yCoord, (double)zCoord, (double)xCoord+1, (double)yCoord+1, (double)zCoord+1);
	}
	
	public AxisAlignedBB getColidingBox()
	{
		return AxisAlignedBB.getAABBPool().getAABB((double)xCoord, (double)yCoord, (double)zCoord, (double)xCoord+1, (double)yCoord+1, (double)zCoord+1);
	}
	
	public void setBoundsOnState(Block par1)
	{
		par1.setBlockBounds(0, 0, 0, 1, 1, 1);
	}
	
	public boolean canConnectToWire(int side)
	{
		return false;
	}
	
	public int isPowering(int side)
	{
		return 0;
	}
	
	public int isIndirectlyPowering(int side)
	{
		return isPowering(side);
	}
	
	public boolean shouldCheckWeakPower(int side)
	{
		return false;
	}
	
	public int getFireBurnLenght(ForgeDirection side)
	{
		return 0;
	}
	
	public int getFireSpreadSpeed(ForgeDirection side)
	{
		return 0;
	}
	
	public boolean isFireSource(ForgeDirection side)
	{
		return false;
	}
	
	public boolean isSilkHarvestable(EntityPlayer player)
	{
		return false;
	}
	
	public void onBreaking()
	{
	}
	
	public void onDistroyedByExplosion(Explosion par0)
	{
	}
	
	public boolean canEntityDistroyMe(Entity par1)
	{
		return true;
	}
	
	public boolean removeAbleByPlayer(EntityPlayer player)
	{
		return true;
	}
	
	public int getDroppedExp()
	{
		return -1;
	}
	
	public ItemStack pickBlock(MovingObjectPosition target)
	{
		return new ItemStack(worldObj.getBlockId(xCoord, yCoord, zCoord), 1, worldObj.getBlockMetadata(xCoord, yCoord, zCoord));
	}
	
	public ArrayList<ItemStack> onDrop(int fortune)
	{
		ArrayList<ItemStack> drop = new ArrayList<ItemStack>();
		if(dropNormalBlock())
		{
			try
			{
				ItemStack result = TileIconMaker.getIconMaker().getStackFromTile(this);
				if(result == null)
				{
					result = new ItemStack(this.getBlockType(), 1, worldObj.getBlockMetadata(xCoord, yCoord, zCoord));
				}
				drop.add(result);
			}
			catch(Exception e)
			{
			}
		}
		return drop;
	}
	
	public void onClientTick()
	{
	}
	
	//Render Functions TODO
	
	@SideOnly(Side.CLIENT)
	public void onRenderInv(BlockStack stack, RenderBlocks render)
	{
		BlockRenderHelper.renderInInv(stack, render);
	}
	@SideOnly(Side.CLIENT)
	public void onRenderWorld(Block block, RenderBlocks renderer)
	{
		renderer.renderStandardBlock(block, xCoord, yCoord, zCoord);
	}
	
	public int getRenderPass()
	{
		return renderPass;
	}
	
	public void setRenderPass(int pass)
	{
		renderPass = pass;
	}
	
	public boolean dissableRenderer()
	{
		return false;
	}
	
	//Item Functions TODO
	
	@SideOnly(Side.CLIENT)
	public void onItemInformation(EntityPlayer par1, List par2, ItemStack par3)
	{
		
	}
	
	//Texture Functions TODO
	
	public void registerIcon(TextureEngine par1, Block par2)
	{
		
	}
	
	public abstract Icon getIconFromSideAndMetadata(int side, int renderPass);

	
	

	

	
}
