package speiger.src.spmodapi.common.blocks.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITaskEntry;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.util.WorldReading;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.client.render.utils.RenderUtilsBlock;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.entity.EntityOverridenEnderman;
import speiger.src.spmodapi.common.enums.EnumGuiIDs;
import speiger.src.spmodapi.common.lib.SpmodAPILib;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.TileIconMaker;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockUtils extends BlockContainer
{

	public BlockUtils(int par1)
	{
		super(par1, Material.rock);
		this.setCreativeTab(APIUtils.tabCrafing);
		this.setResistance(4F);
		this.setHardness(4.0F);
	}
	
	

	@Override
	public float getBlockHardness(World par1World, int par2, int par3, int par4)
	{
		int meta = par1World.getBlockMetadata(par2, par3, par4);
		if(meta == 0)
		{
			return 3F;
		}
		else if(meta == 1)
		{
			return 5F;
		}
		else if(meta == 3)
		{
			return 1F;
		}
		return super.getBlockHardness(par1World, par2, par3, par4);
	}



	@Override
	public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ)
	{
		int meta = world.getBlockMetadata(x, y, z);
		if(meta == 0)
		{
			return 8F;
		}
		else if(meta == 1)
		{
			return 3F;
		}
		else if(meta == 3)
		{
			return 1F;
		}
		return super.getExplosionResistance(par1Entity, world, x, y, z, explosionX, explosionY, explosionZ);
	}



	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return null;
	}

	@Override
	public TileEntity createTileEntity(World world, int meta)
	{
		try
		{
			switch(meta)
			{
				case 1: return new ExpStorage();
				case 2: return new MobMachine();
			}
		}
		catch (Exception e)
		{
		}
		return null;
	}
	
	@Override
	public int getRenderType()
	{
		return RenderUtilsBlock.renderID;
	}
	
	

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
	{
		
	}



	@Override
	public int damageDropped(int par1)
	{
		return par1;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5, ItemStack par6ItemStack)
	{
		int facing = 0;
		int rotation = 0;
		int var7 = MathHelper.floor_double(par5.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
		int var8 = Math.round(par5.rotationPitch);
		if (var8 > 57)
		{
			rotation = ForgeDirection.UP.ordinal();
		}
		else if (var8 < -57)
		{
			rotation = ForgeDirection.DOWN.ordinal();
		}

		if (var7 == 0)
		{
			facing = ForgeDirection.NORTH.ordinal();
		}
		else if (var7 == 1)
		{
			facing = ForgeDirection.EAST.ordinal();
		}
		else if (var7 == 2)
		{
			facing = ForgeDirection.SOUTH.ordinal();
		}
		else if (var7 == 3)
		{
			facing = ForgeDirection.WEST.ordinal();
		}
		
		TileEntity tile = par1World.getBlockTileEntity(par2, par3, par4);
		if (tile != null && tile instanceof AdvTile)
		{
			((AdvTile)tile).onAdvPlacing(rotation, facing);
		}
	}

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
	{
		if(!par1World.isRemote)
		{
			if(par5EntityPlayer.isSneaking())
			{
				return false;
			}
			int meta = par1World.getBlockMetadata(par2, par3, par4);
			if(meta == 0)
			{
				par5EntityPlayer.openGui(SpmodAPI.instance, EnumGuiIDs.WorkBench.getID(), par1World, par2, par3, par4);
				return true;
			}
			else
			{
				TileEntity tile = par1World.getBlockTileEntity(par2, par3, par4);
				if(tile != null && tile instanceof AdvTile)
				{
					return ((AdvTile)tile).onActivated(par5EntityPlayer);
				}
			}
		}
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int par1, int par2)
	{
		Icon[] texture = textures.get(par2);
		switch(par2)
		{
			case 0: return par1 < 2 ? texture[0] : texture[1];
			case 1: return TileIconMaker.getIconFromTile(this, ExpStorage.class, par1);
			case 2: return TileIconMaker.getIconFromTile(this, MobMachine.class, par1);
			case 3: return Block.glass.getBlockTextureFromSide(0);
			default: return null;
		}
	}
	
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5)
	{
		int meta = par1iBlockAccess.getBlockMetadata(par2, par3, par4);
		Icon[] texture = textures.get(meta);
		AdvTile tile = WorldReading.getAdvTile(par1iBlockAccess, par2, par3, par4);
		switch(meta)
		{
			case 0: return par5 < 2 ? texture[0] : texture[1];
			case 1:
			case 2: return tile.getIconFromSideAndMetadata(par5, 1);
			case 3: return Block.glass.getBlockTextureFromSide(0);
			default: return null;
		}
	}



	HashMap<Integer, Icon[]> textures = new HashMap<Integer, Icon[]>();
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1)
	{
		TileIconMaker.registerIcon(this, par1);
		Icon[] texture = new Icon[2];
		texture[0] = par1.registerIcon(SpmodAPILib.ModID.toLowerCase()+":utils/cobble.bench.top");
		texture[1] = par1.registerIcon(SpmodAPILib.ModID.toLowerCase()+":utils/cobble.bench.side");
		textures.put(0, texture);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for(int i = 0;i<4;i++)
		{
			par3List.add(new ItemStack(par1, 1, i));
		}
	}

	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
	{
		TileEntity tile = par1World.getBlockTileEntity(par2, par3, par4);
		if(tile != null && tile instanceof AdvTile)
		{
			((AdvTile)tile).onBreaking();
		}
		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}
	
	public void updateTick(World world, int i, int j, int k, Random random)
    {
		if(!world.isRemote)
		{
			int meta = world.getBlockMetadata(i, j, k);
			if(meta == 3)
			{
				List<EntityMob> mobs = world.getEntitiesWithinAABB(EntityMob.class, AxisAlignedBB.getBoundingBox(i, j, k, i+1, j+1, k+1).expand(25, 10, 25));
				for(EntityMob mob : mobs)
				{
					if(mob != null)
					{
						if(mob instanceof EntityEnderman && !(mob instanceof EntityOverridenEnderman))
						{
							EntityEnderman man = (EntityEnderman) mob;
							World worldObj = man.worldObj;
							NBTTagCompound nbt = new NBTTagCompound();
							man.writeToNBT(nbt);
							man.setDead();
							EntityOverridenEnderman newMan = new EntityOverridenEnderman(worldObj);
							newMan.readFromNBT(nbt);
							worldObj.spawnEntityInWorld(newMan);
						}
						else
						{
							mob.setTarget(null);
							mob.setPathToEntity(mob.getNavigator().getPathToXYZ(i, j, k));
							mob.getNavigator().tryMoveToXYZ(i, j, k, 1D);
						}
					}
				}
			}
			
			notifyNeighbors(world, i, j, k);
			world.scheduleBlockUpdate(i, j, k, blockID, tickRate());
		}

    }
	
	
	
	@Override
	public int getLightOpacity(World world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);
		if(meta == 3)
		{
			return 0;
		}
		return super.getLightOpacity(world, x, y, z);
	}

	public void onBlockDestroyedByPlayer(World world, int i, int j, int k, int l)
    {
		notifyNeighbors(world, i, j, k);
    }
	
	public void notifyNeighbors(World world, int i, int j, int k)
	{
		world.notifyBlocksOfNeighborChange(i, j, k, blockID);
		world.notifyBlocksOfNeighborChange(i, j - 1, k, blockID);
        world.notifyBlocksOfNeighborChange(i, j + 1, k, blockID);
        world.notifyBlocksOfNeighborChange(i - 1, j, k, blockID);
        world.notifyBlocksOfNeighborChange(i + 1, j, k, blockID);
        world.notifyBlocksOfNeighborChange(i, j, k - 1, blockID);
        world.notifyBlocksOfNeighborChange(i, j, k + 1, blockID);
	}
	
	
	public void onBlockAdded(World world, int i, int j, int k)
    {        
		if(world.getBlockMetadata(i, j, k) == 3)
		{
			world.scheduleBlockUpdate(i, j, k, blockID, tickRate());
		}
    }
	
	public int tickRate()
	{
		return 10;
	}
	
	public boolean containsTask(List<EntityAITaskEntry> list)
	{
		for(EntityAITaskEntry task : list)
		{
			if(task.action instanceof EntityAIWander)
			{
				return true;
			}
		}
		return false;
	}
	
	
}
