package speiger.src.api.common.registry.animalgas.parts;

import net.minecraft.entity.EntityAgeable;

public interface IEntityCustomInfo extends IEntityInfo
{
	public boolean canUpdate();
	
	//You can Cast IEntityLogic to EntityProcessor with that you can handle Effects. and Stuff.
	public void onPreTick(EntityAgeable par1, IEntityLogic par2);
	
	//Same as pre. but after everything already happend.
	public void onPostTick(EntityAgeable par1, IEntityLogic par2);
}
