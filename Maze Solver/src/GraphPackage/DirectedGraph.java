package GraphPackage;
import java.util.Iterator;
import ADTPackage.*; // Classes that implement various ADTs
/**
 A class that implements the ADT directed graph.
- @author Frank M. Carrano
 @author Timothy M. Henry
 @version 5.1
 */
public class DirectedGraph<T> implements GraphInterface<T>
{
   private DictionaryInterface<T, VertexInterface<T>> vertices;
   private int edgeCount;
   
   public DirectedGraph()
   {
      vertices = new UnsortedLinkedDictionary<>();
      edgeCount = 0;
   } // end default constructor

   public boolean addVertex(T vertexLabel)
   {
      VertexInterface<T> addOutcome = vertices.add(vertexLabel, new Vertex<>(vertexLabel));
      return addOutcome == null; // Was addition to dictionary successful?
   } // end addVertex
   
   public boolean addEdge(T begin, T end, double edgeWeight)
   {
      boolean result = false;
      VertexInterface<T> beginVertex = vertices.getValue(begin);
      VertexInterface<T> endVertex = vertices.getValue(end);
      if ( (beginVertex != null) && (endVertex != null) )
         result = beginVertex.connect(endVertex, edgeWeight);
      if (result)
         edgeCount++;
      return result;
   } // end addEdge
   
   public boolean addEdge(T begin, T end)
   {
      return addEdge(begin, end, 0);
   } // end addEdge

   public boolean hasEdge(T begin, T end)
   {
      boolean found = false;
      VertexInterface<T> beginVertex = vertices.getValue(begin);
      VertexInterface<T> endVertex = vertices.getValue(end);
      if ( (beginVertex != null) && (endVertex != null) )
      {
         Iterator<VertexInterface<T>> neighbors = beginVertex.getNeighborIterator();
         while (!found && neighbors.hasNext())
         {
            VertexInterface<T> nextNeighbor = neighbors.next();
            if (endVertex.equals(nextNeighbor))
               found = true;
         } // end while
      } // end if
      
      return found;
   } // end hasEdge

	public boolean isEmpty()
	{
	  return vertices.isEmpty();
	} // end isEmpty

	public void clear()
	{
	  vertices.clear();
	  edgeCount = 0;
	} // end clear

	public int getNumberOfVertices()
	{
	  return vertices.getSize();
	} // end getNumberOfVertices

	public int getNumberOfEdges()
	{
	  return edgeCount;
	} // end getNumberOfEdges

	protected void resetVertices()
	{
	   Iterator<VertexInterface<T>> vertexIterator = vertices.getValueIterator();
	   while (vertexIterator.hasNext())
	   {
	      VertexInterface<T> nextVertex = vertexIterator.next();
	      nextVertex.unvisit();
	      nextVertex.setCost(0);
	      nextVertex.setPredecessor(null);
	   } // end while
	} // end resetVertices
	
	public StackInterface<T> getTopologicalOrder() 
	{
		resetVertices();

		StackInterface<T> vertexStack = new LinkedStack<>();
		int numberOfVertices = getNumberOfVertices();
		for (int counter = 1; counter <= numberOfVertices; counter++)
		{
			VertexInterface<T> nextVertex = findTerminal();
			nextVertex.visit();
			vertexStack.push(nextVertex.getLabel());
		} // end for
		
		return vertexStack;	
	} // end getTopologicalOrder
	
	
	public LinkedQueue getBreadthFirstTraversal(T origin, T end) {
		resetVertices();//Resets vertices for multiple calls
		LinkedQueue<VertexInterface<T>> traversalOrder = new LinkedQueue<>();//For traversal
		LinkedQueue<VertexInterface<T>> vertexQueue = new LinkedQueue<>();//For hold the vertices, queue is necessary for bfs
		VertexInterface<T> originVertex = vertices.getValue(origin);//Start vertex
		VertexInterface<T> endVertex = vertices.getValue(end);//End vertex
		if(originVertex == null) {
			System.out.println("There is no starting such you have entered");
			System.exit(0);
		}
		if(endVertex == null) {
			System.out.println("There is no ending such you have entered");
			System.exit(0);
		}
		originVertex.visit();
		traversalOrder.enqueue( originVertex);
		vertexQueue.enqueue( originVertex);
		while(!vertexQueue.isEmpty()) {//Starts looking neigbours of the dequeued vertex
			VertexInterface<T> frontVertex = vertexQueue.dequeue();
			while(frontVertex.hasNeighbor() && frontVertex.getUnvisitedNeighbor()!=null) {//Checks all of the neighbors before  proceed
				VertexInterface<T> nextNeighbor = frontVertex.getUnvisitedNeighbor();
				if(!nextNeighbor.isVisited()) {
					nextNeighbor.visit();
					traversalOrder.enqueue(nextNeighbor);//Adds every neighbour to traversal
					vertexQueue.enqueue(nextNeighbor);//Adds every neighbour to vertex queue to check their neighbors when their turn comes	
				}	
				if(nextNeighbor.getLabel().equals((String)end))
					return traversalOrder;
				 
			}
		}
		return traversalOrder;
	}//end of bfs

