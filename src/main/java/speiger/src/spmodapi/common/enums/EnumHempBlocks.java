package speiger.src.spmodapi.common.enums;

import net.minecraft.block.material.Material;
import speiger.src.spmodapi.common.blocks.hemp.BlockHempDeko.HempBlockInformation;

public enum EnumHempBlocks
{
	BasicHemp(Material.cloth, true, false, "HempBlock", "basic"), 
	BrickHemp(Material.rock, false, false, "HempBrick", "brick"), 
	PlatedHemp(Material.cloth, true, false, "NiceHempBlock", "plated"), 
	PlatedHempBrick(Material.rock, false, false, "NiceHempBrick", "plated.brick"), 
	SaveBasicHemp(Material.cloth, true, true, "HempBlock", "basic"), 
	SaveBrickHemp(Material.rock, false, true, "HempBrick", "brick"), 
	SavePlatedHemp(Material.cloth, true, true, "NiceHempBlock", "plated"), 
	SavePlatedHempBrick(Material.rock, false, true, "NiceHempBrick", "plated.brick");
	
	Material material;
	boolean axe;
	boolean monster;
	String tname;
	String dname;
	
	private EnumHempBlocks(Material par1, boolean hemp, boolean special, String textureName, String name)
	{
		material = par1;
		axe = hemp;
		monster = special;
		tname = textureName;
		dname = name;
	}
	
	public HempBlockInformation getInfos()
	{
		return new HempBlockInformation(material, axe, monster, tname, dname);
	}
}
