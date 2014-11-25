package speiger.src.api.common.registry.animalgas.parts;

import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;

public interface IEntityInfo
{
	//Simply is just for Custom Data that you want to update.
	public void onPreTick(EntityLiving par1);
	
	public void onPostTick(EntityLiving par1);
}
