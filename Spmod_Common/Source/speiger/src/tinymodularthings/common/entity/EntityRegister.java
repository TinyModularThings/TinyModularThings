package speiger.src.tinymodularthings.common.entity;

import net.minecraft.entity.item.EntityMinecart;
import speiger.src.api.common.utils.config.EntityCounter;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityAdvTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityTinyChestCart;
import cpw.mods.fml.common.registry.EntityRegistry;

public class EntityRegister
{
	private static EntityRegister instance = new EntityRegister();
	public EntityCounter IDs = new EntityCounter();
	
	public static EntityRegister getInstance()
	{
		return instance;
	}
	
	public void registerEntities()
	{
		registerMinecart(EntityTinyChestCart.class, "Tiny Chest Cart", 256, 3);
		registerMinecart(EntityAdvTinyChestCart.class, "Advanced Tiny Chest Cart", 256, 3);
	}
	
	public void registerMinecart(Class<? extends EntityMinecart> claz, String name, int range, int updateTicks)
	{
		EntityRegistry.registerModEntity(claz, name, IDs.getCurrentID(), TinyModularThings.instance, range, updateTicks, true);
		IDs.updateToNextID();
	}
}
