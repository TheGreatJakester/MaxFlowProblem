import java.util.*;


public class Node {
	private HashMap<Node,int[]> childNodes;
	private String name;
	
	public static void main(String[] args) {
		Node testNode1 = new Node();
		Node testNode2 = new Node();
		testNode1.addNode(testNode2, 5);
		if(testNode1.isCritical(testNode2)) {
			System.out.println("Critical!");
		}
		else {
			System.out.println("not critical");
		}
		testNode1.use(testNode2, 5);
		if(testNode1.isCritical(testNode2)) {
			System.out.println("Critical!");
		}
		else {
			System.out.println("not critical");
		}
	}
	
	public Node() {
		name = "NULL";
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
			if(values[0] >= values[1] + flow) {
				values[1] += flow;
			}
			else {
				System.out.println("OVER FLOW!");
			}
		}
		else {
			System.out.println("no such child found");
		}
		
	}
	
	public boolean isCritical(Node nextNode) {
		boolean critical=false;
		if(remainingCapacity(nextNode) == 0) {
			critical = true;
		}
		return critical;
	}

	public void name(String name) {
		this.name = name;
	}
}
