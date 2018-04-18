import java.util.*;


public class Node {
	HashMap<Node,Integer> childNodes;
	public Node() {
		childNodes = new HashMap<Node,Integer>();
	}
	
	public Node(HashMap<Node,Integer> childNodes) {
		this.childNodes = childNodes;
	}
	
	public void addNode(Node newNode,int weight) {
		this.childNodes.put(newNode,weight);
	}
	
	public HashMap<Node,Integer> getNodes(){
		return this.childNodes;
	}
}
