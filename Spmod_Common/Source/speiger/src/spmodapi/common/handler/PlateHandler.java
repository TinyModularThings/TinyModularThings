package speiger.src.spmodapi.common.handler;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.util.ResourceLocation;
import speiger.src.api.items.plates.PlateInterface;
import speiger.src.api.items.plates.PlateManager.PlateInformation;
import speiger.src.api.util.config.EntityCounter;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;

public class PlateHandler implements PlateInterface
{
	public HashMap<String, PlateInformation> plates = new HashMap<String, PlateInformation>();
	public HashMap<String, ResourceLocation> textures = new HashMap<String, ResourceLocation>();
	public HashMap<String, String> itemTextures = new HashMap<String, String>();
	public ArrayList<String> identities = new ArrayList<String>();
	public EntityCounter ids = new EntityCounter(0);
	
	@Override
	public PlateInformation registerPlate(ResourceLocation texture, String ItemTexture, String identify, String displayName, float hardness)
	{
		PlateInformation info = new PlateInformation();
		info.setMetadata(ids.getCurrentID());
		ids.updateToNextID();
		info.setItem(APIItems.multiPlate);
		info.setIdentity(identify);
		info.setDisplayName(displayName);
		info.setHardness(hardness);
		plates.put(identify, info);
		textures.put(identify, texture);
		itemTextures.put(identify, ItemTexture);
		identities.add(identify);
		return info;
	}
	
	@Override
	public void removePlate(String identifier)
	{
		plates.remove(identifier);
		textures.remove(identifier);
		identities.remove(identifier);
	}
	
	@Override
	public ResourceLocation getTexture(String identifier)
	{
		return textures.get(identifier);
	}
	
	@Override
	public PlateInformation getInfoFromIdentity(String identifier)
	{
		return plates.get(identifier);
	}
	
	@Override
	public String getIconFromIdentity(String key)
	{
		return itemTextures.get(key);
	}
	
	@Override
	public ArrayList<String> getAllIdentifiers()
	{
		return identities;
	}
	
}
