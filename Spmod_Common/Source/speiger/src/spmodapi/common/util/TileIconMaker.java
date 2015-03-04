package speiger.src.spmodapi.common.util;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import speiger.src.api.common.data.nbt.DataStorage;
import speiger.src.api.common.data.nbt.INBTReciver;
import speiger.src.api.common.data.packets.IPacketReciver;
import speiger.src.api.common.data.packets.SpmodPacketHelper;
import speiger.src.api.common.data.utils.BlockData;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.common.interfaces.IAdvTile;
import speiger.src.spmodapi.common.tile.AdvTile;

public class TileIconMaker
{
	
	private HashMap<Block, ArrayList<IAdvTile>> allTiles = new HashMap<Block, ArrayList<IAdvTile>>();
	private HashMap<BlockData, Class<? extends IAdvTile>> classes = new HashMap<BlockData, Class<? extends IAdvTile>>();
	private HashMap<Class<? extends IAdvTile>, BlockData> ids = new HashMap<Class<? extends IAdvTile>, BlockData>();
	private boolean init = false;
	private static TileIconMaker instance = new TileIconMaker();
	
	public static TileIconMaker getIconMaker()
	{
		return instance;
	}
	
	public static void registerIcon(Block block, TextureEngine par1)
	{
		ArrayList<IAdvTile> tiles = instance.allTiles.get(block);
		for (IAdvTile tile : tiles)
		{
			tile.registerIcon(par1, block);
		}
	}
	
	public IAdvTile getTileEntityFormBlockAndMetadata(Block par1, int meta)
	{
		Class<? extends IAdvTile> tile = classes.get(new BlockData(par1, meta));
		if(tile != null)
		{
			return getTileEntityFromClass(par1, tile);
		}
		return null;
	}
	
	public IAdvTile getTileEntityFormBlockAndMetadata(BlockStack par1)
	{
		Class<? extends IAdvTile> tile = classes.get(new BlockData(par1));
		if(tile != null)
		{
			return getTileEntityFromClass(par1.getBlock(), tile);
		}
		return null;
	}
	
	public void addTileEntity(Block par1, int meta, IAdvTile tile)
	{
		if (allTiles.get(par1) == null)
		{
			allTiles.put(par1, new ArrayList<IAdvTile>());
		}
		allTiles.get(par1).add(tile);
		tile.onIconMakerLoading();
		extraIniting(tile);
		classes.put(new BlockData(par1, meta), tile.getClass());
		ids.put(tile.getClass(), new BlockData(par1, meta));
	}
	
	private void extraIniting(IAdvTile par1)
	{
		if(par1.requireForgeRegistration())
		{
			ForgeRegister.regist(par1);
		}
		if(par1 instanceof IPacketReciver)
		{
			SpmodPacketHelper.getHelper().registerPacketReciver((IPacketReciver)par1);
		}
		if(par1 instanceof INBTReciver)
		{
			DataStorage.registerNBTReciver((INBTReciver)par1);
		}
	}
	
	public <T> T getTileEntityFromClass(Class<T> tile)
	{
		return this.getTileEntityFromClass(null, tile);
	}
	
	public <T> T getTileEntityFromClass(Block block, Class<T> tile)
	{
		ArrayList<IAdvTile> tiles = new ArrayList<IAdvTile>();
		if (block == null)
		{
			for (ArrayList<IAdvTile> cuTile : allTiles.values())
			{
				tiles.addAll(cuTile);
			}
		}
		else
		{
			tiles.addAll(allTiles.get(block));
		}
		
		for (IAdvTile list : tiles)
		{
			if (list.getClass().getSimpleName().equalsIgnoreCase(tile.getSimpleName()))
			{
				return (T)list;
			}
		}
		
		return null;
	}
	
	public ItemStack getStackFromTile(IAdvTile tile)
	{
		BlockData result = ids.get(tile.getClass());
		if(result != null)
		{
			return result.getResult();
		}
		return null;
	}
	
	/**
	 * WhyEver this Should be needed
	 */
	public HashMap<Block, ArrayList<IAdvTile>> getAllTiles()
	{
		return allTiles;
	}
	
	
	public static Icon getIconFromTile(Block block, Class<? extends IAdvTile> clz, int side)
	{
		return TextureEngine.getTextures().getIconSafe(instance.getIconFromTileEntity(block, clz, side));
	}
	
	public Icon getIconFromTileEntity(Block block, Class<? extends IAdvTile> clz, int side)
	{
		ArrayList<IAdvTile> tiles = allTiles.get(block);
		try
		{
			for (IAdvTile tile : tiles)
			{
				if (match(clz, tile))
				{
					return TextureEngine.getTextures().getIconSafe(tile.getIconFromSideAndMetadata(side, tile.getRenderPass()));
				}
			}
		}
		catch (Exception e)
		{
		}
		return TextureEngine.getTextures().getIconSafe();
	}
	
	public Icon getIconFromTile(Block block, int meta, int side)
	{
		return TextureEngine.getTextures().getIconSafe(getIconFromTileEntity(block, classes.get(new BlockData(block, meta)), side));
	}
	
	public boolean match(Class<? extends IAdvTile> tiles, IAdvTile tile)
	{
		if (tiles.getSimpleName().equalsIgnoreCase(tile.getClass().getSimpleName()))
		{
			return true;
		}
		return false;
	}
	
	public Icon getIconSafe(Icon par1Icon)
	{
		if (par1Icon == null)
		{
			par1Icon = ((TextureMap) Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.locationBlocksTexture)).getAtlasSprite("missingno");
		}
		
		return (Icon) par1Icon;
	}

}
