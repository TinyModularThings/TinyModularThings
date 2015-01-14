package speiger.src.spmodapi.common.blocks.deko;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import speiger.src.spmodapi.common.lib.SpmodAPILib;
import speiger.src.spmodapi.common.tile.TileFacing;
import speiger.src.spmodapi.common.util.TextureEngine;

public class KyrokaTheFox extends TileFacing
{
	int type = 0;
	
	public static HashMap<Integer, ResourceLocation> textures = new HashMap<Integer, ResourceLocation>();
	
	public KyrokaTheFox()
	{
		
	}
	
	public KyrokaTheFox(World world)
	{
		if(world == null)
		{
			type = 0;
		}
		else if(world.provider.isHellWorld)
		{
			type = 1;
		}
		else if(world.provider.dimensionId == 1)
		{
			//There will be a third type soon. I need only to find the right picture :/
			type = 1;
		}
		else
		{
			type = 0;
		}
	}
	
	@Override
	public void registerIcon(TextureEngine par1, Block par2)
	{
		textures.put(0, new ResourceLocation(SpmodAPILib.ModID.toLowerCase()+":textures/models/armor/ModelKyrokaTheFox.png"));
		textures.put(1, new ResourceLocation(SpmodAPILib.ModID.toLowerCase()+":textures/models/armor/ModelKyrokaTheFoxDark.png"));
	}

	public ResourceLocation getTexture()
	{
		ResourceLocation texture = textures.get(type);
		if(texture == null)
		{
			texture = textures.get(0);
		}
		
		if(texture == null)
		{
			texture = new ResourceLocation(SpmodAPILib.ModID.toLowerCase()+":textures/models/armor/ModelKyrokaTheFox.png");
		}
		return texture;
	}

	@Override
	public boolean isSixSidedFacing()
	{
		return false;
	}
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		return null;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		type = nbt.getInteger("Type");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("Type", type);
	}
	
	@Override
	public boolean canUpdate()
	{
		return false;
	}
}
