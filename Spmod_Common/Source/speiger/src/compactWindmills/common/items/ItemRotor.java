package speiger.src.compactWindmills.common.items;

import java.util.HashMap;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumHelper;

import org.lwjgl.input.Keyboard;

import speiger.src.api.common.data.nbt.NBTHelper;
import speiger.src.api.common.utils.MathUtils;
import speiger.src.api.common.world.items.IRotorItem;
import speiger.src.api.common.world.tiles.interfaces.IWindmill;
import speiger.src.compactWindmills.CompactWindmills;
import speiger.src.compactWindmills.common.core.CWPreference;
import speiger.src.spmodapi.common.items.core.SpmodItem;
import speiger.src.spmodapi.common.util.TextureEngine;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemRotor extends SpmodItem implements IRotorItem
{
	public static HashMap<BasicRotorType, Icon> textures = new HashMap<BasicRotorType, Icon>();
	
	public ItemRotor(int par1)
	{
		super(par1);
		this.setHasSubtypes(true);
		this.setMaxDamage(100);
		this.setMaxStackSize(1);
		this.setNoRepair();
	}
	
	public static ItemStack createRotor(BasicRotorType par1)
	{
		ItemStack stack = new ItemStack(CompactWindmills.rotor, 1, par1.ordinal());
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("Damage", 0);
		stack.setTagInfo("Rotor", nbt);
		return stack;
	}
	
	@Override
	public boolean isDamaged(ItemStack stack)
	{
		if(NBTHelper.nbtCheck(stack, "Rotor"))
		{
			int damage = stack.getTagCompound().getCompoundTag("Rotor").getInteger("Damage");
			return damage > 0;
		}
		return false;
	}
	
	@Override
	public int getDisplayDamage(ItemStack stack)
	{
		if(NBTHelper.nbtCheck(stack, "Rotor"))
		{
			int damage = stack.getTagCompound().getCompoundTag("Rotor").getInteger("Damage");
			BasicRotorType type = BasicRotorType.values()[stack.getItemDamage()];
			double per = ((double)damage / (double)type.getMaxDamage()) * 100;
			return (int)per;
		}
		return 0;
	}
	
	@Override
	public boolean hasCustomSpeedMath(IWindmill par1, ItemStack rotor)
	{
		return false;
	}

	@Override
	public RotorWeight getRotorWeight(IWindmill par1, ItemStack par2)
	{
		return BasicRotorType.values()[par2.getItemDamage()].getWeight();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int par1)
	{
		return TextureEngine.getTextures().getTexture(this, par1);
	}
	
	@Override
	public void damageRotor(ItemStack par1, int damage, IWindmill windmill)
	{
		BasicRotorType type = BasicRotorType.values()[par1.getItemDamage()];
		if(!NBTHelper.nbtCheck(par1, "Rotor"))
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setInteger("Damage", 0);
			par1.setTagInfo("Rotor", nbt);
		}
		int totalDamage = par1.getTagCompound().getCompoundTag("Rotor").getInteger("Damage");
		if(totalDamage + damage > type.getMaxDamage())
		{
			windmill.distroyRotor();
			return;
		}
		totalDamage += damage;
		par1.getTagCompound().getCompoundTag("Rotor").setInteger("Damage", totalDamage);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ResourceLocation getRenderTexture(ItemStack par1)
	{
		return BasicRotorType.values()[par1.getItemDamage()].getRenderTexture();
	}
	
	@Override
	public void onRotorTick(IWindmill windMill, World world, ItemStack rotor)
	{
		
	}
	
	@Override
	public boolean isInfinite(ItemStack par1)
	{
		return BasicRotorType.values()[par1.getItemDamage()].getMaxDamage() == 0;
	}
	
	@Override
	public IRotorModel getCustomModel(ItemStack par1)
	{
		return null;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1, EntityPlayer par2EntityPlayer, List par3, boolean par4)
	{
		if(NBTHelper.nbtCheck(par1, "Rotor"))
		{
			BasicRotorType type = BasicRotorType.values()[par1.getItemDamage()];
			int damage = par1.getTagCompound().getCompoundTag("Rotor").getInteger("Damage");
			int damageLeft = type.getMaxDamage() - damage;
			int eff = (int)(type.getEfficeny() * 100);
			if(this.isInfinite(par1))
			{
				par3.add("Stays Infinite");
			}
			else
			{
				par3.add("Time the Rotor will stay at Least: ");
				par3.add(MathUtils.getTicksInTime(damageLeft * 64));
			}
			par3.add("Rotor Efficency: " + eff + "%");
			if(Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54))
			{
				par3.add("Damage: " + damage + " / " + type.getMaxDamage());
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("Damage", 0);
		for(BasicRotorType type : BasicRotorType.values())
		{
			ItemStack stack = new ItemStack(par1, 1, type.ordinal());
			stack.setTagInfo("Rotor", nbt);
			par3List.add(stack);
		}
	}
	
	@Override
	public String getName(ItemStack par1)
	{
		switch(par1.getItemDamage())
		{
			case 0: return "Wooden Rotor";
			case 1: return "Wool Rotor";
			case 2: return "Iron Rotor";
			case 3: return "Carbon Rotor";
			case 4: return "Alloy Rotor";
			case 5: return "Iridium Rotor";
			default: return "Unnamed Rotor";
		}
	}
	
	public static enum BasicRotorType
	{
		WoodenRotor(2250, 0, 0.5F, "rotor.basic.wood", RotorWeight.Leight),
		WoolRotor(562, 1, 0.9F, "rotor.basic.wool", RotorWeight.VeryLeight),
		IronRotor(18000, 2, 0.68F, "rotor.basic.iron", RotorWeight.Heavy),
		CarbonRotor(27000, 2, 0.75F, "rotor.basic.carbon", RotorWeight.Medium),
		AlloyRotor(6750, 3, 0.9F, "rotor.basic.alloy", RotorWeight.Heavy),
		IridiumRotor(0, 4, 1.0F, "rotor.basic.iridium", RotorWeight.VeryHeavy);
		
		int maxDamage;
		int tier;
		float eff;
		String displayName;
		ResourceLocation texture;
		RotorWeight weight;
		
		private BasicRotorType(int par1, int par2, float par3, String par4, RotorWeight par5)
		{
			maxDamage = par1;
			tier = par2;
			eff = par3;
			displayName = par4;
			weight = par5;
		}
		
		public static BasicRotorType addRotor(String name, int maxDamage, int tier, float effiency, String names)
		{
			Class[][] classes = new Class[][] {{BasicRotorType.class } };
			BasicRotorType type = EnumHelper.addEnum(classes, BasicRotorType.class, name, maxDamage, tier, effiency, names);
			return type;
		}
		
		public String getDisplayName()
		{
			return displayName;
		}
		
		public float getEfficeny()
		{
			return eff;
		}
		
		public ResourceLocation getRenderTexture()
		{
			if(texture == null)
			{
				texture = new ResourceLocation(CWPreference.ModID.toLowerCase() + ":textures/renders/" + displayName + ".png");
			}
			return texture;
		}
		
		public int getMaxDamage()
		{
			return maxDamage;
		}
		
		public int getTier()
		{
			return tier;
		}
		
		public boolean matchTier(int Wtier)
		{
			if(Wtier - 1 == tier || Wtier == tier || Wtier + 1 == tier)
			{
				return true;
			}
			return false;
		}
		
		public boolean isMetal()
		{
			if(this == AlloyRotor || this == IronRotor)
			{
				return true;
			}
			return false;
		}
		
		public RotorWeight getWeight()
		{
			return weight;
		}
	}




	
}
