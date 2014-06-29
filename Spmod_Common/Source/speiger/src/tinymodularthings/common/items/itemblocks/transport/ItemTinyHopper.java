package speiger.src.tinymodularthings.common.items.itemblocks.transport;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.util.SpmodMod;
import speiger.src.tinymodularthings.common.blocks.transport.TinyHopper;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.items.core.TinyItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTinyHopper extends TinyItem
{
	public ItemTinyHopper(int par1)
	{
		super(par1);
		setHasSubtypes(true);
		setCreativeTab(CreativeTabs.tabFood);
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int i = 0; i < 9; i++)
		{
			par3List.add(new ItemStack(par1, 1, i));
		}
	}
	
	public void registerItems(int id, SpmodMod par0)
	{
	}
	
	public String getDisplayName(ItemStack par1, SpmodMod Start)
	{
		return "Basic Tiny Hopper";
	}
	
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
	{
		int i1 = par3World.getBlockId(par4, par5, par6);
		
		if ((i1 == Block.snow.blockID) && ((par3World.getBlockMetadata(par4, par5, par6) & 0x7) < 1))
		{
			par7 = 1;
		}
		else if ((i1 != Block.vine.blockID) && (i1 != Block.tallGrass.blockID) && (i1 != Block.deadBush.blockID) && ((Block.blocksList[i1] == null) || (!Block.blocksList[i1].isBlockReplaceable(par3World, par4, par5, par6))))
		{
			if (par7 == 0)
			{
				par5--;
			}
			
			if (par7 == 1)
			{
				par5++;
			}
			
			if (par7 == 2)
			{
				par6--;
			}
			
			if (par7 == 3)
			{
				par6++;
			}
			
			if (par7 == 4)
			{
				par4--;
			}
			
			if (par7 == 5)
			{
				par4++;
			}
		}
		
		if (par1ItemStack.stackSize == 0)
		{
			return false;
		}
		if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack))
		{
			return false;
		}
		if ((par5 == 255) && (Block.blocksList[par3World.getBlockId(par4, par5, par6)].blockMaterial.isSolid()))
		{
			return false;
		}
		
		if (par3World.setBlock(par4, par5, par6, TinyBlocks.transportBlock.blockID, 10, 3))
		{
			TileEntity tile = par3World.getBlockTileEntity(par4, par5, par6);
			if ((tile != null) && ((tile instanceof TinyHopper)))
			{
				TinyHopper tiny = (TinyHopper) tile;
				tiny.setMode(par1ItemStack.getItemDamage() + 1);
				tiny.updateMode();
				Block.blocksList[par3World.getBlockId(par4, par5, par6)].onBlockPlacedBy(par3World, par4, par5, par6, par2EntityPlayer, par1ItemStack);
				
				if (par2EntityPlayer.isSneaking())
				{
					tiny.setRotation(ForgeDirection.getOrientation(par7).getOpposite().ordinal());
				}
			}
		}
		
		return true;
	}
}
