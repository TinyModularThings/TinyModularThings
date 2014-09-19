package speiger.src.tinymodularthings.common.items.tools;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidHandler;
import speiger.src.api.items.DisplayStack;
import speiger.src.api.items.IItemGui;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.nbt.NBTHelper;
import speiger.src.api.util.InventoryUtil;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import speiger.src.tinymodularthings.common.items.core.TinyItem;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 
 * @author Alex Ender
 * @Author Speiger (Drasticly changes and code efficency and a few changes)
 *
 */
public class ItemCell extends TinyItem implements IFluidContainerItem
{
	
	Icon[] icon = new Icon[4];
	public int[] array = new int[]{1,5,10,50,100,500,1000};
	public final int capacity;
	
	public ItemCell(int id, int container_capacity)
	{
		super(id);
		capacity = container_capacity;
		setMaxStackSize(1);
		this.setCreativeTab(CreativeTabs.tabFood);
		setUnlocalizedName("cell");
		setHasSubtypes(true);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register)
	{
		icon[0] = register.registerIcon(this.getModID() + ":tools/cell_foreground");
		icon[1] = register.registerIcon(this.getModID() + ":tools/cell_background");
		icon[2] = register.registerIcon(this.getModID() + ":tools/cell_def_empty");
		icon[3] = register.registerIcon(this.getModID() + ":tools/cell_def_full");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int dmg)
	{
		return icon[2];
	}
	
	public Icon getBackgroundIcon()
	{
		return icon[1];
	}
	
	public Icon getFrontgroundIcon()
	{
		return icon[0];
	}
	
	@ForgeSubscribe
	public void PlayerInteract(PlayerInteractEvent event)
	{
		ItemStack stack = event.entityPlayer.getHeldItem();
		if (stack != null && stack.itemID == itemID && event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
		{
			event.setCanceled(true);
		}
	}
	
	public void setFluid(ItemStack is, FluidStack fluid)
	{
		if (fluid != null)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			fluid.writeToNBT(nbt);
			is.setTagInfo("Fluid", nbt);
		}
		else
		{
			NBTHelper.removeTag(is, "Fluid");
		}
	}
	
