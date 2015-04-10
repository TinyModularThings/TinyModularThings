package speiger.src.spmodapi.common.interfaces;

import java.util.ArrayList;
import java.util.List;

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
import net.minecraft.network.packet.Packet;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.Explosion;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.common.data.packets.SpmodPacketHelper.ModularPacket;
import speiger.src.api.common.registry.helpers.SpmodMod;
import speiger.src.api.common.world.blocks.BlockPosition;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.common.enums.EnumColor.SpmodColor;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 
 * @author Speiger
 * Reference is at the AdvTile.class look there what every function does.
 */
public interface IAdvTile
{
	/**
	 * if this class contains the AdvTileClass then it returns true.
	 * Else you HAVE TO return FALSE. Else it can Crash!
	 */
	public boolean isAdvTile();
	
	//TODO Item TileEntity Function
	
	public void loadInformation(List par1);
	
	public TextureEngine getEngine();
	
	public boolean hasContainer();
	
	public AdvContainer getInventory(InventoryPlayer par1);
	
	@SideOnly(Side.CLIENT)
	public GuiInventoryCore getGui(InventoryPlayer par1);
	
	public boolean isPowered();
	
	public boolean isPowered(int side);
	
	public int getPower(int side);
	
	public int getGeneralPower();
	
	public int getHighestPower();

	public int getLowestPower();

	public BlockPosition getPosition();
	
	public boolean requireForgeRegistration();
	
	public void setupUser(EntityPlayer player);
	
	public void sendPacketToServer(Packet par1);
	
	public void sendPacketToClient(Packet par1, int range);
	
	public void sendPacketToPlayer(Packet par1, EntityPlayer par2);
	
	public ModularPacket createBasicPacket(SpmodMod mod);
	
	public void onIconMakerLoading();
		
	public ItemStack getItemDrop();
		
	// Gui Functions
	
	@SideOnly(Side.CLIENT)
	public void onGuiConstructed(GuiInventoryCore par1);
	
	@SideOnly(Side.CLIENT)
	public void onGuiLoad(GuiInventoryCore par1, int guiX, int guiY);
	
	@SideOnly(Side.CLIENT)
	public void drawExtras(GuiInventoryCore par1, int guiX, int guiY, int mouseX, int mouseY);
	
	@SideOnly(Side.CLIENT)
	public void onButtonClick(GuiInventoryCore par1, GuiButton par2);
	
	@SideOnly(Side.CLIENT)
	public void onButtonUpdate(GuiInventoryCore par1, GuiButton par2);
	
	@SideOnly(Side.CLIENT)
	public void onButtonReleased(GuiInventoryCore par1, GuiButton par2);
	
	@SideOnly(Side.CLIENT)
	public void drawFrontExtras(GuiInventoryCore par1, int guiX, int guiY, int mouseX, int mouseY);
	
	public String getInvName();
	
	@SideOnly(Side.CLIENT)
	public int getNameXOffset();
	
	@SideOnly(Side.CLIENT)
	public int getNameYOffset();
	
	@SideOnly(Side.CLIENT)
	public int getInvNameXOffset();
	
	@SideOnly(Side.CLIENT)
	public int getInvNameYOffset();
	
	@SideOnly(Side.CLIENT)
	public int getNameColor();
	
	@SideOnly(Side.CLIENT)
	public boolean onKeyTyped(GuiInventoryCore par1, char par2, int par3);
	
	@SideOnly(Side.CLIENT)
	public ResourceLocation getTexture();
	
	//Container Function
	
	public boolean isUseableByPlayer(EntityPlayer entityplayer);
	
	public void onPlayerOpenContainer(EntityPlayer par1);
	
	public void onPlayerCloseContainer(EntityPlayer par1);
	
	public boolean hasUsers();
	
	public int getUserSize();
	
	public void onReciveGuiInfo(int key, int val);
	
	public void onSendingGuiInfo(Container par1, ICrafting par2);
	
	public void addContainerSlots(AdvContainer par1);
	
	public boolean onSlotClicked(AdvContainer par1, int slotID, int mouseButton, int modifier, EntityPlayer player);
	
	public boolean canMergeItem(ItemStack par1, int slotID);
	
	public void onMatrixChanged(AdvContainer par1, IInventory par2);
	
	public int getSizeInventory();
	
	public boolean renderInnerInv();
	
	public boolean renderOuterInv();
	
	public boolean tilehandlesItemMoving();
	
	public ItemStack transferStackInSlot(AdvContainer par1, EntityPlayer par2, int par3);
	
	//Block Functions
	
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(int side);
	
	public void onBlockChange(Block par1, int par2);
	
	public boolean SolidOnSide(ForgeDirection side);
	
	public boolean canPlacedOnSide(ForgeDirection side);
	
	public boolean canPlaced();
	
	public StepSound getStepSound();
	
	public void onPlaced(int facing);
	
	public void onAdvPlacing(int rotation, int facing);
	
	public boolean canBeReplaced();
	
	public boolean isAir();
	
	public float getBlockHardness();
	
	public float getHardnessForPlayer(EntityPlayer par1);
	
	public float getExplosionResistance(Entity par1);
	
	public boolean canMonsterSpawn(EnumCreatureType type);
	
	public void onEntityWalk(Entity par1);
	
	public void onColide(Entity par1);
	
	public void onFallen(Entity par1);
	
	public boolean onActivated(EntityPlayer par1);
	
	public boolean onActivated(EntityPlayer par1, int side);
	
	public boolean onOpened(EntityPlayer par1, int side);
	
	public boolean onClick(boolean sneak, EntityPlayer par1, Block par2, int side);
	
	public void onLeftClick(EntityPlayer par1);
	
	public boolean canPlaceTorchOnTop();
	
	public int getBlockLightLevel();
	
	public int getLightOpactiy();
	
	public boolean isNormalCube();
	
	@SideOnly(Side.CLIENT)
	public SpmodColor getColor();
	
	public ArrayList<AxisAlignedBB> getColidingBoxes(Entity par2);
	
	public AxisAlignedBB getSelectedBoxes();

	public AxisAlignedBB getColidingBox();
	
	public void setBoundsOnState(Block par1);
	
	public boolean canConnectToWire(int side);
	
	public int isPowering(int side);
	
	public int isIndirectlyPowering(int side);
	
	public boolean shouldCheckWeakPower(int side);
	
	public int getFireBurnLenght(ForgeDirection side);
	
	public int getFireSpreadSpeed(ForgeDirection side);
	
	public boolean isFireSource(ForgeDirection side);
	
	public boolean isSilkHarvestable(EntityPlayer player);
	
	public void onBreaking();
	
	public void onDistroyedByExplosion(Explosion par0);
	
	public boolean canEntityDistroyMe(Entity par1);
	
	public boolean removeAbleByPlayer(EntityPlayer player);
	
	public int getDroppedExp();
	
	public ItemStack pickBlock(MovingObjectPosition target);
	
	public ArrayList<ItemStack> getItemDrops(int fortune);
	
	public void onClientTick();
	
	@SideOnly(Side.CLIENT)
	public void onRenderInv(BlockStack stack, RenderBlocks render);
	
	@SideOnly(Side.CLIENT)
	public void onRenderWorld(Block block, RenderBlocks renderer);
	
	public int getRenderPass();
	
	public void setRenderPass(int pass);
		
	//Item Functions
	
	@SideOnly(Side.CLIENT)
	public void onItemInformation(EntityPlayer par1, List par2, ItemStack par3);
	
	//Texture Functions
	
	public void registerIcon(TextureEngine par1, Block par2);
	
	public Icon getIconFromSideAndMetadata(int side, int renderPass);

}
