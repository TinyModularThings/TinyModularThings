package speiger.src.tinymodularthings.common.blocks.ores;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import speiger.src.spmodapi.common.blocks.cores.SpmodBlockBase;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSpmodOre extends SpmodBlockBase
{
	
	public BlockSpmodOre(int par1)
	{
		super(par1, Material.rock);
		setHardness(4.0F);
		setResistance(4.0F);
		setCreativeTab(CreativeTabs.tabFood);
		MinecraftForge.setBlockHarvestLevel(this, 0, "pickaxe", 1);
		MinecraftForge.setBlockHarvestLevel(this, 1, "pickaxe", 1);
		MinecraftForge.setBlockHarvestLevel(this, 2, "pickaxe", 2);
		MinecraftForge.setBlockHarvestLevel(this, 3, "pickaxe", 2);
		MinecraftForge.setBlockHarvestLevel(this, 5, "pickaxe", 3);
		OreDictionary.registerOre("oreCopper", new ItemStack(this));
		OreDictionary.registerOre("oreTin", new ItemStack(this, 1, 1));
		OreDictionary.registerOre("oreSilver", new ItemStack(this, 1, 2));
		OreDictionary.registerOre("oreLead", new ItemStack(this, 1, 3));
	}
	
	@Override
	public int idDropped(int meta, Random par2Random, int dmg)
	{
		if (meta == 5)
		{
			return TinyItems.IridiumDrop.itemID;
		}
		return blockID;
	}
	
	@Override
	public int damageDropped(int par1)
	{
		if (par1 == 5)
		{
			return 0;
		}
		return par1;
	}
	
	@Override
	public int quantityDropped(int meta, int fortune, Random random)
	{
		if(meta == 5)
		{
			int j = random.nextInt(fortune + 2) - 1;
			if(j < 0)
			{
				j = 0;
			}
			return 1 + j;
		}
		return 1;
	}
	
	@Override
	public float getBlockHardness(int meta)
	{
		return 4F;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int par1, int par2)
	{
		return TextureEngine.getTextures().getTexture(this, par2);
	}
	
	@Override
	public void registerTextures(TextureEngine par1)
	{
		String[] textures = new String[]{"oreCopper", "oreTin", "oreSilver", "oreLead", "oreBauxit", "oreIridium"};
		par1.registerTexture(this, textures);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3)
	{
		for (int i = 0; i < 6; i++)
		{
			if (i == 4)
			{
				continue;
			}
			
			par3.add(new ItemStack(par1, 1, i));
		}
	}
	
}