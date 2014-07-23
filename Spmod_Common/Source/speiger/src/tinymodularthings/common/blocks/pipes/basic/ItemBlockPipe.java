package speiger.src.tinymodularthings.common.blocks.pipes.basic;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.blocks.BlockHelper;
import speiger.src.api.blocks.BlockStack;
import speiger.src.api.items.InfoStack;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.nbt.NBTHelper;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import speiger.src.api.util.WorldReading;
import speiger.src.spmodapi.common.util.proxy.LangProxy;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.interfaces.IBasicPipe;
import speiger.src.tinymodularthings.common.items.core.ItemBlockTinyChest;
import speiger.src.tinymodularthings.common.utils.PipeInformation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockPipe extends ItemBlockTinyChest
{
	
	public ItemBlockPipe(int par1)
	{
		super(par1);
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod start)
	{
		BlockStack stack = new BlockStack(par1);
		String name = "nameless.Pipe";
		if (stack.getBlock() instanceof IBasicPipe)
		{
			IBasicPipe pipe = (IBasicPipe) stack.getBlock();
			PipeInformation info = pipe.getItemInformation(par1);
			if (info != null)
			{
				name = info.getItemName();
			}
		}
		
		return LanguageRegister.getLanguageName(stack.getBlock(), name, start);
	}
	
	@Override
	public void registerItems(int id, SpmodMod par0)
	{
		if (!SpmodModRegistry.areModsEqual(par0, TinyModularThings.instance))
		{
			return;
		}
		BlockStack stack = new BlockStack(id);
		String name = "nameless.Pipe";
		if (stack.getBlock() instanceof IBasicPipe)
		{
			IBasicPipe pipe = (IBasicPipe) stack.getBlock();
			PipeInformation info = pipe.getItemInformation(stack.asItemStack());
			if (info != null)
			{
				name = info.getItemName();
			}
		}
		LanguageRegister.getLanguageName(stack.getBlock(), name, par0);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1, EntityPlayer par2, List par3, boolean par4)
	{
		BlockStack stack = new BlockStack(par1);
		if (stack.getBlock() instanceof IBasicPipe)
		{
			IBasicPipe pipe = (IBasicPipe) stack.getBlock();
			if (pipe != null && pipe.getItemInformation(par1) != null)
			{
				pipe.getItemInformation(par1).addInformation(par3);
			}
		}
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack par1, World par2, EntityPlayer par3)
	{
		NBTTagCompound nbt = NBTHelper.getTinyChestTagCompound(par3.getEntityData());
		if (nbt.hasKey(NBTHelper.getPlayerNBTStringFromMode(0)))
		{
			int mode = nbt.getInteger(NBTHelper.getPlayerNBTStringFromMode(0));
			par3.sendChatToPlayer(LanguageRegister.createChatMessage(LangProxy.getModeString(TinyModularThings.instance) + ": " + LanguageRegister.getLanguageName(new InfoStack(), BlockHelper.getPlacingMode(mode), TinyModularThings.instance)));
			return par1;
		}
		par3.sendChatToPlayer(LanguageRegister.createChatMessage(LangProxy.getModeString(TinyModularThings.instance) + ": " + LanguageRegister.getLanguageName(new InfoStack(), "error.hasNot.Mode", TinyModularThings.instance)));
		return par1;
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
		else if (par5 == 255 && Block.blocksList[getBlockID()].blockMaterial.isSolid())
		{
			return false;
		}
		else
		{
			Block block = Block.blocksList[getBlockID()];
			
			ForgeDirection direction = ForgeDirection.UNKNOWN;
			
			int mode = NBTHelper.getTinyChestTagCompound(par2EntityPlayer.getEntityData()).getInteger(NBTHelper.getPlayerNBTStringFromMode(0));
			
			if (!NBTHelper.getTinyChestTagCompound(par2EntityPlayer.getEntityData()).hasKey(NBTHelper.getPlayerNBTStringFromMode(0)))
			{
				mode = -1;
			}
			
			switch (mode)
			{
				case 0:
					direction = ForgeDirection.getOrientation(WorldReading.getLookingDirectionFromEnitty(par2EntityPlayer));
					break;
				case 1:
					direction = ForgeDirection.getOrientation(WorldReading.getLookingDirectionFromEnitty(par2EntityPlayer)).getOpposite();
					break;
				case 2:
					direction = ForgeDirection.getOrientation(par7);
					break;
				case 3:
					direction = ForgeDirection.getOrientation(par7).getOpposite();
					break;
				default:
					direction = ForgeDirection.getOrientation(WorldReading.getLookingDirectionFromEnitty(par2EntityPlayer));
			}
			
			if (par2EntityPlayer.isSneaking())
			{
				direction = direction.getOpposite();
			}
			
			if (par3World.setBlock(par4, par5, par6, getBlockID(), direction.ordinal(), 3))
			{
				par3World.playSoundEffect(par4 + 0.5F, par5 + 0.5F, par6 + 0.5F, block.stepSound.getPlaceSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
				--par1ItemStack.stackSize;
			}
			return true;
		}
	}
}
