package speiger.src.spmodapi.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;

public class EnchantmentOreFortune extends Enchantment
{

	public EnchantmentOreFortune(int par1)
	{
		super(par1, 2, EnumEnchantmentType.digger);
		this.setName("oreFortune");
	}
	
    public int getMinEnchantability(int par1)
    {
        return 15 + (par1 - 1) * 9;
    }
    
    public int getMaxEnchantability(int par1)
    {
        return super.getMinEnchantability(par1) + 50;
    }
    
    public int getMaxLevel()
    {
        return 1;
    }

	@Override
	public String getName()
	{
		return "Ore Fortune";
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
		if(par1.effectId == APIUtils.oreSilk.effectId)
		{
			return false;
		}
		return true;
	}
	
}
