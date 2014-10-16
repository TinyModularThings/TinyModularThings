package speiger.src.spmodapi.common.items.exp;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import speiger.src.api.items.DisplayStack;
import speiger.src.api.items.IExpBottle;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.items.SpmodItem;
import speiger.src.spmodapi.common.util.TextureEngine;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ExpBottle extends SpmodItem implements IExpBottle
{
	String[] exp = new String[] { "small", "medium", "big", "huge", "transdimensional" };
	static int[] exps = new int[] { 100, 1000, 10000, 100000, 1000000 };
	
	public ExpBottle(int par1)
	{
		super(par1);
		this.setHasSubtypes(true);
		this.setMaxDamage(100);
		this.setNoRepair();
		this.setMaxStackSize(1);
		this.setCreativeTab(APIUtils.tabCrafing);
	}
	
	@Override
	public void registerItems(int id, SpmodMod par0)
	{
		if (!SpmodModRegistry.areModsEqual(par0, getMod()))
		{
			return;
		}
		for (int i = 0; i < exp.length; i++)
		{
			LanguageRegister.getLanguageName(new DisplayStack(new ItemStack(id, 1, 0)), "exp.bottle." + exp[i], par0);
		}
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod Start)
	{
		if (par1.getTagCompound() == null)
		{
			par1 = this.getExpBottle(par1.itemID, 0, false, false);
		}
		NBTTagCompound nbt = par1.getTagCompound().getCompoundTag("Exp");
		return LanguageRegister.getLanguageName(new DisplayStack(par1), "exp.bottle." + exp[nbt.getInteger("ID")], Start);
		
	}
	
	@Override
	public Icon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
	{
		if (stack.hasTagCompound())
		{
			NBTTagCompound nbt = stack.getTagCompound().getCompoundTag("Exp");
			return TextureEngine.getTextures().getTexture(this, nbt.getInteger("ID"));
		}
		return TextureEngine.getTextures().getTexture(this, 0);
	}
	
	@Override
	public Icon getIcon(ItemStack stack, int pass)
	{
		if (stack.hasTagCompound())
		{
			NBTTagCompound nbt = stack.getTagCompound().getCompoundTag("Exp");
			return TextureEngine.getTextures().getTexture(this, nbt.getInteger("ID"));
		}
		return TextureEngine.getTextures().getTexture(this, 0);
	}
	
	
	
	@Override
	public void registerTexture(TextureEngine par1)
	{
		par1.setCurrentPath("exp");
		String[] array = new String[exp.length];
		for(int i = 0;i<exp.length;i++)
		{
			array[i] = "bottle_" + exp[i];
		}
		par1.registerTexture(this, array);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1, World par2, EntityPlayer par3)
	{
		if (!par2.isRemote)
		{
			if (par3.isSneaking())
			{
				int expLevel = par3.experienceLevel;
				if (expLevel > 0)
				{
					if (this.needExp(par1))
					{
						
						par3.addExperienceLevel(-1);
						int expCap = par3.xpBarCap();
						expCap -= this.charge(par1, expCap);
						if (expCap > 0)
						{
							par3.addExperience(expCap);
						}
					}
					else
					{
						par3.addChatMessage("Exp Bottle is Full");
					}
				}
				else
				{
					par3.addChatMessage("You need at least 1 Exp level");
				}
			}
			else
			{
				if (this.hasExp(par1))
				{
					par3.addExperience(this.discharge(par1, this.getTransferlimit(par1)));
				}
			}
		}
		return par1;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		if (par1.hasTagCompound())
		{
			NBTTagCompound nbt = par1.getTagCompound().getCompoundTag("Exp");
			int stored = nbt.getInteger("Cu");
			int max = nbt.getInteger("Max");
			int transfer = nbt.getInteger("Transfer");
			par3List.add("Can Store: " + max + " Exp");
			par3List.add("Stored Exp: " + stored + " Exp");
			par3List.add("Exp Transferlimit: " + transfer + " Exp");
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}
	
	@Override
	public int getDisplayDamage(ItemStack stack)
	{
		double charge = ((double) this.getStoredExp(stack) / (double) this.getMaxExp(stack)) * 100;
		if (charge == 0 && this.getStoredExp(stack) > 0)
		{
			return 100;
		}
		return 100 - (int) charge;
	}
	
	@Override
	public int getMaxExp(ItemStack par1)
	{
		NBTTagCompound nbt = par1.getTagCompound().getCompoundTag("Exp");
		return nbt.getInteger("Max");
	}
	
	@Override
	public int getStoredExp(ItemStack par1)
	{
		NBTTagCompound nbt = par1.getTagCompound().getCompoundTag("Exp");
		return nbt.getInteger("Cu");
	}
	
	public static ItemStack getExpBottle(int id, int type, boolean doubles, boolean full)
	{
		ItemStack item = new ItemStack(id, 1, full ? 1 : 100);
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("ID", type);
		nbt.setInteger("Max", doubles ? exps[type] * 2 : exps[type]);
		nbt.setInteger("Cu", full ? nbt.getInteger("Max") : 0);
		nbt.setInteger("Transfer", nbt.getInteger("Max") / 100);
		item.setTagInfo("Exp", nbt);
		return item;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3)
	{
		for (int i = 0; i < exp.length; i++)
		{
			par3.add(getExpBottle(par1, i, false, false));
			par3.add(getExpBottle(par1, i, false, true));
		}
	}
	
	@Override
	public int charge(ItemStack par1, int amount)
	{
		boolean toBig = this.getTransferlimit(par1) < amount;
		if (toBig)
		{
			int charge = this.getStoredExp(par1);
			if (charge + this.getTransferlimit(par1) > this.getMaxExp(par1))
			{
				int max = (charge + this.getTransferlimit(par1)) - this.getMaxExp(par1);
				NBTTagCompound nbt = par1.getTagCompound().getCompoundTag("Exp");
				nbt.setInteger("Cu", this.getMaxExp(par1));
				return this.getTransferlimit(par1) - max;
			}
			NBTTagCompound nbt = par1.getTagCompound().getCompoundTag("Exp");
			nbt.setInteger("Cu", this.getTransferlimit(par1) + this.getStoredExp(par1));
			return this.getTransferlimit(par1);
		}
		
		int adding = this.getStoredExp(par1) + amount;
		if (adding > this.getMaxExp(par1))
		{
			int totalAdded = amount - (adding - this.getStoredExp(par1));
			NBTTagCompound nbt = par1.getTagCompound().getCompoundTag("Exp");
			nbt.setInteger("Cu", this.getMaxExp(par1));
			return totalAdded;
		}
		
		NBTTagCompound nbt = par1.getTagCompound().getCompoundTag("Exp");
		nbt.setInteger("Cu", adding);
		return amount;
	}
	
	@Override
	public int discharge(ItemStack par1, int amount)
	{
		boolean toMuch = this.getTransferlimit(par1) < amount;
		if (toMuch)
		{
			int re = this.getStoredExp(par1) - this.getTransferlimit(par1);
			int extra = 0;
			if (re < 0)
			{
				extra += -re;
				re = 0;
			}
			NBTTagCompound nbt = par1.getTagCompound().getCompoundTag("Exp");
			nbt.setInteger("Cu", re);
			return this.getTransferlimit(par1) - extra;
		}
		int re = this.getStoredExp(par1) - amount;
		int extra = 0;
		if (re < 0)
		{
			extra += -re;
			re = 0;
		}
		NBTTagCompound nbt = par1.getTagCompound().getCompoundTag("Exp");
		nbt.setInteger("Cu", re);
		
		return amount - extra;
	}
	
	@Override
	public int getTransferlimit(ItemStack par1)
	{
		NBTTagCompound nbt = par1.getTagCompound().getCompoundTag("Exp");
		return nbt.getInteger("Transfer");
	}
	
	@Override
	public boolean hasExp(ItemStack par1)
	{
		return this.getStoredExp(par1) > 0;
	}
	
	@Override
	public boolean needExp(ItemStack par1)
	{
		return this.getStoredExp(par1) < this.getMaxExp(par1);
	}
	
}
