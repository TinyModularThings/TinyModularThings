package speiger.src.api.common.recipes.util;

public enum RecipeHardness
{
    Extrem_Easy(0), 
    Very_Easy(1), 
    Easy(2), 
    Medium(3), 
    Hard(4), 
    Very_Hard(5), 
    Extrem_Hard(6), 
    Insane(7), 
    Nearly_Impossible(8);
    
	int id;
	
	private RecipeHardness(int par1)
	{
		id = par1;
	}
	
    public int getComplexity()
    {
      return this.id;
    }
}
