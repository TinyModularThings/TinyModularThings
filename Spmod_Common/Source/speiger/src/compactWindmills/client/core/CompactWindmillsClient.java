package speiger.src.compactWindmills.client.core;

import speiger.src.compactWindmills.CompactWindmills;
import speiger.src.compactWindmills.client.render.WindmillRenderer;
import speiger.src.compactWindmills.common.blocks.WindMill;
import speiger.src.compactWindmills.common.core.CompactWindmillsCore;
import cpw.mods.fml.client.registry.ClientRegistry;

public class CompactWindmillsClient extends CompactWindmillsCore
{

	@Override
	public void onClientLoad(CompactWindmills par0)
	{
		if(par0.specailRenderer)
		{
			ClientRegistry.bindTileEntitySpecialRenderer(WindMill.class, new WindmillRenderer());
		}
	}
	
}
