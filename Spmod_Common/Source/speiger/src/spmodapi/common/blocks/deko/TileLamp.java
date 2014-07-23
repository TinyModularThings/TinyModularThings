package speiger.src.spmodapi.common.blocks.deko;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.util.RedstoneUtils;
import speiger.src.spmodapi.common.enums.EnumColor;
import speiger.src.spmodapi.common.enums.EnumColor.SpmodColor;
import speiger.src.spmodapi.common.lib.SpmodAPILib;
import speiger.src.spmodapi.common.modHelper.BC.ActionAdvLoop;
import speiger.src.spmodapi.common.modHelper.BC.ActionChange;
import speiger.src.spmodapi.common.modHelper.BC.ActionRandomLoop;
import speiger.src.spmodapi.common.modHelper.BC.ColorChangeAdv;
import speiger.src.spmodapi.common.tile.TileFacing;
import buildcraft.BuildCraftCore;
import buildcraft.api.gates.IAction;
import buildcraft.api.gates.IActionReceptor;
import buildcraft.api.tools.IToolWrench;
import cpw.mods.fml.common.network.PacketDispatcher;

public class TileLamp extends TileFacing implements IActionReceptor
{
	public ArrayList<EnumColor> validColors = new ArrayList<EnumColor>();
	public ArrayList<EnumColor> roubinColor = new ArrayList<EnumColor>();
	public int color = 0;
	public int meta = 0;
	public EnumLampType lampType = null;
	public boolean isActive = false;
	public boolean inverted = false;
	public boolean allColored = false;
	public boolean noneColored = false;
	public boolean power = false;
	
	public TileLamp()
	{
	}

