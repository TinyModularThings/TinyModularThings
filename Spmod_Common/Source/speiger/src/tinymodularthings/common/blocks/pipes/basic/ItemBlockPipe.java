package speiger.src.tinymodularthings.common.blocks.pipes.basic;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.common.data.nbt.NBTHelper;
import speiger.src.api.common.utils.WorldReading;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.tinymodularthings.common.interfaces.IBasicPipe;
import speiger.src.tinymodularthings.common.items.core.ItemBlockTinyChest;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockPipe extends ItemBlockTinyChest
{
	
	public ItemBlockPipe(int par1)
	{
		super(par1);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1, EntityPlayer par2, List par3, boolean par4)
	{
		BlockStack stack = new BlockStack(par1);
		if (stack.getBlock() instanceof IBasicPipe)
		{
			IBasicPipe pipe = (IBasicPipe) stack.getBlock();
			if (pipe != null && pipe.getItemInformation(par1) != null)
			{
				pipe.getItemInformation(par1).addInformation(par3);
			}
		}
	}
	
	@Override
	public BlockStack getBlockToPlace(int meta)
	{
		return new BlockStack(getBlockID());
	}
	
	

	@Override
	public boolean onAfterPlaced(World world, int x, int y, int z, int side, EntityPlayer player, ItemStack item)
	{
		world.setBlockMetadataWithNotify(x, y, z, side, 3);
		return true;
	}

	@Override
	public String getName(ItemStack par1)
	{
		BlockStack stack = new BlockStack(par1);
		if(stack.getBlock() instanceof IBasicPipe)
		{
			IBasicPipe pipe = (IBasicPipe)stack.getBlock();
			if(pipe != null && pipe.getItemInformation(par1) != null)
			{
				return pipe.getItemInformation(par1).getItemName();
			}
		}
		return null;
	}
}
