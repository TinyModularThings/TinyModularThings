package speiger.src.spmodapi.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import speiger.src.api.blocks.BlockStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TextureEngine
{
	private static TextureEngine instance = new TextureEngine();
	
	private TextureEngine()
	{
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public static TextureEngine getTextures()
	{
		return instance;
	}
	
	HashMap<BlockData, String[]> blockString = new HashMap<BlockData, String[]>();
	HashMap<ItemData, String[]> itemString = new HashMap<ItemData, String[]>();
	
	
	HashMap<BlockData, Icon[]> blockTextures = new HashMap<BlockData, Icon[]>();
	HashMap<ItemData, Icon[]> itemTextures = new HashMap<ItemData, Icon[]>();
	
	
	
	public void registerTexture(BlockStack par1, String...par2)
	{
		registerTexture(par1.getBlock(), par1.getMeta(), par2);
	}
	
	public void registerTexture(ItemStack par1, String...par2)
	{
		registerTexture(par1.getItem(), par1.getItemDamage(), par2);
	}
	
	public void registerTexture(Item par1, int par2, String...par3)
	{
		ItemData data = new ItemData(par1, par2);
		ArrayList<String> textures = new ArrayList<String>();
		String[] before = itemString.get(data);
		if(before != null && before.length > 0)
		{
			for(String key : before)
			{
				textures.add(key);
			}
		}
		for(String key : par3)
		{
			textures.add(key);
		}
		
		itemString.put(data, textures.toArray(new String[textures.size()]));
	}
	
	public void registerTexture(Block par1, int par2, String...par3)
	{
		BlockData data = new BlockData(par1, par2);
		ArrayList<String> textures = new ArrayList<String>();
		String[] before = blockString.get(data);
		if(before != null && before.length > 0)
		{
			for(String key : before)
			{
				textures.add(key);
			}
		}
		for(String key : par3)
		{
			textures.add(key);
		}
		blockString.put(data, textures.toArray(new String[textures.size()]));
	}
	
	
	
	
	@ForgeSubscribe
	@SideOnly(Side.CLIENT)
	public void createAfterIcons(TextureStitchEvent.Post par1)
	{
		if(par1.map.textureType == 0)
		{
			Iterator<Entry<BlockData, String[]>> iter = blockString.entrySet().iterator();
			for(;iter.hasNext();)
			{
				Entry<BlockData, String[]> texture = iter.next();
				Icon[] icons = new Icon[texture.getValue().length];
				for(int i = 0;i<icons.length;i++)
				{
					icons[i] = par1.map.registerIcon(texture.getValue()[i]);
				}
				blockTextures.put(texture.getKey(), icons);
			}
		}
		else
		{
			Iterator<Entry<ItemData, String[]>> iter = itemString.entrySet().iterator();
			for(;iter.hasNext();)
			{
				Entry<ItemData, String[]> texture = iter.next();
				Icon[] icons = new Icon[texture.getValue().length];
				for(int i = 0;i<icons.length;i++)
				{
					icons[i] = par1.map.registerIcon(texture.getValue()[i]);
				}
				itemTextures.put(texture.getKey(), icons);
			}
		}
	}
	
	@ForgeSubscribe
	@SideOnly(Side.CLIENT)
	public void createBeforeIcon(TextureStitchEvent.Pre par1)
	{
		if(par1.map.textureType == 0)
		{
			Iterator<Entry<BlockData, String[]>> iter = blockString.entrySet().iterator();
			for(;iter.hasNext();)
			{
				Entry<BlockData, String[]> texture = iter.next();
				Icon[] icons = new Icon[texture.getValue().length];
				for(int i = 0;i<icons.length;i++)
				{
					icons[i] = par1.map.registerIcon(texture.getValue()[i]);
				}
				blockTextures.put(texture.getKey(), icons);
			}
		}
		else
		{
			Iterator<Entry<ItemData, String[]>> iter = itemString.entrySet().iterator();
			for(;iter.hasNext();)
			{
				Entry<ItemData, String[]> texture = iter.next();
				Icon[] icons = new Icon[texture.getValue().length];
				for(int i = 0;i<icons.length;i++)
				{
					icons[i] = par1.map.registerIcon(texture.getValue()[i]);
				}
				itemTextures.put(texture.getKey(), icons);
			}
		}
	}
	
	
	
	
	public Icon getTexture(Block par1, int key)
	{
		return getTexture(par1, 0, key);
	}
	
	public Icon getTexture(Item par1, int key)
	{
		return getTexture(par1, 0, key);
	}
	
	public Icon getTexture(Block par1, int meta, int key)
	{
		Icon[] texture = blockTextures.get(new BlockData(par1, meta));
		if(texture != null && texture.length > key)
		{
			return texture[key];
		}
		return TileIconMaker.getIconMaker().getIconSafe(null);
	}
	
	public Icon getTexture(Item par1, int meta, int key)
	{
		Icon[] texture = itemTextures.get(new ItemData(par1, meta));
		if(texture != null && texture.length > key)
		{
			return texture[key];
		}
		return TileIconMaker.getIconMaker().getIconSafe(null);
	}
	
	public static Icon[] getIcon(Block par1, int par2)
	{
		Icon[] texture = instance.blockTextures.get(new BlockData(par1, par2));
		if(texture == null)
		{
			texture = new Icon[0];
		}
		return texture;
	}
	
	public static Icon[] getIcon(Item par1, int par2)
	{
		Icon[] texture = instance.itemTextures.get(new ItemData(par1, par2));
		if(texture == null)
		{
			texture = new Icon[0];
		}
		return texture;
	}
	
	
	
	
	public static class ItemData
	{
		Item item;
		int meta;
		
		public ItemData(ItemStack par2)
		{
			this(par2.getItem(), par2.getItemDamage());
		}
		
		public ItemData(Item par1, int par2)
		{
			item = par1;
			meta = par2;
		}

		@Override
		public boolean equals(Object arg0)
		{
			if(arg0 == null)
			{
				return false;
			}
			if(!(arg0 instanceof ItemData))
			{
				return false;
			}
			ItemData par1 = (ItemData)arg0;
			if(par1.item != item)
			{
				return false;
			}
			if(par1.meta != meta)
			{
				return false;
			}
			return true;
		}
		
		
		
	}
	
	public static class BlockData
	{
		Block block;
		int meta;
		
		public BlockData(BlockStack par1)
		{
			this(par1.getBlock(), par1.getMeta());
		}
		public BlockData(Block par1, int par2)
		{
			block = par1;
			meta = par2;
		}
		@Override
		public boolean equals(Object arg0)
		{
			if(arg0 == null)
			{
				return false;
			}
			if(!(arg0 instanceof BlockData))
			{
				return false;
			}
			BlockData par1 = (BlockData)arg0;
			if(par1.block != block)
			{
				return false;
			}
			if(par1.meta != meta)
			{
				return false;
			}
			return true;
		}
	}
	
	
	
}
