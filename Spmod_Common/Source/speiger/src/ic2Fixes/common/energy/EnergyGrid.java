package speiger.src.ic2Fixes.common.energy;

import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

import net.minecraftforge.common.ForgeDirection;

import org.ejml.data.DenseMatrix64F;
import org.ejml.factory.LinearSolverFactory;

import speiger.src.ic2Fixes.common.core.Util;

class EnergyGrid
{
	private final int uid;
	private final LocalEnergyNet energyNet;
	private final Map<Integer, EnergyNode> nodes = new HashMap<Integer, EnergyNode>();
	private boolean hasNonZeroVoltages = false;
	private boolean lastVoltagesNeedUpdate = false;
	
	private final Set<Integer> activeSources = new HashSet<Integer>();
	private final Set<Integer> activeSinks = new HashSet<Integer>();
	
	private final StructureChance chance = new StructureChance();
	private Future<Iterable<EnergyNode>> calculation;
	private StructureChance.Data lastData = null;
	
	EnergyGrid(LocalEnergyNet par1)
	{
		uid = LocalEnergyNet.getNextEnergyGridUID();
		energyNet = par1;
		par1.grids.add(this);
	}
	
	@Override
	public String toString()
	{
		return new StringBuilder().append("EnergyGrid ").append(uid).toString();
	}
	
	void add(EnergyNode par1, Collection<EnergyNode> par2)
	{
		invalidate();
		
		assert ((!this.nodes.isEmpty()) || (par2.isEmpty()));
		assert ((this.nodes.isEmpty()) || (!par2.isEmpty()) || (par1.isExtraNode()));
		assert (par1.links.isEmpty());
		
		add(par1);
		
		for(EnergyNode neighbor : par2)
		{
			assert (neighbor != par1);
			assert (this.nodes.containsKey(Integer.valueOf(neighbor.uid)));
			
			double loss = (par1.getInnerLoss() + neighbor.getInnerLoss()) / 2.0D;
			EnergyNodeLink link = new EnergyNodeLink(par1, neighbor, loss);
			par1.links.add(link);
			neighbor.links.add(link);
		}
	}
	
