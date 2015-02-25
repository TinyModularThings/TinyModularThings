package speiger.src.tinymodularthings.common.blocks.storage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import speiger.src.api.common.data.nbt.NBTHelper;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.items.core.ItemBlockTinyChest;

public class ItemBlockStorage extends ItemBlockTinyChest
{
	public ItemBlockStorage(int par1)
	{
		super(par1);
		setHasSubtypes(true);
	}
	
	@Override
	public int getMetadata(int par1)
	{
		return par1 > 4 ? 4 : par1;
	}

	@Override
	public BlockStack getBlockToPlace(int meta)
	{
		return new BlockStack(getBlockID(), meta);
	}
	
	public static ItemStack createTinyBarrel(int meta)
	{
		NBTTagCompound data = new NBTTagCompound();
		data.setInteger("Metadata", meta);
		ItemStack result = new ItemStack(TinyBlocks.storageBlock, 1, 4);
		result.setTagInfo("BarrelMeta", data);
		return result;
	}

	@Override
	public boolean onAfterPlaced(World world, int x, int y, int z, int side, EntityPlayer player, ItemStack item)
	{
		if(item.getItemDamage() == 4)
		{
			int meta = NBTHelper.getTag(item, "BarrelMeta").getInteger("Metadata");
			TileEntity tile = world.getBlockTileEntity(x, y, z);
			if(tile != null && tile instanceof TinyBarrel)
			{
				TinyBarrel tiny = (TinyBarrel)tile;
				tiny.setMax((1 + meta) * 16);
			}
		}
		return true;
	}

	@Override
	public String getName(ItemStack par1)
	{
		switch(par1.getItemDamage())
		{
			case 0: return "TinyChest";
			case 1: return "TinyTank";
			case 2: return "Advanced TinyChest";
			case 3: return "Advanced TinyTank";
			case 4: return "Tiny Barrel";
		}
		return null;
	}
	
}
