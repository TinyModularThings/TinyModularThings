package speiger.src.tinymodularthings.common.plugins.Waila.core;

import mcp.mobius.waila.api.impl.ModuleRegistrar;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.plugins.Waila.logics.CraftingBlockLogic;
import speiger.src.tinymodularthings.common.plugins.Waila.logics.MachineBlockLogic;

public class WailaRegistry
{
	public static void init()
	{
		ModuleRegistrar.instance().registerBodyProvider(new CraftingBlockLogic(), TinyBlocks.craftingBlock.blockID);
		ModuleRegistrar.instance().registerBodyProvider(new MachineBlockLogic(), TinyBlocks.machine.blockID);

	}
}
