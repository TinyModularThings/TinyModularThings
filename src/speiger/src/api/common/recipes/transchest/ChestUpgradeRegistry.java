package speiger.src.api.common.recipes.transchest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import speiger.src.api.common.utils.blocks.BlockStack;

public class ChestUpgradeRegistry
{
	public static ChestUpgradeRegistry instance = new ChestUpgradeRegistry();
	
	List<IChestUpgrade> allUpgrades = new ArrayList<IChestUpgrade>();
	Map<Block, IChestUpgrade> sortedUpgrades = new HashMap<Block, IChestUpgrade>();
	List<BlockStack> validUpgrades = new ArrayList<BlockStack>();
	
	public void addChestUpgrade(IChestUpgrade upgrades)
	{
		if(upgrades == null)
		{
			return;
		}
		allUpgrades.add(upgrades);
		sortedUpgrades.put(upgrades.getBlock(), upgrades);
		validateOptions();
	}
	
	private void validateOptions()
	{
		validUpgrades.clear();
		for(IChestUpgrade upgrade : allUpgrades)
		{
			Block block = upgrade.getBlock();
			int[] metaArray = upgrade.getMetas();
			if(block == null || block == Blocks.air || metaArray == null || metaArray.length <= 0)
			{
				continue;
			}
			for(int i = 0;i<metaArray.length;i++)
			{
				int slotCount = upgrade.getSlotCountForMeta(metaArray[i]);
				if(slotCount <= 0)
				{
					continue;
				}
				validUpgrades.add(new BlockStack(block, metaArray[i]));
			}
		}
	}
	
	public List<BlockStack> getValidUpgrades()
	{
		return validUpgrades;
	}
	
	public IChestUpgrade getUpgrade(BlockStack par1)
	{
		return getUpgrade(par1.getBlock());
	}
	
	public IChestUpgrade getUpgrade(Block par1)
	{
		return sortedUpgrades.get(par1);
	}
}
