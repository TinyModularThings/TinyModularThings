package speiger.src.tinymodularthings.common.items.itemblocks.transport;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import speiger.src.api.blocks.BlockStack;
import speiger.src.api.inventory.IAcceptor;
import speiger.src.api.items.InfoStack;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.proxy.LangProxy;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.items.core.TinyItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemInterfaceBlock extends TinyItem
{
	
	public ItemInterfaceBlock(int par1)
	{
		super(par1);
		setHasSubtypes(true);
		setCreativeTab(CreativeTabs.tabFood);
	}
	
	@Override
	public void registerItems(int id, SpmodMod par0)
	{
		if (!SpmodModRegistry.areModsEqual(par0, TinyModularThings.instance))
		{
			return;
		}
		LanguageRegister.getLanguageName(new ItemStack(id, 1, 0), "interface.item", par0);
		LanguageRegister.getLanguageName(new ItemStack(id, 1, 1), "interface.fluid", par0);
		LanguageRegister.getLanguageName(new ItemStack(id, 1, 2), "interface.energy", par0);
		LanguageRegister.getLanguageName(new InfoStack(), "internal.block", par0);
		
	}
	
	
	public static ItemStack getBlockFromInterface(ItemStack par1)
	{
		NBTTagCompound nbt = par1.getTagCompound().getCompoundTag("Interface");
		return new BlockStack(nbt.getInteger("ID"), nbt.getInteger("Meta")).asItemStack();
	}
	
	public static boolean isValidBlock(ItemStack par1)
	{
		NBTTagCompound nbt = par1.getTagCompound().getCompoundTag("Interface");
		if (Block.blocksList[nbt.getInteger("ID")] != null)
		{
			return true;
		}
		return false;
	}
	
	public static ItemStack addBlockToInterface(ItemStack par1, BlockStack stack)
	{
		if (!par1.hasTagCompound())
		{
			par1.setTagInfo("Interface", new NBTTagCompound());
		}
		NBTTagCompound nbt = par1.getTagCompound().getCompoundTag("Interface");
		nbt.setInteger("ID", stack.getBlockID());
		nbt.setInteger("Meta", stack.getMeta());
		return par1;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3)
	{
		for (int i = 0; i < 3; i++)
		{
			par3.add(createInterface(par1, i));
		}
	}
	
	public static ItemStack createInterface(int id, int meta)
	{
		ItemStack stack = new ItemStack(id, 1, meta);
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("ID", 0);
		nbt.setInteger("Meta", 0);
		NBTTagCompound end = new NBTTagCompound();
		end.setCompoundTag("Interface", nbt);
		stack.setTagCompound(end);
		
		return stack;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int par1)
	{
		return TextureEngine.getTextures().getTexture(TinyBlocks.transportBlock, par1);
	}
	
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getSpriteNumber()
	{
		return 0;
	}

	@Override
	public String getDisplayName(ItemStack par1, SpmodMod par0)
	{
		String core = "";
		switch (par1.getItemDamage())
		{
			case 0:
				core = LanguageRegister.getLanguageName(par1, "interface.item", par0);
				break;
			case 1:
				core = LanguageRegister.getLanguageName(par1, "interface.fluid", par0);
				break;
			case 2:
				core = LanguageRegister.getLanguageName(par1, "interface.energy", par0);
				break;
		}
		
		return core;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1, EntityPlayer par2, List par3, boolean par4)
	{
		String text = LanguageRegister.getLanguageName(new InfoStack(), "internal.block", TinyModularThings.instance);
		
		String nothing = LangProxy.getStoredNothing(getMod());
		
		if (par1.hasTagCompound() && par1.getTagCompound().getCompoundTag("Interface") != null)
		{
			NBTTagCompound nbt = par1.getTagCompound().getCompoundTag("Interface");
			int id = nbt.getInteger("ID");
			int meta = nbt.getInteger("Meta");
			
			if (id != 0)
			{
				ItemStack stack = new ItemStack(id, 1, meta);
				String end = stack.getItem().getItemDisplayName(stack);
				if (end != null)
				{
					par3.add(text + ": " + end);
				}
			}
			else
			{
				par3.add(text + ": " + nothing);
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
		else
		{
			if (!par3World.isRemote)
			{
				BlockStack stack = null;
				
				if (par1ItemStack.hasTagCompound() && par1ItemStack.getTagCompound().getCompoundTag("Interface") != null)
				{
					NBTTagCompound nbt = par1ItemStack.getTagCompound().getCompoundTag("Interface");
					if (nbt.getInteger("ID") == 0)
					{
						par2EntityPlayer.sendChatToPlayer(LanguageRegister.createChatMessage(LanguageRegister.getLanguageName(new InfoStack(), "need.internal.block", getMod())));
						return false;
					}
					stack = new BlockStack(nbt.getInteger("ID"), nbt.getInteger("Meta"));
				}
				
				if (stack == null)
				{
					return false;
				}
				
				if (par3World.setBlock(par4, par5, par6, TinyBlocks.transportBlock.blockID, getMetaFromDamage(par1ItemStack), 3))
				{
					TileEntity tile = par3World.getBlockTileEntity(par4, par5, par6);
					if (tile != null && tile instanceof IAcceptor)
					{
						IAcceptor hidden = (IAcceptor) tile;
						hidden.setBlock(stack);
						par1ItemStack.stackSize--;
						return true;
					}
					
				}
			}
			
			return false;
		}
	}
	
	public int getMetaFromDamage(ItemStack par1)
	{
		int meta = par1.getItemDamage();
		return meta + 1;
	}
	
}
