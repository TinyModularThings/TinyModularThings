package speiger.src.tinymodularthings.common.plugins.Waila.logics;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.oredict.OreDictionary;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.tinymodularthings.common.blocks.crafting.OreCrafter;
import speiger.src.tinymodularthings.common.blocks.crafting.Uncrafter;

public class CraftingBlockLogic implements IWailaDataProvider
{

	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return null;
	}

	@Override
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return currenttip;
	}

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		TileEntity tile = accessor.getTileEntity();
		if(tile != null && tile instanceof AdvTile)
		{
			int meta = accessor.getMetadata();
			if(meta == 1)
			{
				OreCrafter adv = (OreCrafter)tile;
				ItemStack stack = accessor.getPlayer().getCurrentEquippedItem();
				if(stack != null)
				{
					String name = OreDictionary.getOreName(OreDictionary.getOreID(stack));
					if(!name.equals("Unknown"))
					{
						currenttip.add("Equippet Item OreDictionary Name: "+name);
					}
					else
					{
						currenttip.add("No OreDictionary Name Found");
					}
				}
			}
			else if(meta == 3)
			{
				Uncrafter adv = (Uncrafter)tile;
				if(adv.getCurrentItem() != null)
				{
					currenttip.add("Currently Working Item: "+adv.getCurrentItem().getDisplayName());
					currenttip.add("Progress: "+adv.getProgress()+"%");
				}
			}
		}
		return currenttip;
	}

	@Override
	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return currenttip;
	}
	
}
