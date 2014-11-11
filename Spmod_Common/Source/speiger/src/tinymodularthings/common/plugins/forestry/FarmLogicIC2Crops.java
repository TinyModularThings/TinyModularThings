package speiger.src.tinymodularthings.common.plugins.forestry;

import ic2.api.item.Items;

import java.util.Collection;
import java.util.Stack;

import speiger.src.spmodapi.common.util.TextureEngine;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.farming.Farmables;
import forestry.api.farming.ICrop;
import forestry.api.farming.IFarmHousing;
import forestry.api.farming.IFarmable;
import forestry.core.utils.Vect;
import forestry.farming.logic.FarmLogicWatered;

public class FarmLogicIC2Crops extends FarmLogicWatered
{
	IFarmable[] seeds = new IFarmable[0];
	IFarmHousing core;
	
	public FarmLogicIC2Crops(IFarmHousing housing)
	{
		super(housing, new ItemStack[] {new ItemStack(Block.dirt)}, new ItemStack[] {new ItemStack(Block.tilledField)}, new ItemStack[] {new ItemStack(Block.dirt), new ItemStack(Block.grass)});
		core = housing;
		Collection<IFarmable> possibles = Farmables.farmables.get("IC2Crops");
		if(possibles != null && possibles.size() > 0)
		{
			seeds = possibles.toArray(new IFarmable[0]);
		}
		this.setManual(true);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon()
	{
		ItemStack stack = Items.getItem("cropSeed");
		if(stack != null)
		{
			return stack.getIconIndex();
		}
		return TextureEngine.getTextures().getIconSafe();
	}
	
	@Override
	public String getName()
	{
		return "IC2 Crops";
	}
	
	@Override
	public Collection<ICrop> harvest(int x, int y, int z, ForgeDirection direction, int extent)
	{
	    World world = this.core.getWorld();
	    Stack crops = new Stack();
	    for (int i = 0; i < extent; i++) {
	      Vect position = translateWithOffset(x, y + 1, z, direction, i);
	      for (IFarmable seed : this.seeds) {
	        ICrop crop = seed.getCropAt(world, position.x, position.y, position.z);
	        if (crop != null)
	          crops.push(crop);
	      }
	    }
	    return crops;
	}
	
	@Override
	public boolean isAcceptedGermling(ItemStack arg0)
	{
		return false;
	}

	@Override
	public int getFertilizerConsumption()
	{
		return 40;
	}

	@Override
	public int getWaterConsumption(float hydrationModifier)
	{
		return (int)(240.0F * hydrationModifier);
	}
	
	
}
