package speiger.src.spmodapi.common.items.hemp;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.items.core.SpmodItem;

public class ItemHempCake extends SpmodItem
{
	
	public ItemHempCake(int par1)
	{
		super(par1);
		this.setCreativeTab(APIUtils.tabHemp);
		this.setNoRepair();
		this.setRarity(EnumRarity.uncommon);
	}

	@Override
	public ItemStack onEaten(ItemStack par1, World par2, EntityPlayer par3)
	{
        par2.playSoundAtEntity(par3, "random.burp", 0.5F, par2.rand.nextFloat() * 0.1F + 0.9F);
		par3.clearActivePotions();
		par3.getFoodStats().addStats(3, 1.0F);
		par3.heal(1);
		par1.stackSize--;
		
		if(!par2.isRemote)
		{
			par3.addPotionEffect(new PotionEffect(Potion.regeneration.id, 60, 1));
		}
		return par1;
	}

	

	@Override
	public ItemStack onItemRightClick(ItemStack par1, World par2, EntityPlayer par3)
	{
		if(par3.canEat(true))
		{
			par3.setItemInUse(par1, getMaxItemUseDuration(par1));
		}
		return par1;
	}

    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 50;
    }

    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.eat;
    }
    
	@Override
	public String getName(ItemStack par1)
	{
		return "Hemp Cake";
	}
	
}
