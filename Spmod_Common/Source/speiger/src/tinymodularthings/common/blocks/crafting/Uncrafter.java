package speiger.src.tinymodularthings.common.blocks.crafting;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.common.registry.recipes.output.RecipeOutput;
import speiger.src.api.common.registry.recipes.uncrafter.UncrafterRecipeList;
import speiger.src.api.common.utils.WorldReading;
import speiger.src.api.common.world.tiles.energy.EnergyProvider;
import speiger.src.api.common.world.tiles.energy.IEnergyProvider;
import speiger.src.api.common.world.tiles.energy.IEnergySubject;
import speiger.src.spmodapi.common.lib.bc.IStackFilter;
import speiger.src.spmodapi.common.lib.bc.ITransactor;
import speiger.src.spmodapi.common.lib.bc.Transactor;
import speiger.src.spmodapi.common.tile.TileFacing;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.proxy.LangProxy;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import buildcraft.api.power.*;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.transport.IPipeTile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Uncrafter extends TileFacing implements IPowerReceptor, IEnergyProvider, ILaserTarget
{
	private EnergyProvider provider = new EnergyProvider(this, 500);
	private ItemStack currentItem = null;
	private List<ItemStack> results = new ArrayList<ItemStack>();
	private int progress = 0;
	private int maxprogress = 1000;
	private boolean laser = false;
	
	@Override
	public boolean isSixSidedFacing()
	{
		return false;
	}

	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		TextureEngine engine = TextureEngine.getTextures();
		if(side == getFacing() || side == 1)
		{
			return engine.getTexture(TinyBlocks.craftingBlock, 3, 0);
		}
		if(side == 0)
		{
			return engine.getTexture(TinyBlocks.craftingBlock, 3, 1);
		}
		if(renderPass == 1)
		{
			int percent = progress / 100;
			return engine.getTexture(TinyBlocks.craftingBlock, 3, 3+percent);
		}
		else
		{
			return engine.getTexture(TinyBlocks.craftingBlock, 3, 2);
		}
	}

	@Override
	public IEnergySubject getEnergyProvider(ForgeDirection side)
	{
		return provider;
	}
	
	

	@Override
	public void registerIcon(TextureEngine par1, Block par2)
	{
		super.registerIcon(par1, par2);
		String id = "Uncrafter";
		par1.registerTexture(par2, 3, id+"_Bottom", id+"_Top", id+"_Front", 
				id+"_Overlay_0", id+"_Overlay_1", id+"_Overlay_2", id+"_Overlay_3", id+"_Overlay_4", id+"_Overlay_5",
				id+"_Overlay_6", id+"_Overlay_7", id+"_Overlay_8", id+"_Overlay_9");
	}

	@Override
	public PowerReceiver getPowerReceiver(ForgeDirection side)
	{
		if(currentItem == null)
		{
			return null;
		}
		return provider.getSaveBCPowerProvider();
	}
	
	@Override
	public void doWork(PowerHandler workProvider)
	{
		
	}
	
	public ItemStack getCurrentItem()
	{
		return currentItem;
	}
	
	public int getProgress()
	{
		return (int)(((double)progress / (double)maxprogress) * 100D);
	}
	
	@Override
	public void onPlaced(int facing)
	{
		this.setFacing((short)facing);
	}
	
	@Override
	public float getBlockHardness()
	{
		return 4F;
	}

	@Override
	public float getExplosionResistance(Entity par1)
	{
		return 15F;
	}

	@Override
	public void onTick()
	{
		super.onTick();
		if(worldObj.isRemote)
		{
			return;
		}
		provider.update();
		
		if(currentItem != null && results.isEmpty())
		{
			if(provider.useEnergy(20, true) == 20)
			{
				progress+=1;
				provider.useEnergy(20, false);
				if(progress >= maxprogress)
				{
					progress = 0;
					List<RecipeOutput> drops = UncrafterRecipeList.getInstance().getRecipeOutput(currentItem);
					currentItem = null;
					int chance = laser ? 95 : 85;
					if(worldObj.rand.nextInt(100) < chance)
					{
						for(RecipeOutput output : drops)
						{
							if(worldObj.rand.nextInt(100) < output.getChance())
							{
								ItemStack result = output.getOutput();
								if(result != null)
								{
									results.add(result.copy());
								}
							}
						}
					}
				}
			}
			laser = false;
		}
		
		if(results.isEmpty() && currentItem == null)
		{
			TileEntity tile = WorldReading.getTileEntity(worldObj, xCoord, yCoord, zCoord, ForgeDirection.UP.ordinal());
			if(tile != null)
			{
				ITransactor actor = Transactor.getTransactorFor(tile);
				if(actor != null)
				{
					IStackFilter filter = new IStackFilter(){

						@Override
						public boolean matches(ItemStack stack)
						{
							return UncrafterRecipeList.getInstance().hasRecipeOutput(stack);
						}
						
					};
					ItemStack result = actor.remove(filter, 1, ForgeDirection.DOWN, true);
					if(result != null)
					{
						currentItem = result.copy();
					}
				}
			}
		}
		
		if(!results.isEmpty())
		{
			TileEntity tile = WorldReading.getTileEntity(worldObj, xCoord, yCoord, zCoord, getFacing());
			if(tile == null)
			{
				return;
			}
			ITransactor acto = Transactor.getTransactorFor(tile);
			if(acto != null)
			{
				for(int i = 0;i<results.size();i++)
				{
					ItemStack result = results.get(i);
					result.stackSize -= acto.add(result, ForgeDirection.getOrientation(getFacing()).getOpposite(), true).stackSize;
					if(result.stackSize <= 0)
					{
						results.remove(i--);
					}
				}
			}
			else
			{
				if(tile instanceof IPipeTile)
				{
					IPipeTile pipe = (IPipeTile)tile;
					for(ItemStack result : results)
					{
						pipe.injectItem(result, true, ForgeDirection.getOrientation(getFacing()).getOpposite());
					}
					results.clear();
				}
			}
			
		}

	}

	@Override
	public World getWorld()
	{
		return worldObj;
	}

	@Override
	public boolean requiresLaserEnergy()
	{
		return currentItem != null && provider.requestEnergy();
	}

	@Override
	public void receiveLaserEnergy(float energy)
	{	
		provider.addEnergy((int)energy, false);
		laser = true;
	}

	@Override
	public boolean isInvalidTarget()
	{
		return currentItem == null;
	}

	@Override
	public int getXCoord()
	{
		return xCoord;
	}

	@Override
	public int getYCoord()
	{
		return yCoord;
	}

	@Override
	public int getZCoord()
	{
		return zCoord;
	}
	
	@Override
	public void onLoad()
	{
		super.onLoad();
		ILaserTargetRegistry.addTarget(this);
	}

	@Override
	public void onUnload(boolean chunk)
	{
		super.onUnload(chunk);
		ILaserTargetRegistry.removeTarget(this);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		provider.readFromNBT(nbt);
		currentItem = null;
		if(nbt.hasKey("Item"))
		{
			currentItem = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Item"));
		}
		results = new ArrayList<ItemStack>();
		if(nbt.hasKey("Results"))
		{
			NBTTagList list = nbt.getTagList("Results");
			for(int i = 0;i<list.tagCount();i++)
			{
				NBTTagCompound data = (NBTTagCompound)list.tagAt(i);
				ItemStack stack = ItemStack.loadItemStackFromNBT(data);
				if(stack != null)
				{
					results.add(stack);
				}
				
			}
		}
		progress = nbt.getInteger("progress");
		laser = nbt.getBoolean("laser");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		provider.writeToNBT(nbt);
		if(currentItem != null)
		{
			NBTTagCompound data = new NBTTagCompound();
			currentItem.writeToNBT(data);
			nbt.setCompoundTag("Item", data);
		}
		if(!results.isEmpty())
		{
			NBTTagList list = new NBTTagList();
			for(int i = 0;i<results.size();i++)
			{
				NBTTagCompound data = new NBTTagCompound();
				results.get(i).writeToNBT(data);
				list.appendTag(data);
			}
			nbt.setTag("Results", list);
		}
		nbt.setInteger("progress", progress);
		nbt.setBoolean("laser", laser);
	}

	@Override
	public boolean onClick(boolean sneak, EntityPlayer par1, Block par2, int side)
	{
		if(!worldObj.isRemote)
		{
			ItemStack stack = par1.getCurrentEquippedItem();
			if(stack == null)
			{
				if(currentItem != null)
				{
					par1.sendChatToPlayer(LangProxy.getText("Progressing Item: "+currentItem.getDisplayName()));
					double end = ((double)progress / (double) maxprogress) * 100; 
					par1.sendChatToPlayer(LangProxy.getText("Progress at: "+end+"%"));
				}
				else
				{
					par1.sendChatToPlayer(LangProxy.getText("Machine has Nothing to do"));
				}
			}
			else
			{
				if(UncrafterRecipeList.getInstance().hasRecipeOutput(stack))
				{
					par1.sendChatToPlayer(LangProxy.getText("Item has the Following Items as result: "));
					for(RecipeOutput cu : UncrafterRecipeList.getInstance().getRecipeOutput(stack))
					{
						par1.sendChatToPlayer(LangProxy.getText(cu.getOutput().getDisplayName()+" with a chance of "+cu.getChance()+"%"));
					}
				}
				else
				{
					if(currentItem != null)
					{
						par1.sendChatToPlayer(LangProxy.getText("Progressing Item: "+currentItem.getDisplayName()));
						double end = ((double)progress / (double) maxprogress) * 100; 
						par1.sendChatToPlayer(LangProxy.getText("Progress at: "+end+"%"));
					}
					else
					{
						par1.sendChatToPlayer(LangProxy.getText("Machine has Nothing to do"));
					}
				}
			}
		}
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onItemInformation(EntityPlayer par1, List par2, ItemStack par3)
	{
		super.onItemInformation(par1, par2, par3);
		par2.add("Machine has No Gui");
		par2.add("It requires 20k MJ for Each Progress and it uses Max 20MJ");
		par2.add("It Imports from the Top");
		par2.add("And Exports to the Front");
		if(GuiScreen.isCtrlKeyDown())
		{
			par2.add("It has a general Chance for Success of 85%");
			par2.add("With Lasers 95%");
			par2.add("If you Rightclick with an Item on it it shows you what you get out");
		}
		else
		{
			par2.add("Press Ctrl to get Extra Infos");
		}
	}

	@Override
	public void onRenderWorld(Block block, RenderBlocks renderer)
	{
		for(int i = 0;i<2;i++)
		{
			this.setRenderPass(i);
			renderer.renderStandardBlock(block, xCoord, yCoord, zCoord);
		}
	}


}
