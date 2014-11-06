package speiger.src.spmodapi.common.enums;

import net.minecraft.block.material.Material;
import speiger.src.spmodapi.common.blocks.hemp.BlockHempDeko.HempBlockInformation;

public enum EnumHempBlocks
{
	BasicHemp(Material.cloth, true, false, "HempBlock", "Hemp Block"),
	BrickHemp(Material.rock, false, false, "HempBrick", "Hemp Brick"),
	PlatedHemp(Material.cloth, true, false, "NiceHempBlock", "Plated Hemp Block"),
	PlatedHempBrick(Material.rock, false, false, "NiceHempBrick", "plated.brick"),
	SaveBasicHemp(Material.cloth, true, true, "HempBlock", "Plated Hemp Brick"),
	SaveBrickHemp(Material.rock, false, true, "HempBrick", "Hemp Brick"),
	SavePlatedHemp(Material.cloth, true, true, "NiceHempBlock", "Plated Hemp Block"),
	SavePlatedHempBrick(Material.rock, false, true, "NiceHempBrick", "Plated Hemp Brick");
	
	
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
