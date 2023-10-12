import GraphPackage.*;

import java.io.*;
import java.util.*;

import ADTPackage.*;

public class Test {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String[][] adjMatrix;
		
		String[] mazeArray = fileReader("maze2.txt");
		UndirectedGraph<String> maze = graphMaker(mazeArray);
		
		maze.displayEdges();//Can be placed afte displayTwoDimensional
		adjMatrix = maze.setAdjacencyMatrix();
		displayTwoDimensional(adjMatrix);
		System.out.println("Number Of Edges: " + maze.getNumberOfEdges() + "\n");
		
		LinkedQueue bfs, dfs = new LinkedQueue();
		LinkedStack shortestPath = new LinkedStack(), cheapestPath = new LinkedStack();
		
		double costOfShortest, costOfCheapest = 0;
		
		System.out.println("Breadth First Search Travaversal:");
		bfs = maze.getBreadthFirstTraversal("0-1",String.valueOf(mazeArray.length-2)+"-"+String.valueOf(mazeArray[0].length()-1 ));
		mazeCleaner(mazeArray);
		displayTraversal(bfs, null, mazeArray);
		
		System.out.println("\nDepth First Search Travaversal:");
		dfs = maze.getDepthFirstTraversal("0-1", String.valueOf(mazeArray.length-2)+"-"+String.valueOf(mazeArray[0].length()-1 ));
		mazeCleaner(mazeArray);
		displayTraversal(dfs, null, mazeArray);
		
		System.out.println("\nShortest Path:");
		costOfShortest = maze.getShortestPath("0-1", String.valueOf(mazeArray.length-2)+"-"+String.valueOf(mazeArray[0].length()-1 ), shortestPath);
		mazeCleaner(mazeArray);
		displayTraversal(null, shortestPath, mazeArray);
		if(costOfShortest == 0)
			System.out.println("There is no way to wanted location");
		//System.out.println("Cost Of Shortes Path: " + costOfShortes);
		
