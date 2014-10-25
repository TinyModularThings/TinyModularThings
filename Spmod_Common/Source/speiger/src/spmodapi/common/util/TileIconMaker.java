package speiger.src.spmodapi.common.util;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.Icon;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.TextureEngine.BlockData;

public class TileIconMaker
{
	
	private HashMap<Block, ArrayList<AdvTile>> allTiles = new HashMap<Block, ArrayList<AdvTile>>();
	private HashMap<BlockData, Class<? extends AdvTile>> classes = new HashMap<BlockData, Class<? extends AdvTile>>();
	
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
	
	public void addTileEntity(Block par1, int meta, AdvTile tile)
	{
		if (allTiles.get(par1) == null)
		{
			allTiles.put(par1, new ArrayList<AdvTile>());
		}
		tile.onPlaced(4);
		allTiles.get(par1).add(tile);
		
		classes.put(new BlockData(par1, meta), tile.getClass());
	}
	
	public AdvTile getTileEntityFromClass(Class<? extends AdvTile> tile)
	{
		return this.getTileEntityFromClass(null, tile);
	}
	
	public AdvTile getTileEntityFromClass(Block block, Class<? extends AdvTile> tile)
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
				return list;
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
