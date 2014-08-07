package speiger.src.tinymodularthings.common.items.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import speiger.src.api.blocks.BlockStack;
import speiger.src.api.items.DisplayStack;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import speiger.src.spmodapi.common.util.BlockPosition;
import speiger.src.tinymodularthings.common.items.core.TinyItem;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemNetherCrystal extends TinyItem
{
	public static HashMap<String, ArrayList<BlockPosition>> todo = new HashMap<String, ArrayList<BlockPosition>>();
	public static HashMap<String, ArrayList<BlockPosition>> replace = new HashMap<String, ArrayList<BlockPosition>>();
	
	public String[] names = new String[]{
			"nether.crystal",
			"nether.crystal.charging",
			"nether.crystal.charging",
			"nether.crystal.charged",
			"nether.crystal.used",
			"nether.crystal.broken"
	};
	
	public ItemNetherCrystal(int par1)
	{
		super(par1);
		this.setContainerItem(this);
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
		this.setCreativeTab(CreativeTabs.tabFood);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		par3List.add(createEmptyNetherCrystal(par1));
		par3List.add(new ItemStack(par1, 1, 1));
	}



	@Override
	public void registerItems(int id, SpmodMod par0)
	{
		if(!SpmodModRegistry.areModsEqual(getMod(), par0))
		{
			return;
		}
		for(int i = 0;i<names.length;i++)
		{
			LanguageRegister.getLanguageName(new DisplayStack(new ItemStack(id, 1, i)), names[i], par0);
		}
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod Start)
	{
		return LanguageRegister.getLanguageName(new DisplayStack(par1), names[par1.getItemDamage()], Start);
	}
	
	

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		if(!par2World.isRemote)
		{
			if((par2World.provider.dimensionId == -1) && par1ItemStack.getItemDamage() == 0)
			{
				this.createLoadedNetherCrystal(par1ItemStack, par3EntityPlayer);
				return par1ItemStack;
			}
			
			MovingObjectPosition move = this.getMovingObjectPositionFromPlayer(par3EntityPlayer.worldObj, par3EntityPlayer, true);
			if(move == null)
			{
				sendChargeInfo(par1ItemStack, par3EntityPlayer);
			}
			else
			{
				if(!isFillAbleBlock(move, par2World))
				{
					sendChargeInfo(par1ItemStack, par3EntityPlayer);
				}
			}
		}
		return par1ItemStack;
	}

	public boolean isFillAbleBlock(MovingObjectPosition pos, World world)
	{
		TileEntity tile = world.getBlockTileEntity(pos.blockX, pos.blockY, pos.blockZ);
		if(tile != null && tile instanceof IFluidHandler)
		{
			return true;
		}
		
		return false;
	}

	public void sendChargeInfo(ItemStack par1, EntityPlayer par2)
	{
		if(par1 != null && par1.hasTagCompound() && par1.getTagCompound().getCompoundTag("Lava") != null)
		{
			int charge = par1.getTagCompound().getCompoundTag("Lava").getInteger("Charges");
			
			par2.sendChatToPlayer(LanguageRegister.createChatMessage(charge+" Charges Left"));
		}
	}
	
	

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		if(!world.isRemote)
		{
			if(stack.hasTagCompound() && stack.getTagCompound().getCompoundTag("Lava") != null)
			{
				NBTTagCompound nbt = stack.getTagCompound().getCompoundTag("Lava");
				int charges = nbt.getInteger("Charges");
				
				if(charges > 0)
				{
					TileEntity tile = world.getBlockTileEntity(x, y, z);
					if(tile != null && tile instanceof IFluidHandler)
					{
						IFluidHandler fluid = (IFluidHandler) tile;
						ForgeDirection face = ForgeDirection.getOrientation(side);
						
						if(fluid.canFill(face, FluidRegistry.LAVA))
						{
							int filled = fluid.fill(face, new FluidStack(FluidRegistry.LAVA, 1000), false);
							if(filled > 0)
							{
								fluid.fill(face, new FluidStack(FluidRegistry.LAVA, 1000), true);
								this.discharge(stack, 1);
								return true;
							}
						}
						
					}
				}
			}
		}
		
		
		return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
	}

	@Override
	public ItemStack getContainerItemStack(ItemStack itemStack)
	{
		ItemStack par1 = itemStack.copy();
		
		discharge(par1, 1);
		
		return par1;
	}
	
	public void discharge(ItemStack par1, int amount)
	{
		if(par1.hasTagCompound() && par1.getTagCompound().getCompoundTag("Lava") != null)
		{
			NBTTagCompound nbt = par1.getTagCompound().getCompoundTag("Lava");
			int charge = nbt.getInteger("Charges");
			if(charge > 0)
			{
				nbt.setInteger("Charges", charge-amount);
			}
			else
			{
				par1.setItemDamage(5);
			}
		}
		else
		{
			par1.setItemDamage(5);
		}
	}
	
	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem)
	{
		ItemStack stack = entityItem.getEntityItem();
		if(stack != null)
		{
			PreCharge(stack, entityItem.worldObj, null);
		}
		return super.onEntityItemUpdate(entityItem);
	}
	
	
	
	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
	{
		super.onUpdate(par1ItemStack, par2World, par3Entity, par4, par5);
		if(par1ItemStack != null)
		{
			if(par3Entity != null && par3Entity instanceof EntityPlayer)
			{
				PreCharge(par1ItemStack, par2World, (EntityPlayer)par3Entity);
			}
		}
	}

	


	public void PreCharge(ItemStack par1, World world, EntityPlayer player)
	{
		if(!world.isRemote)
		{
			if(world.provider.dimensionId == -1)
			{
				if(par1.getItemDamage() == 1)
				{
					chargeCrystal(par1, world, player);
				}
				if(par1.getItemDamage() == 2)
				{
					cleanUpArea(par1, world);
				}
				if(par1.getItemDamage() == 4)
				{
					recharge(par1);
				}
			}
		}
	}
	
	public void recharge(ItemStack par1)
	{
		if(par1.hasTagCompound() && par1.getTagCompound().getCompoundTag("Lava") != null)
		{
			NBTTagCompound nbt = par1.getTagCompound().getCompoundTag("Lava");
			int charges = nbt.getInteger("Charges");
			if(charges > 0)
			{
				nbt.setInteger("Charges", charges-1);
				par1.setItemDamage(3);
				return;
			}
			par1.setItemDamage(5);
		}
		else
		{
			par1.setItemDamage(5);
		}
	}
	
	public void cleanUpArea(ItemStack par1, World par2)
	{
		if(par1.hasTagCompound() && par1.getTagCompound().getCompoundTag("Lava") != null)
		{
			NBTTagCompound nbt = par1.getTagCompound().getCompoundTag("Lava");
			int[] pos = nbt.getIntArray("Pos");
			ArrayList<BlockPosition> area = replace.get(nbt.getString("ID"));
			
			if(area != null)
			{
				for(BlockPosition cu : area)
				{
					par2.setBlock(cu.xCoord, cu.yCoord, cu.zCoord, Block.netherrack.blockID, 0, 3);
				}
				
				area.clear();
				replace.remove(nbt.getString("ID"));
			}
			
			
			if(pos.length == 3)
			{
				int range = 40;
				int rangeY = 10;
				
				for(int x = pos[0]-range;x<pos[0]+range;x++)
				{
					for(int y = pos[1]-rangeY;y<pos[1]+rangeY;y++)
					{
						for(int z = pos[2]-range;z<pos[2]+range;z++)
						{
							BlockPosition block = new BlockPosition(par2, x, y, z);
							if(block.doesBlockExsist() && block.isThisBlock(new BlockStack(Block.lavaMoving), false))
							{
								par2.setBlock(x, y, z, 0, 0, 1);
							}
						}
					}
				}
				par1.setItemDamage(3);
			}
		}
	}

	public void chargeCrystal(ItemStack par1, World world, EntityPlayer player)
	{
		boolean flag = player != null;

		if(par1 == null)
		{
			return;
		}
		
		if(!par1.hasTagCompound())
		{
			return;
		}
		
		if(!par1.getTagCompound().hasKey("Lava"))
		{
			return;
		}
		
		boolean workDone = false;
		
		if(!world.isRemote)
		{
			NBTTagCompound nbt = par1.getTagCompound().getCompoundTag("Lava");
			
			ArrayList<BlockPosition> pos = todo.get(nbt.getString("ID"));
			
			int charge = nbt.getInteger("Charges");
			
			int todo = 100;
			
			ArrayList<BlockPosition> remove = new ArrayList<BlockPosition>();
			
			boolean canWork = true;
			
			if(pos == null || pos.size() == 0)
			{
				canWork = false;
			}
			
			if(canWork)
			{
				for(BlockPosition cu : pos)
				{
					todo--;
					if(todo < 0)
					{
						break;
					}
					
					if(cu.doesBlockExsist() && cu.isThisBlock(new BlockStack(Block.lavaStill), false))
					{
						
						if(cu.worldID.setBlock(cu.xCoord, cu.yCoord, cu.zCoord, 0))
						{
							remove.add(cu);
							charge++;
						}
					}
				}
				nbt.setInteger("Charges", charge);
				pos.removeAll(remove);
				
				
				if(pos.size() > 0 && pos.size() < 100)
				{
					int tick = nbt.getInteger("Tick");
					if(tick > 20)
					{
						pos.clear();
						tick = 0;
					}
					tick++;
					nbt.setInteger("Tick", tick);
				}	
			}

			if(pos != null && pos.size() <= 0 || !canWork)
			{
				workDone = true;
			}
		}
	
		if(workDone)
		{
			if(flag)
			{
				player.sendChatToPlayer(LanguageRegister.createChatMessage("Work done"));
			}
			par1.setItemDamage(2);
		}
	}
	
	public static ItemStack createEmptyNetherCrystal(int itemID)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("Charges", 0);
		nbt.setIntArray("Pos", new int[3]);
		nbt.setString("ID", "Time"+System.nanoTime());
		ItemStack item = new ItemStack(itemID, 1, 0);
		item.setTagInfo("Lava", nbt);
		return item;
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1, EntityPlayer par2EntityPlayer, List par3, boolean par4)
	{
		if(par1 != null && par1.hasTagCompound() && par1.getTagCompound().getCompoundTag("Lava") != null)
		{
			NBTTagCompound nbt = par1.getTagCompound().getCompoundTag("Lava");
			if(nbt != null)
			{
				int damage = par1.getItemDamage();
				if(damage == 1)
				{
					int totalTodo = nbt.getInteger("Size");
					ArrayList<BlockPosition> pos = todo.get(nbt.getString("ID"));
					if(pos != null)
					{
						int todos = pos.size();
						int realTodo = totalTodo - todos;
						
						double real = ((double)realTodo / (double)totalTodo) * 100;
						
						String end = ""+real;
						if(end.length() > 4)
						{
							end = end.substring(0, 4);
						}
						par3.add("Charging Progress: "+end+"%");
					}
					
				}
				else if(damage == 2)
				{
					int remove = nbt.getInteger("RemoveSize");
					int total = replace.get(nbt.getString("ID")).size();
					int totalRemove = remove-total;
					
					double real = ((double)totalRemove / (double)remove) * 100;
					
					String end = ""+real;
					
					end = end.substring(0, 4);
					
					par3.add("Clean Up Progress: "+end+"%");
				}
				else if(damage == 3)
				{
					int charges = nbt.getInteger("Charges");
					par3.add("Charges: "+charges);
				}
			}
		}
	}

	public void createLoadedNetherCrystal(ItemStack stack, EntityPlayer player)
	{
		if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("Lava"))
		{
			stack = createEmptyNetherCrystal(stack.itemID);
		}
		
		MovingObjectPosition move = this.getMovingObjectPositionFromPlayer(player.worldObj, player, true);
		
		if(move == null)
		{
			player.sendChatToPlayer(LanguageRegister.createChatMessage("You need to click a block"));
			return;
		}
		
		World world = player.worldObj;
		
		int range = 40;
		int rangeY = 10;
		
		int all = 0;
		
		NBTTagCompound nbt = stack.getTagCompound().getCompoundTag("Lava");
		
		ArrayList<BlockPosition> pos = new ArrayList<BlockPosition>();
		ArrayList<BlockPosition> rep = new ArrayList<BlockPosition>();
		
		for(int y = move.blockY + rangeY;y>move.blockY-range;y--)
		{
			for(int x = move.blockX - range;x<move.blockX+range;x++)
			{
				for(int z = move.blockZ - range;z<move.blockZ+range;z++)
				{
					if(world.getBlockId(x, y, z) == Block.lavaStill.blockID)
					{
						pos.add(new BlockPosition(world, x, y, z));
						all++;
					}
					if(z+1 == move.blockZ+range || z-1 == move.blockZ-range || x+1 == move.blockX+range || x-1 == move.blockX-range)
					{
						if(y < 32)
						{
							BlockPosition cu = new BlockPosition(world, x, y, z);
							rep.add(cu);
						}
						
					}
				}
			}
		}
		
		if(all > 250)
		{
			todo.put(nbt.getString("ID"), pos);
			replace.put(nbt.getString("ID"), rep);
			int[] min = new int[]{move.blockX-range, move.blockX+range};
			int[] max = new int[]{move.blockZ-range, move.blockZ+range};
			nbt.setIntArray("Pos", new int[]{move.blockX, move.blockY, move.blockZ});
			nbt.setInteger("Size", all);
			nbt.setInteger("RemoveSize", rep.size());
			stack.setItemDamage(1);
			player.sendChatToPlayer(LanguageRegister.createChatMessage("Start Charging Nether Crystal"));
			player.sendChatToPlayer(LanguageRegister.createChatMessage("Please Stay in the Nether or let the Item Dropped (Item Life Time 10 Minutes) until its done and keep all chunks loaded from (x"+min[0]+"-"+max[0]+" z"+min[1]+"-"+max[1]+")"));
		}
		else
		{
			player.sendChatToPlayer(LanguageRegister.createChatMessage("Not Enought Lava"));
		}
		
	}

	@Override
	public int getEntityLifespan(ItemStack itemStack, World world)
	{
		return 12000;
	}
	
}
