package speiger.src.spmodapi.common.fluids.hemp;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.lib.SpmodAPILib;

public class FluidHempResin extends Fluid
{
	IIcon itemIcon;
	public FluidHempResin()
	{
		super("hemp.resin");
		this.setDensity(2000);
		FluidRegistry.registerFluid(this);
	}

	@Override
	public IIcon getStillIcon()
	{
		return itemIcon;
	}

	@Override
	public IIcon getFlowingIcon()
	{
		return itemIcon;
	}

	@Override
	public String getLocalizedName()
	{
		return APIItems.hempResin.getItemStackDisplayName(new ItemStack(APIItems.hempResin));
	}

	public void registerIcon(IIconRegister par1)
	{
		this.itemIcon = par1.registerIcon(SpmodAPILib.ModID.toLowerCase()+":hemp/hemp.resin");
	}
	
	
	
	
	
	
}
