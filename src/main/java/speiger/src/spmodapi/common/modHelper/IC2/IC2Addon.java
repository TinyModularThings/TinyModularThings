package speiger.src.spmodapi.common.modHelper.IC2;

import net.minecraft.item.ItemStack;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.util.proxy.PathProxy;
import cpw.mods.fml.common.FMLLog;

public class IC2Addon
{
	public static void LoadIC2()
	{
		short ID = Crops.instance.registerCrop(new HempCrop());
		if (ID != -1)
		{
			Crops.instance.registerBaseSeed(new ItemStack(APIItems.hempSeed, 2, PathProxy.getRecipeBlankValue()), ID, 1, 1, 1, 1);
		}
		
		try
		{
			IC2Crops crops = (IC2Crops)Crops.instance;
			crops.registerBaseSeed(new ItemStack(APIBlocks.blueFlower, 4, 0), crops.cropBlueFlower.getId(), 4, 1, 1, 1);
		}
		catch (Exception e)
		{
			FMLLog.getLogger().info("Crash");
		}
	
	}
}
