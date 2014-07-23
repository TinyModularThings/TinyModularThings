package speiger.src.tinymodularthings.common.upgrades.hoppers;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import speiger.src.api.hopper.HopperRegistry;
import speiger.src.api.hopper.HopperUpgrade;
import speiger.src.api.hopper.IHopper;
import speiger.src.api.hopper.IUpgradeGuiProvider;
import speiger.src.api.items.InfoStack;
import speiger.src.api.language.LanguageRegister;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.enums.EnumIDs;
import speiger.src.tinymodularthings.common.utils.HopperType;
import cpw.mods.fml.common.FMLLog;

public class HopperItemFilter implements HopperUpgrade, IUpgradeGuiProvider
{
	
	@Override
	public void onTick(IHopper paramIHopper)
	{
	}
	
	@Override
	public void onNBTWrite(NBTTagCompound paramNBTTagCompound)
	{
	}
	
	@Override
	public void onNBTRead(NBTTagCompound paramNBTTagCompound)
	{
	}
	
	@Override
	public String getUpgradeName()
	{
		return LanguageRegister.getLanguageName(new InfoStack(), "hopper.upgrade.item.filter", TinyModularThings.instance);
	}
	
	@Override
	public String getNBTName()
	{
		return "hopper.upgrade.item.filter";
	}
	
	@Override
	public void getInformationList(EntityPlayer player, List par2)
	{
	}
	
	@Override
	public void onRegisterUpgrade(IHopper par1)
	{	
	}
	
	@Override
	public void onRemovingUpgrade(IHopper par1)
	{	
	}
	
	@Override
	public int getMaxStackSize()
	{
		return 1;
	}
	
	@Override
	public boolean onClick(boolean sneak, EntityPlayer player, Block block, IHopper hopper, int side)
	{
		ItemStack item = player.getCurrentEquippedItem();
		FMLLog.getLogger().info("Test");
		if(item != null && item.itemID == Item.silk.itemID)
		{
			FMLLog.getLogger().info("Test");
			HopperRegistry.makeGuiProviderForUpgrade(item, this);
			player.openGui(TinyModularThings.instance, EnumIDs.HopperUpgrades.getId(), hopper.getWorld(), hopper.getXPos(), hopper.getYPos(), hopper.getZPos());
			HopperRegistry.isRemovingUpgrade(item);
			return true;
		}
		return false;
	}
	
	@Override
	public HopperType getUpgradeType()
	{
		return HopperType.Items;
	}

	@Override
	public GuiContainer getGui(InventoryPlayer par1, IHopper par2)
	{
		return ((AdvTile)par2).getGui(par1);
	}

	@Override
	public Container getInventory(InventoryPlayer par1, IHopper par2)
	{
		return ((AdvTile)par2).getInventory(par1);
	}
	
	
	
}
