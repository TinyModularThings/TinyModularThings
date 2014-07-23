package speiger.src.spmodapi.common.fluids.hemp;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import speiger.src.spmodapi.common.lib.SpmodAPILib;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
		if(!par1World.isRemote && par5Entity != null && par5Entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) par5Entity;
			if(par1World.rand.nextInt(20) == 0)
			{
				player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 200, 4));
			}
			if(par1World.rand.nextInt(400) == 0 && par1World.getBlockMetadata(par2, par3, par4) == 0)
			{
				par1World.playSoundAtEntity(player, "random.burp", 0.5F, par1World.rand.nextFloat() * 0.1F + 0.9F);
				par1World.setBlockToAir(par2, par3, par4);
				player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 1200, 20));
			}
			if(player.isSneaking() && par1World.getBlockMetadata(par2, par3, par4) == 0)
			{
				par1World.playSoundAtEntity(player, "random.burp", 0.5F, par1World.rand.nextFloat() * 0.1F + 0.9F);
				par1World.setBlockToAir(par2, par3, par4);
				player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 1200, 20));
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1)
	{
		this.blockIcon = par1.registerIcon(SpmodAPILib.ModID.toLowerCase()+":hemp/hemp.resin");
	}
	
	
	
	
	
}