	public LinkedQueue getDepthFirstTraversal(T origin, T end) {
		resetVertices();//Resets vertices for multiple calls
		LinkedQueue<VertexInterface<T>> traversalOrder = new LinkedQueue<>();//For traversal
		LinkedStack<VertexInterface<T>> vertexStack = new LinkedStack<>();//For hold the vertices, stack is necessary for dfs
		VertexInterface<T> originVertex = vertices.getValue(origin);//Start vertex
		VertexInterface<T> endVertex = vertices.getValue(end);//Start vertex
		if(originVertex == null) {
			System.out.println("There is no starting such you have entered");
			System.exit(0);
		}
		if(endVertex == null) {
			System.out.println("There is no ending such you have entered");
			System.exit(0);
		}
		originVertex.visit();
		traversalOrder.enqueue(originVertex);
		vertexStack.push(originVertex);
		while(!vertexStack.isEmpty()) {//Starts looking neigbours of the top vertex
			VertexInterface<T> topVertex = vertexStack.peek();
			if(topVertex.getUnvisitedNeighbor() != null) {
				VertexInterface<T> nextNeighbor = topVertex.getUnvisitedNeighbor();
				nextNeighbor.visit();
				traversalOrder.enqueue(nextNeighbor);//Adds every neighbour to traversal
				vertexStack.push(nextNeighbor);//Adds every neighbour to vertex stack then starts looking from thaa
				if(nextNeighbor.getLabel().equals((String)end))
					return traversalOrder;
			}
			else
				vertexStack.pop();

		}
		return traversalOrder;
	}//end of dfs
	
	public int getShortestPath(T begin, T end, StackInterface<T> path) {
		// TODO Auto-generated method stub
		resetVertices();//Resets vertices for multiple calls
		boolean done = false;
		LinkedQueue<VertexInterface<T>> vertexQueue = new LinkedQueue<>();//For look vertices and their neighbors
		VertexInterface<T> originVertex = vertices.getValue(begin);
		VertexInterface<T> endVertex = vertices.getValue(end);
		if(originVertex == null) {
			System.out.println("There is no starting such you have entered");
			System.exit(0);
		}
		if(endVertex == null) {
			System.out.println("There is no ending such you have entered");
			System.exit(0);
		}
		originVertex.visit();
		vertexQueue.enqueue(originVertex);
		while(!done && !vertexQueue.isEmpty()) {//Looks until there is no vertex to check
			VertexInterface<T> frontVertex = vertexQueue.dequeue();
			while(!done && frontVertex.getUnvisitedNeighbor()!=null) {//Checks unvisited neighbors
				VertexInterface<T> nextNeighbor = frontVertex.getUnvisitedNeighbor();
				if(!nextNeighbor.isVisited()) {
					 nextNeighbor.visit();
					 nextNeighbor.setCost(frontVertex.getCost()+1);//Sets costs
					 nextNeighbor.setPredecessor(frontVertex);//Sets predecessor to track until start vertex
					 vertexQueue.enqueue(nextNeighbor);//Adds new neighbor to queue
				}
				if(nextNeighbor.getLabel().equals((String)end))//Find condition
					 done = true;
			}
		}
		//Tracks from end to start and adds them into the path stack
		path.push((T) endVertex);
		VertexInterface<T> vertex = endVertex;
		while(vertex.hasPredecessor()) {
			vertex = vertex.getPredecessor();
			path.push((T) vertex);
		}
		return (int)endVertex.getCost();
	}//end of shortestPath
  
