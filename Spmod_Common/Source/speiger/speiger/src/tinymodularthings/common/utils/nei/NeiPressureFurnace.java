package speiger.src.tinymodularthings.common.utils.nei;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import speiger.src.api.recipe.pressureFurnace.PressureRecipe;
import speiger.src.api.recipe.pressureFurnace.helper.PressureRecipeList;
import speiger.src.tinymodularthings.client.gui.machine.PressureFurnaceGui;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;
import codechicken.core.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class NeiPressureFurnace extends TemplateRecipeHandler
{
	
	@Override
	public String getRecipeName()
	{
		return "Pressure Furnace";
	}
	
	@Override
	public int recipiesPerPage()
	{
		return 2;
	}
	
	@Override
	public String getGuiTexture()
	{
		return TinyModularThingsLib.ModID.toLowerCase() + ":textures/gui/machine/pressuredFurnace.png";
	}
	
	public String getRecipeID()
	{
		return "tiny.Pressure";
	}
	
	@Override
	public void loadTransferRects()
	{
		transferRects.add(new RecipeTransferRect(new Rectangle(90, 50, 30, 20), getRecipeID(), new Object[0]));
	}
	
	@Override
	public void drawBackground(int recipe)
	{
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GuiDraw.changeTexture(getGuiTexture());
		GuiDraw.drawTexturedModalRect(0, -11, 5, 11, 165, 80);
	}
	
	@Override
	public Class<? extends GuiContainer> getGuiClass()
	{
		return PressureFurnaceGui.class;
	}
	
	@Override
	public String getOverlayIdentifier()
	{
		return "smelting";
	}
	
	@Override
	public void drawExtras(int recipe)
	{
		CachedRecipe cuRecipe = arecipes.get(recipe);
		GuiDraw.drawTexturedModalRect(114, 26, 216, 14, 22, 16);
		GuiDraw.drawTexturedModalRect(101, 32, 216, 33, 13, 4);
		GuiDraw.drawTexturedModalRect(88, 32, 216, 33, 13, 4);
		GuiDraw.drawTexturedModalRect(75, 32, 216, 33, 13, 4);
		GuiDraw.drawTexturedModalRect(72, 32, 216, 33, 13, 4);
		if (cuRecipe != null && cuRecipe instanceof ChancedIOPressureRecipe)
		{
			ChancedIOPressureRecipe end = (ChancedIOPressureRecipe) cuRecipe;
			if (end.isSpecialRecipe())
			{
				GuiDraw.drawTexturedModalRect(101, 17, 216, 39, 4, 15);
				if (end.useCombiner())
				{
					GuiDraw.fontRenderer.drawString("Uses Cominer", 72, 40, 4210752);
				}
				else
				{
					GuiDraw.fontRenderer.drawString("Combiner Not Used", 50, 54, 4210752);
				}
			}
		}
	}
	
	@Override
	public void loadUsageRecipes(ItemStack ingredient)
	{
		for (PressureRecipe cu : PressureRecipeList.getInstance().getRecipeList())
		{
			if (cu.combinerEquals(ingredient) || cu.firstInputEquals(ingredient) || cu.secondInputEquals(ingredient))
			{
				arecipes.add(new ChancedIOPressureRecipe(cu));
			}
		}
	}
	
	@Override
	public void loadCraftingRecipes(ItemStack result)
	{
		for (PressureRecipe cu : PressureRecipeList.getInstance().getRecipeList())
		{
			if (NEIServerUtils.areStacksSameTypeCrafting(cu.getOutput(), result))
			{
				arecipes.add(new ChancedIOPressureRecipe(cu));
				break;
			}
		}
	}
	
	@Override
	public void loadCraftingRecipes(String outputId, Object... results)
	{
		if (outputId.equals(getRecipeID()))
		{
			for (PressureRecipe cu : PressureRecipeList.getInstance().getRecipeList())
			{
				arecipes.add(new ChancedIOPressureRecipe(cu));
			}
		}
		else
		{
			super.loadCraftingRecipes(outputId, results);
		}
	}
	
	public class ChancedIOPressureRecipe extends
			TemplateRecipeHandler.CachedRecipe
	{
		
		List<PositionedStack> list;
		PositionedStack output;
		boolean special = false;
		boolean useCombiner = false;
		
		public ChancedIOPressureRecipe(PressureRecipe par1)
		{
			output = new PositionedStack(par1.getOutput(), 145, 26, false);
			list = new ArrayList<PositionedStack>();
			useCombiner = par1.useCombiner();
			if (par1.hasFirstInput())
			{
				list.add(new PositionedStack(par1.getFirstInput(), 53, 15, false));
			}
			if (par1.hasSecondInput())
			{
				list.add(new PositionedStack(par1.getSecondInput(), 53, 36, false));
			}
			if (par1.hasCombiner())
			{
				special = true;
				list.add(new PositionedStack(par1.getCombiner(), 96, -2, false));
			}
		}
		
		public boolean isSpecialRecipe()
		{
			return special;
		}
		
		@Override
		public PositionedStack getResult()
		{
			return output;
		}
		
		@Override
		public List<PositionedStack> getIngredients()
		{
			return list;
		}
		
		public boolean useCombiner()
		{
			return useCombiner;
		}
		
	}
	
}
