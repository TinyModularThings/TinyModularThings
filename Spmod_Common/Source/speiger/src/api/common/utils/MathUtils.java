package speiger.src.api.common.utils;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

import com.google.common.math.DoubleMath;

public class MathUtils
{
	public static String getTicksInTime(int ticks)
	{
		int TotalTime = ticks / 20;
		
		int Sekunde = TotalTime % 60;
		int Minute = (TotalTime / 60) % 60;
		int Stunde = ((TotalTime / 60) / 60) % 24;
		int Tag = (((TotalTime / 60) / 60) / 24) % 30;
		
		return "Days: " + Tag + " Hours: " + Stunde + " Minutes: " + Minute + " Seconds: " + Sekunde;
	}
	
	public static int getSekInRotorTicks(int sek)
	{
		int copy = sek * 20;
		return copy / 64;
	}
	
	public static String getTicksInTimeShort(int ticks)
	{
		int TotalTime = ticks / 20;
		
		if(TotalTime <= 0)
		{
			return "0";
		}
		
		String Sekunde = ""+TotalTime % 60;
		String Minute = ""+(TotalTime / 60) % 60;
		String Stunde = ""+((TotalTime / 60) / 60) % 24;
		String Tag = ""+(((TotalTime / 60) / 60) / 24) % 30;
		if(Sekunde.length() == 1)
		{
			Sekunde = "0" + Sekunde;
		}
		if(Minute.length() == 1)
		{
			Minute = "0" + Minute;
		}
		if(Stunde.length() == 1)
		{
			Stunde = "0" + Stunde;
		}
		if(Tag.length() == 1)
		{
			Tag = "0" + Tag;
		}
		
		String result = "";
		if(Integer.valueOf(Tag) > 0)
		{
			result += Tag+":";
		}
		if(Integer.valueOf(Tag) > 0 || Integer.valueOf(Stunde) > 0)
		{
			result += Stunde+":";
		}
		if(Integer.valueOf(Tag) > 0 || Integer.valueOf(Stunde) > 0 || Integer.valueOf(Minute) > 0)
		{
			result += Minute+":";
		}
		if(Integer.valueOf(Tag) > 0 || Integer.valueOf(Stunde) > 0 || Integer.valueOf(Minute) > 0 || Integer.valueOf(Sekunde) > 0)
		{
			result+= Sekunde+"";
		}
		return result;
	}

	public static int[] getArrayFromList(List<Integer> ints)
	{
		int[] result = new int[ints.size()];
		for(int i = 0;i<result.length;i++)
		{
			result[i] = ints.get(i).intValue();
		}
		return result;
	}

	public static List<Integer> getListFromArray(int[] par1)
	{
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(int i : par1)
		{
			result.add(i);
		}
		return result;
	}

	public static int[] getAdjustedMeta(int end, int provide, int requested)
	{
		if(end + provide < requested)
		{
			return new int[]{(end+provide), 0};
		}
		return new int[]{requested, ((end+provide)-requested)};
	}
	
	public static int[] getEqualMeta(int key, int value)
	{
		if(key+value == 0)
		{
			return new int[]{0, 0};
		}
		if(key+value == 1)
		{
			return new int[]{1, 0};
		}
		
		if(areTwoNumbersDivideableToInteger(key, value))
		{
			int result = (key+value)/2;
			return new int[]{result, result};
		}
		double end = ((double)key + (double)value) / 2;
		return new int[]{DoubleMath.roundToInt(end, RoundingMode.DOWN), DoubleMath.roundToInt(end, RoundingMode.UP)};
	}
	
	public static boolean areTwoNumbersDivideableToInteger(int key, int value)
	{
		double end = ((double)key + (double)value) / 2;
		if((end+"").contains(".") || (end+"").contains(","))
		{
			return false;
		}
		return true;
	}

	public static void setEntityRotation(Entity effect, float rotation, float speed)
	{
	        float f1 = MathHelper.cos(rotation);
	        float f2 = MathHelper.sin(rotation);
	        double d0 = speed * (double)f1 + speed * (double)f2;
	        double d2 = speed * (double)f1 - speed * (double)f2;
	        effect.motionX = d0;
	        effect.motionZ = d2;
	}
}
