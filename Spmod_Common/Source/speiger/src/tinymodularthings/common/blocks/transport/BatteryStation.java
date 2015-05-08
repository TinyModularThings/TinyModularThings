package speiger.src.tinymodularthings.common.blocks.transport;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.common.data.packets.SpmodPacketHelper.SpmodPacket;
import speiger.src.api.common.utils.WorldReading;
import speiger.src.api.common.world.items.energy.IBCBattery;
import speiger.src.api.common.world.tiles.energy.EnergyProvider;
import speiger.src.api.common.world.tiles.energy.IEnergyProvider;
import speiger.src.api.common.world.tiles.energy.IEnergySubject;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.client.gui.buttons.GuiSliderButton;
import speiger.src.spmodapi.common.network.packets.base.TileNBTPacket;
import speiger.src.spmodapi.common.tile.AdvInventory;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import buildcraft.api.power.IPowerEmitter;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BatteryStation extends AdvInventory implements IPowerEmitter, ISidedInventory
{
	
	public float transferlimit = 0.01F;
	public EnergyProvider energy = new EnergyProvider(this, 30000);
	public boolean noBatteries = true;
	public boolean noCharge;
	public boolean discharging;
	public boolean[] dischargingAmount = new boolean[20];
	public byte state = 0;
	
	
	public BatteryStation()
	{
		super(19);
	}

	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		if(side == 0 || side == 1)
		{
			return getEngine().getTexture(TinyBlocks.transportBlock, 6, side);
		}
		if(noBatteries)
		{
			return getEngine().getTexture(TinyBlocks.transportBlock, 6, 2);
		}
		if(noCharge)
		{
			return getEngine().getTexture(TinyBlocks.transportBlock, 6, 3);
		}
		if(discharging)
		{
			return getEngine().getTexture(TinyBlocks.transportBlock, 6, 4);
		}
		return getEngine().getTexture(TinyBlocks.transportBlock, 6, 5);
	}
	
	@Override
	public void registerIcon(TextureEngine par1, Block par2)
	{
		par1.registerTexture(par2, 6, "BatteryStation_Bottom", "BatteryStation_Top", "BatteryStation_Side_Empty", "BatteryStation_Side_NoCharge", "BatteryStation_Side_Discharging", "BatteryStation_Side_Full");
	}
	
	@Override
	public boolean canMergeItem(ItemStack par1, int slotID)
	{
		return slotID < 10 && par1 != null && par1.getItem() instanceof IBCBattery && ((IBCBattery)par1.getItem()).wantToSendEnergy(par1);
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
			if(bc.wantToSendEnergy(inv[8]))
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
			
			if(!bc.wantToSendEnergy(inv[9]) || bc.isEmpty(inv[9]))
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
	public void onTick()
	{
		if(worldObj.isRemote)
		{
			return;
		}
		if(worldObj.getWorldTime() % 20 == 0)
		{
			transferItems();
		}
		handleItems();
		handleEnergyOutput();
		state++;
		if(state >= 20)
		{
			state = 0;
			if(discharging != isDischarging())
			{
				onInventoryChanged();
			}
		}
	}
	
	public boolean isDischarging()
	{
		boolean flag = false;
		for(int i = 0;i<20;i++)
		{
			flag = flag || this.dischargingAmount[i];
		}
		return flag;
	}
	
	public void handleEnergyOutput()
	{
		for(int i = 0;i<6;i++)
		{
			if(energy.getStoredEnergy() <= 0)
			{
				break;
			}
			TileEntity tile = WorldReading.getTileEntity(worldObj, xCoord, yCoord, zCoord, i);
			if(tile != null)
			{
				if(tile instanceof IEnergyProvider)
				{
					IEnergySubject sub = ((IEnergyProvider)tile).getEnergyProvider(ForgeDirection.getOrientation(i).getOpposite());
					if(sub != null && sub.requestEnergy())
					{
						int added = sub.addEnergy(energy.getStoredEnergy(), false);
						if(added > 0)
						{
							energy.useEnergy(added, false);
						}
					}
				}
				else if(tile instanceof IPowerReceptor)
				{
					PowerReceiver receiver = ((IPowerReceptor)tile).getPowerReceiver(ForgeDirection.getOrientation(i).getOpposite());
					if(receiver != null)
					{
						int added = (int)receiver.receiveEnergy(Type.STORAGE, energy.getStoredEnergy(), ForgeDirection.getOrientation(i).getOpposite());
						if(added > 0)
						{
							energy.useEnergy(added, false);
						}
					}
				}
			}
		}
	}
	
	@Override
	public void onInventoryChanged()
	{
		super.onInventoryChanged();
		this.noBatteries = this.noBatteries();
		this.noCharge = this.noCharge();
		TileNBTPacket packet = new TileNBTPacket(this);
		NBTTagCompound nbt = packet.getData();
		nbt.setBoolean("NoBatteries", noBatteries);
		nbt.setBoolean("NoCharge", noCharge);
		nbt.setBoolean("Discharging", discharging);
		sendPacketToClient(SpmodAPI.handler.createFinishPacket(packet), 20);
		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	
	
	@Override
	public String getInvName()
	{
		return "Battery Station";
	}

	public void handleItems()
	{	
		if(inv[9] != null && inv[9].getItem() instanceof IBCBattery)
		{
			IBCBattery battery = (IBCBattery)inv[9].getItem();
			if(!battery.isEmpty(inv[9]) && battery.wantToSendEnergy(inv[9]))
			{
				int send = Math.min(battery.energyToSend(inv[9]), battery.getTransferlimit(inv[9]));
				if(send <= 0)
				{
					this.dischargingAmount[state] = false;
					return;
				}
				int provided = battery.discharge(inv[9], send, true);
				if(provided <= 0)
				{
					this.dischargingAmount[state] = false;
					return;
				}
				int added = energy.addEnergy(provided, false);
				if(added <= 0)
				{
					this.dischargingAmount[state] = false;
					return;
				}
				battery.discharge(inv[9], added, false);
				this.dischargingAmount[state] = true;
			}
		}
	}

	@Override
	public void addContainerSlots(AdvContainer par1)
	{
		par1.setOffset(0, 38);
		for(int x = 0;x<3;x++)
		{
			for(int y = 0;y<3;y++)
			{
				par1.addSpmodSlot(this, x + (y * 3), 8 + (x * 18), 20 + (y * 18));
			}
		}
		
		par1.addSpmodSlot(this, 9, 80, 38);
		
		for(int x = 0;x<3;x++)
		{
			for(int y = 0;y<3;y++)
			{
				par1.addSpmodSlot(this, 10 + x + (y * 3), 115 + (x * 18), 20 + (y * 18));
			}
		}
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
			TileNBTPacket packet = new TileNBTPacket(this);
			packet.getData().setFloat("Limit", transferlimit);
			this.sendPacketToServer(SpmodAPI.handler.createFinishPacket(packet));
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
	public boolean hasContainer()
	{
		return true;
	}
	
	@Override
	public void onSpmodPacket(SpmodPacket par1)
	{
		if(par1.getPacket() instanceof TileNBTPacket)
		{
			TileNBTPacket packet = (TileNBTPacket)par1.getPacket();
			NBTTagCompound nbt = packet.getData();
			if(nbt.hasKey("NoBatteries"))
			{
				noBatteries = nbt.getBoolean("NoBatteries");
			}
			if(nbt.hasKey("NoCharge"))
			{
				noCharge = nbt.getBoolean("NoCharge");
			}
			if(nbt.hasKey("Discharging"))
			{
				discharging = nbt.getBoolean("Discharging");
			}
			if(nbt.hasKey("Limit"))
			{
				this.transferlimit = nbt.getFloat("Limit");
				energy.setTransferlimtit((int)(5000 * transferlimit));
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound par1)
	{
		super.readFromNBT(par1);
		NBTTagCompound energyNet = par1.getCompoundTag("EnergyNet");
		energy.readFromNBT(energyNet);
		transferlimit = par1.getFloat("Limit");
		discharging = par1.getBoolean("discharging");
		noBatteries = par1.getBoolean("NoBatteries");
		noCharge = par1.getBoolean("NoCharge");
	}

	@Override
	public void writeToNBT(NBTTagCompound par1)
	{
		super.writeToNBT(par1);
		NBTTagCompound energyNet = new NBTTagCompound();
		energy.writeToNBT(energyNet);
		par1.setCompoundTag("EnergyNet", energyNet);
		par1.setFloat("Limit", transferlimit);
		par1.setBoolean("discharging", discharging);
		par1.setBoolean("NoBatteries", noBatteries);
		par1.setBoolean("NoCharge", noCharge);
	}
	
	public boolean noBatteries()
	{
		int count = 0;
		for(int i = 0;i<this.getSizeInventory();i++)
		{
			ItemStack stack = this.getStackInSlot(i);
			if(stack != null && stack.getItem() instanceof IBCBattery)
			{
				count++;
			}
		}
		return count <= 0;
	}
	
	public boolean noCharge()
	{
		int count = 0;
		for(int i = 0;i<this.getSizeInventory();i++)
		{
			ItemStack stack = this.getStackInSlot(i);
			if(stack != null && stack.getItem() instanceof IBCBattery)
			{
				IBCBattery battery = (IBCBattery)stack.getItem();
				if(battery.getStoredMJ(stack) > 0)
				{
					count++;
				}
			}
		}
		return count <= 0;
	}

	@Override
	public boolean canEmitPowerFrom(ForgeDirection side)
	{
		return true;
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
		return new ItemStack(TinyBlocks.transportBlock, 1, 6);
	}
	
}
