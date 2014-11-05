package speiger.src.spmodapi.common.blocks.hemp;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import speiger.src.api.common.registry.helpers.SpmodMod;
import speiger.src.api.common.registry.helpers.SpmodModRegistry;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.SpmodAPI;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockHempStraw extends ItemBlock
{
	
	public ItemBlockHempStraw(int par1)
	{
		super(par1);
		setHasSubtypes(true);
	}
	
	@Override
	public int getMetadata(int par1)
	{
		return par1;
	}
	
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getSpriteNumber()
	{
		return 0;
	}

	
}
