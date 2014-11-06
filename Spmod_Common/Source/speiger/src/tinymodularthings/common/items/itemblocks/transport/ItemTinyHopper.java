package speiger.src.tinymodularthings.common.items.itemblocks.transport;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.common.registry.helpers.SpmodMod;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.tinymodularthings.common.blocks.transport.TinyHopper;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.items.core.TinyPlacerItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTinyHopper extends TinyPlacerItem
{
	HopperType type;
	
	public ItemTinyHopper(int par1, HopperType par2)
	{
		super(par1);
		type = par2;
		setHasSubtypes(true);
		setCreativeTab(CreativeTabs.tabFood);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add(type + " Hopper");
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for(int i = 0;i < 9;i++)
		{
			par3List.add(new ItemStack(par1, 1, i));
		}
	}
	
	
	
	
	
	@Override
	public void onAfterPlaced(World world, int x, int y, int z, int side, EntityPlayer player, ItemStack item)
	{
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if(tile != null && tile instanceof TinyHopper)
		{
			TinyHopper tiny = (TinyHopper)tile;
			tiny.setMode(item.getItemDamage() + 1);
			tiny.updateMode();
			if(player.isSneaking())
			{
				tiny.setRotation(ForgeDirection.getOrientation(side).getOpposite().ordinal());
			}
			this.removeItem(player, item);
		}
	}
	
	public static enum HopperType
	{
		Items,
		AdvItems,
		Fluids,
		AdvFluids,
		Energy,
		AdvancedEnergy;
	}
	
	@Override
	public BlockStack getBlockToPlace(int meta)
	{
		return new BlockStack(TinyBlocks.transportBlock, 10 + type.ordinal());
	}
	
	@Override
	public String getName(ItemStack par1)
	{
		return "Tiny Hopper";
	}
}
