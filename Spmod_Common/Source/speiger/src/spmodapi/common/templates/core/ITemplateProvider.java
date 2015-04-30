package speiger.src.spmodapi.common.templates.core;

import net.minecraft.entity.player.EntityPlayer;

public interface ITemplateProvider
{
	public ITemplate getTemplate();
	
	public void initTemplate();
	
	public void onStructureChange();
	
	public void onInteraction(EntityPlayer par1);
	
	public boolean hasValidTemplate();
}
