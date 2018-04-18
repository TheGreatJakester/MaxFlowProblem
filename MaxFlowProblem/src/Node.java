import java.util.*;


public class Node {
	HashMap<Node,int[]> childNodes;
	public Node() {
		childNodes = new HashMap<Node,int[]>();
	}
	
	public Node(HashMap<Node,int[]> childNodes) {
		this.childNodes = childNodes;
	}
	
	public void addNode(Node newNode,int weight) {
		this.childNodes.put(newNode,new int[] {weight,0});
	}
	
	public HashMap<Node,int[]> getNodes(){
		return this.childNodes;
	}
	
	public int capacity(Node nextNode) {
		if(childNodes.containsKey(nextNode)) {
			return childNodes.get(nextNode)[0];
		}
		else {
			return 0;
		}
	}
	
	public int remainingCapacity(Node nextNode) {
		if(childNodes.containsKey(nextNode)) {
			return childNodes.get(nextNode)[0] - childNodes.get(nextNode)[1];
		}
		else {
			return 0;
		}
	}
	
	public void use(Node nextNode, int flow) {
		if(childNodes.containsKey(nextNode)) {
			int[] values = childNodes.get(nextNode);
			if(values[0] <= values[1] + flow) {
				values[1] += flow;
			}
			else {
				System.err.println("OVER FLOW!");
			}
		}
		else {
			System.err.println("no such child found");
		}
		
	}
}
