package speiger.src.compactWindmills.common.items;

import ic2.api.item.Items;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumHelper;

import org.lwjgl.input.Keyboard;

import speiger.src.api.items.DisplayStack;
import speiger.src.api.items.IRotorItem;
import speiger.src.api.items.LanguageItem;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.nbt.NBTHelper;
import speiger.src.api.tiles.IWindmill;
import speiger.src.api.util.MathUtils;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import speiger.src.compactWindmills.CompactWindmills;
import speiger.src.compactWindmills.common.items.ItemRotor.BasicRotorType;
import speiger.src.compactWindmills.common.utils.WindmillFake;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemAdvancedRotor extends ItemTool implements IRotorItem,
		LanguageItem
{
	private static String[] names = new String[] { "rotor.adv.wood", "rotor.adv.wool", "rotor.adv.iron", "rotor.adv.carbon", "rotor.adv.alloy", "rotor.adv.iridium" };
	
	public ItemAdvancedRotor(int par1)
	{
		super(par1, 0.0F, EnumHelper.addToolMaterial("Rotors", 0, 0, 0, 0, 0), new Block[0]);
		this.setHasSubtypes(true);
		this.setMaxDamage(100);
		this.setMaxStackSize(1);
		this.setNoRepair();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1, EntityPlayer par2EntityPlayer, List par3, boolean par4)
	{
		if (NBTHelper.nbtCheck(par1, "Rotor"))
		{
			NBTTagCompound nbt = par1.getTagCompound().getCompoundTag("Rotor");
			BasicRotorType type = BasicRotorType.values()[par1.getItemDamage()];
			int damage = nbt.getInteger("Damage");
			int damageLeft = (type.getMaxDamage() + nbt.getInteger("ExtraLife")) - damage;
			int eff = (int) (this.getRotorEfficeny(par1, WindmillFake.fake) * 100);
			if (this.isInfinite(par1))
			{
				par3.add("Stays Infinite");
			}
			else
			{
				par3.add("Time the Rotor will stay at Least: ");
				par3.add(MathUtils.getTicksInTime(damageLeft * 64));
			}
			par3.add("Rotor Efficency: " + eff + "%");
			if (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54))
			{
				par3.add("Modifires Left: " + nbt.getInteger("Modifire"));
				if (nbt.getBoolean("Ench"))
				{
					par3.add("Is Enchantable");
				}
				par3.add("Damage: " + damage + " / " + (type.getMaxDamage() + nbt.getInteger("ExtraLife")));
				par3.add("Efficency Boost: " + nbt.getFloat("EffBoost") + "%");
				if (nbt.getBoolean("AutoRepair"))
				{
					par3.add("AutoRepair");
				}
				par3.add("Absorbing Damage: " + nbt.getInteger("AbsorbeDamage"));
			}
		}
		
		par3.add("Press Ctrl to get Crafting infos");
		
		if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
		{
			par3.remove(par3.size()-1);
			par3.add("");
			par3.addAll(Arrays.asList("Crafting works like this:", "Throw the Rotor and StickyResin (IC2) and the the Requirement item in Water", "StackSize has to be 1", "It will combine automaticly if it finds everything"));
			par3.add("");
			par3.add("Crafting Items and Effects:");
			par3.addAll(Arrays.asList("IridiumPlate: Efficeny: -1/2 , Unbreakable", "Obsidian: Efficeny: -5%, Extra Damage: 40500 Points", 
					"Book: Allow Enchanting, (Only 1 Time Possible)", 
					"Wool: Efficeny: +25%, ExtraLife: -1/10", "Plutonium: Efficeny: -5%, Activate Autorepair (Rotor still can break,"," Only 1 Time Possible and works only with Metal Rotors)", 
					"Uran235: DamageAbsorbtion: 200000 Points","(Does absorb damage randomly, Works only with Metal Rotors)",
					"DensedIronPlate: Efficey: -2%, Extra Damage: 2250 Points", 
					"Carbon Plate: Extra Damage: -625 Points, Efficeny: +15%"));
		}
	}
	
	@Override
	public boolean isDamaged(ItemStack stack)
	{
		if (NBTHelper.nbtCheck(stack, "Rotor"))
		{
			int damage = stack.getTagCompound().getCompoundTag("Rotor").getInteger("Damage");
			return damage > 0;
		}
		return false;
	}
	
	@Override
	public int getDisplayDamage(ItemStack stack)
	{
		if (NBTHelper.nbtCheck(stack, "Rotor"))
		{
			NBTTagCompound nbt = stack.getTagCompound().getCompoundTag("Rotor");
			int damage = nbt.getInteger("Damage");
			BasicRotorType type = BasicRotorType.values()[stack.getItemDamage()];
			double per = ((double) damage / ((double) type.getMaxDamage() + (double) nbt.getInteger("ExtraLife"))) * 100;
			return (int) per;
		}
		return 0;
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod par0)
	{
		return LanguageRegister.getLanguageName(new DisplayStack(par1), names[par1.getItemDamage()], par0);
	}
	
	@Override
	public String getItemDisplayName(ItemStack par1ItemStack)
	{
		return getDisplayName(par1ItemStack, CompactWindmills.instance);
	}
	
	
	
	@Override
	public void registerItems(int id, SpmodMod par0)
	{
		if (!SpmodModRegistry.areModsEqual(CompactWindmills.instance, par0))
		{
			return;
		}
		for (int i = 0; i < names.length; i++)
		{
			LanguageRegister.getLanguageName(new DisplayStack(new ItemStack(id, 1, i)), names[i], par0);
		}
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
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int par1)
	{
		return CompactWindmills.rotor.getIconFromDamage(par1);
	}
	
	@Override
	public int getTier(ItemStack par1)
	{
		return BasicRotorType.values()[par1.getItemDamage()].getTier();
	}
	
	@Override
	public void damageRotor(ItemStack par1, int damage, IWindmill windmill)
	{
		IRotorItem rotor = getRotor(par1);
		NBTTagCompound nbt = par1.getTagCompound().getCompoundTag("Rotor");
		if (rotor == null || nbt == null)
		{
			return;
		}
		int unBreaking = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, par1);
		if (windmill.getWindmill().getWorldObj().rand.nextInt(10 - unBreaking) == 0)
		{
			damage -= unBreaking;
		}
		if (damage <= 0)
		{
			return;
		}
		
		if (nbt.getInteger("AbsorbeDamage") > 0 && windmill.getWindmill().getWorldObj().rand.nextInt(3) == 0)
		{
			int absorbeLeft = nbt.getInteger("AbsorbeDamage") - damage;
			if (absorbeLeft < 0)
			{
				damage = 0 - absorbeLeft;
			}
			else
			{
				damage = 0;
			}
			nbt.setInteger("AbsorbeDamage", absorbeLeft);
			
		}
		
		if (damage > 0)
		{
			nbt.setInteger("Damage", nbt.getInteger("Damage") + damage);
			makeDamageCheck(par1, windmill);
		}
	}
	
	public void makeDamageCheck(ItemStack par1, IWindmill par2)
	{
		NBTTagCompound nbt = par1.getTagCompound().getCompoundTag("Rotor");
		BasicRotorType type = BasicRotorType.values()[par1.getItemDamage()];
		int damage = nbt.getInteger("Damage");
		if (damage > type.getMaxDamage() + nbt.getInteger("ExtraLife"))
		{
			par2.distroyRotor();
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ResourceLocation getRenderTexture(ItemStack par1)
	{
		return BasicRotorType.values()[par1.getItemDamage()].getRenderTexture();
	}
	
	@Override
	public void onRotorTick(IWindmill windMill, World world, ItemStack item)
	{
		IRotorItem rotor = getRotor(item);
		NBTTagCompound nbt = item.getTagCompound().getCompoundTag("Rotor");
		if (rotor == null || nbt == null)
		{
			return;
		}
		
		if (nbt.getBoolean("AutoRepair") && world.rand.nextInt(3) == 0)
		{
			if (nbt.getInteger("Damage") > 0)
			{
				nbt.setInteger("Damage", nbt.getInteger("Damage") - 1);
			}
		}
		
	}
	
	public static ItemStack createRotor(BasicRotorType par1)
	{
		ItemStack stack = new ItemStack(CompactWindmills.advRotor, 1, par1.ordinal());
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("Damage", 0);
		nbt.setInteger("ExtraLife", 0);
		nbt.setInteger("AbsorbeDamage", 0);
		nbt.setInteger("Modifire", par1.getTier());
		nbt.setFloat("EffBoost", 0.0F);
		nbt.setBoolean("Ench", false);
		nbt.setBoolean("Infinite", false);
		nbt.setBoolean("AutoRepair", false);
		if (par1 == par1.IridiumRotor)
		{
			nbt.setInteger("Modifire", 0);
		}
		stack.setTagInfo("Rotor", nbt);
		return stack;
	}
	
	@Override
	public float getRotorEfficeny(ItemStack par1, IWindmill par2)
	{
		float eff = BasicRotorType.values()[par1.getItemDamage()].getEfficeny();
		if (par2.getFacing() == 0 || par2.getFacing() == 1)
		{
			eff = 0.01F;
		}
		if(!par2.isFake() && par2.getWindmill().getWorldObj().provider.isHellWorld)
		{
			return 0.01F;
		}
		
		if (NBTHelper.nbtCheck(par1, "Rotor"))
		{
			NBTTagCompound nbt = par1.getTagCompound().getCompoundTag("Rotor");
			float end = nbt.getFloat("EffBoost");
			end /= 100;
			eff += end;
		}
		int size = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, par1);
		if (size > 0)
		{
			eff += ((float) (size * 5) / 100);
		}
		
		return eff;
	}
	
	@Override
	public boolean isAdvancedRotor(ItemStack par1)
	{
		return true;
	}
	
	@Override
	public int getItemEnchantability()
	{
		return 100;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (BasicRotorType type : BasicRotorType.values())
		{
			par3List.add(createRotor(type));
		}
	}
	
	@Override
	public boolean isItemTool(ItemStack par1)
	{
		if (NBTHelper.nbtCheck(par1, "Rotor"))
		{
			return par1.getTagCompound().getCompoundTag("Rotor").getBoolean("Ench");
		}
		return false;
	}
	
	@Override
	public boolean isInfinite(ItemStack par1)
	{
		BasicRotorType type = BasicRotorType.values()[par1.getItemDamage()];
		boolean flag = false;
		if (NBTHelper.nbtCheck(par1, "Rotor"))
		{
			flag = par1.getTagCompound().getCompoundTag("Rotor").getBoolean("Infinite");
		}
		return type == type.IridiumRotor || flag;
	}
	
	@Override
	public IRotorModel getCustomModel(ItemStack par1, int size)
	{
		return null;
	}
	
	@Override
	public boolean onEntityItemUpdate(EntityItem par1)
	{
		World world = par1.worldObj;
		if (!world.isRemote && world.getWorldTime() % 20 == 0)
		{
			ItemStack stack = par1.getEntityItem();
			
			if (stack == null || getRotor(stack) == null)
			{
				return super.onEntityItemUpdate(par1);
			}
			BasicRotorType type = BasicRotorType.values()[stack.getItemDamage()];
			IRotorItem rotor = getRotor(stack);
			
			int x = (int) par1.posX;
			int y = (int) par1.posY;
			int z = (int) par1.posZ;
			if (world.getBlockId(x, y, z) == Block.waterStill.blockID)
			{
				NBTTagCompound nbt = stack.getTagCompound().getCompoundTag("Rotor");
				if (nbt.getInteger("Modifire") > 0)
				{
					List<EntityItem> item = world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getAABBPool().getAABB(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1));
					if (match(item, Items.getItem("resin")))
					{
						if (match(item, Items.getItem("iridiumPlate")) && !nbt.getBoolean("Modifire"))
						{
							if (remove(item, Items.getItem("resin"), Items.getItem("iridiumPlate")))
							{
								float eff = rotor.getRotorEfficeny(stack, WindmillFake.fake);
								eff /= 2;
								eff -= nbt.getFloat("EffBoost");
								nbt.setBoolean("Infinite", true);
								nbt.setInteger("Modifire", nbt.getInteger("Modifire") - 1);
								nbt.setFloat("EffBoost", -eff);
							}
						}
						else if (match(item, new ItemStack(Block.obsidian)))
						{
							if (remove(item, Items.getItem("resin"), new ItemStack(Block.obsidian)))
							{
								nbt.setFloat("EffBoost", nbt.getFloat("EffBoost") - 5F);
								nbt.setInteger("ExtraLife", nbt.getInteger("ExtraLife") + 40500);
								nbt.setInteger("Modifire", nbt.getInteger("Modifire") - 1);
							}
						}
						else if (match(item, new ItemStack(Item.book)) && !nbt.getBoolean("Ench"))
						{
							if (remove(item, Items.getItem("resin"), new ItemStack(Item.book)))
							{
								nbt.setInteger("Modifire", nbt.getInteger("Modifire") - 1);
								nbt.setBoolean("Ench", true);
							}
						}
						else if (match(item, new ItemStack(Block.cloth)))
						{
							if (remove(item, Items.getItem("resin"), new ItemStack(Block.cloth)))
							{
								nbt.setInteger("Modifire", nbt.getInteger("Modifire") - 1);
								nbt.setInteger("ExtraLife", (nbt.getInteger("ExtraLive") - (nbt.getInteger("ExtraLive") + type.getMaxDamage()) / 10));
								nbt.setFloat("EffBoost", nbt.getFloat("EffBoost") + 25F);
							}
						}
						else if (match(item, Items.getItem("Plutonium")) && type.isMetal() && !nbt.getBoolean("AutoRepair"))
						{
							if (remove(item, Items.getItem("resin"), Items.getItem("Plutonium")))
							{
								nbt.setInteger("Modifire", nbt.getInteger("Modifire") - 1);
								nbt.setBoolean("AutoRepair", true);
								nbt.setFloat("EffBoost", nbt.getFloat("EffBoost") - 5F);
							}
						}
						else if (match(item, Items.getItem("Uran235")) && type.isMetal())
						{
							if (remove(item, Items.getItem("resin"), Items.getItem("Uran235")))
							{
								nbt.setInteger("Modifire", nbt.getInteger("Modifire") - 1);
								nbt.setInteger("AbsorbeDamage", nbt.getInteger("AbsorbeDamage") + 200000);
							}
						}
						else if (match(item, Items.getItem("denseplateiron")))
						{
							if (remove(item, Items.getItem("denseplateiron"), Items.getItem("resin")))
							{
								nbt.setInteger("Modifire", nbt.getInteger("Modifire") - 1);
								nbt.setInteger("ExtraLife", nbt.getInteger("ExtraLife") + MathUtils.getSekInRotorTicks(7200));
								nbt.setFloat("EffBoost", nbt.getFloat("EffBoost") - 2);
							}
						}
						else if (match(item, Items.getItem("carbonPlate")))
						{
							if (remove(item, Items.getItem("carbonPlate"), Items.getItem("resin")))
							{
								nbt.setInteger("Modifire", nbt.getInteger("Modifire") - 1);
								nbt.setInteger("ExtraLife", nbt.getInteger("ExtraLife") - MathUtils.getSekInRotorTicks(2000));
								nbt.setFloat("EffBoost", nbt.getFloat("EffBoost") + 15F);
							}
						}
					}
				}
			}
			
		}
		return super.onEntityItemUpdate(par1);
	}
	
	public IRotorItem getRotor(ItemStack par1)
	{
		if (par1 != null && par1.getItem() != null && par1.getItem() instanceof IRotorItem)
		{
			return (IRotorItem) par1.getItem();
		}
		return null;
	}
	
	public boolean remove(List<EntityItem> par1, ItemStack... par2)
	{
		int removed = 0;
		for (ItemStack item : par2)
		{
			for (EntityItem par3 : par1)
			{
				ItemStack stack = par3.getEntityItem();
				if (stack != null && item != null && stack.isItemEqual(item) && stack.stackSize == 1)
				{
					par3.setDead();
					if (par3.isDead)
					{
						removed++;
					}
				}
			}
		}
		
		return removed == par2.length;
	}
	
	public boolean match(List<EntityItem> par1, ItemStack par2)
	{
		if (par2 == null)
		{
			return false;
		}
		
		for (EntityItem item : par1)
		{
			ItemStack stack = item.getEntityItem();
			if (stack != null && stack.stackSize == 1 && stack.isItemEqual(par2))
			{
				return true;
			}
		}
		return false;
	}
	
}
