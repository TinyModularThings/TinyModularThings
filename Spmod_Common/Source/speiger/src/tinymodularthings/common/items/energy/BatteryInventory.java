package speiger.src.tinymodularthings.common.items.energy;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import speiger.src.api.common.data.packets.SpmodPacketHelper.SpmodPacket;
import speiger.src.api.common.world.items.energy.IBCBattery.BatteryType;
import speiger.src.api.common.world.items.energy.ItemEnergyNet.BatteryContainer;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.client.gui.buttons.GuiSliderButton;
import speiger.src.spmodapi.common.items.core.ItemInventory;
import speiger.src.tinymodularthings.common.network.packets.client.BatteryPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class BatteryInventory extends ItemInventory
{
	public static final String[] sendingModes = new String[] {"Only Machines", "Only Batteries", "Everything" };
	public static final String[] drawingModes = new String[] {"Only Generators", "Only Batteries", "Everything" };
	
	BatteryContainer container;
	boolean supportEnergyNet;
	boolean energyNetActive;
	boolean supportBatteryConfig;
	boolean batteryConfigActive;
	boolean supportAutomatics;
	
	//EnergyNet Functions
	private boolean randomMode;
	private boolean multiMode;
	private float sendingMode;
	private float drawingMode;
	
	//BatteryOptions
	private boolean acceptingPower;
	private boolean acceptingPowerDraw;
	private boolean autoSending;
	private boolean autoDrawing;
	private float transferlimitIn;
	private float transferlimitOut;
	private float mastertransferlimit;
	private float tickRate;
	
	private NBTTagCompound backup;
	
	public BatteryInventory(EntityPlayer player, ItemStack provider, int inventorySize)
	{
		super(player, provider, inventorySize);
		container = new BatteryContainer(provider);
		loadData();
	}
	
	public BatteryInventory(EntityPlayer player, ItemStack provider, int inventorySize, boolean supportsEnergyNet, boolean supportBatteryOptions, boolean supportsautomatics)
	{
		this(player, provider, inventorySize);
		supportEnergyNet = supportsEnergyNet;
		supportBatteryConfig = supportBatteryOptions;
		supportAutomatics = supportsautomatics;
	}
	
	public boolean isEnergyNetGuiActive()
	{
		return energyNetActive;
	}
	
	public boolean isBatteryOptionGuiActive()
	{
		return batteryConfigActive;
	}
	
	@Override
	public boolean stopTickingOnGuiOpen()
	{
		return true;
	}
	
	
	
	@Override
	public void onSpmodPacket(SpmodPacket par1)
	{
		super.onSpmodPacket(par1);
		if(par1.getPacket() instanceof BatteryPacket)
		{
			BatteryPacket packet = (BatteryPacket)par1.getPacket();
			onPacketData(packet.key, packet.value);
		}
	}
	
	private void onPacketData(int id, float value)
	{
		BatteryType type = container.getType();
		if(id == 250)
			randomMode = !randomMode;
		if(id == 251)
			multiMode = !multiMode;
		if(id == 252)
		{
			if(type == BatteryType.Generator || type == BatteryType.Storage)
			{
				sendingMode = value;
			}
			else
			{
				drawingMode = value;
			}
		}
		switch(type)
		{
			case Generator:
				if(id == 261)
					acceptingPowerDraw = !acceptingPowerDraw;
				if(id == 262)
					transferlimitOut = value;
				if(id == 263)
					autoSending = !autoSending;
				if(id == 264)
					mastertransferlimit = value;
				if(id == 265)
					tickRate = value;
				break;
			case Machine:
				if(id == 261)
					acceptingPower = !acceptingPower;
				if(id == 262)
					transferlimitIn = value;
				if(id == 263)
					autoDrawing = !autoDrawing;
				if(id == 264)
					mastertransferlimit = value;
				if(id == 265)
					tickRate = value;
				break;
			case Storage:
				if(id == 261)
					acceptingPower = !acceptingPower;
				if(id == 262)
					transferlimitIn = value;
				if(id == 263)
					acceptingPowerDraw = !acceptingPowerDraw;
				if(id == 264)
					transferlimitOut = value;
				if(id == 265)
					autoSending = !autoSending;
				if(id == 266)
					mastertransferlimit = value;
				if(id == 267)
					tickRate = value;
				break;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onButtonClick(GuiInventoryCore par1, GuiButton par2)
	{
		int id = par2.id;
		boolean sendPacket = false;
		BatteryType type = container.getType();
		if(supportEnergyNet)
		{
			if(id == 249)
			{
				this.energyNetActive = !this.energyNetActive;
				par1.setScreenChanges();
				this.onGuiLoad(par1, par1.getGuiX(), par1.getGuiY());
			}
			if(container.getType() == BatteryType.Generator || container.getType() == BatteryType.Storage)
			{
				if(id == 250)
				{
					randomMode = !randomMode;
					par2.displayString = randomMode ? "Send in Order" : "Send Randomly";
					sendPacket = true;
				}
				if(id == 251)
				{
					multiMode = !multiMode;
					par2.displayString = multiMode ? "Send To First Target" : "Send To All Targets";
					sendPacket = true;
				}
			}
			else
			{
				if(id == 250)
				{
					randomMode = !randomMode;
					par2.displayString = randomMode ? "Drawing in Order" : "Drawing Randomly";
					sendPacket = true;
				}
				if(id == 251)
				{
					multiMode = !multiMode;
					par2.displayString = multiMode ? "Draw from First Target" : "Draw from All Targets";
					sendPacket = true;
				}
			}
		}
		if(supportBatteryConfig)
		{
			if(id == 260)
			{
				this.batteryConfigActive = !batteryConfigActive;
				par1.setScreenChanges();
				if(batteryConfigActive)
				{
					par1.setY(204);
				}
				else
				{
					par1.setY(166);
				}
				this.onGuiLoad(par1, par1.getGuiX(), par1.getGuiY());
			}
			switch(type)
			{
				case Generator:
					if(id == 261)
					{
						acceptingPowerDraw = !acceptingPowerDraw;
						par2.displayString = "Allow Energy Draw: " + this.acceptingPowerDraw;
						sendPacket = true;
					}
					if(id == 263)
					{
						autoSending = !autoSending;
						par2.displayString = "Auto Providing: " + this.autoSending;
						sendPacket = true;
					}
					break;
				case Machine:
					if(id == 261)
					{
						acceptingPower = !acceptingPower;
						par2.displayString = "Accepting Energy: " + acceptingPower;
						sendPacket = true;
					}
					if(id == 263)
					{
						autoDrawing = !autoDrawing;
						par2.displayString = "Auto Drawing: " + this.autoDrawing;
						sendPacket = true;
					}
					break;
				case Storage:
					if(id == 261)
					{
						acceptingPower = !acceptingPower;
						par2.displayString = "Accepting Energy: " + acceptingPower;
						sendPacket = true;
						if(acceptingPower && autoSending && type == BatteryType.Storage)
						{
							this.onButtonClick(par1, par1.getButtonFromID(265));
						}
					}
					if(id == 263)
					{
						acceptingPowerDraw = !acceptingPowerDraw;
						par2.displayString = "Allow Energy Draw: " + acceptingPowerDraw;
						sendPacket = true;
					}
					if(id == 265)
					{
						autoSending = !autoSending;
						par2.displayString = "Auto Sending: " + autoSending;
						sendPacket = true;
						if(type == BatteryType.Storage && acceptingPower && autoSending)
						{
							this.onButtonClick(par1, par1.getButtonFromID(261));
						}
					}
					break;
			}
			
		}

		if(sendPacket)
		{
			BatteryPacket packet = new BatteryPacket(this);
			packet.setData(id);
			this.sendPacketToServer(SpmodAPI.handler.createFinishPacket(packet));
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onGuiLoad(GuiInventoryCore par1, int guiX, int guiY)
	{
		par1.getButtonsList().clear();
		
		if(energyNetActive)
		{
			List<GuiButton> buttons = par1.getButtonsList();
			switch(container.getType())
			{
				case Generator:
					buttons.add(new GuiButton(249, guiX + 55, guiY + 95, 60, 20, "Back"));
					buttons.add(new GuiButton(250, guiX + 12, guiY + 20, 150, 20, randomMode ? "Send in Order" : "Send Randomly"));
					buttons.add(new GuiButton(251, guiX + 12, guiY + 45, 150, 20, multiMode ? "Send To First Target" : "Send To All Targets"));
					buttons.add(new GuiSliderButton(252, guiX + 12, guiY + 70, "Sending Mode: ", sendingMode, par1).setWeelEffect(0.3F));
					break;
				case Machine:
					buttons.add(new GuiButton(249, guiX + 55, guiY + 95, 60, 20, "Back"));
					buttons.add(new GuiButton(250, guiX + 12, guiY + 20, 150, 20, randomMode ? "Drawing in Order" : "Drawing Randomly"));
					buttons.add(new GuiButton(251, guiX + 12, guiY + 45, 150, 20, multiMode ? "Draw from First Target" : "Draw from All Targets"));
					buttons.add(new GuiSliderButton(252, guiX + 12, guiY + 70, "Drawing Mode: ", drawingMode, par1).setWeelEffect(0.3F));
					break;
				case Storage:
					buttons.add(new GuiButton(249, guiX + 55, guiY + 95, 60, 20, "Back"));
					buttons.add(new GuiButton(250, guiX + 12, guiY + 20, 150, 20, randomMode ? "Send in Order" : "Send Randomly"));
					buttons.add(new GuiButton(251, guiX + 12, guiY + 45, 150, 20, multiMode ? "Send To First Target" : "Send To All Targets"));
					buttons.add(new GuiSliderButton(252, guiX + 12, guiY + 70, "Sending Mode: ", sendingMode, par1).setWeelEffect(0.3F));
					break;
			
			}
		}
		if(batteryConfigActive)
		{
			List<GuiButton> buttons = par1.getButtonsList();
			switch(container.getType())
			{
				case Generator:
					buttons.add(new GuiButton(261, guiX + 7, guiY + 15, 160, 20, "Allow Energy Draw: " + this.acceptingPowerDraw));
					buttons.add(new GuiSliderButton(262, guiX + 7, guiY + 40, 160, 20, "Drawing Transferlimit: ", this.transferlimitOut, par1).setWeelEffect(1F / (float)container.getMaxTransferlimit()));
					if(this.supportAutomatics)
					{
						buttons.add(new GuiButton(263, guiX + 7, guiY + 65, 160, 20, "Auto Providing: " + this.autoSending));
						buttons.add(new GuiSliderButton(264, guiX + 7, guiY + 115, 160, 20, "Sending Transferlimit: ", this.mastertransferlimit, par1).setWeelEffect(1F / (float)container.getMaxTransferlimit()));
						buttons.add(new GuiSliderButton(265, guiX + 7, guiY + 90, 160, 20, "Tick Rate: ", this.tickRate, par1).setWeelEffect(1F / 1000F));
						buttons.add(new GuiButton(260, guiX + 55, guiY + 140, 60, 20, "Back"));
					}
					else
					{
						buttons.add(new GuiButton(260, guiX + 55, guiY + 65, 60, 20, "Back"));
					}
					break;
				case Machine:
					buttons.add(new GuiButton(261, guiX + 7, guiY + 15, 160, 20, "Accepting Energy: " + this.acceptingPower));
					buttons.add(new GuiSliderButton(262, guiX + 7, guiY + 40, 160, 20, "Income Transferlimit: ", this.transferlimitIn, par1).setWeelEffect(1F / (float)container.getMaxTransferlimit()));
					if(this.supportAutomatics)
					{
						buttons.add(new GuiButton(263, guiX + 7, guiY + 65, 160, 20, "Auto Drawing: " + this.autoDrawing));
						buttons.add(new GuiSliderButton(264, guiX + 7, guiY + 90, 160, 20, "Drawing Transferlimit: ", this.mastertransferlimit, par1).setWeelEffect(1F / (float)container.getMaxTransferlimit()));
						buttons.add(new GuiSliderButton(265, guiX + 7, guiY + 115, 160, 20, "Tick Rate: ", this.tickRate, par1).setWeelEffect(1F / 1000F));
						buttons.add(new GuiButton(260, guiX + 55, guiY + 140, 60, 20, "Back"));
					}
					else
					{
						buttons.add(new GuiButton(260, guiX + 55, guiY + 65, 60, 20, "Back"));
					}
					break;
				case Storage:
					buttons.add(new GuiButton(261, guiX + 7, guiY + 15, 160, 20, "Accepting Energy: " + this.acceptingPower));
					buttons.add(new GuiSliderButton(262, guiX + 7, guiY + 37, 160, 20, "Income Transferlimit: ", this.transferlimitIn, par1).setWeelEffect(1F / (float)container.getMaxTransferlimit()));
					buttons.add(new GuiButton(263, guiX + 7, guiY + 59, 160, 20, "Allow Energy Draw: " + this.acceptingPowerDraw));
					buttons.add(new GuiSliderButton(264, guiX + 7, guiY + 81, 160, 20, "Output Transferlimit: ", this.transferlimitOut, par1).setWeelEffect(1F / (float)container.getMaxTransferlimit()));
					if(supportAutomatics)
					{
						buttons.add(new GuiButton(265, guiX + 7, guiY + 103, 160, 20, "Auto Sending: " + this.autoSending));
						buttons.add(new GuiSliderButton(266, guiX + 7, guiY + 125, 160, 20, "Sending Transferlimit: ", this.mastertransferlimit, par1).setWeelEffect(1F / (float)container.getMaxTransferlimit()));
						buttons.add(new GuiSliderButton(267, guiX + 7, guiY + 148, 160, 20, "Tick Rate: ", this.tickRate, par1).setWeelEffect(1F / 1000F));
					}
					buttons.add(new GuiButton(260, guiX + 55, guiY + 180, 60, 20, "Back"));
					break;
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onButtonUpdate(GuiInventoryCore par1, GuiButton par2)
	{
		int id = par2.id;
		BatteryType type = container.getType();
		if(this.energyNetActive)
		{
			if(id == 252)
			{
				GuiSliderButton button = (GuiSliderButton)par2;
				int data = (int)(2 * button.sliderValue);
				if(type == BatteryType.Generator || type == BatteryType.Storage)
					button.displayString = "Sending Mode: " + sendingModes[data];
				else
					button.displayString = "Drawing Mode: " + drawingModes[data];
			}
		}
		if(this.supportBatteryConfig)
		{
			switch(type)
			{
				case Generator:
					if(id == 262)
					{
						GuiSliderButton button = (GuiSliderButton)par2;
						int newValue = (int)((float)container.getMaxTransferlimit() * button.sliderValue);
						button.displayString = "Drawing Transferlimit: " + newValue + " MJ";
					}
					if(id == 264)
					{
						GuiSliderButton button = (GuiSliderButton)par2;
						int newValue = (int)((float)container.getMaxTransferlimit() * button.sliderValue);
						button.displayString = "Sending Transferlimit: " + newValue + " MJ";
					}
					if(id == 265)
					{
						GuiSliderButton button = (GuiSliderButton)par2;
						int newValue = (int)(1000F * button.sliderValue);
						button.displayString = "Tick Rate: " + newValue + " Ticks";
					}
					break;
				case Machine:
					if(id == 262)
					{
						GuiSliderButton button = (GuiSliderButton)par2;
						int newValue = (int)((float)container.getMaxTransferlimit() * button.sliderValue);
						button.displayString = "Income Transferlimit: " + newValue + " MJ";
					}
					if(id == 264)
					{
						GuiSliderButton button = (GuiSliderButton)par2;
						int newValue = (int)((float)container.getMaxTransferlimit() * button.sliderValue);
						button.displayString = "Drawing Transferlimit: " + newValue + " MJ";
					}
					if(id == 265)
					{
						GuiSliderButton button = (GuiSliderButton)par2;
						int newValue = (int)(1000F * button.sliderValue);
						button.displayString = "Tick Rate: " + newValue + " Ticks";
					}
					break;
				case Storage:
					if(id == 262)
					{
						GuiSliderButton button = (GuiSliderButton)par2;
						int newValue = (int)((float)container.getMaxTransferlimit() * button.sliderValue);
						button.displayString = "Income Transferlimit: " + newValue + " MJ";
					}
					if(id == 264)
					{
						GuiSliderButton button = (GuiSliderButton)par2;
						int newValue = (int)((float)container.getMaxTransferlimit() * button.sliderValue);
						button.displayString = "Output Transferlimit: " + newValue + " MJ";
					}
					if(id == 266)
					{
						GuiSliderButton button = (GuiSliderButton)par2;
						int newValue = (int)((float)container.getMaxTransferlimit() * button.sliderValue);
						button.displayString = "Sending Transferlimit: " + newValue + " MJ";
					}
					if(id == 267)
					{
						GuiSliderButton button = (GuiSliderButton)par2;
						int newValue = (int)(1000F * button.sliderValue);
						button.displayString = "Tick Rate: " + newValue + " Ticks";
					}
					break;
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onButtonReleased(GuiInventoryCore par1, GuiButton par2)
	{
		int id = par2.id;
		boolean sendPacket = false;
		BatteryType type = container.getType();
		BatteryPacket packet = new BatteryPacket(this);
		packet.setData(id);
		if(energyNetActive)
		{
			if(id == 252)
			{
				if(type == BatteryType.Generator || type == BatteryType.Storage)
				{
					this.sendingMode = ((GuiSliderButton)par2).sliderValue;
					packet.setValue(sendingMode);
					sendPacket = true;
				}
				else
				{
					this.drawingMode = ((GuiSliderButton)par2).sliderValue;
					packet.setValue(drawingMode);
					sendPacket = true;
				}
				
			}
		}
		if(batteryConfigActive)
		{
			if(type == BatteryType.Generator)
			{
				if(id == 262)
				{
					this.transferlimitOut = ((GuiSliderButton)par2).sliderValue;
					packet.setValue(transferlimitOut);
				}
				if(id == 264)
				{
					this.mastertransferlimit = ((GuiSliderButton)par2).sliderValue;
					packet.setValue(mastertransferlimit);
				}
				if(id == 265)
				{
					tickRate = ((GuiSliderButton)par2).sliderValue;
					packet.setValue(tickRate);
				}
			}
			else if(type == BatteryType.Machine)
			{
				if(id == 262)
				{
					this.transferlimitIn = ((GuiSliderButton)par2).sliderValue;
					packet.setValue(transferlimitIn);
				}
				if(id == 264)
				{
					this.mastertransferlimit = ((GuiSliderButton)par2).sliderValue;
					packet.setValue(mastertransferlimit);
				}
				if(id == 265)
				{
					tickRate = ((GuiSliderButton)par2).sliderValue;
					packet.setValue(tickRate);
				}
			}
			else
			{
				if(id == 262)
				{
					transferlimitIn = ((GuiSliderButton)par2).sliderValue;
					packet.setValue(transferlimitIn);
				}
				if(id == 264)
				{
					transferlimitOut = ((GuiSliderButton)par2).sliderValue;
					packet.setValue(transferlimitOut);
				}
				if(id == 266)
				{
					mastertransferlimit = ((GuiSliderButton)par2).sliderValue;
					packet.setValue(mastertransferlimit);
				}
				if(id == 267)
				{
					tickRate = ((GuiSliderButton)par2).sliderValue;
					packet.setValue(tickRate);
				}
			}
			sendPacket = true;
		}
		
		if(sendPacket)
		{
			this.sendPacketToServer(SpmodAPI.handler.createFinishPacket(packet));
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void addEnergyNetButton(GuiInventoryCore par1, int posX, int posY, int sizeX, int sizeY)
	{
		par1.getButtonsList().add(new GuiButton(249, posX, posY, sizeX, sizeY, "EnergyNet"));
	}
	
	@SideOnly(Side.CLIENT)
	public void addBatteryOptionButton(GuiInventoryCore par1, int posX, int posY, int sizeX, int sizeY)
	{
		par1.getButtonsList().add(new GuiButton(260, posX, posY, sizeX, sizeY, "Battery Settings"));
	}
	
	@Override
	public void readFromNBT(NBTTagCompound par1)
	{
		super.readFromNBT(par1);
		backup = par1;
	}
	
	public void loadData()
	{
		NBTTagCompound par1 = backup;
		backup = null;
		if(par1.hasKey("EnergyNet"))
		{
			NBTTagCompound nbt = par1.getCompoundTag("EnergyNet");
			randomMode = nbt.getBoolean("Random");
			multiMode = nbt.getBoolean("Multi");
			sendingMode = nbt.getFloat("SendMode");
			drawingMode = nbt.getFloat("ReqMode");
		}
		else
		{
			randomMode = false;
			multiMode = false;
			sendingMode = 1F;
			drawingMode = 1F;
		}
		if(par1.hasKey("BatteryData"))
		{
			NBTTagCompound nbt = par1.getCompoundTag("BatteryData");
			tickRate = nbt.getFloat("TickRate");
			mastertransferlimit = nbt.getFloat("Transferlimit");
			transferlimitIn = nbt.getFloat("MaxIn");
			transferlimitOut = nbt.getFloat("MaxOut");
			autoSending = nbt.getBoolean("AutoOut");
			autoDrawing = nbt.getBoolean("AutoIn");
			acceptingPower = nbt.getBoolean("AllowIn");
			acceptingPowerDraw = nbt.getBoolean("AllowOut");
		}
		else
		{
			acceptingPower = true;
			acceptingPowerDraw = true;
			autoSending = false;
			autoDrawing = false;
			transferlimitIn = 1F;
			transferlimitOut = 1F;
			mastertransferlimit = 1F;
			tickRate = 0F;
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound par1)
	{
		super.writeToNBT(par1);
		if(this.supportEnergyNet)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setBoolean("Random", this.randomMode);
			nbt.setBoolean("Multi", this.multiMode);
			nbt.setFloat("ReqMode", this.drawingMode);
			nbt.setFloat("SendMode", this.sendingMode);
			par1.setCompoundTag("EnergyNet", nbt);
		}
		if(this.supportBatteryConfig)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setFloat("TickRate", tickRate);
			nbt.setFloat("Transferlimit", mastertransferlimit);
			nbt.setFloat("MaxIn", transferlimitIn);
			nbt.setFloat("MaxOut", transferlimitOut);
			nbt.setBoolean("AutoOut", autoSending);
			nbt.setBoolean("AutoIn", autoDrawing);
			nbt.setBoolean("AllowIn", acceptingPower);
			nbt.setBoolean("AllowOut", acceptingPowerDraw);
			par1.setCompoundTag("BatteryData", nbt);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ResourceLocation getTexture()
	{
		return needBiggerTexture() ? getEngine().getTexture("MediumFrame") : super.getTexture();
	}
	
	public boolean needBiggerTexture()
	{
		return this.container.getType() == BatteryType.Storage && this.batteryConfigActive;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getNameYOffset()
	{
		return needBiggerTexture() ? -18 : super.getNameYOffset();
	}
	
	
}
