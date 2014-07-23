package speiger.src.spmodapi.common.fluids.hemp;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.lib.SpmodAPILib;

public class FluidHempResin extends Fluid
{
	Icon itemIcon;
	public FluidHempResin()
	{
		super("hemp.resin");
		this.setDensity(2000);
		FluidRegistry.registerFluid(this);
	}

	@Override
	public Icon getStillIcon()
	{
		return itemIcon;
	}

	@Override
	public Icon getFlowingIcon()
	{
		return itemIcon;
	}

	@Override
	public String getLocalizedName()
	{
		return APIItems.hempResin.getItemDisplayName(new ItemStack(APIItems.hempResin));
	}

	public void registerIcon(IconRegister par1)
	{
		this.itemIcon = par1.registerIcon(SpmodAPILib.ModID.toLowerCase()+":hemp/hemp.resin");
	}
	
	
	
	
	
	
}
