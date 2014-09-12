package speiger.src.api.items.plates;

import java.util.ArrayList;

import net.minecraft.util.ResourceLocation;
import speiger.src.api.items.plates.PlateManager.PlateInformation;

public interface PlateInterface
{
	public abstract PlateInformation registerPlate(ResourceLocation texture, String ItemTexture, String identify, String displayName);
	
	public abstract void removePlate(String identifier);
	
	public abstract ResourceLocation getTexture(String identifier);
	
	public abstract PlateInformation getInfoFromIdentity(String identifier);
	
	public abstract String getIconFromIdentity(String key);
	
	public abstract ArrayList<String> getAllIdentifiers();
}
