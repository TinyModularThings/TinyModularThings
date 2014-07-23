package speiger.src.spmodapi.common.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.StepSound;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.util.BlockPosition;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class AdvTile extends TileEntity
{
	 
	public void loadInformation(List par1)
	{
		
	}
	
	public StepSound getStepSound()
	{
		return getBlockType().stepSound;
	}
	
	public float getBlockHardness()
	{
		return getBlockType().blockHardness;
	}
	
	public int getBlockLightLevel()
	{
		return 0;
	}
	
	public AxisAlignedBB getColidingBoxes()
	{
		return null;
	}
	
	public AxisAlignedBB getSelectedBoxes()
	{
		return null;
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		
		if (!SpmodConfig.loadTiles)
		{
			return;
		}
		
		onTick();
	}
	
	public void updateBlock()
	{
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		if (worldObj.blockExists(xCoord, yCoord, zCoord))
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
	
	public boolean onSpecialClick(boolean sneak, EntityPlayer par1, Block par2, int side, float xSideHit, float ySideHit, float zSideHit)
	{
		return false;
	}
	
	public void onPlayerDistroyed()
	{
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
		return canPlaced();
	}
	
	public boolean canPlaced()
	{
		return true;
	}
	
	public boolean onActivated(EntityPlayer par1)
	{
		return false;
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
	
	public boolean isBurning()
	{
		return false;
	}
	
	public boolean isAir()
	{
		return false;
	}
	
	public boolean removeAbleByPlayer(EntityPlayer player)
	{
		return true;
	}
	
	public int getBurnability()
	{
		return 0;
	}
	
	public boolean isBurnable()
	{
		return getBurnability() > 0;
	}
	
	public boolean isFireSource(ForgeDirection side)
	{
		return false;
	}
	
	public ArrayList<ItemStack> onDrop(int fortune)
	{
		return new ArrayList<ItemStack>();
	}
	
	public boolean isSilkHarvestable()
	{
		return false;
	}
	
	public boolean canMonsterSpawn(EnumCreatureType type)
	{
		return true;
	}
	
	public boolean canConnectToWire()
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
	
	public boolean shouldCheckWeakPower()
	{
		return false;
	}
	
	public void registerIcon(IconRegister par1)
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
	
	public void setBoundingBoxes()
	{
		
	}
	
	public ItemStack pickBlock(MovingObjectPosition target)
	{
		return new ItemStack(worldObj.getBlockId(xCoord, yCoord, zCoord), 1, worldObj.getBlockMetadata(xCoord, yCoord, zCoord));
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
		if(value < 0) value += 65536;
		return value;
	}
	
}
