package speiger.src.api.client.render;

import net.minecraft.nbt.NBTTagCompound;

public class DisplayText
{
	int x;
	int y;
	String text;
	int color;
	//x = 75
	//y = 55
	private DisplayText()
	{
	}
	
	public DisplayText(int par1, int par2, String par3, int par4)
	{
		x = par1;
		y = par2;
		text = par3;
		color = par4;
	}
	
	public String getText()
	{
		return text;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public void addLetter(char par1)
	{
		text += par1;
	}
	
	public void removeLetter()
	{
		if(text.length() <= 0)
		{
			return;
		}
		text = text.substring(0, text.length() - 1);
	}
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
	
	public void setColor(int color)
	{
		this.color = color;
	}
	
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		text = nbt.getString("UTF");
		x = nbt.getInteger("xCoord");
		y = nbt.getInteger("yCoord");
		color = nbt.getInteger("Color");
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("UTF", text);
		nbt.setInteger("xCoord", x);
		nbt.setInteger("yCoord", y);
		nbt.setInteger("Color", color);
	}
	
	public static DisplayText loadFromNBT(NBTTagCompound nbt)
	{
		DisplayText text = new DisplayText();
		text.readFromNBT(nbt);
		return text;
	}

	public int getColor()
	{
		return color;
	}

	public void setText(String par1)
	{
		text = par1;
	}
}
