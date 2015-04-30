package speiger.src.tinymodularthings.common.blocks.transport;

import java.io.DataInput;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.common.data.packets.IPacketReciver;
import speiger.src.api.common.world.items.energy.IBCBattery;
import speiger.src.api.common.world.tiles.energy.EnergyProvider;
import speiger.src.api.common.world.tiles.energy.IEnergyProvider;
import speiger.src.api.common.world.tiles.energy.IEnergySubject;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.client.gui.buttons.GuiSliderButton;
import speiger.src.spmodapi.common.tile.AdvInventory;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ChargingBench extends AdvInventory implements IPacketReciver, IEnergyProvider,
	IPowerReceptor, ISidedInventory
{

	public EnergyProvider energy = new EnergyProvider(this, 30000);
	public float transferlimit = 0.01F;
	public boolean empty = true;
	public boolean charged = false;
	
	
	public ChargingBench()
	{
		super(19);
	}
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		if(side == 0 || side == 1)
		{
			return getEngine().getTexture(TinyBlocks.transportBlock, 5, side);
		}
		if(empty)
		{
			return getEngine().getTexture(TinyBlocks.transportBlock, 5, 2);
		}
		if(!charged)
		{
			return getEngine().getTexture(TinyBlocks.transportBlock, 5, 3);
		}
		return getEngine().getTexture(TinyBlocks.transportBlock, 5, 4);
	}
	
	@Override
	public boolean canMergeItem(ItemStack par1, int slotID)
	{
		return slotID < 10 && par1 != null && par1.getItem() instanceof IBCBattery && ((IBCBattery)par1.getItem()).requestEnergy(par1);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound par1)
	{
		super.readFromNBT(par1);
		NBTTagCompound energyData = par1.getCompoundTag("EnergyData");
		energy.readFromNBT(energyData);
		transferlimit = par1.getFloat("Transferlimit");
		empty = par1.getBoolean("Empty");
		charged = par1.getBoolean("Full");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound par1)
	{
		super.writeToNBT(par1);
		NBTTagCompound energyData = new NBTTagCompound();
		energy.writeToNBT(energyData);
		par1.setCompoundTag("EnergyData", energyData);
		par1.setFloat("Transferlimit", transferlimit);
		par1.setBoolean("Empty", empty);
		par1.setBoolean("Full", charged);
	}

	@Override
	public void recivePacket(DataInput par1)
	{
		try
		{
			int id = par1.readInt();
			if(id == 0)
			{
				transferlimit = par1.readFloat();
				energy.setTransferlimtit((int)(5000 * transferlimit));
			}
			else if(id == 1)
			{
				empty = par1.readBoolean();
				charged = par1.readBoolean();
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				worldObj.notifyBlockOfNeighborChange(xCoord, yCoord, zCoord, 0);
			}
		}
		catch(Exception e)
		{
		}
	}

	@Override
	public String identifier()
	{
		return null;
	}

	@Override
	public void onTick()
	{
		if(worldObj.isRemote)
		{
			return;
		}
		energy.update();
		if(worldObj.getWorldTime() % 20 == 0)
		{
			transferItems();
		}
		if(inv[9] != null && inv[9].getItem() instanceof IBCBattery)
		{
			IBCBattery bc = (IBCBattery)inv[9].getItem();
			if(bc.requestEnergy(inv[9]))
			{
				int needed = bc.getMaxMJStorage(inv[9]) - bc.getStoredMJ(inv[9]);
				int limit = Math.min(needed, bc.getTransferlimit(inv[9]));
				
				int provided = energy.useEnergy(limit, true);
				
				if(provided <= 0)
				{
					return;
				}
				int added = bc.charge(inv[9], provided, false);
				if(added <= 0)
				{
					return;
				}
				energy.useEnergy(added, false);
			}
		}
	}
	
	public boolean transferItems()
	{
		boolean flag = false;
		flag = transferItem(0, 1) || flag;
		flag = transferItem(1, 2) || flag;
		flag = transferItem(2, 3) || flag;
		flag = transferItem(3, 4) || flag;
		flag = transferItem(4, 5) || flag;
		flag = transferItem(5, 6) || flag;
		flag = transferItem(6, 7) || flag;
		flag = transferItem(7, 8) || flag;
		
		if(inv[9] == null && inv[8] != null && inv[8].getItem() instanceof IBCBattery)
		{
			IBCBattery bc = (IBCBattery)inv[8].getItem();
			if(bc.requestEnergy(inv[8]))
			{
				flag = transferItem(8, 9) || flag;
			}
			else
			{
				flag = transferItem(8, 10) || flag;
			}
		}
		if(inv[9] != null && inv[10] == null && inv[9].getItem() instanceof IBCBattery)
		{
			IBCBattery bc = (IBCBattery)inv[9].getItem();
			
			if(!bc.requestEnergy(inv[9]) || bc.isFull(inv[9]))
			{
				flag = transferItem(9, 10) || flag;
			}
		}
		flag = transferItem(10, 11) || flag;
		flag = transferItem(11, 12) || flag;
		flag = transferItem(12, 13) || flag;
		flag = transferItem(13, 14) || flag;
		flag = transferItem(14, 15) || flag;
		flag = transferItem(15, 16) || flag;
		flag = transferItem(16, 17) || flag;
		flag = transferItem(17, 18) || flag;
		if(flag && !transferItems())
		{
			this.onInventoryChanged();
		}
		return flag;
	}
	
	public boolean transferItem(int slotX, int slotY)
	{
		if(inv[slotX] != null && inv[slotY] == null)
		{
			inv[slotY] = inv[slotX].copy();
			inv[slotX] = null;
			return true;
		}
		return false;
	}

	@Override
	public boolean hasContainer()
	{
		return true;
	}
	
	

	@Override
	public void onInventoryChanged()
	{
		super.onInventoryChanged();
		this.empty = this.isEmpty();
		this.charged = this.allBatteriesCharged();
		this.sendPacketToClient(this.createBasicPacket(TinyModularThings.instance).InjectNumbers(1).InjectBooleans(empty, charged).finishPacket(), 20);
		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	@Override
	public void addContainerSlots(AdvContainer par1)
	{
		par1.setOffset(0, 38);
		for(int x = 0;x<3;x++)
		{
			for(int y = 0;y<3;y++)
			{
				par1.addSpmodSlot(this, x + (y * 3), 8 + (x * 18), 20 + (y * 18)).addUsage("Input Slot");
			}
		}
		
		par1.addSpmodSlot(this, 9, 80, 38).addUsage("Charging Slot");
		
		for(int x = 0;x<3;x++)
		{
			for(int y = 0;y<3;y++)
			{
				par1.addSpmodSlot(this, 10 + x + (y * 3), 115 + (x * 18), 20 + (y * 18)).addUsage("Output Slot");
			}
		}
	}

	@Override
	public String getInvName()
	{
		return "Charging Bench";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ResourceLocation getTexture()
	{
		return getEngine().getTexture("MediumFrame");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onGuiConstructed(GuiInventoryCore par1)
	{
		par1.setY(204);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onGuiLoad(GuiInventoryCore par1, int guiX, int guiY)
	{
		par1.getButtonsList().add(new GuiSliderButton(0, guiX + 12, guiY + 83, "Transferlimit: ", transferlimit, par1).setWeelEffect(0.0002F));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onButtonUpdate(GuiInventoryCore par1, GuiButton par2)
	{
		int id = par2.id;
		if(id == 0)
		{
			GuiSliderButton button = (GuiSliderButton)par2;
			int newAmount = (int)(5000 * button.sliderValue);
			button.displayString = "Transferlimit: "+newAmount+" MJ";
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onButtonReleased(GuiInventoryCore par1, GuiButton par2)
	{
		int id = par2.id;
		if(id == 0)
		{
			GuiSliderButton button = (GuiSliderButton)par2;
			transferlimit = button.sliderValue;
			this.sendPacketToServer(this.createBasicPacket(TinyModularThings.instance).InjectNumbers(0, transferlimit).finishPacket());
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void drawFrontExtras(GuiInventoryCore par1, int guiX, int guiY, int mouseX, int mouseY)
	{
		par1.getFontRenderer().drawString("Stored: "+energy.getStoredEnergy()+" MJ", 80, 110, 4210752);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void drawExtras(GuiInventoryCore par1, int guiX, int guiY, int mouseX, int mouseY)
	{
		par1.setTexture(getEngine().getTexture("Objects"));
		par1.defineSlot("ProgBarH");
		par1.drawSlotPros(65, 39, 12, 0, 10, 20);
		par1.drawSlotPros(101, 39, 12, 0, 10, 20);
	}

	@Override
	public IEnergySubject getEnergyProvider(ForgeDirection side)
	{
		return energy;
	}

	@Override
	public PowerReceiver getPowerReceiver(ForgeDirection side)
	{
		return energy.getSaveBCPowerProvider();
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

	@Override
	public void onReciveGuiInfo(int key, int val)
	{
		if(key == 0)
		{
			energy.setEnergy(val);
		}
	}

	@Override
	public void onSendingGuiInfo(Container par1, ICrafting par2)
	{
		par2.sendProgressBarUpdate(par1, 0, energy.getStoredEnergy());
	}

	@Override
	public void registerIcon(TextureEngine par1, Block par2)
	{
		par1.registerTexture(par2, 5, "ChargingBench_Bottom", "ChargingBench_Top", "ChargingBench_Side_Empty", "ChargingBench_Side_Charging", "ChargingBench_Side_Full");
	}
	
	public boolean allBatteriesCharged()
	{
		int stored = 0;
		int work = 0;
		for(int i = 0;i<this.getSizeInventory();i++)
		{
			ItemStack stack = this.getStackInSlot(i);
			if(stack != null)
			{
				stored++;
				if(stack.getItem() instanceof IBCBattery)
				{
					IBCBattery battery = (IBCBattery)stack.getItem();
					if(!battery.isFull(stack) || (!battery.isFull(stack) && battery.requestEnergy(stack)))
					{
						work++;
					}
				}
			}
		}
		boolean result = stored > 0 && work <= 0;
		charged = result;
		return result;
	}
	
	public boolean isEmpty()
	{
		int stored = 0;
		for(int i = 0;i<getSizeInventory();i++)
		{
			ItemStack stack = this.getStackInSlot(i);
			if(stack != null)
			{
				stored++;
			}
		}
		boolean result = stored <= 0;
		return result;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1)
	{
		return new int[]{0, 18};
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j)
	{
		return i == 0 && itemstack != null && itemstack.getItem() instanceof IBCBattery;
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j)
	{
		return i == 18;
	}

	@Override
	public ItemStack getItemDrop()
	{
		return new ItemStack(TinyBlocks.transportBlock, 1, 5);
	}
	
}