	void remove(EnergyNode node)
	{
		invalidate();
		
		for(Iterator<EnergyNodeLink> it = node.links.iterator();it.hasNext();)
		{
			EnergyNodeLink link = it.next();
			EnergyNode neighbor = link.getNeighbor(node);
			boolean found = false;
			
			for(Iterator<EnergyNodeLink> it2 = neighbor.links.iterator();it2.hasNext();)
			{
				if(it2.next() == link)
				{
					it2.remove();
					found = true;
				}
			}
			
			assert (found);
			
			if((neighbor.links.isEmpty()) && (neighbor.tile.removeExtraNode(neighbor)))
			{
				it.remove();
				nodes.remove(Integer.valueOf(neighbor.uid));
			}
		}
		this.nodes.remove(Integer.valueOf(node.uid));
		
		if(node.links.isEmpty())
		{
			this.energyNet.grids.remove(this);
		}
		else if((node.links.size() > 1) && (node.nodeType == EnergyNodeType.Conductor))
		{
			List<Set<EnergyNode>> nodeTable = new ArrayList<Set<EnergyNode>>();
			for(int i = 0;i < node.links.size();i++)
			{
				EnergyNode neighbor = ((EnergyNodeLink)node.links.get(i)).getNeighbor(node);
				Set<EnergyNode> connectedNodes = new HashSet<EnergyNode>();
				
				Queue<EnergyNode> nodesToCheck = new LinkedList<EnergyNode>(Arrays.asList(new EnergyNode[] {neighbor }));
				EnergyNode cNode;
				while((cNode = nodesToCheck.poll()) != null)
				{
					if((connectedNodes.add(cNode)) && (cNode.nodeType == EnergyNodeType.Conductor))
					{
						for(EnergyNodeLink link : cNode.links)
						{
							EnergyNode nNode = link.getNeighbor(cNode);
							if(!connectedNodes.contains(nNode))
							{
								nodesToCheck.add(nNode);
							}
						}
					}
				}
				nodeTable.add(connectedNodes);
			}
			
			assert (nodeTable.size() == node.links.size());
			
			Set<EnergyNode> connectedNodes;
			EnergyGrid grid;
			for(int i = 1;i < node.links.size();i++)
			{
				connectedNodes = nodeTable.get(i);
				EnergyNode neighbor = ((EnergyNodeLink)node.links.get(i)).getNeighbor(node);
				
				assert (connectedNodes.contains(neighbor));
				
				boolean split = true;
				
				for(int j = 0;j < i;j++)
				{
					Set<EnergyNode> cmpList = nodeTable.get(j);
					
					if(cmpList.contains(neighbor))
					{
						split = false;
						break;
					}
				}
				
				if(split)
				{
					grid = new EnergyGrid(energyNet);
					
					for(EnergyNode cNode : connectedNodes)
					{
						boolean needsExtraNode = false;
						if((!cNode.links.isEmpty()) && (cNode.nodeType != EnergyNodeType.Conductor))
						{
							for(int j = 0;j < i;j++)
							{
								Set<EnergyNode> cmpList = nodeTable.get(j);
								if(cmpList.contains(cNode))
								{
									needsExtraNode = true;
									break;
								}
							}
						}
						if(needsExtraNode)
						{
							EnergyNode extraNode = new EnergyNode(this.energyNet, cNode.tile, cNode.nodeType);
							cNode.tile.addExtraNode(extraNode);
							for(Iterator<EnergyNodeLink> it = cNode.links.iterator();it.hasNext();)
							{
								EnergyNodeLink link = it.next();
								if(connectedNodes.contains(link.getNeighbor(cNode)))
								{
									link.replaceNode(cNode, extraNode);
									extraNode.links.add(link);
									it.remove();
								}
							}
							
							assert (!extraNode.links.isEmpty());
							
						}
						else
						{
							assert (this.nodes.containsKey(Integer.valueOf(cNode.uid)));
							nodes.remove(Integer.valueOf(cNode.uid));
							grid.add(cNode);
						}
					}
				}
			}
		}
	}
	
	void merge(EnergyGrid grid)
	{
		assert (this.energyNet.grids.contains(grid));
		
		invalidate();
		
		for(EnergyNode node : grid.nodes.values())
		{
			boolean found = false;
			if(node.nodeType == EnergyNodeType.Conductor)
			{
				for(EnergyNode node2 : this.nodes.values())
				{
					if((node2.tile == node.tile) && (node2.nodeType == node.nodeType))
					{
						found = true;
						for(EnergyNodeLink link : node.links)
						{
							link.replaceNode(node, node2);
							node2.links.add(link);
						}
						
						node2.tile.removeExtraNode(node);
					}
				}
			}
			
			if(!found)
			{
				add(node);
			}
		}
	}
	
	void prepareCalculation()
	{
		assert (this.calculation == null);
		
		if(!this.activeSources.isEmpty())
			this.activeSources.clear();
		if(!this.activeSinks.isEmpty())
			this.activeSinks.clear();
		
		List<EnergyNode> dynamicTierNodes = new ArrayList<EnergyNode>();
		int maxSourceTier = 0;
		
		for(EnergyNode node : this.nodes.values())
		{
			assert (node.getGrid() == this);
			
			switch(EnergyNodeType.values()[node.nodeType.ordinal()])
			{
				case Source:
					IEnergySource source = (IEnergySource)node.tile.entity;
					double offer = source.getOfferedEnergy();
					node.setTier(GlobalEnergyNet.getTierFromPower(offer));
					node.setAmount(offer);
					if(node.getAmount() > 0.0D)
					{
						this.activeSources.add(Integer.valueOf(node.uid));
						maxSourceTier = Math.max(node.getTier(), maxSourceTier);
					}
					else
					{
						node.setAmount(0.0D);
					}
					break;
				case Sink:
					IEnergySink sink = (IEnergySink)node.tile.entity;
					node.setTier(GlobalEnergyNet.getTierFromPower(sink.getMaxSafeInput()));
					node.setAmount(sink.demandedEnergyUnits());
					
					if(node.getAmount() > 0.0D)
					{
						this.activeSinks.add(Integer.valueOf(node.uid));
						if(node.getTier() == 2147483647)
						{
							dynamicTierNodes.add(node);
						}
					}
					else
					{
						node.setAmount(0.0D);
					}
					break;
				case Conductor:
					node.setAmount(0.0D);
			}
			
			assert (node.getAmount() >= 0.0D);
		}
		
		for(EnergyNode node : dynamicTierNodes)
		{
			node.setTier(maxSourceTier);
		}
	}
	
