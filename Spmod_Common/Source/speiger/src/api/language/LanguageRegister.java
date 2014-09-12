package speiger.src.api.language;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Scanner;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraftforge.fluids.Fluid;
import speiger.src.api.blocks.BlockStack;
import speiger.src.api.items.DisplayItem;
import speiger.src.api.items.DisplayStack;
import speiger.src.api.items.InfoStack;
import speiger.src.api.util.SpmodMod;
import cpw.mods.fml.common.FMLLog;

/**
 * 
 * @author Speiger
 * 
 */
public class LanguageRegister
{
	private static HashMap<SpmodMod, ArrayList<String>> strings = new HashMap<SpmodMod, ArrayList<String>>();
	
	public static ChatMessageComponent createChatMessage(String text)
	{
		return new ChatMessageComponent().addText(text);
	}
	
	/**
	 * 
	 * @param par0
	 *            The Mod that Request the Locilised Name
	 * @param par1
	 *            The Object that means simply from which kind of thing you
	 *            request
	 * @param par2
	 *            The Name of the Item
	 * @return the name
	 */
	public static String getLanguageName(Object par1, String par2, SpmodMod par0)
	{
		String object = "";
		
		if (par1 instanceof DisplayItem)// Normal Items
		{
			object = "item";
		}
		else if (par1 instanceof DisplayStack)// Metasensitive Items
		{
			object = "metaItem";
		}
		else if (par1 instanceof InfoStack) // The Add information function
		{
			object = "info";
		}
		else if (par1 instanceof Block) // Normal Blocks
		{
			object = "block";
		}
		else if (par1 instanceof BlockStack) // Meta Blocks
		{
			object = "metaBlock";
		}
		else if (par1 instanceof Fluid) // Fluids/Liquids
		{
			object = "fluid";
		}
		else if (par1 instanceof GuiContainer || par1 instanceof TileEntity) // Guis
		{
			object = "tile";
		}
		else if (par1 instanceof CreativeTabs)
		{
			object = "tab";
		}
		
		String end = "";
		
		if (object == "")
		{
			end = par2;
		}
		else
		{
			end = object + "." + par2;
		}
		
		addString(end, par0);
		
		HashMap<String, String> list = LanguageBuilder.getCurrentLanguage(par0);
		if (list.get(end) != null)
		{
			return list.get(end);
		}
		
		list = LanguageBuilder.getDefaultLanguage(par0);
		
		if (list.get(end) != null)
		{
			return list.get(end);
		}
		
		return end;
		
	}
	
	private static void addString(String par1, SpmodMod par2)
	{
		ArrayList<String> list = new ArrayList<String>();
		if (strings.get(par2) != null)
		{
			list.addAll(strings.get(par2));
		}
		
		if (!list.contains(par1))
		{
			list.add(par1);
		}
		
		strings.put(par2, list);
		
	}
	
	public static void printModLanguage(SpmodMod par1)
	{
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> everything = new ArrayList<String>();
		if (strings.get(par1) != null)
		{
			list.addAll(strings.get(par1));
			everything.addAll(strings.get(par1));
		}
		
		if (list != null && list.size() > 0)
		{
			File file = new File(Minecraft.getMinecraft().mcDataDir, "Spmod-Debug");
			try
			{
				if (!file.exists() || !file.isDirectory())
				{
					file.mkdirs();
				}
				if (file.exists() && file.isDirectory())
				{
					File lang = new File(file, "LanguageExport_" + par1.getName() + ".lang");
					if (!lang.exists())
					{
						lang.createNewFile();
					}
					FMLLog.getLogger().info("Before: " + list.size());
					Scanner scan = new Scanner(lang);
					while (scan.hasNext())
					{
						String next = scan.nextLine();
						next = next.substring(0, next.length() - 1);
						if (next.equalsIgnoreCase("NEW"))
						{
							continue;
						}
						if (list.contains(next))
						{
							list.remove(next);
						}
						else
						{
							everything.add(next);
						}
					}
					scan.close();
					FMLLog.getLogger().info("After: " + list.size());
					Collections.sort(everything);
					if (list.size() > 0)
					{
						FMLLog.getLogger().info("Found new things to print");
						Formatter format = new Formatter(lang);
						
						for (String s : everything)
						{
							if (list.contains(s))
							{
								String finals = s + "=";
								format.format("%s%n%s%n", "NEW!", finals);
								continue;
							}
							String finals = s + "=";
							format.format("%s%n", finals);
						}
						
						format.close();
					}
					
				}
			}
			catch (Exception e)
			{
				FMLLog.getLogger().info("Spmod Debug folder did not get Created");
			}
		}
	}
	
}