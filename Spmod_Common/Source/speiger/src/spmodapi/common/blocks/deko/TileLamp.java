package speiger.src.spmodapi.common.blocks.deko;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.util.WorldReading;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.enums.EnumColor;
import speiger.src.spmodapi.common.enums.EnumColor.SpmodColor;
import speiger.src.spmodapi.common.lib.SpmodAPILib;
import speiger.src.spmodapi.common.plugins.BC.ActionAdvLoop;
import speiger.src.spmodapi.common.plugins.BC.ActionChange;
import speiger.src.spmodapi.common.plugins.BC.ActionRandomLoop;
import speiger.src.spmodapi.common.plugins.BC.ColorChangeAdv;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.tile.TileFacing;
import speiger.src.spmodapi.common.util.proxy.CodeProxy;
import buildcraft.BuildCraftCore;
import buildcraft.api.gates.IAction;
import buildcraft.api.gates.IActionReceptor;
import buildcraft.api.tools.IToolWrench;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileLamp extends TileFacing implements IActionReceptor
{
	static double[][] sizes = new double[][] { 
		{0.125D, 0D, 0.125D, 0.875D, 0.735D, 0.875D }, 
		{0.125D, 0.265D, 0.125D, 0.875D, 1D, 0.875D }, 
		{0.125D, 0.125D, 0D, 0.875D, 0.875D, 0.735D }, 
		{0.125D, 0.125D, 0.265D, 0.875D, 0.875D, 1D }, 
		{0D, 0.125D, 0.125D, 0.735D, 0.875D, 0.875D }, 
		{0.265D, 0.125D, 0.125D, 1D, 0.875D, 0.875D }};
	
	public ArrayList<EnumColor> installedColor = new ArrayList<EnumColor>();
	public ArrayList<EnumColor> roundRoubinMode = new ArrayList<EnumColor>();
	public EnumLampType type = EnumLampType.Nothing;
	public SpmodColor color = new SpmodColor();
	public boolean isActive = false;
	public boolean isInverted = false;
	public boolean isNoneColored = false;
	public boolean isAllColored = false;
	public int meta = 0;
	
	
	
	
	
	@Override
	public void onTick()
	{
		if(worldObj.isRemote || worldObj.getWorldTime() % 2 == 0)
		{
			return;
		}
		if(isActive() != isActive)
		{
			changeState(isActive);
		}
		
		boolean powered = isPowered();
		
		if(isActive != powered)
		{
			isActive = powered;
		}
		
		if(worldObj.getWorldTime() % 20 == 0)
		{
			PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, getDescriptionPacket());
			this.updateLight();
			this.updateBlock();
		}
	}
	
	public EnumLampType getType()
	{
		return type;
	}
	
	
	@Override
	public float getBlockHardness()
	{
		return 4F;
	}

	public boolean isInverted()
	{
		return isInverted;
	}
	
	public void setInverted()
	{
		this.isInverted = true;
	}
	
	public void setType(int lamp)
	{
		type = EnumLampType.values()[lamp];
	}
	
	public void setNoneColored()
	{
		this.isNoneColored = true;
	}
	
	public void setColor(int colorID)
	{
		color = new SpmodColor(EnumColor.values()[colorID].getAsHex());
	}
	
	public void setAllColored()
	{
		this.isAllColored = true;
		this.installedColor.addAll(Arrays.asList(EnumColor.values()));
		this.installedColor.remove(EnumColor.Nothing);
	}
	
	public void setMetadata(int meta)
	{
		this.meta = meta;
	}
	
	public void addColor(EnumColor par1)
	{
		SpmodColor colors = new SpmodColor(par1.getAsHex());
		if(color != colors)
		{
			setColor(par1.ordinal());
		}
		if(!installedColor.contains(par1))
		{
			installedColor.add(par1);
		}
	}
	
	@Override
	public void setBoundsOnState(Block par1)
	{
		if(type == EnumLampType.RP2CAGELAMP)
		{
			switch (getFacing())
			{
				case 0:
					par1.setBlockBounds(0.125F, 0F, 0.125F, 0.875F, 0.735F, 0.875F);
					break;
				case 1:
					par1.setBlockBounds(0.125F, 0.265F, 0.125F, 0.875F, 1F, 0.875F);
					break;
				case 2:
					par1.setBlockBounds(0.125F, 0.125F, 0F, 0.875F, 0.875F, 0.735F);
					break;
				case 3:
					par1.setBlockBounds(0.125F, 0.125F, 0.265F, 0.875F, 0.875F, 1F);
					break;
				case 4:
					par1.setBlockBounds(0F, 0.125F, 0.125F, 0.735F, 0.875F, 0.875F);
					break;
				case 5:
					par1.setBlockBounds(0.265F, 0.125F, 0.125F, 1F, 0.875F, 0.875F);
					break;
				default:
					par1.setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
			}
		}
		else
		{
			super.setBoundsOnState(par1);
		}
	}

	@Override
	public AxisAlignedBB getSelectedBoxes()
	{
		if(type == EnumLampType.RP2CAGELAMP)
		{
			double[] array = sizes[facing];
			return AxisAlignedBB.getAABBPool().getAABB(xCoord + array[0], yCoord + array[1], zCoord + array[2], xCoord + array[3], yCoord + array[4], zCoord + array[5]);
		}
		return super.getSelectedBoxes();
	}

	@Override
	public AxisAlignedBB getColidingBox()
	{
		if(type == EnumLampType.RP2CAGELAMP)
		{
			double[] array = sizes[facing];
			return AxisAlignedBB.getAABBPool().getAABB(xCoord + array[0], yCoord + array[1], zCoord + array[2], xCoord + array[3], yCoord + array[4], zCoord + array[5]);
		}
		return super.getColidingBox();
	}

	@Override
	public int getBlockLightLevel()
	{
		if((this.isActive() && !this.isInverted()) || (!this.isActive() && this.isInverted()))
		{
			return 15;
		}
		return super.getBlockLightLevel();
	}
	
	@Override
	public boolean hasContainer()
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public SpmodColor getColor()
	{
		return color;
	}
	
	@Override
	public int getLightOpactiy()
	{
		if(type == EnumLampType.FULL)
		{
			return 255;
		}
		return super.getLightOpactiy();
	}
	
	@Override
	public boolean SolidOnSide(ForgeDirection side)
	{
		if(type == EnumLampType.RP2CAGELAMP)
		{
			return side.ordinal() == this.getFacing();
		}
		return super.SolidOnSide(side);
	}
	
	@Override
	public boolean canConnectToWire(int side)
	{
		if(type == EnumLampType.FULL)
		{
			return true;
		}
		else if(type == EnumLampType.RP2CAGELAMP)
		{
			return side == this.getFacing();
		}
		return false;
	}
	
	public boolean isActive()
	{
		return this.getBlockMetadata() == 1;
	}
	
	public void changeState(boolean active)
	{
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, active ? 1 : 0, 3);
		this.updateNeighbors(true);
		this.updateBlock();
		this.updateLight();
	}
	
	public void refillRoundRobin()
	{
		this.roundRoubinMode.clear();
		this.roundRoubinMode.addAll(installedColor);
	}

	@Override
	public boolean onClick(boolean sneak, EntityPlayer par1, Block par2, int side)
	{
		if(!worldObj.isRemote)
		{
			ItemStack stack = par1.getCurrentEquippedItem();
			if(stack != null)
			{
				if(stack.getItem() instanceof IToolWrench)
				{
					IToolWrench tool = (IToolWrench)stack.getItem();
					
					if(tool.canWrench(par1, xCoord, yCoord, zCoord))
					{
						if(!sneak && (this.isAllColored || this.isNoneColored))
						{
							if(this.roundRoubinMode.isEmpty())
							{
								this.refillRoundRobin();
							}
							if(this.roundRoubinMode.isEmpty())
							{
								return false;
							}
							setColor(roundRoubinMode.remove(0).ordinal());
							tool.wrenchUsed(par1, xCoord, yCoord, zCoord);
						}
						else if(sneak && type == EnumLampType.RP2CAGELAMP)
						{
							this.setFacing(this.setNextFacing());
							this.updateBlock();
							tool.wrenchUsed(par1, xCoord, yCoord, zCoord);
						}
					}
				}
				else if(CodeProxy.isDye(stack))
				{
					if(isAllColored || isNoneColored)
					{
						this.addColor(CodeProxy.getColorFromItemStack(stack));
					}
					else
					{
						int oldMeta = this.meta;
						oldMeta -= color.getColor().ordinal();
						int color = CodeProxy.getColorFromItemStack(stack).ordinal();
						this.setColor(color);
						oldMeta += color;
						meta = oldMeta;
						stack.stackSize--;
					}
				}
			}

		}
		return super.onClick(sneak, par1, par2, side);
	}

	@Override
	public ArrayList<ItemStack> onDrop(int fortune)
	{
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		if(isNoneColored)
		{
			ItemStack drop = new ItemStack(APIBlocks.hempLamp, 1, meta);
			NBTTagList list = new NBTTagList();
			for (EnumColor color : this.installedColor)
			{
				NBTTagInt cuColor = new NBTTagInt("Color", color.ordinal());
				list.appendTag(cuColor);
			}
			drop.setTagInfo("Colors", list);
			drops.add(drop);
		}
		else
		{
			drops.add(new ItemStack(APIBlocks.hempLamp, 1, meta));
		}
		return drops;
	}

	@Override
	public ItemStack pickBlock(MovingObjectPosition target)
	{
		if (this.isNoneColored)
		{
			ItemStack drop = new ItemStack(APIBlocks.hempLamp, 1, meta);
			NBTTagList list = new NBTTagList();
			for (EnumColor color : this.installedColor)
			{
				NBTTagInt cuColor = new NBTTagInt("Color", color.ordinal());
				list.appendTag(cuColor);
			}
			drop.setTagInfo("Colors", list);
			return drop;
		}
		return new ItemStack(getBlockType(), 1, meta);
	}

	@Override
	public boolean isSixSidedFacing()
	{
		return type.hasFacing();
	}
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		return Block.redstoneLampActive.getIcon(0, 0);
	}
	
	@Override
	public void actionActivated(IAction action)
	{
		if(!worldObj.isRemote && action != null)
		{
			try
			{
				if(action == BuildCraftCore.actionOff)
				{
					isActive = false;
				}
				else if(action == BuildCraftCore.actionOn)
				{
					isActive = true;
				}
				else if(action instanceof ActionChange)
				{
					changeGateState(((ActionChange)action).active(), ForgeDirection.UNKNOWN);
				}
				else if(action instanceof ColorChangeAdv)
				{
					ColorChangeAdv colors = (ColorChangeAdv) action;
					if(colors.adv)
					{
						this.sendColor(colors.color.ordinal(), ForgeDirection.UNKNOWN);
					}
					else
					{
						this.setColor(colors.color.ordinal());
					}
				}
				else if(action instanceof ActionAdvLoop)
				{
					ActionAdvLoop loop = (ActionAdvLoop) action;
					if(worldObj.getWorldTime() % loop.getTickRate() == 0)
					{
						if (this.roundRoubinMode.isEmpty())
						{
							this.refillRoundRobin();
						}
						if (this.roundRoubinMode.isEmpty())
						{
							return;
						}
						if (this.roundRoubinMode.get(0).ordinal() != this.color.getColor().ordinal())
						{
							if(loop.allBlocks())
							{
								this.sendColor(this.roundRoubinMode.remove(0).ordinal(), ForgeDirection.UNKNOWN);
							}
							else
							{
								this.setColor(this.roundRoubinMode.remove(0).ordinal());
							}
						}
						else if (this.roundRoubinMode.size() == 1)
						{
							this.refillRoundRobin();
						}
						else
						{
							this.roundRoubinMode.add(this.roundRoubinMode.remove(0));
						}
					}
				}
				else if(action instanceof ActionRandomLoop)
				{
					ActionRandomLoop loop = (ActionRandomLoop) action;
					if(worldObj.getWorldTime() % loop.getDelay() == 0)
					{
						EnumColor result = EnumColor.Nothing;
						if(loop.randOnly)
						{
							result = installedColor.get(worldObj.rand.nextInt(installedColor.size()));
						}
						else
						{
							if(this.roundRoubinMode.isEmpty())
							{
								this.refillRoundRobin();
								Collections.shuffle(roundRoubinMode);
							}
							if(this.roundRoubinMode.isEmpty())
							{
								return;
							}
							result = this.roundRoubinMode.remove(0);
						}
						if(loop.allBlocks())
						{
							this.sendColor(result.ordinal(), ForgeDirection.UNKNOWN);
						}
						else
						{
							this.setColor(result.ordinal());
						}
					}
				}
				
			}
			catch(Exception e)
			{
			}
		}
	}
	
	public void sendColor(int color, ForgeDirection sender)
	{
		if(this.isAllColored || this.isNoneColored)
		{
			if(color == this.color.getColor().ordinal())
			{
				return;
			}
			
			EnumColor data = EnumColor.values()[color];
			if(this.installedColor.contains(data))
			{
				this.setColor(color);
				for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
				{
					if(dir.ordinal() == sender.ordinal() || !this.SolidOnSide(dir))
					{
						continue;
					}
					AdvTile tile = WorldReading.getAdvTile(worldObj, xCoord, yCoord, zCoord, dir);
					if(tile != null && tile instanceof TileLamp)
					{
						TileLamp lamp = (TileLamp)tile;
						if(lamp.SolidOnSide(dir.getOpposite()))
						{
							lamp.sendColor(color, dir.getOpposite());
						}
					}
				}
				this.updateBlock();
			}
		}
	}
	
	public void changeGateState(boolean par1, ForgeDirection sender)
	{
		if(isActive == par1)
		{
			return;
		}
		isActive = par1;
		if(this.isActive() != par1)
		{
			this.changeState(par1);
		}
		for(ForgeDirection side : ForgeDirection.VALID_DIRECTIONS)
		{
			if(side.ordinal() == sender.ordinal() || !this.SolidOnSide(side))
			{
				continue;
			}
			AdvTile adv = WorldReading.getAdvTile(getWorldObj(), xCoord, yCoord, zCoord, side);
			if(adv != null && adv instanceof TileLamp)
			{
				TileLamp lamp = (TileLamp)adv;
				if(adv.SolidOnSide(side.getOpposite()))
				{
					lamp.changeGateState(par1, side.getOpposite());
				}
			}
		}
		this.updateBlock();
		this.updateNeighbors(true);
	}
	
	public static enum EnumLampType
	{
		FULL(new ResourceLocation(SpmodAPILib.ModID.toLowerCase() + ":textures/models/blocks/ModelFullLamp.png"), 0, "Full", false),
		RP2CAGELAMP(new ResourceLocation(SpmodAPILib.ModID.toLowerCase() + ":textures/models/blocks/ModelCageLamp.png"), 1, "RPCageLamp", true),
		Nothing();
		
		ResourceLocation texture;
		int render;
		String name;
		boolean facing;
		
		private EnumLampType()
		{
			texture = new ResourceLocation("", "");
			render = -1;
			name = "";
			facing = false;
		}
		
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

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		isAllColored = nbt.getBoolean("All");
		isInverted = nbt.getBoolean("Inverted");
		isNoneColored = nbt.getBoolean("NoneColor");
		isActive = nbt.getBoolean("Active");
		type = EnumLampType.values()[nbt.getInteger("Type")];
		meta = nbt.getInteger("Meta");
		color = new SpmodColor(nbt.getInteger("Color"));
		NBTTagList round = nbt.getTagList("Round");
		for (int i = 0; i < round.tagCount(); i++)
		{
			NBTTagInt ints = (NBTTagInt) round.tagAt(i);
			roundRoubinMode.add(EnumColor.values()[ints.data]);
		}
		NBTTagList valids = nbt.getTagList("Colors");
		for (int i = 0; i < valids.tagCount(); i++)
		{
			NBTTagInt ints = (NBTTagInt) valids.tagAt(i);
			installedColor.add(EnumColor.values()[ints.data]);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setBoolean("All", isAllColored);
		nbt.setBoolean("Inverted", isInverted);
		nbt.setBoolean("NoneColor", isNoneColored);
		nbt.setBoolean("Active", isActive);
		nbt.setInteger("Type", type.ordinal());
		nbt.setInteger("Meta", meta);
		nbt.setInteger("Color", color.getHex());
		NBTTagList round = new NBTTagList();
		for (EnumColor color : this.roundRoubinMode)
		{
			NBTTagInt ints = new NBTTagInt("Color", color.ordinal());
			round.appendTag(ints);
		}
		nbt.setTag("Round", round);
		NBTTagList valids = new NBTTagList();
		for (EnumColor color : this.installedColor)
		{
			NBTTagInt ints = new NBTTagInt("Color", color.ordinal());
			valids.appendTag(ints);
		}
		nbt.setTag("Colors", valids);
	}
	
	
	
}
