package speiger.src.tinymodularthings.common.blocks.machine;

import ic2.api.crops.CropCard;
import ic2.api.crops.Crops;
import ic2.api.crops.ICropTile;

import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraftforge.common.FakePlayerFactory;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class IC2CropFarm extends AdvTile
{
	public int fails = 0;
	
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		if(side < 2)
		{
			return getEngine().getTexture(TinyBlocks.machine, 6, 0);
		}
		return getEngine().getTexture(TinyBlocks.machine, 6, 1);
	}

	@Override
	public boolean canUpdate()
	{
		return false;
	}
	
	

	@Override
	@SideOnly(Side.CLIENT)
	public void onItemInformation(EntityPlayer par1, List par2, ItemStack par3)
	{
		par2.add("Crop Farm For IC2 Crops");
		if(GuiScreen.isCtrlKeyDown())
		{
			par2.add("Check a 3x3 1 block Below him");
			par2.add("Check a 3x3 2 blocks above him");
			par2.add("Redstone Pulse (At the turning of part) it checks the area");
			par2.add("If it fails 5 Times to harvest Crops it distroy them! (No Drops)");
			par2.add("Harvesting 1 Crop means Success and Resets the Fails to 0");
		}
		else
		{
			par2.add("Press Ctrl to get Extra Infos");
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound par1)
	{
		super.readFromNBT(par1);
		fails = par1.getInteger("Fails");
	}

	@Override
	public void writeToNBT(NBTTagCompound par1)
	{
		super.writeToNBT(par1);
		par1.setInteger("Fails", fails);
	}

	@Override
	public void onRedstonePulseApplied()
	{
		int y = - 1;
		boolean didWork = false;
		for(int x = -1;x<2;x++)
		{
			for(int z = -1;z<2;z++)
			{
				TileEntity tile = worldObj.getBlockTileEntity(xCoord + x, yCoord + y, zCoord + z);
				if(tile != null && tile instanceof ICropTile)
				{
					didWork = check((ICropTile)tile) || didWork;
				}
			}
		}
		y = 2;
		for(int x = -1;x<2;x++)
		{
			for(int z = -1;z<2;z++)
			{
				TileEntity tile = worldObj.getBlockTileEntity(xCoord + x, yCoord + y, zCoord + z);
				if(tile != null && tile instanceof ICropTile)
				{
					didWork = check((ICropTile)tile) || didWork;
				}
			}
		}
		if(didWork)
		{
			fails = 0;
		}
		else
		{
			fails++;
			if(fails >= 5)
			{
				distroy();
			}
		}
	}
	
	private void distroy()
	{
		int y = - 1;
		for(int x = -1;x<2;x++)
		{
			for(int z = -1;z<2;z++)
			{
				TileEntity tile = worldObj.getBlockTileEntity(xCoord + x, yCoord + y, zCoord + z);
				if(tile != null && tile instanceof ICropTile)
				{
					distroy((ICropTile)tile);
				}
			}
		}
		y = 2;
		for(int x = -1;x<2;x++)
		{
			for(int z = -1;z<2;z++)
			{
				TileEntity tile = worldObj.getBlockTileEntity(xCoord + x, yCoord + y, zCoord + z);
				if(tile != null && tile instanceof ICropTile)
				{
					distroy((ICropTile)tile);
				}
			}
		}
	}
	
	public void distroy(ICropTile par1)
	{
		par1.reset();
	}
	
	private boolean check(ICropTile par1)
	{
		if(par1.getID() <= 0)
		{
			return false;
		}
		CropCard card = Crops.instance.getCropList()[par1.getID()];
		if(card == null)
		{
			return false;
		}
		if(card.canBeHarvested(par1))
		{
			card.rightclick(par1, FakePlayerFactory.getMinecraft(worldObj));
			return true;
		}
		return false;
	}
	
}
