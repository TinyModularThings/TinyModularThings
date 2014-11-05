package speiger.src.compactWindmills.common.blocks;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import speiger.src.compactWindmills.common.utils.WindmillType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockWindmill extends ItemBlock
{
	
	public ItemBlockWindmill(int par1)
	{
		super(par1);
		this.setHasSubtypes(true);
	}
	
	@Override
	public int getMetadata(int par1)
	{
		return par1;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		WindmillType type = WindmillType.values()[par1ItemStack.getItemDamage()];
		if (type != null)
		{
			par3List.add("Max. Output: " + type.getOutput() + "EU/t");
		}
	}
	
}
