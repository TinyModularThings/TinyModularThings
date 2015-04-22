package speiger.src.spmodapi.common.handler;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.oredict.OreDictionary;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.util.TickHelper;
import speiger.src.spmodapi.common.world.retrogen.RetroGenTickHandler.OreReplacer;
import cpw.mods.fml.common.Loader;

public class BlockHandler
{
	public static BlockHandler instance = new BlockHandler();
	private boolean init = false;
	private boolean neiLoaded = false;
	
	public static BlockHandler getInstance()
	{
		return instance;
	}
	
	public BlockHandler()
	{
		OreDictionary.registerOre("oreCoal", Block.oreCoal);
		OreDictionary.registerOre("oreRedstone", Block.oreRedstone);
		OreDictionary.registerOre("oreRedstone", Block.oreRedstoneGlowing);
		OreDictionary.registerOre("oreLapis", Block.oreLapis);
		OreDictionary.registerOre("oreQuartz", Block.oreNetherQuartz);
		OreDictionary.registerOre("oreDiamond", Block.oreDiamond);
		OreDictionary.registerOre("oreIron", Block.oreIron);
		OreDictionary.registerOre("oreGold", Block.oreGold);
		OreDictionary.registerOre("oreEmerald", Block.oreEmerald);
	}
	
	@ForgeSubscribe
	public void onBlockBreak(HarvestDropsEvent evt)
	{
		EntityPlayer player = evt.harvester;
		if(player != null)
		{
			if(isOre(evt.block, evt.blockMetadata))
			{
				int id = EnchantmentHelper.getEnchantmentLevel(APIUtils.oreSilk.effectId, player.getCurrentEquippedItem());
				if(id > 0)
				{
					if(!isSilked(evt.drops.get(0), evt.block, evt.blockMetadata))
					{
						evt.drops.clear();
						evt.drops.add(new ItemStack(evt.block, 1, evt.blockMetadata));
					}
				}
				id = EnchantmentHelper.getEnchantmentLevel(APIUtils.oreFortune.effectId, player.getCurrentEquippedItem());
				if(id > 0)
				{
					int randLevel = 1 + evt.world.rand.nextInt(5);
					evt.drops.clear();
					evt.drops.addAll(evt.block.getBlockDropped(evt.world, evt.x, evt.y, evt.z, evt.blockMetadata, randLevel));
				}
			}
		}
	}
	
	@ForgeSubscribe
	public void onBlockBreak(BreakEvent evt)
	{
		EntityPlayer player = evt.getPlayer();
		if(player != null && evt.block.blockID == Block.mobSpawner.blockID)
		{
			int id = EnchantmentHelper.getEnchantmentLevel(APIUtils.oreSilk.effectId, player.getCurrentEquippedItem());
			if(id > 0)
			{
				TileEntity tile = evt.world.getBlockTileEntity(evt.x, evt.y, evt.z);
				if(tile instanceof TileEntityMobSpawner)
				{
					TileEntityMobSpawner mob = (TileEntityMobSpawner)tile;
					int meta = EntityList.getEntityID(EntityList.createEntityByName(mob.getSpawnerLogic().getEntityNameToSpawn(), evt.world));
					if(!evt.world.isRemote)
					{
						evt.world.spawnEntityInWorld(new EntityItem(evt.world, evt.x, evt.y, evt.z, new ItemStack(evt.block, 1, meta)));
					}
				}
			}
		}
	}
	
	@ForgeSubscribe
	public void onBlockClicked(PlayerInteractEvent evt)
	{
		if(!isNeiLoaded())
		{
			if(evt.action == Action.RIGHT_CLICK_BLOCK && !evt.entityPlayer.worldObj.isRemote)
			{
				ItemStack item = evt.entityPlayer.getCurrentEquippedItem();
				if(item != null && item.itemID == Block.mobSpawner.blockID)
				{
					ForgeDirection facing = ForgeDirection.getOrientation(evt.face);
					TickHelper.getInstance().regainOre.add(new SpawnerAdjuster(evt.entityPlayer.worldObj, evt.x + facing.offsetX, evt.y + facing.offsetY, evt.z + facing.offsetZ, item.getItemDamage()));
				}
			}
		}
	}
	
	private boolean isNeiLoaded()
	{
		if(!init)
		{
			init = true;
			neiLoaded = Loader.isModLoaded("NotEnoughItems");
		}
		return neiLoaded;
	}
	
	private boolean isSilked(ItemStack par1, Block par2, int par3)
	{
		if(par1.itemID == par2.blockID && par1.getItemDamage() == par3)
		{
			return true;
		}
		return false;
	}
	
	private boolean isOre(Block par1, int meta)
	{
		ItemStack stack = new ItemStack(par1, 1, meta);
		String name = OreDictionary.getOreName(OreDictionary.getOreID(stack));
		return name.startsWith("ore");
	}
	
	private static class SpawnerAdjuster extends OreReplacer
	{
		int delay = 3;
		int x;
		int y;
		int z;
		int meta;
		World world;
		
		public SpawnerAdjuster(World worldObj, int x, int y, int z, int itemDamage)
		{
			world = worldObj;
			this.x = x;
			this.y = y;
			this.z = z;
			meta = itemDamage;
		}

		@Override
		public boolean generate()
		{
			if(delay <= 0)
			{
				return true;
			}
			if(world.getBlockId(x, y, z) != Block.mobSpawner.blockID)
			{
				delay--;
				return false;
			}
			if(!(world.getBlockTileEntity(x, y, z) instanceof TileEntityMobSpawner))
			{
				delay--;
				return false;
			}
			TileEntityMobSpawner tile = (TileEntityMobSpawner)world.getBlockTileEntity(x, y, z);
			tile.getSpawnerLogic().setMobID(EntityList.getStringFromID(meta));
			return true;
		}
		
	}
	
}
