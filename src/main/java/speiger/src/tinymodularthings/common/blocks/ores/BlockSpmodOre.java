package speiger.src.tinymodularthings.common.blocks.ores;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import speiger.src.api.blocks.BlockStack;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.utils.TinyTextureHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSpmodOre extends Block
{
	
	public BlockSpmodOre()
	{
		super(Material.rock);
		setHardness(4.0F);
		setResistance(4.0F);
		setCreativeTab(CreativeTabs.tabFood);
		setHarvestLevel("pickaxe", 0, 1);
		setHarvestLevel("pickaxe", 1, 1);
		setHarvestLevel("pickaxe", 2, 2);
		setHarvestLevel("pickaxe", 3, 2);
		setHarvestLevel("pickaxe", 5, 3);
		OreDictionary.registerOre("oreCopper", new ItemStack(this));
		OreDictionary.registerOre("oreTin", new ItemStack(this, 1, 1));
		OreDictionary.registerOre("oreSilver", new ItemStack(this, 1, 2));
		OreDictionary.registerOre("oreLead", new ItemStack(this, 1, 3));
	}
	
	@Override
	public Item getItemDropped(int meta, Random par2Random, int dmg)
	{
		if (meta == 5)
		{
			return TinyItems.IridiumDrop;
		}
		return super.getItemDropped(meta, par2Random, dmg);
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
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		return new BlockStack(world, x, y, z).asItemStack();
	}
	
	@Override
	public int quantityDropped(int meta, int fortune, Random random)
	{
		return 1;
	}
	
	@Override
	public float getBlockHardness(World par1World, int par2, int par3, int par4)
	{
		return 4.0F;
	}
	
	IIcon[] oreTexture = new IIcon[6];
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2)
	{
		return oreTexture[par2];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1)
	{
		oreTexture[0] = par1.registerIcon(TinyTextureHelper.getTextureStringFromName("ores/oreCopper"));
		oreTexture[1] = par1.registerIcon(TinyTextureHelper.getTextureStringFromName("ores/oreTin"));
		oreTexture[2] = par1.registerIcon(TinyTextureHelper.getTextureStringFromName("ores/oreSilver"));
		oreTexture[3] = par1.registerIcon(TinyTextureHelper.getTextureStringFromName("ores/oreLead"));
		oreTexture[4] = par1.registerIcon(TinyTextureHelper.getTextureStringFromName("ores/oreBauxit"));
		oreTexture[5] = par1.registerIcon(TinyTextureHelper.getTextureStringFromName("ores/oreIridium"));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3)
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