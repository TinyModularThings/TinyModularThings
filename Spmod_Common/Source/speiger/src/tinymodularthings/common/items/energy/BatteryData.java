package speiger.src.tinymodularthings.common.items.energy;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import speiger.src.api.common.data.packets.IPacketReciver;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.tinymodularthings.common.items.energy.Batteries.BatterieType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BatteryData extends BatteryInventory implements IPacketReciver
{
	BatterieType type;
	
	public BatteryData(EntityPlayer player, ItemStack provider)
	{
		super(player, provider, 0, true, true, true);
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
		super.onGuiLoad(par1, guiX, guiY);
		if(!this.isEnergyNetGuiActive() && !this.isBatteryOptionGuiActive())
		{
			this.addEnergyNetButton(par1, guiX + 12, guiY + 15, 150, 20);
			this.addBatteryOptionButton(par1, guiX + 12, guiY + 40, 150, 20);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onButtonClick(GuiInventoryCore par1, GuiButton par2)
	{
		super.onButtonClick(par1, par2);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onButtonUpdate(GuiInventoryCore par1, GuiButton par2)
	{
		super.onButtonUpdate(par1, par2);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onButtonReleased(GuiInventoryCore par1, GuiButton par2)
	{
		super.onButtonReleased(par1, par2);
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
	}
	
	@Override
	public void writeToNBT(NBTTagCompound par1)
	{
		super.writeToNBT(par1);
	}
}
