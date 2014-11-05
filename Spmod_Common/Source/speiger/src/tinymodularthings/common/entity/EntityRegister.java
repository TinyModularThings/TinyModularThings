package speiger.src.tinymodularthings.common.entity;

import net.minecraft.entity.item.EntityMinecart;
import speiger.src.api.common.utils.config.EntityCounter;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityAdvTinyChestCart.EightSlotAdvTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityAdvTinyChestCart.FiveSlotAdvTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityAdvTinyChestCart.FourSlotAdvTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityAdvTinyChestCart.NineSlotAdvTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityAdvTinyChestCart.OneSlotAdvTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityAdvTinyChestCart.SevenSlotAdvTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityAdvTinyChestCart.SixSlotAdvTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityAdvTinyChestCart.ThreeSlotAdvTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityAdvTinyChestCart.TwoSlotAdvTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityTinyChestCart.EightSlotTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityTinyChestCart.FiveSlotTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityTinyChestCart.FourSlotTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityTinyChestCart.NineSlotTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityTinyChestCart.OneSlotTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityTinyChestCart.SevenSlotTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityTinyChestCart.SixSlotTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityTinyChestCart.ThreeSlotTinyChestCart;
import speiger.src.tinymodularthings.common.entity.minecarts.tinychest.EntityTinyChestCart.TwoSlotTinyChestCart;
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
		registerMinecart(OneSlotTinyChestCart.class, "TinyChestCartOne", 256, 3);
		registerMinecart(TwoSlotTinyChestCart.class, "TinyChestCartTwo", 256, 3);
		registerMinecart(ThreeSlotTinyChestCart.class, "TinyChestCartThree", 256, 3);
		registerMinecart(FourSlotTinyChestCart.class, "TinyChestCartFour", 256, 3);
		registerMinecart(FiveSlotTinyChestCart.class, "TinyChestCartFive", 256, 3);
		registerMinecart(SixSlotTinyChestCart.class, "TinyChestCartSix", 256, 3);
		registerMinecart(SevenSlotTinyChestCart.class, "TinyChestCartSeven", 256, 3);
		registerMinecart(EightSlotTinyChestCart.class, "TinyChestCartEight", 256, 3);
		registerMinecart(NineSlotTinyChestCart.class, "TinyChestCartNine", 256, 3);
		
		registerMinecart(OneSlotAdvTinyChestCart.class, "AdvTinyChestCartOne", 256, 3);
		registerMinecart(TwoSlotAdvTinyChestCart.class, "AdvTinyChestCartTwo", 256, 3);
		registerMinecart(ThreeSlotAdvTinyChestCart.class, "AdvTinyChestCartThree", 256, 3);
		registerMinecart(FourSlotAdvTinyChestCart.class, "AdvTinyChestCartFour", 256, 3);
		registerMinecart(FiveSlotAdvTinyChestCart.class, "AdvTinyChestCartFive", 256, 3);
		registerMinecart(SixSlotAdvTinyChestCart.class, "AdvTinyChestCartSix", 256, 3);
		registerMinecart(SevenSlotAdvTinyChestCart.class, "AdvTinyChestCartSeven", 256, 3);
		registerMinecart(EightSlotAdvTinyChestCart.class, "AdvTinyChestCartEight", 256, 3);
		registerMinecart(NineSlotAdvTinyChestCart.class, "AdvTinyChestCartNine", 256, 3);
		
	}
	
	public void registerMinecart(Class<? extends EntityMinecart> claz, String name, int range, int updateTicks)
	{
		EntityRegistry.registerModEntity(claz, name, IDs.getCurrentID(), TinyModularThings.instance, range, updateTicks, true);
		IDs.updateToNextID();
	}
}
