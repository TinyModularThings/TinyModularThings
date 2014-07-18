package speiger.src.spmodapi.common.util;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;
import speiger.src.spmodapi.client.render.utils.RenderUtilsBlock;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.fluids.hemp.FluidHempResin;
import speiger.src.spmodapi.common.lib.SpmodAPILib;
import speiger.src.spmodapi.common.tile.AdvTile;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileIconMaker
{
	
	private HashMap<Block, ArrayList<AdvTile>> allTiles = new HashMap<Block, ArrayList<AdvTile>>();
	
	private static TileIconMaker instance = new TileIconMaker();
	
	public static TileIconMaker getIconMaker()
	{
		return instance;
	}
	
	public static void registerIcon(Block block, IIconRegister par1)
	{
		ArrayList<AdvTile> tiles = instance.allTiles.get(block);
		for(AdvTile tile : tiles)
		{
			tile.registerIcon(par1);
		}
	}
	
	public void addTileEntity(Block par1, AdvTile tile)
	{
		if(allTiles.get(par1) == null)
		{
			allTiles.put(par1, new ArrayList<AdvTile>());
		}
		allTiles.get(par1).add(tile);
	}
	
	/**
	 * WhyEver this Should be needed
	 */
	public HashMap<Block, ArrayList<AdvTile>> getAllTiles()
	{
		return allTiles;
	}
	
	public static IIcon getIconFromTile(Block block, Class<? extends AdvTile> clz, int side)
	{
		return instance.getIconSafe(instance.getIconFromTileEntity(block, clz, side));
	}
	
	public IIcon getIconFromTileEntity(Block block, Class<? extends AdvTile> clz, int side)
	{
		ArrayList<AdvTile> tiles = allTiles.get(block);
		try
		{
			for(AdvTile tile : tiles)
			{
				if(match(clz, tile))
				{
					return tile.getIconFromSideAndMetadata(side, 0);
				}
			}
		}
		catch (Exception e)
		{
		}
		return null;
	}
	
	public boolean match(Class<? extends AdvTile> tiles, AdvTile tile)
	{
		if(tiles.getSimpleName().equalsIgnoreCase(tile.getClass().getSimpleName()))
		{
			return true;
		}
		return false;
	}
	
    public IIcon getIconSafe(IIcon par1Icon)
    {
        if (par1Icon == null)
        {
            par1Icon = ((TextureMap)Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.locationBlocksTexture)).getAtlasSprite("missingno");
        }

        return (IIcon)par1Icon;
    }
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void createBeforeIcon(TextureStitchEvent.Pre par1)
	{
		if(par1.map.getTextureType() == 0)
		{
			for(ArrayList<AdvTile> tiles : allTiles.values())
			{
				for(AdvTile tile : tiles)
				{
					tile.registerIcon(par1.map);
				}
			}
			RenderUtilsBlock.pork = par1.map.registerIcon(SpmodAPILib.ModID.toLowerCase()+":utils/pork.raw");
		}

		((FluidHempResin)APIUtils.hempResin).registerIcon(par1.map);
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void createAfterIcons(TextureStitchEvent.Post par1)
	{
		if(par1.map.getTextureType() == 0)
		{
			for(ArrayList<AdvTile> tiles : allTiles.values())
			{
				for(AdvTile tile : tiles)
				{
					tile.registerIcon(par1.map);
				}
			}
			RenderUtilsBlock.pork = par1.map.registerIcon(SpmodAPILib.ModID.toLowerCase()+":utils/pork.raw");
		}

		((FluidHempResin)APIUtils.hempResin).registerIcon(par1.map);
	}
}
