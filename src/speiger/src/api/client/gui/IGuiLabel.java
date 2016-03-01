package speiger.src.api.client.gui;

import net.minecraft.client.Minecraft;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IGuiLabel
{
	@SideOnly(Side.CLIENT)
	public void onRender(Minecraft mc, int x, int y);
}
