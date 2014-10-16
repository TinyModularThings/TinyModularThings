package speiger.src.spmodapi.common.fluids.hemp;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.lib.SpmodAPILib;
import speiger.src.spmodapi.common.util.TextureEngine;

public class FluidHempResin extends Fluid
{
	
	public FluidHempResin()
	{
		super("hemp.resin");
		this.setDensity(2000);
		FluidRegistry.registerFluid(this);
	}
	
	@Override
	public Icon getStillIcon()
	{
		return TextureEngine.getTextures().getTexture(APIBlocks.fluidHempResin);
	}
	
	@Override
	public Icon getFlowingIcon()
	{
		return TextureEngine.getTextures().getTexture(APIBlocks.fluidHempResin);
	}
	
	@Override
	public String getLocalizedName()
	{
		return APIItems.hempResin.getItemDisplayName(new ItemStack(APIItems.hempResin));
	}
	
}
