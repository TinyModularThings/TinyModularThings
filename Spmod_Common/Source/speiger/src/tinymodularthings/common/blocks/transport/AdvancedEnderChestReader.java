package speiger.src.tinymodularthings.common.blocks.transport;

import speiger.src.api.blocks.BlockStack;
import speiger.src.spmodapi.common.handler.PlayerHandler;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;

public class AdvancedEnderChestReader extends EnderChestReader
{

	@Override
	public InventoryEnderChest findEnderChest()
	{
		for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++)
		{
			int x = xCoord + ForgeDirection.getOrientation(i).offsetX;
			int y = yCoord + ForgeDirection.getOrientation(i).offsetY;
			int z = zCoord + ForgeDirection.getOrientation(i).offsetZ;
			
			TileEntity tile = worldObj.getBlockTileEntity(x, y, z);
			if (tile != null && tile instanceof TileEntityEnderChest)
			{
				return PlayerHandler.getEnderChestFromUsername(owner);
			}
		}
		return null;
	}

	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		TextureEngine engine = TextureEngine.getTextures();
		Icon result = engine.getTexture(new BlockStack(TinyBlocks.transportBlock, 4), 0);
		if(engine.isMissingTexture(result))
		{
			result = Block.portal.getIcon(0, 0);
		}
		return result;	
	}

	@Override
	public void registerIcon(TextureEngine par1)
	{
		par1.registerTexture(new BlockStack(TinyBlocks.transportBlock, 4), "advEnderChestReader");
	}
	
	
	
}
