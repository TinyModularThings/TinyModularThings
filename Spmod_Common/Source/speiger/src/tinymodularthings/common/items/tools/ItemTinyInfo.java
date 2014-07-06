package speiger.src.tinymodularthings.common.items.tools;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import speiger.src.api.blocks.BlockHelper;
import speiger.src.api.items.InfoStack;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.nbt.NBTHelper;
import speiger.src.api.util.SpmodMod;
import speiger.src.tinymodularthings.TinyModularThings;
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
	public void registerItems(int id, SpmodMod par0)
	{
		
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod Start)
	{
		return "Player Editor";
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack par1, World par2, EntityPlayer par3)
	{
		if (!par2.isRemote)
		{
			NBTTagCompound nbt = NBTHelper.getTinyChestTagCompound(par1);
			
			if (par3.isSneaking())
			{
				int mode = nbt.getInteger("Mode");
				
				mode++;
				
				if (mode >= getMaxMode())
				{
					mode = 0;
				}
				nbt.setInteger("Mode", mode);
				par3.sendChatToPlayer(LanguageRegister.createChatMessage(LanguageRegister.getLanguageName(new InfoStack(), getModeFromKey(mode), TinyModularThings.instance)));
			}
			else
			{
				int mode = nbt.getInteger("Mode");
				NBTTagCompound player = par3.getEntityData();
				NBTTagCompound data = NBTHelper.getTinyChestTagCompound(player);
				
				switch (mode)
				{
				
					case 0:
					{
						int placer = data.getInteger(NBTHelper.getPlayerNBTStringFromMode(mode));
						placer++;
						if (placer >= BlockHelper.getMaxPlaceingModes())
						{
							placer = 0;
						}
						
						data.setInteger(NBTHelper.getPlayerNBTStringFromMode(mode), placer);
						par3.sendChatToPlayer(LanguageRegister.createChatMessage(LanguageRegister.getLanguageName(new InfoStack(), BlockHelper.getPlacingMode(placer), TinyModularThings.instance)));
						break;
					}
					
				}
				
				NBTHelper.setTinyChestData(data, player);
			}
		}
		
		return par1;
	}
	
	public String getModeFromKey(int mode)
	{
		switch (mode)
		{
			case 0:
				return "config.pipe.placement";
			default:
				return "error.nothing";
		}
	}
	
	public int getMaxMode()
	{
		return 1;
	}
	
}
