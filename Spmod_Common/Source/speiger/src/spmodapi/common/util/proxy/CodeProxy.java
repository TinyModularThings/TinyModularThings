package speiger.src.spmodapi.common.util.proxy;

import java.lang.reflect.Field;
import java.util.Random;

import cpw.mods.fml.common.FMLLog;

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
