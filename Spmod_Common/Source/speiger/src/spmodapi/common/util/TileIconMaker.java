package speiger.src.spmodapi.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.Icon;
import speiger.src.api.common.data.utils.BlockData;
import speiger.src.api.common.registry.helpers.SpmodMod;
import speiger.src.api.common.registry.helpers.Ticks;
import speiger.src.api.common.registry.helpers.Ticks.ITickReader;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.tile.AdvTile;
import cpw.mods.fml.relauncher.Side;

public class TileIconMaker
{
	
	private HashMap<Block, ArrayList<AdvTile>> allTiles = new HashMap<Block, ArrayList<AdvTile>>();
	private HashMap<BlockData, Class<? extends AdvTile>> classes = new HashMap<BlockData, Class<? extends AdvTile>>();
	private boolean init = false;
	private static TileIconMaker instance = new TileIconMaker();
	
	public static TileIconMaker getIconMaker()
	{
		return instance;
	}
	
	public static void registerIcon(Block block, TextureEngine par1)
	{
		ArrayList<AdvTile> tiles = instance.allTiles.get(block);
		for (AdvTile tile : tiles)
		{
			tile.registerIcon(par1);
		}
	}
	
	public AdvTile getTileEntityFormBlockAndMetadata(Block par1, int meta)
	{
		Class<? extends AdvTile> tile = classes.get(new BlockData(par1, meta));
		if(tile != null)
		{
			return getTileEntityFromClass(par1, tile);
		}
		return null;
	}
	
	public void addTileEntity(Block par1, int meta, AdvTile tile)
	{
		if (allTiles.get(par1) == null)
		{
			allTiles.put(par1, new ArrayList<AdvTile>());
		}
		allTiles.get(par1).add(tile);
		tile.onIconMakerLoading();
		classes.put(new BlockData(par1, meta), tile.getClass());
	}
	
	public <T> T getTileEntityFromClass(Class<T> tile)
	{
		return this.getTileEntityFromClass(null, tile);
	}
	
	public <T> T getTileEntityFromClass(Block block, Class<T> tile)
	{
		ArrayList<AdvTile> tiles = new ArrayList<AdvTile>();
		if (block == null)
		{
			for (ArrayList<AdvTile> cuTile : allTiles.values())
			{
				tiles.addAll(cuTile);
			}
		}
		else
		{
			tiles.addAll(allTiles.get(block));
		}
		
		for (AdvTile list : tiles)
		{
			if (list.getClass().getSimpleName().equalsIgnoreCase(tile.getSimpleName()))
			{
				return (T)list;
			}
		}
		
		return null;
	}
	
	/**
	 * WhyEver this Should be needed
	 */
	public HashMap<Block, ArrayList<AdvTile>> getAllTiles()
	{
		return allTiles;
	}
	
	
	public static Icon getIconFromTile(Block block, Class<? extends AdvTile> clz, int side)
	{
		return TextureEngine.getTextures().getIconSafe(instance.getIconFromTileEntity(block, clz, side));
	}
	
	public Icon getIconFromTileEntity(Block block, Class<? extends AdvTile> clz, int side)
	{
		ArrayList<AdvTile> tiles = allTiles.get(block);
		try
		{
			for (AdvTile tile : tiles)
			{
				if (match(clz, tile))
				{
					return TextureEngine.getTextures().getIconSafe(tile.getIconFromSideAndMetadata(side, 0));
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
	
	public boolean match(Class<? extends AdvTile> tiles, AdvTile tile)
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
