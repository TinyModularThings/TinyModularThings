package speiger.src.api.common.world.items;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import speiger.src.api.common.world.tiles.interfaces.IWindmill;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IRotorItem
{
	
	/**
	 * @Windmill call the function distroy rotor if the rotor should die.
	 * @ItemStack is the Rotor itself.
	 * @Damage is the Damage what the Rotor get. Based on the Tier of the
	 *         Windmill and the Tier of the rotor. if the Rotor ignore the Tier
	 *         of the Windmill than the damage can be less or higher than 1.
	 * @Note: Even if a Tier 5 Rotor stays in a Tier 0 Windmill than it still
	 *        gains damage. But the chance is really low.
	 * @AnotherNot: If a Tier 0 Rotor stays in a Tier 5 Rotor it gains 4-5 Times
	 *              more damage. But that is also random.
	 */
	void damageRotor(ItemStack par1, int damage, IWindmill windmill);
	
	/**
	 * @ItemStack Rotor Itself.
	 * @Return returns the Rotor Texture for Special Renderer.
	 */
	@SideOnly(Side.CLIENT)
	ResourceLocation getRenderTexture(ItemStack par1);
	
	/**
	 * This Function ask the Rotor on the Initing of the RotorBlade if he does handle
	 * his Speed math by its own. If you return false then its using the basic compact windmills math.
	 * Only use at the moment the IceRotor that speeds up in the nether.
	 * @param par1
	 * @param rotor
	 * @return
	 */
	boolean hasCustomSpeedMath(IWindmill par1, ItemStack rotor);
	
	/**
	 * This is the Rotor tick. Happens every 64 Ticks (3,2 Sek) This happens
	 * Before everything else in requesting Stuff.
	 * 
	 * If you do custom Rotor Speeds then you can do that here. So that the recalculation happens not to often.
	 * 
	 * @IWindMill Windmill Itself.
	 * @World is the world where the Windmill is in
	 * @Rotor is the rotor itself.
	 */
	void onRotorTick(IWindmill windMill, World world, ItemStack rotor);
	
	/**
	 * @ItemStack rotor Itself
	 * @Return if the Rotor Stays for Inifite Time.
	 */
	boolean isInfinite(ItemStack par1);
	
	/**
	 * @Return the Weight of the Rotor which is for the Calculation needed.
	 * Will be requested in the Initing of the Item. So only 1 Time.
	 */
	public RotorWeight getRotorWeight(IWindmill par1, ItemStack par2);
	
	/**
	 * @ItemStack just Rotor Itself Again.
	 * @Size is the Size of the Rotor. So how big it has to be renderer. So you
	 *       can use it in your Constructure.
	 * @Return the Custom model of the Rotor. If its null it load the Default
	 *         model.
	 */
	IRotorModel getCustomModel(ItemStack par1);
	
	public static interface IRotorModel
	{
		/**
		 * @Note Render Function that the Model get Rendererd.
		 */
		void render(float f5);
	}

	public static enum RotorWeight
	{
		VeryLeight(0.01F),
		Leight(0.005F),
		Medium(0.001F),
		Heavy(0.0001F),
		VeryHeavy(0.00001F);
		
		float change;
		
		private RotorWeight(float speedchange)
		{
			change = speedchange;
		}
		
		public float getSpeedChange()
		{
			return change;
		}
	}
}
