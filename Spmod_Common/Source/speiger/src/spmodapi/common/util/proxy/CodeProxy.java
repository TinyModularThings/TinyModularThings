package speiger.src.spmodapi.common.util.proxy;

import java.lang.reflect.Field;
import java.util.Random;

public class CodeProxy
{
	static Random random = new Random();
	
	public static <T> T getField(Class<?> class1, Class<T> fieldType, Object instance, int fieldIndex) throws IllegalArgumentException, IllegalAccessException
	{
		Field field = class1.getDeclaredFields()[fieldIndex];
		field.setAccessible(true);
		return (T) field.get(instance);
	}
	
	public static void setField(Class<?> class1, Object instance, int fieldIndex, Object value) throws IllegalArgumentException, IllegalAccessException
	{
		Field field = class1.getDeclaredFields()[fieldIndex];
		field.setAccessible(true);
		field.set(instance, value);
	}
	
	public static Random getRandom()
	{
		return random;
	}
}
