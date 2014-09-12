package speiger.src.tinymodularthings.common.items.itemblocks.storage;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.StepSound;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import speiger.src.api.items.DisplayItem;
import speiger.src.api.items.InfoStack;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;
import speiger.src.spmodapi.common.util.proxy.LangProxy;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.blocks.storage.TinyTank;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.items.core.TinyItem;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTinyTank extends TinyItem
{
	
	public ItemTinyTank(int par1)
	{
		super(par1);
		setCreativeTab(CreativeTabs.tabFood);
		setHasSubtypes(true);
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod start)
	{
		return LanguageRegister.getLanguageName(new DisplayItem(par1.getItem()), "tinyTank", start);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1, EntityPlayer par2EntityPlayer, List par3, boolean par4)
	{
		int[] tankSizes = new int[] { 1000, 2000, 4000, 8000, 12000, 16000, 24000, 32000, 64000 };
		String name = LanguageRegister.getLanguageName(new InfoStack(), "tank.size", TinyModularThings.instance);
		par3.add(name + ": " + tankSizes[par1.getItemDamage()] + "mB");
		if (par1.hasTagCompound() && par1.getTagCompound().hasKey("Fluid"))
		{
			NBTTagCompound nbt = par1.getTagCompound().getCompoundTag("Fluid");
			String stored = LangProxy.getStored(TinyModularThings.instance);
			FluidStack fluid = new FluidStack(nbt.getInteger("FluidID"), nbt.getInteger("Amount"));
			if (fluid != null)
			{
				par3.add(stored + " Fluid: " + fluid.getFluid().getLocalizedName());
				par3.add(LangProxy.getAmount(TinyModularThings.instance) + ": " + fluid.amount + "mB");
			}
		}
	}
	
	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
	{
		int i1 = par3World.getBlockId(par4, par5, par6);
		
		if (i1 == Block.snow.blockID && (par3World.getBlockMetadata(par4, par5, par6) & 7) < 1)
		{
			par7 = 1;
		}
		else if (i1 != Block.vine.blockID && i1 != Block.tallGrass.blockID && i1 != Block.deadBush.blockID && (Block.blocksList[i1] == null || !Block.blocksList[i1].isBlockReplaceable(par3World, par4, par5, par6)))
		{
			if (par7 == 0)
			{
				--par5;
			}
			
			if (par7 == 1)
			{
				++par5;
			}
			
			if (par7 == 2)
			{
				--par6;
			}
			
			if (par7 == 3)
			{
				++par6;
			}
			
			if (par7 == 4)
			{
				--par4;
			}
			
			if (par7 == 5)
			{
				++par4;
			}
		}
		
		if (par1ItemStack.stackSize == 0)
		{
			return false;
		}
		else if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack))
		{
			return false;
		}
		else if (par5 == 255)
		{
			return false;
		}
		else if (!par3World.canPlaceEntityOnSide(TinyBlocks.storageBlock.blockID, par4, par5, par6, false, par7, par2EntityPlayer, par1ItemStack))
		{
			return false;
		}
		else
		{
			if (par3World.setBlock(par4, par5, par6, TinyBlocks.storageBlock.blockID, 1, 3))
			{
				TileEntity tile = par3World.getBlockTileEntity(par4, par5, par6);
				if (tile != null && tile instanceof TinyTank)
				{
					((TinyTank) tile).setTankMode(par1ItemStack.getItemDamage());
					Block.blocksList[par3World.getBlockId(par4, par5, par6)].onBlockPlacedBy(par3World, par4, par5, par6, par2EntityPlayer, par1ItemStack);
					if (par1ItemStack.hasTagCompound() && par1ItemStack.getTagCompound().hasKey("Fluid"))
					{
						NBTTagCompound nbt = par1ItemStack.getTagCompound().getCompoundTag("Fluid");
						FluidStack fluid = new FluidStack(nbt.getInteger("FluidID"), nbt.getInteger("Amount"));
						if (nbt.hasKey("Data"))
						{
							fluid.tag = nbt.getCompoundTag("Data");
						}
						((TinyTank) tile).tank.setFluid(fluid);
					}
					StepSound sound = Block.blocksList[par3World.getBlockId(par4, par5, par6)].stepSound;
					par3World.playSoundEffect(par4, par5, par6, sound.getPlaceSound(), sound.stepSoundVolume, sound.stepSoundPitch);
					par1ItemStack.stackSize--;
					return true;
				}
				
			}
			
			return false;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int i = 0; i < 9; i++)
		{
			par3List.add(new ItemStack(par1, 1, i));
			
		}
	}
	
	@Override
	public void registerItems(int id, SpmodMod par0)
	{
		if (!par0.getName().equals(TinyModularThingsLib.Name))
		{
			return;
		}
		LanguageRegister.getLanguageName(new DisplayItem(id), "tinyTank", par0);
		LanguageRegister.getLanguageName(new InfoStack(), "tank.size", TinyModularThings.instance);
	}
	
}
