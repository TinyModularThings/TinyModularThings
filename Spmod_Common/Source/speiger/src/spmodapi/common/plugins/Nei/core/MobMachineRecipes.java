package speiger.src.spmodapi.common.plugins.Nei.core;

import static codechicken.core.gui.GuiDraw.changeTexture;
import static codechicken.core.gui.GuiDraw.drawTexturedModalRect;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import speiger.src.api.common.data.utils.ResultData;
import speiger.src.api.common.utils.MathUtils;
import speiger.src.spmodapi.common.blocks.utils.MobMachine;
import speiger.src.spmodapi.common.blocks.utils.MobMachine.DropType;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.plugins.Nei.gui.GuiMobMachine;
import speiger.src.spmodapi.common.util.TextureEngine;
import codechicken.core.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class MobMachineRecipes extends TemplateRecipeHandler
{
	TextureEngine engine = TextureEngine.getTextures();

	@Override
	public String getRecipeName()
	{
		return "Mob Machine";
	}

	@Override
	public String getGuiTexture()
	{
		return engine.getTexture("BasicFrame").toString();
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass()
	{
		return GuiMobMachine.class;
	}

	@Override
	public int recipiesPerPage()
	{
		return 1;
	}
	
	public String getRecipeID()
	{
		return "mob.machine";
	}
	
	@Override
	public void loadTransferRects()
	{
		transferRects.add(new RecipeTransferRect(new Rectangle(75, 20, 20, 20), getRecipeID(), new Object[0]));
	}

	@Override
	public void drawBackground(int recipe)
	{
		MobMachineRecipe cu = (MobMachineRecipe)this.arecipes.get(recipe);
		cu.onTick();
        GL11.glColor4f(1, 1, 1, 1);
        changeTexture(getGuiTexture());
        drawTexturedModalRect(0, 0, 5, 11, 166, 65);
        changeTexture(engine.getTexture("Objects").toString());
        
        int[] key = engine.getGuiPos("ProgBarH");
        drawTexturedModalRect(77, 30, key[0], key[1], 23, 16);
        
        int[] data = engine.getGuiPos("Slot");
        int par1 = data[0];
        int par2 = data[1];
        drawTexturedModalRect(105, 13, par1, par2, 18, 18);
        drawTexturedModalRect(123, 13, par1, par2, 18, 18);
        drawTexturedModalRect(141, 13, par1, par2, 18, 18);
        drawTexturedModalRect(105, 31, par1, par2, 18, 18);
        drawTexturedModalRect(123, 31, par1, par2, 18, 18);
        drawTexturedModalRect(141, 31, par1, par2, 18, 18);
        drawTexturedModalRect(105, 49, par1, par2, 18, 18);
        drawTexturedModalRect(123, 49, par1, par2, 18, 18);
        drawTexturedModalRect(141, 49, par1, par2, 18, 18);
        if(cu.needMoreSpace())
        {
          drawTexturedModalRect(105, 67, par1, par2, 18, 18);
          drawTexturedModalRect(123, 67, par1, par2, 18, 18);
          drawTexturedModalRect(141, 67, par1, par2, 18, 18);
          drawTexturedModalRect(105, 85, par1, par2, 18, 18);
          drawTexturedModalRect(123, 85, par1, par2, 18, 18);
          drawTexturedModalRect(141, 85, par1, par2, 18, 18);
          drawTexturedModalRect(105, 103, par1, par2, 18, 18);
          drawTexturedModalRect(123, 103, par1, par2, 18, 18);
          drawTexturedModalRect(141, 103, par1, par2, 18, 18);
        }
        drawTexturedModalRect(6, 49, par1, par2, 18, 18);
        drawTexturedModalRect(30, 49, par1, par2, 18, 18);
        drawTexturedModalRect(54, 49, par1, par2, 18, 18);
        int[] progBar = engine.getGuiPos("ProgBarV");
        drawTexturedModalRect(13, 4, progBar[0], progBar[1], 4, 43);
        drawTexturedModalRect(62, 4, progBar[0], progBar[1], 4, 43);
	}


	
	@Override
	public void loadCraftingRecipes(ItemStack result)
	{
		int[] mobMachines = MobMachine.getMobMachineResultItem(result);
		if(mobMachines != null && mobMachines.length > 0)
		{
			for(int i : mobMachines)
			{
				this.arecipes.add(new MobMachineRecipe(i));
			}
		}
		if(result.itemID == APIItems.mobMachineHelper.itemID)
		{
			arecipes.add(new MobMachineRecipe(result.getItemDamage()));
		}
	}
	
	

	@Override
	public void loadCraftingRecipes(String outputId, Object... results)
	{
		if(outputId.equals(this.getRecipeID()))
		{
			for(int i = 0;i<23;i++)
			{
				this.arecipes.add(new MobMachineRecipe(i));
			}
		}
		else
		{
			super.loadCraftingRecipes(outputId, results);
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient)
	{
		int[] mobMachines = MobMachine.getMobMachineFromFoodItem(ingredient);
		if(mobMachines != null && mobMachines.length > 0)
		{
			for(int i : mobMachines)
			{
				this.arecipes.add(new MobMachineRecipe(i));
			}
		}
		if(ingredient.itemID == APIItems.mobMachineHelper.itemID)
		{
			arecipes.add(new MobMachineRecipe(ingredient.getItemDamage()));
		}
	}

	@Override
	public void drawExtras(int recipe)
	{
		MobMachineRecipe cu = (MobMachineRecipe)this.arecipes.get(recipe);
		changeTexture(engine.getTexture("Objects"));
        if(cu.type == 0)
        {
            GuiDraw.fontRenderer.drawString("Exp Call Every: "+MathUtils.getTicksInTimeShort(12000)+" Min", 0, 70, 4210752);
            GuiDraw.fontRenderer.drawString("Common Drop Every: "+MathUtils.getTicksInTimeShort(24000)+" Min", 0, 80, 4210752);
            GuiDraw.fontRenderer.drawString("Rare Drop Every: "+MathUtils.getTicksInTimeShort(12000*5)+" Min", 0, 90, 4210752);
            GuiDraw.fontRenderer.drawString("Legend Drop Every: "+MathUtils.getTicksInTimeShort(12000*7)+" Min", 0, 100, 4210752);
            GuiDraw.fontRenderer.drawString("Drop Chance: Common: "+DropType.Common.getChance()+"% ", 0, 110, 4210752);
            GuiDraw.fontRenderer.drawString("Rare: "+DropType.Rare.getChance()+"% Legendary: "+DropType.Legendary.getChance()+"%", 0, 120, 4210752);
        }
        else
        {
        	int ticks = this.cycleticks % 43;
        	int[] tex = engine.getGuiPos("ProgBarVOverlay");
            drawTexturedModalRect(13, 4 + 43 - ticks, tex[0], tex[1] + 43 - ticks, 4, ticks);
            
            ticks = ticks * 3 % 43;
            drawTexturedModalRect(62, 4 + 43 - ticks, tex[0], tex[1] + 43 - ticks, 4, ticks);
        
            try
			{
                GuiDraw.fontRenderer.drawString("Food Time: "+MathUtils.getTicksInTimeShort(MobMachine.foodList.get(cu.type).get(new ResultData(cu.lastFood.item))*20), 0, 70, 4210752);
                String exp = MobMachine.needExp.get(cu.type) ? "Use Exp: " : "Create Exp: ";
                GuiDraw.fontRenderer.drawString(exp+MobMachine.neededExp.get(cu.type), 0, 80, 4210752);

			}
			catch(Exception e)
			{
			}
        }

        
	}




	public class MobMachineRecipe extends CachedRecipe
	{
		int type;
		
		public PositionedStack lastFood;
		
		public PositionedStack[] lastOutput;
		
		ArrayList<ItemStack> food = new ArrayList<ItemStack>();
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		int foodDelay = 5;
		int dropDelay = 5;
		
		public MobMachineRecipe(int type)
		{
			this.type = type;
			food.addAll(MobMachine.getAllFoodItems(type));
			drops.addAll(MobMachine.getResults(type));
			if(food.size() > 0)
			{
				lastFood = new PositionedStack(food.get(0), 7, 50);
			}
			onTick();
		}
		
		public boolean needMoreSpace()
		{
			return drops.size() > 9;
		}
		
		

		@Override
		public PositionedStack getIngredient()
		{
			return lastFood;
		}
		
		

		@Override
		public List<PositionedStack> getOtherStacks()
		{
			ArrayList<PositionedStack> list = new ArrayList<PositionedStack>();
			if(lastOutput != null)
			{
				for(PositionedStack par2 : lastOutput)
				{
					list.add(par2);
				}
			}
			return list;
		}

		@Override
		public PositionedStack getResult()
		{
			return new PositionedStack(new ItemStack(APIItems.mobMachineHelper, 1, type), 55, 50);
		}
		
		
		
		public void onTick()
		{
			
			if(MobMachineRecipes.this.cycleticks % 43 == 0)
			{
				if(food.size() > 1)
				{
					food.add(food.remove(0));
					lastFood = new PositionedStack(food.get(0), 7, 50);
					MobMachineRecipes.this.cycleticks++;
				}
				
				
			}
			if(lastOutput == null)
			{
				int size = Math.min(18, drops.size());
				lastOutput = new PositionedStack[size];
				for(int i = 0;i<size;i++)
				{
					int[] slot = MobMachineRecipes.slots[i];
					lastOutput[i] = new PositionedStack(drops.get(i), slot[0], slot[1]);
				}
			}
			else if(requireCycle() && MobMachineRecipes.this.cycleticks % 20 == 0)
			{
				this.drops.add(this.drops.remove(0));
				int size = Math.min(18, drops.size());
				lastOutput = new PositionedStack[size];
				for(int i = 0;i<size;i++)
				{
					int[] slot = MobMachineRecipes.slots[i];
					lastOutput[i] = new PositionedStack(drops.get(i), slot[0], slot[1]);
				}
				MobMachineRecipes.this.cycleticks++;
			}
		}
		
		public boolean requireCycle()
		{
			return this.drops.size() > 18;
		}
	}
	
	public static int[][] slots = new int[][]{
		{106, 14},
		{124, 14},
		{142, 14},
		{106, 32},
		{124, 32},
		{142, 32},
		{106, 50},
		{124, 50},
		{142, 50},
		{106, 68},
		{124, 68},
		{142, 68},
		{106, 86},
		{124, 86},
		{142, 86},
		{106, 104},
		{124, 104},
		{142, 104},
	};
	
	
}
