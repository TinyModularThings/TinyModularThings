package speiger.src.tinymodularthings.common.plugins.BC.triggers;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.tinymodularthings.common.pipes.PipeEmeraldExtractionPower;
import buildcraft.BuildCraftTransport;
import buildcraft.api.gates.ITrigger;
import buildcraft.api.gates.ITriggerParameter;
import buildcraft.transport.ITriggerPipe;
import buildcraft.transport.Pipe;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TriggerPipeRequestPower implements ITrigger, ITriggerPipe
{
	
	@Override
	public int getLegacyId()
	{
		return 0;
	}
	
	@Override
	public String getUniqueTag()
	{
		return "trigger.Power.2";
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon()
	{
		return BuildCraftTransport.triggerPipeRequestsEnergy.getIcon();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		
	}
	
	@Override
	public boolean hasParameter()
	{
		return false;
	}
	
	@Override
	public boolean requiresParameter()
	{
		return false;
	}
	
	@Override
	public String getDescription()
	{
		return null;
	}
	
	@Override
	public boolean isTriggerActive(ForgeDirection side, TileEntity tile, ITriggerParameter parameter)
	{
		return false;
	}
	
	@Override
	public ITriggerParameter createParameter()
	{
		return null;
	}

	@Override
	public boolean isTriggerActive(Pipe pipe, ITriggerParameter parameter)
	{
		if(pipe != null)
		{
			if(pipe instanceof PipeEmeraldExtractionPower)
			{
				return ((PipeEmeraldExtractionPower)pipe).requestsPower();
			}
		}
		return false;
	}
	
}
