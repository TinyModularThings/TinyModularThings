package speiger.src.api.inventory;

public interface InterfaceAcceptor
{
	boolean acceptItems(IAcceptor par1);
	
	boolean acceptFluids(IAcceptor par1);
	
	boolean acceptEnergy(IAcceptor par1);
	
	boolean addAcceptor(IAcceptor par1);
	
	boolean removeAcceptor(IAcceptor par1);
}
