package speiger.src.spmodapi.common.items.hemp;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.api.common.world.items.plates.PlateInterface;
import speiger.src.api.common.world.items.plates.PlateManager;
import speiger.src.spmodapi.common.blocks.deko.MultiPlate;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.handler.PlateHandler;
import speiger.src.spmodapi.common.items.core.SpmodPlacerItem;
import speiger.src.spmodapi.common.util.TextureEngine;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMultiPlate extends SpmodPlacerItem
{
	
	public ItemMultiPlate(int par1)
	{
		super(par1);
		this.setHasSubtypes(true);
		this.setCreativeTab(APIUtils.tabHempDeko);
		PlateManager.plates = new PlateHandler();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int par1)
	{
		return TextureEngine.getTextures().getTexture(this, par1, 0);
	}
	
	@Override
	public void registerTexture(TextureEngine par1)
	{
		par1.markForDelay(this);
	}
	
	@Override
	public boolean onTextureAfterRegister(TextureEngine par1)
	{
		boolean flag = false;
		String[] mods = new String[PlateManager.plates.getAllIdentifiers().size()];
		String[] plates = new String[mods.length];
		par1.removePath();
		for(int i = 0;i<plates.length;i++)
		{
			String cu = PlateManager.plates.getIconFromIdentity(PlateManager.plates.getAllIdentifiers().get(i));
			String[] textures = cu.split(":");
			par1.setCurrentMod(textures[0]);
			par1.registerTexture(new ItemStack(this, 1, i), textures[1]);
			par1.finishMod();
			flag = true;
		}
		return flag;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3)
	{
		PlateInterface manager = PlateManager.plates;
		if (manager != null && manager.getAllIdentifiers().size() > 0)
		{
			for (int i = 0; i < manager.getAllIdentifiers().size(); i++)
			{
				par3.add(new ItemStack(par1, 1, manager.getInfoFromIdentity(manager.getAllIdentifiers().get(i)).getMetadata()));
			}
		}
	}
	
	@Override
	public String getName(ItemStack par1)
	{
		PlateInterface manager = PlateManager.plates;
		if(manager != null && manager.getAllIdentifiers().size() > par1.getItemDamage())
		{
			return manager.getInfoFromIdentity(manager.getAllIdentifiers().get(par1.getItemDamage())).getDisplayName();
		}
		return null;
	}

	@Override
	public BlockStack getBlockToPlace(int meta)
	{
		return new BlockStack(APIBlocks.multiPlate);
	}
	
	

	@Override
	public boolean canPlaceBlock(World world, int x, int y, int z, BlockStack block, int side, ItemStack par1)
	{
		return block.getBlock().isBlockSolidOnSide(world, x, y, z, ForgeDirection.getOrientation(side));
	}

	@Override
	public boolean onAfterPlaced(World world, int x, int y, int z, int side, EntityPlayer player, ItemStack item)
	{
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		boolean flag = true;
		if(tile != null && tile instanceof MultiPlate)
		{
			MultiPlate plate = (MultiPlate)tile;
			flag = false;
			int meta = item.getItemDamage();
			plate.setFacing((short)side);
			if(side == 0 || side == 1)
			{
				plate.setRotation((short)((short)MathHelper.floor_double(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3));
			}
			if(!plate.canPlacedOnSide(ForgeDirection.getOrientation(side).getOpposite()))
			{
				flag = true;
			}
			PlateInterface info = PlateManager.plates;
			if(info != null && info.getAllIdentifiers().size() > meta && !flag)
			{
				plate.setIdentity(info.getAllIdentifiers().get(meta));
				return true;
			}
			flag = true;
		}
		if(world.isRemote)
		{
			flag = false;
		}
		if(flag)
		{
			world.setBlockToAir(x, y, z);
		}
		return false;
	}
}