	public double getCheapestPath(T begin, T end, StackInterface<T> path) {
		// TODO Auto-generated method stub
		resetVertices();//Resets vertices for multiple calls		
		boolean done = false;
		HeapPriorityQueue<EntryPQ>  priorityQueue = new HeapPriorityQueue<>();//For look vertices and their neighbors with priority queue to determine chepast way
		VertexInterface<T> originVertex = vertices.getValue(begin);
		VertexInterface<T> endVertex = vertices.getValue(end);
		if(originVertex == null) {
			System.out.println("There is no starting such you have entered");
			System.exit(0);
		}
		if(endVertex == null) {
			System.out.println("There is no ending such you have entered");
			System.exit(0);
		}
		priorityQueue.add( new EntryPQ(originVertex, 0, null));
		while(!done && !priorityQueue.isEmpty()) {//Looks until there is no vertex to check
			EntryPQ frontEntry = (DirectedGraph<T>.EntryPQ) priorityQueue.remove();
			VertexInterface<T> frontVertex = frontEntry.vertex;
			if(!frontVertex.isVisited()) {//Looks unvisited neighbors
				frontVertex.visit();
				frontVertex.setCost(frontEntry.cost+1);//Sets cost
				frontVertex.setPredecessor( frontEntry.previousVertex);
				 if(frontVertex.getLabel().equals((String)end)) {//End condition
					 done  = true;
				 }

				 else {
					 Iterator<VertexInterface<T>> v =  frontVertex.getNeighborIterator();//Neighbors of looked vertex
					 Iterator<Double> w = frontVertex.getWeightIterator();//Weight of looked vertex
					 while(v.hasNext()) {//Looks all neighbors of looked vertex
						 VertexInterface<T> nextNeighbor = v.next();
						 double weightOfEdgeToNeighbor = w.next();
						 if(!nextNeighbor.isVisited()) {
							 double nextCost = weightOfEdgeToNeighbor + frontVertex.getCost();//Sets costs with weight
							 priorityQueue.add(new EntryPQ(nextNeighbor, nextCost, frontVertex));//Adds new vertex to priority queue
						 }
					 }
				 }
			}
		}
		//Tracks from end to start and adds them into the path stack
		path.push((T) endVertex);
		VertexInterface<T> vertex = endVertex;
		while(vertex.hasPredecessor()) {
			vertex = vertex.getPredecessor();
			path.push((T) vertex);
		}
		return (int)endVertex.getCost();
	}//end of cheapestPath

	protected VertexInterface<T> findTerminal()
	{
		boolean found = false;
		VertexInterface<T> result = null;

		Iterator<VertexInterface<T>> vertexIterator = vertices.getValueIterator();

		while (!found && vertexIterator.hasNext())
		{
			VertexInterface<T> nextVertex = vertexIterator.next();
			
			// If nextVertex is unvisited AND has only visited neighbors)
			if (!nextVertex.isVisited())
			{ 
				if (nextVertex.getUnvisitedNeighbor() == null )
				{ 
					found = true;
					result = nextVertex;
				} // end if
			} // end if
		} // end while

		return result;
	} // end findTerminal

	// Used for testing
	public void displayEdges()
	{
		System.out.println("\nEdges exist from the first vertex in each line to the other vertices in the line.");
		System.out.println("(Edge weights are given; weights are zero for unweighted graphs):\n");
		Iterator<VertexInterface<T>> vertexIterator = vertices.getValueIterator();
		while (vertexIterator.hasNext())
		{
			((Vertex<T>)(vertexIterator.next())).display();
		} // end while
	} // end displayEdges 
	
	private class EntryPQ implements Comparable<EntryPQ>
	{
		private VertexInterface<T> vertex; 	
		private VertexInterface<T> previousVertex; 
		private double cost; // cost to nextVertex
		
		private EntryPQ(VertexInterface<T> vertex, double cost, VertexInterface<T> previousVertex)
		{
			this.vertex = vertex;
			this.previousVertex = previousVertex;
			this.cost = cost;
		} // end constructor
		
