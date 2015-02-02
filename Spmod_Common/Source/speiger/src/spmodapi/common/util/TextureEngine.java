package speiger.src.spmodapi.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import speiger.src.api.common.data.utils.BlockData;
import speiger.src.api.common.data.utils.ItemData;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.common.interfaces.ITextureRequester;
import speiger.src.spmodapi.common.lib.SpmodAPILib;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TextureEngine
{
	private static TextureEngine instance;
	
	private TextureEngine()
	{
		MinecraftForge.EVENT_BUS.register(this);
		currentSide = FMLCommonHandler.instance().getEffectiveSide();
	}
	
	public static TextureEngine getTextures()
	{
		if(instance == null)
		{
			instance = new TextureEngine();
		}
		return instance;
	}
	
	Icon[] missingTexture = new Icon[2];
	@SideOnly(Side.CLIENT)
	private TextureMap map;
	HashMap<BlockData, String[]> blockString = new HashMap<BlockData, String[]>();
	HashMap<ItemData, String[]> itemString = new HashMap<ItemData, String[]>();
	HashMap<ItemData, String[]> itemBlockString = new HashMap<ItemData, String[]>();
	
	HashMap<BlockData, Icon[]> blockTextures = new HashMap<BlockData, Icon[]>();
	HashMap<ItemData, Icon[]> itemTextures = new HashMap<ItemData, Icon[]>();
	HashMap<ItemData, Icon[]> itemBlockTextures = new HashMap<ItemData, Icon[]>();
	
	HashMap<String, ResourceLocation> guiTextures = new HashMap<String, ResourceLocation>();
	HashMap<String, int[]> guiPositions = new HashMap<String, int[]>();
	
	String currentMod = "";
	String currentPath = "";
	
	ArrayList<RequestData> requestLaterRegistration = new ArrayList<RequestData>();
	
	final Side currentSide;
	
	public void setCurrentMod(String modID)
	{
		if(currentMod != "")
		{
			finishMod();
		}
		currentMod = modID;
	}
	
	public void finishMod()
	{
		currentMod = "";
	}
	
	public void setCurrentPath(String path)
	{
		if(currentPath != "")
		{
			removePath();
		}
		currentPath = path+"/";
	}
	
	public void removePath()
	{
		currentPath = "";
	}
	
	public void registerGuiTexture(String key, String filename)
	{
		guiTextures.put(key, new ResourceLocation(currentMod+":textures/gui/"+currentPath+filename+".png"));
	}
	
	public ResourceLocation getTexture(String key)
	{
		return guiTextures.get(key);
	}
	
	public void registerGuiPos(String key, int x, int y)
	{
		guiPositions.put(key, new int[]{x,y});
	}
	
	public int[] getGuiPos(String key)
	{
		return guiPositions.get(key);
	}
	
	public void markForDelay(ITextureRequester par1)
	{
		RequestData data = new RequestData();
		data.data = par1;
		this.requestLaterRegistration.add(data);
	}

	public String[] getTextureNames(BlockData data)
	{
		if(blockString.containsKey(data))
		{
			return blockString.get(data);
		}
		return new String[]{"No Texture Key There"};
	}
	
	public String[] getTextureNames(ItemData data)
	{
		if(itemString.containsKey(data))
		{
			return itemString.get(data);
		}
		return new String[]{"No Texture Key There"};
	}
	
	public void registerTexture(Block par1, String...par2)
	{
		registerTexture(par1, 0, par2);
	}
	
	public void registerTexture(Item par1, String...par2)
	{
		registerTexture(par1, 0, par2);
	}
	
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
		if(currentSide == Side.SERVER)
		{
			return;
		}
		if(par1.getSpriteNumber() == 1)
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
				textures.add(currentMod+":"+currentPath+key);
			}

			itemString.put(data, textures.toArray(new String[textures.size()]));
		}
		else
		{
			ItemData data = new ItemData(par1, par2);
			ArrayList<String> textures = new ArrayList<String>();
			String[] before = itemBlockString.get(data);
			if(before != null && before.length > 0)
			{
				for(String key : before)
				{
					textures.add(key);
				}
			}
			for(String key : par3)
			{
				textures.add(currentMod+":"+currentPath+key);
			}
			itemBlockString.put(data, textures.toArray(new String[textures.size()]));
		}
	}
	
	public void registerTexture(Block par1, int par2, String...par3)
	{
		if(currentSide == Side.SERVER)
		{
			return;
		}
		
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
			textures.add(currentMod+":"+currentPath+key);
		}
		blockString.put(data, textures.toArray(new String[textures.size()]));
	}
	
	
	
	
	@ForgeSubscribe
	@SideOnly(Side.CLIENT)
	public void createAfterIcons(TextureStitchEvent.Post par1)
	{	
		ArrayList<RequestData> remove = new ArrayList<RequestData>();
		for(RequestData data : requestLaterRegistration)
		{
			if(data.data.onTextureAfterRegister(this))
			{
				remove.add(data);
			}
		}
		if(!this.requestLaterRegistration.isEmpty() && !remove.isEmpty())
		{
			this.requestLaterRegistration.removeAll(remove);
		}
		
		
		if(par1.map.textureType == 0)
		{
			Iterator<Entry<BlockData, String[]>> iter = blockString.entrySet().iterator();
			this.missingTexture[0] = par1.map.registerIcon(SpmodAPILib.ModID.toLowerCase()+":missingTexture");
			for(;iter.hasNext();)
			{
				Entry<BlockData, String[]> texture = iter.next();
				Icon[] icons = new Icon[texture.getValue().length];
				for(int i = 0;i<icons.length;i++)
				{
					icons[i] = par1.map.registerIcon(texture.getValue()[i]);
					if(!isTextureRegistered(icons[i], map))
					{
						icons[i] = this.getIconSafe(missingTexture[0]);
					}
				}
				blockTextures.put(texture.getKey(), icons);
			}
			Iterator<Entry<ItemData, String[]>> itemIter = itemBlockString.entrySet().iterator();
			for(;itemIter.hasNext();)
			{
				Entry<ItemData, String[]> texture = itemIter.next();
				Icon[] icons = new Icon[texture.getValue().length];
				for(int i = 0;i<icons.length;i++)
				{
					icons[i] = par1.map.registerIcon(texture.getValue()[i]);
					if(!isTextureRegistered(icons[i], map))
					{
						icons[i] = this.getIconSafe(missingTexture[0]);
					}
				}
				itemBlockTextures.put(texture.getKey(), icons);
			}
		}
		else
		{
			this.missingTexture[1] = par1.map.registerIcon(SpmodAPILib.ModID.toLowerCase()+":missingTexture");
			Iterator<Entry<ItemData, String[]>> iter = itemString.entrySet().iterator();
			for(;iter.hasNext();)
			{
				Entry<ItemData, String[]> texture = iter.next();
				Icon[] icons = new Icon[texture.getValue().length];
				for(int i = 0;i<icons.length;i++)
				{
					icons[i] = par1.map.registerIcon(texture.getValue()[i]);
					if(!isTextureRegistered(icons[i], map))
					{
						icons[i] = this.getIconSafe(missingTexture[1]);
					}
				}
				itemTextures.put(texture.getKey(), icons);
			}
		}
	}
	
	@ForgeSubscribe
	@SideOnly(Side.CLIENT)
	public void createBeforeIcon(TextureStitchEvent.Pre par1)
	{
		map = par1.map;
		
		ArrayList<RequestData> remove = new ArrayList<RequestData>();
		for(RequestData data : requestLaterRegistration)
		{
			if(data.data.onTextureAfterRegister(this))
			{
				remove.add(data);
			}
		}
		if(!this.requestLaterRegistration.isEmpty() && !remove.isEmpty())
		{
			this.requestLaterRegistration.removeAll(remove);
		}
		
		
		
		if(par1.map.textureType == 0)
		{
			this.missingTexture[0] = par1.map.registerIcon(SpmodAPILib.ModID.toLowerCase()+":missingTexture");
			Iterator<Entry<BlockData, String[]>> iter = blockString.entrySet().iterator();
			for(;iter.hasNext();)
			{
				Entry<BlockData, String[]> texture = iter.next();
				Icon[] icons = new Icon[texture.getValue().length];
				for(int i = 0;i<icons.length;i++)
				{
					icons[i] = par1.map.registerIcon(texture.getValue()[i]);
					if(!isTextureRegistered(icons[i], map))
					{
						icons[i] = this.getIconSafe(missingTexture[0]);
					}
				}
				blockTextures.put(texture.getKey(), icons);
			}
			Iterator<Entry<ItemData, String[]>> itemIter = itemBlockString.entrySet().iterator();
			for(;itemIter.hasNext();)
			{
				Entry<ItemData, String[]> texture = itemIter.next();
				Icon[] icons = new Icon[texture.getValue().length];
				for(int i = 0;i<icons.length;i++)
				{
					icons[i] = par1.map.registerIcon(texture.getValue()[i]);
					if(!isTextureRegistered(icons[i], map))
					{
						icons[i] = this.getIconSafe(missingTexture[0]);
					}
				}
				itemBlockTextures.put(texture.getKey(), icons);
			}
		}
		else
		{
			this.missingTexture[1] = par1.map.registerIcon(SpmodAPILib.ModID.toLowerCase()+":missingTexture");
			Iterator<Entry<ItemData, String[]>> iter = itemString.entrySet().iterator();
			for(;iter.hasNext();)
			{
				Entry<ItemData, String[]> texture = iter.next();
				Icon[] icons = new Icon[texture.getValue().length];
				for(int i = 0;i<icons.length;i++)
				{
					icons[i] = par1.map.registerIcon(texture.getValue()[i]);
					if(!isTextureRegistered(icons[i], map))
					{
						icons[i] = this.getIconSafe(missingTexture[1]);
					}
				}
				itemTextures.put(texture.getKey(), icons);
			}
		}

	}
	@SideOnly(Side.CLIENT)
	public boolean isTextureRegistered(Icon par1, TextureMap par2)
	{
		Icon result = par2.getAtlasSprite(par1.getIconName());
		if(result.getIconName().equalsIgnoreCase("missingno"))
		{
			return false;
		}
		return true;
	}
	
	
	public Icon getTexture(Block par1)
	{
		return getTexture(par1, 0);
	}
	
	public Icon getTexture(Item par1)
	{
		return getTexture(par1, 0);
	}
	
	public Icon getTexture(Block par1, int key)
	{
		return getTexture(par1, 0, key);
	}
	
	public Icon getTexture(Item par1, int key)
	{
		return getTexture(par1, 0, key);
	}
	
	public Icon getTexture(BlockStack par1, int key)
	{
		return getTexture(par1.getBlock(), par1.getMeta(), key);
	}
	
	public Icon getTexture(ItemStack par1, int key)
	{
		return getTexture(par1.getItem(), par1.getItemDamage(), key);
	}
	
	public Icon getTexture(Block par1, int meta, int key)
	{
		Icon[] texture = blockTextures.get(new BlockData(par1, meta));
		if(texture != null && texture.length > key)
		{
			return texture[key];
		}
		return getIconSafe();
	}
	
	public Icon getTexture(Item par1, int meta, int key)
	{
		Icon[] texture = itemTextures.get(new ItemData(par1, meta));
		if(texture != null && texture.length > key)
		{
			return texture[key];
		}
		texture = itemBlockTextures.get(new ItemData(par1, meta));
		if(texture != null && texture.length > key)
		{
			return texture[key];
		}
		return getIconSafe();
	}
	
	public static Icon[] getIcon(Block par1, int par2)
	{
		Icon[] texture = instance.blockTextures.get(new BlockData(par1, par2));
		if(texture == null)
		{
			texture = new Icon[]{instance.getIconSafe()};
		}
		return texture;
	}
	
	public static Icon[] getIcon(Item par1, int par2)
	{
		Icon[] texture = instance.itemTextures.get(new ItemData(par1, par2));
		if(texture == null)
		{
			texture = new Icon[]{instance.getIconSafe()};
		}
		texture = instance.itemBlockTextures.get(new ItemData(par1, par2));
		if(texture == null)
		{
			texture = new Icon[]{instance.getIconSafe()};
		}
		return texture;
	}
	
	public Icon[] getTextureInRange(Item par1, int min, int max)
	{
		return getTextureInRange(par1, 0, min, max);
	}
	
	public Icon[] getTextureInRange(Block par1, int min, int max)
	{
		return getTextureInRange(par1, 0, min, max);
	}
	
	public Icon[] getTextureInRange(Item par1, int meta, int min, int max)
	{
		Icon[] result = new Icon[max-min];
		for(int i = min;i<max;i++)
		{
			result[i-min] = getTexture(par1, meta, i);
		}
		return result;
	}
	
	public Icon[] getTextureInRange(Block par1, int meta, int min, int max)
	{
		Icon[] result = new Icon[max-min];
		for(int i = min;i<max;i++)
		{
			result[i-min] = getTexture(par1, meta, i);
		}
		return result;
	}
	
	public Icon getIconSafe(Icon par1)
	{
		if(par1 == null)
		{
			par1 = getIconSafe();
		}
		return par1;
	}
	
	@SideOnly(Side.CLIENT)
	public Icon getIconSafe()
	{
		if(map != null)
		{
			return this.missingTexture[map.textureType];
		}
		return TileIconMaker.getIconMaker().getIconSafe(missingTexture[1]);
	}
	
	public boolean isMissingTexture(Icon par1)
	{
		if(par1 == null)
		{
			return true;
		}
		if(par1.getIconName().equalsIgnoreCase(getIconSafe().getIconName()))
		{
			return true;
		}
		if(par1.getIconName().equalsIgnoreCase(TileIconMaker.getIconMaker().getIconSafe(null).getIconName()))
		{
			return true;
		}
		
		return false;
	}
	
	public static class RequestData
	{
		ITextureRequester data;
	}

	@SideOnly(Side.CLIENT)
	public TextureMap getTextureMap()
	{
		return map;
	}

	
	
	
}
