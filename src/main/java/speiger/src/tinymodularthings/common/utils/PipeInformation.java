package speiger.src.tinymodularthings.common.utils;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import speiger.src.api.items.InfoStack;
import speiger.src.api.language.LanguageRegister;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;

public class PipeInformation
{
	/**
	 * Energy of the Pipe that it can transfer
	 */
	int energy;
	
	/**
	 * How much Fluid it can transfer
	 */
	int fluid;
	
	/**
	 * How Items he can transfer
	 */
	int item;
	
	/**
	 * Name of the Pipe
	 */
	String name;
	
	/**
	 * Texture String
	 */
	String texture;
	
	/**
	 * Texture of the pipe and the Texture of the Pipe with the Arrow
	 */
	IIcon[] pipeTexture = new IIcon[2];
	
	public PipeInformation()
	{
		energy = 0;
		fluid = 0;
		item = 0;
		name = "";
		texture = "";
	}
	
	public PipeInformation(int energyT, int fluidT, int itemT, String nameT, String textureT)
	{
		energy = energyT;
		fluid = fluidT;
		item = itemT;
		name = nameT;
		texture = textureT;
	}
	
	public int getEnergyTransferlimit()
	{
		return energy;
	}
	
	public int getFluidTransferlimit()
	{
		return fluid;
	}
	
	public int getItemTransferlimit()
	{
		return item;
	}
	
	public String getItemName()
	{
		return name;
	}
	
	public void addInformation(List par1)
	{
		par1.add(LanguageRegister.getLanguageName(new InfoStack(), "transfer.energy.limit", TinyModularThings.instance) + ": " + energy + " MJ");
		par1.add(LanguageRegister.getLanguageName(new InfoStack(), "transfer.fluid.limit", TinyModularThings.instance) + ": " + fluid + " mB");
		par1.add(LanguageRegister.getLanguageName(new InfoStack(), "transfer.item.limit", TinyModularThings.instance) + ": " + item + " Items");
		par1.add(LanguageRegister.getLanguageName(new InfoStack(), "pipe.transfer.require", TinyModularThings.instance));
		par1.add("No Use Yet!");
	}
	
	public IIcon getDirectionIcon()
	{
		return pipeTexture[1];
	}
	
	public IIcon getPipeIcon()
	{
		return pipeTexture[0];
	}
	
	public void updateIcon(IIconRegister par1)
	{
		pipeTexture[0] = par1.registerIcon(TinyModularThingsLib.ModID.toLowerCase() + ":pipes/" + texture);
		pipeTexture[1] = par1.registerIcon(TinyModularThingsLib.ModID.toLowerCase() + ":pipes/" + texture + "_face");
	}
	
}
