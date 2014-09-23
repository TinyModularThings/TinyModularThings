package speiger.src.compactWindmills.common.core;

import ic2.api.item.Items;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import speiger.src.compactWindmills.CompactWindmills;
import speiger.src.compactWindmills.common.items.IceRotor;
import speiger.src.compactWindmills.common.items.ItemAdvancedRotor;
import speiger.src.compactWindmills.common.items.ItemRotor;
import speiger.src.compactWindmills.common.items.ItemRotor.BasicRotorType;
import speiger.src.compactWindmills.common.utils.WindmillType;
import speiger.src.spmodapi.common.util.proxy.PathProxy;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public class CompactWindmillsCore
{
	public void onClientLoad(CompactWindmills par0)
	{
		
	}
	
	public void onServerLoad(CompactWindmills par0)
	{
		PathProxy.addRecipe(new ItemStack(par0.windmill, 1, WindmillType.ELV.ordinal()), " W ", "WTW", " W ", 'W', Items.getItem("windMill"), 'T', Items.getItem("lvTransformer"));
		PathProxy.addRecipe(new ItemStack(par0.windmill, 1, WindmillType.LV.ordinal()), " W ", "WTW", " W ", 'W', new ItemStack(par0.windmill, 1, 0), 'T', Items.getItem("transformerUpgrade"));
		PathProxy.addRecipe(new ItemStack(par0.windmill, 1, WindmillType.MV.ordinal()), " W ", "WTW", " W ", 'W', new ItemStack(par0.windmill, 1, 1), 'T', Items.getItem("transformerUpgrade"));
		PathProxy.addRecipe(new ItemStack(par0.windmill, 1, WindmillType.HV.ordinal()), " W ", "WTW", " W ", 'W', new ItemStack(par0.windmill, 1, 2), 'T', Items.getItem("transformerUpgrade"));
		PathProxy.addRecipe(new ItemStack(par0.windmill, 1, WindmillType.EV.ordinal()), " W ", "WTW", " W ", 'W', new ItemStack(par0.windmill, 1, 3), 'T', Items.getItem("transformerUpgrade"));
		if (!par0.oldIC2)
		{
			PathProxy.addRecipe(ItemRotor.createRotor(BasicRotorType.CarbonRotor), "CCC", "CMC", "CCC", 'C', Items.getItem("carbonPlate"), 'M', Items.getItem("machine"));
			PathProxy.addRecipe(ItemRotor.createRotor(BasicRotorType.AlloyRotor), "AAA", "AMA", "AAA", 'A', Items.getItem("advancedAlloy"), 'M', Items.getItem("machine"));
			PathProxy.addRecipe(new ShapedOreRecipe(ItemRotor.createRotor(BasicRotorType.WoodenRotor), " S ", "SIS", " S ", 'S', "stickWood", 'I', "plateIron"));
			PathProxy.addRecipe(ItemRotor.createRotor(BasicRotorType.IridiumRotor), " I ", "IRI", " I ", 'I', Items.getItem("iridiumPlate"), 'R', new ItemStack(par0.rotor, 1, BasicRotorType.CarbonRotor.ordinal()));
			PathProxy.addRecipe(ItemRotor.createRotor(BasicRotorType.WoolRotor), "SWS", "WRW", "SWS", 'S', new ItemStack(Item.silk), 'W', new ItemStack(Block.cloth, 1, OreDictionary.WILDCARD_VALUE), 'R', new ItemStack(par0.rotor, 1, BasicRotorType.WoodenRotor.ordinal()));
			PathProxy.addRecipe(ItemRotor.createRotor(BasicRotorType.IronRotor), "XXX", "XYX", "XXX", 'Y', Items.getItem("machine"), 'X', Items.getItem("denseplateiron"));
			if (Loader.isModLoaded("Railcraft"))
			{
				ItemStack rotor = GameRegistry.findItemStack("Railcraft", "part.turbine.blade", 1);
				if (rotor != null)
				{
					PathProxy.addRecipe(ItemRotor.createRotor(BasicRotorType.IronRotor), "XXX", "XYX", "XXX", 'X', rotor, 'Y', Items.getItem("machine"));
					rotor.stackSize = 8;
					PathProxy.addAssemblyRecipe(ItemAdvancedRotor.createRotor(BasicRotorType.IronRotor), 25000, Items.getItem("machine"), rotor);
				}
			}
			
			PathProxy.addAssemblyRecipe(ItemAdvancedRotor.createRotor(BasicRotorType.WoodenRotor), 25000, Items.getItem("plateiron"), new ItemStack(Item.stick, 4));
			PathProxy.addAssemblyRecipe(ItemAdvancedRotor.createRotor(BasicRotorType.WoolRotor), 25000, new ItemStack(Item.silk, 4), new ItemStack(Block.cloth, 4), new ItemStack(par0.advRotor, 1, BasicRotorType.WoodenRotor.ordinal()));
			PathProxy.addAssemblyRecipe(ItemAdvancedRotor.createRotor(BasicRotorType.CarbonRotor), 25000, Items.getItem("machine"), PathProxy.getIC2Item("carbonPlate", 8));
			PathProxy.addAssemblyRecipe(ItemAdvancedRotor.createRotor(BasicRotorType.AlloyRotor), 25000, Items.getItem("machine"), PathProxy.getIC2Item("advancedAlloy", 8));
			PathProxy.addAssemblyRecipe(ItemAdvancedRotor.createRotor(BasicRotorType.IronRotor), 25000, Items.getItem("machine"), PathProxy.getIC2Item("denseplateiron", 8));
			PathProxy.addAssemblyRecipe(ItemAdvancedRotor.createRotor(BasicRotorType.IridiumRotor), 25000, new ItemStack(par0.advRotor, 1, BasicRotorType.CarbonRotor.ordinal()), PathProxy.getIC2Item("iridiumPlate", 4));
			PathProxy.addRecipe(IceRotor.getRotor(CompactWindmills.iceRotor.itemID), new Object[]{"XXX", "XCX", "XXX", 'C', Items.getItem("reactorVentDiamond"), 'X', Block.ice});
			
		}
	}
}
