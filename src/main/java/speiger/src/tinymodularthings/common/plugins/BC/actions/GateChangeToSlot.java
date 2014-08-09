package speiger.src.tinymodularthings.common.plugins.BC.actions;

import javax.swing.Icon;

import speiger.src.api.items.InfoStack;
import speiger.src.api.language.LanguageRegister;
import speiger.src.tinymodularthings.TinyModularThings;
import buildcraft.BuildCraftTransport;
import buildcraft.api.gates.IAction;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GateChangeToSlot implements IAction
{
	int SlotID;
	
	public GateChangeToSlot(int id)
	{
		SlotID = id;
	}
	
	public int getSlotID()
	{
		return SlotID;
	}
	
	@Override
	public int getLegacyId()
	{
		return 0;
	}
	
	@Override
	public String getUniqueTag()
	{
		return "change.to.Slot." + SlotID;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon()
	{
		return BuildCraftTransport.actionPowerLimiter[6].getIcon();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		
	}
	
	@Override
	public boolean hasParameter()
	{
		return false;
	}
	
	@Override
	public String getDescription()
	{
		return LanguageRegister.getLanguageName(new InfoStack(), "Change.to.Slot", TinyModularThings.instance) + ": " + SlotID;
	}
	
}
