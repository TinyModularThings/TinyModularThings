package speiger.src.api.common.utils.math;


public class IntCounter
{
	int state;
	int startNumber;
	
	public IntCounter()
	{
		this(0);
	}
	
	public IntCounter(int start)
	{
		state = start;
		startNumber = start;
	}
	
	public IntCounter(int start, int state)
	{
		this(start);
		this.startNumber = state;
	}
	
	public int getCount()
	{
		return state;
	}
	
	public void increase()
	{
		state++;
	}
	
	public void decrease()
	{
		state--;
	}
	
	public boolean isDownReady()
	{
		decrease();
		return state <= 0;
	}
	
	public int getAndIncrease()
	{
		int buffer = state;
		state++;
		return buffer;
	}
	
	public int getAndDecrease()
	{
		int buffer = state;
		state--;
		return buffer;
	}
	
	public void reset()
	{
		state = startNumber;
	}
	
	public void resetTotal()
	{
		state = 0;
	}

	public int getStart()
	{
		return startNumber;
	}
}