		System.out.println("\nCheapest Path:");
		costOfCheapest = maze.getCheapestPath("0-1", String.valueOf(mazeArray.length-2)+"-"+String.valueOf(mazeArray[0].length()-1 ), cheapestPath);
		mazeCleaner(mazeArray);
		displayTraversal(null, cheapestPath, mazeArray);
		if(costOfCheapest == 0)
			System.out.println("There is no way to wanted location");
		else
			System.out.println("Cost Of Cheapest Path: " + costOfCheapest);
		
	}
	
	//Reaads the file and returns it as a String
	public static String[] fileReader(String fileName) throws IOException  {
		String content = "";
		String[] maze;
		Scanner sc = null;
		try {
			sc = new Scanner(new File(fileName));
		} catch (FileNotFoundException e) {
			System.out.println("There is no file such named '" + fileName +"' in directory eclipse-workspace");
			System.exit(0);
		}
		  while(sc.hasNextLine())//Converting 'txt' file to a String array
			  content += sc.nextLine() + "\n";

		  maze = content.split("\n");//Maze as an array
		return maze;
	}//end of fileReader
	
	//Method for displaying two dimensional String arrays
	public static void displayTwoDimensional(String[][] matrix) {
		System.out.println();
		for(int i = 0; i<matrix.length; i++) {
			for(int j =0; j<matrix[i].length; j++) {
				if(matrix[i][j] == null) {
					System.out.printf("%-3s", " 0");
					System.out.print(" ");
				}
				else	
					System.out.print(matrix[i][j] + " ");
			}
			System.out.println("\n");
		}
	}//end of displayTwoDimensional
	
	//Creates graph with vertices and edges
	public static UndirectedGraph<String> graphMaker(String[] mazeToGraph) {
		DirectedGraph<String> maze = new UndirectedGraph<String>();
		for(int i = 0; i<mazeToGraph.length; i++) {//Looks every char at maze array and adds spaces to the graph
			for(int j = 0; j  < mazeToGraph[i].length(); j++) {
				if(mazeToGraph[i].charAt(j) == ' ') {
					maze.addVertex(String.valueOf(i) + "-" + String.valueOf(j));
				}
			}
		}
		maze.setEdges();//Sets edges
		return (UndirectedGraph<String>) maze;
	}//end of graphMaker
	
	//Determines wheter a space in txt is a vertex or a part of a road
	/*public static boolean isVertex(String[] mazeToGraph, int i, int j) {
		if(i == 0 || j == mazeToGraph[i].length()-1 )//Start index or end index
			return true;
		else if((mazeToGraph[i].charAt(j+1) == ' ' || mazeToGraph[i].charAt(j-1) == ' ')//If it is a junction
				&& (mazeToGraph[i+1].charAt(j) == ' ' || mazeToGraph[i-1].charAt(j) == ' '))
			return true;
		//If it is an end of a road
		else if(mazeToGraph[i].charAt(j+1) == ' ' && mazeToGraph[i].charAt(j-1) == '#' 
				&& mazeToGraph[i+1].charAt(j) == '#' && mazeToGraph[i-1].charAt(j) == '#')
			return true;
		else if(mazeToGraph[i].charAt(j+1) == '#' && mazeToGraph[i].charAt(j-1) == ' ' 
				&& mazeToGraph[i+1].charAt(j) == '#' && mazeToGraph[i-1].charAt(j) == '#')
			return true;
		else if(mazeToGraph[i].charAt(j+1) == '#' && mazeToGraph[i].charAt(j-1) == '#' 
				&& mazeToGraph[i+1].charAt(j) == ' ' && mazeToGraph[i-1].charAt(j) == '#')
			return true;
		else if(mazeToGraph[i].charAt(j+1) == '#' && mazeToGraph[i].charAt(j-1) == '#' 
				&& mazeToGraph[i+1].charAt(j) == '#' && mazeToGraph[i-1].charAt(j) == ' ')
			return true;
		else if(mazeToGraph[i].charAt(j+1) == '#' && mazeToGraph[i].charAt(j-1) == '#' //Surrounded with walls
				&& mazeToGraph[i+1].charAt(j) == '#' && mazeToGraph[i-1].charAt(j) == '#')
			return true;
		//returns false if it is on a lineer way
		return false;
	}*/

	//To connect vertical vertices if vertices created with isVertex function
	/*public static String verticalEdger(LinkedQueue<String> vertexList, int i, int j) {

		String firstEntry = vertexList.getFront();
		vertexList.enqueue(vertexList.dequeue());
		int entryCount = 1;
		while(!vertexList.getFront().equals(firstEntry)) {
			vertexList.enqueue(vertexList.dequeue());
			entryCount++;
		}
		for(int k = 0; k<entryCount; k++) {
			firstEntry = vertexList.dequeue();
			String[] positions = firstEntry.split("-");
			int checkVertical = Integer.valueOf(positions[1]);
			if(!firstEntry.equals(String.valueOf(i) + "-" + String.valueOf(j)) && checkVertical - j ==0)
				return firstEntry;
			else
				vertexList.enqueue(firstEntry);
		}
		return "";
			
		
			
	}*/

	//Cleans maze from drawed roads
	public static void mazeCleaner(String[] mazeArray) {
		for(int row = 0; row<mazeArray.length; row++) {//Looks every char at maze array and changes '.' to spaces
			for(int column = 0; column<mazeArray[row].length(); column++) {
				StringBuilder newString = new StringBuilder(mazeArray[row]);
				if(newString.charAt(column) == '.') {
					newString.setCharAt(column, ' ');
					mazeArray[row] = newString.toString();
				}
			}
		}
	}//end of mazeCleaner

	//Adds road to maze then displays it
	public static   void displayTraversal(LinkedQueue queuePath, LinkedStack stackPath, String[] mazeArray) {
		int count = 0;
		//If it is called for shortest or chepast path
		while(stackPath != null && !stackPath.isEmpty()) {//Looks every element of stack
			String vertexName = (String) ((VertexInterface) stackPath.pop()).getLabel();
			count ++;
			int indexOf = vertexName.indexOf("-");////For determine row and column names of vertex
			int row = Integer.valueOf(vertexName.substring(0, indexOf));//For determine row name of vertex
			int column = Integer.valueOf(vertexName.substring(indexOf+1, vertexName.length()));//For determine column name of vertex
			StringBuilder newString = new StringBuilder(mazeArray[row]);//Draws road
			newString.setCharAt(column, '.');	
			mazeArray[row] = newString.toString();
		}
		//If it is called for bfs or dfs
		while(queuePath != null && !queuePath.isEmpty()) {
			String vertexName = (String) ((VertexInterface) queuePath.dequeue()).getLabel();
			count++;
			int indexOf = vertexName.indexOf("-");////For determine row and column names of vertex
			int row = Integer.valueOf(vertexName.substring(0, indexOf));///For determine row name of vertex
			int column = Integer.valueOf(vertexName.substring(indexOf+1, vertexName.length()));//For determine column name of vertex
			StringBuilder newString = new StringBuilder(mazeArray[row]);//Draws road
			newString.setCharAt(column, '.');	
			mazeArray[row] = newString.toString();
		}
		//Prints maze array
		for(int i = 0; i < mazeArray.length; i++)
			System.out.println(mazeArray[i]); 
		System.out.println("Number Of Visited Vertices: " + count);
	}//end of displayTraversal
}
