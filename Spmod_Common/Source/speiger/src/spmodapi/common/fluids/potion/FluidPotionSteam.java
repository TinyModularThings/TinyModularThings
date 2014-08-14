package speiger.src.spmodapi.common.fluids.potion;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import speiger.src.api.language.LanguageRegister;
import speiger.src.spmodapi.SpmodAPI;

public class FluidPotionSteam extends Fluid
{
	private static ArrayList list = new ArrayList();
	
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
	
	public static class PotionRecipe
	{
		FluidStack fluidKey;
		FluidStack fluidPart;
		ItemStack itemPart;
		FluidStack output;
		
		public PotionRecipe(PotionRecipePart par1)
		{
			fluidKey = par1.fluidKey;
			fluidPart = par1.fluidPart;
			itemPart = par1.itemPart;
			output = par1.output;
		
			if(fluidPart != null && itemPart != null)
			{
				fluidKey = null;
				fluidPart = null;
				itemPart = null;
				output = null;
			}
		}
		
		public FluidStack getFirstFluid()
		{
			return fluidKey;
		}
		
		public FluidStack getRecipeFluid()
		{
			return fluidPart;
		}
		
		public ItemStack getRecipeItem()
		{
			return itemPart;
		}
		
		public FluidStack getResult()
		{
			return output;
		}
	}
	
	
	public static class PotionRecipePart
	{
		FluidStack fluidKey;
		FluidStack fluidPart;
		ItemStack itemPart;
		FluidStack output;
		
		public PotionRecipePart()
		{		
		}
		
		public PotionRecipePart setFluidKey(FluidStack par1)
		{
			fluidKey = par1.copy();
			return this;
		}
		
		public PotionRecipePart setFluidPart(FluidStack par1)
		{
			fluidPart = par1.copy();
			return this;
		}
		
		public PotionRecipePart setItemPart(ItemStack par1)
		{
			itemPart = par1.copy();
			return this;
		}
		
		public PotionRecipePart setResult(FluidStack par1)
		{
			output = par1.copy();
			return this;
		}
		
		
	}
	
}