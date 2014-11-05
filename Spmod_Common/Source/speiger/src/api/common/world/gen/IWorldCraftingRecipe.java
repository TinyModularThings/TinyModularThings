package speiger.src.api.common.world.gen;

import net.minecraft.entity.player.EntityPlayer;
import speiger.src.api.common.world.blocks.BlockPosition;
import speiger.src.api.common.world.blocks.BlockStack;

public interface IWorldCraftingRecipe
{
	/**
	 * This function is simply for checking if the recipe Matches. Thats it.
	 * @return Your Block that start your crafting structure.
	 */
	public BlockStack getStartingBlock();
	
	/**
	 * This function asks you if the player can craft this multi structure. If not the crafting progress will be stopped.
	 * @param blockPosition The Position of the your Block
	 * @param crafter Player who want to craft it.
	 * @return true for allow starting and false for denie.
	 */
	public boolean canCraftBlockStructure(BlockPosition blockPosition, EntityPlayer crafter);
	
	/**
	 * if Player Finishes the Crafting Progress and says now i am finish. 
	 * @return Your block that make the Structure finish.
	 */
	public BlockStack getFinsiherBlock();
	
	/**
	 * last thing. Maybe if the player finish the structure you get the Access to the Player. To do all your stuff. 
	 * @param par1 The Player
	 * @return false to deny the finish of the crafting. Note: this will remove the whole crafting progress of the player!
	 */
	public boolean canFinishMultiStructureCrafting(EntityPlayer par1);
	
	/**
	 * This function returns will be called before you get the access. Here you can check your structure.
	 * @param startBlock Core Position
	 * @return true for validating the structure. False denies it but only send a message to the Player that the structure is not finish.
	 */
	public boolean isStructureFinish(BlockPosition startBlock);
	
	/**
	 * This function simply checks if the player placed the Finishing block at the right Position. Maybe he did not want to place it at that position. Can happen AnyTime.
	 * @param startBlock 
	 * @param finishBlock
	 * @return false means simply its removeing the crafting position. true will call the is structure finish function.
	 */
	public boolean isFinishingBlockAtRightPosition(BlockPosition startBlock, BlockPosition finishBlock);
	
	/**
	 * This function you can see as the ICraftingHandler function on Crafted. But here you start everything what should after it
	 * @Note: if you want that the ICrafting Handler get called on your block. Then just place your Block at the starter Position. If that is not null and differend Before than i call the ICraftingHandler.
	 * @param startBlock
	 * @param player
	 */
	public void onFinsihedCrafting(BlockPosition startBlock, EntityPlayer crafter);

}
