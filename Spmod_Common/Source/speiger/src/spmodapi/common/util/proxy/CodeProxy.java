package speiger.src.spmodapi.common.util.proxy;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
	
	public static <T> T getField(Class<T> result, Class<?> search, Object instance) throws IllegalArgumentException, IllegalAccessException
	{
		Field[] fields = search.getClass().getDeclaredFields();
		for(Field field : fields)
		{
			if(field != null && field.getClass().getSimpleName().equalsIgnoreCase(result.getSimpleName()))
			{
				field.setAccessible(true);
				return (T)field.get(instance);
			}
		}
		return null;
	}
	
	public static void setField(Class<?> searchingClass, Class<?> instanceClass, Object instance, Object value) throws IllegalArgumentException, IllegalAccessException
	{
		Field[] fields = instanceClass.getDeclaredFields();
		for(Field field : fields)
		{
			if(field != null && field.getClass().getSimpleName().equalsIgnoreCase(searchingClass.getSimpleName()))
			{
				field.setAccessible(true);
				field.set(instance, value);
				break;
			}
		}
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
	
	public static String getListAsSimpleName(List par1)
	{
		String result = "";
		for(int i = 0;i<par1.size();i++)
		{
			result += par1.get(i).getClass().getSimpleName();
			
			if(i == par1.size()-1)
			{
				break;
			}
			
			result += ":";
		}
		
		return result;
	}
	
	public static String getListAsSimpleName(Set par1)
	{
		String result = "";
		for(Object obj : par1)
		{
			result += obj.getClass().getSimpleName()+":";
		}
		
		return result;
	}
}
