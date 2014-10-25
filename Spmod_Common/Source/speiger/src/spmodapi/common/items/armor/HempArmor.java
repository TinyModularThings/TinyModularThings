package speiger.src.spmodapi.common.items.armor;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import speiger.src.api.items.DisplayItem;
import speiger.src.api.items.LanguageItem;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.lib.SpmodAPILib;
import speiger.src.spmodapi.common.util.TextureEngine;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class HempArmor extends ItemArmor implements ISpecialArmor, LanguageItem
{
	
	int type;
	
	public HempArmor(int par1, int par4)
	{
		super(par1, APIUtils.hempArmor, SpmodAPI.core.getArmorTypeFromName("hemp"), par4);
		setMaxDamage(500);
		setCreativeTab(APIUtils.tabHemp);
		APIUtils.hempArmor.customCraftingMaterial = APIItems.hemp;
		type = par4;
	}
	
	@Override
	public String getItemDisplayName(ItemStack par1ItemStack)
	{
		return getDisplayName(par1ItemStack, SpmodAPI.instance);
	}
	
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
	{
		if (slot == 2)
		{
			return SpmodAPILib.ModID.toLowerCase() + ":textures/models/armor/hemp_layer_2.png";
		}
		return SpmodAPILib.ModID.toLowerCase() + ":textures/models/armor/hemp_layer_1.png";
	}
	
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int par1)
	{
		return TextureEngine.getTextures().getTexture(this, 0);
	}

	
	@Override
	public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot)
	{
		if (source == DamageSource.fall || source == DamageSource.starve)
		{
			return new ArmorProperties(0, 0, 0);
		}
		armor.damageItem((int) damage, player);
		return new ArmorProperties(0, 0, 0);
	}
	
	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot)
	{
		return 0;
	}
	
	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot)
	{
	}
	
	@Override
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack item)
	{
		if (world.isRemote)
		{
			return;
		}
		
		if ((world.getWorldTime() % 200 == 0 && player.isWet()) || (world.getWorldTime() % 2400 == 0 && item.getItemDamage() < item.getMaxDamage() - 10))
		{
			item.damageItem(1, player);
		}
		
		if (world.getWorldTime() % 20 != 0)
		{
			return;
		}
		
		NBTTagCompound nbt = item.getTagCompound();
		if (nbt == null)
		{
			nbt = new NBTTagCompound();
		}
		
		if (nbt != null)
		{
			NBTTagList ench = item.getEnchantmentTagList();
			if (ench != null)
			{
				nbt.removeTag(ench.getName());
			}
			
			if (player.isBurning() && !nbt.getBoolean("Burn"))
			{
				nbt.setBoolean("Burn", true);
			}
			if (player.isWet() && nbt.getBoolean("Burn"))
			{
				nbt.setBoolean("Burn", false);
			}
			
			if (nbt.getBoolean("Burn"))
			{
				applyBadEffects(player, item);
				if (player.isBurning())
				{
					player.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 80));
				}
				
			}
		}
		
		if (nbt != null)
		{
			
			nbt.setTag("AttributeModifiers", buildTributeFromItem(item, getArmorSize(player), player));
			
			if (item.itemID == APIItems.hempHelmet.itemID && item.getItemDamage() < 430 && !world.isRemote)
			{
				if (player.getCurrentArmor(0) != null && player.getCurrentArmor(1) != null && player.getCurrentArmor(2) != null && player.getCurrentArmor(3) != null)
				{
					
					if (player.getCurrentArmor(0).itemID == APIItems.hempBoots.itemID && player.getCurrentArmor(1).itemID == APIItems.hempLeggings.itemID && player.getCurrentArmor(2).itemID == APIItems.hempChestPlate.itemID && player.getCurrentArmor(3).itemID == APIItems.hempHelmet.itemID)
					{
						player.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 80));
						List<EntityAnimal> entitys = world.getEntitiesWithinAABB(EntityAnimal.class, AxisAlignedBB.getAABBPool().getAABB(player.posX, player.posY, player.posZ, player.posX, player.posY + 1, player.posZ).expand(30, 10, 30));
						for (EntityAnimal live : entitys)
						{
							if (!live.isChild() && live.inLove == 0 && live.getAge() == 0)
							{
								live.getNavigator().tryMoveToEntityLiving(player, 1D);
								if (live.getDistanceSqToEntity(player) < 1.5D)
								{
									player.inventory.damageArmor(1);
									return;
								}
							}
						}
					}
				}
			}
		}
		item.setTagCompound(nbt);
	}
	
	public int getArmorSize(EntityPlayer player)
	{
		int size = 0;
		ItemStack armor = player.getCurrentArmor(0);
		if (armor != null && armor.itemID == APIItems.hempBoots.itemID)
		{
			size++;
		}
		armor = player.getCurrentArmor(1);
		if (armor != null && armor.itemID == APIItems.hempLeggings.itemID)
		{
			size++;
		}
		armor = player.getCurrentArmor(2);
		if (armor != null && armor.itemID == APIItems.hempChestPlate.itemID)
		{
			size++;
		}
		armor = player.getCurrentArmor(3);
		if (armor != null && armor.itemID == APIItems.hempHelmet.itemID)
		{
			size++;
		}
		return size;
	}
	
	NBTTagList buildTributeFromItem(ItemStack par1, int multible, EntityPlayer player)
	{
		NBTTagList list = new NBTTagList();
		
		Attribute boost = null;
		double strenght = 0D;
		int damage = par1.getItemDamage();
		
		damage -= 20;
		if (damage < 0)
		{
			damage = 0;
		}
		
		switch (type)
		{
			case 3:
				boost = SharedMonsterAttributes.movementSpeed;
				strenght = 0.01D * multible;
				strenght -= (0.01D * multible) * damage / 450;
				
				break;
			case 2:
				boost = APIUtils.jumpBoost;
				strenght = 0.5D * multible;
				strenght -= (0.5D * multible) * damage / 450;
				break;
			case 1:
				boost = SharedMonsterAttributes.maxHealth;
				strenght = 5D * multible;
				strenght -= (5D * multible) * damage / 450;
				break;
			case 0:
				boost = SharedMonsterAttributes.knockbackResistance;
				strenght = 0.25D * multible;
				strenght -= (0.25D * multible) * damage / 450;
				break;
		}
		
		if (strenght <= 0D || boost == null)
		{
			return list;
		}
		
		if (boost == SharedMonsterAttributes.movementSpeed)
		{
			if (player.isInWater())
			{
				strenght -= strenght * 10;
			}
		}
		
		NBTTagCompound nbt = createAtribute(new AttributeModifier(field_111210_e, "Armor Modifire", strenght, 0));
		nbt.setString("AttributeName", boost.getAttributeUnlocalizedName());
		list.appendTag(nbt);
		return list;
	}
	
	private NBTTagCompound createAtribute(AttributeModifier par0AttributeModifier)
	{
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setString("Name", par0AttributeModifier.getName());
		nbttagcompound.setDouble("Amount", par0AttributeModifier.getAmount());
		nbttagcompound.setInteger("Operation", par0AttributeModifier.getOperation());
		nbttagcompound.setLong("UUIDMost", par0AttributeModifier.getID().getMostSignificantBits());
		nbttagcompound.setLong("UUIDLeast", par0AttributeModifier.getID().getLeastSignificantBits());
		return nbttagcompound;
	}
	
	@Override
	public void onUpdate(ItemStack par1, World par2World, Entity par3, int par4, boolean par5)
	{
		if (!par2World.isRemote)
		{
			if (par2World.getWorldTime() % 20 != 0)
			{
				return;
			}
			
			NBTTagCompound nbt = par1.getTagCompound();
			if (nbt == null)
			{
				nbt = new NBTTagCompound();
			}
			
			if (nbt != null)
			{
				NBTTagList ench = par1.getEnchantmentTagList();
				if (ench != null)
				{
					nbt.removeTag(ench.getName());
				}
				
				setStandart(nbt);
				
				if (nbt.getBoolean("Burn"))
				{
					if (par3 != null && par3 instanceof EntityLivingBase)
					{
						par1.damageItem(1, (EntityLivingBase) par3);
					}
				}
				
				if (par3 instanceof EntityPlayer)
				{
					EntityPlayer player = (EntityPlayer) par3;
					if (nbt.getBoolean("Burn"))
					{
						applyBadEffects(player, par1);
					}
				}
			}
			
			par1.setTagCompound(nbt);
			
		}
	}
	
	void setStandart(NBTTagCompound nbt)
	{
		Attribute atribute = null;
		double strenght = 0;
		
		switch (type)
		{
			case 0:
				atribute = SharedMonsterAttributes.knockbackResistance;
				strenght = 0.25D;
				break;
			case 1:
				atribute = SharedMonsterAttributes.maxHealth;
				strenght = 5D;
				break;
			case 2:
				atribute = APIUtils.jumpBoost;
				strenght = 0.5D;
				break;
			case 3:
				atribute = SharedMonsterAttributes.movementSpeed;
				strenght = 0.01D;
				break;
		}
		if (nbt.hasKey("AttributeModifiers"))
		{
			nbt.removeTag("AttributeModifiers");
		}
		NBTTagList list = new NBTTagList();
		NBTTagCompound effect = createAtribute(new AttributeModifier(field_111210_e, "Armor Modifire", strenght, 0));
		effect.setString("AttributeName", atribute.getAttributeUnlocalizedName());
		list.appendTag(effect);
		
		nbt.setTag("AttributeModifiers", list);
		
	}
	
	public void applyBadEffects(EntityPlayer player, ItemStack item)
	{
		item.damageItem(1, player);
		PotionEffect potion = player.getActivePotionEffect(Potion.confusion);
		if (potion == null)
		{
			potion = new PotionEffect(Potion.confusion.id, 100);
		}
		if (potion != null)
		{
			potion.duration += 200;
			if (potion.duration > 2000 && potion.getAmplifier() < 2)
			{
				potion = new PotionEffect(potion.getPotionID(), potion.getDuration() / 20, potion.getAmplifier() + 1);
			}
		}
		
		player.addPotionEffect(potion);
		
		potion = player.getActivePotionEffect(Potion.moveSlowdown);
		if (potion == null)
		{
			potion = new PotionEffect(Potion.moveSlowdown.id, 80);
		}
		if (potion != null)
		{
			potion.duration += 160;
			if (potion.duration > 1600 && potion.getAmplifier() < 2)
			{
				potion = new PotionEffect(potion.getPotionID(), potion.getDuration() / 16, potion.getAmplifier() + 1);
			}
		}
		player.addPotionEffect(potion);
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod par0)
	{
		switch (type)
		{
			case 3:
				return LanguageRegister.getLanguageName(new DisplayItem(par1.itemID), "hemp.armor.Boots", par0);
			case 2:
				return LanguageRegister.getLanguageName(new DisplayItem(par1.itemID), "hemp.armor.Leggings", par0);
			case 1:
				return LanguageRegister.getLanguageName(new DisplayItem(par1.itemID), "hemp.armor.Plate", par0);
			case 0:
				return LanguageRegister.getLanguageName(new DisplayItem(par1.itemID), "hemp.armor.Helmet", par0);
			default:
				return null;
		}
	}
	
	@Override
	public void registerItems(int id, SpmodMod par0)
	{		
	}
	
}
