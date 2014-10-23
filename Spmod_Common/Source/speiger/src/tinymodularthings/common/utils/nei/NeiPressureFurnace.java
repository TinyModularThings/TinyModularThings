package speiger.src.tinymodularthings.common.utils.nei;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mods.railcraft.common.plugins.forge.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

import org.lwjgl.opengl.GL11;

import speiger.src.api.recipe.pressureFurnace.PressureRecipe;
import speiger.src.api.recipe.pressureFurnace.helper.PressureRecipeList;
import speiger.src.api.util.MathUtils;
import speiger.src.spmodapi.common.util.proxy.CodeProxy;
import speiger.src.tinymodularthings.client.gui.machine.PressureFurnaceGui;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;
import speiger.src.tinymodularthings.common.utils.slot.SlotCoal;
import codechicken.core.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.common.Loader;

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
		for (PressureRecipe cu : PressureRecipeList.getInstance().getRecipeList())
		{
			if (cu.combinerEquals(ingredient) || cu.firstInputEquals(ingredient) || cu.secondInputEquals(ingredient))
			{
				arecipes.add(new ChancedIOPressureRecipe(cu));
			}
		}
		if(SlotCoal.isFuel(ingredient))
		{
			arecipes.add(new ChancedIOPressureRecipe(ingredient));
		}
	}
	
	@Override
	public void loadCraftingRecipes(ItemStack result)
	{
		
		for (PressureRecipe cu : PressureRecipeList.getInstance().getRecipeList())
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
				for (PressureRecipe cu : PressureRecipeList.getInstance().getRecipeList())
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
		ArrayList<PressureRecipe> recipeList = new ArrayList<PressureRecipe>();
		PressureRecipe par1 = null;
		List<PositionedStack> fuels = new ArrayList<PositionedStack>();
		PositionedStack last;
		boolean flag;
		
		public ChancedIOPressureRecipe(PressureRecipe par1)
		{
			recipeList.add(par1);
			flag = true;
			fuels.add(new PositionedStack(new ItemStack(Item.coal), 8, 31));
			fuels.add(new PositionedStack(new ItemStack(Item.coal, 1, 1), 8, 31));
			fuels.add(new PositionedStack(new ItemStack(Block.coalBlock), 8, 31));
			if(Loader.isModLoaded("Railcraft"))
			{
				try
				{
					ItemStack stack = ItemRegistry.getItem("railcraft.cube.coke", 1);
					if(stack != null)
					{
						fuels.add(new PositionedStack(stack, 8, 31));
					}
					stack = ItemRegistry.getItem("railcraft.fuel.coke", 1);
					if(stack != null)
					{
						fuels.add(new PositionedStack(stack, 8, 31));
					}
					stack = ItemRegistry.getItem("firestone.refined", 1);
					if(stack != null)
					{
						fuels.add(new PositionedStack(stack, 8, 31));
					}
				}
				catch(Exception e)
				{
					
				}
			}
		}
		
		public ChancedIOPressureRecipe(ItemStack fuel)
		{
			flag = false;
			fuels.add(new PositionedStack(fuel, 8, 31));
			recipeList.addAll((Collection<? extends PressureRecipe>)PressureRecipeList.getInstance().getRecipeList().clone());
			onTick();
		}
		
		public ChancedIOPressureRecipe()
		{
			flag = false;
			fuels.add(new PositionedStack(new ItemStack(Item.coal), 8, 31));
			fuels.add(new PositionedStack(new ItemStack(Item.coal, 1, 1), 8, 31));
			fuels.add(new PositionedStack(new ItemStack(Block.coalBlock), 8, 31));
			if(Loader.isModLoaded("Railcraft"))
			{
				try
				{
					ItemStack stack = ItemRegistry.getItem("railcraft.cube.coke", 1);
					if(stack != null)
					{
						fuels.add(new PositionedStack(stack, 8, 31));
					}
					stack = ItemRegistry.getItem("railcraft.fuel.coke", 1);
					if(stack != null)
					{
						fuels.add(new PositionedStack(stack, 8, 31));
					}
					stack = ItemRegistry.getItem("firestone.refined", 1);
					if(stack != null)
					{
						fuels.add(new PositionedStack(stack, 8, 31));
					}
				}
				catch(Exception e)
				{
					
				}
			}
			recipeList.addAll((Collection<? extends PressureRecipe>)PressureRecipeList.getInstance().getRecipeList().clone());
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
			return par1.hasCombiner();
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
				list.add(new PositionedStack(par1.getCombiner(), 96, -2, false));
			}
			return list;
		}
		
		public boolean useCombiner()
		{
			return par1.useCombiner();
		}
		
	}
	
}
