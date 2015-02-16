package speiger.src.compactWindmills.common.utils;

import java.util.ArrayList;

import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.compactWindmills.CompactWindmills;
import speiger.src.spmodapi.common.util.TextureEngine;

public enum WindmillType
{
	ELV(8, "ELV"),
	LV(32, "LV"),
	MV(128, "MV"),
	HV(512, "HV"),
	EV(2048, "EV"),
	Nothing(0);
	
	int output;
	String texture;
	
	private WindmillType(int par1)
	{
		output = par1;
		texture = "Nothing";
	}
	
	private WindmillType(int par1, String par3)
	{
		this(par1);
		texture = par3;
	}
	
	public int getOutput()
	{
		return output;
	}
	
	public String getTextureName()
	{
		return texture;
	}
	
	public static WindmillType[] getValidValues()
	{
		ArrayList<WindmillType> list = new ArrayList<WindmillType>();
		for(WindmillType type : values())
		{
			if(type != type.Nothing)
			{
				list.add(type);
			}
		}
		return list.toArray(new WindmillType[list.size()]);
	}
	
	public static void registerTextures(TextureEngine par1)
	{
		WindmillType[] types = getValidValues();
		for(int i = 0;i<types.length;i++)
		{
			par1.registerTexture(new BlockStack(CompactWindmills.windmill, i), types[i].getTextureName()+"_bottom", types[i].getTextureName()+"_front", types[i].getTextureName()+"_side");
		}
	}
}
