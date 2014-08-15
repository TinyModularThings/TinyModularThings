package speiger.src.spmodapi.common.fluids.potion;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;

public class PotionRecipeList
{
	
	public static class PotionKey
	{
		PotionType type;
		Item item;
		int meta;
		
		public PotionKey(PotionType par1, ItemStack par2)
		{
			type = par1;
			item = par2.getItem();
			meta = par2.getItemDamage();
		}
	}
	
	public static enum PotionType
	{
		NetherWater(false),
		SugarWater(false),
		FruitWater(false),
		MelonWater(false),
		BlazedWater(false),
		MagmadigWater(false),
		PoisenWater(false),
		FermentedWater(false),
		TearedWater(false),
		ElectricWater(false),
		GlowingWater(false),
		ExplosiveWater(false),
		
		SpeedPotion(true, true, Potion.moveSpeed),
		SlownessPotion(true, false, Potion.moveSlowdown),
		DigSpeedPotion(true, true, Potion.digSpeed),
		DigSlownessPotion(true, false, Potion.digSlowdown),
		StrenghtPotion(true, true, Potion.damageBoost),
		WeaknessPotion(true, false, Potion.weakness),
		HealingPotion(true, true, Potion.heal),
		HarmingPotion(true, false, Potion.harm),
		JumpBoostPotion(true, true, Potion.jump),
		ConfusionPotion(true, false, Potion.confusion),
		RegenerationPotion(true, true, Potion.regeneration),
		PoisenPotion(true, false, Potion.poison),
		FireResistancePotion(true, true, Potion.fireResistance),
		WaterBreathingPotion(true, true, Potion.waterBreathing),
		InvisiblityPotion(true, true, Potion.invisibility),
		BlindnessPotion(true, false, Potion.blindness),
		NightVisionPotion(true, true, Potion.nightVision);
				
		boolean effect;
		boolean goodEffect;
		int potionID;
		
		private PotionType(boolean hasEffect)
		{
			effect = hasEffect;
			potionID = -1;
		}
		
		private PotionType(boolean hasEffect, boolean goodEffect)
		{
			this(hasEffect);
			this.goodEffect = goodEffect;
		}
		
		private PotionType(boolean hasEffect, boolean goodEffect, int potionID)
		{
			this(hasEffect, goodEffect);
			this.potionID = potionID;
		}
		
		private PotionType(boolean hasEffect, boolean goodEffect, Potion potion)
		{
			this(hasEffect, goodEffect, potion.getId());
		}	
	}
}
