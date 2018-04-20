import java.util.*;


public class Graph {
	private Node startNode;
	private Node sinkNode;
	private Node[] allNodes;
	
	public static void main(String[] args) {
		int[][] testMatrix1 = new int[][]
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
					{ 0, 0, 0, 0, 0, 0, 0, 0}, //A
					{ 5, 0, 0, 0, 0, 0, 0, 0}, //B
					{ 7, 0, 0, 0, 0, 0, 0, 0}, //C
					{20, 0, 0, 0, 0, 0, 0, 0}, //D
					{ 0, 14, 6, 0, 0, 0, 0, 0}, //E
					{ 0, 0,10, 0, 0, 0, 0, 0}, //F
					{ 0, 0, 0,13, 0,15, 0, 0}, //G
					{ 0, 0, 0, 0,11, 8,12, 0}, //H
					
				};
		
		Graph testGraph = new Graph(testMatrix2);
		testGraph.printGraphMatrix();
		System.out.println();
		
		
		ArrayList<Node> path = testGraph.shortestPathWithoutCritical();
		for(int i = 0;i<testGraph.allNodes.length;i++) {
			if(path.contains(testGraph.allNodes[i])) {
				System.out.println(i);
			}
		}
		
		

		testGraph.flow();
		testGraph.printFlow(); 
		
		System.out.println("Finished");
	}
	
	public Graph(int[][] graphMatrix) {
		if(!isValidGraphMatrix(graphMatrix)) {
			System.err.println("This matrix isn't square");
			return;
		}
		parseMatrix(graphMatrix);
	}
	
	private void parseMatrix(int[][] graphMatrix) {
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
	


	public void flow() {
		ArrayList<Node> curPath = shortestPathWithoutCritical();
		int min;
		while(curPath != null) {
			min = minAbility(curPath);
			augmentPath(curPath,min);
			curPath = shortestPathWithoutCritical();
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
	

	
	private ArrayList<Node> shortestPathWithoutCritical(){
		class Searchable{
			public Node node;
			public ArrayList<Node> path;
			public Searchable(Node node,ArrayList<Node> path) {
				this.node = node;
				this.path = path;
			}
			public ArrayList<Searchable> getNext(){
				ArrayList<Searchable> nexts = new ArrayList<Searchable>();
				for(Node n:node.getNodes().keySet()) {
					if(!node.isCritical(n)) {
						ArrayList<Node> newPath = (ArrayList<Node>) path.clone();
						newPath.add(node);
						nexts.add(new Searchable(n,newPath));
					}
				}
				return nexts;
			}
			
			@Override
			public boolean equals(Object s) {
				
				Searchable casted; 
				if(s.getClass() == Searchable.class) {
					casted = (Searchable) s;
				}
				else {
					return false;
				}
				if(casted.node == this.node) {
					return true;
				}
				else {
					return false;
				}				
			}
			
		}
	
		LinkedList<Searchable> searchList = new LinkedList<Searchable>();
		searchList.push(new Searchable(this.startNode,new ArrayList<Node>()));
		ArrayList<Searchable> visited = new ArrayList<Searchable>();
		
		while(!searchList.isEmpty()) {
			Searchable curSearch = searchList.poll();
			if(visited.contains(curSearch)) {
				continue;
			}
			for(Searchable s:curSearch.getNext()) {
				if(s.node == this.sinkNode) {
					//found the shortest
					ArrayList<Node> out =(ArrayList<Node>) s.path.clone();
					out.add(this.sinkNode);
					return out;
				}
				// keep looking for the shortest
				else if(searchList.contains(s)) {
					searchList.remove(s);
					searchList.push(s);
				}
				else {
					searchList.push(s);
				}
			}
			visited.add(curSearch);
		}
		// no path was found
		return null;
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
				System.out.print(graphMatrix[j][i]+"\t");
			}
			System.out.println();
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
				System.out.print(graphMatrix[j][i]+"\t");
			}
			System.out.println();
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
}
