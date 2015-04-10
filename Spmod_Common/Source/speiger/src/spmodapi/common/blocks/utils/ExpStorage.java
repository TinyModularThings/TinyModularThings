package speiger.src.spmodapi.common.blocks.utils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import speiger.src.api.common.utils.FluidUtils;
import speiger.src.api.common.utils.RedstoneUtils;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.api.common.world.items.IExpBottle;
import speiger.src.api.common.world.tiles.interfaces.IExpProvider;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.tile.AdvInventory;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ExpStorage extends AdvInventory implements IFluidHandler, IExpProvider
{
	public int exp = 0;
	public boolean canAbsorbDeath = false;
	
	public ExpStorage()
	{
		super(2);
	}
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		return TextureEngine.getTextures().getTexture(APIBlocks.blockUtils, 1, side < 2 ? 1 : 0);
	}
	
	@Override
	public void registerIcon(TextureEngine par1, Block par2)
	{
		par1.registerTexture(new BlockStack(par2, 1), "expBench.side","expBench.top");
	}
	
	
	
	@Override
	public ItemStack getItemDrop()
	{
		return new ItemStack(APIBlocks.blockUtils, 1, 1);
	}

	@Override
	public void addContainerSlots(AdvContainer par1)
	{
		par1.addSpmodSlot(this, 0, 43, 45).addUsage("Import Exp");
		par1.addSpmodSlot(this, 1, 116, 45).addUsage("Export Exp");
	}

	@Override
	public boolean canMergeItem(ItemStack par1, int slotID)
	{
		if(par1 != null)
		{
			if(slotID == 0)
			{
				if(par1.itemID == Item.expBottle.itemID)
				{
					return true;
				}
				return par1.getItem() instanceof IExpBottle && ((IExpBottle)par1.getItem()).hasExp(par1);
			}
			else if(slotID == 1)
			{
				return par1.getItem() instanceof IExpBottle && ((IExpBottle)par1.getItem()).needExp(par1);
			}
		}
		return super.canMergeItem(par1, slotID);
	}
	
	@SideOnly(Side.CLIENT)
	public void drawFrontExtras(GuiInventoryCore par1, int guiX, int guiY, int mouseX, int mouseY)
	{
		par1.getFontRenderer().drawString("Stored Exp: " + exp, 55, 25, 4210752);
	}

	@Override
	public float getBlockHardness()
	{
		return 5F;
	}
	
	@Override
	public float getExplosionResistance(Entity par1)
	{
		return 3F;
	}
	

	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		if (FluidUtils.MFRExp())
		{
			if (resource.fluidID == FluidUtils.getMobEssens().getID())
			{
				if (resource.amount > 10)
				{
					int drain = resource.amount / 10;
					if (doFill)
					{
						this.addExp(drain);
					}
					return drain * 10;
				}
			}
		}
		if (FluidUtils.OpenBlocksExp())
		{
			if (resource.fluidID == FluidUtils.getLiquidExp().getID())
			{
				if (resource.amount > 20)
				{
					int drain = resource.amount / 20;
					if (doFill)
					{
						this.addExp(drain);
					}
					return drain * 20;
				}
			}
		}
		return 0;
	}
	
	public int addExp(int exp)
	{
		this.exp += exp;
		return exp;
	}
	
	public int getExp()
	{
		return exp;
	}
	
	public int removeExp(int exp)
	{
		int total = exp;
		this.exp -= exp;
		if (this.exp < 0)
		{
			total += this.exp;
			exp = 0;
		}
		return total;
	}
	
	public int requestExp(int exp, boolean doUse)
	{
		int canGive = Math.min(this.exp, exp);
		if (doUse)
		{
			this.exp -= canGive;
		}
		if (this.exp < 0)
		{
			this.exp = 0;
		}
		return canGive;
	}
	
	
	
	@Override
	public ArrayList<ItemStack> getItemDrops(int fortune)
	{
		ArrayList<ItemStack> stack = super.getItemDrops(fortune);
		if(this.canAbsorbDeath)
		{
			stack.add(new ItemStack(Item.diamond));
		}
		return stack;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		boolean flag = false;
		if (FluidUtils.MFRExp())
		{
			if (FluidUtils.getMobEssens().getID() == resource.fluidID)
			{
				flag = true;
			}
		}
		if (FluidUtils.OpenBlocksExp())
		{
			if (FluidUtils.getLiquidExp().getID() == resource.fluidID)
			{
				flag = true;
			}
		}
		return flag ? this.drain(from, resource.amount, doDrain) : null;
	}
	
	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		boolean mobEssens = FluidUtils.MFRExp();
		boolean liquidExp = FluidUtils.OpenBlocksExp();
		
		if (!mobEssens && !liquidExp)
		{
			return null;
		}
		
		FluidStack fluid = null;
		if (mobEssens && liquidExp)
		{
			if (RedstoneUtils.isBlockGettingPowered(this))
			{
				fluid = new FluidStack(FluidUtils.getMobEssens(), this.requestExp(maxDrain, doDrain));
			}
			else
			{
				if(maxDrain < 20)
				{
					return null;
				}
				fluid = new FluidStack(FluidUtils.getLiquidExp(), this.requestExp((maxDrain / 20), doDrain) * 20);
			}
		}
		else if (mobEssens || liquidExp)
		{
			if (mobEssens)
			{
				fluid = new FluidStack(FluidUtils.getMobEssens(), this.requestExp(maxDrain, doDrain));
			}
			else if (liquidExp)
			{
				if(maxDrain < 20)
				{
					return null;
				}
				fluid = new FluidStack(FluidUtils.getLiquidExp(), this.requestExp((maxDrain / 20), doDrain) * 20);
			}
		}
		return fluid;
	}
	
	@Override
	public void onTick()
	{
		super.onTick();
		if (worldObj.isRemote || worldObj.getWorldTime() % 1 != 0)
		{
			return;
		}

		if (inv[0] != null)
		{
			if (inv[0].getItem() instanceof IExpBottle && ((IExpBottle) inv[0].getItem()).hasExp(inv[0]))
			{
				IExpBottle bottle = (IExpBottle) inv[0].getItem();
				this.addExp(bottle.discharge(inv[0], bottle.getTransferlimit(inv[0])));
			}
			else if (inv[0].itemID == Item.expBottle.itemID)
			{
				int i = 3 + this.worldObj.rand.nextInt(5) + this.worldObj.rand.nextInt(5);
				this.addExp(i);
				inv[0].stackSize--;
				if (inv[0].stackSize <= 0)
				{
					inv[0] = null;
				}
			}
		}
		if (inv[1] != null)
		{
			if (inv[1].getItem() instanceof IExpBottle && ((IExpBottle) inv[1].getItem()).needExp(inv[1]))
			{
				IExpBottle bottle = (IExpBottle) inv[1].getItem();
				bottle.charge(inv[1], this.requestExp(bottle.getTransferlimit(inv[1]), true));
			}
			else if (inv[1].itemID == Item.glassBottle.itemID && inv[1].stackSize == 1)
			{
				if (this.requestExp(10, false) == 10)
				{
					inv[1] = new ItemStack(Item.expBottle);
					this.removeExp(10);
				}
			}
		}
		if (worldObj.getWorldTime() % 20 == 0)
		{
			this.importExp();
		}
	
		
	}
	
	public void importExp()
	{
		List<EntityXPOrb> hitList = worldObj.getEntitiesWithinAABB(EntityXPOrb.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1).expand(10.0D, 10.0D, 10.0D));
		
		if (hitList.size() > 0)
		{
			Loop: for (EntityXPOrb orb : hitList)
			{
				if (orb.isDead)
				{
					continue Loop;
					
				}
				
				int oldxp = exp;
				worldObj.playSoundAtEntity(orb, "random.orb", 0.1F, 0.5F * ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.8F));
				exp += orb.getXpValue();
				orb.setDead();
				if (oldxp == 0 && exp > 0)
				{
					notifyChange();
				}
				else
				{
					notifyResize();
				}
			}
		}
	}
	
	private void notifyChange()
	{
		worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType().blockID);
		worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord - 1, zCoord, getBlockType().blockID);
		notifyResize();
	}
	
	private void notifyResize()
	{
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return true;
	}
	
	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return true;
	}
	
	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		boolean mobEssens = FluidUtils.MFRExp();
		boolean liquidExp = FluidUtils.OpenBlocksExp();
		return new FluidTankInfo[mobEssens && liquidExp ? 2 : liquidExp || mobEssens ? 1 : 0];
	}
	
	@Override
	public String getInvName()
	{
		return "Exp Storage";
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		if (itemstack != null)
		{
			if (itemstack != null && itemstack.getItem() instanceof IExpBottle)
			{
				return true;
			}
			else if (itemstack != null && itemstack.itemID == Item.expBottle.itemID)
			{
				return true;
			}
		}
		return false;
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		exp = nbt.getInteger("Exp");
		canAbsorbDeath = nbt.getBoolean("DEAD");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("Exp", exp);
		nbt.setBoolean("DEAD", canAbsorbDeath);
	}
	
	int helper = 0;
	
	@Override
	public void onReciveGuiInfo(int key, int val)
	{
		switch (key)
		{
			case 0:
				helper = this.upcastShort(val);
				break;
			case 1:
				exp = (helper | (val << 16));
				break;
		}
	}
	
	@Override
	public void onSendingGuiInfo(Container par1, ICrafting par2)
	{
		par2.sendProgressBarUpdate(par1, 0, exp);
		par2.sendProgressBarUpdate(par1, 1, exp >> 16);
	}
	
	@Override
	public boolean hasContainer()
	{
		return true;
	}
	
	@Override
	public boolean onClick(boolean sneak, EntityPlayer par1, Block par2, int side)
	{
		if (!this.canAbsorbDeath && par1.getCurrentEquippedItem() != null && par1.getCurrentEquippedItem().itemID == Item.diamond.itemID)
		{
			par1.addChatMessage("Machine absorbs now from dieing entities exp");
			par1.getCurrentEquippedItem().stackSize--;
			canAbsorbDeath = true;
			return true;
		}
		return super.onClick(sneak, par1, par2, side);
	}

	@Override
	public void onBreaking()
	{
		if (!worldObj.isRemote)
		{
			while (exp > 0)
			{
				EntityXPOrb orb = new EntityXPOrb(worldObj, xCoord, yCoord, zCoord, this.requestExp(EntityXPOrb.getXPSplit(exp), true));
				worldObj.spawnEntityInWorld(orb);
			}
		}
	}
	@Override
	public boolean dropNormalBlock()
	{
		return false;
	}
	
	@Override
	public int getExpStored()
	{
		return exp;
	}
	
	@Override
	public int getMaxStoredExp()
	{
		return Integer.MAX_VALUE;
	}
	
	@Override
	public boolean absorbDeath()
	{
		return this.canAbsorbDeath;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onItemInformation(EntityPlayer par1, List par2, ItemStack par3)
	{
		super.onItemInformation(par1, par2, par3);
		par2.add("Stores Extreme much EXP");
		
		if(GuiScreen.isCtrlKeyDown())
		{
			par2.add("Supports Liquid form of XP");
			par2.add("Drop all XP on breaking");
			par2.add("Import every XP in a 10 block Radius");
			par2.add("Power it with a redstone signal to force MFR MobEssens");
			par2.add("Converting Rate Liquid XP: 20mB = 1XP and 1 XP = 20mB");
			par2.add("Converting Rate Liquid MobEssens: 10mB = 1XP and 1XP = 1mB");
			par2.add("Click with a Diamond on me and the i absorb Exp from dieing monsters");
		}
		else
		{
			par2.add("Press Ctrl to get Extra Info");
		}
	}
}
