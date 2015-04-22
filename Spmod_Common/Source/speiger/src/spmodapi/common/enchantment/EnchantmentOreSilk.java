package speiger.src.spmodapi.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;

public class EnchantmentOreSilk extends Enchantment
{

	public EnchantmentOreSilk(int par1)
	{
		super(par1, 1, EnumEnchantmentType.digger);
		this.setName("oreSilk");
		
	}
	
    public int getMinEnchantability(int par1)
    {
        return 15;
    }
    
    public int getMaxEnchantability(int par1)
    {
        return super.getMinEnchantability(par1) + 50;
    }

	@Override
	public boolean canApplyTogether(Enchantment par1)
	{
		if(par1.effectId == Enchantment.fortune.effectId)
		{
			return false;
		}
		if(par1.effectId == Enchantment.silkTouch.effectId)
		{
			return false;
		}
		if(par1.effectId == APIUtils.oreFortune.effectId)
		{
			return false;
		}
		return true;
	}

	@Override
	public String getName()
	{
		return "Ore Silk Touch";
	}
    
    
    
}
