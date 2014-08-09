package speiger.src.spmodapi.common.blocks.utils;

import java.util.List;
import java.util.Random;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import speiger.src.api.items.IExpBottle;
import speiger.src.api.tiles.IExpProvider;
import speiger.src.api.util.FluidUtils;
import speiger.src.api.util.RedstoneUtils;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.client.gui.utils.GuiExpBottle;
import speiger.src.spmodapi.common.enums.EnumGuiIDs;
import speiger.src.spmodapi.common.lib.SpmodAPILib;
import speiger.src.spmodapi.common.tile.AdvTile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ExpStorage extends AdvTile implements IInventory, IFluidHandler, IExpProvider
{
	public static IIcon[] textures = new IIcon[2];
	
	public ItemStack[] inv = new ItemStack[2];
	
	public int exp = 0;
	public boolean canAbsorbDeath = false;
	public Random rand = new Random();
	
	@Override
	public IIcon getIconFromSideAndMetadata(int side, int renderPass)
	{
		return side < 2 ? textures[0] : textures[1];
	}
	
	@Override
	public void registerIcon(IIconRegister par1)
	{
		textures[1] = par1.registerIcon(SpmodAPILib.ModID.toLowerCase()+":utils/expBench.side");
		textures[0] = par1.registerIcon(SpmodAPILib.ModID.toLowerCase()+":utils/expBench.top");
	}
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		if(FluidUtils.MFRExp())
		{
			if(resource.fluidID == FluidUtils.getMobEssens().getID())
			{
				if(resource.amount > 10)
				{
					int drain = resource.amount / 10;
					if(doFill)
					{
						this.addExp(drain);
					}
					return drain * 10;
				}
			}
		}
		if(FluidUtils.OpenBlocksExp())
		{
			if(resource.fluidID == FluidUtils.getLiquidExp().getID())
			{
				if(resource.amount > 20)
				{
					int drain = resource.amount / 20;
					if(doFill)
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
		if(this.exp < 0)
		{
			total += this.exp;
			exp = 0;
		}
		return total;
	}
	
	public int requestExp(int exp, boolean doUse)
	{
		int canGive = Math.min(this.exp, exp);
		if(doUse)
		{
			this.exp -= canGive;
		}
		if(this.exp < 0)
		{
			this.exp = 0;
		}
		return canGive;
	}
	
	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		boolean flag = false;
		if(FluidUtils.MFRExp())
		{
			if(FluidUtils.getMobEssens().getID() == resource.fluidID)
			{
				flag = true;
			}
		}
		if(FluidUtils.OpenBlocksExp())
		{
			if(FluidUtils.getLiquidExp().getID() == resource.fluidID)
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
		
		if(!mobEssens && !liquidExp)
		{
			return null;
		}
		
		FluidStack fluid = null;
		if(mobEssens && liquidExp)
		{
			if(RedstoneUtils.isBlockGettingPowered(this))
			{
				fluid = new FluidStack(FluidUtils.getMobEssens(), this.requestExp(maxDrain, doDrain));
			}
			else
			{
				fluid = new FluidStack(FluidUtils.getLiquidExp(), this.requestExp(maxDrain, doDrain)*20);
			}
		}
		else if(mobEssens || liquidExp)
		{
			if(mobEssens)
			{
				fluid = new FluidStack(FluidUtils.getMobEssens(), this.requestExp(maxDrain, doDrain));
			}
			else if(liquidExp)
			{
				fluid = new FluidStack(FluidUtils.getLiquidExp(), this.requestExp(maxDrain, doDrain)*20);
			}
		}
		
		return fluid;
	}
	
	
	
	
	
	@Override
	public void onTick()
	{
		super.onTick();
		if(!worldObj.isRemote)
		{
			if(inv[0] != null)
			{
				if(inv[0].getItem() instanceof IExpBottle && ((IExpBottle)inv[0].getItem()).hasExp(inv[0]))
				{
					IExpBottle bottle = (IExpBottle) inv[0].getItem();
					this.addExp(bottle.discharge(inv[0], bottle.getTransferlimit(inv[0])));
				}
				else if(inv[0].getItem() == Items.experience_bottle)
				{
					int i = 3 + this.worldObj.rand.nextInt(5) + this.worldObj.rand.nextInt(5);
					this.addExp(i);
					inv[0].stackSize--;
					if(inv[0].stackSize <= 0)
					{
						inv[0] = null;
					}
				}
			}
			if(inv[1] != null)
			{
				if(inv[1].getItem() instanceof IExpBottle && ((IExpBottle)inv[1].getItem()).needExp(inv[1]))
				{
					IExpBottle bottle = (IExpBottle) inv[1].getItem();
					bottle.charge(inv[1], this.requestExp(bottle.getTransferlimit(inv[1]), true));
				}
				else if(inv[1].getItem() == Items.glass_bottle && inv[1].stackSize == 1)
				{
					if(this.requestExp(10, false) == 10)
					{
						inv[1] = new ItemStack(Items.experience_bottle);
						this.removeExp(10);
					}
				}
			}
			
			if(worldObj.getWorldTime() % 20 == 0)
			{
				this.importExp();
			}
		}
	}
	
	public void importExp()
	{
		List<EntityXPOrb> hitList = worldObj.getEntitiesWithinAABB(EntityXPOrb.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1).expand(10.0D, 10.0D, 10.0D));

		if (hitList.size() > 0) {
			Loop:
			for (EntityXPOrb orb : hitList) {
				if (orb.isDead) {
					continue Loop;
					
				}

				int oldxp = exp;
				worldObj.playSoundAtEntity(orb, "random.orb", 0.1F, 0.5F * ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.8F));
				exp += orb.getXpValue();
				orb.setDead();
				if (oldxp == 0 && exp > 0) {
					notifyChange();
				} else {
					notifyResize();
				}
			}
		}
	}
	
	private void notifyChange() {
		worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType());
		worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord - 1, zCoord, getBlockType());
		notifyResize();
	}

	private void notifyResize() {
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
	public int getSizeInventory()
	{
		return 2;
	}
	
    public ItemStack getStackInSlot(int par1)
    {
        return this.inv[par1];
    }

    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.inv[par1] != null)
        {
            ItemStack itemstack;

            if (this.inv[par1].stackSize <= par2)
            {
                itemstack = this.inv[par1];
                this.inv[par1] = null;
                return itemstack;
            }
            else
            {
                itemstack = this.inv[par1].splitStack(par2);

                if (this.inv[par1].stackSize == 0)
                {
                    this.inv[par1] = null;
                }

                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.inv[par1] != null)
        {
            ItemStack itemstack = this.inv[par1];
            this.inv[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.inv[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }
    
	@Override
	public String getInventoryName()
	{
		return "Exp Storage";
	}
	
	@Override
	public boolean hasCustomInventoryName()
	{
		return true;
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer)
	{
		return false;
	}
	
	@Override
	public void openInventory()
	{
		
	}
	
	@Override
	public void closeInventory()
	{
		
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		if(itemstack != null)
		{
			if(itemstack != null && itemstack.getItem() instanceof IExpBottle)
			{
				return true;
			}
			else if(itemstack != null && itemstack.getItem() == Items.experience_bottle);
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
        NBTTagList nbttaglist = nbt.getTagList("Items", 10 /* COMPOUND */);
        this.inv = new ItemStack[this.getSizeInventory()];
        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.inv.length)
            {
                this.inv[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
        exp = nbt.getInteger("Exp");
        canAbsorbDeath = nbt.getBoolean("DEAD");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.inv.length; ++i)
        {
            if (this.inv[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.inv[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
        nbt.setTag("Items", nbttaglist);
        nbt.setInteger("Exp", exp);
        nbt.setBoolean("DEAD", canAbsorbDeath);
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.func_148857_g());
	}

	int helper = 0;
	@Override
	public void onReciveGuiInfo(int key, int val)
	{
		switch(key)
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
	public Container getInventory(InventoryPlayer par1)
	{
		return new InventoryExpStorage(par1, this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer getGui(InventoryPlayer par1)
	{
		return new GuiExpBottle(par1, this);
	}

	@Override
	public boolean onActivated(EntityPlayer par1)
	{
		if(!this.canAbsorbDeath && par1.getCurrentEquippedItem() != null && par1.getCurrentEquippedItem().getItem() == Items.diamond)
		{
			par1.addChatMessage(new ChatComponentText("Machine absorbs now from dieing entities exp"));
			par1.getCurrentEquippedItem().stackSize--;
			canAbsorbDeath = true;
		}
		if(hasContainer())
		{
			par1.openGui(SpmodAPI.instance, EnumGuiIDs.Tiles.getID(), worldObj, xCoord, yCoord, zCoord);
			return true;
		}
		return false;
	}

	@Override
	public void onBreaking()
	{
		if(!worldObj.isRemote)
		{
			while(exp > 0)
			{
				EntityXPOrb orb = new EntityXPOrb(worldObj, xCoord, yCoord, zCoord, this.requestExp(EntityXPOrb.getXPSplit(exp), true));
				worldObj.spawnEntityInWorld(orb);
			}
			
			if(this.canAbsorbDeath)
			{
				EntityItem item = new EntityItem(worldObj, xCoord, yCoord, zCoord, new ItemStack(Items.diamond));
				item.delayBeforeCanPickup = 10;
				worldObj.spawnEntityInWorld(item);
			}
		}
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



	
	
	
	
}
