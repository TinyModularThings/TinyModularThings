package speiger.src.spmodapi.common.blocks.utils;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import speiger.src.api.common.data.packets.ISpmodPacket;
import speiger.src.api.common.data.packets.SpmodPacketHelper.SpmodPacket;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.network.packets.base.TileNBTPacket;
import speiger.src.spmodapi.common.tile.TileFacing;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Radio extends TileFacing
{
	public static final String InvalidTrack = "No Track";
	public PlayList currentList = new PlayList();
	public ArrayList<PlayList> playlists = new ArrayList<PlayList>();
	public boolean publicRadio = false;
	public boolean isPlaying = false;
	public PlayingMode mode = PlayingMode.PlayingNothing;
	
	//Client Only Varibales
	
	
	@Override
	public ItemStack getItemDrop()
	{
		return new ItemStack(APIBlocks.blockUtils);
	}
	
	@Override
	public boolean isSixSidedFacing()
	{
		return false;
	}
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		return Block.music.getIcon(side, 0);
	}
	
	@Override
	public boolean canUpdate()
	{
		return worldObj.isRemote;
	}

	@Override
	public boolean hasContainer()
	{
		return true;
	}
	
	public void onTick()
	{
		if(!worldObj.isRemote)
		{
			//This Tile Entity Ticks only on Clients so No Server Effects at all...
			return;
		}
	}
	
	@Override
	public void init()
	{
		super.init();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getInvNameYOffset()
	{
		return 55;
	}

	@Override
	public boolean renderInnerInv()
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onGuiLoad(GuiInventoryCore par1, int guiX, int guiY)
	{
		
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void drawFrontExtras(GuiInventoryCore par1, int guiX, int guiY, int mouseX, int mouseY)
	{
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onButtonClick(GuiInventoryCore par1, GuiButton par2)
	{
		
	}

	
	@SideOnly(Side.CLIENT)
	public boolean validPlayer()
	{
		return getPlayer().username.equalsIgnoreCase(getBlockOwner());
	}
	
	@SideOnly(Side.CLIENT)
	public EntityPlayer getPlayer()
	{
		return Minecraft.getMinecraft().thePlayer;
	}

	@Override
	public boolean onSlotClicked(AdvContainer par1, int slotID, int mouseButton, int modifier, EntityPlayer player)
	{
		return true;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
	}
	
	
	@Override
	public void onSpmodPacket(SpmodPacket par1)
	{
		ISpmodPacket basePacket = par1.getPacket();
		if(par1.getSide() == Side.SERVER)
		{
			if(basePacket instanceof TileNBTPacket)
			{
				TileNBTPacket packet = (TileNBTPacket)basePacket;
				NBTTagCompound data = packet.getData();
				if(data.hasKey("Init"))
				{
					PacketDispatcher.sendPacketToPlayer(getDescriptionPacket(), par1.getFMLPlayer());
				}
			}
		}
	}


	public static class PlayList
	{
		ArrayList<String> songs = new ArrayList<String>();
		LoopMode mode = LoopMode.None;
		byte currentSong = 0;
	}
	
	public static enum LoopMode
	{
		Track,
		List,
		None;
	}
	
	public static enum PlayingMode
	{
		PlayingNothing,
		Playing,
		ChangingSong,
		SongNotAviable;
	}
	

}
