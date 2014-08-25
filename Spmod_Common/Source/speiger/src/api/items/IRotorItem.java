package speiger.src.api.items;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import speiger.src.api.tiles.IWindmill;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IRotorItem
{
	/**
	 * @ItemStack is the Rotor Itself. Just NBT support
	 * @Return If the rotor ignore the Tier of the 
	 */
	boolean ignoreTier(ItemStack par1);

	/**
	 * @ItemStack is the rotor.
	 * @Tier Is the Windmill Tier.
	 * @Return if the rotor can Work with that Tier.
	 */
	boolean canWorkWithWindmillTier(ItemStack par1, int tier);
	
	/**
	 * @ItemStack is the Rotor itself
	 * @Return the Tier of the Rotor.
	 */
	int getTier(ItemStack par1);
	
	/**
	 * @Windmill call the function distroy rotor if the rotor should die.
	 * @ItemStack is the Rotor itself.
	 * @Damage is the Damage what the Rotor get. Based on the Tier of the Windmill and the Tier of the rotor.
	 * if the Rotor ignore the Tier of the Windmill than the damage can be less or higher than 1. 
	 * @Note: Even if a Tier 5 Rotor stays in a Tier 0 Windmill than it still gains damage. But the chance is really low.
	 * @AnotherNot: If a Tier 0 Rotor stays in a Tier 5 Rotor it gains 4-5 Times more damage. But that is also random.
	 */
	void damageRotor(ItemStack par1, int damage, IWindmill windmill);

	/**
	 * @ItemStack Rotor Itself.
	 * @Return returns the Rotor Texture for Special Renderer.
	 */
	@SideOnly(Side.CLIENT)
	ResourceLocation getRenderTexture(ItemStack par1);

	/**
	 * This is the Rotor tick. Happens every 60 Ticks (3 Sek)
	 * This happens Before everything else in requesting Stuff.
	 * @IWindMill Windmill Itself.
	 * @World is the world where the Windmill is in
	 * @Rotor is the rotor itself.
	 */
	void onRotorTick(IWindmill windMill, World world, ItemStack rotor);
	
	/**
	 * @ItemStack The Rotor itself.
	 * @Return the efficeny of the Rotor
	 */
	float getRotorEfficeny(ItemStack par1, IWindmill par2);
	
	/**
	 * This function will call the damage stuff.
	 * If the rotor is advanced the damage modifires will be based on the Tiers. (if rotor tier is lower as the Windmill damage will higher and opposite)
	 * return false to make normal damage. it gains ever 64 ticks (3,2 sek) 1 damage.
	 * @ItemStack the rotor Itself.
	 * @Return Return if its a Basic (false) or Advanced (true) rotor.
	 */
	boolean isAdvancedRotor(ItemStack par1);
	
	/**
	 * @ItemStack rotor Itself
	 * @Return if the Rotor Stays for Inifite Time. 
	 */
	boolean isInfinite(ItemStack par1);
	
	/**
	 * @ItemStack just Rotor Itself Again.
	 * @Size is the Size of the Rotor. So how big it has to be renderer. So you can use it in your Constructure.
	 * @Return the Custom model of the Rotor. If its null it load the Default model.
	 */
	IRotorModel getCustomModel(ItemStack par1, int size);
	
	
	public static interface IRotorModel
	{
		/**
		 * @Note Render Function that the Model get Rendererd.
		 */
		void render(float f5);
	}
}