	public ItemStack fromFluidStack(int id, FluidStack fluid)
	{
		ItemStack stack = new ItemStack(id, 1, 0);
		if (fluid == null)
		{
			return stack;
		}
		if (fluid.amount > capacity)
		{
			fluid.amount = capacity;
		}
		fill(stack, fluid, true);
		return stack;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int id, CreativeTabs tabs, List list)
	{
		list.add(fromFluidStack(id, null));
		
		Map<String, Fluid> fluids = FluidRegistry.getRegisteredFluids();
		if (fluids != null && fluids.size() > 0)
		{
			for (Fluid f : fluids.values())
			{
				if (f != null && f.getName() != null)
				{
					FluidStack fluid = new FluidStack(f, capacity);
					list.add(fromFluidStack(id, fluid));
				}
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
	{
		if (NBTHelper.nbtCheck(stack, "Fluid"))
		{
			FluidStack fluid = getFluid(stack);
			if (fluid == null)
			{
				list.add(EnumChatFormatting.BLUE + "Empty");
			}
			else
			{
				String name = fluid.getFluid().getName();
				String first = name.substring(0, 1);
				name = first.toUpperCase() + name.substring(1);
				list.add(EnumChatFormatting.BLUE + name);
				list.add(EnumChatFormatting.BLUE + String.valueOf(fluid.amount) + " / " + String.valueOf(capacity) + " mB");
			}
		}
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		if(!world.isRemote)
		{
			FluidStack fluid = getFluid(stack);
			MovingObjectPosition obj = getMovingObjectPositionFromPlayer(world, player, true);

			if (!NBTHelper.nbtCheck(stack, "Transport"))
			{
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setInteger("Size", 3);
				stack.setTagInfo("Transport", nbt);
			}
			if (obj == null)
			{
				if (player.isSneaking())
				{
					int size = NBTHelper.getTag(stack, "Transport").getInteger("Size");
					if(size+1 >= array.length)
					{
						size = 0;
					}
					else
					{
						size++;
					}
					NBTHelper.getTag(stack, "Transport").setInteger("Size", size);
				}
				return stack;
			}
			if (obj.typeOfHit == EnumMovingObjectType.TILE)
			{
				int size = array[NBTHelper.getTag(stack, "Transport").getInteger("Size")];
				int x = obj.blockX;
				int y = obj.blockY;
				int z = obj.blockZ;
				
				TileEntity entity = world.getBlockTileEntity(x, y, z);
				
				if (entity instanceof IFluidHandler)
				{
					IFluidHandler handler = (IFluidHandler) entity;
					ForgeDirection side = ForgeDirection.getOrientation(obj.sideHit);
					if (player.isSneaking())
					{
						
						FluidStack drained = drain(stack, size, false);
						if (drained == null || drained.amount == 0)
						{
							return stack;
						}
						int filled = handler.fill(side, drained, false);
						if (filled == 0)
						{
							return stack;
						}
						drained.amount = filled;
						
						drain(stack, filled, true);
						handler.fill(side, drained, true);
					}
					else
					{
						
						FluidStack drained = handler.drain(side, size, false);
						if (drained == null || drained.amount == 0)
						{
							return stack;
						}
						int filled = fill(stack, drained, false, true);
						if (filled == 0)
						{
							return stack;
						}
						if (!InventoryUtil.addItemToPlayer(stack, player))
						{
							return stack;
						}
						drained.amount = filled;
						handler.drain(side, filled, true);
						fill(stack, drained, true, false);
					}
					
					return stack;
				}
				if (!world.canMineBlock(player, x, y, z))
				{
					return stack;
				}
				
				
				if (player.isSneaking())
				{
					switch (obj.sideHit)
					{
						case 0:
							--y;
							break;
						case 1:
							++y;
							break;
						case 2:
							--z;
							break;
						case 3:
							++z;
							break;
						case 4:
							--x;
							break;
						case 5:
							++x;
							break;
					}
					
					if (!player.canPlayerEdit(x, y, z, obj.sideHit, stack))
					{
						return stack;
					}
					
					Material material = world.getBlockMaterial(x, y, z);
					
					if (world.isAirBlock(x, y, z) || !material.isSolid())
					{
						
						FluidStack drained = drain(stack, FluidContainerRegistry.BUCKET_VOLUME, false);
						if (drained != null && drained.getFluid().canBePlacedInWorld() && drained.amount == FluidContainerRegistry.BUCKET_VOLUME)
						{
							drain(stack, FluidContainerRegistry.BUCKET_VOLUME, true);
							if (!world.isRemote && !material.isLiquid())
							{
								world.destroyBlock(x, y, z, true);
							}
							int id = drained.getFluid().getBlockID();
							if (id == Block.waterStill.blockID)
							{
								id = Block.waterMoving.blockID;
							}
							if (id == Block.lavaStill.blockID)
							{
								id = Block.lavaMoving.blockID;
							}
							world.setBlock(x, y, z, id, 0, 3);
						}
						return stack;
					}
					
				}
				else
				{
					if (!player.canPlayerEdit(x, y, z, obj.sideHit, stack))
					{
						return stack;
					}
					
					Block block = Block.blocksList[world.getBlockId(x, y, z)];
					
					if (block instanceof IFluidBlock)
					{
						IFluidBlock fluid_block = (IFluidBlock) block;
						if (!fluid_block.canDrain(world, x, y, z))
						{
							return stack;
						}
						FluidStack drained = fluid_block.drain(world, x, y, z, false);
						if (drained == null)
						{
							return stack;
						}
						int filled = fill(stack, drained, false, true);
						if (filled != drained.amount)
						{
							return stack;
						}
						if (!InventoryUtil.addItemToPlayer(stack, player))
						{
							return stack;
						}
						fluid_block.drain(world, x, y, z, true);
						fill(stack, drained, true, false);
						
						return stack;
					}
					
					if (world.getBlockMaterial(x, y, z) == Material.water && world.getBlockMetadata(x, y, z) == 0)
					{
						FluidStack fill = new FluidStack(FluidRegistry.WATER, FluidContainerRegistry.BUCKET_VOLUME);
						if (fill(stack, fill, false, true) == FluidContainerRegistry.BUCKET_VOLUME)
						{
							if (!InventoryUtil.addItemToPlayer(stack, player))
							{
								return stack;
							}
							fill(stack, fill, true, false);
							world.setBlockToAir(x, y, z);
						}
						
						return stack;
					}
					
					if (world.getBlockMaterial(x, y, z) == Material.lava && world.getBlockMetadata(x, y, z) == 0)
					{
						FluidStack fill = new FluidStack(FluidRegistry.LAVA, FluidContainerRegistry.BUCKET_VOLUME);
						if (fill(stack, fill, false, true) == FluidContainerRegistry.BUCKET_VOLUME)
						{
							FMLLog.getLogger().info("Test");
							if (!InventoryUtil.addItemToPlayer(stack, player))
							{
								return stack;
							}
							fill(stack, fill, true, false);
							world.setBlockToAir(x, y, z);
						}
						
						return stack;
					}
				}
			}
		}
		return stack;
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		FluidStack fluid = getFluid(stack);
		if (fluid == null)
		{
			return 64;
		}
		return 1;
	}
	
	public ItemStack EmptyContainer(int stack_size)
	{
		ItemStack stack = new ItemStack(itemID, stack_size, 0);
		setFluid(stack, null);
		return stack;
	}
	
	// ILanguageItem functions
		
	@Override
	public void registerItems(int id, SpmodMod par0)
	{
		if(!SpmodModRegistry.areModsEqual(par0, getMod()))
		{
			return;
		}
		LanguageRegister.getLanguageName(new DisplayStack(new ItemStack(id, 1, 0)), "cell.fluid", par0);
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod par0)
	{
		return LanguageRegister.getLanguageName(new DisplayStack(par1), "cell.fluid", par0);
	}
	
	// IFluidContainerItem function
	
	@Override
	public FluidStack getFluid(ItemStack stack)
	{
		if (!NBTHelper.nbtCheck(stack, "Fluid"))
		{
			return null;
		}
		return FluidStack.loadFluidStackFromNBT(NBTHelper.getTag(stack, "Fluid"));
	}
	
	@Override
	public int getCapacity(ItemStack container)
	{
		return capacity;
	}
	
	@Override
	public int fill(ItemStack container, FluidStack resource, boolean doFill)
	{
		return fill(container, resource, doFill, false);
	}
	
	@Override
	public FluidStack drain(ItemStack stack, int amount, boolean do_drain)
	{
		if (stack.stackSize > 1)
		{
			return null;
		}
		FluidStack fluid = getFluid(stack);
		
		if (fluid == null)
		{
			return null;
		}
		
		int drained = amount;
		if (fluid.amount < drained)
		{
			drained = fluid.amount;
		}
		FluidStack drain = new FluidStack(fluid, drained);
		if (do_drain)
		{
			fluid.amount -= drained;
			if (fluid.amount <= 0)
			{
				fluid = null;
			}
			setFluid(stack, fluid);
			
		}
		return drain;
	}
	
	public int fill(ItemStack stack, FluidStack fluid, boolean do_fill, boolean ignore_stacksize)
	{
		if (!ignore_stacksize && stack.stackSize > 1)
		{
			return 0;
		}
		FluidStack storedFluid = getFluid(stack);
		if (storedFluid == null)
		{
			int added = Math.min(fluid.amount, capacity);
			if (do_fill)
			{
				fluid.amount = added;
				setFluid(stack, fluid);
			}
			return added;
		}
		else
		{
			if(capacity == storedFluid.amount)
			{
				return 0;
			}
			if(capacity < storedFluid.amount + fluid.amount)
			{
				return fluid.amount - (storedFluid.amount + fluid.amount - capacity);
			}
			
			int newFluid = Math.min(capacity, storedFluid.amount + fluid.amount);
			int added = storedFluid.amount + (fluid.amount - storedFluid.amount);
			if (do_fill)
			{
				storedFluid.amount = newFluid;
				setFluid(stack, storedFluid);
			}
			
			return added;
		}
	}



}
