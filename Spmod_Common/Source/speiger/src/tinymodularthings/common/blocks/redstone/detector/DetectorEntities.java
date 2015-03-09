package speiger.src.tinymodularthings.common.blocks.redstone.detector;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.nbt.NBTTagCompound;
import speiger.src.api.common.world.tiles.machine.IDetector;
import speiger.src.api.common.world.tiles.machine.IDetectorModul;

public class DetectorEntities implements IDetectorModul
{
	
	int type;
	
	public DetectorEntities(int par1)
	{
		type = par1;
	}
	
	@Override
	public void onUnloading(IDetector detector)
	{
		
	}
	
	@Override
	public int getTickRate(IDetector detector)
	{
		return 5;
	}
	
	@Override
	public boolean doesHaveTileEntityTick(IDetector detector)
	{
		return true;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound par1)
	{
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound par1)
	{
		
	}
	
	@Override
	public void onTileEntityTick(IDetector par1)
	{
		Class filter = null;
		
		switch(type)
		{
			case 0:
				filter = EntityLiving.class;
				break;
			case 1:
				filter = EntityAnimal.class;
				break;
			case 2:
				filter = EntityMob.class;
				break;
			case 3:
				filter = EntityTameable.class;
				break;
			case 4:
				filter = EntityWaterMob.class;
				break;
			case 5:
				filter = EntityVillager.class;
				break;
			case 6:
				filter = EntityItem.class;
				break;
			case 7: 
				filter = IProjectile.class;
				break;
		}
		
		if(filter != null)
		{
			List<Entity> data = par1.getEntitiesInfront(filter);
			if(data.size() > 0)
			{
				par1.setRedstoneSignal(15);
				return;
			}
		}
		par1.setRedstoneSignal(0);
	}
	
	@Override
	public void onBlockUpdate(IDetector par1)
	{
		
	}

	@Override
	public void addItemInformation(List par1)
	{
		String text = "";
		switch(type)
		{
			case 0:
				text = "Living Entities";
				break;
			case 1:
				text = "Animals";
				break;
			case 2:
				text = "Monsters";
				break;
			case 3:
				text = "Tameable Entities";
				break;
			case 4:
				text = "Water Entities";
				break;
			case 5:
				text = "Villagers";
				break;
			case 6:
				text = "Items";
				break;
			case 7:
				text = "Projectile";
				break;
		}
		
		par1.add("Detects if "+text+" are Infront of him");
	}
	
}
