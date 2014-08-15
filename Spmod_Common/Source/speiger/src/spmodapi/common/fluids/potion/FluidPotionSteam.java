package speiger.src.spmodapi.common.fluids.potion;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import speiger.src.api.language.LanguageRegister;
import speiger.src.spmodapi.SpmodAPI;

public class FluidPotionSteam extends Fluid
{	
	public FluidPotionSteam(String fluidName)
	{
		super(fluidName);
		this.setGaseous(true);
		this.setTemperature(383);
	}

	@Override
	public String getLocalizedName()
	{
		return LanguageRegister.getLanguageName(this, "steam.potion", SpmodAPI.instance);
	}
	
	@Override
	public int getColor(FluidStack stack)
	{
		return stack.tag.getInteger("Color");
	}
	
}