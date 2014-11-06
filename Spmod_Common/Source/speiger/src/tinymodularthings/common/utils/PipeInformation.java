package speiger.src.tinymodularthings.common.utils;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.util.Icon;
import speiger.src.spmodapi.common.util.TextureEngine;

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
	public Block block;
	
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
	
	public void injectBlock(Block par1)
	{
		block = par1;
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
		par1.add("Energy Transferlimit: " + energy + " MJ");
		par1.add("Fluid Transferlimit: " + fluid + " mB");
		par1.add("Item Transferlimit: " + item + " Items");
		par1.add("Require Tiny Hopper For work");
		par1.add("No Use Yet!");
	}
	
	public Icon getDirectionIcon()
	{
		return TextureEngine.getTextures().getTexture(block, 1);
	}
	
	public Icon getPipeIcon()
	{
		return TextureEngine.getTextures().getTexture(block, 0);
	}
	
	public void updateIcon(TextureEngine par1)
	{
		par1.registerTexture(block, texture, texture+"_face");
	}
	
}
