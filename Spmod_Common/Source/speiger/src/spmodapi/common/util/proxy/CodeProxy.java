package speiger.src.spmodapi.common.util.proxy;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import speiger.src.spmodapi.common.enums.EnumColor;

public class CodeProxy
{
	static Random random = new Random();
	static OreDictionary ore;
	
	public static <T> T getField(Class<?> class1, Class<T> fieldType, Object instance, int fieldIndex) throws IllegalArgumentException, IllegalAccessException
	{
		Field field = class1.getDeclaredFields()[fieldIndex];
		field.setAccessible(true);
		return (T) field.get(instance);
	}
	
	public static <T> T getField(Class<?> class1, Class<T> fieldType, Object instance, String fieldName) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
	{
		Field field = class1.getDeclaredField(fieldName);
		field.setAccessible(true);
		return (T)field.get(instance);
	}
	
	public static <T> T getField(Class<T> fieldType, Object instance, String fieldName) throws Exception
	{
		Field field = instance.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		return (T)field.get(instance);
	}
	
	public static <T> T getMethode(Class<T> fieldType, Object instance, String fieldName, Object key) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		return (T)instance.getClass().getMethod(fieldName, Object.class).invoke(null, key);
	}
	
	public static <T> T getMethode(Class<T> fieldType, Object instance, int type, Object arg) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException
	{
		return (T)instance.getClass().getMethods()[type].invoke(null, arg);
	}
	
	public static <T> T getField(Class<T> fieldType, Object instance, int index) throws IllegalArgumentException, IllegalAccessException
	{
		Field field = instance.getClass().getDeclaredFields()[index];
		field.setAccessible(true);
		return (T)field.get(instance);
	}
	
	public static void setField(Class<?> class1, Object instance, int fieldIndex, Object value) throws IllegalArgumentException, IllegalAccessException
	{
		Field field = class1.getDeclaredFields()[fieldIndex];
		field.setAccessible(true);
		field.set(instance, value);
	}
	
	public static void setField(int i,Class<?> class1, Object instance, int fieldIndex, Object value) throws IllegalArgumentException, IllegalAccessException
	{
		Field field = class1.getFields()[fieldIndex];
		field.setAccessible(true);
		field.set(instance, value);
	}
	
	public static Random getRandom()
	{
		return random;
	}
	
	public static boolean isDye(ItemStack par1)
	{
		String name = ore.getOreName(ore.getOreID(par1));
		if(name.startsWith("dye"))
		{
			return true;
		}
		return false;
	}
	
	public static EnumColor getColorFromItemStack(ItemStack par1)
	{
		String name = ore.getOreName(ore.getOreID(par1));
		name = name.substring(3);
		return EnumColor.valueOf(name);
	}
}
