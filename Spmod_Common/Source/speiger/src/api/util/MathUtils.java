package speiger.src.api.util;

public class MathUtils
{
	public static String getTicksInTime(int ticks)
	{
		int TotalTime = ticks / 20;
		
		int Sekunde = TotalTime%60;
		int Minute = (TotalTime / 60)%60;
		int Stunde = ((TotalTime / 60) / 60)%24;
		int Tag = (((TotalTime / 60) / 60) / 24)%30;
		
		return "Days: "+Tag+" Hours: "+Stunde+" Minutes: "+Minute+" Seconds: "+Sekunde;
	}
}
