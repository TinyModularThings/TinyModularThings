package speiger.src.spmodapi.common.fluids.hemp;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import buildcraft.BuildCraftTransport;

public class BlockFluidHempResin extends BlockFluidClassic
{
	
	public BlockFluidHempResin(int id, Fluid fluid)
	{
		super(id, fluid, Material.water);
		this.setQuantaPerBlock(15);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
	{
		if (!par1World.isRemote && par5Entity != null)
		{
			if (par5Entity instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) par5Entity;
				if (par1World.rand.nextInt(20) == 0)
				{
					player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 200, 4));
				}
				if (par1World.rand.nextInt(400) == 0 && par1World.getBlockMetadata(par2, par3, par4) == 0)
				{
					par1World.playSoundAtEntity(player, "random.burp", 0.5F, par1World.rand.nextFloat() * 0.1F + 0.9F);
					par1World.setBlockToAir(par2, par3, par4);
					player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 1200, 20));
				}
				if (player.isSneaking() && par1World.getBlockMetadata(par2, par3, par4) == 0)
				{
					par1World.playSoundAtEntity(player, "random.burp", 0.5F, par1World.rand.nextFloat() * 0.1F + 0.9F);
					par1World.setBlockToAir(par2, par3, par4);
					player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 1200, 20));
				}
			}
			else if (par5Entity instanceof EntityItem)
			{
				EntityItem item = (EntityItem) par5Entity;
				if (item.getEntityItem().itemID == Item.silk.itemID && item.getEntityItem().stackSize > 1)
				{
					if (par1World.rand.nextInt(80) == 0)
					{
						try
						{
							ItemStack stack = item.getEntityItem();
							int removeChance = 64 - stack.stackSize;
							if (removeChance == 0)
							{
								par1World.setBlock(par2, par3, par4, 0);
							}
							else if (par1World.rand.nextInt(removeChance) == 0)
							{
								par1World.setBlock(par2, par3, par4, 0);
							}
							
							EntityItem drop = new EntityItem(par1World, par2, par3, par4, new ItemStack(BuildCraftTransport.pipeWaterproof, (item.getEntityItem().stackSize / 2)));
							par1World.spawnEntityInWorld(drop);
							stack.stackSize -= drop.getEntityItem().stackSize * 2;
						}
						catch (Exception e)
						{
						}
					}
				}
			}
		}
	}
	
}