	@Override
	public boolean isSixSidedFacing()
	{
		return true;
	}
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		return null;
	}
	
	public int getColor()
	{
		return color; 
	}
	
	public SpmodColor getFullColor()
	{
		if(color == 16)
		{
			return null;
		}
		int rc = EnumColor.values()[this.color].getAsHex().intValue();
		if((!isActive() && !isInverted()) || (isActive() && isInverted()))
		{
			return SpmodColor.fromHex(rc).add(0.3D);
		}
		else
		{
			return SpmodColor.fromHex(rc).add(1.4D);
		}
	}
	
	public boolean isActive()
	{
		return this.isActive;
	}

	
	
	
	@Override
	public void onTick()
	{
		if(worldObj.isRemote || worldObj.getWorldTime() % 2 != 0)
		{
			return;
		}
		
		
		if(isActive != power)
		{
			isActive = power;
			PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, getDescriptionPacket());
			this.updateBlock();
			this.updateLight();
		}
		
		boolean powered = RedstoneUtils.isBlockGettingPowered(this);
		
		
	
		if(power != powered)
		{
			this.power = powered;
			PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, getDescriptionPacket());
			this.updateBlock();
			this.updateLight();
		}
	}
	
	

	@Override
	public boolean onActivated(EntityPlayer par1)
	{
		ItemStack current = par1.getCurrentEquippedItem();
		if(!worldObj.isRemote)
		{
			if(current != null)
			{
				if(current.getItem() instanceof IToolWrench && (this.allColored || this.noneColored) && ((IToolWrench)current.getItem()).canWrench(par1, xCoord, yCoord, zCoord) && !par1.isSneaking())
				{
					if(this.roubinColor.isEmpty())
					{
						this.refillRoundRobin();
					}
					if(this.roubinColor.isEmpty())
					{
						return false;
					}
					this.setColor(roubinColor.remove(0).ordinal());
					((IToolWrench)current.getItem()).wrenchUsed(par1, xCoord, yCoord, zCoord);
				}
				else if(current.getItem() instanceof IToolWrench && ((IToolWrench)current.getItem()).canWrench(par1, xCoord, yCoord, zCoord) && par1.isSneaking())
				{
					this.setFacing(this.setNextFacing());
					this.updateBlock();
					((IToolWrench)current.getItem()).wrenchUsed(par1, xCoord, yCoord, zCoord);
				}
				else if(current.itemID == Item.dyePowder.itemID)
				{
					int meta = current.getItemDamage();
					if(this.noneColored)
					{
						boolean consume = this.validColors.contains(EnumColor.values()[meta]);
						if(this.addColor(EnumColor.values()[meta]) && !consume)
						{
							par1.getCurrentEquippedItem().stackSize--;
						}
						return false;
					}
					else if(this.allColored)
					{
						this.setColor(meta);
					}
					else
					{
						int newMeta = this.meta;
						newMeta -= this.color;
						this.setColor(meta);
						newMeta += this.color;
						this.meta = newMeta;
						par1.getCurrentEquippedItem().stackSize--;
					}
				}
			}
		}
		return false;
	}

	public void setColor(int color)
	{
		boolean change = this.color != color;
		this.color = color;
		if(change)
		{
			updateBlock();
			PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 30, this.worldObj.provider.dimensionId, getDescriptionPacket());
		}
	}
	
	public boolean addColor(EnumColor par1)
	{
		if(this.validColors.contains(par1))
		{
			if(color != par1.ordinal())
			{
				this.setColor(par1.ordinal());
				return true;
			}
		}
		else
		{
			validColors.add(par1);
			if(color != par1.ordinal())
			{
				this.setColor(par1.ordinal());
				return true;
			}
		}
		
		return false;
	}
	
	
	public void setInverted()
	{
		this.inverted = true;
	}
	
	public boolean isInverted()
	{
		return inverted;
	}
	
	public void setAllColored()
	{
		this.allColored = true;
		this.validColors.addAll(Arrays.asList(EnumColor.values()));
		this.validColors.remove(EnumColor.Nothing);
	}
	
	public void setNoneColored()
	{
		this.noneColored = true;
	}
	
	public void setType(int mode)
	{
		this.lampType = EnumLampType.values()[mode];	
	}
	
	public EnumLampType getType()
	{
		return this.lampType;
	}
	
	public void refillRoundRobin()
	{
		this.roubinColor.clear();
		this.roubinColor.addAll(validColors);
	}

	public void setMetadata(int meta)
	{
		this.meta = meta;
	}
	
	@Override
	public ArrayList<ItemStack> onDrop(int fortune)
	{
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		ItemStack drop = new ItemStack(this.getBlockType(), 1, meta);
		if(this.noneColored)
		{
			NBTTagList list = new NBTTagList();
			for(EnumColor color : this.validColors)
			{
				NBTTagInt cuColor = new NBTTagInt("Color", color.ordinal());
				list.appendTag(cuColor);
			}
			drop.setTagInfo("Colors", list);
		}
		
		drops.add(drop);
		return drops;
	}

	@Override
	public ItemStack pickBlock(MovingObjectPosition target)
	{
		if(this.noneColored)
		{
			ItemStack drop = new ItemStack(this.getBlockType(), 1, meta);
			NBTTagList list = new NBTTagList();
			for(EnumColor color : this.validColors)
			{
				NBTTagInt cuColor = new NBTTagInt("Color", color.ordinal());
				list.appendTag(cuColor);
			}
			drop.setTagInfo("Colors", list);
			return drop;
		}
		return new ItemStack(this.getBlockType(), 1, meta);
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, nbt);
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
	{
		this.readFromNBT(pkt.data);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		allColored = nbt.getBoolean("All");
		inverted = nbt.getBoolean("Inverted");
		isActive = nbt.getBoolean("Active");
		noneColored = nbt.getBoolean("NoColor");
		power = nbt.getBoolean("Power");
		lampType = EnumLampType.values()[nbt.getInteger("Type")];
		meta = nbt.getInteger("Meta");
		color = nbt.getInteger("Color");
		NBTTagList round = nbt.getTagList("Round");
		for(int i = 0;i<round.tagCount();i++)
		{
			NBTTagInt ints = (NBTTagInt) round.tagAt(i);
			roubinColor.add(EnumColor.values()[ints.data]);
		}
		NBTTagList valids = nbt.getTagList("Colors");
		for(int i = 0;i<valids.tagCount();i++)
		{
			NBTTagInt ints = (NBTTagInt) valids.tagAt(i);
			this.validColors.add(EnumColor.values()[ints.data]);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setBoolean("All", allColored);
		nbt.setBoolean("Inverted", inverted);
		nbt.setBoolean("Active", isActive);
		nbt.setBoolean("NoColor", noneColored);
		nbt.setBoolean("Power", power);
		nbt.setInteger("Type", lampType.ordinal());
		nbt.setInteger("Meta", meta);
		nbt.setInteger("Color", color);
		NBTTagList round = new NBTTagList();
		for(EnumColor color : this.roubinColor)
		{
			NBTTagInt ints = new NBTTagInt("Color", color.ordinal());
			round.appendTag(ints);
		}
		nbt.setTag("Round", round);
		NBTTagList valids = new NBTTagList();
		for(EnumColor color : this.validColors)
		{
			NBTTagInt ints = new NBTTagInt("Color", color.ordinal());
			valids.appendTag(ints);
		}
		nbt.setTag("Colors", valids);
	}


	public void changeAndSendColor(int colorID)
	{
		if(this.allColored)
		{
			if(this.color != colorID)
			{
				this.setColor(colorID);
				for(ForgeDirection side : ForgeDirection.VALID_DIRECTIONS)
				{
					if(lampType == lampType.RP2CAGELAMP)
					{
						if(side.ordinal() != this.getFacing())
						{
							continue;
						}
					}
					TileEntity tile = worldObj.getBlockTileEntity(xCoord+side.offsetX, yCoord+side.offsetY, zCoord+side.offsetZ);
					if(tile != null && tile instanceof TileLamp && tile.getBlockType().isBlockSolidOnSide(worldObj, tile.xCoord, tile.yCoord, tile.zCoord, side.getOpposite()))
					{
						((TileLamp)tile).changeAndSendColor(colorID);
					}
				}
			}
		}
		if(this.noneColored)
		{
			if(validColors.contains(EnumColor.values()[colorID]))
			{
				if(this.color != colorID)
				{
					this.setColor(colorID);
					for(ForgeDirection side : ForgeDirection.VALID_DIRECTIONS)
					{
						TileEntity tile = worldObj.getBlockTileEntity(xCoord+side.offsetX, yCoord+side.offsetY, zCoord+side.offsetZ);
						if(lampType == lampType.RP2CAGELAMP)
						{
							if(side.ordinal() != this.getFacing())
							{
								continue;
							}
						}
						
						if(tile != null && tile instanceof TileLamp && tile.getBlockType().isBlockSolidOnSide(worldObj, tile.xCoord, tile.yCoord, tile.zCoord, side.getOpposite()))
						{
							((TileLamp)tile).changeAndSendColor(colorID);
						}
					}
				}
			}
		}
	}








	public static enum EnumLampType
	{
		FULL(new ResourceLocation(SpmodAPILib.ModID.toLowerCase()+":textures/models/blocks/ModelFullLamp.png"), 0, "Full", false),
		RP2CAGELAMP(new ResourceLocation(SpmodAPILib.ModID.toLowerCase()+":textures/models/blocks/ModelCageLamp.png"), 1, "RPCageLamp", true);
		
		ResourceLocation texture;
		int render;
		String name;
		boolean facing;
		
		private EnumLampType(ResourceLocation texture, int renderType, String name, boolean hasFacing)
		{
			this.texture = texture;
			this.render = renderType;
			this.name = name;
			facing = hasFacing;
		}
		
		public String getName()
		{
			return name;
		}
		
		public ResourceLocation getTexture()
		{
			return texture;
		}
		
		public int getRenderType()
		{
			return render;
		}

		public boolean hasFacing()
		{
			return facing;
		}
	}




	public void changeActiveState(boolean par1)
	{
		if(power == par1)
		{
			return;
		}
		this.isActive = par1;
		power = par1;
		for(ForgeDirection side : ForgeDirection.VALID_DIRECTIONS)
		{
			TileEntity tile = worldObj.getBlockTileEntity(xCoord+side.offsetX, yCoord+side.offsetY, zCoord+side.offsetZ);
			if(lampType == lampType.RP2CAGELAMP)
			{
				if(side.ordinal() != this.getFacing())
				{
					continue;
				}
			}
			if(tile != null && tile instanceof TileLamp && tile.getBlockType().isBlockSolidOnSide(worldObj, tile.xCoord, tile.yCoord, tile.zCoord, side.getOpposite()))
			{
				((TileLamp)tile).changeActiveState(par1);
			}
		}
		this.updateBlock();
		this.updateLight();
	}



	@Override
	public void actionActivated(IAction action)
	{
		if(action != null && !worldObj.isRemote)
		{
			try
			{
				if(action == BuildCraftCore.actionOff)
				{
					this.isActive = false;
					power = true;
				}
				else if(action == BuildCraftCore.actionOn)
				{
					this.isActive = true;
					power = true;
				}
				else if(action instanceof ActionChange)
				{
					ActionChange change = (ActionChange) action;
					this.changeActiveState(change.active());
				}
				else if(action instanceof ColorChangeAdv)
				{
					ColorChangeAdv colors = (ColorChangeAdv)action;
					if(colors.adv)
					{
						this.changeAndSendColor(colors.color.ordinal());
					}
					else
					{
						this.setColor(colors.color.ordinal());
					}
				}
				else if(action instanceof ActionAdvLoop)
				{
					ActionAdvLoop loop = (ActionAdvLoop) action;
					if(loop.allBlocks())
					{
						if(worldObj.getWorldTime() % loop.getTickRate() == 0)
						{
							if(this.roubinColor.isEmpty())
							{
								this.refillRoundRobin();
							}
							if(this.roubinColor.isEmpty())
							{
								return;
							}
							if(this.roubinColor.get(0).ordinal() != this.color)
							{
								this.changeAndSendColor(this.roubinColor.remove(0).ordinal());
							}
							else if(this.roubinColor.size() == 1)
							{
								this.refillRoundRobin();
							}
							else
							{
								this.roubinColor.add(this.roubinColor.remove(0));
							}
						}
					}
					else
					{
						
						if(worldObj.getWorldTime() % loop.getTickRate() == 0)
						{
							if(this.roubinColor.isEmpty())
							{
								this.refillRoundRobin();
							}
							if(this.roubinColor.isEmpty())
							{
								return;
							}
							if(this.roubinColor.get(0).ordinal() != this.color)
							{
								this.setColor(this.roubinColor.remove(0).ordinal());
							}
							else if(this.roubinColor.size() == 1)
							{
								this.refillRoundRobin();
							}
							else
							{
								this.roubinColor.add(this.roubinColor.remove(0));
							}
						}
					}
				}
				else if(action instanceof ActionRandomLoop)
				{
					ActionRandomLoop loop = (ActionRandomLoop) action;
					if(loop.randOnly)
					{
						if(worldObj.getWorldTime() % loop.getDelay() == 0)
						{
							int id = worldObj.rand.nextInt(validColors.size());
							EnumColor colors = this.validColors.get(id);
							if(this.color != colors.ordinal())
							{
								if(loop.allBlocks())
								{
									this.changeAndSendColor(colors.ordinal());
								}
								else
								{
									this.color = colors.ordinal();	
								}
								
							}
						}
					}
					else
					{
						if(worldObj.getWorldTime() % loop.getDelay() == 0)
						{
							if(this.roubinColor.size() == 0)
							{
								this.refillRoundRobin();
							}
							if(this.roubinColor.isEmpty())
							{
								return;
							}
							
							int id = worldObj.rand.nextInt(roubinColor.size());
							EnumColor colors = this.roubinColor.get(id);
							if(this.color != colors.ordinal())
							{
								if(loop.allBlocks())
								{
									this.changeAndSendColor(colors.ordinal());
								}
								else
								{
									this.color = colors.ordinal();	
								}
							}
							else if(this.roubinColor.size() == 1)
							{
								this.refillRoundRobin();
							}
							else
							{
								this.roubinColor.add(this.roubinColor.remove(id));
							}
						}
					}
				}
			}
			catch (Exception e)
			{
				
			}
		}
	}
	


	
}
