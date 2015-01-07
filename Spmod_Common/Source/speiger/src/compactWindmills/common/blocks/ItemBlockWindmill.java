package speiger.src.compactWindmills.common.blocks;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.compactWindmills.common.utils.WindmillType;
import speiger.src.spmodapi.common.items.core.ItemBlockSpmod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockWindmill extends ItemBlockSpmod
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

	@Override
	public BlockStack getBlockToPlace(int meta)
	{
		return new BlockStack(this.getBlockID(), meta);
	}

	@Override
	public String getName(ItemStack par1)
	{
		switch(par1.getItemDamage())
		{
			case 0: return "Extreme Low Voltage Windmill";
			case 1: return "Low Voltage Windmill";
			case 2: return "Medium Voltage Windmill";
			case 3: return "High Voltage Windmill";
			case 4: return "Extreme Voltage Windmill";
		}
		return null;
	}
	
	
	
}
