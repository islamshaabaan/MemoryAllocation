package sample;

import java.util.Comparator;

enum N
{
    OldProcess,Hole,Segment,EmptyBlock;
}

public class Node implements Comparable<Node> {
    private int     StartingAdd;
    private int     size;
    private N NodeState;
    private String NodeName;
    private Node    next;
    private String processName="";
    private String color;
    private int     FinishingAdd;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public int getStartingAdd() {
        return StartingAdd;
    }
    public void setStartingAdd(int stadd) {
        this.StartingAdd= stadd;
    }
    public int getSize() {
        return size;
    }
    public void setSize(int sz) {
        this.size= sz;
    }
    public N getNodeState() {
        return NodeState;
    }
    public void setNodeState(N nodest){
        this.NodeState=nodest;
    }
    public String getNodeName() {
        return NodeName;
    }
    public void setNodeName(String nodename) {
        this.NodeName= nodename;
    }
    public Node getNext(){
        return next;
    }
    public void setNext(Node nxt){
        this.next=nxt;
    }
    public int getFinishingAdd() {
        return FinishingAdd;
    }
    public void setFinishingAdd(int stadd,int sz) {
        this.FinishingAdd= stadd + sz -1;
    }
    // Constructor for generating a hole or old process in memory
    Node (int stadd, int sz,N nodeState , Node nxt ) {
        StartingAdd     = stadd;
        size         = sz;
        NodeState      = nodeState;
        next         = nxt;
        FinishingAdd = stadd+sz-1;
        NodeName = "Hole1";
        processName="ProcHole1";

    }
    // Constructor for generating a segment in memory
    Node (int stadd, int sz, N nodeState,String name,Node nxt,String ProcessN) {
        StartingAdd     = stadd;
        size         = sz;
        NodeState      = nodeState;
        next         = nxt;
        FinishingAdd = stadd+sz-1;
        NodeName = name;
        processName=ProcessN;
    }

    public int compareTo(Node compareNode) {
        int compareQuantity = ((Node) compareNode).getSize();
        //ascending order
        return this.getSize() - compareQuantity;
    }
    public static Comparator<Node> NodeStartAddComparator = new Comparator<Node>() {
        public int compare(Node node1, Node node2) {
            return node1.getStartingAdd() - node2.getStartingAdd();
        }
    };
    public static Comparator<Node> DesSize = new Comparator<Node>() {
        public int compare(Node node1,Node node2) {
            return node2.getSize()-node1.getSize();
        }
    };
} // End Node class

