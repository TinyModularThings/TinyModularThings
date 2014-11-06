package speiger.src.spmodapi.common.blocks.hemp;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.common.blocks.hemp.BlockHempDeko.HempBlockInformation;
import speiger.src.spmodapi.common.items.core.ItemBlockSpmod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockHempDeko extends ItemBlockSpmod
{
	
	public String[] colorsnames = new String[] { "Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "Light Gray", "Gray", "Pink", "Lime", "Yellow", "Light Blue", "Magenta", "Orange", "White" };
	
	public ItemBlockHempDeko(int par1)
	{
		super(par1);
		setHasSubtypes(true);
	}
	
	@Override
	public int getMetadata(int par1)
	{
		return par1;
	}
	
	public HempBlockInformation getHempInfo(BlockStack par1)
	{
		return ((BlockHempDeko) par1.getBlock()).getInfos();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1, EntityPlayer par2EntityPlayer, List par3, boolean par4)
	{
		HempBlockInformation info = getHempInfo(new BlockStack(par1));
		if (!info.canMonsterSpawn())
		{
			par3.add("Monster can not Spawn on me");
		}
	}

	@Override
	public BlockStack getBlockToPlace(int meta)
	{
		return new BlockStack(getBlockID(), meta);
	}

	@Override
	public String getName(ItemStack par1)
	{
		HempBlockInformation info = getHempInfo(new BlockStack(this.getBlockID()));
		return colorsnames[par1.getItemDamage()]+" "+info.getName();
	}
	
}
