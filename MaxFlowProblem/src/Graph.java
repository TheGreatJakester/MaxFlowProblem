import java.util.*;


public class Graph {
	private Node startNode;
	private Node sinkNode;
	private Node[] allNodes;
	
	public static void main(String[] args) {
		int[][] testMatirx1 = new int[][]
			{
				{0,0,0,0,0,0},
				{1,0,0,0,0,0},
				{2,0,0,0,0,0},
				{0,3,0,0,0,0},
				{0,4,5,0,0,0},
				{0,0,0,6,7,0}
			};
		int[][] testMatrix2 = new int[][]
				{	//A, B, C, D, E, F, G, H 
					{ 0,14, 0, 0, 0, 0, 0, 0}, //A
					{ 5, 0, 0, 0, 0, 0, 0, 0}, //B
					{ 7, 0, 0, 0, 0, 0, 0, 0}, //C
					{20, 0, 0, 0, 0, 0, 0, 0}, //D
					{ 0, 0, 6, 0, 0, 0, 0, 0}, //E
					{ 0, 0,10, 0, 0, 0, 0, 0}, //F
					{ 0, 0, 0,13, 0,15, 0, 0}, //G
					{ 0, 0, 0, 0,11, 8,12, 0}, //H
					
				};
		
		Graph testGraph = new Graph(testMatrix2);
		testGraph.printGraphMatrix();
		
		ArrayList<Node> path = testGraph.shortestPathWithoutCritical(testGraph.startNode, new ArrayList<Node>());
		for(int i = 0;i<testGraph.allNodes.length;i++) {
			if(path.contains(testGraph.allNodes[i])) {
				System.out.println(i);
			}
		}
		

		testGraph.flow();
		testGraph.printFlow();
	}
	
	public Graph(int[][] graphMatrix) {
		parseMatrix(graphMatrix);
	}
	
	private void parseMatrix(int[][] graphMatrix) {
		if(!isValidGraphMatrix(graphMatrix)) {
			System.err.println("This matrix isn't square");
			return;
		}
		//initialize all the nodes
		Node[] nodes = new Node[graphMatrix.length];
		for(int i=0;i<nodes.length;i++) {
			nodes[i] = new Node();
		}
		//set the start node and the end node
		this.startNode = nodes[0];
		this.sinkNode = nodes[nodes.length-1];
		
		//set up the rest of the nodes that connect
		for(int i=0;i<graphMatrix.length;i++) {
			for(int j=0;j<graphMatrix[0].length;j++) {
				int weight = graphMatrix[j][i];
				if(weight != 0) {
					nodes[i].addNode(nodes[j], weight);
				}
			}
		}
		this.allNodes = nodes;
	}
	
	public static boolean isValidGraphMatrix(int[][] graphMatrix) {
		boolean good = true;
		int width = graphMatrix.length;
		int height = graphMatrix[0].length;
		if(width != height) {
			good = false;
		}
		return good;
	}
	
	public void printGraphMatrix() {
		int[][] graphMatrix = new int[this.allNodes.length][this.allNodes.length];
		
		for(int i=0;i<this.allNodes.length;i++) {
			Node curNode = this.allNodes[i];
			for(int j=0;j<this.allNodes.length;j++) {
				if(curNode.getNodes().containsKey(this.allNodes[j])) {
					graphMatrix[i][j] = curNode.getNodes().get(this.allNodes[j])[0];
				}
			}
		}
		
		for(int i=0;i<graphMatrix.length;i++) {
			for(int j =0;j<graphMatrix[0].length;j++) {
				System.out.print(graphMatrix[j][i]);
			}
			System.out.println();
		}
	}

	public void flow() {
		ArrayList<Node> curPath = shortestPathWithoutCritical(this.startNode, new ArrayList<Node>());
		int min;
		while(curPath != null) {
			min = minAbility(curPath);
			augmentPath(curPath,min);
			curPath = shortestPathWithoutCritical(this.startNode, new ArrayList<Node>());
		}
	}
	
	public void printFlow() {
		int[][] graphMatrix = new int[this.allNodes.length][this.allNodes.length];
		
		for(int i=0;i<this.allNodes.length;i++) {
			Node curNode = this.allNodes[i];
			for(int j=0;j<this.allNodes.length;j++) {
				if(curNode.getNodes().containsKey(this.allNodes[j])) {
					graphMatrix[i][j] = curNode.getNodes().get(this.allNodes[j])[1];
				}
			}
		}
		
		for(int i=0;i<graphMatrix.length;i++) {
			for(int j =0;j<graphMatrix[0].length;j++) {
				System.out.print(graphMatrix[j][i]);
			}
			System.out.println();
		}
	}
	
	private int minAbility(ArrayList<Node> shortestPath) {
		int min = 1000;
		for(int i=0;i<shortestPath.size()-1;i++) {
			int curEdge = shortestPath.get(i).remainingCapacity(shortestPath.get(i+1));
			if( min > curEdge) {
				min = curEdge;
			}
		}
		return min;
	}
	
	private void augmentPath(ArrayList<Node> shortestPath, int flow) {
		for(int i=0;i<shortestPath.size()-1;i++) {
			Node sourceNode = shortestPath.get(i);
			Node nextNode = shortestPath.get(i+1);
			System.out.println(nn(sourceNode) + "->" + nn(nextNode) + " : "+ flow + "/" + sourceNode.remainingCapacity(nextNode));
			shortestPath.get(i).use(shortestPath.get(i+1), flow);
		}
	}
	
	private String nn(Node n) {
		for(int i=0;i<this.allNodes.length;i++) {
			if(n == this.allNodes[i]) {
				return Integer.toString(i);
			}
		}
		return null;
	}
	
	private void printPath(ArrayList<Node> path) {
		for(int i = 0; i < this.allNodes.length;i++) {
			if(path.contains(this.allNodes[i])) {
				System.out.print(i);
			}
		}
		System.out.println();
	}
	
	private ArrayList<Node> shortestPathWithoutCritical(Node currentNode, ArrayList<Node> pathToNow){
		ArrayList<Node> pathToNowCopy = new ArrayList<Node>(pathToNow);
		pathToNowCopy.add(currentNode);
		ArrayList<Node> shortest = null;
		for(Iterator<Node> it=currentNode.getNodes().keySet().iterator();it.hasNext();) {
			Node curChild = it.next();
			if(!currentNode.isCritical(curChild)) {
				if(curChild == this.sinkNode) {
					pathToNowCopy.add(curChild);
					return pathToNowCopy;
				}
				if(shortest == null) {
					shortest = shortestPathWithoutCritical(curChild,pathToNowCopy);
				}
				else if(shortestPathWithoutCritical(curChild,pathToNowCopy).size() < shortest.size()) {
					shortest = shortestPathWithoutCritical(curChild,pathToNowCopy);
				}
			}
		}
		return shortest;
	}
	
}