		public VertexInterface<T> getVertex()
		{
			return vertex;
		} // end getVertex
		
		public VertexInterface<T> getPredecessor()
		{
			return previousVertex;
		} // end getPredecessor

		public double getCost()
		{
			return cost;
		} // end getCost
		
		public int compareTo(EntryPQ otherEntry)
		{
			// Using opposite of reality since our priority queue uses a maxHeap;
			// could revise using a minheap
			return (int)Math.signum(otherEntry.cost - cost);
		} // end compareTo
		
		public String toString()
		{
			return vertex.toString() + " " + cost;
		} // end toString 
	} // end EntryPQ

	//Looks every vertex from first vertex and compares every vertex with other vertices to determine if they are adjacent
	public void setEdges() {
		String[] beginVertexLabel, tempVertexLabel;
		Iterator<VertexInterface<T>> vertexIterator = vertices.getValueIterator();
		while(vertexIterator.hasNext()) {//Checks every vertex
			VertexInterface<T> beginVertex = vertexIterator.next();
			beginVertexLabel = beginVertex.getLabel().toString().split("-");
			Iterator<VertexInterface<T>> tempVertexIterator = vertices.getValueIterator();
			while(tempVertexIterator.hasNext()) {//Checks every vertex
				VertexInterface<T> tempVertex = tempVertexIterator.next();
				tempVertexLabel = tempVertex.getLabel().toString().split("-");
				int rowDifference = Integer.valueOf(beginVertexLabel[0])- Integer.valueOf(tempVertexLabel[0]);
				int columnDifference = Integer.valueOf(beginVertexLabel[1])- Integer.valueOf(tempVertexLabel[1]);
				//If they are adjacent then adds edge between themm with a random weight
				if((rowDifference == 1 || rowDifference == -1) && columnDifference == 0) {
					addEdge(tempVertex.getLabel(), beginVertex.getLabel(), randomNumber(1,5));
					addEdge(beginVertex.getLabel(), tempVertex.getLabel(), randomNumber(1,5));

				}
				else if((columnDifference == 1 || columnDifference == -1) && rowDifference == 0) {
					addEdge(tempVertex.getLabel(), beginVertex.getLabel(), randomNumber(1,5));
					addEdge(beginVertex.getLabel(), tempVertex.getLabel(), randomNumber(1,5));
				}
				
			}
		}
	}//end of setEdges

	public String[][] setAdjacencyMatrix() {
		String[][] adjMatrix = new String[getNumberOfVertices()+1][getNumberOfVertices()+1];
		Iterator<VertexInterface<T>> vertexIterator = vertices.getValueIterator();
		int index = adjMatrix.length-1;
		while(vertexIterator.hasNext()) {//Initialize matrix with the vertex names
			String label = (String)(vertexIterator.next()).getLabel();
			adjMatrix[index][0] = label;
			adjMatrix[0][index] = label;
			index--;
		}
		vertexIterator = vertices.getValueIterator();
		int row = 0, column = 0;
		while(vertexIterator.hasNext()) {//Loop for every vertex
			VertexInterface<T> vertex = vertexIterator.next();
			Iterator<VertexInterface<T>> v = vertex.getNeighborIterator();
			Iterator<Double> w = vertex.getWeightIterator();
			for(int i =1; i<adjMatrix.length; i++) {//Finds the row which vertex exists
				if(adjMatrix[i][0].equals(vertex.getLabel())) {
					row = i;
					break;
				}
			}
			while(v.hasNext()) {//Loop for a vertex's neighbours
				VertexInterface<T> neighbour = v.next();
				for(int i =1; i<adjMatrix[i].length; i++) {//Finds the column which neighbor vertex exits
					if(adjMatrix[0][i].equals(neighbour.getLabel())) {
						column = i;
						break;
					}
				}
				double weight = w.next();
				adjMatrix[row][column] = String.valueOf(weight);//Adds the weight to show adjacency to the found index
			}
		}
		return adjMatrix;
	}//end of setAdjacencyMatrix

	//Function for genereta random numbers in given range
	public int randomNumber(int min, int max) {
		return (int) ((Math.random() * (max-min)) + min);
	}//end of randomNumber
	
} // end DirectedGraph
