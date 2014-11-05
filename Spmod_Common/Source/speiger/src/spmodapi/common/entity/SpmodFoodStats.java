package speiger.src.spmodapi.common.entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import speiger.src.api.common.data.nbt.INBTReciver;
import speiger.src.api.common.registry.helpers.SpmodMod;
import speiger.src.spmodapi.SpmodAPI;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SpmodFoodStats extends FoodStats implements INBTReciver
{
	public static HashMap<String, Boolean> hardcorePeacefull = new HashMap<String, Boolean>();
	
	public static SpmodFoodStats food = new SpmodFoodStats();
	
	private SpmodFoodStats()
	{
		
	}
	
    /** The player's food level. */
    public int foodLevel = 20;

    /** The player's food saturation. */
    public float foodSaturationLevel = 5.0F;

    /** The player's food exhaustion. */
    public float foodExhaustionLevel;

    /** The player's food timer value. */
    public int foodTimer;
    public int prevFoodLevel = 20;

    public SpmodFoodStats(FoodStats par1)
	{
    	NBTTagCompound nbt = new NBTTagCompound();
    	par1.writeNBT(nbt);
    	this.readNBT(nbt);
	}
    
    /**
     * Args: int foodLevel, float foodSaturationModifier
     */
    public void addStats(int par1, float par2)
    {
        this.foodLevel = Math.min(par1 + this.foodLevel, 20);
        this.foodSaturationLevel = Math.min(this.foodSaturationLevel + (float)par1 * par2 * 2.0F, (float)this.foodLevel);
    }

    /**
     * Eat some food.
     */
    public void addStats(ItemFood par1ItemFood)
    {
        this.addStats(par1ItemFood.getHealAmount(), par1ItemFood.getSaturationModifier());
    }

    /**
     * Handles the food game logic.
     */
    public void onUpdate(EntityPlayer par1EntityPlayer)
    {
        int i = par1EntityPlayer.worldObj.difficultySetting;
        this.prevFoodLevel = this.foodLevel;
        initPlayer(par1EntityPlayer);
        if(hardcorePeacefull.get(par1EntityPlayer.username))
        {
        	i = 2;
        }
        
        if (this.foodExhaustionLevel > 4.0F)
        {
            this.foodExhaustionLevel -= 4.0F;

            if (this.foodSaturationLevel > 0.0F)
            {
                this.foodSaturationLevel = Math.max(this.foodSaturationLevel - 1.0F, 0.0F);
            }
            else if (i > 0)
            {
                this.foodLevel = Math.max(this.foodLevel - 1, 0);
            }
        }

        if (par1EntityPlayer.worldObj.getGameRules().getGameRuleBooleanValue("naturalRegeneration") && this.foodLevel >= 18 && par1EntityPlayer.shouldHeal())
        {
            ++this.foodTimer;

            if (this.foodTimer >= 80)
            {
                par1EntityPlayer.heal(1.0F);
                this.addExhaustion(3.0F);
                this.foodTimer = 0;
            }
        }
        else if (this.foodLevel <= 0)
        {
            ++this.foodTimer;

            if (this.foodTimer >= 80)
            {
                if (par1EntityPlayer.getHealth() > 10.0F || i >= 3 || par1EntityPlayer.getHealth() > 1.0F && i >= 2)
                {
                    par1EntityPlayer.attackEntityFrom(DamageSource.starve, 1.0F);
                }

                this.foodTimer = 0;
            }
        }
        else
        {
            this.foodTimer = 0;
        }
    }

    private void initPlayer(EntityPlayer par1)
	{
    	if(hardcorePeacefull.get(par1.username) == null)
    	{
    		hardcorePeacefull.put(par1.username, false);
    	}
	}

	/**
     * Reads food stats from an NBT object.
     */
    public void readNBT(NBTTagCompound par1NBTTagCompound)
    {
        if (par1NBTTagCompound.hasKey("foodLevel"))
        {
            this.foodLevel = par1NBTTagCompound.getInteger("foodLevel");
            this.foodTimer = par1NBTTagCompound.getInteger("foodTickTimer");
            this.foodSaturationLevel = par1NBTTagCompound.getFloat("foodSaturationLevel");
            this.foodExhaustionLevel = par1NBTTagCompound.getFloat("foodExhaustionLevel");
        }
    }

    /**
     * Writes food stats to an NBT object.
     */
    public void writeNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setInteger("foodLevel", this.foodLevel);
        par1NBTTagCompound.setInteger("foodTickTimer", this.foodTimer);
        par1NBTTagCompound.setFloat("foodSaturationLevel", this.foodSaturationLevel);
        par1NBTTagCompound.setFloat("foodExhaustionLevel", this.foodExhaustionLevel);
    }

    /**
     * Get the player's food level.
     */
    public int getFoodLevel()
    {
        return this.foodLevel;
    }

    @SideOnly(Side.CLIENT)
    public int getPrevFoodLevel()
    {
        return this.prevFoodLevel;
    }

    /**
     * If foodLevel is not max.
     */
    public boolean needFood()
    {
        return this.foodLevel < 20;
    }

    /**
     * adds input to foodExhaustionLevel to a max of 40
     */
    public void addExhaustion(float par1)
    {
        this.foodExhaustionLevel = Math.min(this.foodExhaustionLevel + par1, 40.0F);
    }

    /**
     * Get the player's food saturation level.
     */
    public float getSaturationLevel()
    {
        return this.foodSaturationLevel;
    }

    @SideOnly(Side.CLIENT)
    public void setFoodLevel(int par1)
    {
        this.foodLevel = par1;
    }

    @SideOnly(Side.CLIENT)
    public void setFoodSaturationLevel(float par1)
    {
        this.foodSaturationLevel = par1;
    }

	@Override
	public void loadFromNBT(NBTTagCompound par1)
	{
		hardcorePeacefull.clear();
		NBTTagList list = par1.getTagList("FoodStats");
		for(int i = 0;i<list.tagCount();i++)
		{
			NBTTagCompound nbt = (NBTTagCompound)list.tagAt(i);
			String key = nbt.getString("Key");
			boolean value = nbt.getBoolean("Value");
			hardcorePeacefull.put(key, value);
		}
	}

	@Override
	public void saveToNBT(NBTTagCompound par1)
	{
		NBTTagList nbt = new NBTTagList();
		Iterator<Entry<String, Boolean>> iter = hardcorePeacefull.entrySet().iterator();
		for(;iter.hasNext();)
		{
			Entry<String, Boolean> entry = iter.next();
			NBTTagCompound cu = new NBTTagCompound();
			cu.setString("Key", entry.getKey());
			cu.setBoolean("Value", entry.getValue());
			nbt.appendTag(cu);
		}
		par1.setTag("FoodStats", nbt);
	}

	@Override
	public void finishLoading()
	{
	}

	@Override
	public SpmodMod getOwner()
	{
		return SpmodAPI.instance;
	}

	@Override
	public String getID()
	{
		return "hardcore.food";
	}

}
