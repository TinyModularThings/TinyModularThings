package speiger.src.tinymodularthings.common.plugins.BC.triggers;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.IIcon;
import speiger.src.api.items.InfoStack;
import speiger.src.api.language.LanguageRegister;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.blocks.machine.PressureFurnace;
import buildcraft.api.gates.IGate;
import buildcraft.api.gates.IStatement;
import buildcraft.api.gates.IStatementParameter;
import buildcraft.api.gates.ITrigger;
import buildcraft.api.gates.ITriggerParameter;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TriggerFuel implements ITrigger
{
	
	@Override
	public String getUniqueTag()
	{
		return "trigger.fuel";
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon()
	{
		return Items.coal.getIconFromDamage(0);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister)
	{
		
	}
	
	

	@Override
	public String getDescription()
	{
		return LanguageRegister.getLanguageName(new InfoStack(), "need.Fuel", TinyModularThings.instance);
	}
	
	
	public boolean hasFurnaceFuel(TileEntityFurnace par1)
	{
		int totalFuel = par1.furnaceBurnTime;
		if (par1.getStackInSlot(1) != null)
		{
			totalFuel += TileEntityFurnace.getItemBurnTime(par1.getStackInSlot(1)) * par1.getStackInSlot(1).stackSize;
		}
		if (totalFuel < 5000)
		{
			return true;
		}
		return false;
	}

	@Override
	public int maxParameters()
	{
		return 0;
	}

	@Override
	public int minParameters()
	{
		return 0;
	}

	@Override
	public IStatementParameter createParameter(int index)
	{
		return null;
	}

	@Override
	public IStatement rotateLeft()
	{
		return null;
	}

	@Override
	public boolean isTriggerActive(IGate gate, ITriggerParameter[] parameters)
	{
		TileEntity tile = gate.getPipe().getAdjacentTile(gate.getSide());
		if (tile != null)
		{
			if (tile instanceof PressureFurnace)
			{
				return ((PressureFurnace) tile).hasFuel();
			}
			else if (tile instanceof TileEntityFurnace)
			{
				return hasFurnaceFuel((TileEntityFurnace) tile);
			}
			
		}
		return false;
	}


}
