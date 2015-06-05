package speiger.src.spmodapi.common.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
import net.minecraft.inventory.ISidedInventory;
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
import speiger.src.api.common.data.packets.SpmodPacketHelper.SpmodPacket;
import speiger.src.api.common.utils.RedstoneUtils;
import speiger.src.api.common.world.blocks.BlockPosition;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.enums.EnumColor.SpmodColor;
import speiger.src.spmodapi.common.enums.EnumGuiIDs;
import speiger.src.spmodapi.common.interfaces.IAdvTile;
import speiger.src.spmodapi.common.templates.core.ITemplate;
import speiger.src.spmodapi.common.templates.core.ITemplateProvider;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.proxy.CodeProxy;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import buildcraft.api.core.SafeTimeTracker;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class AdvTile extends TileEntity implements IAdvTile
{
	public ArrayList<String> users = new ArrayList<String>();
	public boolean init = false;
	public boolean tileLoaded = false;
	public Random rand = new Random();
	String owner = "";
	SafeTimeTracker tracker = new SafeTimeTracker();
	private int clock = CodeProxy.getRandom().nextInt();
	private int renderPass = 0;
	private byte[] redstoneStrenght = new byte[6];
	private byte[] sendingStrenght = new byte[6];
	private TileDataBuffer[] tiles = null;
	NBTTagCompound templateNBT = null;
	//TODO Custom Functions
	
	
	
	public boolean clockCanTick()
	{
		if(getWorldObj() == null)
		{
			setWorldObj(DimensionManager.getWorld(0));
		}
		return tracker.markTimeIfDelay(getWorldObj(), 1);
	}
	
	@Override
	public boolean isAdvTile()
	{
		return true;
	}

	public int getClockTime()
	{
		return clock;
	}
	
	protected void onClockTick()
	{
		clock++;
	}
	
	protected void setClockTime(int time)
	{
		clock = time;
	}
	
	public TileDataBuffer getBuffer(int side)
	{
		if(tiles == null)
		{
			tiles = TileDataBuffer.makeBuffer(worldObj, xCoord, yCoord, zCoord, false);
		}
		return tiles[side];
	}
	
	public void loadInformation(List par1)
	{
		
	}
	
	public TextureEngine getEngine()
	{
		return TextureEngine.getTextures();
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
		return new GuiInventoryCore(par1, this).setAutoDrawing();
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
		boolean flag = false;
		for(int i = 0;i<6;i++)
		{
			flag = redstoneStrenght[i] > 0;
			if(flag)
			{
				break;
			}
		}
		return flag;
	}
	
	public boolean isPowered(int side)
	{
		return redstoneStrenght[side] > 0;
	}
	
	public int getPower(int side)
	{
		return redstoneStrenght[side];
	}
	
	public int getTotalPower()
	{
		int totalPower = 0;
		for(int i = 0;i<6;i++)
		{
			totalPower += getPower(i);
		}
		return totalPower;
	}
	
	public int getGeneralPower()
	{
		int power = 0;
		for(int i = 0;i<6;i++)
		{
			power+=redstoneStrenght[i];
		}
		power /= redstoneStrenght.length;
		return power;
	}
	
	public int getHighestPower()
	{
		int power = 0;
		for(int i = 0;i<6;i++)
		{
			power = Math.max(power, redstoneStrenght[i]);
		}
		return power;
	}
	
	public int getLowestPower()
	{
		int power = 15;
		for(int i = 0;i<6;i++)
		{
			power = Math.min(power, redstoneStrenght[i]);
		}
		return power;
	}
	
	public void onRedstonePulseApplied()
	{
		
	}
	
	public void onRedstonePulseApplied(int side)
	{
		
	}
	
	
	public void setRedstoneSignal(int total)
	{
		boolean flag = false;
		for(int i = 0;i<sendingStrenght.length;i++)
		{
			if(sendingStrenght[i] != total)
			{
				flag = true;
				this.sendingStrenght[i] = (byte)total;
			}
		}
		if(flag)
		{
			updateNeighbors(true);
		}
	}

	public void setRedstoneSignal(int side, int total)
	{
		if(sendingStrenght[side] != total)
		{
			sendingStrenght[side] = (byte)total;
			updateNeighbors(true);
		}	
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
	
	public boolean requireForgeRegistration()
	{
		return false;
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
	
	public String getBlockOwner()
	{
		return owner;
	}
	
	//TODO Packet Functions
	
	public void sendPacketToServer(Packet par1)
	{
		PacketDispatcher.sendPacketToServer(par1);
	}
	
	public void sendPacketToClient(Packet par1, int range)
	{
		PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, range, worldObj.provider.dimensionId, par1);
	}
	
	public void sendPacketToPlayer(Packet par1, EntityPlayer par2)
	{
		PacketDispatcher.sendPacketToPlayer(par1, (Player)par2);
	}
	
	public void sendAllAround(int range)
	{
		PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, range, worldObj.provider.dimensionId, getDescriptionPacket());
	}
	
	@Override
	public void onSpmodPacket(SpmodPacket par1)
	{
		
	}
	
	//TODO TileEntity functions
	
	public void init()
	{
		if(this instanceof ITemplateProvider)
		{
			ITemplate template = ((ITemplateProvider)this).getTemplate();
			template.init();
		}
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
		redstoneStrenght = par1.getByteArray("Receiving");
		sendingStrenght = par1.getByteArray("Sending");
		if(redstoneStrenght.length != 6) redstoneStrenght = new byte[6];
		if(sendingStrenght.length != 6) sendingStrenght = new byte[6];
		
		NBTTagList list = par1.getTagList("Users");
		for(int i = 0;i<list.tagCount();i++)
		{
			NBTTagString text = (NBTTagString)list.tagAt(i);
			users.add(text.data);
		}
		if(this instanceof ITemplateProvider)
		{
			ITemplateProvider provider = (ITemplateProvider)this;
			templateNBT = par1.getCompoundTag("Template");
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound par1)
	{
		super.writeToNBT(par1);
		par1.setString("Owner", owner);
		par1.setByteArray("Receiving", redstoneStrenght);
		par1.setByteArray("Sending", sendingStrenght);
		NBTTagList list = new NBTTagList();
		for(String user : users)
		{
			list.appendTag(new NBTTagString("Key", user));
		}
		par1.setTag("Users", list);
		if(this instanceof ITemplateProvider)
		{
			ITemplateProvider provider = (ITemplateProvider)this;
			NBTTagCompound nbt = new NBTTagCompound();
			provider.getTemplate().writeToNBT(nbt);
			par1.setCompoundTag("Template", nbt);
		}
		
	}

	@Override
	public void invalidate()
	{
		super.invalidate();
		if(tileLoaded)
		{
			tileLoaded = false;
			onUnload(false);
		}
	}

	@Override
	public void validate()
	{
		super.validate();
		if(!tileLoaded)
		{
			tileLoaded = true;
			onLoad();
		}
	}

	@Override
	public void onChunkUnload()
	{
		super.onChunkUnload();
		if(tileLoaded)
		{
			tileLoaded = false;
			onUnload(true);
		}
	}
	
	public void onLoad()
	{
		if(this instanceof ITemplateProvider)
		{
			ITemplateProvider provider = (ITemplateProvider)this;
			provider.initTemplate();
			if(templateNBT != null)
			{
				provider.getTemplate().readFromNBT(templateNBT);
				templateNBT = null;
			}
		}
	}
	
	public void onUnload(boolean chunk)
	{
		if(this instanceof ITemplateProvider)
		{
			ITemplateProvider provider = (ITemplateProvider)this;
			provider.getTemplate().onUnload();
		}
	}
	
	//TODO Gui Functions

	@SideOnly(Side.CLIENT)
	public void onGuiConstructed(GuiInventoryCore par1)
	{
		
	}
	
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
	public void onButtonUpdate(GuiInventoryCore par1, GuiButton par2)
	{
		
	}
	
	@SideOnly(Side.CLIENT)
	public void onButtonReleased(GuiInventoryCore par1, GuiButton par2)
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
	public int getNameXOffset()
	{
		return 0;
	}
	
	@SideOnly(Side.CLIENT)
	public int getNameYOffset()
	{
		return 0;
	}
	
	@SideOnly(Side.CLIENT)
	public int getInvNameXOffset()
	{
		return 0;
	}
	
	@SideOnly(Side.CLIENT)
	public int getInvNameYOffset()
	{
		return 0;
	}
	
	@SideOnly(Side.CLIENT)
	public int getNameColor()
	{
		return 4210752;
	}
	
	@SideOnly(Side.CLIENT)
	public boolean onKeyTyped(GuiInventoryCore par1, char par2, int par3)
	{
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	public ResourceLocation getTexture()
	{
		return getEngine().getTexture("BasicFrame");
	}
	
	//TODO Container Functions
	
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
		if(this instanceof ISidedInventory)
		{
			return ((ISidedInventory)this).canInsertItem(slotID, par1, 0);
		}
		return false;
	}
	
	public void onMatrixChanged(AdvContainer par1, IInventory par2)
	{
		
	}
	
	public int getSizeInventory()
	{
		return 0;
	}
	
	public boolean renderInnerInv()
	{
		return true;
	}
	
	public boolean renderOuterInv()
	{
		return true;
	}
	
	public boolean tilehandlesItemMoving()
	{
		return false;
	}
	
	public ItemStack transferStackInSlot(AdvContainer par1, EntityPlayer par2, int par3)
	{
		return null;
	}
	
	//TODO Block Functions
	
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
		byte[] lastRedstoneStrenght = this.redstoneStrenght;
		int totalStrenght = getTotalPower();
		for(int i = 0;i<6;i++)
		{
			this.redstoneStrenght[i] = (byte)RedstoneUtils.getPowerInput(this, i);
		}
		if(totalStrenght > 0)
		{
			totalStrenght = this.getTotalPower();
			if(totalStrenght <= 0)
			{
				this.onRedstonePulseApplied();
			}
		}
		for(int i = 0;i<6;i++)
		{
			if(lastRedstoneStrenght[i] > 0 && redstoneStrenght[i] <= 0)
			{
				this.onRedstonePulseApplied(i);
			}
		}
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
	
	public void onLeftClick(EntityPlayer par1)
	{
		
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
		if(side >= 6)
		{
			return 0;
		}
		return this.sendingStrenght[side];
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
		if(this instanceof ITemplateProvider)
		{
			ITemplateProvider template = (ITemplateProvider)this;
			template.getTemplate().onBreaking();
		}
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
	
	public ArrayList<ItemStack> getItemDrops(int fortune)
	{
		ArrayList<ItemStack> drop = new ArrayList<ItemStack>();
		if(dropNormalBlock())
		{
			drop.add(getItemDrop());
		}
		return drop;
	}
	
	public void onClientTick()
	{
	}
	
	//TODO Render Functions
	
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
	
	//TODO Item Functions
	
	@SideOnly(Side.CLIENT)
	public void onItemInformation(EntityPlayer par1, List par2, ItemStack par3)
	{
		
	}
	
	//TODO Texture Functions
	
	public void registerIcon(TextureEngine par1, Block par2)
	{
		
	}
	
	public abstract Icon getIconFromSideAndMetadata(int side, int renderPass);

	
	

	

	
}