	Runnable startCalculation()
	{
		assert (this.calculation == null);
		
		boolean run = this.hasNonZeroVoltages;
		Iterator<Integer> it;
		if((this.activeSinks.isEmpty()) && (!this.activeSources.isEmpty()))
		{
			run = true;
			for(it = activeSources.iterator();it.hasNext();)
			{
				int nodeId = it.next().intValue();
				EnergyNode node = this.nodes.get(nodeId);
				
				int sharedCount = 1;
				
				for(EnergyNode shared : node.tile.nodes)
				{
					if((shared.uid != nodeId) && (shared.nodeType == EnergyNodeType.Source) && (!shared.getGrid().activeSinks.isEmpty()))
					{
						assert (shared.getGrid().activeSources.contains(Integer.valueOf(shared.uid)));
						assert (shared.getGrid() != this);
						
						sharedCount++;
					}
				}
				node.setAmount(node.getAmount() / sharedCount);
				
				IEnergySource source = (IEnergySource)node.tile.entity;
				source.drawEnergy(node.getAmount());
			}
		}
		
		if(run)
		{
			RunnableFuture task = new FutureTask(new EnergyGridCalculation(this));
			calculation = task;
			return task;
		}
		return null;
	}
	
	void finishCalculation()
	{
		if(calculation == null)
			return;
		try
		{
			Iterable<EnergyNode> result = this.calculation.get();
			for(EnergyNode node : result)
			{
				ForgeDirection dir;
				if(node.links.isEmpty())
				{
					dir = node.links.get(0).getDirFrom(node);
				}
				else
				{
					dir = ForgeDirection.UNKNOWN;
				}
				this.energyNet.addChange(node, dir, node.getAmount(), node.getVoltage());
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		this.calculation = null;
	}
	
	void updateStats()
	{
		if(this.lastVoltagesNeedUpdate)
		{
			this.lastVoltagesNeedUpdate = false;
			for(EnergyNode node : this.nodes.values())
			{
				node.updateStats();
			}
		}
	}
	
	Iterable<EnergyNode> calculate()
	{
		this.lastVoltagesNeedUpdate = true;
		if((this.activeSources.isEmpty()) || (this.activeSinks.isEmpty()))
		{
			for(EnergyNode node : this.nodes.values())
			{
				node.setVoltage(0.0D);
				node.resetCurrents();
			}
			
			if(!this.activeSources.isEmpty())
				this.activeSources.clear();
			if(!this.activeSinks.isEmpty())
				this.activeSinks.clear();
			
			this.hasNonZeroVoltages = false;
			
			return new ArrayList<EnergyNode>();
		}
		
		StructureChance.Data data = calculateDistribution();
		calculateEffects(data);
		
		this.activeSources.clear();
		this.activeSinks.clear();
		
		List<EnergyNode> ret = new ArrayList<EnergyNode>();
		
		for(EnergyNode node : data.emittedNodes)
		{
			if((node.nodeType == EnergyNodeType.Sink) || (node.nodeType == EnergyNodeType.Source))
			{
				ret.add(node.getTop());
			}
		}
		
		this.hasNonZeroVoltages = true;
		return ret;
	}
	
	private void add(EnergyNode node)
	{
		node.setGrid(this);
		EnergyNode prev = this.nodes.put(Integer.valueOf(node.uid), node);
		if(prev != null)
		{
			throw new IllegalStateException(new StringBuilder().append("duplicate node uid, new ").append(node).append(", old ").append(prev).toString());
		}
	}
	
	private void invalidate()
	{
		finishCalculation();
		chance.clear();
	}
	
	private StructureChance.Data calculateDistribution()
	{
		long time = System.nanoTime();
		
		StructureChance.Data data = this.chance.get(activeSources, activeSinks);
		lastData = data;
		
		if(!data.isInitialized)
		{
			copyForOptimize(data);
			optimize(data);
			determineEmittingNodes(data);
			
			int size = data.emittedNodes.size();
			
			data.networkMatrix = new DenseMatrix64F(size, size);
			data.sourceMatrix = new DenseMatrix64F(size, 1);
			data.resultMatrix = new DenseMatrix64F(size, 1);
			
			populateNetworkMatrix(data);
			
			data.solver = LinearSolverFactory.symmPosDef(size);
			if(!data.solver.setA(data.networkMatrix))
			{
				throw new RuntimeException("incompatible matrix");
			}
			data.isInitialized = true;
		}
		
		populateSourceMatrix(data);
		
		data.solver.solve(data.sourceMatrix, data.resultMatrix);
		
		assert (!data.solver.modifiesB());
		
		return data;
	}
	
	private void calculateEffects(StructureChance.Data data)
	{
		for(EnergyNode node : this.nodes.values())
		{
			node.setVoltage(0.0D);
			node.resetCurrents();
		}
		
		for(int row = 0;row < data.emittedNodes.size();row++)
		{
			EnergyNode node = data.emittedNodes.get(row);
			node.setVoltage(data.resultMatrix.get(row));
			
			switch(EnergyNodeType.values()[node.nodeType.ordinal()])
			{
				case Source:
					double currentSource;
					if(LocalEnergyNet.useLinearTransfering)
					{
						double result = data.sourceMatrix.get(row) - node.getVoltage() / node.getSourceResistance();
						double actualAmount = result * node.getVoltage();
						assert (actualAmount <= node.getAmount()): new StringBuilder().append(actualAmount).append(" <= ").append(node.getAmount()).append(" (u=").append(node.getVoltage()).append(")").toString();
						
						node.setAmount(actualAmount - node.getAmount());
					}
					else
					{
						currentSource = node.getAmount();
						node.setAmount(0.0D);
					}
					
					assert (node.getAmount() <= 0.0D);
				case Sink:
					double currentSink = node.getVoltage() / node.getSinkResistance();
					if(LocalEnergyNet.useLinearTransfering)
					{
						node.setAmount(node.getVoltage() * currentSink);
					}
					else
					{
						node.setAmount(currentSink);
					}
					
					assert (node.getAmount() >= 0.0D);
					break;
				case Conductor:
					
			}
		}
		
		Set visiteLinks = new HashSet();
		
		for(Iterator<EnergyNode> it = data.emittedNodes.iterator();it.hasNext();)
		{
			EnergyNode node = it.next();
			for(EnergyNodeLink link : node.links)
			{
				if(link.nodeA == node)
				{
					EnergyNode nodeA = link.nodeA.getTop();
					EnergyNode nodeB = link.nodeB.getTop();
					double totalLoss = link.loss;
					
					for(EnergyNode skipped : link.skippedNodes)
					{
						assert (skipped.nodeType == EnergyNodeType.Conductor);
						skipped = skipped.getTop();
						if(!Double.isNaN(skipped.getVoltage()))
						{
							// TODO Things mayBreak;
						}
						
						EnergyNodeLink link2 = nodeA.getConnectionTo(skipped);
						
						assert (link2 != null);
						
						skipped.setVoltage(Util.lerp(nodeA.getVoltage(), nodeB.getVoltage(), link2.loss / totalLoss));
						link.updateCurrent();
						
						nodeA = skipped;
						totalLoss -= link2.loss;
					}
					
					nodeA.getConnectionTo(nodeB).updateCurrent();
				}
			}
		}
	}
	
	private void copyForOptimize(StructureChance.Data data)
	{
		data.optimizedNodes = new HashMap<Integer, EnergyNode>();
		for(EnergyNode node : this.nodes.values())
		{
			assert (!node.links.isEmpty());
			
			if((node.getAmount() > 0.0D) || (node.nodeType == EnergyNodeType.Conductor))
			{
				assert ((node.nodeType != EnergyNodeType.Sink) || (this.activeSinks.contains(Integer.valueOf(node.uid))));
				assert ((node.nodeType != EnergyNodeType.Source) || (this.activeSources.contains(Integer.valueOf(node.uid))));
				
				data.optimizedNodes.put(Integer.valueOf(node.uid), new EnergyNode(node));
			}
		}
		
		for(Iterator<EnergyNode> it = data.optimizedNodes.values().iterator();it.hasNext();)
		{
			EnergyNode node = it.next();
			assert (!node.links.isEmpty());
			assert (node.getGrid() == this);
			
			for(ListIterator<EnergyNodeLink> iter = node.links.listIterator();iter.hasNext();)
			{
				EnergyNodeLink link = iter.next();
				EnergyNode neighbor = link.getNeighbor(node.uid);
				
				assert (neighbor.getGrid() == this);
				
				if((neighbor.nodeType == EnergyNodeType.Sink) || (neighbor.nodeType == EnergyNodeType.Source) && neighbor.getAmount() <= 0.0)
				{
					iter.remove();
				}
				else if(link.nodeA.uid == node.uid)
				{
					link.nodeA = ((EnergyNode)data.optimizedNodes.get(Integer.valueOf(link.nodeA.uid)));
					link.nodeB = ((EnergyNode)data.optimizedNodes.get(Integer.valueOf(link.nodeB.uid)));
					
					assert ((link.nodeA != null) && (link.nodeB != null));
					
					List<EnergyNode> newSkippedNodes = new ArrayList<EnergyNode>();
					
					for(EnergyNode skippedNode : link.skippedNodes)
					{
						newSkippedNodes.add(data.optimizedNodes.get(Integer.valueOf(skippedNode.uid)));
					}
					link.skippedNodes = newSkippedNodes;
				}
				else
				{
					assert (link.nodeB.uid == node.uid);
					
					boolean foundReverseLink = false;
					
					for(EnergyNodeLink reverseLink : data.optimizedNodes.get(Integer.valueOf(link.nodeA.uid)).links)
					{
						assert (reverseLink.nodeA.uid == link.nodeA.uid);
						
						if((reverseLink.nodeB.uid == node.uid) && (!node.links.contains(reverseLink)))
						{
							foundReverseLink = true;
							iter.set(reverseLink);
							break;
						}
					}
					assert (foundReverseLink);
				}
			}
		}
		
		for(Iterator<EnergyNode> iter = data.optimizedNodes.values().iterator();iter.hasNext();)
		{
			EnergyNode node = iter.next();
			
			assert (!node.links.isEmpty());
			
			for(EnergyNodeLink link : node.links)
			{
				assert (data.optimizedNodes.containsValue(link.nodeA));
				assert (data.optimizedNodes.containsValue(link.nodeB));
				assert (link.nodeA != link.nodeB);
				assert (link.getNeighbor(node).links.contains(link));
			}
		}
		int uid;
		for(Iterator<Integer> it = this.activeSources.iterator();it.hasNext();)
		{
			uid = it.next().intValue();
			assert (data.optimizedNodes.containsKey(Integer.valueOf(uid)));
		}
		
		for(Iterator<Integer> it = this.activeSinks.iterator();it.hasNext();)
		{
			uid = it.next().intValue();
			assert (data.optimizedNodes.containsKey(Integer.valueOf(uid)));
		}
	}
	
	private void optimize(StructureChance.Data data)
	{
		int removed;
		Iterator<EnergyNode> it;
		do
		{
			removed = 0;
			for(it = data.optimizedNodes.values().iterator();it.hasNext();)
			{
				EnergyNode node = it.next();
				if(node.nodeType == EnergyNodeType.Conductor)
				{
					if(node.links.size() < 2)
					{
						it.remove();
						removed++;
						for(EnergyNodeLink link : node.links)
						{
							boolean found = false;
							for(Iterator<EnergyNodeLink> iter = link.getNeighbor(node).links.iterator();iter.hasNext();)
							{
								if(iter.next() == link)
								{
									found = true;
									iter.remove();
								}
							}
							assert (found);
						}
					}
					else if(node.links.size() == 2)
					{
						it.remove();
						removed++;
						
						EnergyNodeLink linkA = node.links.get(0);
						EnergyNodeLink linkB = node.links.get(1);
						EnergyNode neighborA = linkA.getNeighbor(node);
						EnergyNode neighborB = linkB.getNeighbor(node);
						
						if(neighborA == neighborB)
						{
							neighborA.links.remove(linkA);
							neighborB.links.remove(linkB);
						}
						else
						{
							linkA.loss += linkB.loss;
							
							if(linkA.nodeA == node)
							{
								linkA.nodeA = neighborB;
								linkA.dirFromA = linkB.getDirFrom(neighborB);
								
								if(linkB.nodeA == node)
								{
									assert (linkB.nodeB == neighborB);
									Collections.reverse(linkB.skippedNodes);
								}
								else
								{
									assert ((linkB.nodeB == node) && (linkB.nodeA == neighborB));
								}
								
								linkB.skippedNodes.add(node);
								linkB.skippedNodes.addAll(linkA.skippedNodes);
								linkA.skippedNodes = linkB.skippedNodes;
							}
							else
							{
								linkA.nodeB = neighborB;
								linkA.dirFromB = linkB.getDirFrom(neighborB);
								
								if(linkB.nodeB == node)
								{
									assert (linkB.nodeA == neighborB);
									Collections.reverse(linkB.skippedNodes);
								}
								else
								{
									assert ((linkB.nodeA == node) && (linkB.nodeB == neighborB));
								}
								
								linkA.skippedNodes.add(node);
								linkA.skippedNodes.addAll(linkB.skippedNodes);
							}
							assert (linkA.nodeA != linkA.nodeB);
							assert ((linkA.nodeA == neighborA) || linkA.nodeB == neighborA);
							assert ((linkA.nodeA == neighborB) || (linkA.nodeB == neighborB));
							
							boolean found = false;
							
							for(ListIterator<EnergyNodeLink> iter = neighborB.links.listIterator();iter.hasNext();)
							{
								if(iter.next() == linkB)
								{
									found = true;
									iter.set(linkA);
								}
							}
							assert (found);
						}
					}
				}
			}
		}
		while(removed > 0);
		
		for(Iterator<EnergyNode> iter = data.optimizedNodes.values().iterator();it.hasNext();)
		{
			EnergyNode node = iter.next();
			
			assert (!node.links.isEmpty());
			
			for(EnergyNodeLink link : node.links)
			{
				assert (data.optimizedNodes.containsValue(link.nodeA));
				assert (data.optimizedNodes.containsValue(link.nodeB));
				assert (!this.nodes.containsValue(link.nodeA));
				assert (!this.nodes.containsValue(link.nodeB));
				assert (this.nodes.containsValue(link.nodeA.getTop()));
				assert (this.nodes.containsValue(link.nodeB.getTop()));
				assert (link.nodeA != link.nodeB);
				assert ((link.nodeA == node) || (link.nodeB == node));
				assert (link.getNeighbor(node).links.contains(link));
				assert (!link.skippedNodes.contains(link.nodeA));
				assert (!link.skippedNodes.contains(link.nodeB));
				assert (Collections.disjoint(link.skippedNodes, data.optimizedNodes.values()));
				assert (Collections.disjoint(link.skippedNodes, this.nodes.values()));
				assert (new HashSet(link.skippedNodes).size() == link.skippedNodes.size());
				
				EnergyNode start = node.getTop();
				List<EnergyNode> skippedNodes;
				if(link.nodeA == node)
				{
					skippedNodes = link.skippedNodes;
				}
				else
				{
					skippedNodes = new ArrayList<EnergyNode>(link.skippedNodes);
					Collections.reverse(skippedNodes);
				}
				
				for(EnergyNode skipped : link.skippedNodes)
				{
					assert (start.getConnectionTo(skipped.getTop()) != null): new StringBuilder().append(start).append(" -> ").append(skipped.getTop()).append(" not in ").append(start.links).toString();
					start = skipped.getTop();
				}
				assert (start.getConnectionTo(link.getNeighbor(node).getTop()) != null): new StringBuilder().append(start).append(" -> ").append(link.getNeighbor(node).getTop()).append(" not in ").append(start.links).toString();
				
			}
		}
		
		int uid;
		for(Iterator<Integer> iter = this.activeSources.iterator();iter.hasNext();)
		{
			uid = iter.next().intValue();
			assert (data.optimizedNodes.containsKey(Integer.valueOf(uid)));
		}
		for(Iterator<Integer> iter = this.activeSinks.iterator();iter.hasNext();)
		{
			uid = iter.next().intValue();
			assert (data.optimizedNodes.containsKey(Integer.valueOf(uid)));
		}
	}
	
	private static void determineEmittingNodes(StructureChance.Data data)
	{
		data.emittedNodes = new ArrayList<EnergyNode>();
		data.gs = 0.0D;
		data.gl = 0.0D;
		
		int index = 0;
		
		for(EnergyNode node : data.optimizedNodes.values())
		{
			switch(EnergyNodeType.values()[node.nodeType.ordinal()])
			{
				case Source:
					data.emittedNodes.add(node);
					data.gs += 1.0D / node.getSourceResistance();
					break;
				case Sink:
					data.emittedNodes.add(node);
					data.gl += 1.0D / node.getSinkResistance();
					break;
				case Conductor:
					data.emittedNodes.add(node);
			}
		}
		
		assert (data.gs > 0.0D);
		assert (data.gl > 0.0D);
	}
	
	private static void populateNetworkMatrix(StructureChance.Data data)
	{
		for(int row = 0;row < data.emittedNodes.size();row++)
		{
			EnergyNode node = data.emittedNodes.get(row);
			
			for(int col = 0;col < data.emittedNodes.size();col++)
			{
				double value = 0.0D;
				EnergyNode possibleNeighbor;
				if(row == col)
				{
					for(EnergyNodeLink link : node.links)
					{
						if(link.getNeighbor(node) != node)
						{
							value += 1.0D / link.loss;
							assert (link.loss >= 0.0D);
						}
					}
					if(node.nodeType == EnergyNodeType.Sink)
					{
						value += 1.0D / node.getSinkResistance();
						assert (node.getSinkResistance() > 0.0D);
					}
					
					if(LocalEnergyNet.useLinearTransfering && (node.nodeType == EnergyNodeType.Source))
					{
						value += 1.0D;
						if(node.getSourceResistance() <= 0.0D)
						{
							throw new AssertionError();
						}
					}
				}
				else
				{
					possibleNeighbor = data.emittedNodes.get(col);
					for(EnergyNodeLink link : node.links)
					{
						EnergyNode neighbor = link.getNeighbor(node);
						if(neighbor != node)
						{
							if(neighbor == possibleNeighbor)
							{
								value -= 1.0D / link.loss;
								assert (link.loss >= 0.0D);
							}
							
						}
					}
				}
				data.networkMatrix.set(row, col, value);
			}
		}
	}
	
	private void populateSourceMatrix(StructureChance.Data data)
	{
		for(int row = 0;row < data.emittedNodes.size();row++)
		{
			EnergyNode node = data.emittedNodes.get(row);
			double input = 0.0D;
			
			if(node.nodeType == EnergyNodeType.Source)
			{
				if(LocalEnergyNet.useLinearTransfering)
				{
					input = (data.gs + data.gl) * Math.sqrt(data.gl * node.getAmount()) / data.gl;
				}
				else
				{
					input = node.getAmount();
					assert (input > 0.0D);
				}
				data.sourceMatrix.set(row, 0, input);
			}
		}
	}
	
}
