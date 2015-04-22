package speiger.src.api.common.utils.config;

public class EntityCounter
{
	int ID;
	
	public EntityCounter()
	{
		ID = 1;
	}
	
	public EntityCounter(int i)
	{
		ID = i;
	}
	
	public int getCurrentID()
	{
		return ID;
	}
	
	public void updateToNextID()
	{
		ID++;
	}
	
	public void resetCounter()
	{
		ID = 0;
	}

	public int getAndUpdateID()
	{
		int id = getCurrentID();
		updateToNextID();
		return id;
	}
	
}
