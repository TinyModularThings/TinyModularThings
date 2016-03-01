package speiger.src.api.common.recipes.gas;

import java.util.Random;

import net.minecraft.entity.EntityAgeable;

public interface IGasEntity
{
	public GasInfo getGasInfo(EntityAgeable par1);
	
	public static class GasInfo
	{
		int min;
		int max;
		
		int result;
		
		public GasInfo(int par1, int par2)
		{
			min = par1;
			max = par2;
			if(max > 9)
			{
				max = 9;
			}
			if(min > 9)
			{
				min = 9;
			}
		}
		
		public int getMax()
		{
			return max;
		}
		
		public int getMin()
		{
			return min;
		}
		
		public int getResult()
		{
			return result;
		}
		
		public GasInfo setResult(Random rand)
		{
			result = min + rand.nextInt(max + 1);
			if(result > 10)
			{
				result = 10;
			}
			return this;
		}
	}
}
