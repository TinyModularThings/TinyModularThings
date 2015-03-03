package speiger.src.spmodapi.common.items.hemp;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.items.core.SpmodItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemHempOil extends SpmodItem implements IFluidContainerItem
{
	
	public ItemHempOil(int par1)
	{
		super(par1);
		this.setMaxStackSize(1);
		this.setMaxDamage(1000);
		this.setNoRepair();
		this.setCreativeTab(APIUtils.tabHemp);
		this.setRarity(EnumRarity.epic);
	}
	
	@Override
	public String getName(ItemStack par1)
	{
		return "Hemp Oil";
	}
	
	@Override
	public FluidStack getFluid(ItemStack container)
	{
		return new FluidStack(APIUtils.hempResin, container.getItemDamage());
	}
	
	@Override
	public int getCapacity(ItemStack container)
	{
		return 1000;
	}
	
	@Override
	public int fill(ItemStack container, FluidStack resource, boolean doFill)
	{
		if(resource.fluidID != APIUtils.hempResin.getID())
		{
			return 0;
		}
		int damage = container.getItemDamage();
		int newDamage = Math.min(1000, damage + resource.amount);
		if(doFill)
		{
			container.setItemDamage(newDamage);
		}
		return newDamage - damage;
	}
	
	@Override
	public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain)
	{
		return null;
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		if(stack != null && entity != null && entity instanceof EntityPlayer && ((EntityPlayer)entity).getHealth() < ((EntityPlayer)entity).getMaxHealth())
		{
			return HealPlayer((EntityPlayer)entity, stack, entity.worldObj);
		}
		return false;
	}
	
	public boolean HealPlayer(EntityPlayer par1, ItemStack par2, World par3)
	{
		if(par1 != null && par2 != null && !par3.isRemote)
		{
			if(par1.getHealth() < par1.getMaxHealth())
			{
				if(par2.getItemDamage() <= 500)
				{
					par1.clearActivePotions();
					par2.damageItem(500, par1);
					par1.heal(5);
					par1.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 400, 1));
					par1.addPotionEffect(new PotionEffect(Potion.nightVision.id, 400, 1));
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1, EntityPlayer par2, List par3, boolean par4)
	{
		par3.add("Hemp Resin does heal Players");
		par3.add("You can heal other Player, by Punshing them");
	}
	
	@Override
	public ItemStack onEaten(ItemStack par1, World par2, EntityPlayer par3)
	{
		if(par1 != null && par3 != null && !par2.isRemote)
		{
			this.HealPlayer(par3, par1, par2);
		}
		return par1;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack par1, World par2, EntityPlayer par3)
	{
		if(par3.canEat(true) && par1.getItemDamage() <= 500 && par3.getHealth() < par3.getMaxHealth())
		{
			par3.setItemInUse(par1, this.getMaxItemUseDuration(par1));
		}
		return par1;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack)
	{
		return EnumAction.drink;
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack)
	{
		return 60;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int par1)
	{
		return super.getIconFromDamage(0);
	}
	
	
}
