package speiger.src.tinymodularthings.common.items.tools;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import speiger.src.api.common.world.items.IDetectorModulItem;
import speiger.src.api.common.world.tiles.machine.IDetectorModul;
import speiger.src.tinymodularthings.common.blocks.redstone.detector.*;
import speiger.src.tinymodularthings.common.items.core.TinyItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class DetectorModules extends TinyItem implements IDetectorModulItem
{
	
	public DetectorModules(int par1)
	{
		super(par1);
		this.setHasSubtypes(true);
		this.setCreativeTab(CreativeTabs.tabFood);
	}

	@Override
	public String getName(ItemStack par1)
	{
		return "Detector Modul";
	}

	@Override
	public IDetectorModul createDetectorModul(ItemStack par1, TileEntity detector)
	{
		switch(par1.getItemDamage())
		{
			case 0: return new DetectorBase();
			case 1: return new DetectorBaseCrop();
			case 2: return new DetectorIC2Crops();
			case 3: return new DetectorGostBlocks();
		
			case 50: return new DetectorInventory(0);
			case 51: return new DetectorInventory(1);
			case 52: return new DetectorInventory(2);
			case 53: return new DetectorInventory(3);
			
			case 100: return new DetectorEntities(0);
			case 101: return new DetectorEntities(1);
			case 102: return new DetectorEntities(2);
			case 103: return new DetectorEntities(3);
			case 104: return new DetectorEntities(4);
			case 105: return new DetectorEntities(5);
			case 106: return new DetectorEntities(6);
			case 107: return new DetectorEntities(7);
		}
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for(int i = 0;i<2000;i++)
		{
			ItemStack stack = new ItemStack(par1, 1, i);
			if(this.createDetectorModul(stack, null) != null)
			{
				par3List.add(stack);
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
		if(par1ItemStack != null)
		{
			IDetectorModul modul = this.createDetectorModul(par1ItemStack, null);
			if(modul != null)
			{
				modul.addItemInformation(par3List);
			}
		}
	}
	
	
	
}
