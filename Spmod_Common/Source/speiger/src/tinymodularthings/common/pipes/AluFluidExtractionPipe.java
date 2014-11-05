package speiger.src.tinymodularthings.common.pipes;

import java.io.DataInput;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import speiger.src.api.common.data.packets.IPacketReciver;
import speiger.src.api.common.data.packets.SpmodPacketHelper;
import speiger.src.api.common.utils.RedstoneUtils;
import speiger.src.api.common.utils.WorldReading;
import speiger.src.spmodapi.common.util.proxy.LangProxy;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.client.gui.pipes.GuiAluPipe;
import speiger.src.tinymodularthings.common.enums.EnumIDs;
import speiger.src.tinymodularthings.common.handler.PipeIconHandler;
import speiger.src.tinymodularthings.common.interfaces.IPipeGuiProvider;
import buildcraft.api.core.IIconProvider;
import buildcraft.api.transport.IPipeTile;
import buildcraft.api.transport.PipeManager;
import buildcraft.core.network.TileNetworkData;
import buildcraft.transport.Pipe;
import buildcraft.transport.PipeConnectionBans;
import buildcraft.transport.PipeTransportFluids;
import buildcraft.transport.TileGenericPipe;
import buildcraft.transport.pipes.PipeFluidsEmerald;
import buildcraft.transport.pipes.PipeFluidsWood;
import buildcraft.transport.pipes.PipeLogicWood;
import buildcraft.transport.pipes.PipePowerWood;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AluFluidExtractionPipe extends Pipe<PipeTransportFluids> implements IPacketReciver, IPipeGuiProvider
{
	@TileNetworkData
	public boolean continueless = false;
	@TileNetworkData
	public int todos = 0;
	@TileNetworkData
	public int extractionTodo = 0;
	@TileNetworkData
	public int FluidSetup = 1;
	@TileNetworkData
	public int delay = 0;
	@TileNetworkData
	public boolean powered = false;
	

	
	public AluFluidExtractionPipe(int itemID)
	{
		super(new PipeTransportFluids(), itemID);
		this.transport.flowRate = 20;
		this.transport.travelDelay = 6;
	}
	
	
	
	private PipeLogicWood logic = new PipeLogicWood(this)
	{
		@Override
		protected boolean isValidConnectingTile(TileEntity tile)
		{
			if(tile instanceof IPipeTile)
				return false;
			if(!(tile instanceof IFluidHandler))
				return false;
			if(!PipeManager.canExtractFluids(pipe, tile.worldObj, tile.xCoord, tile.yCoord, tile.zCoord))
				return false;
			return true;
		}
	};
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIconProvider getIconProvider()
	{
		return PipeIconHandler.getIcons();
	}
	
	@Override
	public void onNeighborBlockChange(int blockId)
	{
		logic.onNeighborBlockChange(blockId);
		super.onNeighborBlockChange(blockId);
	}
	
	@Override
	public void initialize()
	{
		logic.initialize();
		super.initialize();
	}
	
	public static void init(int id)
	{
		SpmodPacketHelper.getHelper().registerPacketReciver(new AluFluidExtractionPipe(id));
		PipeConnectionBans.banConnection(PipeEmeraldExtractionPower.class);
		PipeConnectionBans.banConnection(PipeEmeraldExtractionPower.class, PipePowerWood.class);
		PipeConnectionBans.banConnection(AluFluidExtractionPipe.class);
		PipeConnectionBans.banConnection(AluFluidExtractionPipe.class, PipeFluidsEmerald.class, PipeFluidsWood.class);
		
	}
	
	@Override
	public int getIconIndex(ForgeDirection direction) {
		if (direction == ForgeDirection.UNKNOWN)
			return 6;
		else {
			int metadata = container.getBlockMetadata();

			if (metadata == direction.ordinal())
				return 7;
			else
				return 6;
		}
	}
	@Override
	public void recivePacket(DataInput par1)
	{
		try
		{
			World world = DimensionManager.getWorld(par1.readInt());
			int x = par1.readInt();
			int y = par1.readInt();
			int z = par1.readInt();
			
			boolean cont = par1.readBoolean();
			int amount = par1.readInt();
			
			if(world != null && !world.isRemote)
			{
				TileEntity tile = world.getBlockTileEntity(x, y, z);
				if(tile != null && tile instanceof TileGenericPipe)
				{
					TileGenericPipe pipeProvider = (TileGenericPipe)tile;
					if(pipeProvider.pipe != null && pipeProvider.pipe instanceof AluFluidExtractionPipe)
					{
						AluFluidExtractionPipe bronze = (AluFluidExtractionPipe)pipeProvider.pipe;
						bronze.continueless = cont;
						bronze.FluidSetup = amount;
						bronze.todos = 0;
						if(cont)
						{
							bronze.delay = 20;
						}
						bronze.updateNeighbors(true);
					}
				}
			}
			
		}
		catch(Exception e)
		{
			
		}
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if(!getWorld().isRemote)
		{
			if(this.continueless)
			{
				if(todos <= 0 && extractionTodo <= 0 && RedstoneUtils.isBlockGettingPowered(getContainer()) )
				{
					if(delay <= 0)
					{
						todos++;
						delay = 20;
					}
					else
					{
						delay--;
					}
				}
			}
			else
			{
				if(powered && !RedstoneUtils.isBlockGettingPowered(getContainer()))
				{
					powered = false;
					if(todos < 100)
					{
						todos++;
					}
				}
				else if(!powered && RedstoneUtils.isBlockGettingPowered(getContainer()))
				{
					powered = true;
				}
			}
			
			if(this.extractionTodo <= 0 && todos > 0)
			{
				todos--;
				extractionTodo = FluidSetup;
			}
			
			int meta = this.getContainer().getBlockMetadata();
			if(extractionTodo > 0 && meta < 6)
			{
				if(WorldReading.hasTank(getContainer(), meta))
				{
					IFluidHandler tank = WorldReading.getFluidTank(getContainer(), meta);
					int flowRate = Math.min(this.extractionTodo, this.transport.flowRate);
					ForgeDirection dir = ForgeDirection.getOrientation(meta).getOpposite();
					FluidStack extracted = tank.drain(dir, flowRate, false);
					
					int inserted = 0;
					if(extracted != null)
					{
						inserted = this.transport.fill(dir.getOpposite(), extracted, true);
						
						tank.drain(dir, inserted, true);
					}
					extractionTodo -= inserted;
				}
			}
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound data)
	{
		super.writeToNBT(data);
		data.setBoolean("Loop", continueless);
		data.setBoolean("Powered", powered);
		data.setInteger("Left", extractionTodo);
		data.setInteger("Extract", FluidSetup);
		data.setInteger("Todos", todos);
		data.setInteger("Delay", delay);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound data)
	{
		super.readFromNBT(data);
		this.continueless = data.getBoolean("Loop");
		this.powered = data.getBoolean("Powered");
		this.extractionTodo = data.getInteger("Left");
		this.FluidSetup = data.getInteger("Extract");
		this.todos = data.getInteger("Todos");
		this.delay = data.getInteger("Delay");
	}
	
	@Override
	public boolean blockActivated(EntityPlayer entityplayer)
	{
		if(!this.container.worldObj.isRemote)
		{
			if(!logic.blockActivated(entityplayer))
			{
				if(entityplayer.isSneaking())
				{
					entityplayer.sendChatToPlayer(LangProxy.getText("Current Setup: " + FluidSetup + "mB " + (continueless ? "Loops Automaticly with Redstone Signal" : "Require Redstone Clock")));
				}
				else
				{
					entityplayer.openGui(TinyModularThings.instance, EnumIDs.BCPipes.getId(), getWorld(), container.xCoord, container.yCoord, container.zCoord);
				}
			}
		}
		
		return true;
	}
	
	@Override
	public String identifier()
	{
		return "TMTAluExtractionPipe";
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer getGui(InventoryPlayer par1)
	{
		return new GuiAluPipe(this);
	}
	
	@Override
	public Container getInventory(InventoryPlayer par1)
	{
		return new Container()
		{
			
			@Override
			public boolean canInteractWith(EntityPlayer entityplayer)
			{
				return true;
			}
			
		};
		
	}
	
	@Override
	public boolean outputOpen(ForgeDirection to)
	{
		int meta = container.getBlockMetadata();
		return super.outputOpen(to) && meta != to.ordinal();
	}
	
}
