package speiger.src.tinymodularthings.common.items.energy;

import java.io.DataInput;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import speiger.src.api.common.data.packets.IPacketReciver;
import speiger.src.api.common.data.packets.SpmodPacketHelper;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.client.gui.buttons.GuiSliderButton;
import speiger.src.spmodapi.common.items.core.ItemInventory;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.items.energy.Batteries.BatterieType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class BatteryData extends ItemInventory implements IPacketReciver
{
	BatterieType type;
	boolean autoProviding;
	float transferlimit;
	boolean acceptingEnergy;
	boolean energyNet = false;
	//EnergyNet Functions
	boolean sendRandom;
	boolean sendToAll;
	int sendMode;
	
	public BatteryData(EntityPlayer player, ItemStack provider)
	{
		super(player, provider, 0);
		type = ((Batteries)provider.getItem()).getType();
	}

	@Override
	public String getInvName()
	{
		return "Battery Data";
	}
	
	@Override
	public boolean renderInnerInv()
	{
		return false;
	}
	
	@Override
	public boolean renderOuterInv()
	{
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onGuiLoad(GuiInventoryCore par1, int guiX, int guiY)
	{
		par1.getButtonsList().clear();
		
		if(!energyNet)
		{
			par1.getButtonsList().add(new GuiButton(0, guiX + 12, guiY + 20, 150, 20, "Auto Providing: "+autoProviding));
			par1.getButtonsList().add(new GuiButton(1, guiX + 12, guiY + 50, 150, 20, "Accepting Energy: "+acceptingEnergy));
			par1.getButtonsList().add(new GuiSliderButton(2, guiX + 12, guiY + 80, "Transferlimit: ", transferlimit, par1));
			par1.getButtonsList().add(new GuiButton(3, guiX + 12, guiY + 110, 150, 20, "Energy Net Settings"));
		}
		else
		{
			String[] possibles = new String[]{"Only Machines", "Only Batteries", "Everything"};
			par1.getButtonsList().add(new GuiButton(4, guiX + 55, guiY + 140, 60, 20, "Back"));
			par1.getButtonsList().add(new GuiButton(5, guiX + 12, guiY + 20, 150, 20, sendRandom ? "Send in Order" :"Send Randomly"));
			par1.getButtonsList().add(new GuiButton(6, guiX + 12, guiY + 50, 150, 20, sendToAll ? "Send To First Target" : "Send To All Targets"));
			par1.getButtonsList().add(new GuiSliderButton(7, guiX + 12, guiY + 80, "Sending Mode: ", 1F, par1));
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onButtonClick(GuiInventoryCore par1, GuiButton par2)
	{
		int id = par2.id;
		if(id == 0)
		{
			autoProviding = !autoProviding;
			par2.displayString = "Auto Providing: "+autoProviding;
		}
		else if(id == 1)
		{
			acceptingEnergy = !acceptingEnergy;
			par2.displayString = "Accepting Energy: "+acceptingEnergy;
		}
		else if(id == 3)
		{
			energyNet = true;
			onGuiLoad(par1, par1.getGuiX(), par1.getGuiY());
		}
		else if(id == 4)
		{
			energyNet = false;
			onGuiLoad(par1, par1.getGuiX(), par1.getGuiY());
		}
		else if(id == 5)
		{
			this.sendRandom = !sendRandom;
			par2.displayString = sendRandom ? "Send in Order" : "Send Randomly";
		}
		else if(id == 6)
		{
			sendToAll = !sendToAll;
			par2.displayString = sendToAll ? "Send To First Target" : "Send To All Targets";
		}

		if(id != 2 && id != 3 && id != 4 && id != 7)
		{
			this.sendPacketToServer(SpmodPacketHelper.getHelper().createPlayerTilePacket(player, TinyModularThings.instance).InjectNumber(id).finishPacket());
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onButtonUpdate(GuiInventoryCore par1, GuiButton par2)
	{
		int id = par2.id;
		if(id == 2)
		{
			GuiSliderButton button = (GuiSliderButton)par2;
			int newAmount = (int)(type.getTransferlimit() * button.sliderValue);
			button.displayString = button.originalName+newAmount+" MJ";
		}
		else if(id == 7)
		{
			String[] possibles = new String[]{"Only Machines", "Only Batteries", "Everything"};
			GuiSliderButton button = (GuiSliderButton)par2;
			int newAmount = (int)(2 * button.sliderValue);
			button.displayString = "SendingMode: "+possibles[newAmount];
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onButtonReleased(GuiInventoryCore par1, GuiButton par2)
	{
		int id = par2.id;
		if(id == 2)
		{
			GuiSliderButton button = (GuiSliderButton)par2;
			transferlimit = button.sliderValue;
			this.sendPacketToServer(SpmodPacketHelper.getHelper().createPlayerTilePacket(player, TinyModularThings.instance).InjectNumbers(id, transferlimit).finishPacket());
		}
		else if(id == 7)
		{
			GuiSliderButton button = (GuiSliderButton)par2;
			sendMode = (int)(2 * button.sliderValue);
			this.sendPacketToServer(SpmodPacketHelper.getHelper().createPlayerTilePacket(player, TinyModularThings.instance).InjectNumbers(id, sendMode).finishPacket());
		}
	}

	@Override
	public void recivePacket(DataInput par1)
	{
		try
		{
			int id = par1.readInt();
			if(id == 0)
			{
				autoProviding = !autoProviding;
			}
			else if(id == 1)
			{
				acceptingEnergy = !acceptingEnergy;
			}
			else if(id == 2)
			{
				transferlimit = par1.readFloat();
			}
			else if(id == 5)
			{
				sendRandom = !sendRandom;
			}
			else if(id == 6)
			{
				sendToAll = !sendToAll;
			}
			else if(id == 7)
			{
				sendMode = par1.readInt();
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
	public void readFromNBT(NBTTagCompound par1)
	{
		super.readFromNBT(par1);
		if(par1.hasKey("Limit")) transferlimit = par1.getFloat("Limit");
		else transferlimit = 1F;
		
		if(par1.hasKey("Input")) acceptingEnergy = par1.getBoolean("Input");
		else acceptingEnergy = true;
		
		if(par1.hasKey("AutoSend")) autoProviding = par1.getBoolean("AutoSend");
		else autoProviding = false;
		
		if(par1.hasKey("EnergyNet"))
		{
			NBTTagCompound net = par1.getCompoundTag("EnergyNet");
			sendRandom = net.getBoolean("Random");
			sendToAll = net.getBoolean("Multi");
			sendMode = net.getInteger("SendMode");
		}
		else
		{
			sendRandom = false;
			sendToAll = false;
			sendMode = 2;
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound par1)
	{
		super.writeToNBT(par1);
		par1.setFloat("Limit", transferlimit);
		par1.setBoolean("Input", acceptingEnergy);
		par1.setBoolean("AutoSend", autoProviding);
		NBTTagCompound net = new NBTTagCompound();
		net.setBoolean("Random", sendRandom);
		net.setBoolean("Multi", sendToAll);
		net.setInteger("SendMode", sendMode);
		par1.setCompoundTag("EnergyNet", net);
	}
	
	
}
