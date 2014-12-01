package speiger.src.spmodapi.common.items.crafting;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import speiger.src.api.common.data.nbt.NBTHelper;
import speiger.src.spmodapi.common.items.core.SpmodItem;
import speiger.src.spmodapi.common.util.TextureEngine;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCircuit extends SpmodItem
{
	public ItemCircuit(int id)
	{
		super(id);
		this.setHasSubtypes(true);
	}
	
	
	
	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		switch(stack.getItemDamage())
		{
			case 3: return 1;
		}
		return 64;
	}
	
	@Override
	public int getMaxDamage(ItemStack stack)
	{
		switch(stack.getItemDamage())
		{
			case 3: return 100;
		}
		return 0;
	}
	
	
	
	@Override
	public boolean hasContainerItem()
	{
		return true;
	}
	
	public boolean isDamageable(int meta)
	{
		switch(meta)
		{
			case 3: return true;
			default: return false;
		}
	}

	@Override
	public ItemStack getContainerItemStack(ItemStack itemStack)
	{
		ItemStack par1 = itemStack.copy();
		if(isDamageable(par1.getItemDamage()))
		{
			par1 = damageItem(par1);
			return par1;
		}
		return null;
	}

	public ItemStack damageItem(ItemStack par1)
	{
		int damage = par1.getItemDamageForDisplay();
		damage+=1;
		if(damage > par1.getMaxDamage())
		{
			return null;
		}
		par1.setItemDamage(damage);
		return par1;
	}

	@Override
	public void setDamage(ItemStack stack, int damage)
	{		
		if(isDamageable(stack.getItemDamage()))
		{
			stack.setTagInfo("Damage", new NBTTagInt("Damage", damage));
			return;
		}
		super.setDamage(stack, 0);
	}


	@Override
	public int getDisplayDamage(ItemStack stack)
	{
		if(isDamageable(stack.getItemDamage()))
		{
			if(NBTHelper.nbtCheck(stack, "Damage"))
			{
				return stack.getTagCompound().getInteger("Damage");
			}
		}
		return super.getDisplayDamage(stack);
	}



	@Override
	public String getName(ItemStack par1)
	{
		switch(par1.getItemDamage())
		{
			case 0: return "Simple Circuit";
			case 1: return "Advanced Circuit";
			default: return "Logic Diamond";
		
		}
	}
	
	

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for(int i = 0;i<4;i++)
		{
			par3List.add(new ItemStack(par1, 1, i));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1, EntityPlayer par2, List par3, boolean par4)
	{
		int id = par1.getItemDamage();
		if(id == 3)par3.add("Storage Logic");
	}

	@Override
	public void registerTexture(TextureEngine par1)
	{
		super.registerTexture(par1);
		par1.setCurrentPath("circuits");
		par1.registerTexture(this, "BasicCircuit", "AdvancedCircuit", 
		"LogicDiamond", "StorageLogicDiamond");
	}
	
	
	
	
}
