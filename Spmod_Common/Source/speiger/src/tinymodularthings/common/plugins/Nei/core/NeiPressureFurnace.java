package speiger.src.tinymodularthings.common.plugins.Nei.core;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

import org.lwjgl.opengl.GL11;

import speiger.src.api.common.registry.recipes.pressureFurnace.IPressureFurnaceRecipe;
import speiger.src.api.common.registry.recipes.pressureFurnace.PressureRecipeList;
import speiger.src.api.common.utils.InventoryUtil;
import speiger.src.api.common.utils.MathUtils;
import speiger.src.spmodapi.common.util.proxy.CodeProxy;
import speiger.src.tinymodularthings.common.blocks.machine.PressureFurnace;
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
		transferRects.add(new RecipeTransferRect(new Rectangle(112, 20, 25, 30), getRecipeID(), new Object[]{true}));
		transferRects.add(new RecipeTransferRect(new Rectangle(7, 16, 20, 13), getRecipeID(), new Object[]{false}));
	}
	
	@Override
	public void drawBackground(int recipe)
	{
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GuiDraw.changeTexture(getGuiTexture());
		GuiDraw.drawTexturedModalRect(0, -11, 5, 11, 165, 70);
	}
	
	
	@Override
	public String getOverlayIdentifier()
	{
		return "smelting";
	}
	
	@Override
	public void drawExtras(int recipe)
	{
		ChancedIOPressureRecipe cuRecipe = (ChancedIOPressureRecipe)arecipes.get(recipe);
		
		GuiDraw.drawTexturedModalRect(114, 26, 216, 14, 22, 16);
		GuiDraw.drawTexturedModalRect(101, 32, 216, 33, 13, 4);
		GuiDraw.drawTexturedModalRect(88, 32, 216, 33, 13, 4);
		GuiDraw.drawTexturedModalRect(75, 32, 216, 33, 13, 4);
		GuiDraw.drawTexturedModalRect(72, 32, 216, 33, 13, 4);
		if (cuRecipe != null && cuRecipe instanceof ChancedIOPressureRecipe)
		{
			ChancedIOPressureRecipe end = (ChancedIOPressureRecipe) cuRecipe;
			if(end.flag())
			{
				if (end.isSpecialRecipe())
				{
					GuiDraw.drawTexturedModalRect(102, 17, 216, 39, 4, 15);
					if (end.useCombiner())
					{
						GuiDraw.fontRenderer.drawString("Uses Cominer", 72, 40, 4210752);
					}
					else
					{
						GuiDraw.fontRenderer.drawString("Combiner Not Used", 50, 54, 4210752);
					}
				}
				GuiDraw.fontRenderer.drawString("Cook Time: ", 115, -1, 4210752);
				GuiDraw.fontRenderer.drawString(Math.max(end.par1.getRequiredCookTime(), 100)+" Ticks", 115, 9, 4210752);
			}
			else
			{
				ItemStack stack = end.getOtherStack().item;
				if(stack != null)
				{
					int time = TileEntityFurnace.getItemBurnTime(stack)*4;
					GuiDraw.fontRenderer.drawString("BurnTime:", 0, 50, 4210752);
					GuiDraw.fontRenderer.drawString("Idling:", 0, 61, 4210752);
					GuiDraw.fontRenderer.drawString("Working:", 0, 71, 4210752);
					
					GuiDraw.fontRenderer.drawString(MathUtils.getTicksInTimeShort(time), 120, 61, 4210752);
					time /= 2;
					GuiDraw.fontRenderer.drawString(MathUtils.getTicksInTimeShort(time), 120, 71, 4210752);
				}

			}
		}
	}
	
	@Override
	public void loadUsageRecipes(ItemStack ingredient)
	{
		for (IPressureFurnaceRecipe cu : PressureRecipeList.getInstance().getBlankRecipeList())
		{
			if(InventoryUtil.isItemEqual(ingredient, cu.getInput()) || InventoryUtil.isItemEqual(ingredient, cu.getSecondInput()) || InventoryUtil.isItemEqual(ingredient, cu.getCombiner()))
			{
				arecipes.add(new ChancedIOPressureRecipe(cu));
			}
		}
		if(PressureFurnace.isValidFuel(ingredient))
		{
			arecipes.add(new ChancedIOPressureRecipe(ingredient));
		}
	}
	
	@Override
	public void loadCraftingRecipes(ItemStack result)
	{
		
		for (IPressureFurnaceRecipe cu : PressureRecipeList.getInstance().getBlankRecipeList())
		{
			if (NEIServerUtils.areStacksSameTypeCrafting(result, cu.getOutput()))
			{
				arecipes.add(new ChancedIOPressureRecipe(cu));
			}
		}
	}
	
	@Override
	public void loadCraftingRecipes(String outputId, Object... results)
	{
		if (outputId.equals(getRecipeID()) && results.length == 1)
		{
			if(((Boolean)results[0]))
			{
				for (IPressureFurnaceRecipe cu : PressureRecipeList.getInstance().getBlankRecipeList())
				{
					arecipes.add(new ChancedIOPressureRecipe(cu));
				}
			}
			else
			{
				arecipes.add(new ChancedIOPressureRecipe());
			}
			
			
		}
		else
		{
			super.loadCraftingRecipes(outputId, results);
		}
	}
	
	public class ChancedIOPressureRecipe extends TemplateRecipeHandler.CachedRecipe
	{
		ArrayList<IPressureFurnaceRecipe> recipeList = new ArrayList<IPressureFurnaceRecipe>();
		IPressureFurnaceRecipe par1 = null;
		List<PositionedStack> fuels = new ArrayList<PositionedStack>();
		PositionedStack last;
		boolean flag;
		
		public ChancedIOPressureRecipe(IPressureFurnaceRecipe par1)
		{
			recipeList.add(par1);
			flag = true;
			for(Integer ids : PressureFurnace.validFuels)
			{
				if(PressureFurnace.fuelMeta.containsKey(ids))
				{
					for(Integer id : PressureFurnace.fuelMeta.get(ids))
					{
						fuels.add(new PositionedStack(new ItemStack(ids, 1, id), 8, 31));
					}
				}
				else
				{
					fuels.add(new PositionedStack(new ItemStack(ids, 1, 0), 8, 31));
				}
			}
		}
		
		public ChancedIOPressureRecipe(ItemStack fuel)
		{
			flag = false;
			fuels.add(new PositionedStack(fuel, 8, 31));
			recipeList.addAll((Collection<? extends IPressureFurnaceRecipe>)PressureRecipeList.getInstance().getBlankRecipeList().clone());
			onTick();
		}
		
		public ChancedIOPressureRecipe()
		{
			flag = false;
			for(Integer ids : PressureFurnace.validFuels)
			{
				if(PressureFurnace.fuelMeta.containsKey(ids))
				{
					for(Integer id : PressureFurnace.fuelMeta.get(ids))
					{
						fuels.add(new PositionedStack(new ItemStack(ids, 1, id), 8, 31));
					}
				}
				else
				{
					fuels.add(new PositionedStack(new ItemStack(ids, 1, 0), 8, 31));
				}
			}
			recipeList.addAll((Collection<? extends IPressureFurnaceRecipe>)PressureRecipeList.getInstance().getBlankRecipeList().clone());
			onTick();
		}
		
		public void onTick()
		{
			if(flag)
			{
				par1 = recipeList.get(0);
			}
			else
			{
				if(par1 == null)
				{
					par1 = recipeList.remove(0);
					recipeList.add(par1);
				}
				else if(NeiPressureFurnace.this.cycleticks % 20 == 0)
				{
					par1 = recipeList.remove(0);
					recipeList.add(par1);
					NeiPressureFurnace.this.cycleticks++;
				}
			}
		}
		

		@Override
		public PositionedStack getOtherStack()
		{
			if(last == null)
			{
				last = fuels.get(CodeProxy.getRandom().nextInt(fuels.size()));
			}
			else if(NeiPressureFurnace.this.cycleticks % 20 == 0)
			{
				last = fuels.remove(0);
				fuels.add(last);
			}
			return last;
		}

		public boolean flag()
		{
			onTick();
			return flag;
		}


		public boolean isSpecialRecipe()
		{
			return par1.getCombiner() != null;
		}
		
		@Override
		public PositionedStack getResult()
		{
			if(par1 == null)
			{
				onTick();
			}
			return new PositionedStack(par1.getOutput(), 145, 26, false);
		}
		
		@Override
		public List<PositionedStack> getIngredients()
		{
			ArrayList<PositionedStack> list = new ArrayList<PositionedStack>();
			if(par1 == null)
			{
				onTick();
			}
			if(par1 == null)
			{
				return list;
			}
			if (par1.getInput() != null)
			{
				list.add(new PositionedStack(par1.getInput(), 53, 15, false));
			}
			if (par1.getSecondInput() != null)
			{
				list.add(new PositionedStack(par1.getSecondInput(), 53, 36, false));
			}
			if (par1.getCombiner() != null)
			{
				list.add(new PositionedStack(par1.getCombiner(), 96, -2, false));
			}
			return list;
		}
		
		public boolean useCombiner()
		{
			return par1.usesCombiner();
		}
		
	}
	
}
