import java.util.*;


public class Graph {
	private Node startNode;
	private Node sinkNode;
	private Node[] allNodes;
	
	public static void main(String[] args) {
		Graph testGraph = new Graph(new int[][]
			{
				{0,0,0,0,0,0},
				{1,0,0,0,0,0},
				{2,0,0,0,0,0},
				{0,3,0,0,0,0},
				{0,4,5,0,0,0},
				{0,0,0,6,7,0}
			});
		testGraph.printGraphMatrix();
		
		
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
				int weight = graphMatrix[i][j];
				if(weight != 0) {
					nodes[i].addNode(nodes[j], weight);
				}
			}
		}
		this.allNodes = nodes;
		
	}
	
	private boolean isValidGraphMatrix(int[][] graphMatrix) {
		boolean good = true;
		int width = graphMatrix.length;
		int height = graphMatrix[0].length;
		if(width != height) {
			good = false;
		}
		return good;
	}
	
	private void printGraphMatrix() {
		int[][] graphMatrix = new int[this.allNodes.length][this.allNodes.length];
		
		for(int i=0;i<this.allNodes.length;i++) {
			Node curNode = this.allNodes[i];
			for(int j=0;j<this.allNodes.length;j++) {
				if(curNode.getNodes().containsKey(this.allNodes[j])) {
					graphMatrix[i][j] = curNode.getNodes().get(this.allNodes[j]);
				}
			}
		}
		
		for(int i=0;i<graphMatrix.length;i++) {
			for(int j =0;j<graphMatrix[0].length;j++) {
				System.out.print(graphMatrix[i][j]);
			}
			System.out.println();
		}
	}
	/*
	public void printGraphMatrix() {
		TreeSet<Node> nodes = new TreeSet<Node>();
		nodes.add(this.startNode);
		Stack<Node> toVisit = new Stack<Node>();
		toVisit.push(this.startNode);
		//add all the nodes to treeset usinig breadth first search
		while(!toVisit.isEmpty()) {
			Node curNode = toVisit.pop();
			for(Iterator<Node> it = curNode.getNodes().keySet().iterator();it.hasNext();) {
				Node curChild = it.next();
				if(
					!nodes.contains(curChild) && 
					!toVisit.contains(curChild) &&
					curChild != this.sinkNode
				) {
					toVisit.push(curChild);
				}
				nodes.add(curChild);
			}
		}
		nodes.add(this.sinkNode);
		//now print all the weights of each node
		for (Node curNode: nodes){
			
		}
		
	}
	*/
}
