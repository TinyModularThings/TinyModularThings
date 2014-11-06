package speiger.src.tinymodularthings.common.items.tools;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.proxy.LangProxy;
import speiger.src.tinymodularthings.common.items.core.TinyItem;

public class ItemTinyInfo extends TinyItem
{
	
	public ItemTinyInfo(int par1)
	{
		super(par1);
		setMaxStackSize(1);
		setCreativeTab(CreativeTabs.tabFood);
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		if(!world.isRemote)
		{
			TileEntity tile = world.getBlockTileEntity(x, y, z);
			if(tile != null && tile instanceof AdvTile)
			{
				AdvTile adv = (AdvTile)tile;
				List<String> data = new ArrayList<String>();
				adv.loadInformation(data);
				if(data.size() > 0)
				{
					player.sendChatToPlayer(LangProxy.getText("Following infos:"));
					for(String key : data)
					{
						player.sendChatToPlayer(LangProxy.getText(key));
					}
					return true;
				}
				else
				{
					player.sendChatToPlayer(LangProxy.getText("No Problems / Debug Infos"));
				}
			}
		}
		
		return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
	}

	@Override
	public String getName(ItemStack par1)
	{
		return "Debug Tool";
	}
	
	
	
}
