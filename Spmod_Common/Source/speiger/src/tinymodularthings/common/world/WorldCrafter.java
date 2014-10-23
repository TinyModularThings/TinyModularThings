package speiger.src.tinymodularthings.common.world;

import java.util.HashMap;

import net.minecraftforge.event.ForgeSubscribe;
import speiger.src.api.blocks.BlockPosition;
import speiger.src.api.blocks.BlockStack;
import speiger.src.api.event.BlockPlacedEvent;
import speiger.src.api.hopper.CopiedIInventory;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.world.IWorldCraftingRecipe;
import speiger.src.api.world.WorldCraftingManager;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.GameRegistry;


public class WorldCrafter
{
	public HashMap<String, PlayerWorldCraftingData> data = new HashMap<String, PlayerWorldCraftingData>();
	
	public WorldCrafter()
	{
		
	}
	
	@ForgeSubscribe
	public void onBlockPlaced(BlockPlacedEvent evt)
	{
		PlayerWorldCraftingData crafter = data.get(evt.placer.username);
		if(crafter == null)
		{
			IWorldCraftingRecipe recipe = WorldCraftingManager.getRecipe(evt.blockData.getAsBlockStack());
			if(recipe != null && recipe.canCraftBlockStructure(evt.blockData, evt.placer))
			{
				data.put(evt.placer.username, new PlayerWorldCraftingData(evt.blockData, recipe));
				FMLLog.getLogger().info("Player: "+evt.placer.username+" Started this CraftingRecipe: "+recipe.getClass());
			}
		}
		else
		{
			if(crafter.par3.getFinsiherBlock().match(evt.blockData.getAsBlockStack()))
			{
				if(crafter.par3.isFinishingBlockAtRightPosition(crafter.par2, evt.blockData))
				{
					if(crafter.par3.isStructureFinish(crafter.par2))
					{
						if(crafter.par3.canFinishMultiStructureCrafting(evt.placer))
						{
							BlockStack before = crafter.par2.getAsBlockStack();
							crafter.par3.onFinsihedCrafting(crafter.par2, evt.placer);
							if(!before.match(crafter.par2.getAsBlockStack()))
							{
								GameRegistry.onItemCrafted(evt.placer, crafter.par2.getAsBlockStack().asItemStack(), new CopiedIInventory());
							}
							data.remove(evt.placer.username);
						}
						else
						{
							data.remove(evt.placer.username);
						}
					}
					else
					{
						evt.placer.sendChatToPlayer(LanguageRegister.createChatMessage("MultiStructure is not Finish"));
					}
				}
				else
				{
					data.remove(evt.placer.username);
				}
			}
		}
	}
	
	
	public static class PlayerWorldCraftingData
	{
		BlockPosition par2;
		IWorldCraftingRecipe par3;
		
		public PlayerWorldCraftingData(BlockPosition pos, IWorldCraftingRecipe recipe)
		{
			par2 = pos;
			par3 = recipe;
		}

		
		
		
	}
}
