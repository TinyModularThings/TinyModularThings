package speiger.src.ic2Fixes.common.energy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ejml.data.DenseMatrix64F;
import org.ejml.factory.LinearSolver;

public class StructureChance
{
	private static final int maxSize = 32;
	Map<Key, Data> entries = new HashMap<Key, Data>();
	int hints = 0;
	int misses = 0;
	
	Data get(Set<Integer> activeSources, Set<Integer> activeSinks)
	{
		Key key = new Key(activeSources, activeSinks);
		
		Data ret = (Data)entries.get(key);
		if(ret == null)
		{
			ret = new Data();
			add(key, ret);
			misses+=1;
		}
		else
		{
			hints+=1;
		}
		ret.queries += 1;
		return ret;
	}
	
	void clear()
	{
		entries.clear();
	}
	
	int size()
	{
		return entries.size();
	}
	
	private void add(Key par1, Data par2)
	{
		int min = 2147483647;
		Key minKey = null;
		if(this.entries.size() >= 32)
		{
			for(Map.Entry<Key, Data> entry : this.entries.entrySet())
			{
				if(entry.getValue().queries < min)
				{
					min = entry.getValue().queries;
					minKey = entry.getKey();
				}
			}
			entries.remove(minKey);
		}
		entries.put(new Key(par1), par2);
	}
	
	static class Data
	{
		boolean isInitialized = false;
		Map<Integer, EnergyNode> optimizedNodes;
		List<EnergyNode> emittedNodes;
		DenseMatrix64F networkMatrix;
		DenseMatrix64F sourceMatrix;
		DenseMatrix64F resultMatrix;
		LinearSolver<DenseMatrix64F> solver;
		double gs;
		double gl;
		int queries = 0;
	}
	
	static class Key
	{
		final Set<Integer> activeSources;
		final Set<Integer> activeSinks;
		final int hashCode;
		Key(Set<Integer> par1, Set<Integer> par2)
		{
			activeSources = par1;
			activeSinks = par2;
			hashCode = (activeSources.hashCode() * 31 + activeSinks.hashCode());
		}
		
		Key(Key par1)
		{
			activeSources = par1.activeSources;
			activeSinks = par1.activeSinks;
			hashCode = par1.hashCode;
		}
		
		@Override
		public int hashCode()
		{
			return hashCode;
		}
		
		@Override
		public boolean equals(Object o)
		{
			if(!(o instanceof Key)) return false;
			Key key = (Key)o;
			return (key.activeSources.equals(activeSources)) && (key.activeSinks.equals(activeSinks));
		}


		
		
	}
}
