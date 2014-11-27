package speiger.src.api.common.utils;

public class DoubleCounter
{
	double value = 0D;
	
	public DoubleCounter(double par1)
	{
		value = par1;
	}
	
	public DoubleCounter()
	{
		value = 1D;
	}
	
	public void add(double amount)
	{
		value += amount;
	}
	
	public void removeOne()
	{
		value-=1;
	}
	
	public double get()
	{
		return value;
	}
	
	public void addOne()
	{
		value+=1D;
	}
}
