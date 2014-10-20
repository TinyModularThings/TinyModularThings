package speiger.src.api.util;

import java.util.ArrayList;

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

	public static int[] getArrayFromList(ArrayList<Integer> ints)
	{
		int[] result = new int[ints.size()];
		for(int i = 0;i<result.length;i++)
		{
			result[i] = ints.get(i).intValue();
		}
		return result;
	}
}
