package speiger.src.spmodapi.common.blocks.utils;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAITaskEntry;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import speiger.src.api.client.gui.IBlockGui;
import speiger.src.api.client.render.BlockRenderHelper;
import speiger.src.api.common.world.blocks.BlockPosition;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.common.blocks.cores.SpmodBlockContainerBase;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.entity.EntityOverridenEnderman;
import speiger.src.spmodapi.common.enums.EnumGuiIDs;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.TileIconMaker;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockUtils extends SpmodBlockContainerBase implements IBlockGui
{
	
	public BlockUtils(int par1)
	{
		super(par1, Material.rock);
		this.setCreativeTab(APIUtils.tabCrafing);
		MinecraftForge.setBlockHarvestLevel(this, 0, "pickaxe", 0);
		MinecraftForge.setBlockHarvestLevel(this, 1, "pickaxe", 1);
		MinecraftForge.setBlockHarvestLevel(this, 2, "pickaxe", 1);
		MinecraftForge.setBlockHarvestLevel(this, 3, "pickaxe", 1);
		MinecraftForge.setBlockHarvestLevel(this, 4, "pickaxe", 1);
		this.dissableDrops(1);
		this.dissableDrops(2);
		this.dissableDrops(4);
		this.dissableDrops(5);
	}
	
	
	
	@Override
	public boolean hasTileDrops(int meta)
	{
		return true;
	}



	@Override
	public float getBlockHardness(int meta)
	{
		switch(meta)
		{
			case 0: return 3F;
			case 3: return 1F;
		}
		return super.getBlockHardness(meta);
	}

	@Override
	public float getBlockResistance(Entity par1, int meta)
	{
		switch(meta)
		{
			case 0: return 8F;
			case 3: return 1F;
		}
		return super.getBlockResistance(par1, meta);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return null;
	}
	
	@Override
	public TileEntity createTileEntity(World world, int meta)
	{
		switch (meta)
		{
			case 1: return new ExpStorage();
			case 2: return new MobMachine();
			case 4: return new InventoryAccesser();
			case 5: return new CobbleChest();
			case 6: return new MobMachineSpawner();
			case 7: return new Radio();
		}
		return null;
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
	public int getGuiIDForMeta(int meta)
	{
		switch(meta)
		{
			case 0: return EnumGuiIDs.BlockGui.getID();
		}
		return super.getGuiIDForMeta(meta);
	}

	@Override
	public int getLightOpacity(int meta)
	{
		switch(meta)
		{
			case 3:	return 255;
		}
		return super.getLightOpacity(meta);
	}

	@Override
	public Icon getTexture(TextureEngine par1, int meta, int side)
	{
		
		switch(meta)
		{
			case 0: return par1.getTexture(this, side < 2 ? 0 : 1);
			case 3: return Block.glass.getIcon(0, 0);
		}
		return super.getTexture(par1, meta, side);
	}
	
	@Override
	public void registerTextures(TextureEngine par1)
	{
		par1.setCurrentPath("utils");
		par1.registerTexture(new BlockStack(this, 0), "cobble.bench.top", "cobble.bench.side");
		TileIconMaker.registerIcon(this, par1);
	}

	
	
	@Override
	public boolean hasTileEntity(int meta)
	{
		switch(meta)
		{
			case 0:
			case 3: return false;
		}
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int i = 0; i < 8; i++)
		{
			if(i == 6)
			{
				continue;
			}
			par3List.add(new ItemStack(par1, 1, i));
		}
	}
	
	public void updateTick(World world, int i, int j, int k, Random random)
	{
		if (!world.isRemote)
		{
			int meta = world.getBlockMetadata(i, j, k);
			if (meta == 3)
			{
				List<EntityMob> mobs = world.getEntitiesWithinAABB(EntityMob.class, AxisAlignedBB.getBoundingBox(i, j, k, i + 1, j + 1, k + 1).expand(25, 10, 25));
				for (EntityMob mob : mobs)
				{
					if (mob != null)
					{
						if (mob instanceof EntityEnderman && !(mob instanceof EntityOverridenEnderman))
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
		if (world.getBlockMetadata(i, j, k) == 3)
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
		for (EntityAITaskEntry task : list)
		{
			if (task.action instanceof EntityAIWander)
			{
				return true;
			}
		}
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer getGui(int meta, InventoryPlayer par1, BlockPosition par2)
	{
		if(meta == 0)
		{
			return new GuiCrafting(par1, par2.getWorld(), par2.getXCoord(), par2.getYCoord(), par2.getZCoord());
		}
		return null;
	}

	@Override
	public AdvContainer getInventory(int meta, InventoryPlayer par1, BlockPosition par2)
	{
		if(meta == 0)
		{
			return new InventoryCrafter(par1, par2.getWorld(), par2.getXCoord(), par2.getYCoord(), par2.getZCoord());
		}
		return null;
	}

	@Override
	public boolean requiresRender()
	{
		return true;
	}

	@Override
	public void onRenderInv(BlockStack par1, RenderBlocks render)
	{
		if(par1.getMeta() == 3)
		{
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.setNormal(1.0F, 1.0F, 0.0F);
			Minecraft mc = FMLClientHandler.instance().getClient();
			mc.renderEngine.bindTexture(TextureMap.locationItemsTexture);
			render.renderFaceXPos(this, -0.5D, 0.0D, 0.0D, TextureEngine.getTextures().getIconSafe(Item.porkRaw.getIconFromDamage(0)));
			tessellator.draw();
			mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
			BlockRenderHelper.renderInInv(par1, render);
		}
		else
		{
			super.onRenderInv(par1, render);
		}
	}

	@Override
	public void onRenderWorld(IBlockAccess world, int x, int y, int z, RenderBlocks render)
	{
		render.renderStandardBlock(this, x, y, z);
		Tessellator tes = Tessellator.instance;
		Minecraft mc = FMLClientHandler.instance().getClient();
		tes.draw();
		mc.renderEngine.bindTexture(TextureMap.locationItemsTexture);
		tes.startDrawingQuads();
		tes.setNormal(1F, 1F, 0F);
		render.setOverrideBlockTexture(Item.porkRaw.getIconFromDamage(0));
		render.renderCrossedSquares(this, x, y, z);
		render.clearOverrideBlockTexture();
		tes.draw();
		tes.startDrawingQuads();
		mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
	}
	
	
}