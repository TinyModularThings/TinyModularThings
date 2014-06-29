package speiger.src.spmodapi.common.interfaces;

/**
 * 
 * @author Speiger
 * 
 */
public interface IRotation
{
	/**
	 * My Rotation/Facing Interface. Very Basic
	 */
	
	void setFacing(int side);
	
	int getFacing();
	
	void setRotation(int side);
	
	int getRotation();
	
	int setNextFacing();
	
	int setNextRotation();
	
}
