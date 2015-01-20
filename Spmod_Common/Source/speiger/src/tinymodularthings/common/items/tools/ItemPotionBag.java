package speiger.src.tinymodularthings.common.items.tools;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import speiger.src.api.client.gui.IItemGui;
import speiger.src.api.common.data.nbt.NBTHelper;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.enums.EnumIDs;
import speiger.src.tinymodularthings.common.items.core.TinyItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPotionBag extends TinyItem implements IItemGui
{
	
	
	public ItemPotionBag(int par1)
	{
		super(par1);
		this.setHasSubtypes(true);
		this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabs.tabFood);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public boolean hasGui(ItemStack par1)
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer getGui(InventoryPlayer par1, ItemStack par2)
	{
		return new GuiInventoryCore(getContainer(par1, par2)).setAutoDrawing();
	}

	@Override
	public boolean hasContainer(ItemStack par1)
	{
		return true;
	}
	
	@Override
	public Icon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
	{
		return getIcon(stack, renderPass);
	}

	@Override
	public Icon getIcon(ItemStack stack, int pass)
	{
		NBTTagCompound nbt = NBTHelper.getTag(stack, "Bag");
		NBTTagCompound data = nbt.getCompoundTag("BagData");
		if(!data.getBoolean("Active"))
		{
			return TextureEngine.getTextures().getTexture(this, 2);
		}
		
		PotionInventory inv = new PotionInventory(null, stack);
		if(!inv.hasPotions())
		{
			return TextureEngine.getTextures().getTexture(this, 1);
		}
		return TextureEngine.getTextures().getTexture(this, 0);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}
	
	@Override
	public int getRenderPasses(int metadata)
	{
		return 1;
	}

	@Override
	public AdvContainer getContainer(InventoryPlayer par1, ItemStack par2)
	{
		return new AdvContainer(par1, new PotionInventory(par1.player, par2));
	}
	
	
	
	@Override
	public void onUpdate(ItemStack par1, World par2, Entity par3, int par4, boolean par5)
	{
		super.onUpdate(par1, par2, par3, par4, par5);
		if(!par2.isRemote && par3 != null && par3 instanceof EntityPlayer)
		{
			if(!NBTHelper.getTag(par1, "Bag").hasKey("BagData"))
			{
				NBTTagCompound data = new NBTTagCompound();
				data.setBoolean("Active", true);
				NBTHelper.getTag(par1, "Bag").setCompoundTag("BagData", data);
			}
			boolean active = NBTHelper.getTag(par1, "Bag").getCompoundTag("BagData").getBoolean("Active");
			if(active)
			{
				PotionInventory inv = new PotionInventory(((EntityPlayer)par3), par1);
				inv.onTick(par1);
				inv.onInventoryChanged();
			}
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1, World par2, EntityPlayer par3)
	{
		if(!par2.isRemote)
		{
			if(par3.isSneaking())
			{
				NBTTagCompound nbt = NBTHelper.getTag(par1, "Bag").getCompoundTag("BagData");
				nbt.setBoolean("Active", !nbt.getBoolean("Active"));
			}
			else
			{
				par3.openGui(TinyModularThings.instance, EnumIDs.Items.getId(), par2, 0, 0, 0);
			}
		}
		return par1;
	}

	public static ItemStack createEmptyPotionBag(int id)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("ID", ("Bag:"+System.currentTimeMillis()));
		ItemStack stack = new ItemStack(id, 1, 0);
		stack.setTagInfo("Bag", nbt);
		return stack;
	}
	
	public static boolean delay = false;
	
	@ForgeSubscribe
	public void onDrop(ItemTossEvent evt)
	{
		if(delay)
		{
			delay = false;
			return;
		}
		ItemStack stack = evt.entityItem.getEntityItem();
		EntityPlayer player = evt.player;
		if(player != null && player.openContainer != null && player.openContainer instanceof AdvContainer && ((AdvContainer)player.openContainer).getInvName().equals("Potion Bag"))
		{
			if(stack.itemID == TinyItems.potionBag.itemID)
			{
				((AdvContainer)player.openContainer).onContainerClosed(player);
				player.closeScreen();
				delay = true;
			}
		}
	}

	@Override
	public String getName(ItemStack par1)
	{
		return "Potion Bag";
	}
	
	
}