package speiger.src.compactWindmills.common.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import speiger.src.api.items.DisplayItem;
import speiger.src.api.items.IRotorItem;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.nbt.NBTHelper;
import speiger.src.api.tiles.IWindmill;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import speiger.src.compactWindmills.CompactWindmills;
import speiger.src.compactWindmills.common.core.CWPreference;
import speiger.src.compactWindmills.common.items.ItemRotor.BasicRotorType;
import speiger.src.spmodapi.common.enums.EnumColor;
import speiger.src.spmodapi.common.enums.EnumColor.SpmodColor;
import speiger.src.spmodapi.common.items.SpmodItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class IceRotor extends SpmodItem implements IRotorItem
{
	public static ResourceLocation texture = new ResourceLocation(CWPreference.ModID.toLowerCase()+":textures/renders/rotor.base.ice.png");
	public IceRotor(int par1)
	{
		super(par1);
		this.setMaxDamage(Short.MAX_VALUE);
		this.setMaxStackSize(1);
	}

	@Override
	public void registerItems(int par1, SpmodMod par0)
	{
		if(!SpmodModRegistry.areModsEqual(par0, CompactWindmills.instance))
		{
			return;
		}
		LanguageRegister.getLanguageName(new DisplayItem(par1), "rotor.ice", par0);
	}
	
	@Override
	public boolean ignoreTier(ItemStack par1)
	{
		return true;
	}
	
	@Override
	public boolean canWorkWithWindmillTier(ItemStack par1, int tier)
	{
		return true;
	}
	
	@Override
	public int getTier(ItemStack par1)
	{
		return 0;
	}
	
	@Override
	public void damageRotor(ItemStack par1, int damage, IWindmill windmill)
	{
		par1.setItemDamage(par1.getItemDamage()+damage);
		if(par1.getItemDamage() > par1.getMaxDamage())
		{
			windmill.distroyRotor();
		}
	}
	
	
	
	public static ItemStack getRotor(int id)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("Damage", 0);
		nbt.setFloat("Eff", 0.2F);
		ItemStack item = new ItemStack(id, 1, 0);
		return item;
	}
	
	

	@Override
	public void onUpdate(ItemStack par1, World par2, Entity par3, int par4, boolean par5)
	{
		if(!par2.isRemote)
		{
			if(!NBTHelper.nbtCheck(par1, "Rotor"))
			{
				par1.setTagInfo("Rotor", new NBTTagCompound());
			}
			
			NBTTagCompound data = NBTHelper.getTag(par1, "Rotor");
			float eff = data.getFloat("Eff");
			if(eff > 0.2F)
			{
				data.setFloat("Eff", 0.2F);
			}
		}
	}

	@Override
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
	{
		if(par2ItemStack != null && par2ItemStack.itemID == Block.ice.blockID)
		{
			return true;
		}
		return super.getIsRepairable(par1ItemStack, par2ItemStack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ResourceLocation getRenderTexture(ItemStack par1)
	{
		return texture;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int par1)
	{
		return CompactWindmills.rotor.getIconFromDamage(BasicRotorType.IridiumRotor.ordinal());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
	{
		return SpmodColor.fromHex(EnumColor.LIGHTBLUE.getAsHex()).mixWith(SpmodColor.fromHex(EnumColor.WHITE.getAsHex())).getHex();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		par3List.add(getRotor(par1));
	}

	@Override
	public void onRotorTick(IWindmill windMill, World world, ItemStack rotor)
	{
		if(!world.isRemote)
		{
			if(!NBTHelper.nbtCheck(rotor, "Rotor"))
			{
				rotor.setTagInfo("Rotor", new NBTTagCompound());
			}
			if(windMill.getFacing() != 0 && windMill.getFacing() != 1)
			{
				return;
			}
			
			float eff = NBTHelper.getTag(rotor, "Rotor").getFloat("Eff");
			if(eff < 15F && world.rand.nextFloat() > 0.9F)
			{
				eff+=(world.rand.nextFloat() / 10);
				rotor.setItemDamage(rotor.getItemDamage()+world.rand.nextInt(4));
				ChunkCoordinates coord = windMill.getChunkCoordinates();
				world.playSoundEffect((double)((float)coord.posX + 0.5F), (double)((float)coord.posY + 0.5F), (double)((float)coord.posZ + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
			}
			if(eff > 0.000F)
			{
				eff-=0.001F;
			}
			NBTHelper.getTag(rotor, "Rotor").setFloat("Eff", eff);
		}
	}
	
	@Override
	public float getRotorEfficeny(ItemStack par1, IWindmill par2)
	{
		if(par2.getFacing() != 0 && par2.getFacing() != 1)
		{
			return 0F;
		}
		if(!par2.isFake() && !par2.getWindmill().getWorldObj().provider.isHellWorld)
		{
			return 0.01F;
		}
		
		if(!NBTHelper.nbtCheck(par1, "Rotor"))
		{
			par1.setTagInfo("Rotor", new NBTTagCompound());
		}
		
		return NBTHelper.getTag(par1, "Rotor").getFloat("Eff");
	}
	
	@Override
	public boolean isAdvancedRotor(ItemStack par1)
	{
		return false;
	}
	
	@Override
	public boolean isInfinite(ItemStack par1)
	{
		return false;
	}
	
	@Override
	public IRotorModel getCustomModel(ItemStack par1, int size)
	{
		return null;
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod par0)
	{
		return LanguageRegister.getLanguageName(new DisplayItem(par1.itemID), "rotor.ice", CompactWindmills.instance);
	}
	
}
