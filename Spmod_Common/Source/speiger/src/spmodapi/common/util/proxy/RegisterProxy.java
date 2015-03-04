package speiger.src.spmodapi.common.util.proxy;

import static speiger.src.spmodapi.common.lib.SpmodAPILib.ModID;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.interfaces.IAdvTile;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.TileIconMaker;
import cpw.mods.fml.common.registry.GameRegistry;

public class RegisterProxy
{
	public static void RegisterItem(Item par1)
	{
		RegisterItem(ModID, par1);
	}
	
	public static void RegisterItem(String modID, Item par1)
	{
		GameRegistry.registerItem(par1, par1.getItemDisplayName(new ItemStack(par1)), modID);
		
	}
	
	public static void RegisterItem(String modID, String name, Item par1)
	{
		GameRegistry.registerItem(par1, name, modID);
	}
	
	public static void RegisterBlock(Block block, Class<? extends ItemBlock> itemclass, String name)
	{
		GameRegistry.registerBlock(block, itemclass, name);
	}
	
	public static void RegisterTile(Block block, int meta, Class<? extends TileEntity> claz, String name)
	{
		TileEntity.addMapping(claz, name);
		TileEntity tile = null;
		try
		{
			tile = claz.newInstance();
		}
		catch (Exception e)
		{
			SpmodAPI.log.print(String.format("%s%n%s%n%s", "Failing by Reinstancing TileEntity.", "Crash can Happen.", "Reason why it could not Reinstance TileEntity: " + e.getLocalizedMessage()));
			return;
		}
		if (block != null && tile != null && tile instanceof IAdvTile)
		{
			AdvTile regist = (AdvTile) tile;
			TileIconMaker.getIconMaker().addTileEntity(block, meta, regist);
		}
	}
	
	public static void RegisterTile(Block block, Class<? extends TileEntity> claz, String name)
	{
		RegisterTile(block, 0, claz, name);
	}
	
	public static void RegisterTile(Class<? extends TileEntity> claz, String string)
	{
		RegisterTile(null, 0, claz, string);
	}
	
}
