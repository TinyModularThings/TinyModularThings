package speiger.src.spmodapi.common.enums;

public enum EnumColor
{
	
	BLACK(15, 0, 0x333333, "black"),
	RED(14, 1, 0xff0000, "red"),
	GREEN(13, 2, 0x009900, "green"),
	BROWN(12, 3, 0x553300, "brown"),
	BLUE(11, 4, 0x3333ff, "blue"),
	PURPLE(10, 5, 0x9900ff, "purple"),
	CYAN(9, 6, 0x00ffff, "cyan"),
	LIGHTGRAY(8, 7, 0x666666, "lightgray"),
	GRAY(7, 8, 0x434343, "gray"),
	PINK(6, 9, 0xe881a8, "pink"),
	LIME(5, 10, 0x41ff34, "lime"),
	YELLOW(4, 11, 0xffff00, "yellow"),
	LIGHTBLUE(3, 12, 0x6666ff, "lightblue"),
	MAGENTA(2, 13, 0xff0099, "magenta"),
	ORANGE(1, 14, 0xff9900, "orange"),
	WHITE(0, 15, 0xffffff, "white"),
	Nothing(-1, -1, 0xf0f0f0, "nothing");
	
	Integer hex;
	int wool;
	int dye;
	String name;
	
	private EnumColor(int Wool, int Dye, Integer Hex, String Name)
	{
		wool = Wool;
		dye = Dye;
		hex = Hex;
		name = Name;
	}
	
	public static EnumColor getColorFromWool(int id)
	{
		for(EnumColor value : values())
		{
			if(value.wool == id)
			{
				return value;
			}
		}
		return Nothing;
	}
	
	public static EnumColor getFromHex(int hex)
	{
		for(EnumColor value : values())
		{
			if(value.hex.intValue() == hex)
			{
				return value;
			}
		}
		return Nothing;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getNameBig()
	{
		String copy = name.substring(1);
		copy = name.substring(0, 1).toUpperCase() + copy;
		return copy;
	}
	
	public int getAsWool()
	{
		return wool;
	}
	
	public int getAsDye()
	{
		return dye;
	}
	
	public Integer getAsHex()
	{
		return hex;
	}
	
	// Copied from PowerCraft
	public static class SpmodColor
	{
		public double red;
		public double green;
		public double blue;
		
		public SpmodColor()
		{
			red = green = blue = 1D;
		}
		
		public SpmodColor(double r, double g, double b)
		{
			red = r;
			green = g;
			blue = b;
		}
		
		public SpmodColor(int hex)
		{
			red = red(hex);
			green = green(hex);
			blue = blue(hex);
		}
		
		public SpmodColor(EnumColor par1)
		{
			this(par1.getAsHex());
		}
		
		public SpmodColor(SpmodColor par1)
		{
			red = par1.red;
			green = par1.green;
			blue = par1.blue;
		}
		
		public int getHex()
		{
			int r255 = (int)Math.round(red * 255) & 0xff;
			int g255 = (int)Math.round(green * 255) & 0xff;
			int b255 = (int)Math.round(blue * 255) & 0xff;
			return r255 << 16 | g255 << 8 | b255;
		}
		
		public static double red(int hex)
		{
			return (1D / 255D) * ((hex & 0xff0000) >> 16);
		}
		
		public static double green(int hex)
		{
			return (1D / 255D) * ((hex & 0x00ff00) >> 8);
		}
		
		public static double blue(int hex)
		{
			return (1D / 255D) * ((hex & 0x0000ff));
		}
		
		public SpmodColor setTo(int hex)
		{
			red = red(hex);
			green = green(hex);
			blue = blue(hex);
			return this;
		}
		
		public void setTo(double cr, double cg, double cb)
		{
			red = cr;
			green = cg;
			blue = cb;
		}
		
		public static SpmodColor fromHex(int hex)
		{
			return new SpmodColor().setTo(hex);
		}
		
		public EnumColor getColor()
		{
			return EnumColor.getFromHex(getHex());
		}
		
		public SpmodColor add(double par0)
		{
			red *= par0;
			green *= par0;
			blue *= par0;
			return this;
		}
		
		public SpmodColor mixWith(EnumColor color)
		{
			return mixWith(new SpmodColor(color));
		}
		
		public SpmodColor mixWith(SpmodColor color)
		{
			return new SpmodColor((red + color.red) / 2D, (green + color.green) / 2D, (blue + color.blue) / 2D);
		}
	}
	
}
