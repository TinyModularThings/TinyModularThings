package speiger.src.spmodapi.common.modHelper.IC2;

import java.util.List;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import speiger.src.api.items.InfoStack;
import speiger.src.api.language.LanguageRegister;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;



public class HempCrop extends CropCard
{
	
	@Override
	public String name()
	{
		return LanguageRegister.getLanguageName(new InfoStack(), "crop.hemp", SpmodAPI.instance);
	}
	
	@Override
	public int tier()
	{
		return 1;
	}
	
	@Override
	public int stat(int n)
	{
		switch (n)
		{
			case 0:
				return 0;
			case 1:
				return 4;
			case 2:
				return 0;
			case 3:
				return 0;
			case 4:
				return 2;
		}
		return 0;
	}
	
	@Override
	public String[] attributes()
	{
		return new String[] { "Green", "Healing", "Food" };
	}
	
	@Override
	public int maxSize()
	{
		return 7;
	}
	
	@Override
	public boolean canGrow(ICropTile crop)
	{
		return crop.getSize() < 7;
	}
	
	@Override
	public boolean canBeHarvested(ICropTile crop)
	{
		return crop.getSize() == 7;
	}
	
	@Override
	public ItemStack getGain(ICropTile crop)
	{
		return new ItemStack(APIItems.hemp);
	}
	
	@Override
	public boolean rightclick(ICropTile crop, EntityPlayer player)
	{
		if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.dye && player.getCurrentEquippedItem().getItemDamage() == 0)
		{
			crop.setSize((byte) Math.min(crop.getSize() + 1, 7));
		}
		return super.rightclick(crop, player);
	}
	
	@Override
	public ItemStack getSeeds(ICropTile crop)
	{
		return super.getSeeds(crop);
	}
	
	@Override
	public byte getSizeAfterHarvest(ICropTile crop)
	{
		return 4;
	}
	
	@Override
	public void tick(ICropTile crop)
	{
		if (crop.getSize() >= 4)
		{
			ChunkCoordinates coords = crop.getLocation();
			int level = crop.getSize() - 3;
			List<EntityAnimal> animals = crop.getWorld().getEntitiesWithinAABB(EntityAnimal.class, AxisAlignedBB.getBoundingBox(coords.posX, coords.posY, coords.posZ, coords.posX, coords.posY, coords.posY).expand(5 * level, 3 * level, 5 * level));
			for (EntityAnimal cu : animals)
			{
				cu.getNavigator().setAvoidsWater(true);
				cu.getNavigator().setCanSwim(false);
				cu.getNavigator().tryMoveToXYZ(coords.posX, coords.posY, coords.posZ, 1D);
			}
		}
	}
	
	@Override
	public String discoveredBy()
	{
		return "Speiger";
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getSprite(ICropTile crop)
	{
		return APIBlocks.hempCrop.getIcon(0, crop.getSize());
	}
	
	@Override
	public int growthDuration(ICropTile crop)
	{
		return 150;
	}
	
}
