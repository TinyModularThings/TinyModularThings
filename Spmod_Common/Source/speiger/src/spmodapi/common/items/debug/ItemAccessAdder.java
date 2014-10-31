package speiger.src.spmodapi.common.items.debug;

import java.util.List;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.items.SpmodItem;
import speiger.src.spmodapi.common.util.data.AccessConfig;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemAccessAdder extends SpmodItem
{
	public ItemAccessAdder(int id)
	{
		super(id);
		this.setMaxStackSize(1);
		this.setCreativeTab(APIUtils.tabCrafing);
	}
	
	
	@Override
	public void registerItems(int par1, SpmodMod par0)
	{	
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod par0)
	{
		return "Inventory Accesser Debug Tool";
	}


	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add("RightClick for TileEntity Name and Also Remove it From the List if it is inside");
		par3List.add("Sneak RightClick to add the TileEntity to the List");
	}


	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		MinecraftServer server =  FMLCommonHandler.instance().getMinecraftServerInstance();
		if(server != null && !world.isRemote)
		{
			ServerConfigurationManager manager = server.getConfigurationManager();
			if(manager != null)
			{
				if(manager.isPlayerOpped(player.username) || AccessConfig.everyOneCanUse)
				{
					TileEntity tile = world.getBlockTileEntity(x, y, z);
					if(tile != null)
					{
						String name = tile.getClass().getSimpleName();
						if(player.isSneaking())
						{
							AccessConfig.addTileEntity(name);
							player.sendChatToPlayer(LanguageRegister.createChatMessage("Added TileEntity to Inventory Accessor"));
							player.sendChatToPlayer(LanguageRegister.createChatMessage("TileEntity name: "+name));
						}
						else
						{
							AccessConfig.removeTileEntity(name);
							player.sendChatToPlayer(LanguageRegister.createChatMessage("Removed TileEntity from Inventory Accessor"));
							player.sendChatToPlayer(LanguageRegister.createChatMessage("TileEntity name: "+name));
						}
						return false;
					}
				}
			}
		}
		return false;
	}
}
