package speiger.src.spmodapi.common.blocks.utils;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.*;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.api.common.world.tiles.energy.EnergyProvider;
import speiger.src.api.common.world.tiles.energy.IEnergyProvider;
import speiger.src.api.common.world.tiles.energy.IEnergySubject;
import speiger.src.api.common.world.tiles.interfaces.IAcceptor;
import speiger.src.api.common.world.tiles.interfaces.IAcceptor.AcceptorType;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.templates.core.ITemplate;
import speiger.src.spmodapi.common.templates.core.ITemplateProvider;
import speiger.src.spmodapi.common.templates.types.CageTemplate;
import speiger.src.spmodapi.common.tile.AdvInventory;
import speiger.src.spmodapi.common.tile.AdvancedFluidTank;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityCage extends AdvInventory implements ITemplateProvider, IFluidHandler, IEnergyProvider
{
	EnergyProvider provider = new EnergyProvider(this, 2500);
	FluidTank tank = new AdvancedFluidTank(this, "cageTank", 16000);
	ITemplate template = null;
	int size;
	boolean valid;
	IAcceptor[] interfaces = new IAcceptor[3];
	
	public EntityCage()
	{
		super(1);
	}

	@Override
	public ItemStack getItemDrop()
	{
		return new ItemStack(APIBlocks.blockUtils, 1, 7);
	}
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		if(renderPass == 0)
		{
			return Block.blockIron.getBlockTextureFromSide(side);
		}
		return Block.mobSpawner.getBlockTextureFromSide(side);
	}

	@Override
	public ITemplate getTemplate()
	{
		if(template == null)
		{
			initTemplate();
		}
		return template;
	}

	@Override
	public void initTemplate()
	{
		if(template == null)
		{
			template = new CageTemplate(this);
		}
		template.setup(worldObj, xCoord, yCoord, zCoord, size);
		template.setMaxInterfaces(1);
	}

	@Override
	public void onStructureChange()
	{
		removeInterfaces();
		boolean match = template.match();
		if(match != valid)
		{
			valid = match;
			if(valid)
			{
				loadInterfaces();
			}
		}
	}
	
	public void removeInterfaces()
	{
		for(AcceptorType type : AcceptorType.values())
		{
			interfaces[type.ordinal()].removeMaster();
		}
		interfaces = new IAcceptor[3];
	}
	
	public void loadInterfaces()
	{
		for(AcceptorType type : AcceptorType.values())
		{
			interfaces[type.ordinal()] = template.getInterfaces(type, 1)[0];
		}
	}

	@Override
	public void onInteraction(EntityPlayer par1)
	{
		onActivated(par1);
	}

	@Override
	public boolean hasValidTemplate()
	{
		return valid;
	}

	@Override
	public IEnergySubject getEnergyProvider(ForgeDirection side)
	{
		return provider;
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		return this.drain(from, resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		return tank.drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return true;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		return new FluidTankInfo[]{tank.getInfo()};
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onRenderInv(BlockStack stack, RenderBlocks render)
	{
		stack.getBlock().setBlockBounds(0.05F, 0.05F, 0.05F, 0.95F, 0.95F, 0.95F);
		this.setRenderPass(0);
		super.onRenderInv(stack, render);
		stack.getBlock().setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
		this.setRenderPass(1);
		super.onRenderInv(stack, render);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onRenderWorld(Block block, RenderBlocks renderer)
	{
		block.setBlockBounds(0.05F, 0.05F, 0.05F, 0.95F, 0.95F, 0.95F);
		this.setRenderPass(0);
		renderer.renderStandardBlock(block, xCoord, yCoord, zCoord);
		block.setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
		this.setRenderPass(1);
		renderer.renderStandardBlock(block, xCoord, yCoord, zCoord);
	}
	
	
	
}
