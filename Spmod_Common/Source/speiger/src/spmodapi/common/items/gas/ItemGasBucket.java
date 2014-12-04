package speiger.src.spmodapi.common.items.gas;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.items.core.SpmodPlacerItem;

public class ItemGasBucket extends SpmodPlacerItem
{
	
	public ItemGasBucket(int par1)
	{
		super(par1);
		this.setContainerItem(Item.bucketEmpty);
		this.setMaxStackSize(1);
		this.setCreativeTab(APIUtils.tabGas);
	}

	@Override
	public String getName(ItemStack par1)
	{
		return "Animal Gas Bucket";
	}

	@Override
	public BlockStack getBlockToPlace(int meta)
	{
		return new BlockStack(APIBlocks.animalGas, 10);
	}
	
	
	
}
